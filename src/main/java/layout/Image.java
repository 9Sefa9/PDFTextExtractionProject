package layout;

import extractor.Document;
import extractor.DocumentParser;
import interfaces.Analyzable;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import utilities.KeyValueObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Image implements Analyzable {
    private DocumentParser handler;
  //  private List<KeyValueObject<BufferedImage, Document>> imagesList;
    private List<KeyValueObject<Integer, Document>> imageCountList;

    public Image(DocumentParser handler) {

        this.handler = handler;
      //  imagesList = new ArrayList<>();
        this.imageCountList = new ArrayList<>();
    }

    @Override
    public void analyze() {
        System.out.println("Entering Image Extraction on "+Thread.currentThread().getName()+" :: "+Thread.currentThread().getId());
        int imageCount;
        PDResources pdResources;
        PDXObject po;
        PDImageXObject pio;
        for (Document document : this.handler.getDocumentsList()) {
            try {
                imageCount = 0;
                for (PDPage page : document.getPdfDocument().getPages()) {
                    pdResources = page.getResources();
                    for (COSName c : pdResources.getXObjectNames()) {
                        po = pdResources.getXObject(c);
                        pio=null;// = (PDImageXObject) po;
                        if(po instanceof PDImageXObject){
                            pio = ((PDImageXObject)po);

                            if (po != null || pio != null) {
                                //Die breite und höhe soll mindestens 20 betragen. Bei kleineren werten kommen winzige Bilder hinzu und sind in Publikationen
                                //nicht korrekt interpretierbar.
                                if (pio.getWidth() > 50f && pio.getHeight() > 50f) {
                                    // unbedingt die folgenden kommentare auskommentieren, sonst werden die Bilder abgespeichert.
                                 //   File file = new File("C:/Users/MaxMustermann/Desktop/ABbilderOrdner/" + System.nanoTime() / 1000 + ".png");
                                 //   BufferedImage img = pio.getImage();
                                 //   ImageIO.write(img, "png", file);


                                    imageCount += 1;

                                }
                            }
                        }
                    }

                }
                this.imageCountList.add(new KeyValueObject<Integer, Document>(imageCount, document));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Entering Image Extraction on "+Thread.currentThread().getName()+" :: "+Thread.currentThread().getId());
    }

    public List<KeyValueObject<Integer, Document>> getImageCountList() {
        return this.imageCountList;
    }
}
