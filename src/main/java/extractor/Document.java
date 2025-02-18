package extractor;

import exception.ImportException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;

/**
 * Repräsentiert ein "Dokument" Objekt.  Folglich hat diese einen Namen, den Namen der Konferenz aus der er stammt,
 * sein STrippe und der PDDocument um auf weitere Eigenschaften zugreifen zu können.
 */
public class Document {
    private PDDocument pdfDocument;
    private PDFTextStripper pdfTextStripper;
    private String pdfText;
    private String conferenceName;
    private String pdfName;
    private String pdfPath;
    public Document(String conferenceName, String pdfName, String absolutePath) {
        this.conferenceName = conferenceName;
        this.pdfName = pdfName;
        this.pdfPath = absolutePath;
    }

    public void setPdfPath(String pdfPath) {
        this.pdfPath = pdfPath;
    }

    public String getPdfPath() {
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
        StringBuilder br;
        try {
            br = new StringBuilder();
            br.append(this.pdfTextStripper.getText(getPdfDocument()));
            //String fullText = this.pdfTextStripper.getText(getPdfDocument());
            setPdfText(br.toString());
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

    public void print(PDFTextStripper stripper) {
        String pdfDocumentText;
        try {
            pdfDocumentText = stripper.getText(pdfDocument);
            if (!this.pdfDocument.isEncrypted())
                System.out.println(pdfDocumentText);
            else throw new Exception("PDFDocument is encrypted! - can't print PDFDocument");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getConferenceName() {
        return this.conferenceName;
    }

    public void setConferenceName(String conferenceName) {
        this.conferenceName = conferenceName;
    }

    public String getPdfText() {
        return pdfText;
    }

    public void setPdfText(String pdfText) {
        this.pdfText = pdfText;
    }

    public String getPdfName() {
        return this.pdfName;
    }

    public String getParentName() {
        return new File(this.pdfPath).getParentFile().getName();
    }

    public int getPageNumbers() {
        return getPdfDocument().getNumberOfPages();
    }
}
