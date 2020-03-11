package main;

import extractor.PDFExtractor;

import java.io.IOException;

public class Main {
    public static void main(String[] args){

        PDFExtractor p = new PDFExtractor();
        try {
            p.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
