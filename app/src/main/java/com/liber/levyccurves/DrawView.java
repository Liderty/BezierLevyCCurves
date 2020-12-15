package com.liber.levyccurves;

import android.content.Context;
import android.graphics.Canvas;
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
    private int DEFAULT_LINE_WIDTH = 5;
    private boolean isStateChanged = false;

    private void init() {
        paint = new Paint();
        lines = new ArrayList<Line>();
        paint.setStrokeWidth(DEFAULT_LINE_WIDTH);
        this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
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
        if(isStateChanged) {
            for (Line l : lines) {
                paint.setStrokeWidth(l.lineWidth);
                paint.setColor(l.lineColor);
                canvas.drawLine(l.startX, l.startY, l.endX, l.endY, paint);
            }
            isStateChanged = false;
        }
    }

    public void drawLine(int start_x, int start_y, int end_x, int end_y, int width, int color) {
        lines.add(new Line(start_x, start_y, end_x, end_y, width, color));
        isStateChanged = true;
        postInvalidate();
    }

    public void clear() {
        invalidate();
    }
}
