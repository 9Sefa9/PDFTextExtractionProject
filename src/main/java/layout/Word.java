package layout;

import extractor.Document;
import extractor.DocumentHandler;
import interfaces.Analyzable;
import org.apache.pdfbox.text.PDFTextStripper;
import utilities.Helper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;


public class Word implements Analyzable {
    public static HashMap<String, Integer> wordOccurenceMap = new HashMap<>();
    private DocumentHandler handler;

    public Word(DocumentHandler handler) {
        this.handler = handler;
    }

    /**
     * Analysiert die vorhanden Wörter innerhalb des PDF Dokuments.
     */
    @Override
    public void analyze() {
        for (Document document : handler.getDocumentsList()) {
            try {
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
            } catch (IOException e) {
                e.printStackTrace();
            }
            Helper.print(wordOccurenceMap);
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
    }

    @Override
    public void start() {
        analyze();
    }
}
