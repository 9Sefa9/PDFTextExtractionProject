package main;

import dataAnalysis.Analysis;
import interfaces.Analyzable;

/**
 * Author Credentials
 *
 * @author Sefa Gövercin - 3030095 - sefa.goevercin@stud.uni-due.de
 * @version 1.0
 */
public class Main {
    //I:\Informatik\10.Semester\Bachelorarbeit\DatenAnsammlung
    //"src\\main\\resources\\08662658.pdf";
    public static void main(String[] args) {
        java.util.logging.Logger.getLogger("org.apache.pdfbox").setLevel(java.util.logging.Level.SEVERE);
        long now = System.nanoTime();
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
