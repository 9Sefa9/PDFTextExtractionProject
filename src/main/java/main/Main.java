package main;

import dataAnalysis.Analysis;
import extractor.DocumentHandler;
import interfaces.Analyzable;
import interfaces.Extractable;
import io.CSV;
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
        long now;
        System.out.println("Completion Time: "+(now = System.nanoTime()));
        switch (args.length){
            case 0:{System.out.println("No arguments found!\nUsage: java -jar <DIR PATH OF FOLDER CONTAINING DOCUMENTS or DIR PATH OF ONLY 1 PDF DOCUMENT> <SAVEPATH FOR CSV EXPORT>");break;}
            case 1:{System.out.println("Only 1 argument found!\nUsage: java -jar <DIR PATH OF FOLDER CONTAINING DOCUMENTS or DIR PATH OF ONLY 1 PDF DOCUMENT> <SAVEPATH FOR CSV EXPORT>");break; }
            case 2:{
                Analyzable a = new Analysis(args);
                a.analyze();
                break;
            }
            default:{
                System.out.println("Too many Arguments / No dir / No PDF found!\nUsage: java -jar <DIR PATH OF FOLDER CONTAINING DOCUMENTS or DIR PATH OF ONLY 1 PDF DOCUMENT> <SAVEPATH FOR CSV EXPORT>");
            }
        }
        System.out.println("Completion Time: "+(System.nanoTime() - now)/1000000000);

    }

}
