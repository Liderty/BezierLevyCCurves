package com.liber.levyccurves;

public class Point {
    double x,y;

    Point(Point p)
    {
        x = p.x;
        y = p.y;
    }

    Point(int _x, int _y)
    {
        x = _x;
        y = _y;
    }

    Point()
    {
        x = 0;
        y = 0;
    }

    void copy(Point p)
    {
        x = p.x;
        y = p.y;
    }

    void print() {
        System.out.println("Point:("+x+", "+y+")");
    }
}
