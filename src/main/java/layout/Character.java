package layout;

import extractor.Document;
import extractor.DocumentParser;
import interfaces.Analyzable;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Character implements Analyzable {

    private HashMap<java.lang.Character, Integer> charactersOccurenceMap;
    private DocumentParser documentParser;
    public Character(DocumentParser handler) {

        this.documentParser = handler;
        charactersOccurenceMap = new HashMap<>();
    }

    /**
     * Analysiert die vorhanden characters innerhalb des PDF Dokuments und speichert die am häufig vorkommenden Zeichen in eine Liste.
     */
    @Override
    public void analyze() {
        System.out.println("Entering Character Extraction...");
        for(Document document: this.documentParser.getDocumentsList()){
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
                document.getPdfText();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        System.out.println("Character Extraction Done...");
    }
    public  HashMap<java.lang.Character, Integer> getCharactersOccurenceMap(){
        return this.charactersOccurenceMap;
    }

}
