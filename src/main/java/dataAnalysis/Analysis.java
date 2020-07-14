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

        Analyzable[] analyzableDocument = {metadata,literature,image,section,character,word};
        for (Analyzable a : analyzableDocument) {
            a.analyze();
        }

        analysis1();
        analysis2();
        analysis3();
    }

    //Beinhaltet die erste Analyse:  Anzahl an Abbildungen in Abhängigkeit zu Konferenzen
    private void analysis1() {

        //Erstelle *.csv Datei mit einzigartiger Benennung
        csv = new CSV(args[1].concat("\\analysisOne" + (System.nanoTime() / 1000) + ".csv"));

        csv.writeCSV((String[]) Helper.concatenate(new String[]{""}, handler.getConferenceNames()));

        //Berechne das Maximum aller Abbildung je Konferenz und trage den Maximum in je konferenz ein.
       // int count=0;
        int[] countListPerConference = new int[handler.getConferenceNames().length];
        for (int i = 0; i < handler.getConferenceNames().length; i++) {
            String conferenceName = handler.getConferenceNames()[i];
            for (int j = 0; j <handler.getDocumentsList().size(); j++) {
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

    //beinhaltet die zweite Analyse: Maximale Anzahl an seiten in abhängigkeit zu den Konferenzen.
    private void analysis2() {
        //Erstelle *.csv Datei mit einzigartiger Benennung
        csv = new CSV(args[1].concat("\\analysisTwo" + (System.nanoTime() / 1000) + ".csv"));

        csv.writeCSV((String[]) Helper.concatenate(new String[]{""}, handler.getConferenceNames()));

        //Berechne das Maximum aller Seiten je Konferenz und trage diese ein.
        // int count=0;
        int[] countListPerConference = new int[handler.getConferenceNames().length];
        for (int i = 0; i < handler.getConferenceNames().length; i++) {
            //Die Konferenzen
            String conferenceName = handler.getConferenceNames()[i];
            for (int j = 0; j <handler.getDocumentsList().size(); j++) {
                //vergleich ob die Liste aus Handler dem gleichen konferenz entspricht des dokuments.
                String conferenceNameFromHandler = handler.getDocumentsList().get(j).getConferenceName();
                int docCounts = metadata.getPageSizesList().get(j);
                //falls ja, summiere die anzahl vorhandener Seiten einzelner Dokumente und speichere es zur bearbeitung in die Array.
                if(conferenceNameFromHandler.equals(conferenceName)){
                    countListPerConference[i]+= docCounts;
                }
            }
        }
        //Integer Array zu String array umwandeln, da opencsv nur arrays haben möchte.
        String[] finalArray = new String[countListPerConference.length];
        for (int i = 0; i <countListPerConference.length; i++) {
            finalArray[i] = (countListPerConference[i]+"");
        }
        csv.writeCSV((String[]) Helper.concatenate(new String[]{"Anzahl Seiten"}, finalArray));
        csv.closeWriter();
    }

    //TODO beinhaltet die dritte Analyse: Berechnung der Kapitellängen einzelner Konferenzen. (substring?)
    private void analysis3(){

    }
    //TODO beinhaltet die vierte Analyse: Berechnung der maximalen buchstaben bzw. Wörter einzelner Konferenzen.(zwei zeilen + konfeerenz namen..) (substring?)
    private void analysis4(){

    }
    //TODO beinhaltet die fünfte Analyse: Berechnung der Abschnittslänge von einem Abschnitt zum anderen(Kapüitelübergreifen) (substring?)
    private void analysis5(){

    }
    //TODO beinhaltet die sechste Analyse: statistische Berechnung wie z.B Berechnung der prozentualen Anteil.
    private void analysis6(){

    }
}
