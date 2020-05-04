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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class Literature implements Analyzable {
    private DocumentHandler handler;
    private PDPage lastPage;
    private List<Float> test;
    public Literature(DocumentHandler handler) {
        this.handler = handler;
        test = new ArrayList<>();
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


                            for(TextPosition t : textPositions)
                                test.add(t.getFontSizeInPt());

                            super.writeString(text, textPositions);
                        }catch (IOException i){
                            i.printStackTrace();
                        }
                    }
                });
                String docText = document.getPdfText().toLowerCase();
                String []documentText = docText.split(" ");
                int first =0;
                int second =documentText.length-1;
                for(int i = 0; i<documentText.length;i++){
                    if(documentText[i].startsWith("\r\nreferen")){
                        first = i;
                        break;
                    }
                }
                StringBuilder b = new StringBuilder();
                for (int i = first ; i<=second;i++){
                    b.append(b.append(documentText[i]));
                }

                System.out.println(b.toString());
                this.test = test.stream().distinct().collect(Collectors.toList());
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
