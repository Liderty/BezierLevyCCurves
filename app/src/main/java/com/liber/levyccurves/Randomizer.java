package com.liber.levyccurves;

public class Randomizer {
    static private int COLOR_MAX = 255;

    public static int generate(int min, int max) {
        return min + (int)(Math.random() * ((max - min) + 1));
    }

    public static String getRandomColor() {
        String color = "#";

        String red = Integer.toHexString(generate(0, COLOR_MAX));
        if(red.length() == 1) red = "0" + red;
        String green = Integer.toHexString(generate(0, COLOR_MAX));
        if(green.length() == 1) green = "0" + green;
        String blue = Integer.toHexString(generate(0, COLOR_MAX));
        if(blue.length() == 1) blue = "0" + blue;

        return color + red + green + blue;
    }
}