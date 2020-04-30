package figure;

import exception.DrawException;
import extractor.Document;
import interfaces.Drawable;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import java.awt.*;

import java.io.IOException;

public class Rectangle extends Drawable {
    public Rectangle(float x, float y, float width, float height, Color color, PDDocument pdfDocument, int page) {
        super(x, y, width, height, color, pdfDocument, page);
    }
    public
    Rectangle(double x, double y, float width, float height, Color color, PDDocument pdfDocument, int page) {
        super(x, y, width, height, color, pdfDocument, page);
    }
    /**
     * Zeichnet ein rechteck in ein PDF Document.
     */
    @Override
    //Malt etwas entsprechend der Koordinaten, die in super Klasse gesetzt wurden.
    // private void drawCharactersBoundingBox()
    public void draw() {
        try {
            if (getPdfDocument() == null)
                throw new DrawException("PDDocument is null!");

            setPdPage(getPdfDocument().getPage(getPageNumber()));
            setContentStream(new PDPageContentStream(getPdfDocument(), getPdPage(), true, true));

            if (getContentStream() == null)
                throw new DrawException("Content Stream Error!");

            getContentStream().addRect(getX(), getY(), getWidth(), getHeight());/*(float) map.getKey().getBounds2D().getX(), (float) map.getKey().getBounds2D().getY(), (float) map.getKey().getWidth(), (float) map.getKey().getHeight()*/

            getContentStream().setStrokingColor(getColor());
            getContentStream().stroke();
            getContentStream().close();

        } catch (IOException | DrawException i) {
            i.printStackTrace();
        }
    }
}
