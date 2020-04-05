package interfaces;

import extractor.Document;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import java.awt.*;

//Vorlage um eine Klasse zu representieren, dass zeichnen kann.
public abstract class Drawable implements PDFX{
    //Hauptinformationen einer geometrischen Figur
    protected float x,y,width,height;
    protected Color color;
    protected PDPageContentStream contentStream;
    protected PDPage pdPage;
    protected int pageNumber;
    public Drawable(float x, float y, float width, float height, Color color, PDDocument pdfDocument, int pageNumber) {
        setX(x);
        setY(y);
        setWidth(width);
        setHeight(height);
        setColor(color);
        Document.setPdfDocument(pdfDocument);
        setPageNumber(pageNumber);
    }


    protected abstract void draw();
    protected void setColor(Color color){
        this.color = color;
    };
    protected void setX(float x){ this.x = x;}
    protected void setY(float y){ this.y = y;}
    protected void setWidth(float width){ this.width = width;}
    protected void setHeight(float height){ this.height = height;}
    protected void setPageNumber(int pageNumber) { this.pageNumber = pageNumber;}
    protected void setPdPage(PDPage pdPage){this.pdPage=pdPage;}
    protected void setContentStream(PDPageContentStream contentStream){this.contentStream = contentStream;}
    protected Color getColor(){return this.color;}
    protected float getX(){return this.x;}
    protected float getY(){return this.y;}
    protected float getWidth(){return this.width;}
    protected float getHeight(){return this.height;}
    protected int getPageNumber(){return this.pageNumber;}
    protected PDPage getPdPage(){return this.pdPage;}
    protected PDPageContentStream getContentStream(){return this.contentStream;}

}
