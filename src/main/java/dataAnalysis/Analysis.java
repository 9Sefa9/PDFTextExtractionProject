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
import java.util.*;

public class Analysis implements Analyzable {

    private String[] args;
    private DocumentParser handler;
    private Import imp;


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
        Thread t6 = new Thread(() -> {
            analysis6();
        });
        Thread t5 = new Thread(() -> {
            analysis5();
        });
        Thread t4 = new Thread(() -> {
            analysis4();
        });
        Thread t3 = new Thread(() -> {
            analysis3();
        });
        Thread t2 = new Thread(() -> {
            analysis2();
        });
        Thread t1 = new Thread(() -> {
            analysis1();
        });
        try {
            t1.setName("ANALYSIS 1");
            t2.setName("ANALYSIS 2");
            t3.setName("ANALYSIS 3");
            t4.setName("ANALYSIS 4");
            t5.setName("ANALYSIS 5");
            t6.setName("ANALYSIS 6");

            t1.start();
          //  Thread.sleep(2000);
            t1.join();
            t2.start();
            Thread.sleep(1000);
            t3.start();
            Thread.sleep(1000);
            t4.start();
            Thread.sleep(1000);
            t5.start();
            Thread.sleep(1000);
            t6.start();

            t2.join();
            t3.join();
            t4.join();
            t5.join();
            t6.join();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    //TODO Beinhaltet die erste Analyse:  Anzahl an Abbildungen in Abhängigkeit zu Konferenzen
    private void analysis1() {
        Image image = new Image(handler);
        image.analyze();
        //Erstelle *.csv Datei mit einzigartiger Benennung
        CSV csv = new CSV(args[1].concat("\\analysisAnzahlBilder" + (System.nanoTime() / 100000) + ".csv"));

        csv.writeCSV((String[]) Helper.concatenate(new String[]{""}, handler.getConferenceNames()));

        //Berechne das Maximum aller Abbildung je Konferenz und trage den Maximum in je konferenz ein.
        // int count=0;
        float[] countListPerConference = new float[handler.getConferenceNames().length];
        for (int i = 0; i < handler.getConferenceNames().length; i++) {
            String conferenceName = handler.getConferenceNames()[i];
            //Die Anzahl an Bilder im jeweiligen Konferenz
            float imgCounts = 0;
            //die length einer Konferenz minus 1, weil Durchschnitt berechent werden muss..
            float sizeOfAConference = 0;
            for (int j = 0; j < handler.getDocumentsList().size(); j++) {
                String imgConferenceName = image.getImageCountList().get(j).getValue().getConferenceName();
                int imgCountsCurrent = image.getImageCountList().get(j).getKey();

                if (imgConferenceName.equals(conferenceName)) {
                    imgCounts += imgCountsCurrent;
                    sizeOfAConference += 1;
                }
            }
            //Trage den Durschnitt aller Bilder eines Konferenzes in die Liste.
            countListPerConference[i] += (imgCounts / sizeOfAConference);
        }
        //Integer Array zu String array umwandeln
        String[] finalArray = new String[countListPerConference.length];
        for (int i = 0; i < countListPerConference.length; i++) {
            finalArray[i] = (countListPerConference[i] + "");
            finalArray[i] = finalArray[i].replace('.', ',');
        }
        //   csv.writeCSV((String[]) Helper.concatenate(new String[]{"Anzahl Abbildungen"}, finalArray),'\t', '\\');
        csv.writeCSV((String[]) Helper.concatenate(new String[]{"Anzahl Abbildungen"}, finalArray));
        csv.closeWriter();

    }


    //TODO Beinhaltet die zweite Analyse: Maximale Anzahl an seiten in abhängigkeit zu den Konferenzen.
    private void analysis2() {
        Metadata metadata = new Metadata(handler);
        metadata.analyze();
        //Erstelle *.csv Datei mit einzigartiger Benennung
        CSV csv = new CSV(args[1].concat("\\analysisMaximumsSeitenanzahlPublikationen" + (System.nanoTime() / 100000) + ".csv"));

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
            finalArray[i] = (countListPerConference[i] + ",0");
        }
        csv.writeCSV((String[]) Helper.concatenate(new String[]{"Anzahl Seiten"}, finalArray));
        csv.closeWriter();
    }

    //TODO Beinhaltet die dritte Analyse: Berechnung der Kapitellängen einzelner Dokumente   von einem Kapitel zum Anderen.
    private void analysis3() {
        Section section = new Section(handler);
        section.analyze();

        //TODO von einem Kapitel zum anderen die character zahlen itereieren und abspeichern.
        CSV csv = new CSV(args[1].concat("\\analysisKapitellängeKapitelZuKapitel" + (System.nanoTime() / 100000) + ".csv"));
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

                    //Wenn am Ende der Zeile erreicht, subtrahiere mit Ende der Zeile.
                    if (j == section.getChapterList().get(i).getKey().size() - 1) {
                        introductionPos = eachDocument.getValue().getPdfText().length() - positions.get(j);
                    }
                    //Der index des nächsten Kapitels ist immer größer als der vorherige. Die Bedingung muss daher gelten.
                    if (j + 1 < positions.size() && positions.get(j + 1) > positions.get(j))
                        introductionPos = positions.get(j + 1) - positions.get(j);


                    //else
                    intList.add(introductionPos);
                }
            }
            //chapter name List
            //Äquivalent zur Iteration von k< section.getChapterList().get(i).getKey().size ...  chapterNameList.add(section.getChapterList().get(i).getKey().get(k));
            chapterNameList.addAll(section.getChapterList().get(i).getKey());
            //int list to string array
            String[] tmpIntArr = new String[intList.size()];
            for (int j = 0; j < intList.size(); j++) {
                tmpIntArr[j] = (((int) intList.get(j)) + ",0");
            }

            //Speichert die einzelnen Kapitel Namen.
            csv.writeCSV((String[]) Helper.concatenate(new String[]{""}, Helper.toStringArray(chapterNameList)));
            //Speichert den Namen des PDF's und seine Positionen.
            csv.writeCSV((String[]) Helper.concatenate(new String[]{eachDocument.getValue().getPdfName()}, tmpIntArr));
            csv.writeCSV(new String[]{""});
            chapterNameList.clear();
            intList.clear();
        }
        csv.closeWriter();

    }

    //TODO Beinhaltet die vierte Analyse: Wie die dritte Analyse 3 jedoch mit Unterteilung in relevante bzwq. Irrelevante Kapitel.
    //Die relevanten Kapitel:
    public void analysis4() {
        Section section = new Section(handler);
        section.analyze();

        //TODO von einem Kapitel zum anderen die character zahlen itereieren und abspeichern.
        CSV csv = new CSV(args[1].concat("\\analysisAllgemeinSpezifischKapitel" + (System.nanoTime() / 100000) + ".csv"));
        //Erste Zeile direkt mal definieren..
        csv.writeCSV((String[]) Helper.concatenate(new String[]{""}, new String[]{"Allgemeine Kapitel", "Spezifische Kapitel"}));
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

                    //Wenn am Ende der Zeile erreicht, subtrahiere mit Ende der Zeile.
                    if (j == section.getChapterList().get(i).getKey().size() - 1) {
                        introductionPos = eachDocument.getValue().getPdfText().length() - positions.get(j);
                    }
                    //Der index des nächsten Kapitels ist immer größer als der vorherige. Die Bedingung muss daher gelten.
                    if (j + 1 < positions.size() && positions.get(j + 1) > positions.get(j))
                        introductionPos = positions.get(j + 1) - positions.get(j);


                    //else
                    intList.add(introductionPos);
                }
            }
            //chapter name List
            //Äquivalent zur Iteration von k< section.getChapterList().get(i).getKey().size ...  chapterNameList.add(section.getChapterList().get(i).getKey().get(k));
            chapterNameList.addAll(section.getChapterList().get(i).getKey());
            //int list to string array
            String[] tmpIntArr = new String[intList.size()];
            for (int j = 0; j < intList.size(); j++) {
                tmpIntArr[j] = (((int) intList.get(j)) + ",0");

            }
            //Hier wird die Positionsausfindigung fertig gestellt-

            //Jetzt wird unterteilt zwischen gut und schlecht.
            String[] relevantHeaderDefines = {"Abstr", "INTROD","MOTIVAT", "INTRID", "CONCL", "ACKNOWLED", "REFEREN", "RELATE", "EVALUA", "RESUL", "DISCUSS"};
            float irrelevantHeaderPosAll = 0;
            //Wichtig für die Durschschnittsberechnung
            int irrelevantCount = 0;
            float relevantHeaderPosAll = 0;
            //wichtig für die Durschnitssberechnung
            int relevantCount = 0;
            for (int j = 0; j < chapterNameList.size(); j++) {
                boolean foundRelevant = false;
                for (int k = 0; k < relevantHeaderDefines.length; k++) {

                    //Wenn der ChapterName irrelevant ist:
                    if (!chapterNameList.get(j).trim().contains(relevantHeaderDefines[k])) {
                        foundRelevant = false;
                    }
                    //Ansonsten ist es ein relevanter ChapterName.
                    else {
                        foundRelevant = true;
                        break;
                    }

                }
                if (foundRelevant) {
                    //    System.out.println("RELEVANTER KAPITEL: "+ chapterNameList.get(j).trim());
                    relevantCount += 1;
                    relevantHeaderPosAll += intList.get(j);

                } else {
                    irrelevantCount += 1;
                    irrelevantHeaderPosAll += intList.get(j);
                    //  System.out.println("IRRELEVANTER KAPITEL: "+ chapterNameList.get(j).trim());
                }
            }
            //Der Durschnitt aller summierten, irrelevanten, relevanten Daten!.
            irrelevantHeaderPosAll = (irrelevantHeaderPosAll / irrelevantCount);
            relevantHeaderPosAll = (relevantHeaderPosAll / relevantCount);

            csv.writeCSV((String[]) Helper.concatenate(new String[]{eachDocument.getValue().getPdfName()}, new String[]{((int) relevantHeaderPosAll) + "", ((int) irrelevantHeaderPosAll) + ""}));
            chapterNameList.clear();
            intList.clear();
        }
        csv.closeWriter();
    }


    //TODO Beinhaltet die fünfte  Analyse: Berechnung der Absatzsslängen einzelner Dokumente   von einem Abschnitt zum Anderen.
    private void analysis5() {
        Section section = new Section(handler);
        section.analyze();

        //TODO von einem Kapitel zum anderen die character zahlen itereieren und abspeichern.
        CSV csv = new CSV(args[1].concat("\\analysisAbsatzlängeAbsatzZuAbsatz" + (System.nanoTime() / 100000) + ".csv"));
        List<String> sectionNameList = new ArrayList<>();
        List<Integer> intList = new ArrayList<>();
        int oneTime = 1;
        for (int i = 0; i < handler.getDocumentsList().size(); i++) {

            KeyValueObject<List<Integer>, Document> eachDocument = section.getSectionPositionsList().get(i);
            // positionen der Chapters in Dokument 0:
            List<Integer> positions = eachDocument.getKey();
            for (int j = 0; j < positions.size(); j++) {
                //wenn bis Reference erreicht wurde, dann soll die Position auch nur bis dahin kalkulieren.
                if (j < section.getSectionList().get(i).getKey().size()) {
                    int introductionPos = positions.get(j);

                    //Wenn am Ende der Zeile erreicht, subtrahiere mit Ende der Zeile.
                    if (j == section.getSectionList().get(i).getKey().size() - 1) {
                        introductionPos = eachDocument.getValue().getPdfText().length() - positions.get(j);
                    }
                    //Der index des nächsten Kapitels ist immer größer als der vorherige. Die Bedingung muss daher gelten.
                    if (j + 1 < positions.size() && positions.get(j + 1) > positions.get(j))
                        introductionPos = positions.get(j + 1) - positions.get(j);


                    //else
                    intList.add(introductionPos);
                }
            }
            //section name List
            //Äquivalent zur Iteration von k< section.getChapterList().get(i).getKey().size ...  chapterNameList.add(section.getChapterList().get(i).getKey().get(k));
            sectionNameList.addAll(section.getSectionList().get(i).getKey());
            //int list to string array
            String[] tmpIntArr = new String[intList.size()];
            float averageSection = 0;
            for (int j = 0; j < intList.size(); j++) {
                tmpIntArr[j] = (((int) intList.get(j)) + ",0");
                averageSection += intList.get(j);
            }

            //Speichere den Durchschnitte aller berechneten Abschnitte die zum nächsten Abschnitt gehen und pack es als 1. spalte.
            averageSection /= tmpIntArr.length;
            //Speichert die einzelnen Abschnitts Namen.
            csv.writeCSV((String[]) Helper.concatenate(new String[]{""}, oneTime == 1 ? new String[]{"Durschnitt"} : new String[]{""}, Helper.toStringArray(sectionNameList)));
            oneTime = 0;
            //Speichert den Namen des PDF's und seine Positionen.
            csv.writeCSV((String[]) Helper.concatenate(new String[]{eachDocument.getValue().getPdfName()}, new String[]{((int) averageSection) + ""}, tmpIntArr));
            csv.writeCSV(new String[]{""});
            sectionNameList.clear();
            intList.clear();
        }
        csv.closeWriter();
    }

    //TODO Beinhaltet die sechste Analyse: Die "Allgemeinen"-Kapitel in ihre  BEstandteile zerlegt und separat aufgezeigt => nicht PDF entsprechend !
    private void analysis6() {
        Section section = new Section(handler);
        section.analyze();

        //TODO von einem Kapitel zum anderen die character zahlen itereieren und abspeichern.
        CSV csv = new CSV(args[1].concat("\\analysisAllgemeinSplittedKapitel" + (System.nanoTime() / 100000) + ".csv"));


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

                    //Wenn am Ende der Zeile erreicht, subtrahiere mit Ende der Zeile.
                    if (j == section.getChapterList().get(i).getKey().size() - 1) {
                        introductionPos = eachDocument.getValue().getPdfText().length() - positions.get(j);
                    }
                    //Der index des nächsten Kapitels ist immer größer als der vorherige. Die Bedingung muss daher gelten.
                    if (j + 1 < positions.size() && positions.get(j + 1) > positions.get(j))
                        introductionPos = positions.get(j + 1) - positions.get(j);


                    //else
                    intList.add(introductionPos);
                }
            }
            //chapter name List
            //Äquivalent zur Iteration von k< section.getChapterList().get(i).getKey().size ...  chapterNameList.add(section.getChapterList().get(i).getKey().get(k));
            chapterNameList.addAll(section.getChapterList().get(i).getKey());
            //int list to string array
            String[] tmpIntArr = new String[intList.size()];
            for (int j = 0; j < intList.size(); j++) {
                tmpIntArr[j] = (((int) intList.get(j)) + ",0");

            }
            //Hier wird die Positionsausfindigung fertig gestellt. JETZT:

            //Identifizeren, welche allgemin und welche spezfiische Kapitel ist.

        }
        //Kategorisiere nun die jeweiligen Header zu den array lists.  Also :
        // Arralist relevantHeaderLengths
        // ArrayList 1 = ALle Abstrakte.
        // ArrayList 2 = Alle Itroduction.
        // ....
        // bis RelevantHeaderDefines.
        //  Helper.print(chapterNameList);
        //Ab hier beginnt die aussortierung von Allgemein zu Sekundär.
        String[] relevantHeaderDefines = {"Abstr", "INTROD", "INTRID", "RELATE", "BACKG", "APPROA", "CONCLUS", "ACKNOWLED", "REFEREN", "EVALUA", "RESUL", "DISCUSS"};
        ArrayList<ArrayList<String>> relevantHeaderLengths = new ArrayList<>();


        for (int i = 0; i < relevantHeaderDefines.length; i++) {
            relevantHeaderLengths.add(new ArrayList<String>());
        }

        for (int j = 0; j < chapterNameList.size(); j++) {

            boolean found = false;
            int foundRelevantIndex = 0;

            //selektiere alle irrelevanten und relevanten Daten.
            for (int k = 0; k < relevantHeaderDefines.length; k++) {

                //Wenn der ChapterName irrelevant ist:
                if (!chapterNameList.get(j).trim().contains(relevantHeaderDefines[k])) {
                    foundRelevantIndex = k;
                    found = false;

                }
                //Ansonsten ist es ein relevanter ChapterName.
                else {
                    foundRelevantIndex = k;
                    found = true;

                    break;
                }

            }


            if (found) {
                relevantHeaderLengths.get(foundRelevantIndex).add(intList.get(j) + "");
            }
            if (!found) {
                // System.out.println(chapterNameList.get(j));
            }

        }

        //Schreibe nun die Daten entsprechend in die Zeilen! nicht in die Spalte!(Wollte mal auch was anderes probieren...
        for (int i = 0; i < relevantHeaderLengths.size(); i++) {
            String[] preparedArray = new String[relevantHeaderLengths.get(i).size()];
            for (int j = 0; j < relevantHeaderLengths.get(i).size(); j++) {

                //bereite preparedArray Vor, um in die csv abzuspeichern
                preparedArray[j] = relevantHeaderLengths.get(i).get(j);
            }
            //  csv.writeCSV(Helper.toStringArray(relevantHeaderLengths.get(0)));
            csv.writeCSV((String[]) Helper.concatenate(new String[]{relevantHeaderDefines[i]}, preparedArray));
        }


        chapterNameList.clear();
        intList.clear();

        csv.closeWriter();
    }


    //TODO beinhaltet die siebte Analyse:
    private void analysis7() {
        Section section = new Section(handler);
        section.analyze();

        //TODO von einem Kapitel zum anderen die character zahlen itereieren und abspeichern.
        CSV csv = new CSV(args[1].concat("\\analysisAllgemeinSplittedKapitel" + (System.nanoTime() / 100000) + ".csv"));


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

                    //Wenn am Ende der Zeile erreicht, subtrahiere mit Ende der Zeile.
                    if (j == section.getChapterList().get(i).getKey().size() - 1) {
                        introductionPos = eachDocument.getValue().getPdfText().length() - positions.get(j);
                    }
                    //Der index des nächsten Kapitels ist immer größer als der vorherige. Die Bedingung muss daher gelten.
                    if (j + 1 < positions.size() && positions.get(j + 1) > positions.get(j))
                        introductionPos = positions.get(j + 1) - positions.get(j);

                    //else
                    intList.add(introductionPos);
                }
            }
            //chapter name List
            //Äquivalent zur Iteration von k< section.getChapterList().get(i).getKey().size ...  chapterNameList.add(section.getChapterList().get(i).getKey().get(k));
            chapterNameList.addAll(section.getChapterList().get(i).getKey());
            //int list to string array
            String[] tmpIntArr = new String[intList.size()];
            for (int j = 0; j < intList.size(); j++) {
                tmpIntArr[j] = (((int) intList.get(j)) + ",0");

            }
            //Hier wird die Positionsausfindigung fertig gestellt. JETZT:

            //Identifizeren, welche allgemin und welche spezfiische Kapitel ist.

        }
        //Kategorisiere nun die jeweiligen Header zu den array lists.  Also :
        // Arralist relevantHeaderLengths
        // ArrayList 1 = ALle Abstrakte.
        // ArrayList 2 = Alle Itroduction.
        // ....
        // bis RelevantHeaderDefines.
        //  Helper.print(chapterNameList);
        //Ab hier beginnt die aussortierung von Allgemein zu Sekundär.
        String[] relevantHeaderDefines = {"Abstr", "INTROD", "INTRID", "RELATE", "BACKG", "APPROA", "CONCLUS", "ACKNOWLED", "REFEREN", "EVALUA", "RESUL", "DISCUSS"};
        ArrayList<ArrayList<String>> relevantHeaderLengths = new ArrayList<>();


        for (int i = 0; i < relevantHeaderDefines.length; i++) {
            relevantHeaderLengths.add(new ArrayList<String>());
        }

        for (int j = 0; j < chapterNameList.size(); j++) {

            boolean found = false;
            int foundRelevantIndex = 0;

            //selektiere alle irrelevanten und relevanten Daten.
            for (int k = 0; k < relevantHeaderDefines.length; k++) {

                //Wenn der ChapterName irrelevant ist:
                if (!chapterNameList.get(j).trim().contains(relevantHeaderDefines[k])) {
                    foundRelevantIndex = k;
                    found = false;

                }
                //Ansonsten ist es ein relevanter ChapterName.
                else {
                    foundRelevantIndex = k;
                    found = true;

                    break;
                }

            }


            if (found) {
                relevantHeaderLengths.get(foundRelevantIndex).add(intList.get(j) + "");
            }

        }

        //Schreibe nun die Daten entsprechend in die Zeilen! nicht in die Spalte!(Wollte mal auch was anderes probieren...
        for (int i = 0; i < relevantHeaderLengths.size(); i++) {
            String[] preparedArray = new String[relevantHeaderLengths.get(i).size()];
            for (int j = 0; j < relevantHeaderLengths.get(i).size(); j++) {

                //bereite preparedArray Vor, um in die csv abzuspeichern
                preparedArray[j] = relevantHeaderLengths.get(i).get(j);
            }
            //  csv.writeCSV(Helper.toStringArray(relevantHeaderLengths.get(0)));
            csv.writeCSV((String[]) Helper.concatenate(new String[]{relevantHeaderDefines[i]}, preparedArray));
        }


        chapterNameList.clear();
        intList.clear();

        csv.closeWriter();
    }

    //TODO beinhaltet die fünfte Analyse:
    private void analysis8() {

    }

    //TODO beinhaltet die sechste Analyse:
    private void analysis9() {

    }
}
