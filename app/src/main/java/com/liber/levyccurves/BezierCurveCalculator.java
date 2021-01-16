package com.liber.levyccurves;

import java.util.ArrayList;
import java.util.List;

public class BezierCurveCalculator {
    public Point quadraticBezierCurve(Point start_point, Point end_point, Point control_point, double step_size) {
        Point newPoint = new Point();

        newPoint.x = (Math.pow(1 - step_size, 2)*start_point.x +
                (1 - step_size)*2*step_size*control_point.x +
                step_size*step_size*end_point.x);

        newPoint.y = (Math.pow(1 - step_size, 2)*start_point.y +
                (1 - step_size)*2*step_size*control_point.y +
                step_size*step_size*end_point.y);

        return newPoint;
    }

    public List<Point> bezierPoints(Point start_point, Point end_point, Point control_point) {
        ArrayList<Point> drawPoints = new ArrayList<>();

        for(double step=0.0; step<=1; step+=0.1) {
            Point newPoint = quadraticBezierCurve(start_point, end_point, control_point, step);
            drawPoints.add(newPoint);
        }

        return drawPoints;
    }
}
