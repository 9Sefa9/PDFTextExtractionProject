package extractor;

import exception.ImportException;
import interfaces.Extractable;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripper;
import utilities.KeyValueObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

public class DocumentParser {
    //Liste mit PDFDocument's.   Die Klasse Document wird dafür genutzt.
    private List<Document> documentsList;
    private HashSet<String> conferenceNames;
    public DocumentParser() {
        documentsList = new ArrayList<>();
        conferenceNames = new HashSet<>();
    }

    /**
     * mit prepareList werden PDF files den Konferenzen zugeordnet und in die documentsList abgespeichert.
     *
     * @param documentFiles beinhaltet eine Liste mit dem Namen des zugehörigen Konferenz und PDF File.
     */
    public void prepareList(ArrayList<KeyValueObject<String, File>> documentFiles) {
        PDFTextStripper stripper=null;
        PDDocument document=null;
        Document newDocument=null;
        String conferenceName,pdfName,pdfPath;
        try {
            // getKey() = Konferenzname, getValue() = Name des PDF Dokuments.
            for(KeyValueObject<String,File> docFile : documentFiles){
                //Falls docFile null ist
                if (docFile == null)
                    throw new ImportException("Null File in documentFiles!");

                //Abspeichern von information
                conferenceName = docFile.getKey();
                pdfName = docFile.getValue().getName();
                pdfPath = docFile.getValue().getAbsolutePath();

                //Erstelle ein Document mit den Informationen und lade den Dokument.
                newDocument = new Document(conferenceName,pdfName, pdfPath);
                //Weitere Informationen speichern(wichtig für Extraktion..Wird voraussichtlich überladen in den Analyzable unterklassen.)
                document = PDDocument.load(docFile.getValue());
                newDocument.setPdfDocument(document);
                stripper = new PDFTextStripper();
                newDocument.setPdfTextStripper(stripper);

                //Zuletzt werden die Konferenz Namen in eine List gespeichert
                this.conferenceNames.add(conferenceName);
                this.documentsList.add(newDocument);
            }
        } catch (IOException | ImportException i) {
            i.printStackTrace();
        }
    }

    public synchronized List<Document> getDocumentsList() {
        return this.documentsList;
    }

    public synchronized String[] getConferenceNames() {
        String[]tmp = new String[this.conferenceNames.size()];
        return this.conferenceNames.toArray(tmp);
    }
}
