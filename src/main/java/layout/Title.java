package layout;

import extractor.Document;
import extractor.DocumentHandler;
import figure.Rectangle;
import interfaces.Analyzable;
import interfaces.Drawable;
import interfaces.Extractable;
import io.Export;
import org.apache.fontbox.util.BoundingBox;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import org.apache.pdfbox.text.TextPosition;
import utilities.Helper;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static layout.Character.charactersBoxCoordinatesMap;

public class Title extends Analyzable {
    private DocumentHandler documentHandler;
    private List<Float> fontSizeList;
    private float highestSize;

    public Title(DocumentHandler handler) {
        this.documentHandler = handler;
        fontSizeList = new ArrayList<>();
    }

    //@TODO schleife nötig, der in Document handler, alle PDFs durchgreift und analysiert.
    @Override
    protected void analyze() {

        //iteriert über alle PDF die vorher importiert wurden.
        for (Document document : documentHandler.getDocumentsList()) {
            createFontSizeList(document);
            highestSize = getHighestFontSize(this.fontSizeList);

            String title = boundingArea(document);
            System.out.println(title);
        }

    }


    //@TODO muss noch irgendwie gemacht werden.
    private String boundingArea(Document document) {
        try {
            //Erst wird der fontSizecheck gemacht. Im anschluss dann der Bound gesetzt.
            PDFTextStripperByArea stripper = new PDFTextStripperByArea() {

                @Override
                protected void processTextPosition(TextPosition text) {

                    if (text.getFontSizeInPt() == highestSize) {
                        super.processTextPosition(text);
                    }
                }
            };

            PDPage firstPage = document.getPdfDocument().getPage(0);
            float width = firstPage.getMediaBox().getWidth();
            float height = firstPage.getMediaBox().getHeight();
            //true -> text ohne beachtung der spalten.
            stripper.setSortByPosition(true);
            Rectangle2D top = new Rectangle2D.Float(0, 0, width, height / 4.5f);
            stripper.addRegion("top", top);

            stripper.extractRegions(firstPage);

            String titleRegion = stripper.getTextForRegion("top");
            StringBuilder title = new StringBuilder();
            title.append(titleRegion);
            return title.toString().trim();

        } catch (IOException i) {
            i.printStackTrace();
        }
        return null;
    }


    private void createFontSizeList(Document document) {
        try {
            PDFTextStripperByArea stripper = new PDFTextStripperByArea() {
                @Override
                public void extractRegions(PDPage firstPage) throws IOException {

                    float width = firstPage.getMediaBox().getWidth();
                    float height = firstPage.getMediaBox().getHeight();
                    //true -> text ohne beachtung der spalten.
                    setSortByPosition(true);
                    Rectangle2D top = new Rectangle2D.Float(0, 0, width, height / 4.5f);
                    addRegion("top", top);
                    super.extractRegions(firstPage);
                    getTextForRegion("top");
                }

                @Override
                protected void writeString(String text, List<TextPosition> textPositions) {

                    for (TextPosition t : textPositions) {
                        //der text.length>1 soll Fälle mit nur 1 Buchstaben ausschließe, die richtig groß sind.
                        if (!text.isEmpty()) {
                            fontSizeList.add(t.getFontSizeInPt());
                        }
                    }
                }
            };
            PDPage firstPage = document.getPdfDocument().getPage(0);
            stripper.extractRegions(firstPage);
            //löscht duplikate. z.B.  6.0 6.0 6.0 ...
            this.fontSizeList = fontSizeList.stream().distinct().collect(Collectors.toList());
            for (int i = 0; i <this.fontSizeList.size() ; i++) {
                System.out.println(this.fontSizeList.get(i));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private float getHighestFontSize(List<Float> textSizes) {
        float minValue = 0;
        for (float f : textSizes) {
            if (f > minValue) {
                minValue = f;
            }
        }
        return minValue;
    }

    @Override
    public void start() {
        analyze();
    }
}
