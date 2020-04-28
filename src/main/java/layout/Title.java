package layout;

import extractor.Document;
import extractor.DocumentHandler;
import interfaces.Analyzable;
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
                    if (text.getFontSizeInPt() == highestSize)
                        super.processTextPosition(text);
                }
            };

            System.out.println("WRITE STRING!!!");
            PDPage firstPage = document.getPdfDocument().getPage(0);
            float width = firstPage.getMediaBox().getWidth();
            float height = firstPage.getMediaBox().getHeight();
            //true -> text ohne beachtung der spalten.
            stripper.setSortByPosition(true);
            Rectangle2D top = new Rectangle2D.Float(0, 0, width, height / 3.5f);
            stripper.addRegion("top", top);

            stripper.extractRegions(firstPage);

            String titleRegion = stripper.getTextForRegion("top");
            StringBuilder title = new StringBuilder();
            title.append(titleRegion);
            return title.toString();
                    /*
                    for (TextPosition position : textPositions) {
                        if(position.getFontSizeInPt() == highestFont){
                            title.append(titleRegion);
                        }
                    }
                    */
        } catch (IOException i) {
            i.printStackTrace();
        }
        return null;
    }


    private void createFontSizeList(Document document) {
        try {
            //speichere alle schriftgrößen ab.
            PDFTextStripper testStripper = new PDFTextStripper() {
                @Override
                protected void writeString(String text, List<TextPosition> textPositions) throws IOException {
                    for (TextPosition t : textPositions)
                        if(!text.isEmpty())
                            fontSizeList.add(t.getFontSizeInPt());
                }
            };

            //notwendiger Aufruf, damit der STripper ausgeführt wird un die textPositionSizes list voll wird.
            testStripper.getText(document.getPdfDocument());
            //löscht duplikate. z.B.  6.0 6.0 6.0 ...
            this.fontSizeList = fontSizeList.stream().distinct().collect(Collectors.toList());

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
