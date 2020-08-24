package layout;

import extractor.Document;
import extractor.DocumentParser;

import interfaces.Analyzable;

import org.apache.pdfbox.pdmodel.*;

import org.apache.pdfbox.text.PDFTextStripperByArea;
import org.apache.pdfbox.text.TextPosition;

import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Metadata implements Analyzable {
    private DocumentParser documentParser;
    private PDPage firstPage;
    private float width,height,highestSize;
    private Rectangle2D top;
    private List<Float> fontSizeList;
    private List<String> titlesList;
    private List<Integer> pageSizesList;
    private int numberPages;

    public Metadata(DocumentParser handler) {

        this.documentParser = handler;
        titlesList = new ArrayList<>();
        pageSizesList = new ArrayList<>();
    }

    /**
     * Analysiert die Metadaten eines bestimmten Dokuments. Falls Titel nicht existiert, wird mit PDFBox extrahiert
     */
    @Override
    public void analyze() {
        System.out.println("Entering Metadata Extraction on "+Thread.currentThread().getName()+" :: "+Thread.currentThread().getId());
        //iteriert über alle PDF die vorher importiert wurden.
        for (Document document : documentParser.getDocumentsList()) {
          //  DateFormat formatter = new SimpleDateFormat("yyyy-MMM-dd");
            PDDocumentInformation info = document.getPdfDocument().getDocumentInformation();
            if (info.getTitle() == null || info.getTitle().isEmpty()) {
                String fixedTitle = extract(document);
                this.pageSizesList.add(document.getPdfDocument().getNumberOfPages());
                titlesList.add(fixedTitle);

            } else {
                String fixedTitle = info.getTitle();
               this.pageSizesList.add(document.getPdfDocument().getNumberOfPages());
               this.titlesList.add(fixedTitle);
                  //    System.out.println("Page Count=" + document.getPdfDocument().getNumberOfPages() + "\n" + "Title=" + info.getTitle() + "\n" + "Author=" + info.getAuthor() + "\n" + "Subject=" + info.getSubject() + "\n" + "Keywords=" + info.getKeywords() + "\n" + "Creator=" + info.getCreator() + "\n" + "Producer=" + info.getProducer() + "\n" + "Creation Date=" + formatter.format(info.getCreationDate().getTime()) + "\n" + "Modification Date=" + formatter.format(info.getModificationDate().getTime()) + "\n" + "Trapped=" + info.getTrapped());
            }

        }
        System.out.println("Metadata Extraction done on "+Thread.currentThread().getName()+" :: "+Thread.currentThread().getId());
    }

    /**
     * Titel Extraktion  mit PDFBox durch Bounding Area Analyse
     * @see #boundingArea()
     * @param document Das eigentliche Document, welches extrahiert werden soll.
     * @return Titel
     */
    private String extract(Document document) {
        fontSizeList = new ArrayList<Float>();
        firstPage = document.getPdfDocument().getPage(0);
        width = firstPage.getMediaBox().getWidth();
        height = firstPage.getMediaBox().getHeight();
        top = new Rectangle2D.Float(0, 0, width, height / 4.5f);
        //identifiziere alle Schriftgrößen oberhalb der ersten Seite
        createFontSizeList();
        //Suche die größte Schrfitgröße aus der FontSizeList.
        highestSize = getHighestFontSize(fontSizeList);
        //boundArea extrahiert nun den Titel mit FontSizeList.
        return boundingArea();

    }

    /**
     * boundArea analysiert die tatsächliche Überschrift, in abhängigkeit der höchsten Schrift.
     * @see #boundingArea()
     * @return Titel
     */
    private String boundingArea() {
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
                    for (TextPosition p : textPositions) {
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

    /**
     * Identifiziere aller Schriftgrößen oberhalb der ersten Seite und abspeicherung dessen größe.
     *
     */
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


    /***
     * Triviale Funktion, die einfach das Maximum innerhalb einer Liste ausgiebt.
     * @param textSizes
     * @return
     */
    private float getHighestFontSize(List<Float> textSizes) {
        float minValue = 0;
        for (float f : textSizes) {
            if (f > minValue) {
                minValue = f;
            }
        }
        return minValue;
    }
    public List<String> getTitlesList(){
        return this.titlesList;
    }
    public List<Integer> getPageSizesList(){
        return this.pageSizesList;
    }
}

