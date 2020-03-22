package interfaces;
//Vorlage um eine Klasse zu representieren, dass zeichnen kann.
public abstract class Drawable {
    float x,y,width,height;
    //rect, dies das...
    abstract void draw(float x, float y, float width, float height);
}
