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
        System.out.println("Entering Word Extraction on "+Thread.currentThread().getName()+" :: "+Thread.currentThread().getId());
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
        System.out.println("Word Extraction done on "+Thread.currentThread().getName()+" :: "+Thread.currentThread().getId());

    }
    public List<HashMap<String, Integer>> getWordOccurenceList() {
        return this.wordOccurenceList;
    }
}
