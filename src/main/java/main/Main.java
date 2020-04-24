package main;

import extractor.DocumentHandler;
import interfaces.Extractable;
import io.Import;

/**
 * Author Credentials
 *
 * @author Sefa Gövercin - 3030095 - sefa.goevercin@stud.uni-due.de
 * @version 1.0
 */
public class Main {
    //"src\\main\\resources\\08662658.pdf";
    public static void main(String[] args) {
        Extractable imp = new Import();
        DocumentHandler handler = new DocumentHandler();
        if (args.length == 0)
            System.out.println("No Argument found!\nUsage: java -jar <dirPath>");
        if (args.length > 1)
            System.out.println("Too many Arguments found!\nUsage: java -jar <dirPath>");
        if (args.length == 1) {
            System.err.println("START EXTRACTION");
            imp.importDocument(handler,args[0]);

        }
    }
}
