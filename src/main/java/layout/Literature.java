package layout;

import extractor.Document;
import extractor.DocumentParser;
import interfaces.Analyzable;
import org.apache.pdfbox.multipdf.PageExtractor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.IOException;
import java.util.*;

public class Literature implements Analyzable {
    private DocumentParser handler;
    private List<String> literatureList;

    public Literature(DocumentParser handler) {

        this.handler = handler;
        this.literatureList = new ArrayList<>();
    }

    @Override
    public void analyze() {
        System.out.println("Entering Literature Extraction on "+Thread.currentThread().getName()+" :: "+Thread.currentThread().getId());
        int lastPage1;
        int lastPage2;
        PageExtractor extract;
        PDDocument newDocument;
        String docText;
        String []documentText;
        int first;
        int second;
        StringBuilder b;
        for (Document document : this.handler.getDocumentsList()) {

            lastPage1= document.getPdfDocument().getNumberOfPages()-1 == 0 ? 1:document.getPdfDocument().getNumberOfPages()-1;
            lastPage2= document.getPdfDocument().getNumberOfPages();
            try {
                extract = new PageExtractor(document.getPdfDocument(),lastPage1,lastPage2);
                newDocument = extract.extract();
                //der vorherige Dokument wird durch einen neuen PDDocument überladen.
                document.setPdfDocument(newDocument);
                //neuen PDFTextStripper zu initialisieren ist ein wichtiger schritt und darf nicht ausgelassen werden.
                document.setPdfTextStripper(new PDFTextStripper());

                docText = document.getPdfText().toLowerCase();
                //Regex => logisches oder, mit characters, die nicht zwangsweise angenommen werden müssen, aber können.
                // [abc] => a oder b oder c
                documentText = docText.split("[-\\\\t,;.?!:@\\\\[\\\\](){}_*/]");
                first =0;
                second =documentText.length-1;

                //Anfangen soll man ab "referen"ces.
                for(int i = 0; i<documentText.length;i++){
                    if(documentText[i].length()>0) {
                        //System.out.println("blabliblub:"+documentText[i]);
                        if (documentText[i].contains("\nref") || documentText[i].contains("\rref") && documentText[i].endsWith("es\n") && !documentText[i].endsWith(".")) {
                            //   System.out.println(""+documentText[i]+"i_"+i);
                            first = i;
                            // break;
                        }
                    }
                }

                //bis zu der letzten Seite soll untersucht werden. (Erstmal normal printen)
                 b = new StringBuilder();
                for (int i = first ; i<=second;i++){
                    b.append(documentText[i]);
                }
                literatureList.add(b.toString());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Literature Extraction done on "+Thread.currentThread().getName()+" :: "+Thread.currentThread().getId());
    }
    public List<String> getLiteratureList(){
        return this.literatureList;
    }

}
