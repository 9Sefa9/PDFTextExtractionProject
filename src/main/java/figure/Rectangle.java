package figure;

import exception.DrawException;
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
    private void printFAKE(){
        try {
            File file1 = new File("src\\main\\resources\\colored08662658.pdf");
            pdfDocument.save(file1);
            pdfDocument.close();
        } catch (FileNotFoundException f) {

            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            File file1 = new File("src\\main\\resources\\colored08662658.pdf");
            try {
                pdfDocument.save(file1);
                pdfDocument.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
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
            // }



        } catch (IOException | DrawException i) {
            i.printStackTrace();
        }
    }
}
