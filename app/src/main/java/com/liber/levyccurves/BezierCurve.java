package com.liber.levyccurves;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

public class BezierCurve {
    public List<Point> controlPoints;
    public int curveWidth;
    public String curveColor;

    BezierCurve(int width, String color) {
        curveColor = color;
        curveWidth = width;
        controlPoints = new ArrayList<>();
    };

    public List<Point> getPoints() {
        return controlPoints;
    }

    public void addPoint(Point point) {
        controlPoints.add(point);
    }

    public int getColor() {
        return Color.parseColor(curveColor);
    }
}
