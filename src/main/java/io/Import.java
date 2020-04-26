package io;

import exception.ImportException;
import extractor.DocumentHandler;
import interfaces.Extractable;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Paths;

public class Import extends Extractable {
    /**
     * PDF Dockumente werden importiert und unter der Klasse Document in eine List abgespeichert.
     *
     * @param handler
     * @param dirPath Pfad wo sich ein PDF befindet
     */
    @Override
    public void importDocument(DocumentHandler handler, String dirPath) {
        File file;
        try {
            String absolutePath = Paths.get(dirPath).toAbsolutePath().toString();
            file = new File(absolutePath);
            if (!file.isDirectory() && !file.exists()) {
                throw new ImportException("Document kann nicht importiert werden!");
            } else {
                 //falls nur 1 PDF Dokument:
                if(file.getName().endsWith(".pdf"))
                    handler.setDocumentsList(new File[]{file});
                else
                //setze den Ordner mit den PDF Dokumenten in die DocumentsList.
                handler.setDocumentsList(file.listFiles((dir, name) -> name.endsWith(".pdf") ? true : false));
            }
        } catch (ImportException e) {
            e.printStackTrace();
        }
    }

    /**
     * NullFunction
     */
    @Override
    public void exportDocument(DocumentHandler handler, String path) {
    }
}
