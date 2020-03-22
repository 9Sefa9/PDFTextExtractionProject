package main;

import extractor.PLAYGROUND;

import java.io.IOException;

public class Main {
    public static void main(String[] args){

        PLAYGROUND p = new PLAYGROUND();
        try {
            p.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
