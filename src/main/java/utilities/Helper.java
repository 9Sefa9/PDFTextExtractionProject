package utilities;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import exception.EmptyException;
import extractor.Document;
import interfaces.PDFX;

import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Map.*;

//Statische Klasse als Hilfe für bestimmte Funktionalitäten.
public class Helper<E> implements PDFX {
    /**
     * Liest eine *.csv Datei bezüglich csvFile
     *
     * @param csvFile Der Pfad einer *.csv der abgelesen werden soll.
     */
    public static void csvReader(String csvFile) {
        try (CSVReader reader = new CSVReader(new FileReader(csvFile))) {
            String[] line;
            while ((line = reader.readNext()) != null) {
                System.out.println("Country [id= " + line[0] + ", code= " + line[1] + " , name=" + line[2] + "]");
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }

    }

    /**
     * Erstellt mithilfe csvFile eine neue *.csv Datei.
     *
     * @param path                  Pfad wo *.csv abgespeichert werden soll.
     * @param wordsAndOccurencesMap Hashmap mit Wörtern(key) und wie oft diese auftauchen(value).
     */
    public static void csvWriter(String path, HashMap<String, Integer> wordsAndOccurencesMap) {

        try (CSVWriter writer = new CSVWriter(new FileWriter(path))) {
            // feed in your array (or convert your data to an array)
            //String[] entries = "sefa#lol#ha#selamun#aleyküm".split("#");
            //Erst die Wörter => Keys.
            String[] keys = wordsAndOccurencesMap.keySet().toArray(new String[wordsAndOccurencesMap.keySet().size()]);


            Integer[] valuesInt = wordsAndOccurencesMap.values().toArray(new Integer[wordsAndOccurencesMap.values().size()]);

            //Dann die Buchstaben Häufigkeiten => Values
            String[] values = new String[valuesInt.length];
            for (int i = 0; i < keys.length; i++) {
                values[i] = valuesInt[i] + "";
            }

            writer.writeNext(keys);
            writer.writeNext(values);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Startet PDF Document
     *
     * @param path Pfad eines PDF Documents, der gestartet werden soll.
     */
    public static void startPDF(String path) {
        if (Desktop.isDesktopSupported()) {
            try {
                File myFile = new File(path/*"src\\main\\resources\\colored08662658.pdf"*/);
                Desktop.getDesktop().open(myFile);
            } catch (IOException ex) {
                // no application registered for PDFs
                try {
                    Runtime.getRuntime().exec("taskkill /F /IM AcroRd32.exe");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                startPDF(path);
            }
        }
    }

    public static void stopPDF() {
        try {
            Runtime.getRuntime().exec("taskkill /F /IM AcroRd32.exe");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * setzt nur eine linie, damit die prints nicht ineinander geraten. Nützlich für den Nutzer.
     */
    public static void delimiter() {
        System.out.println();
        for (int i = 0; i < 150; i++) {
            System.out.print("#");
        }
        System.out.println();
    }

    /**
     * printed etwas
     *
     * @param document wird geprinted.
     */
    //@TODO Forschleife muss noch abgecheckt werden. was passiert da in der RegEx ?
    public static void print(Document document) {
        try {
            document.setPdfText(document.getPdfTextStripper().getText(document.getPdfDocument()));
            if (!document.getPdfDocument().isEncrypted())
                System.out.println(document.getPdfText());
             else throw new Exception("PDFDocument is encrypted! - can't print PDFDocument");
                    /*  String[] words = line.split(" ");
                      String firstWord = words[0].trim();
                      String lastWord = words[words.length - 1].trim();
                       System.out.println("FirstWord:" + firstWord);


                    Diese If Bedingung sorgt dafür, dass z.B "localization\nof" gesplittet wird.
                       if (lastWord.contains("\n")) {
                            String[] lastWordCorrection = lastWord.split("\n");
                           lastWord = lastWordCorrection[lastWordCorrection.length - 1];
                        }

                         System.out.println("lastWord:" + lastWord);

                    Printed den ganzen Line.

                    System.out.println("********************************************************************");
                      */

        } catch (Exception i) {
            i.printStackTrace();
        }
    }

    public static void print(List list) {
        try {
            if (list == null)
                throw new EmptyException("list ist null!");

            list.forEach(System.out::println);
        } catch (EmptyException e) {
            e.printStackTrace();
        }
    }

    public static void print(HashMap list) {
        try {
            if (list == null)
                throw new EmptyException("HashMap ist null!");

            list.forEach((k, v) -> System.out.println("K: " + k + "  V:" + v));
        } catch (EmptyException e) {
            e.printStackTrace();
        }
    }

    public static void deleteDuplicates(HashMap<Float, String> list) {
        Set<Float> keys = list.keySet(); // The set of keys in the map.

        Iterator<Float> keyIter = keys.iterator();

        while (keyIter.hasNext()) {
            float key = keyIter.next();
            String value = list.get(key);
            list.put(key, value);
        }
    }
}

