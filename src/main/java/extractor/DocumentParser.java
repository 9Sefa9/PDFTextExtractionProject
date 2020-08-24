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


   /* public void prepareList(File[] documentFiles) {
        try {
            for (File docFile : documentFiles) {

                if (docFile == null)
                    throw new ImportException("Null File in documentFiles!");

                //identifiziere die einzelnen konferenzNamen und trage Sie in die documentsList ein.
                Document newDocument = new Document(docFile.getName(), docFile.getAbsolutePath());
                newDocument.setPdfDocument(PDDocument.load(docFile));

                this.documentsList.add(newDocument);
            }
        } catch (IOException | ImportException i) {
            i.printStackTrace();
        }
    }*/

    /**
     * mit prepareList werden PDF files den Konferenzen zugeordnet und in die documentsList abgespeichert.
     *
     * @param documentFiles beinhaltet eine Liste mit dem Namen des zugehörigen Konferenz und PDF File.
     */
    public void prepareList(ArrayList<KeyValueObject<String, File>> documentFiles) {
        try {
            // getKey() = Konferenzname, getValue() = Name des PDF Dokuments.
            for(KeyValueObject<String,File> docFile : documentFiles){
                //Falls docFile null ist
                if (docFile == null)
                    throw new ImportException("Null File in documentFiles!");

                //Abspeichern von information
                String conferenceName = docFile.getKey();
                String pdfName = docFile.getValue().getName();
                String pdfPath = docFile.getValue().getAbsolutePath();

                //Erstelle ein Document mit den Informationen und lade den Dokument.
                Document newDocument = new Document(conferenceName,pdfName, pdfPath);
                //Weitere Informationen speichern(wichtig für Extraktion..Wird voraussichtlich überladen in den Analyzable unterklassen.)
                newDocument.setPdfDocument(PDDocument.load(docFile.getValue()));
                newDocument.setPdfTextStripper(new PDFTextStripper());

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
       /* //der size gefällt mir nicht. ist nicht korrekt. Sollte aber kein problem sein.
        String[] conferencesName = new String[getDetectedFiles().size()];
        String lastConferenceName ="";
        //wichtig, da sonst lücken entstehen.
        int j=0;
        for (int i = 0; i < getDetectedFiles().size(); i++) {
            File parentFile = getDetectedFiles().get(i).getValue().getParentFile();
            //wenn Elternpfad noch existiert
            if (parentFile != null)
                //und falls der nicht vorher eingetragen wurde,
                if(!parentFile.getName().equals(lastConferenceName)) {
                    conferencesName[j] = parentFile.getName();
                    lastConferenceName = parentFile.getName();
                    j+=1;
                }
        }
        return conferencesName;
    }*/
}
