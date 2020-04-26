package layout;

import extractor.Document;
import extractor.DocumentHandler;
import interfaces.Analyzable;
import org.apache.fontbox.util.BoundingBox;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;
import utilities.Helper;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static layout.Character.charactersBoxCoordinatesMap;

public class Title extends Analyzable {
    private DocumentHandler documentHandler;

    public Title(DocumentHandler handler) {
        this.documentHandler = handler;
    }

    //@TODO schleife nötig, der in Document handler, alle PDFs durchgreift und analysiert.
    @Override
    protected void analyze() {
        try {
            for (Document document : documentHandler.getDocumentsList()) {
                document.setPdfTextStripper(new PDFTextStripper() {
                    @Override
                    protected void writeString(String text, List<TextPosition> textPositions) throws IOException {
                        setParagraphStart("\t");

                        super.writeString(text, textPositions);
                    }
                });
                //Wichtig, damit die initialisierung aufgerufen wird! wird aber in Document -> print bereits gemacht.
                //this.pdfDocumentText = pdfTextStripper.getText(this.pdfDocument);
                document.print();
                Helper.delimiter();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start() {
        analyze();
    }
}
