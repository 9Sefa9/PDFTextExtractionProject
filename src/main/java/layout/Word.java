package layout;

import extractor.Document;
import extractor.DocumentParser;
import interfaces.Analyzable;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class Word implements Analyzable {

    private DocumentParser handler;
    private List<HashMap<String,Integer>> wordOccurenceList;
    public Word(DocumentParser handler) {

        this.handler = handler;
        wordOccurenceList = new ArrayList<>();
    }


    /**
     * Analysiert die vorhanden Wörter innerhalb des PDF Dokuments und speichert die häufigen Buchstaben in eine Liste.
     */
    @Override
    public void analyze() {
        System.out.println("Entering Word Extraction...");
        for (Document document : handler.getDocumentsList()) {
            HashMap<String, Integer> wordOccurenceMap;
            try {
                wordOccurenceMap = new HashMap<>();
                document.setPdfTextStripper(new PDFTextStripper(){
                    @Override
                    protected void writeString(String text) throws IOException {
                        String []res = text.split(" ");
                        for (String str:res) {
                            wordOccurenceMap.merge(str.replaceAll("\\W","").toLowerCase(Locale.ENGLISH), 1, Integer::sum);
                        }
                        super.writeString(text);
                    }
                });
                //Wichtig um die Forloop durchzzuführen
                document.getPdfName();
                wordOccurenceList.add(wordOccurenceMap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }
    public List<HashMap<String, Integer>> getWordOccurenceList() {
        return this.wordOccurenceList;
    }
}

    /*
        try {
            Document.getPdfDocument().pdfTextStripper = new PDFTextStripper() {
                @Override
                protected void writeString(String text, List<TextPosition> textPositions) throws IOException {
                    Document.getPdfDocument().pdfTextStripper.setParagraphStart("\t");
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
        }*/
