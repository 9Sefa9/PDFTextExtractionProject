package utilities;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import interfaces.PDFX;

import java.io.*;
import java.util.HashMap;

//Statische Klasse als Hilfe für bestimmte Funktionalitäten.
public class Helper implements PDFX {
    public static void csvReader(String csvFile){
        try(CSVReader reader = new CSVReader(new FileReader(csvFile))){
            String[] line;
            while ((line = reader.readNext()) != null) {
                System.out.println("Country [id= " + line[0] + ", code= " + line[1] + " , name=" + line[2] + "]");
            }
        }catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }

    }
    public static void csvWriter(String path, HashMap<String,Integer> wordsAndOccurencesMap){

        try (CSVWriter writer = new CSVWriter(new FileWriter(path))){
        // feed in your array (or convert your data to an array)
        //String[] entries = "sefa#lol#ha#selamun#aleyküm".split("#");
            //Erst die Wörter => Keys.
            String[] keys = wordsAndOccurencesMap.keySet().toArray(new String[wordsAndOccurencesMap.keySet().size()]);



            Integer[] valuesInt = wordsAndOccurencesMap.values().toArray(new Integer[wordsAndOccurencesMap.values().size()]);

            //Dann die Buchstaben Häufigkeiten => Values
            String[] values= new String[valuesInt.length];
            for(int i=0 ;i<keys.length; i++){
                values[i] = valuesInt[i]+"";
            }

            writer.writeNext(keys);
            writer.writeNext(values);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

        public static void main(String[] args) {
              //  csvReader("G:/Users/Progamer/Desktop/addresses.csv");
                //  csvWriter("G:/Users/Progamer/Desktop/addressesWrite.csv");
        }

}

