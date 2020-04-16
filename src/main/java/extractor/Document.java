package extractor;

import exception.ImportException;
import interfaces.Extractable;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.IOException;

public class Document {
    private static PDDocument pdfDocument;
    private static PDFTextStripper pdfTextStripper;
    public static void setPdfDocument(PDDocument pdfDoc) {
        pdfDocument = pdfDoc;
    }
    public static PDDocument getPdfDocument() {
        try {
            if (pdfDocument == null)
                throw new ImportException("Document null oder nicht importiert!");
            else
                return pdfDocument;
        } catch (ImportException i) {
            i.printStackTrace();
        }
        return null;
    }
    public static void setPdfTextStripper(PDFTextStripper strippper){pdfTextStripper = strippper;}
    public static PDFTextStripper getPdfTextStripper(){return pdfTextStripper;}
    //@TODO Forschleife muss noch abgecheckt werden. was passiert da in der RegEx ?
    public static void printDocument(PDFTextStripper pdfTextStripper, PDDocument pdfDocument) {
        try {
            String pdfDocumentText = pdfTextStripper.getText(pdfDocument);
            if (!pdfDocument.isEncrypted()) {
                for (String line : pdfDocumentText.split(pdfTextStripper.getParagraphStart())) {
                    String[] words = line.split(" ");
                    String firstWord = words[0].trim();
                    String lastWord = words[words.length - 1].trim();
                    System.out.println("FirstWord:" + firstWord);


                    //Diese If Bedingung sorgt dafür, dass z.B "localization\nof" gesplittet wird.
                    if (lastWord.contains("\n")) {
                        String[] lastWordCorrection = lastWord.split("\n");
                        lastWord = lastWordCorrection[lastWordCorrection.length - 1];
                    }

                    System.out.println("lastWord:" + lastWord);

                    //Printed den ganzen Line.
                    System.out.println(line);
                    System.out.println("********************************************************************");
                }
            }
        }catch (IOException i){
            i.printStackTrace();
        }
    }
}
