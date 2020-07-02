package io;

import exception.ImportException;
import extractor.Document;
import extractor.DocumentHandler;
import interfaces.Extractable;
import utilities.KeyValueObject;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Import extends Extractable {
    //Alle PDF Dateien werden in detectedFiles abgespeichert
    ArrayList<KeyValueObject<String,File>> detectedFiles = new ArrayList<>();


    //Anzahl an PDF Dokumenten
    int countConferences=0;
    /**
     * PDF Dockumente werden importiert und unter der Klasse Document in eine List abgespeichert.
     *
     * @param handler
     * @param dirPath Pfad wo sich ein PDF befindet
     */
    @Override
    public void importDocument(DocumentHandler handler, String dirPath) {
        findPDFrecursiv(handler,dirPath);
        System.out.println("Conferences: "+countConferences+" PDFs: "+detectedFiles.size());

        //Überträgt ArrayList<KeyValueOBject<String,File>> zu einem File-Array,
        // da setDocumentsList nur File[] akzeptiert.
        File[] myPDFFiles = new File[detectedFiles.size()];
        for(int i = 0;i<detectedFiles.size();i++)
            myPDFFiles[i] = detectedFiles.get(0).getValue();

        //setze den Ordner mit den PDF Dokumenten in die DocumentsList.
        handler.setDocumentsList(myPDFFiles);

    }
    /**
     * Die Methode wird in {@link #importDocument(DocumentHandler, String)} aufgerufen und
     * durchsucht rekursiv alle tieferliegende PDF Dokumente / Ordner / Unterordner usw.
     * @param handler Der Dokument handler
     * @param dirPath Pfad wo sich ein PDF / Ordner / Unterordner usw. befindet
     */
    private void findPDFrecursiv(DocumentHandler handler, String dirPath) {
        File file;
        String absolutePath;
        try {
            absolutePath = Paths.get(dirPath).toAbsolutePath().toString();
            file = new File(absolutePath);

            if (!file.isDirectory() && !file.exists()) {
                throw new ImportException("Document kann nicht importiert werden!");
            }
            //falls in Gradle, nur ein PDF als Argument angegeben wurde:
            if (file.getName().endsWith(".pdf")) {

                detectedFiles.add(new KeyValueObject<>(null,file));
            }
            //falls in Gradle, ein Ordner als Argument angegeben wurde:
            else if (file.isDirectory()) {
                //Die einzelnen Konferenz-Ordner
                for (File firstDir : file.listFiles()) {
                    //falls eine Konferenz in wirklichkeit eine PDF Datei ist:
                    if(firstDir.isFile() && firstDir.getName().endsWith(".pdf")) {
                        // System.out.println(firstDir.getName()+ "Count: "+(countPDFs +=1)+ "name: "+firstDir.getName());
                        detectedFiles.add(new KeyValueObject<>(firstDir.getName(),firstDir));

                    }else if(firstDir.isDirectory()) {
                        countConferences+=1;
                        for (File secondDir : firstDir.listFiles()) {
                            //Falls der Konferenz-Ordner im Konferenz-Ordner ein File ist:
                            if (secondDir.isFile() && secondDir.getName().endsWith(".pdf")) {
                                //    System.out.println(secondDir.getName()+ "Count: "+(countPDFs +=1)+ "name: "+firstDir.getName());
                                detectedFiles.add(new KeyValueObject<>(firstDir.getName(),secondDir));

                                //Ansonsten ist es ein Ordner, checke Rekursiv nach weiteren PDF Dateien im Ordner
                            }else if(secondDir.isDirectory()){
                                countConferences+=1;
                                System.out.println("RECURSION ENTER WITH PATH "+secondDir.getPath());
                                importDocument(handler,secondDir.getPath());
                            }
                        }
                    }
                }
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