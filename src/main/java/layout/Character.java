package layout;

import extractor.Document;
import extractor.DocumentHandler;
import figure.Rectangle;
import interfaces.Analyzable;
import io.Export;
import org.apache.fontbox.util.BoundingBox;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;
import utilities.Helper;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.function.BiConsumer;

public class Character implements Analyzable {

    private HashMap<java.lang.Character, Integer> charactersOccurenceMap = new HashMap<>();
    private DocumentHandler documentHandler;
    public Character(DocumentHandler handler) {
        this.documentHandler = handler;
    }

    /**
     * Analysiert die vorhanden characters innerhalb des PDF Dokuments und speichert die am häufig vorkommenden Zeichen in eine Liste.
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
                        char []splittedText = text.replaceAll("\\W", "").toLowerCase(Locale.ENGLISH).toCharArray();
                        for(java.lang.Character c:splittedText){
                            charactersOccurenceMap.merge(c, 1, Integer::sum);
                        }

                        super.writeString(text, textPositions);
                    }
                });
               // System.out.println(this.charactersOccurenceMap.size());
              //  Helper.print(this.charactersOccurenceMap);
                //charactersOccurenceMap.forEach((c,i) -> System.out.println("C:"+c+"  I:"+i));


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
