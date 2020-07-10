package dataAnalysis;

import extractor.DocumentHandler;
import interfaces.Analyzable;
import interfaces.Extractable;
import io.CSV;
import io.Import;
import layout.*;
import layout.Character;
import utilities.Helper;

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
    //beinhaltet die erste Analyse: Konferenzen in Abhängigkeit zu Anzahl an Abbildungen
    private void analysis1() {

        csv = new CSV(args[1].concat("\\analysisOne"+(System.nanoTime()/1000)+".csv"));
        //Die Konkatenation ist wichitg, da die erste Zelle in der ersten Saplte leer sein muss.
        csv.writeCSV((String[])Helper.concatenate(new String[]{""},Helper.toStringArray(metadata.getTitlesList())));

        String []imageCountArray = new String[image.getImageCountList().size()];
        for (int i = 0; i < image.getImageCountList().size(); i++)
            imageCountArray[i] = image.getImageCountList().get(i).toString();

        csv.writeCSV((String[])Helper.concatenate(new String[]{"Anzahl Abbildungen"},imageCountArray));

        csv.closeWriter();

    }
    //beinhaltet die zweite Analyse: Maximale Wörter und Buchstaben in einer Konferenz
    private void analysis2() {
        csv = new CSV(args[1].concat("\\analysisTwo"+(System.nanoTime()/1000)+".csv"));
        //Die Konkatenation ist wichitg, da die erste Zelle in der ersten Saplte leer sein muss.
        csv.writeCSV((String[])Helper.concatenate(new String[]{""},Helper.toStringArray(metadata.getTitlesList())));

      //  String []imageCountArray = new String[image.getImageCountList().size()];
     //   for (int i = 0; i < image.getImageCountList().size(); i++)
      //      imageCountArray[i] = image.getImageCountList().get(i).toString();
//TODO Brauch ich dass denn überhaupt das mit dem Wörtern ?
        csv.writeCSV((String[])Helper.concatenate(new String[]{"Anzahl der Wörter"},word.));

        csv.closeWriter();
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