package layout;

import extractor.Document;
import extractor.DocumentHandler;

import interfaces.Analyzable;

import org.apache.pdfbox.pdmodel.*;

import utilities.Helper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Metadata extends Analyzable {
    private DocumentHandler documentHandler;

    public Metadata(DocumentHandler handler) {
        this.documentHandler = handler;
    }

    @Override
    protected void analyze() {

        //iteriert über alle PDF die vorher importiert wurden.
        for (Document document : documentHandler.getDocumentsList()) {

            DateFormat formatter = new SimpleDateFormat("yyyy-MMM-dd");
            PDDocumentInformation info = document.getPdfDocument().getDocumentInformation();
            System.out.println("Page Count=" + document.getPdfDocument().getNumberOfPages());
            System.out.println("Title=" + info.getTitle());
            System.out.println("Author=" + info.getAuthor());
            System.out.println("Subject=" + info.getSubject());
            System.out.println("Keywords=" + info.getKeywords());
            System.out.println("Creator=" + info.getCreator());
            System.out.println("Producer=" + info.getProducer());
            System.out.println("Creation Date=" + formatter.format(info.getCreationDate().getTime()));
            System.out.println("Modification Date=" + formatter.format(info.getModificationDate().getTime()));
            System.out.println("Trapped=" + info.getTrapped());
            Helper.delimiter();
        }

    }

    @Override
    public void start() {
        analyze();
    }
}
    /*

    analyse methode:
     firstPage = document.getPdfDocument().getPage(0);
            width = firstPage.getMediaBox().getWidth();
            height = firstPage.getMediaBox().getHeight();
            top = new Rectangle2D.Float(0, 0, width, height / 4.5f);
            createFontSizeList();
            highestSize = getHighestFontSize(this.fontSizeList);
            Helper.delimiter();
            System.out.print(boundingArea(document));
            Helper.delimiter();

    private String boundingArea(Document document) {
        try {
            //Erst wird der fontSizecheck gemacht. Im anschluss dann der Bound gesetzt.
            PDFTextStripperByArea stripper = new PDFTextStripperByArea() {
                @Override
                public void extractRegions(PDPage page) throws IOException {
                    //true -> text ohne beachtung der spalten.
                    setSortByPosition(true);
                    addRegion("top", top);
                    super.extractRegions(firstPage);
                    getTextForRegion("top");
                }

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

            };
            stripper.extractRegions(firstPage);
            return stripper.getTextForRegion("top").trim();

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
                        if (!text.isEmpty() ) {
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


    */

