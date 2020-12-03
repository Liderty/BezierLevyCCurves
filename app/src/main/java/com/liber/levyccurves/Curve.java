package com.liber.levyccurves;

public class Curve {
    public int curveId;
    public int curveN;
    public double curveX;
    public double curveY;
    public int curveRotation;
    public String curveColor;

    public Curve(){};

    public Curve(int Id, int N, int Rotation, double x, double y, String color) {
        curveId = Id;
        curveN = N;
        curveRotation = Rotation;
        curveX = x;
        curveY = y;
        curveColor = color;
    }

    public Curve(int N, int Rotation, double x, double y, String color) {
        curveN = N;
        curveRotation = Rotation;
        curveX = x;
        curveY = y;
        curveColor = color;
    }

    public String toString(){
        return "Id:"+curveId+", N:"+curveN+", Rotation:"+curveRotation+", X:"+curveX+", Y:"+curveY+", Color:"+curveColor;
    }

    public int getId() {
        return curveId;
    }
}
