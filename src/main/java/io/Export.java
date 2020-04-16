package io;

import exception.ExportException;
import interfaces.Extractable;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.File;
import java.io.IOException;

public class Export extends Extractable {
    @Override
    public void importDocument(String path) {

    }

    /**Exportiert PDF in ein festgelegtes Verzeichnis
     *
     * @param pdfDocument Gibt an, welches Document exportiert werden soll.
     * @param path Gibt an, wohin das Dokument abgspeichert werden soll.
     */
    public void exportDocument(PDDocument pdfDocument, String path) {
        //src\main\resources\colored08662658.pdf
        try {
            if(pdfDocument==null)
                throw new ExportException("pdf Document ist null!");

            File file1 = new File(path);
            pdfDocument.save(file1);
            pdfDocument.close();
            System.out.println("Datei erfolgreich exportiert unter "+path);
        } catch (IOException | ExportException e){
            e.printStackTrace();
        }
    }
}
