package com.liber.levyccurves;

public class Point {
    double x, y;

    Point(int _x, int _y) {
        x = _x;
        y = _y;
    }

    Point() {
        x = 0;
        y = 0;
    }

    void print() {
        System.out.println("Point:("+x+", "+y+")");
    }
}
