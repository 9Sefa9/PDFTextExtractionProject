package layout;

import extractor.Document;
import extractor.DocumentParser;
import interfaces.Analyzable;
import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.multipdf.PageExtractor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;
import utilities.Helper;
import utilities.KeyValueObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.lang.Character;
import java.util.function.Predicate;

public class Section implements Analyzable {
    private DocumentParser handler;
    private String docName;
    //private Listen, wichtig zur Berechnung.
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    //KAPITEL Handling:

    //beinhaltet Anfangsbuchstaben / Phrasen mit der ein Kapitel beginnen kann. Könnte erweitert werden.
    private final String[] chapterHeaderDefines = {
            "ABSTRACT", "ABSTRACT ", "ABSTRACT-", "ABSTRACT -", "ABSTRACT - ",
            //  "Abstr", "Abstr -", "Abstr - ", "Abstr —", "Abstr — ",
            "Abstract ", "Abstract-", "Abstract -", "Abstract - ",
            "Abstract", "Abstract—", "Abstract— ", "Abstract —", "Abstract — ",
            //mit speziellem nicht sichtbarem Zeichen:
            " Abstract", " Abstract—", " Abstract— ", " Abstract —", " Abstract — ", "Abstract— ",

            "I. ","I. Intro", "I. intro", "I.Intro", "I.intro", "I.INTRO", "I. INTRO",
            "I.  Intro", "I.  intro", "I.  Intro", "I.  intro", "I.  INTRO", "I.  INTRO",
            "I.   Intro", "I.   intro", "I.   Intro", "I.   intro", "I.   INTRO", "I.   INTRO",
            "I. Intri", "I. intri", "I.Intri", "I.intri", "I.INTRI", "I. INTRI",
            "I.  Intri", "I.  intri", "I.  Intri", "I.  intri", "I.  INTRi", "I.  INTRi",
            "I.   Intri", "I.   intri", "I.   Intri", "I.   intri", "I.   INTRI", "I.   INTRI",

            "II. ", "II.", "III. ", "III.", "IV. ", "V. ", "V.", "VI. ", "VI.",
            "VII. ", "VII.", "VIII.", "VIII. ", "IX.", "IX. ", "X.", "X. ",
            //  "i. ", "ii. ", "iii. ", "iv. ", "v. ", "vi. ", "vii. ", "viii. ", "ix. ", "x. ",

            //"i.", "ii.", "iii.", "iv.", "v.", "vi.", "vii.", "viii.", "ix.", "x.",
            "INTROD", "REL", "RES", "DISC", "ACKN", "FUT", "REFERENCE", "REF",
            "REFERENCES", "REFERENCE\n", "REFERENCE \n", "REFERENCES \n",
            "1 ", "2 ", "3 ", "4 ", "5 ", "6 ", "7 ", "8 ", "9 ", "10 ", "11 ", "12 ",
            "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};
    /*
    "1 A","1 B","1 C","1 D","1 E","1 F","1 G","1 H","1 I","1 J","1 K","1 L",
                "1 M","1 N","1 O","1 P","1 Q","1 R","1 S","1 T","1 U","1 V","1 W","1 X",
                "1 Y","1 Z",
     */
    //In der List stehen Kapitel mit: Nummerierung + Titel
    private List<String> detectedChapterHeadersList;
    //die jeweilige Position des gefundenen Headers.
    private List<Integer> detectedChapterPositionsList;


    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    //ABSATZ Handling:

    //beinhaltet Anfangsbuchstaben / Phrasen mit der ein Abschnitt beginnen kann. Könnte erweitert werden.
    private final String[] sectionHeaderDefines = {
            "A. ", "A.", "B. ", "B.", "C. ", "C.", "D. ", "D.", "E. ", "E.", "F. ", "F.", "G. ", "G.", "H. ", "H.", "J.", "J. ", "K. ", "K.","L. ","L."};
    private final String[] blacklistSectionHeaderDefines = {"REFERENCE", "REF",
            "REFERENCES", "REFERENCE\n", "REFERENCE \n", "REFERENCES \n",};

    /* "L. ", "M. ", "N. ", "O. ", "P. ", "Q. ", "R. ", "S. ", "T. ", "U. ", "W. ", "Y. ", "Z. "*/
    //In der List stehen Abschnitte mit: Nummerierung + Titel
    private List<String> detectedSectionHeadersList;
    //die jeweilige Position des gefundenen Headers.
    private List<Integer> detectedSectionPositionsList;


    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    private List<KeyValueObject<List<String>, Document>> chapterList;
    private List<KeyValueObject<List<String>, Document>> sectionList;
    private List<KeyValueObject<List<Integer>, Document>> chapterPositionsList;
    private List<KeyValueObject<List<Integer>, Document>> sectionPositionsList;


    public Section(DocumentParser handler) {
        this.handler = handler;
        chapterList = new ArrayList<>();
        sectionList = new ArrayList<>();
        chapterPositionsList = new ArrayList<>();
        sectionPositionsList = new ArrayList<>();
    }

    /**
     * Analysiert die Kapiteln und Absätze innerhalb des PDF Dokuments.
     */
    @Override
    public void analyze() {
        System.out.println("Entering Section Extraction on " + Thread.currentThread().getName() + " :: " + Thread.currentThread().getId());
        for (Document document : this.handler.getDocumentsList()) {
            String fullText;
            try {
                this.docName = document.getPdfName();

                fullText = document.getPdfText();

                findHeaders(fullText);
                calculatePositions(fullText);

                this.chapterList.add(new KeyValueObject<>(detectedChapterHeadersList, document));
                this.sectionList.add(new KeyValueObject<>(detectedSectionHeadersList, document));
                this.sectionPositionsList.add(new KeyValueObject<>(detectedSectionPositionsList, document));
                this.chapterPositionsList.add(new KeyValueObject<>(detectedChapterPositionsList, document));

            } catch (Exception i) {
                i.printStackTrace();
            }
        }
        System.out.println("Section Extraction done on " + Thread.currentThread().getName() + " :: " + Thread.currentThread().getId());
    }

    /***
     *Die Positionen der einzelnen gefunden headers aus detectedSectionHeaders und detectedChapterHeaders wird in relation
     *zum gesammten Text (fulltext) gebracht. Die gefundenen Positionen der einzelnen Kapitel werden in eine liste abgespeichert.
     *
     *@see #analyze()
     *@param fullText der gesammte Text, ohne jegliche Einschränkungen
     */
    private synchronized void calculatePositions(String fullText) {
        detectedChapterPositionsList = new ArrayList<>();
        detectedSectionPositionsList = new ArrayList<>();

        //Chapter(Kapitel) position bestimmung
        int index;
        for (int j = 0; j < detectedChapterHeadersList.size(); j++) {


            index = fullText.indexOf(detectedChapterHeadersList.get(j));

            if (index != -1) {
                detectedChapterPositionsList.add(index);
            }
        }
        //Section(Absatz) position bestimmung
        for (int j = 0; j < detectedSectionHeadersList.size(); j++) {


            index = fullText.indexOf(detectedSectionHeadersList.get(j));

            if (index != -1) {
                detectedSectionPositionsList.add(index);
            }
        }


    }


    /***
     * Identifiziert die Nummer + Titel eines Kapitels und eines Abschnittes und speichert Sie in die jeweiligen detected Listen ein.
     * @param fullText der gesammte Text, ohne jegliche Einschränkungen
     */
    private synchronized void findHeaders(String fullText) {
        detectedChapterHeadersList = new ArrayList<>();
        detectedSectionHeadersList = new ArrayList<>();

        String[] str = fullText.split("(\r\n|\r|\n|\n\r|\t)");

        //Zu Optiomierungszwecken verwendet, damit die Schleife nicht wieder von vorne beginnt zu überprüfen
        int oldFoundedJChapter = 0;
        for (int i = 0; i + 1 < str.length; i++) {

            //Reguläre Ausdrücke filtern solche, die keine Kapiteln sind.
            if (!str[i].isEmpty() && str[i].length() < 90 && str[i].length() > 3 && (Character.isUpperCase(str[i].charAt(0)) || Character.isDigit(str[i].charAt(0))) && str[i].matches("(.*^[^\\[0-9\\]].*(\\w|[0-9])*[^,.;%$=:]$|.*^[0-9]+\\s*[A-Z]+)")) {

                for (int j = oldFoundedJChapter; j < chapterHeaderDefines.length; j++) {


                    if (str[i].startsWith(chapterHeaderDefines[j]) || str[i].equals(chapterHeaderDefines[j])) {
                        //Wurde ein Kapitel gefunden ? abspeichern unter den erkannten listen.
                        detectedChapterHeadersList.add(str[i]);

                        oldFoundedJChapter =j;
                        break;
                    }

                }
            }
        }
        //VariableLimit sorgt dafür, dass immer nur bis zur Reference nach Abschnitten gesucht werden. Falls kein Reference extrahiert werden konnte, bleibt es bei der normalen Dokumenten Länge str.length.
        int variableLimit = str.length;
        for (int i = 0; i + 1 < variableLimit; i++) {
            //Reguläre Ausdrücke filtern solche, die keine Absätze sind .
            if (i > 20 && !str[i].isEmpty() && str[i].length() < 80 && str[i].length() > 3 && str[i].matches(".*^[^\\[0-9\\]]*[^,\\[\\].]$")) {

                variableLimit = (str[i].startsWith("REFE") ? i : str.length);
                for (int j = 0; j < sectionHeaderDefines.length; j++) {

                    if ((str[i].startsWith(sectionHeaderDefines[j]) || str[i].equals(sectionHeaderDefines[j]))) {
                        //Wurde ein Absatz gefunden ? abspeichern unter den erkannten listen.
                        detectedSectionHeadersList.add(str[i]);

                        break;

                    }
                }
            }

        }

    }
    //Getter und Setter

    public List<KeyValueObject<List<String>, Document>> getChapterList() {
        return this.chapterList;
    }

    public List<KeyValueObject<List<Integer>, Document>> getChapterPositionsList() {
        return this.chapterPositionsList;
    }

    public List<KeyValueObject<List<String>, Document>> getSectionList() {
        return this.sectionList;
    }

    public List<KeyValueObject<List<Integer>, Document>> getSectionPositionsList() {
        return this.sectionPositionsList;
    }

    public List<Integer> getDetectedChapterPositionsList() {
        return this.detectedChapterPositionsList;
    }

    public List<Integer> getDetectedSectionPositionsList() {
        return this.detectedSectionPositionsList;
    }
}