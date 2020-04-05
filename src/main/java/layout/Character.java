package layout;

import java.awt.geom.Rectangle2D;
import java.util.HashMap;

public class Character extends Word{
    //INteger ist die Seite Zahl, wo sich der Character befindet.
    //Rectangle2D die Coordinates des characters.
    public static HashMap<Rectangle2D,Integer> charactersBoxCoordinatesMap = new HashMap<>();
}
