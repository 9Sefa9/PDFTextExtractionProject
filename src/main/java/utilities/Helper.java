package utilities;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import interfaces.PDFX;

import java.awt.*;
import java.io.*;
import java.util.HashMap;

//Statische Klasse als Hilfe für bestimmte Funktionalitäten.
public class Helper implements PDFX {
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
}

