package layout;

import extractor.Document;
import extractor.DocumentHandler;
import interfaces.Analyzable;
import org.apache.pdfbox.multipdf.PageExtractor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;
import utilities.Helper;

import java.io.IOException;
import java.util.*;

public class Literature implements Analyzable {
    private DocumentHandler handler;

    public Literature(DocumentHandler handler) {
        this.handler = handler;
    }

    @Override
    public void analyze() {
        for (Document document : this.handler.getDocumentsList()) {

            int lastPage1= document.getPdfDocument().getNumberOfPages()-1 == 0 ? 1:document.getPdfDocument().getNumberOfPages()-1;
            int lastPage2= document.getPdfDocument().getNumberOfPages();
            try {
                PageExtractor extract = new PageExtractor(document.getPdfDocument(),lastPage1,lastPage2);
                PDDocument newDocument = extract.extract();
                document.setPdfDocument(newDocument);
                document.setPdfTextStripper(new PDFTextStripper());
                String docText = document.getPdfText().toLowerCase();
                //Regex => logisches oder, mit characters, die nicht zwangsweise angenommen werden müssen, aber können.
                // [abc] => a oder b oder c
                String []documentText = docText.split("[-\\\\t,;.?!:@\\\\[\\\\](){}_*/]");
                int first =0;
                int second =documentText.length-1;

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
                StringBuilder b = new StringBuilder();
                for (int i = first ; i<=second;i++){
                    b.append(documentText[i]);
                }

              //  System.out.println(b.toString());

               // Helper.print(document);
              //  Helper.delimiter();


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
