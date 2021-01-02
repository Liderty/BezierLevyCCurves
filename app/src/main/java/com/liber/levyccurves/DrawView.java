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
    private List<Point> controlPoints;
    private List<BezierCurve> bezierCurves;

    private int DEFAULT_LINE_WIDTH = 5;
    final static int DEFAULT_CONTROL_POINT_SIZE = 10;

    private boolean isStateChanged = false;
    private boolean clearFlag = false;

    private void init() {
        paint = new Paint();
        lines = new ArrayList<Line>();
        controlPoints = new ArrayList<Point>();
        bezierCurves = new ArrayList<BezierCurve>();
        paint.setStrokeWidth(DEFAULT_LINE_WIDTH);
        paint.setStyle(Paint.Style.FILL);

        this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    public List<Point> getControlPoints() {
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

            for (Point controlPoint : controlPoints) {
                paint.setColor(Color.RED);
                canvas.drawCircle((float) controlPoint.x, (float) controlPoint.y, DEFAULT_CONTROL_POINT_SIZE, paint);
            }

            for (BezierCurve bezierCurve : bezierCurves) {
                paint.setColor(bezierCurve.getColor());
                paint.setStrokeWidth(bezierCurve.curveWidth);
                for (Point point : bezierCurve.getPoints()) {
                    drawPoint(canvas, point.x, point.y, bezierCurve.curveWidth);
                }
            }
            isStateChanged = false;
        }
    }

    public void addSetOfBezierPoints(int index, List<Point> points) {
        for(Point point: points) {
            bezierCurves.get(index).addPoint(point);
        }
        isStateChanged = true;
        postInvalidate();
    }

    public void addBezierCurve(BezierCurve bezierCurve){
        bezierCurves.add(bezierCurve);
    }

    public void addLine(int start_x, int start_y, int end_x, int end_y, int width, int color) {
        lines.add(new Line(start_x, start_y, end_x, end_y, width, color));
        isStateChanged = true;
        postInvalidate();
    }

    public void addControlPoint(Point point) {
        controlPoints.add(point);
        isStateChanged = true;
        postInvalidate();
    }

    public void drawPoint(Canvas canvas, double x, double y, int radis_size) {
        canvas.drawCircle((float) x, (float) y, radis_size, paint);
    }

    public void clear() {
        lines.clear();
        bezierCurves.clear();
        controlPoints.clear();

        clearFlag = true;
        invalidate();
    }
}
