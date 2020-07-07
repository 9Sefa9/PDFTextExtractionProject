package layout;

import extractor.Document;
import extractor.DocumentHandler;
import interfaces.Analyzable;
import org.apache.pdfbox.multipdf.PageExtractor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import org.apache.pdfbox.text.TextPosition;
import utilities.Helper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;


public class Section implements Analyzable {
    private DocumentHandler handler;
    private List<Float> fontSizeList;
    private String[] headersDefines = {"I.", "II.", "III.", "IV.", "V.", "VI.", "VII.", "VIII.", "IX.", "X.",
            "i.", "ii.", "iii.", "iv.", "v.", "vi.", "vii.", "viii.", "ix.", "x.",
            "INTROD", "REL", "RES", "DISC", "ACKN", "REFE", "FUT"};
    private List<String> detectedSectionHeaders;
    private List<Integer> detectedSectionPositions;

    public Section(DocumentHandler handler) {
        this.handler = handler;
    }

    @Override
    public void start() {
        analyze();
    }

    /**
     * Analysiert die vorhanden sections innerhalb des PDF Dokuments.
     */
    @Override
    public void analyze() {
        for (Document document : this.handler.getDocumentsList()) {
            // int middlePage = (int) Math.floor(document.getPdfDocument().getNumberOfPages() / 2f) + 1;
            int lastPage = document.getPdfDocument().getNumberOfPages();
            try {

                PageExtractor extractorObject = new PageExtractor(document.getPdfDocument(), 1, lastPage);
                PDDocument doc = extractorObject.extract();
                document.setPdfDocument(doc);
                document.setPdfTextStripper(new PDFTextStripper());
                String fullText = document.getPdfText();
                findSectionHeaders(fullText);
                calculateSectionLength(fullText);

            } catch (Exception i) {
                i.printStackTrace();
            }
        }

    }

    int wordcount(String string) {
        int count = 0;

        char ch[] = new char[string.length()];
        for (int i = 0; i < string.length(); i++) {
            ch[i] = string.charAt(i);
            if (((i > 0) && (ch[i] != ' ') && (ch[i - 1] == ' ')) || ((ch[0] != ' ') && (i == 0)))
                count++;
        }
        return count;
    }

    private void calculateSectionLength(String fullText) {
        detectedSectionPositions = new ArrayList<>();
        // soutwordcount(fullText);
        for (int i = 0; i < fullText.length(); i++) {
            for (int j = 0; j < detectedSectionHeaders.size(); j++) {
                int index = fullText.indexOf(detectedSectionHeaders.get(j));

                if (index != -1) {
                    // System.out.println(index);
                    detectedSectionPositions.add(index);
                }
            }
        }

    //DURCHBRUCH !! ICh habs geschafft !!!
            System.out.println(fullText.substring(detectedSectionPositions.get(0),detectedSectionPositions.get(1)));
        }


        //Einfacher test, ob wirklich das geprintet wird, was vorher berechnet wurde-TODO Printet nicht ganz ordentlich..
// Es passt nicht zusammen: detected Section Position hat sein eigenes array length, section hat auch, fulltext auch...

  /*      int j = -1;
        for(int i = 0; i<fullText.length();i++){
          //  for(int j = 0; j<detectedSectionHeaders.size();j++) {

                j= (j+1) % detectedSectionHeaders.size();

                System.out.println(fullText.substring(detectedSectionPositions.get(i), detectedSectionPositions.get(i) + detectedSectionHeaders.get(j).length()));

                //break;
            }*/




    private void findSectionHeaders(String fullText) {
        detectedSectionHeaders = new ArrayList<>();
        String[] str = fullText.split("(\r\n|\r|\n)");

        for (int i = 0; i < str.length; i++) {
            if (str[i].length() < 60 && str[i].length() > 10 && str[i].matches(".*[^-,.]$")) {
                for (int j = 0; j < headersDefines.length; j++)
                    if (str[i].startsWith(headersDefines[j])) {
                        //  System.out.println(fullText.indexOf(i)+ "text: "+str[i]);
                        detectedSectionHeaders.add(str[i]);
                        //   System.out.println(str[i]+"\n******");
                        break;
                    }
            }
        }

    }

    private void createFontSizeList(Document document) {

        try {
            PDFTextStripper stripper = new PDFTextStripper() {

                @Override
                protected void writeString(String text, List<TextPosition> textPositions) {
                    for (TextPosition t : textPositions) {
                        //der text.length>1 soll Fälle mit nur 1 Buchstaben ausschließe, die richtig groß sind.
                        if (!text.isEmpty()) {
                            fontSizeList.add(t.getFontSizeInPt());
                        }
                    }
                }
            };
            stripper.getText(document.getPdfDocument());
            //stripper.extractRegions(firstPage);
            //löscht duplikate. z.B.  6.0 6.0 6.0 ...
            this.fontSizeList = fontSizeList.stream().distinct().collect(Collectors.toList());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private float getHighestFontSize(List<Float> textSizes) {
        float minValue = 0;
        for (float f : textSizes) {
            if (f > minValue) {
                minValue = f;
            }
        }
        return minValue;
    }

    private boolean upper(String str) {
        //convert String to char array
        char[] charArray = str.toCharArray();

        for (int i = 0; i < charArray.length; i++) {

            //if any character is not in upper case, return false
            if (!java.lang.Character.isUpperCase(charArray[i]))
                return false;
        }

        return true;
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

