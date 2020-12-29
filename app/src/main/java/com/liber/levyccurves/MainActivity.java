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

    List<Point> currentPoints;
    private boolean isNextCurve;
    private int curveNumber;

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

        // TODO: Add auto graphical area size limit
//        String test = "height: "+graphicArea.getHeight()+" width:"+graphicArea.getWidth();
//        Toast.makeText(this, test, Toast.LENGTH_SHORT).show();

        isNextCurve = false;
        curveNumber = 0;
        currentPoints = new ArrayList<Point>();

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

                for (Curve currentCurve: curvesList) {
                    Point point = new Point((int) currentCurve.curveX, (int) currentCurve.curveY);
                    currentPoints.add(point);
                    recursiveCCurve(currentCurve.curveN, currentCurve.curveLineLength, currentCurve.curveRotation, currentCurve.curveX, currentCurve.curveY, currentCurve.curveWidth, currentCurve.getColor());
                    cubicBezierMultiCurve(currentPoints);
                    currentPoints.clear();
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
                generateRandomsCurves(10);
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
                graphicArea.clear();
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
            Point controlPoint = new Point((int) (x + (length * Math.cos(Math.toRadians(curveRotation)))), (int) (y + (length * Math.sin(Math.toRadians(curveRotation)))));
            currentPoints.add(controlPoint);
        }
    }

    private void addLine(int start_x, int start_y, int end_x, int end_y, int width, int color) {
        graphicArea.addLine(start_x, start_y, end_x, end_y, width, color);
    }

    private void addCurveControlPoints(List<Point> points) {
        graphicArea.addControlPoint(points);
    }

//    private void bezierPoints() {
//        BezierCurve bezierCalc = new BezierCurve();
//        List<Point> controlPoints = graphicArea.getControlPoints();
//
//        for(double step=0.0; step<=1; step+=0.01) {
//            System.out.println(step);
//
////            Point newPoint = bezierCalc.quadraticBezierCurve(controlPoints.get(0), controlPoints.get(2), controlPoints.get(1), step);
//            Point newPoint = bezierCalc.cubicBezierCurve(controlPoints.get(0), controlPoints.get(3), controlPoints.get(1), controlPoints.get(2), step);
//
//            graphicArea.addBezierPoint(newPoint);
//        }
//    }


    public void cubicBezierMultiCurve (List<Point> pointsList) {
        BezierCurve bezierCalc = new BezierCurve();
        Point startPoint, endPoint, controlPoint;
        double control_point_x, control_point_y;

        startPoint = pointsList.get(0);
        endPoint = pointsList.get(0);

        for(int i=1; i<(pointsList.size()-2); i++) {

            controlPoint = pointsList.get(i);
            endPoint = pointsList.get(i + 1);

            control_point_x = (controlPoint.x + endPoint.x)/2;
            control_point_y = (controlPoint.y + endPoint.y)/2;
            Point newEndPoint = new Point((int) control_point_x, (int) control_point_y);

            List<Point> points_list = bezierCalc.bezierPoints(startPoint, newEndPoint, controlPoint);
            graphicArea.addSetOfBezierPoints(points_list);
            startPoint = newEndPoint;
        }

        Point lastStartPoint = pointsList.get(pointsList.size() - 2);
        Point lastEndPoint = pointsList.get(pointsList.size() - 1);
        List<Point> points_list = bezierCalc.bezierPoints(startPoint, lastEndPoint, lastStartPoint);
        graphicArea.addSetOfBezierPoints(points_list);
    }

    private void generateRandomsCurves(int number_of_curves) {
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