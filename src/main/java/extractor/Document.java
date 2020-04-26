package extractor;

import exception.ImportException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class Document {
    private PDDocument pdfDocument;
    private PDFTextStripper pdfTextStripper;

    public void setPdfDocument(PDDocument pdfDoc) {
        this.pdfDocument = pdfDoc;
    }

    public PDDocument getPdfDocument() {
        try {
            if (this.pdfDocument == null)
                throw new ImportException("pdfDocument null oder nicht importiert!");
            else
                return this.pdfDocument;
        } catch (ImportException i) {
            i.printStackTrace();
        }
        return null;
    }

    //wird verwendet, um den string zu bearbeiten.
    public void setPdfTextStripper(PDFTextStripper strippper) {
        this.pdfTextStripper = strippper;
    }

    public PDFTextStripper getPdfTextStripper() {
        try {
            if (this.pdfTextStripper == null)
                throw new ImportException("pdfTextStripper null oder nicht importiert!");
            else
                return this.pdfTextStripper;
        } catch (ImportException i) {
            i.printStackTrace();
        }
        return null;
    }

    //@TODO Forschleife muss noch abgecheckt werden. was passiert da in der RegEx ?
    public void print() {
        try {
            String pdfDocumentText = this.pdfTextStripper.getText(pdfDocument);
            if (!this.pdfDocument.isEncrypted())
                System.out.println(pdfDocumentText);
            else throw new Exception("PDFDocument is encrypted! - can't print PDFDocument");
                    /*  String[] words = line.split(" ");
                      String firstWord = words[0].trim();
                      String lastWord = words[words.length - 1].trim();
                       System.out.println("FirstWord:" + firstWord);


                    Diese If Bedingung sorgt dafür, dass z.B "localization\nof" gesplittet wird.
                       if (lastWord.contains("\n")) {
                            String[] lastWordCorrection = lastWord.split("\n");
                           lastWord = lastWordCorrection[lastWordCorrection.length - 1];
                        }

                         System.out.println("lastWord:" + lastWord);

                    Printed den ganzen Line.

                    System.out.println("********************************************************************");
                      */

        } catch (Exception i) {
            i.printStackTrace();
        }
    }
}
