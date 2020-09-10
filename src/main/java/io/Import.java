package io;

import exception.ImportException;
import extractor.Document;
import extractor.DocumentParser;
import interfaces.Extractable;
import utilities.KeyValueObject;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Import extends Extractable {
    //Alle PDF Dateien werden in detectedFiles abgespeichert. getKey() = Konferenzname, getValue() = Name des PDF Dokuments.)
    private ArrayList<KeyValueObject<String, File>> detectedFiles = new ArrayList<>();

    //Anzahl an PDF Dokumenten

    /**
     * PDF Dockumente werden importiert und unter der Klasse Document in eine List abgespeichert.
     *
     * @param handler
     * @param dirPath Pfad wo sich ein PDF befindet
     */
    @Override
    public void importDocument(DocumentParser handler, String dirPath) {
        //findPDFrecursiv(handler, dirPath);
        File dirPathFile = new File(dirPath);

        if (!dirPathFile.isDirectory() && !dirPathFile.exists()) {
            try {
                throw new ImportException("Document kann nicht importiert werden!");
            } catch (ImportException e) {
                e.printStackTrace();
            }
        }

        //falls in Gradle, nur ein PDF als Argument angegeben wurde:
        if (dirPathFile.getName().endsWith(".pdf")) {

            getDetectedFiles().add(new KeyValueObject<>("_SINGLE PDF_", dirPathFile));

        }
        //Falls es sich doch um ein Ordner handelt:
        else if (dirPathFile.isDirectory()) {
            findPDFrecursiv2(handler, dirPathFile);
        }
        System.out.println(" PDFs bzw. Publikation: " + getDetectedFiles().size());

        //Extraktionsprozess beginnt mit dem Handler
        handler.prepareList(detectedFiles);

    }

    /**
     * Die Methode wird in {@link #importDocument(DocumentParser, String)} aufgerufen und
     * durchsucht rekursiv alle tieferliegende PDF Dokumente / Ordner / Unterordner usw.
     *
     * @param handler Der Dokument handler
     * @param folder Pfad wo sich ein PDF / Ordner / Unterordner usw. befindet
     */
    public void findPDFrecursiv2(DocumentParser handler, File folder) {

        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                System.out.println("RECURSION ENTER WITH PATH " + fileEntry.getPath());

                findPDFrecursiv2(handler, fileEntry);

            } else {
                getDetectedFiles().add(new KeyValueObject<>(folder.getName(), fileEntry));
              //  System.out.println(getDetectedFiles().get(getDetectedFiles().size()-1).getKey()+" :: "+ getDetectedFiles().get(getDetectedFiles().size()-1).getValue().getName());
            }
        }

    }

    //ignore
    private void findPDFrecursiv(DocumentParser handler, String dirPath) {
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

                getDetectedFiles().add(new KeyValueObject<>("_SINGLE PDF_", file));
            }
            //falls in Gradle, ein Ordner als Argument angegeben wurde:
            else if (file.isDirectory()) {
                //Die einzelnen Konferenz-Ordner
                for (File firstDir : file.listFiles()) {
                    //falls eine Konferenz in wirklichkeit eine PDF Datei ist:
                    if (firstDir.isFile() && firstDir.getName().endsWith(".pdf")) {
                        // System.out.println(firstDir.getName()+ "Count: "+(countPDFs +=1)+ "name: "+firstDir.getName());
                        getDetectedFiles().add(new KeyValueObject<>(firstDir.getName(), firstDir));


                    } else if (firstDir.isDirectory()) {
                        //countConferences += 1;
                        for (File secondDir : firstDir.listFiles()) {
                            //Falls der Konferenz-Ordner im Konferenz-Ordner ein File ist:
                            if (secondDir.isFile() && secondDir.getName().endsWith(".pdf")) {
                                //    System.out.println(secondDir.getName()+ "Count: "+(countPDFs +=1)+ "name: "+firstDir.getName());
                                getDetectedFiles().add(new KeyValueObject<>(firstDir.getName(), secondDir));

                                //Ansonsten ist es ein Ordner, checke Rekursiv nach weiteren PDF Dateien im Ordner
                            } else if (secondDir.isDirectory()) {
                             //   countConferences += 1;
                               System.out.println("RECURSION ENTER WITH PATH " + secondDir.getPath());
                                importDocument(handler, secondDir.getPath());
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
     * Empty Function.
     */
    @Override
    public void exportDocument(Document document, String path) {
    }

    /**
     * Gibt die Namen aller Dokumente zurück.
     *
     */
    public String[] getDocumentNames() {

        String[] documentNames = new String[getDetectedFiles().size()];
        for (int i = 0; i < getDetectedFiles().size(); i++) {
            documentNames[i] = getDetectedFiles().get(i).getValue().getName();
        }
        return documentNames;
    }


    public ArrayList<KeyValueObject<String, File>> getDetectedFiles() {
        return detectedFiles;
    }
}
