package extractor;

import exception.DrawException;
import org.apache.fontbox.util.BoundingBox;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import org.apache.pdfbox.text.TextPosition;
import utilities.Helper;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static layout.Character.charactersBoxCoordinatesMap;
import static layout.Word.wordsAndOccurencesMap;

//PLAYERGROUND!   PLAYGROUND!  PLAYGROUND!
public class PLAYGROUND {
    private final String pdfPath = "src\\main\\resources\\08662658.pdf";
    private PDDocument pdfDocument;
    private String pdfDocumentText;
    private PDFTextStripper pdfTextStripper;

    public void start() throws IOException {
        // if (charactersBoxCoordinatesMap == null)
        //   throw new DrawException("Map is NULL!");

        // for (Map.Entry<Rectangle2D, Integer> map : charactersBoxCoordinatesMap.entrySet()) {

        //  }
       // loadPDFDocument(pdfPath);
        // analyzePDFDocument();
      //   Helper.csvWriter("G:/Users/Progamer/Desktop/addressesWrite.csv", wordsAndOccurencesMap);
        //  printCompleteDoc();
        //drawCharactersBoundingBox();
        //  startPDF();
        linksRechtsSectionBoundingArea();
    }
    //Überlegung:  Es existieren wörter, die in der Mitte kein Sinn ergeben. z.b But:  Bu    t
    //ERste Vorüberlegung wäre: wenn die anzahl an brüchigen Wörtern ab einem bestimmten Faktor hoch sind => zwei abschnitte vorhanden.
    //Ansonsten handelt es sich um Fließtext.
    //Ich könnte aber auch teorethisch untersuchen ob es silben trennungen gibt wie :   pedestri-
    //keine ahnung. Muss ich noch mal schauen...
    private void linksRechtsSectionBoundingArea() {
       /* float width = this.pdfDocument.getPage(0).getMediaBox().getWidth();
        float height= this.pdfDocument.getPage(0).getMediaBox().getHeight();
        Rectangle2D region = new Rectangle2D.Double(100,100, 100,100);
        String regionName = "region";
        PDFTextStripperByArea stripper = null;
        try {
            stripper = new PDFTextStripperByArea();
            stripper.setSortByPosition(false);
            stripper.addRegion(regionName, region);
            stripper.extractRegions(this.pdfDocument.getPage(0));
            this.pdfDocumentText = stripper.getText(this.pdfDocument);
            System.out.println(stripper.getTextForRegion(regionName));

        } catch (IOException e) {
            e.printStackTrace();
        }
        */
        try {
            float width = this.pdfDocument.getPage(0).getMediaBox().getWidth();
            float height= this.pdfDocument.getPage(0).getMediaBox().getHeight();
            StringBuilder pdfText = new StringBuilder();
            PDFTextStripperByArea stripper = new PDFTextStripperByArea();

            stripper.setSortByPosition(true);

            Rectangle2D rectLeft = new Rectangle2D.Float(0, 0, width/2,height);

            Rectangle2D rectRight = new Rectangle2D.Float(width/2,0, width/2, height);

            stripper.addRegion("leftColumn", rectLeft);

            stripper.addRegion("rightColumn", rectRight);

            PDPageTree allPages = this.pdfDocument.getDocumentCatalog().getPages();
            int pageNumber = this.pdfDocument.getNumberOfPages();


            String leftText = "";
            String rightText = "";

            for (int i = 0; i < pageNumber; i++) {

                PDPage page = allPages.get(i);

                stripper.extractRegions(page);
                leftText = stripper.getTextForRegion("leftColumn");
                rightText = stripper.getTextForRegion("rightColumn");

                pdfText.append(leftText);
                pdfText.append(rightText);
            }
            System.out.println(pdfText.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    //Eventuell abschnitte rausfiltern, anzahl buchstaben etc filtern.. usw. Mit zusammenarbeit von Data.java
    //hier werden zudem charactersBoxCoordinatesMap gefüllt um es später mit der Methode drawCharactersBoundingBox zeichnen zu können.
    private void analyzePDFDocument() {
        try {
            pdfTextStripper = new PDFTextStripper() {
                @Override
                protected void writeString(String text, List<TextPosition> textPositions) throws IOException {
                    pdfTextStripper.setParagraphStart("\t");
                    StringBuilder builder = new StringBuilder();
                    //Wird zum erstellen eines csv Files verwendet.
                    //\w matched alle char's die kein word sind. any non-word character.
                    wordsAndOccurencesMap.merge(text.replaceAll("\\W", "").toLowerCase(Locale.ENGLISH), 1, Integer::sum);
                    for (TextPosition t : textPositions) {
                        //Zum Umranden der einzelnen Character.
                        PDFont font = t.getFont();
                        BoundingBox boundingBox = font.getBoundingBox();
                        Rectangle2D.Float rect = new Rectangle2D.Float(boundingBox.getLowerLeftX(), boundingBox.getUpperRightY(), boundingBox.getWidth(), boundingBox.getHeight());
                        AffineTransform affineTransform = t.getTextMatrix().createAffineTransform();
                        affineTransform.scale(1 / 1000f, 1 / 1000f);
                        Shape shape = affineTransform.createTransformedShape(rect);
                        // Invertierung der Y - Achse.  Notwendig, da java in 2D User Space arbeitet. Und nicht in PDF User Space.
                        Rectangle2D bounds = shape.getBounds2D();
                        bounds.setRect(bounds.getX(), bounds.getY() - bounds.getHeight(), bounds.getWidth(), bounds.getHeight());

                        //Wird zum zeichnen verwendet.
                        charactersBoxCoordinatesMap.put(bounds, getCurrentPageNo());


                        if (builder.length() == 0) {
                            // builder.append("[[[FontSize:"+t.getFontSizeInPt()+" || "+t.getPageWidth()+ "]]]  ");
                        }
                        if (!t.equals(-1)) {
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

}
