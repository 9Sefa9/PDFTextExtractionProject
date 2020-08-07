package layout;

import extractor.Document;
import extractor.DocumentParser;
import interfaces.Analyzable;
import jdk.internal.jimage.ImageStrings;
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


public class Section implements Analyzable {
    private DocumentParser handler;

    //private Listen, wichtig zur Berechnung.
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++


    //beinhaltet Anfangsbuchstaben / Phrasen mit der ein Kapitel beginnen kann. Könnte erweitert werden.
    private final String[] chapterHeaderDefines = {
            "I. ", "II. ", "III. ", "IV. ", "V. ", "VI. ", "VII. ", "VIII. ", "IX. ", "X. ",
            "i. ", "ii. ", "iii. ", "iv. ", "v. ", "vi. ", "vii. ", "viii. ", "ix. ", "x. ",
            "ABSTRACT","ABSTRACT ","ABSTRACT-", "ABSTRACT -", "ABSTRACT - ",
            "Abstr", "Abstr ", "Abstr -", "Abstr - ", "Abstr —", "Abstr — ",
            "Abstract ", "Abstract-", "Abstract -", "Abstract - ",
            "Abstract", "Abstract—", "Abstract —", "Abstract — ",
            //mit speziellem nicht sichtbarem Zeichen:
            "Abstract", "Abstract—", "Abstract —", "Abstract — ",
            "INTROD", "REL", "RES", "DISC", "ACKN", "REFERENCE",
            "REFERENCES", "REFERENCE\n", "REFERENCE \n", "REFERENCES \n", "FUT",
            "1 ", "2 ", "3 ", "4 ", "5 ", "6 ", "7 ", "8 ", "9 ", "10 ", "11 ", "12 "};
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


    //beinhaltet Anfangsbuchstaben / Phrasen mit der ein Abschnitt beginnen kann. Könnte erweitert werden.
    private final String[] sectionHeaderDefines = {"A. ", "B. ", "C. ", "D. ", "E. ", "F. ", "G. ", "H. ", "J. ", "K. "
            /* "L. ", "M. ", "N. ", "O. ", "P. ", "Q. ", "R. ", "S. ", "T. ", "U. ", "W. ", "Y. ", "Z. "*/};
    //In der List stehen Abschnitte mit: Nummerierung + Titel
    private List<String> detectedSectionHeadersList;
    //die jeweilige Position des gefundenen Headers.
    private List<Integer> detectedSectionPositionsList;


    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    //Öffentlich zugänglich:
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
     * Analysiert die vorhanden sections innerhalb des PDF Dokuments.
     */
    @Override
    public void analyze() {
        System.out.println("Entering Section Extraction on " + Thread.currentThread().getName() + " :: " + Thread.currentThread().getId());
        for (Document document : this.handler.getDocumentsList()) {
            try {

                System.out.println(document.getPdfName());
                document.setPdfTextStripper(new PDFTextStripper() {
                    @Override
                    protected void writeString(String text, List<TextPosition> textPositions) throws IOException {
                        StringBuilder br = new StringBuilder();
                        //  if(!text.isEmpty() && text.contains("Abstr")) {
                            br.append(text);
                        //  }
                        // br.append("FONTSIZEPTR:"+ textPositions.get(0).getFontSizeInPt()+" :: FONTSIZE:"+textPositions.get(0).getFontSize()+"]\n");
                        super.writeString(br.toString(), textPositions);
                    }
                });

                String fullText = document.getPdfText();
           //     System.out.println(fullText);
                //Überschriften, Abschnitte und ihre Positionen ausfindig machen in Abhängigkeit zum fullText.
                findHeaders(fullText);
                calculatePositions(fullText);

                this.chapterList.add(new KeyValueObject<>(detectedChapterHeadersList, document));
                this.sectionList.add(new KeyValueObject<>(detectedSectionHeadersList, document));
                this.sectionPositionsList.add(new KeyValueObject<>(detectedSectionPositionsList, document));
                this.chapterPositionsList.add(new KeyValueObject<>(detectedChapterPositionsList, document));

                // System.out.println(fullText+"\n*******************************************************************");
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

        //  for (int i = 0; i < fullText.length(); i++) {
        //Chapter position bestimmung
        for (int j = 0; j < detectedChapterHeadersList.size(); j++) {
            int index = fullText.indexOf(detectedChapterHeadersList.get(j));

            if (index != -1) {
                detectedChapterPositionsList.add(index);
            }
        }
        //Section position bestimmung
        for (int j = 0; j < detectedSectionHeadersList.size(); j++) {
            int index = fullText.indexOf(detectedSectionHeadersList.get(j));

            if (index != -1) {
                detectedSectionPositionsList.add(index);
            }
        }
        // }

    }


    /***
     * Identifiziert die Nummer + Titel eines Kapitels und eines Abschnittes und speichert Sie in die jeweiligen detected Listen ein.
     * @param fullText der gesammte Text, ohne jegliche Einschränkungen
     */
    private synchronized void findHeaders(String fullText) {
        detectedChapterHeadersList = new ArrayList<>();
        detectedSectionHeadersList = new ArrayList<>();
        // System.out.println(fullText);
        String[] str = fullText.split("(\r\n|\r|\n|\n\r)");
        int count = 0;
        for (int i = 0; i < str.length; i++) {
            str[i] = str[i].replaceAll(".*[\\\\/$§#*~{}^()=:;*\"\\[\\]\n\t]", " ").trim();
            if (str[i].length() < 90 && str[i].length() > 9 && str[i].matches(".*[^,.{}:^~/#]$")) {
                //System.out.println(str[i]+"\n+*+++**+++***+");

                for (int j = 0; j < chapterHeaderDefines.length; j++) {

                    if (str[i].startsWith(chapterHeaderDefines[j])) {
                        if (java.lang.Character.isDigit(str[i].charAt(0))) {
                            if (java.lang.Character.isUpperCase(str[i].charAt(2))) {
                                System.out.println("1    "+ str[i]+ "\n+*+++**+++***+");
                                detectedChapterHeadersList.add(str[i]);
                                break;
                            //    count += 1;

                            }
                        } else if (java.lang.Character.isUpperCase(str[i].charAt(0))) {

                                    System.out.println("2      "+str[i]+"\n+*+++**+++***+");
                                    break;

                            }
                        }

                    }
                 /*   if(str[i].startsWith(chapterHeaderDefines[j]) && (java.lang.Character.isUpperCase(str[i].charAt(1)) || java.lang.Character.isUpperCase(str[i].charAt(2)))){
                        System.out.println("zweiter IF:"+str[i]+""+" length: "+str[i].length()+"\n+*+++**+++***+");
                        detectedChapterHeadersList.add(str[i]);
                    }*/

                }
                for (int j = 0; j < sectionHeaderDefines.length; j++) {
                    if (str[i].startsWith(sectionHeaderDefines[j])) {
                        detectedSectionHeadersList.add(str[i]);
                        break;
                    }
                }

            }
        }
       // System.out.println(count);


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


//  System.out.println(s);
//   for (int i = 0; i < str.length; i++) {
//      if (str[i] != null && !str[i].isEmpty()) {
//           if (str[i].length() < 60 && str[i].length() > 10 && str[i].startsWith("I")&&str[i].matches(".*[^-,.]$")) {
//       sectionCandidates.add(str[i]);
//            }
//        }
//  }

      /*
        try {
            Document.getPdfDocument().pdfTextStripper = new PDFTextStripper() {
                @Override
                protected void writeString(String text, List<TextPosition> textPositions) throws IOException {
                    Document.getPdfDocument().pdfTextStripper.setParagraphStart("\t");
                    StringBuilder builder = new StringBuilder();
                    //Wird zum erstellen eines csv Files verwendet.
                    //\w matched alle char's die kein word sind. any non-word character.
                    wordsAndOccurencesMap.merge(text.replaceAll("\\W", "").toLowerCase(Locale.ENGLISH), 1, Integer::sum);
                    for (TextPosition t : textPositions) {
                        //Zum Umranden der einzelnen Character.
                        PDFont font = t.getFont();
                        BoundingBox boundingBox = font.getBoundingBox();
                        Rectangle2D.Float rect = new Rectangle2D.Float(boundingBox.getLowerLeftX(), boundingBox.getUpperRightY(), boundingBox.getWidth(), boundingBox.getHeight());
                        AffineTransform affineTransform = t.getTextMatrix().createAffineTransform();
                        affineTransform.scale(1 / 1000f, 1 / 1000f);
                        Shape shape = affineTransform.createTransformedShape(rect);
                        // Invertierung der Y - Achse.  Notwendig, da java in 2D User Space arbeitet. Und nicht in PDF User Space.
                        Rectangle2D bounds = shape.getBounds2D();
                        bounds.setRect(bounds.getX(), bounds.getY() - bounds.getHeight(), bounds.getWidth(), bounds.getHeight());

                        //Wird zum zeichnen verwendet.
                        charactersBoxCoordinatesMap.put(bounds, getCurrentPageNo());


                        if (builder.length() == 0) {
                            // builder.append("[[[FontSize:"+t.getFontSizeInPt()+" || "+t.getPageWidth()+ "]]]  ");
                        }
                        if (!t.equals(-1)) {
                            builder.append(t);
                        }

                    }
                    String newText = builder.toString();
                    super.writeString(newText, textPositions);
                }
            };

            //Wichtig, damit die initialisierung aufgerufen wird!
            this.pdfDocumentText = pdfTextStripper.getText(this.pdfDocument);

        } catch (IOException e) {
            e.printStackTrace();
        }*/


//Überlegung:  Es existieren wörter, die in der Mitte kein Sinn ergeben. z.b But:  Bu    t
//ERste Vorüberlegung wäre: wenn die anzahl an brüchigen Wörtern ab einem bestimmten Faktor hoch sind => zwei abschnitte vorhanden.
//Ansonsten handelt es sich um Fließtext.
//Ich könnte aber auch teorethisch untersuchen ob es silben trennungen gibt wie :   pedestri-
//keine ahnung. Muss ich noch mal schauen...
//  private void linksRechtsSectionBoundingArea() {
      /*
        try {
            float width = Document.getPdfDocument().getPage(0).getMediaBox().getWidth();
            float height = Document.getPdfDocument().getPage(0).getMediaBox().getHeight();
            StringBuilder pdfText = new StringBuilder();
            PDFTextStripperByArea stripper = new PDFTextStripperByArea();

            stripper.setSortByPosition(true);

            Rectangle2D rectLeft = new Rectangle2D.Float(0, 0, width / 2, height);

            Rectangle2D rectRight = new Rectangle2D.Float(width / 2, 0, width / 2, height);

            stripper.addRegion("leftColumn", rectLeft);

            stripper.addRegion("rightColumn", rectRight);

            PDPageTree allPages = Document.getPdfDocument().getDocumentCatalog().getPages();
            int pageNumber = Document.getPdfDocument().getNumberOfPages();


            String leftText = "";
            String rightText = "";

            for (int i = 0; i < pageNumber; i++) {

                PDPage page = allPages.get(i);

                stripper.extractRegions(page);
                leftText = stripper.getTextForRegion("leftColumn");
                rightText = stripper.getTextForRegion("rightColumn");

                pdfText.append(leftText);
                pdfText.append(rightText);
            }
            System.out.println(pdfText.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
//  }

