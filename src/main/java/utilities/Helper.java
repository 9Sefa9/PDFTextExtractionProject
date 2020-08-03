package utilities;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import exception.EmptyException;
import extractor.Document;
import interfaces.PDFX;

import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.stream.Stream;

//Statische Klasse als Hilfe für bestimmte Funktionalitäten.
public class Helper<E> implements PDFX {
    private static String OS;



    /**
     * Startet PDF Document (Windows)
     *
     * @param path Pfad eines PDF Documents, der gestartet werden soll.
     */
    public static void startPDF(String path) {
        if (Desktop.isDesktopSupported() && isWindows()) {
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
    /**
     * Stoppt den Acrobat Reader Process (Windows) "taskkill /F /IM AcroRd32.exe"
     *
     */
    public static void stopPDF() {
        if(isWindows()) {
            try {
                Runtime.getRuntime().exec("taskkill /F /IM AcroRd32.exe");
            } catch (IOException e) {
                e.printStackTrace();
            }
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
     * System out println von Parameter
     * @param text String
     */
    public static void print(String text){
        try {
            if (text != null)
                System.out.println(text);
            else throw new EmptyException("text ist  null");
        }catch (EmptyException e){
            e.printStackTrace();
        }
    }
    /**
     * Gibt Elemente aus
     * @param list Java.util.List
     */
    public static void print(List list) {
        try {
            if (list == null)
                throw new EmptyException("list ist null!");

            list.forEach(System.out::println);
        } catch (EmptyException e) {
            e.printStackTrace();
        }
    }
    /**
     * Gibt Elemente in K,V Form aus
     * @param list Eine HashMap mit K un V
     */
    public static void print(HashMap list) {
        try {
            if (list == null)
                throw new EmptyException("HashMap ist null!");

            list.forEach((k, v) -> System.out.println("K: " + k + "  V:" + v));
        } catch (EmptyException e) {
            e.printStackTrace();
        }
    }
    /**
     * Löscht duplikate in einer HashMap<Float,String>
     * @param list Hashmap mit Float und String
     */
    public static void deleteDuplicates(HashMap<Float, String> list) {
        Set<Float> keys = list.keySet(); // The set of keys in the map.

        Iterator<Float> keyIter = keys.iterator();

        while (keyIter.hasNext()) {
            float key = keyIter.next();
            String value = list.get(key);
            list.put(key, value);
        }
    }
    public static String getOsName()
    {
        if(OS == null) { OS = System.getProperty("os.name"); }
        return OS;
    }
    public static boolean isWindows()
    {
        return getOsName().startsWith("Windows");
    }

    public static boolean isUnix(){
        return getOsName().startsWith("Linux");
    }

    public static Integer[] toIntegerArray(List<Integer> list) {
        Integer[]titlesArray = new Integer[list.size()];
        return list.toArray(titlesArray);
    }
    public static String[] toStringArray(List<String> list) {
        String[]titlesArray = new String[list.size()];
        return list.toArray(titlesArray);
    }
    public static Object concatenate(String[] a, String[] b) {
        return Stream.concat(Arrays.stream(a), Arrays.stream(b)).toArray(String[]::new);
    }
    public static Object concatenate(String[] a, String[] b,String[] c) {
        String[] firstSecond = Stream.concat(Arrays.stream(a), Arrays.stream(b)).toArray(String[]::new);

        return Stream.concat(Arrays.stream(firstSecond),  Arrays.stream(c)).toArray(String[]::new);
        //Stream.of(Stream::concat).reduce().or

    }

}

