package main;

import extractor.Document;
import extractor.DocumentHandler;
import interfaces.Analyzable;
import interfaces.Extractable;
import io.Export;
import io.Import;
import layout.*;
import layout.Character;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSStream;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.font.PDFont;

import java.io.IOException;
import java.io.PrintWriter;

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
            System.err.println("START EXTRACTION");
            Extractable imp = new Import();
            DocumentHandler handler = new DocumentHandler();
            imp.importDocument(handler,args[0]);
            Analyzable analyze[] = {new Title(handler)};
            analyze[0].start();
        }
    }

}
