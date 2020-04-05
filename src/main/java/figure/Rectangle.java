package figure;

import exception.DrawException;
import extractor.Document;
import interfaces.Drawable;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import static layout.Character.charactersBoxCoordinatesMap;

public class Rectangle extends Drawable {
    public Rectangle(float x, float y, float width, float height, Color color, PDDocument pdfDocument, int page) {
        super(x, y, width, height, color, pdfDocument, page);

    }
    @Override
    // private void drawCharactersBoundingBox()
    public void draw() {
        try {
            if (Document.getPdfDocument() == null)
                throw new DrawException("PDDocument is null!");

            setPdPage(Document.getPdfDocument().getPage(getPageNumber()));
            setContentStream(new PDPageContentStream(Document.getPdfDocument(), getPdPage(), true, true));

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
