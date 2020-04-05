package io;

import extractor.Document;
import interfaces.Extractable;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class Import extends Document implements Extractable {
    public void importFile(String path){
        String absolutePath="";
        try {
            absolutePath = Paths.get(path).toAbsolutePath().toString();
            pdfDocument = PDDocument.load(new File(absolutePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
