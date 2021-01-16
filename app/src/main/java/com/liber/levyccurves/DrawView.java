package com.liber.levyccurves;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class DrawView extends androidx.appcompat.widget.AppCompatImageView {
    private Paint paint;
    private List<Point> controlPoints;
    private List<BezierCurve> bezierCurves;

    private int DEFAULT_LINE_WIDTH = 5;
    final static int DEFAULT_CONTROL_POINT_SIZE = 10;

    private boolean isStateChanged = false;
    private boolean clearFlag = false;

    private void init() {
        paint = new Paint();
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
            drawCurves(canvas);

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

    public void addControlPoint(Point point) {
        controlPoints.add(point);
        isStateChanged = true;
        postInvalidate();
    }

    public void drawCurves(Canvas canvas) {
        for (BezierCurve bezierCurve : bezierCurves) {
            paint.setColor(bezierCurve.getColor());
            paint.setStrokeWidth(bezierCurve.curveWidth);
            List<Point> points = bezierCurve.getPoints();

            for(int point_index=0; point_index<points.size(); point_index++){
                drawPoint(canvas, points.get(point_index).x, points.get(point_index).y, bezierCurve.curveWidth);

                if(point_index!=(points.size() - 1)) {
                    canvas.drawLine((float) points.get(point_index).x, (float) points.get(point_index).y, (float) points.get(point_index + 1).x, (float) points.get(point_index + 1).y, paint);
                }
            }
        }
    }

    public void drawControlPoints(Canvas canvas) {
        for (Point controlPoint : controlPoints) {
//            paint.setColor();
            canvas.drawCircle((float) controlPoint.x, (float) controlPoint.y, DEFAULT_CONTROL_POINT_SIZE, paint);
        }
    }

    public void drawPoint(Canvas canvas, double x, double y, int point_size) {
        canvas.drawCircle((float) x, (float) y, point_size/2, paint);
    }

    public void clear() {
        bezierCurves.clear();
        controlPoints.clear();

        clearFlag = true;
        invalidate();
    }
}
