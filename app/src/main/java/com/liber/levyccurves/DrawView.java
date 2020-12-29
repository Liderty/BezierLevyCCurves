package com.liber.levyccurves;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

class Line {
    int startX;
    int startY;
    int endX;
    int endY;
    int lineWidth;
    int lineColor;

    public Line(int start_x, int start_y, int end_x, int end_y, int width, int color) {
        startX = start_x;
        startY = start_y;
        endX = end_x;
        endY = end_y;
        lineWidth = width;
        lineColor = color;
    }
}

public class DrawView extends View {
    private Paint paint;
    private List<Line> lines;
    private List<List<Point>> controlPoints;
    private List<List<Point>> bezierPoints;

    private int DEFAULT_LINE_WIDTH = 5;
    final static int DEFAULT_CONTROL_POINT_SIZE = 10;

    private boolean isStateChanged = false;
    private boolean clearFlag = false;

    private void init() {
        paint = new Paint();
        lines = new ArrayList<Line>();
        controlPoints = new ArrayList<List<Point>>();
        bezierPoints = new ArrayList<List<Point>>();
        paint.setStrokeWidth(DEFAULT_LINE_WIDTH);
        paint.setStyle(Paint.Style.FILL);

        this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    public List<List<Point>> getControlPoints() {
        return controlPoints;
    }

    public DrawView(Context context) {
        super(context);
        init();
    }

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DrawView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    @Override
    public void onDraw(Canvas canvas) {
        if(clearFlag) {
            clearFlag = false;
        } else if(isStateChanged) {
            for (Line l : lines) {
                paint.setStrokeWidth(l.lineWidth);
                paint.setColor(l.lineColor);
                canvas.drawLine(l.startX, l.startY, l.endX, l.endY, paint);
            }

//            for (Point cp : controlPoints) {
//                System.out.println(">>> Control point:"+cp.x+", "+cp.y+"; ");
//                paint.setColor(Color.RED);
//                canvas.drawCircle((float) cp.x, (float) cp.y, DEFAULT_CONTROL_POINT_SIZE, paint);
//            }

            for (List<Point> pointsList : bezierPoints) {
                for (Point point : pointsList) {
                    System.out.println("Bezier point:" + point.x + ", " + point.y + "; ");
                    paint.setColor(Color.GREEN);
                    canvas.drawCircle((float) point.x, (float) point.y, 5, paint);
                }
            }
            isStateChanged = false;
        }
    }

//    public void addBezierPoint(Point point) {
//        bezierPoints.add(point);
//        isStateChanged = true;
//        postInvalidate();
//    }

    public void addSetOfBezierPoints(List<Point> points) {
        bezierPoints.add(points);
        isStateChanged = true;
        postInvalidate();
    }

    public void addLine(int start_x, int start_y, int end_x, int end_y, int width, int color) {
        lines.add(new Line(start_x, start_y, end_x, end_y, width, color));
        isStateChanged = true;
        postInvalidate();
    }

    public void addControlPoint(List<Point> points) {
        controlPoints.add(points);
        isStateChanged = true;
        postInvalidate();
    }

    public void clear() {
        lines.clear();
        bezierPoints.clear();
        controlPoints.clear();

        clearFlag = true;
        invalidate();
    }
}
