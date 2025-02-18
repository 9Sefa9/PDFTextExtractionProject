package io;

import exception.ExportException;
import extractor.Document;
import extractor.DocumentParser;
import interfaces.Extractable;

import java.io.File;
import java.io.IOException;

public class Export extends Extractable {
    /**Exportiert PDF in ein festgelegtes Verzeichnis
     * TODO muss noch implementiert werden.
     * @param document beherbergt die liste mit allen PDF'
     * @param path Gibt an, wohin das Dokument abgspeichert werden soll.
     */
    public void exportDocument(Document document, String path) {
        File file1;
        //src\main\resources\colored08662658.pdf
        try {
            if(document==null)
                throw new ExportException("DocumentHandler ist null!");

            file1 = new File(path);
            document.getPdfDocument().save(file1);
            //document.getPdfDocument().close();
            System.out.println("Datei erfolgreich exportiert unter "+path);
        } catch (IOException | ExportException e){
            e.printStackTrace();
        }

    }
    /**
     * NullFunction, keine Funktion.
     */
    @Override
    public void importDocument(DocumentParser handler, String path) {

    }
}
