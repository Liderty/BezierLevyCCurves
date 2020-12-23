package com.liber.levyccurves;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    static final int DEFAULT_LINE_MIN_ITERATIONS = 1;
    static final int DEFAULT_LINE_MAX_ITERATIONS = 14;
    static final int DEFAULT_LINE_MIN_WIDTH = 1;
    static final int DEFAULT_LINE_MAX_WIDTH = 25;
    static final int DEFAULT_LINE_MIN_LENGTH = 50;
    static final int DEFAULT_LINE_MAX_LENGTH = 500;
    static final int DEFAULT_LINE_MIN_XY_COORDINATE = 1;
    static final int DEFAULT_LINE_MAX_XY_COORDINATE = 1000;
    static final int DEFAULT_LINE_MIN_ROTATION = 1;
    static final int DEFAULT_LINE_MAX_ROTATION = 360;

    DataBaseHandler database_handler;

    private DrawView graphicArea;
    private Button addButton;
    private Button drawButton;
    private Button clearButton;
    private Button rndButton;
    private ListView curvesListView;
    ArrayList<Curve> curvesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database_handler = new DataBaseHandler(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        addButton = findViewById(R.id.btnAdd);
        drawButton = (Button) findViewById(R.id.btnDraw);
        clearButton = (Button) findViewById(R.id.btnClear);
        rndButton = (Button) findViewById(R.id.btnRnd);
        curvesListView = findViewById(R.id.curvesListView);

        graphicArea = findViewById(R.id.drowableArea);
        graphicArea.setBackgroundColor(Color.BLACK);

        String test = "height: "+graphicArea.getHeight()+" width:"+graphicArea.getWidth();
        Toast.makeText(this, test, Toast.LENGTH_SHORT).show();

        curvesList = database_handler.getAllCuves();
        if (curvesList.isEmpty()) {
            clearButton.setEnabled(false);
            drawButton.setEnabled(false);
        } else {
            clearButton.setEnabled(true);
            drawButton.setEnabled(true);
        }

        CurvesListViewAdapter adapter = new CurvesListViewAdapter(this, R.layout.listview_curve_row, curvesList);
        adapter.notifyDataSetChanged();
        curvesListView.setAdapter(adapter);


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenAddActivity();
            }
        });

        drawButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableUi(false);
                for (int iter = 0; iter < curvesList.size(); iter++) {
                    Curve currentCurve = curvesList.get(iter);

                    //Control Point
                    Point point = new Point((int) currentCurve.curveX, (int) currentCurve.curveY);
                    drawControlPoint(point);

                    recursiveCCurve(currentCurve.curveN, currentCurve.curveLineLength, currentCurve.curveRotation, currentCurve.curveX, currentCurve.curveY, currentCurve.curveWidth, currentCurve.getColor());
                    bezierPoints();
                }


                disableUi(true);
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showClearAlert();
            }
        });

        rndButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateRandoms(10);
            }
        });
    }

    private void disableUi (boolean enabled) {
        curvesListView.setEnabled(enabled);
        clearButton.setEnabled(enabled);
        rndButton.setEnabled(enabled);
        drawButton.setEnabled(enabled);
        addButton.setEnabled(enabled);
    }

    private void OpenAddActivity() {
        Intent intent = new Intent(this, AddCurveActivity.class);
        startActivity(intent);
    }

    private void ToastMessage(String toastText) {
        Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show();
    }

    private void showClearAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm clearing");
        builder.setMessage("Are you sure to delete all curves?");
        builder.setPositiveButton("Clear", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                database_handler.clearCurves();
                ToastMessage("All Cleared");
                dialog.dismiss();
                onResume();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                ToastMessage("Canceled");
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void recursiveCCurve(int curveN, double curveLength, int curveRotation, double curveX, double curveY, int curveWidth, int curveColor) {
        double x = curveX;
        double y = curveY;
        double length = curveLength;

        int iteration = curveN;

        if (iteration > 0) {
            length = length / Math.sqrt(2);

            recursiveCCurve(iteration - 1, length, curveRotation + 45, x, y, curveWidth, curveColor);

            x = x + (length * Math.cos(Math.toRadians(curveRotation + 45)));
            y = y + (length * Math.sin(Math.toRadians(curveRotation + 45)));

            recursiveCCurve(iteration - 1, length, curveRotation - 45, x, y, curveWidth, curveColor);
        } else {
            //drawLine((int) x, (int) y, (int) (x + (length * Math.cos(Math.toRadians(curveRotation)))), (int) (y + (length * Math.sin(Math.toRadians(curveRotation)))), curveWidth, curveColor);
            Point point2 = new Point((int) (x + (length * Math.cos(Math.toRadians(curveRotation)))), (int) (y + (length * Math.sin(Math.toRadians(curveRotation)))));
            drawControlPoint(point2);
        }
    }

    private void drawLine(int start_x, int start_y, int end_x, int end_y, int width, int color) {
        graphicArea.drawLine(start_x, start_y, end_x, end_y, width, color);
    }

    private void drawControlPoint(Point point) {
        graphicArea.addControlPoint(point);
    }

    private void bezierPoints() {
        BezierCurve bezierCalc = new BezierCurve();
        List<Point> controlPoints = graphicArea.getControlPoints();

        for(double step=0.0; step<=1; step+=0.01) {
            System.out.println(step);

            Point newPoint = bezierCalc.quadraticBezierCurve(controlPoints.get(0), controlPoints.get(2), controlPoints.get(1), step);
            graphicArea.addBezierPoint(newPoint);
        }
    }

    private void generateRandoms(int number_of_curves) {
        database_handler = new DataBaseHandler(this);

        for(int i=0; i<number_of_curves; i++) {
            Curve randomizedCurve = getRandomCurve();
            database_handler.addCurve(randomizedCurve);
        }
        onResume();
    }

    private Curve getRandomCurve() {
        int randomN = Randomizer.generate(DEFAULT_LINE_MIN_ITERATIONS, DEFAULT_LINE_MAX_ITERATIONS);
        int randomX = Randomizer.generate(DEFAULT_LINE_MIN_XY_COORDINATE, DEFAULT_LINE_MAX_XY_COORDINATE);
        int randomY = Randomizer.generate(DEFAULT_LINE_MIN_XY_COORDINATE, DEFAULT_LINE_MAX_XY_COORDINATE);
        int randomRotation = Randomizer.generate(DEFAULT_LINE_MIN_ROTATION, DEFAULT_LINE_MAX_ROTATION);
        int randomLineLength = Randomizer.generate(DEFAULT_LINE_MIN_LENGTH, DEFAULT_LINE_MAX_LENGTH);
        int randomWidth = Randomizer.generate(DEFAULT_LINE_MIN_WIDTH, DEFAULT_LINE_MAX_WIDTH);
        String randomColor = Randomizer.getRandomColor();

        Curve randromCurve = new Curve(randomN, randomRotation, randomX, randomY, randomLineLength, randomWidth, randomColor);
        return randromCurve;
    }
}