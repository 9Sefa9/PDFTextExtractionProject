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
import java.util.stream.Collectors;

public class Literature implements Analyzable {
    private DocumentHandler handler;
   // private List<Float> test;
    public Literature(DocumentHandler handler) {
        this.handler = handler;
      //  test = new ArrayList<>();
    }

    @Override
    public void analyze() {
        for (Document document : this.handler.getDocumentsList()) {
            //test = new HashMap<Float,String>();
            int lastPage1= document.getPdfDocument().getNumberOfPages()-1;
            int lastPage2= document.getPdfDocument().getNumberOfPages();
            try {
                PageExtractor testt = new PageExtractor(document.getPdfDocument(),lastPage1,lastPage2);
                PDDocument newDocument = testt.extract();
                document.setPdfDocument(newDocument);
                document.setPdfTextStripper(new PDFTextStripper() {

                    @Override
                    protected void writeString(String text, List<TextPosition> textPositions) {
                        try {
                            //letzte Seite soll analysiert werden,

                            for(TextPosition t : textPositions) {
                                //test.add(t.getFontSizeInPt());
                              //  System.out.println(t.getFontSizeInPt());
                            }

                           // System.out.println(textPositions.get(0).getFontSizeInPt() + " "+text);
                            super.writeString(text, textPositions);
                        }catch (IOException i){
                            i.printStackTrace();
                        }
                    }
                });
               // System.out.println(document.getPdfText());
              //  float minFontSize = test.indexOf(Collections.min(test));
                String docText = document.getPdfText().toLowerCase();
                String []documentText = docText.split(" ");
                int first =0;
                int second =documentText.length-1;
              //  System.out.println(docText);
                //Anfangen soll man ab "referen"ces.
                for(int i = 0; i<documentText.length;i++){
                    if(documentText[i].length()>0) {

                        if (documentText[i].contains("\nref") || documentText[i].contains("\rref")
                                && documentText[i].startsWith("ref") && documentText[i].endsWith("es\n")
                                && (documentText[i + 1].substring(0, documentText[i + 1].length() - 1).endsWith("]"))
                                && !documentText[i].startsWith(".")
                                && !documentText[i].startsWith(" ") && !documentText[i].contains("\n")) {
                            //   System.out.println(""+documentText[i]+"i_"+i);
                            first = i;
                            // break;
                        }
                    }
                }
                //System.out.println(first);
                //bis zu der letzten Seite soll untersucht werden. (Erstmal normal printen)
                StringBuilder b = new StringBuilder();
                for (int i = first ; i<=second;i++){
                    b.append(documentText[i]);
                }

                System.out.println(b.toString());
               // this.test = test.stream().distinct().collect(Collectors.toList());
               // Helper.print(document);
                Helper.delimiter();


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
