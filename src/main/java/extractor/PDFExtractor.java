package extractor;

import org.apache.fontbox.util.BoundingBox;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType3Font;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PDFExtractor {
    private final String pdfPath = "src\\main\\resources\\08662658.pdf";
    private float min= Float.MAX_VALUE;
    private PDDocument document;
    private PDFTextStripper pdfTextStripper;
    private HashMap<Rectangle2D,Integer> m = new HashMap<>();
    public void start() throws IOException {

        loadPDF(pdfPath);
        analyze();
        printCompleteDoc();
        drawRectangle();
        startPDF();

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
    private void analyze() {
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

                            m.put(bounds, getCurrentPageNo());

                        if(builder.length()==0){
                            builder.append("[[[FontSize:"+t.getFontSizeInPt()+" || "+t.getPageWidth()+ "]]]  ");
                        }
                        if(!t.equals(-1)){
                            builder.append(t);
                        }

                    }
                    String newText = builder.toString();
                    super.writeString(newText, textPositions);
                }
            };
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void loadPDF(String pdfPath) {
        String absolutePath;
        try {
            absolutePath = Paths.get(pdfPath).toAbsolutePath().toString();
            document = PDDocument.load(new File(absolutePath));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void printCompleteDoc(){
        try {
            if (!document.isEncrypted()) {
                for (String line : pdfTextStripper.getText(document).split(pdfTextStripper.getParagraphStart())) {

                    System.out.println(line);
                    System.out.println("********************************************************************");
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void drawRectangle(){
        PDPage page;
        PDPageContentStream contentStream=null;
        try {
            for(Map.Entry<Rectangle2D, Integer> map : this.m.entrySet()) {
                page = this.document.getPage(map.getValue()-1);
                contentStream = new PDPageContentStream(this.document,page,true,true);

                contentStream.addRect((float)map.getKey().getBounds2D().getX(),(float)map.getKey().getBounds2D().getY(), (float)map.getKey().getWidth(),(float)map.getKey().getHeight());
                contentStream.setStrokingColor(Color.RED);
                contentStream.stroke();
                contentStream.close();
            }
            try {
                File file1 = new File("src\\main\\resources\\colored08662658.pdf");
                document.save(file1);
                document.close();
            }catch (FileNotFoundException f){
                Runtime.getRuntime().exec("taskkill /F /IM AcroRd32.exe");
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                File file1 = new File("src\\main\\resources\\colored08662658.pdf");
                document.save(file1);
                document.close();
            }
        } catch (IOException e) {
            e.printStackTrace();

        }

    }

}
