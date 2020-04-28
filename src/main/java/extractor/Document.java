package extractor;

import exception.ImportException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.IOException;

public class Document {
    private PDDocument pdfDocument;
    private PDFTextStripper pdfTextStripper;
    private String pdfText;
    private String pdfName;
    private String pdfPath;
    public Document(String name, String absolutePath) {
        this.pdfName =name;
        this.pdfPath = absolutePath;
    }
    public void setPdfPath(String pdfPath){
        this.pdfPath = pdfPath;
    }
    public String getPdfPath(){
        return this.pdfPath;
    }
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
        //Wenn pdfTextStripper gesetzt wurde, soll der entsprechende Text auch gepseichert werden!
        //spart uns Zeit, wenn wir printen wollen.
        try {
            setPdfText(strippper.getText(getPdfDocument()));
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public void print(PDFTextStripper stripper){
        try {
            String pdfDocumentText = stripper.getText(pdfDocument);
            //if (!this.pdfDocument.isEncrypted())
                System.out.println(pdfDocumentText);
            //else throw new Exception("PDFDocument is encrypted! - can't print PDFDocument");
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    //@TODO Forschleife muss noch abgecheckt werden. was passiert da in der RegEx ?
    public void print() {
        try {
            String pdfDocumentText = this.pdfTextStripper.getText(pdfDocument);
            //if (!this.pdfDocument.isEncrypted())
                System.out.println(pdfDocumentText);
           // else throw new Exception("PDFDocument is encrypted! - can't print PDFDocument");
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

    public String getPdfText() {
        return pdfText;
    }

    public void setPdfText(String pdfText) {
        this.pdfText = pdfText;
    }
    public String getPdfName(){
        return this.pdfName;
    }
}
