package io;

import exception.ImportException;
import extractor.Document;
import extractor.DocumentHandler;
import interfaces.Extractable;

import java.io.File;
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
    public void exportDocument(Document document, String path) {
    }
}
/*                else if(file.isDirectory()){
                   // int count=0;

                    ArrayList<File> temp = new ArrayList<>();
                    //Die einzelnen Konferenz-Ordner
                    for(File conferencesDir: file.listFiles()){
                        //Die PDF Daten innerhalb eines Konferenzes
                        for(File files: conferencesDir.listFiles()){
                            temp.add(files);
                        }
                    }
                    File[] newFiles = new File[temp.size()];
                    for (int i = 0; i < temp.size(); i++) {
                        newFiles[i] = temp.get(i);
                    }
                    handler.setDocumentsList(newFiles);
                }
                */