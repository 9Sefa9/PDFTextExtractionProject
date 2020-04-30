package main;

import extractor.DocumentHandler;
import interfaces.Analyzable;
import interfaces.Extractable;
import io.Import;
import layout.*;
import layout.Character;

/**
 * Author Credentials
 *
 * @author Sefa Gövercin - 3030095 - sefa.goevercin@stud.uni-due.de
 * @version 1.0
 */
public class Main {
    //"src\\main\\resources\\08662658.pdf";
    public static void main(String[] args) {

        if (args.length == 0)
            System.out.println("No Argument found!\nUsage: java -jar <dirPath>");
        if (args.length > 1)
            System.out.println("Too many Arguments found!\nUsage: java -jar <dirPath>");
        if (args.length == 1) {
            float jetzt = System.currentTimeMillis();
            Extractable imp = new Import();
            DocumentHandler handler = new DocumentHandler();
            imp.importDocument(handler,args[0]);
            Analyzable analyzableDocument[] = {new Metadata(handler), new Character(handler), new Word(handler)};
            for (Analyzable a : analyzableDocument) {
                a.start();
            }
            System.out.println((System.currentTimeMillis() - jetzt)/1000);
        }
    }

}
