package com.liber.levyccurves;

public class BezierCurve {
    Point cubicBezierCurve () {
        return new Point();
    }

    public Point quadraticBezierCurve(Point start_point, Point end_point, Point control_point, double step_size) {
        Point newPoint = new Point();

        newPoint.x = (Math.pow(1 - step_size, 2)*start_point.x +
                (1 - step_size)*2*step_size*control_point.x +
                step_size*step_size*end_point.x); // TODO: Int to Double propably


        newPoint.y = (Math.pow(1 - step_size, 2)*start_point.y +
                (1 - step_size)*2*step_size*control_point.y +
                step_size*step_size*end_point.y); // TODO: Int to Double propably

        newPoint.print();
        return newPoint;
    }



}
