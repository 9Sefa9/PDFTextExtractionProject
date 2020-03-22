package interfaces;

import java.awt.*;

//Vorlage um eine Klasse zu representieren, dass zeichnen kann.
public abstract class Drawable implements PDFX{
    //Hauptinformationen einer geometrischen Figur
    protected float x,y,width,height;
    protected Color color;
    //rect, dies das...

    public Drawable(){
        //default color
        setColor(Color.RED);

    }
    protected abstract void draw(float x, float y, float width, float height);
    protected void setColor(Color color){
        this.color = color;
    };

}
