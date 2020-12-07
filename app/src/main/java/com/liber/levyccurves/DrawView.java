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

    public Line(int start_x, int start_y, int end_x, int end_y) {
        startX = start_x;
        startY = start_y;
        endX = end_x;
        endY = end_y;
    }
}

public class DrawView extends View {
    private Paint paint;
    private List<Line> lines;

    private void init() {
        paint = new Paint();

        lines = new ArrayList<Line>();

        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(3);
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
        for(Line l : lines) {
            canvas.drawLine(l.startX, l.startY, l.endX, l.endY, paint);
        }
    }

    public void drawLine(int start_x, int start_y, int end_x, int end_y) {
        lines.add(new Line(start_x, start_y, end_x, end_y));
        postInvalidate();
    }

    public void clear(){
        invalidate();
    }
}
