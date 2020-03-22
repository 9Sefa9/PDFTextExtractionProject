package layout;

import interfaces.Storable;

import java.awt.geom.Rectangle2D;
import java.util.HashMap;

public class Word implements Storable {
    public static HashMap<Rectangle2D,Integer> wordsAndOccurencesMap = new HashMap<>();
}
