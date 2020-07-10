package dataAnalysis;

import extractor.DocumentHandler;
import interfaces.Analyzable;
import interfaces.Extractable;
import io.CSV;
import io.Import;
import layout.*;
import layout.Character;

public class Analysis implements Analyzable {

    private String[] args;
    private Metadata metadata;
    private CSV csv;
    private Section section;
    private Character character;
    private Word word;
    private Image image;
    private Literature literature;


    public Analysis(String[] args) {
        this.args = args;
    }

    @Override
    public void analyze() {
        Extractable imp = new Import();
        DocumentHandler handler = new DocumentHandler();
        imp.importDocument(handler, args[0]);
        //"src\\main\\resources\\sectionClass.csv"

        metadata = new Metadata(handler);
        section = new Section(handler);
        character = new Character(handler);
        word = new Word(handler);
        image = new Image(handler);
        literature = new Literature(handler);
        Analyzable[] analyzableDocument = {metadata,section,character,word,image,literature};
        for (Analyzable a : analyzableDocument) {
            a.analyze();
        }
        analysis1();
        analysis2();
    }

    private void analysis1() {
        csv = new CSV(args[1].concat("\\analysisOne"+(System.nanoTime()/1000)+".csv"));
        //beinhaltet die erste Analyse z.B Anzahl an Abbildungen in Konferenzen
        for(String s: metadata.getTitlesList())
            System.out.println(s);
    }

    private void analysis2() {

    }

}
/*
     String[]titlesArray = new String[this.titles.size()];
        titles.toArray(titlesArray);

        csv.writeCSV(titlesArray);
        csv.closeWriter();

 */




/*
String [] detectedChapterHeadersArray = new String[detectedSectionHeaders.size()];
            String [] test = (String[]) detectedChapterHeaders.toArray(detectedChapterHeadersArray);
            csv.writeCSV(test);
            csv.closeWriter();
 */