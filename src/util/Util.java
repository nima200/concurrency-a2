package util;

import java.util.Random;

public class Util {
    private static final Random rand = new Random();

    public static float randFloat(float min, float max) {
        return rand.nextFloat() * (max - min) + min;
    }

    public static int randInt(int min, int max) {
        return rand.nextInt((max - min) + 1) + min;
    }
}
