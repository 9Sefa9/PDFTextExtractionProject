package layout;

import extractor.Document;
import extractor.DocumentHandler;
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
    private DocumentHandler handler;
    private List<KeyValueObject<BufferedImage, String>> imagesList;
    private List<KeyValueObject<Integer, String>> imageCountList;

    public Image(DocumentHandler handler) {
        this.handler = handler;
        imagesList = new ArrayList<>();
        imageCountList = new ArrayList<>();
    }

    @Override
    public void analyze() {
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
                                    BufferedImage img = pio.getImage();
                               //     ImageIO.write(img, "png", file);

                                    this.imagesList.add(new KeyValueObject<BufferedImage, String>(img, document.getParentName()));
                                    imageCount += 1;

                                }
                            }
                        }
                    }

                }
                this.imageCountList.add(new KeyValueObject<Integer, String>(imageCount, document.getParentName()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public List<KeyValueObject<BufferedImage, String>> getImagesList() {
        return this.imagesList;
    }

    public List<KeyValueObject<Integer, String>> getImageCountList() {
        return this.imageCountList;
    }
}
