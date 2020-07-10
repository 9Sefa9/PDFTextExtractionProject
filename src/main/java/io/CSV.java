package io;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import extractor.Document;
import extractor.DocumentHandler;
import interfaces.Extractable;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class CSV {
    private CSVWriter writer;
    private FileWriter fileWriter;
    private CSVReader reader;
    private FileReader fileReader;

    //Inkrementoren
    private int i,j;

    public CSV(String commonPath){
        try {
            this.fileWriter = new FileWriter(commonPath);
            this.fileReader = new FileReader(commonPath);

            this.writer = new CSVWriter(fileWriter,CSVWriter.DEFAULT_SEPARATOR,
                    CSVWriter.NO_QUOTE_CHARACTER,
                    CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                    CSVWriter.DEFAULT_LINE_END);
            this.reader = new CSVReader(fileReader);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public CSV(String importPath, String exportPath) {
        try {

            this.fileWriter = new FileWriter(exportPath);
            this.fileReader = new FileReader(importPath);

            this.writer = new CSVWriter(fileWriter);
            this.reader = new CSVReader(fileReader);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Erstellt mithilfe csvFile eine neue *.csv Datei.
     *
     * @param values Werte die abgespeichert werden sollen.
     */

    public void writeCSV(String[] values) {
        writer.writeNext(values,true);
    }
/*
    /**
     * Liest eine *.csv Datei bezüglich csvFile
     * TODO könnte gemacht werden.
     *
     * @param csvFile Der Pfad einer *.csv der abgelesen werden soll.


    public void readCSV(String csvFile) {
        try {

            String[] line;
            while ((line = reader.readNext()) != null) {
                System.out.println("Country [id= " + line[0] + ", code= " + line[1] + " , name=" + line[2] + "]");
            }

        } catch (IOException | CsvValidationException i) {
            i.printStackTrace();
        }
    }
*/
    public void closeWriter() {
        try {
            if (this.writer != null) {
                this.writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();

        }
    }
}
