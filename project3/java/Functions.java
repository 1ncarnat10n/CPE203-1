import java.util.List;
import java.util.Optional;
import java.util.Random;

import processing.core.PImage;

final class Functions {
    public static final Random rand = new Random();


    public static int clamp(int value, int low, int high) {
        return Math.min(high, Math.max(value, low));
    }

}
