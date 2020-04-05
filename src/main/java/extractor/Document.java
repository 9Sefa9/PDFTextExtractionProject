package extractor;

import exception.ImportException;
import interfaces.Extractable;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

public class Document implements Extractable {
    public static PDDocument pdfDocument;

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
}
