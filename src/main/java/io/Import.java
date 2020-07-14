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
    int countConferences = 0;

    /**
     * PDF Dockumente werden importiert und unter der Klasse Document in eine List abgespeichert.
     *
     * @param handler
     * @param dirPath Pfad wo sich ein PDF befindet
     */
    @Override
    public void importDocument(DocumentParser handler, String dirPath) {
        findPDFrecursiv(handler, dirPath);
        System.out.println("Conferences: " + countConferences + " PDFs: " + getDetectedFiles().size());
        handler.prepareList(detectedFiles);

    }

    /**
     * Die Methode wird in {@link #importDocument(DocumentParser, String)} aufgerufen und
     * durchsucht rekursiv alle tieferliegende PDF Dokumente / Ordner / Unterordner usw.
     *
     * @param handler Der Dokument handler
     * @param dirPath Pfad wo sich ein PDF / Ordner / Unterordner usw. befindet
     */
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

                getDetectedFiles().add(new KeyValueObject<>(null, file));
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
                        countConferences += 1;
                        for (File secondDir : firstDir.listFiles()) {
                            //Falls der Konferenz-Ordner im Konferenz-Ordner ein File ist:
                            if (secondDir.isFile() && secondDir.getName().endsWith(".pdf")) {
                                //    System.out.println(secondDir.getName()+ "Count: "+(countPDFs +=1)+ "name: "+firstDir.getName());
                                getDetectedFiles().add(new KeyValueObject<>(firstDir.getName(), secondDir));

                                //Ansonsten ist es ein Ordner, checke Rekursiv nach weiteren PDF Dateien im Ordner
                            } else if (secondDir.isDirectory()) {
                                countConferences += 1;
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
     * NullFunction
     */
    @Override
    public void exportDocument(Document document, String path) {
    }

    public String[] getDocumentNames() {
        //der size gefällt mir nicht. ist nicht korrekt. Sollte aber kein problem sein.
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
