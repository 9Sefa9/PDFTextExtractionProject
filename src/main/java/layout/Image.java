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

import java.awt.image.BufferedImage;
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
        for (Document document : this.handler.getDocumentsList()) {
            try {
                int imageCount = 0;
                for (PDPage page : document.getPdfDocument().getPages()) {
                    PDResources pdResources = page.getResources();
                    for (COSName c : pdResources.getXObjectNames()) {
                        PDXObject po = pdResources.getXObject(c);
                        PDImageXObject pio=null;// = (PDImageXObject) po;
                        if(po instanceof PDImageXObject){
                            pio = ((PDImageXObject)po);

                            if (po != null || pio != null) {
                                //Die breite und höhe beträgt 20. Bei kleineren werten kommen winzige Bilder hinzu
                                if (pio.getWidth() > 50f && pio.getHeight() > 50f) {
                                    // nbedingt beide kommentare auskommentieren, wenn die Bilder abgespeichert werden sollen auf Desktop.
                                //    File file = new File("G:/Users/Sefa/Desktop/TEST/" + System.nanoTime() / 1000 + ".png");
                                //    BufferedImage img = pio.getImage();
                               //     ImageIO.write(img, "png", file);

                                  //  this.imagesList.add(new KeyValueObject<BufferedImage, Document>(img, document));
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

  //  public List<KeyValueObject<BufferedImage, Document>> getImagesList() {
   //     return this.imagesList;
 //   }

    public List<KeyValueObject<Integer, Document>> getImageCountList() {
        return this.imageCountList;
    }
}
