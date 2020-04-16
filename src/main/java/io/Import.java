package io;

import exception.ImportException;
import extractor.Document;
import interfaces.Extractable;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class Import extends Extractable {
    /**
     * PDF Document wird importiert und unter der Klasse Document abgespeichert.
     *
     * @param path Pfad wo sich ein PDF befindet
     */
    @Override
    public void importDocument(String path) {
        try {
            String absolutePath = Paths.get(path).toAbsolutePath().toString();
            if (!new File(absolutePath).exists()) {
                throw new ImportException("Document nicht gefunden!");
            } else
                Document.setPdfDocument(PDDocument.load(new File(absolutePath)));

        } catch (IOException | ImportException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void exportDocument(PDDocument pdfDocument, String path) {
    }
}
