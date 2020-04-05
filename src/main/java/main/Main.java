package main;

import extractor.PLAYGROUND;

import java.io.IOException;

/**Author Credentials
 * @author Sefa Gövercin - 3030095 - sefa.goevercin@stud.uni-due.de
 * @version 1.0
 */
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
