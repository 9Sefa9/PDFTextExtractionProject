package extractor;

import exception.ImportException;
import interfaces.Extractable;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class Document {
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

public class DocumentHandler {
    //Liste mit PDFDocument's.   Die innerclass wird dafür genutzt.
    private List<Document> documentsList = new ArrayList<Document>();

    public List<Document> getDocumentsList() {
        return this.documentsList;
    }

    public void setDocumentsList(File[] documentFiles) {
        try {
            for (File docFile : documentFiles) {
                if (docFile == null)
                    throw new ImportException("Null File in documentFiles!");
                Document newDocument = new Document();
                newDocument.setPdfDocument(PDDocument.load(docFile));
                getDocumentsList().add(newDocument);
            }
        } catch (IOException | ImportException i) {
            i.printStackTrace();
        }
    }
}
