package main;

import analyze.Analyze;
import interfaces.Extractable;
import io.Import;

/**
 * Author Credentials
 *
 * @author Sefa Gövercin - 3030095 - sefa.goevercin@stud.uni-due.de
 * @version 1.0
 */
public class Main {
    public static void main(String[] args) {
        Extractable imp= new Import();
        Extractable analyze = new Analyze();
        if (args.length == 0)
            System.out.println("No Argument found!\nUsage: java -jar <dirPath>");
        if (args.length > 1)
            System.out.println("Too many Arguments found!\nUsage: java -jar <dirPath>");
        if (args.length == 1) {
            System.err.println("START EXTRACTION");
            imp.importDocument(args[0]);

        }
    }
}
