package io;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CSV {
    private CSVWriter writer;
    private FileWriter fileWriter;
    private CSVReader reader;
    private FileReader fileReader;


    public CSV(String commonPath){
        try {
            this.fileWriter = new FileWriter(commonPath);
            this.fileReader = new FileReader(commonPath);

            this.writer = new CSVWriter(fileWriter,'\t',
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
    public void writeCSV(List<String[]> values) {
        writer.writeAll(values,true);
    }

    public synchronized void closeWriter() {
        try {
            if (this.writer != null) {
                this.writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();

        }
    }
}
