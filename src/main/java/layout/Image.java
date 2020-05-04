package layout;

import extractor.Document;
import extractor.DocumentHandler;
import interfaces.Analyzable;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSObjectKey;
import org.apache.pdfbox.cos.COSStream;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Image implements Analyzable {
    private DocumentHandler handler;

    public Image(DocumentHandler handler) {
        this.handler = handler;
    }

    @Override
    public void analyze() {
        for (Document document : this.handler.getDocumentsList()) {
            try {
                for (PDPage page : document.getPdfDocument().getPages()) {
                    PDResources pdResources = page.getResources();
                    for (COSName c : pdResources.getXObjectNames()) {
                        PDXObject po = pdResources.getXObject(c);
                        PDImageXObject pio= (PDImageXObject) po;
                        long timestamp = System.nanoTime();
                        //Die breite und höhe beträgt 20. Bei kleineren werten kommen winzige Bilder hinzu, die
                        //kein Sinn haben.
                          if (pio != null && pio.getWidth()>20f && pio.getHeight()>20f) {
                             // System.out.println("G:/Users/Progamer/Desktop/TEST/" + System.nanoTime() + ".p"+" width: "+pio.getWidth() +"  height: "+pio.getHeight());
                            File file = new File("G:/Users/Progamer/Desktop/TEST/" + timestamp + ".png");
                            ImageIO.write(pio.getImage(), "png", file);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void start() {
        analyze();
    }
}
