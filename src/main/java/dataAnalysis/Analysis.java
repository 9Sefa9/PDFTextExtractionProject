package dataAnalysis;

import extractor.Document;
import extractor.DocumentParser;
import interfaces.Analyzable;
import io.CSV;
import io.Import;
import layout.*;
import layout.Character;
import utilities.Helper;

import java.io.IOException;
import java.util.HashMap;

public class Analysis implements Analyzable {

    private String[] args;
    private DocumentParser handler;
    private Import imp;
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

    //"src\\main\\resources\\sectionClass.csv"
    @Override
    public void analyze() {
        imp = new Import();
        handler = new DocumentParser();
        imp.importDocument(handler, args[0]);
        section = new Section(handler);
        metadata = new Metadata(handler);
        character = new Character(handler);
        word = new Word(handler);
        image = new Image(handler);
        literature = new Literature(handler);
//TODO irgendwie funktionieren image und literature nicht gemeinsam...
        Analyzable[] analyzableDocument = {literature,image,section,metadata,character,word};
        for (Analyzable a : analyzableDocument) {
            a.analyze();
        }

        analysis1();
        //  analysis2();

      //  closeAllPDF();
    }

    private void closeAllPDF() {
    for(Document doc : this.handler.getDocumentsList()){
        try {
            if (doc != null)
                doc.getPdfDocument().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    }

    //TODO beinhaltet die erste Analyse:  Anzahl an Abbildungen in Abhängigkeit zu Konferenzen
    private void analysis1() {

        //Erstelle *.csv Datei mit einzigartiger Benennung
        csv = new CSV(args[1].concat("\\analysisOne" + (System.nanoTime() / 1000) + ".csv"));

        csv.writeCSV((String[]) Helper.concatenate(new String[]{""}, handler.getConferenceNames()));

        //Berechne das Maximum aller Abbildung je Konferenz und trage den Maximum in je konferenz ein.
       // int count=0;
        int[] countListPerConference = new int[handler.getConferenceNames().length];
        for (int i = 0; i < handler.getConferenceNames().length; i++) {
            String conferenceName = handler.getConferenceNames()[i];
            for (int j = 0; j <image.getImageCountList().size(); j++) {
                String imgConferenceName = image.getImageCountList().get(j).getValue().getConferenceName();
                Integer imgCounts = image.getImageCountList().get(j).getKey();
                if(imgConferenceName.equals(conferenceName)){
                    countListPerConference[i]+= imgCounts;
                }
            }
        }
        //Integer Array zu String array umwandeln
        String[] finalArray = new String[countListPerConference.length];
        for (int i = 0; i <countListPerConference.length; i++) {
            finalArray[i] = (countListPerConference[i]+"");
        }
        csv.writeCSV((String[]) Helper.concatenate(new String[]{"Anzahl Abbildungen"}, finalArray));
        csv.closeWriter();

    }

    //beinhaltet die zweite Analyse: Maximale Wörter und Buchstaben in einer Konferenz
    private void analysis2() {
        csv = new CSV(args[1].concat("\\analysisTwo" + (System.nanoTime() / 1000) + ".csv"));
        //Die Konkatenation ist wichitg, da die erste Zelle in der ersten Saplte leer sein muss.
        csv.writeCSV((String[]) Helper.concatenate(new String[]{""}, Helper.toStringArray(metadata.getTitlesList())));

        //Einzelne Wörter samt ihrer vorkommen
        String[] newEntry = new String[metadata.getTitlesList().size()];
        int index = 1;
        for (HashMap<String, Integer> currentDocumentWordOccurence : word.getWordOccurenceList()) {
            newEntry[0] = "Wissenschaftliches Dokument " + index;
            index += 1;
            for (String w : currentDocumentWordOccurence.keySet()) {
                //    newEntry
            }
        }
        //  String []imageCountArray = new String[image.getImageCountList().size()];
        //   for (int i = 0; i < image.getImageCountList().size(); i++)
        //      imageCountArray[i] = image.getImageCountList().get(i).toString();
//TODO Brauch ich dass denn überhaupt das mit dem Wörtern ?
        // csv.writeCSV((String[])Helper.concatenate(new String[]{"Anzahl der Wörter"},word.));

        //    csv.closeWriter();
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
//plus 1, weil die 0te Stelle den Namen des Dokumenten hat.
     /*   String[] preparedString = new String[image.getImagesList().size() + 1];
        preparedString[0] = "Anzahl Abbildungen";

        for (int i = 0; i < image.getImageCountList().size(); i++) {
            for (int j = 1; j < image.getImageCountList().size(); j++) {
                if(image.getImageCountList().get(j).getValue().equals(preparedString[0])){
                    preparedString[j] = image.getImageCountList().get(j).getKey().toString();
                    i+=1;
                }

            }
            csv.writeCSV(preparedString);
            preparedString = new String[image.getImagesList().size() + 1];
            // Helper.concatenate(new String[]{image.getImageCountList().get(j).getValue()},image.getImageCountList().get(i).getKey());
        }

      */
//Die Konkatenation ist wichitg, da die erste Zelle in der ersten Saplte leer sein muss.
      /*  csv.writeCSV((String[])Helper.concatenate(new String[]{""},Helper.toStringArray(metadata.getTitlesList())));

        String []imageCountArray = new String[image.getImageCountList().size()];
        for (int i = 0; i < image.getImageCountList().size(); i++)
            imageCountArray[i] = image.getImageCountList().get(i).toString();

        csv.writeCSV((String[])Helper.concatenate(new String[]{"Anzahl Abbildungen"},imageCountArray));

       csv.closeWriter();
*/