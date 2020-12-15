package com.liber.levyccurves;

public class Randomizer {
    private static String[] mColors = {
            "#39add1", // light blue
            "#3079ab", // dark blue
            "#c25975", // mauve
            "#e15258", // red
            "#f9845b", // orange
            "#838cc7", // lavender
            "#7d669e", // purple
            "#53bbb4", // aqua
            "#51b46d", // green
            "#e0ab18", // mustard
            "#637a91", // dark gray
            "#f092b0", // pink
            "#b7c0c7"  // light gray
    };

    public static int generate(int min, int max) {
        return min + (int)(Math.random() * ((max - min) + 1));
    }

    public static String getRandomColor() {
        String color = "";
        int randomNumber = generate(0, mColors.length-1);
        color = mColors[randomNumber];
        return color;
    }
}