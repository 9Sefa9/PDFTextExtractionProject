package extractor;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class PDFExtractor {
    private final String pdfPath = "src\\main\\resources\\08662658.pdf";
    private float min= Float.MAX_VALUE;
    private PDDocument document;
    private PDFTextStripper pdfTextStripper;

    public void start() throws IOException {
        analyze();
        loadPDF(pdfPath);
        printCompleteDoc();
    }

    private void loadPDF(String pdfPath) {
        String absolutePath;
        try {
            absolutePath = Paths.get(pdfPath).toAbsolutePath().toString();
            document = PDDocument.load(new File(absolutePath));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void printCompleteDoc(){
        try {
            if (!document.isEncrypted()) {
                for (String line : pdfTextStripper.getText(document).split(pdfTextStripper.getParagraphStart())) {
                    System.out.println(line);
                    System.out.println("********************************************************************");
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Eventuell abschnitte rausfiltern, anzahl buchstaben etc filtern.. usw. Mit zusammenarbeit von Data.java
    private void analyze() {
        try {
            pdfTextStripper = new PDFTextStripper(){
                @Override
                protected void writeString(String text, List<TextPosition> textPositions) throws IOException {
                    pdfTextStripper.setParagraphStart("\t");
                    StringBuilder builder = new StringBuilder();
                    for(TextPosition t : textPositions){
                        if(builder.length()==0){
                            builder.append("["+t.getFontSizeInPt()+"]");
                        }
                        if(!t.equals(-1)){
                            builder.append(t);
                        }

                    }
                    String newText = builder.toString();
                    super.writeString(newText, textPositions);
                }
            };
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
