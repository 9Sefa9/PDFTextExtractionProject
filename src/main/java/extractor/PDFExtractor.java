package extractor;

import exception.DrawException;
import org.apache.fontbox.util.BoundingBox;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import static layout.CharacterContainer.charactersBoxCoordinatesMap;

public class PDFExtractor {
    private final String pdfPath = "src\\main\\resources\\08662658.pdf";
    private PDDocument pdfDocument;
    private String pdfDocumentText;
    private PDFTextStripper pdfTextStripper;

    public void start() throws IOException {

        loadPDFDocument(pdfPath);
        analyzePDFDocument();
        printCompleteDoc();
        //drawCharactersBoundingBox();
      //  startPDF();

    }


    private void startPDF() {
        if (Desktop.isDesktopSupported()) {
            try {
                File myFile = new File("src\\main\\resources\\colored08662658.pdf");
                Desktop.getDesktop().open(myFile);
            } catch (IOException ex) {
                // no application registered for PDFs
                try {
                    Runtime.getRuntime().exec("taskkill /F /IM AcroRd32.exe");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                startPDF();
            }
        }
    }

    //Eventuell abschnitte rausfiltern, anzahl buchstaben etc filtern.. usw. Mit zusammenarbeit von Data.java
    private void analyzePDFDocument() {
        try {
            pdfTextStripper = new PDFTextStripper(){
                @Override
                protected void writeString(String text, List<TextPosition> textPositions) throws IOException {
                    pdfTextStripper.setParagraphStart("\t");
                    StringBuilder builder = new StringBuilder();
                    for(TextPosition t : textPositions){

                            PDFont font = t.getFont();
                            BoundingBox boundingBox = font.getBoundingBox();
                            Rectangle2D.Float rect = new Rectangle2D.Float(boundingBox.getLowerLeftX(), boundingBox.getUpperRightY(), boundingBox.getWidth(), boundingBox.getHeight());
                            AffineTransform affineTransform = t.getTextMatrix().createAffineTransform();
                            affineTransform.scale(1 / 1000f, 1 / 1000f);
                            Shape shape = affineTransform.createTransformedShape(rect);
                            // Invertierung der Y - Achse.  Notwendig, da java in 2D User Space arbeitet. Und nicht in PDF User Space.
                            Rectangle2D bounds = shape.getBounds2D();
                            bounds.setRect(bounds.getX(), bounds.getY() - bounds.getHeight(), bounds.getWidth(), bounds.getHeight());

                            charactersBoxCoordinatesMap.put(bounds, getCurrentPageNo());

                        if(builder.length()==0){
                           // builder.append("[[[FontSize:"+t.getFontSizeInPt()+" || "+t.getPageWidth()+ "]]]  ");
                        }
                        if(!t.equals(-1)){
                            builder.append(t);
                        }

                    }
                    String newText = builder.toString();
                    super.writeString(newText, textPositions);
                }
            };

            //Wichtig, damit die initialisierung aufgerufen wird!
            this.pdfDocumentText = pdfTextStripper.getText(this.pdfDocument);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void loadPDFDocument(String pdfPath) {
        String absolutePath;
        try {
            absolutePath = Paths.get(pdfPath).toAbsolutePath().toString();
            this.pdfDocument = PDDocument.load(new File(absolutePath));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void printCompleteDoc(){
        if (!pdfDocument.isEncrypted()) {
            for (String line : this.pdfDocumentText.split(pdfTextStripper.getParagraphStart())) {
                String[] words = line.split(" ");
                String firstWord = words[0].trim();
                String lastWord = words[words.length-1].trim();
                System.out.println("FirstWord:"+firstWord);


                //Diese If Bedingung sorgt dafür, dass z.B "localization\nof" gesplittet wird.
                if(lastWord.contains("\n")){
                   String[] lastWordCorrection = lastWord.split("\n");
                   lastWord = lastWordCorrection[lastWordCorrection.length-1];
                }

                System.out.println("lastWord:"+lastWord);

                System.out.println("********************************************************************");
            }
        }
    }
    private void drawCharactersBoundingBox(){
        PDPage page=null;
        PDPageContentStream contentStream=null;
        try {
            if(charactersBoxCoordinatesMap == null)
                throw new DrawException("Map is NULL!");

            for(Map.Entry<Rectangle2D, Integer> map : charactersBoxCoordinatesMap.entrySet()) {

                page = this.pdfDocument.getPage(map.getValue()-1);
                contentStream = new PDPageContentStream(this.pdfDocument,page,true,true);
                contentStream.addRect((float)map.getKey().getBounds2D().getX(),(float)map.getKey().getBounds2D().getY(), (float)map.getKey().getWidth(),(float)map.getKey().getHeight());
                contentStream.setStrokingColor(Color.RED);
                contentStream.stroke();
                contentStream.close();
            }
            try {
                File file1 = new File("src\\main\\resources\\colored08662658.pdf");
                pdfDocument.save(file1);
                pdfDocument.close();
            }catch (FileNotFoundException f){
                Runtime.getRuntime().exec("taskkill /F /IM AcroRd32.exe");
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                File file1 = new File("src\\main\\resources\\colored08662658.pdf");
                pdfDocument.save(file1);
                pdfDocument.close();
            }
        } catch (IOException | DrawException e) {
            e.printStackTrace();
        }
    }

}
