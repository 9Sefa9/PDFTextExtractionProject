package figure;

import interfaces.Drawable;

public class Rectangle extends Drawable {
    @Override
    public void draw(float x, float y, float width, float height) {
        super.x = x;
        super.y = y;
        super.width = width;
        super.height = height;
    }
}
