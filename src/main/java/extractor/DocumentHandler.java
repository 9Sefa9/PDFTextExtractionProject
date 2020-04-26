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

public class DocumentHandler {
    //Liste mit PDFDocument's.   Die innerclass wird dafür genutzt.
    private List<Document> documentsList;
    public DocumentHandler(){
        documentsList = new ArrayList<>();
    }
    public List<Document> getDocumentsList() {
        return this.documentsList;
    }
    /**
     * mit setDocumentsList werden PDF files in die documentsList abgespeichert.
     * @param documentFiles beinhaltet eine Liste mit PDF Files.
     */
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
