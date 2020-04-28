package layout;

import extractor.Document;
import extractor.DocumentHandler;
import figure.Rectangle;
import interfaces.Analyzable;
import interfaces.Drawable;
import interfaces.Extractable;
import io.Export;
import org.apache.fontbox.util.BoundingBox;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSStream;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
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
import java.io.PrintWriter;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static layout.Character.charactersBoxCoordinatesMap;

public class Title extends Analyzable {
    private DocumentHandler documentHandler;
    private List<Float> fontSizeList;
    private PDPage firstPage;
    private float width, height, highestSize;
    private Rectangle2D top;

    public Title(DocumentHandler handler) {
        this.documentHandler = handler;
        fontSizeList = new ArrayList<>();
    }

    //@TODO schleife nötig, der in Document handler, alle PDFs durchgreift und analysiert.
    @Override
    protected void analyze() {

        //iteriert über alle PDF die vorher importiert wurden.
        for (Document document : documentHandler.getDocumentsList()) {
            firstPage = document.getPdfDocument().getPage(0);
            width = firstPage.getMediaBox().getWidth();
            height = firstPage.getMediaBox().getHeight();
            top = new Rectangle2D.Float(0, 0, width, height / 4.5f);
            createFontSizeList();
            highestSize = getHighestFontSize(this.fontSizeList);
             Helper.delimiter();
             System.out.print(boundingArea(document));
             Helper.delimiter();

        }

    }



    //@TODO muss noch irgendwie gemacht werden.
    private String boundingArea(Document document) {
        try {
            //Erst wird der fontSizecheck gemacht. Im anschluss dann der Bound gesetzt.
            PDFTextStripperByArea stripper = new PDFTextStripperByArea() {
                @Override
                protected void writeString(String text, List<TextPosition> textPositions) throws IOException {
                    StringBuilder builder = new StringBuilder();
                    for(TextPosition p: textPositions) {
                        if (!text.isEmpty() && p.getFontSizeInPt() == highestSize) {
                            builder.append(p);
                        }
                    }
                    super.writeString(builder.toString(), textPositions);
                }

                /*@Override
                protected void processTextPosition(TextPosition text) {

                    if (text.getFontSizeInPt() == highestSize) {

                        super.processTextPosition(text);
                    }
                }*/
            };

            PDPage firstPage = document.getPdfDocument().getPage(0);
            //true -> text ohne beachtung der spalten.
            stripper.setSortByPosition(true);
            stripper.addRegion("top", this.top);

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


    private void createFontSizeList() {
        try {
            PDFTextStripperByArea stripper = new PDFTextStripperByArea() {
                @Override
                public void extractRegions(PDPage firstPage) throws IOException {

                    //true -> text ohne beachtung der spalten.
                    setSortByPosition(true);
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
            stripper.extractRegions(firstPage);
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
