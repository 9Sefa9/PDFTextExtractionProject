package dataAnalysis;

import extractor.Document;
import extractor.DocumentParser;
import interfaces.Analyzable;
import io.CSV;
import io.Import;
import layout.*;
import layout.Character;
import utilities.Helper;
import utilities.KeyValueObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

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


        //   character = new Character(handler);
        //   word = new Word(handler);
        //    image = new Image(handler);
        //    literature = new Literature(handler);

        //       Analyzable[] analyzableDocument = {metadata,literature,character,word};
        //      for (Analyzable a : analyzableDocument) {
        //           a.analyze();
        //      }

        //   analysis1();
        //   analysis2();
       // analysis3();
        section = new Section(handler);
        section.analyze();
        section.getChapterList().forEach(e->e.getKey().forEach(System.out::println));
        section.getChapterList().forEach(e-> System.out.println(e.getKey().size()));
    }


    //Beinhaltet die erste Analyse:  Anzahl an Abbildungen in Abhängigkeit zu Konferenzen
    private void analysis1() {
        image = new Image(handler);
        image.analyze();
        //Erstelle *.csv Datei mit einzigartiger Benennung
        csv = new CSV(args[1].concat("\\analysisOne" + (System.nanoTime() / 1000) + ".csv"));

        csv.writeCSV((String[]) Helper.concatenate(new String[]{""}, handler.getConferenceNames()));

        //Berechne das Maximum aller Abbildung je Konferenz und trage den Maximum in je konferenz ein.
        // int count=0;
        int[] countListPerConference = new int[handler.getConferenceNames().length];
        for (int i = 0; i < handler.getConferenceNames().length; i++) {
            String conferenceName = handler.getConferenceNames()[i];
            for (int j = 0; j < handler.getDocumentsList().size(); j++) {
                String imgConferenceName = image.getImageCountList().get(j).getValue().getConferenceName();
                Integer imgCounts = image.getImageCountList().get(j).getKey();
                if (imgConferenceName.equals(conferenceName)) {
                    countListPerConference[i] += imgCounts;
                }
            }
        }
        //Integer Array zu String array umwandeln
        String[] finalArray = new String[countListPerConference.length];
        for (int i = 0; i < countListPerConference.length; i++) {
            finalArray[i] = (countListPerConference[i] + "");
        }
        csv.writeCSV((String[]) Helper.concatenate(new String[]{"Anzahl Abbildungen"}, finalArray));
        csv.closeWriter();

    }


    //beinhaltet die zweite Analyse: Maximale Anzahl an seiten in abhängigkeit zu den Konferenzen.
    private void analysis2() {
        metadata = new Metadata(handler);
        metadata.analyze();
        //Erstelle *.csv Datei mit einzigartiger Benennung
        csv = new CSV(args[1].concat("\\analysisTwo" + (System.nanoTime() / 1000) + ".csv"));

        csv.writeCSV((String[]) Helper.concatenate(new String[]{""}, handler.getConferenceNames()));

        //Berechne das Maximum aller Seiten je Konferenz und trage diese ein.
        // int count=0;
        int[] countListPerConference = new int[handler.getConferenceNames().length];
        for (int i = 0; i < handler.getConferenceNames().length; i++) {
            //Die Konferenzen
            String conferenceName = handler.getConferenceNames()[i];
            for (int j = 0; j < handler.getDocumentsList().size(); j++) {
                //vergleich ob die Liste aus Handler dem gleichen konferenz entspricht des dokuments.
                String conferenceNameFromHandler = handler.getDocumentsList().get(j).getConferenceName();
                int docCounts = metadata.getPageSizesList().get(j);
                //falls ja, summiere die anzahl vorhandener Seiten einzelner Dokumente und speichere es zur bearbeitung in die Array.
                if (conferenceNameFromHandler.equals(conferenceName)) {
                    countListPerConference[i] += docCounts;
                }
            }
        }
        //Integer Array zu String array umwandeln, da opencsv nur arrays haben möchte.
        String[] finalArray = new String[countListPerConference.length];
        for (int i = 0; i < countListPerConference.length; i++) {
            finalArray[i] = (countListPerConference[i] + "");
        }
        csv.writeCSV((String[]) Helper.concatenate(new String[]{"Anzahl Seiten"}, finalArray));
        csv.closeWriter();
    }


    //TODO beinhaltet die dritte Analyse: Berechnung der Kapitellängen einzelner Dokumente   von einem Kapitel zum Anderen. (substring?)
    private void analysis3() {
        section = new Section(handler);
        section.analyze();

        //TODO von einem Kapitel zum anderen die character zahlen itereieren und abspeichern.
        csv = new CSV(args[1].concat("\\analysisThree" + (System.nanoTime() / 1000) + ".csv"));
        List<String> chapterNameList = new ArrayList<>();
        List<Integer> intList = new ArrayList<>();

        for (int i = 0; i < handler.getDocumentsList().size(); i++) {

            KeyValueObject<List<Integer>, Document> eachDocument = section.getChapterPositionsList().get(i);
            // positionen der Chapters in Dokument 0:
            List<Integer> positions = eachDocument.getKey();
            for (int j = 0; j < positions.size(); j++) {
                //wenn bis Reference erreicht wurde, dann soll die Position auch nur bis dahin kalkulieren.
                if (j < section.getChapterList().get(i).getKey().size()) {
                    int introductionPos = positions.get(j);
                    //Der index des nächsten Kapitels ist immer größer als der vorherige. Die Bedingung muss daher gelten.
                    if (j + 1 < positions.size() && positions.get(j + 1) > positions.get(j))
                        introductionPos = positions.get(j + 1) - positions.get(j);
                    //else
                    intList.add(introductionPos);
                }
            }
            //chapter name List
            for (int k = 0; k < section.getChapterList().get(i).getKey().size(); k++) {
                chapterNameList.add(section.getChapterList().get(i).getKey().get(k));
            }
            //int lsit to string array
            String[] tmpIntArr = new String[intList.size()];
            for (int j = 0; j < intList.size(); j++) {
                tmpIntArr[j] = intList.get(j) + "";
            }
            csv.writeCSV((String[]) Helper.concatenate(new String[]{""}, Helper.toStringArray(chapterNameList)));
            csv.writeCSV((String[]) Helper.concatenate(new String[]{eachDocument.getValue().getPdfName()}, tmpIntArr));
            csv.writeCSV(new String[]{""});
            chapterNameList.clear();
            intList.clear();
        }


        csv.closeWriter();

    }

    //TODO beinhaltet die vierte Analyse: Berechnung der maximalen buchstaben bzw. Wörter einzelner Konferenzen.(zwei zeilen + konfeerenz namen..) (substring?)
    private void analysis4() {

    }

    //TODO beinhaltet die fünfte Analyse: Berechnung der Abschnittslänge von einem Abschnitt zum anderen(Kapüitelübergreifen) (substring?)
    private void analysis5() {

    }

    //TODO beinhaltet die sechste Analyse: statistische Berechnung wie z.B Berechnung der prozentualen Anteil.
    private void analysis6() {

    }
}

 /*
        //  String[] finalPreparedChapterPositions = new String[preparedChapterPositions.size()];
        //  for (int i = 0; i < preparedChapterPositions.size(); i++) {
        //       finalPreparedChapterPositions[i] = preparedChapterPositions.get(i);
        //   }


        //Kapitelzahlen - Erste Zeile...
        //String currentConferenceName = handler.getConferenceNames()[i];

            ArrayList<String> documentNamesList = new ArrayList<>();
            //ALle Dokumente mit jeweiliger KonferenName - zweite Zeile...
            for (int j = 0; j < handler.getDocumentsList().size(); j++) {
                if(currentConferenceName.equals(handler.getDocumentsList().get(j).getConferenceName())){
                    documentNamesList.add(handler.getDocumentsList().get(j).getPdfName());
                }
            }


            String[] finalDocumentNames = new String[documentNamesList.size()];
            //ArrayList in ein StringArray umwandeln
            for (int j = 0; j < documentNamesList.size(); j++) {
                finalDocumentNames[j] = documentNamesList.get(j);
            }

            //Das entspricht der zweiten Zeile. Also Leerzeile, Dok1, Dok2, Dok 3.. von dem jeweiligen Konferenz.
            csv.writeCSV((String[]) Helper.concatenate(new String[]{""},finalDocumentNames));

            //jetzt die dritte und die nachfolgenden Zeilen.. Also I., II., usw.


            //csv.writeCSV((String[]) Helper.concatenate(new String[]{"Kapitelname"},new String[]{currentConferenceName}));
      //  }
        csv.closeWriter();

        String[] documentNameArray = new String[handler.getDocumentsList().size()];
        //jeden Dokumentnamen in ein Array abspeichern
        for (int i = 0; i < handler.getDocumentsList().size(); i++) {
            documentNameArray[i] = handler.getDocumentsList().get(i).getPdfName()+" "+handler.getDocumentsList().get(i).getConferenceName();
        }
        //schreibe jeden Namen des Dokuments + des Konferenzen in der ersten Zeile. Zeile 1 Spalte 1 bleibt leer wie üblich.
        csv.writeCSV((String[]) Helper.concatenate(new String[]{""},documentNameArray ));

        //Nun in jede Zeile : den position des Kapitels (I) des ersten Dokuments, des zweiten des dritten usw.
        String []testArr = new String[section.getChapterList().size()];

        for (int i = 0; i < section.getChapterPositionsList().get(i).getKey().size(); i++) {
            testArr[i] = ""+section.getChapterPositionsList().get(i).getKey().get(i);
        }
        csv.writeCSV((String[]) Helper.concatenate(new String[]{"Dokument 1"},testArr));
        csv.closeWriter();

        for (int i = 0; i < section.getChapterPositionsList().get(i).getKey().size(); i++) {
            System.out.println(section.getChapterPositionsList().get(i).getValue().getPdfName()+" "+section.getChapterPositionsList().get(i).getKey().get(i));
        }
        //Berechne das Maximum aller Seiten je Konferenz und trage diese ein.
        // int count=0;
    //    System.out.println("section.getChapterPositionsList().size(): "+section.getChapterPositionsList().size());

     //   int[] sectionLengthPerConference = new int[handler.getConferenceNames().length];
     //   for(int i = 0; i<section.getChapterList().size();i++){
    //        for (int j = 0; j < section.g; j++) {

    //        }
    //    }
        //~++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

        for (int i = 0; i < handler.getConferenceNames().length; i++) {

            String conferenceName = handler.getConferenceNames()[i];
            

            for (int j = 0; j < section.getChapterPositionsList().size(); j++) {

                if (j + 1 < section.getChapterPositionsList().get(j).getKey().size()) {
                    if (conferenceName.equals(section.getChapterPositionsList().get(j).getValue().getConferenceName()) && conferenceName.equals(section.getChapterPositionsList().get(j + 1).getValue().getConferenceName())) {

                        sectionLengthPerConference[i] = section.getChapterPositionsList().get(j + 1).getKey().get(j + 1) - section.getChapterPositionsList().get(j).getKey().get(j);
                        break;
                    }
                } else {
                    sectionLengthPerConference[i] = section.getChapterPositionsList().get(j).getKey().get(j);
                    break;
                }
            }
        }


        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        //Integer Array zu String array umwandeln, da opencsv nur arrays haben möchte.
        //    String[] finalArray = new String[sectionLengthPerConference.length];
        //    for (int i = 0; i < sectionLengthPerConference.length; i++) {
        //        finalArray[i] = (sectionLengthPerConference[i] + "");
        //   }
        //    csv.writeCSV((String[]) Helper.concatenate(new String[]{"Kapitel 1"}, finalArray));
        //   csv.closeWriter();
*/