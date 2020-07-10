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
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Image implements Analyzable {
    private DocumentHandler handler;
    public List<BufferedImage> imagesList;
    public Image(DocumentHandler handler) {
        this.handler = handler;
        imagesList = new ArrayList<>();
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
                        //Die breite und höhe beträgt 20. Bei kleineren werten kommen winzige Bilder hinzu, die
                        //sinngemäß kein Teil des wissenschaftlichen Dokuments sind.
                          if (pio != null && pio.getWidth()>20f && pio.getHeight()>20f) {
                             // System.out.println("G:/Users/Progamer/Desktop/TEST/" + System.nanoTime() + ".p"+" width: "+pio.getWidth() +"  height: "+pio.getHeight());
                            File file = new File("G:/Users/Progamer/Desktop/TEST/" + System.nanoTime() + ".png");
                            ImageIO.write(pio.getImage(), "png", file);
                              imagesList.add(pio.getImage());
                          }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public List<BufferedImage> getImagesList(){
        return this.imagesList;
    }

}
