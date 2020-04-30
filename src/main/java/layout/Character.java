package layout;

import extractor.Document;
import extractor.DocumentHandler;
import interfaces.Analyzable;
import org.apache.fontbox.util.BoundingBox;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Character implements Analyzable {

    private HashMap<java.lang.Character, Integer> charactersOccurenceMap = new HashMap<>();
    private DocumentHandler documentHandler;
    public Character(DocumentHandler handler) {
        this.documentHandler = handler;
    }

    /**
     * Analysiert die vorhanden characters innerhalb des PDF Dokuments.
     */
    @Override
    public void analyze() {
        for(Document document: this.documentHandler.getDocumentsList()){
        try {

                document.setPdfTextStripper(new PDFTextStripper() {
                    @Override
                    protected void writeString(String text, List<TextPosition> textPositions) throws IOException {
                        setParagraphStart("\t");
                        //\w matched alle char's aber nicht non-word character.
                        // charactersOccurenceMap.merge(text.replaceAll("\\W", "").toLowerCase(Locale.ENGLISH), 1, Integer::sum);
                        for(java.lang.Character c:text.replaceAll("\\W", "").toLowerCase(Locale.ENGLISH).toCharArray()){
                            charactersOccurenceMap.merge(c, 1, Integer::sum);
                        }

                        super.writeString(text, textPositions);
                    }
                });
                document.print();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void start() {
        analyze();
    }
}
