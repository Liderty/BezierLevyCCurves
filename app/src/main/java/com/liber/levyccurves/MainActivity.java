package com.liber.levyccurves;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    static final int DEFAULT_LINE_MIN_ITERATIONS = 1;
    static final int DEFAULT_LINE_MAX_ITERATIONS = 10;
    static final int DEFAULT_LINE_MIN_WIDTH = 1;
    static final int DEFAULT_LINE_MAX_WIDTH = 25;
    static final int DEFAULT_LINE_MIN_LENGTH = 50;
    static final int DEFAULT_LINE_MAX_LENGTH = 500;
    static final int DEFAULT_LINE_MIN_XY_COORDINATE = -250;
    static final int DEFAULT_LINE_MAX_XY_COORDINATE = 250;
    static final int DEFAULT_LINE_MIN_ROTATION = 1;
    static final int DEFAULT_LINE_MAX_ROTATION = 360;

    public static final String SHARED_PREFS = "mainSettings";
    public static final String CP_DRAW_CHECKBOX = "cp_draw_checkbox";
    public static final String CP_COLOR = "cp_color";
    public static final String BG_COLOR = "bg_color";
    public static final String SP_MIDDLE_POINT_X = "mid_point_x";
    public static final String SP_MIDDLE_POINT_Y = "mid_point_y";
    public static final String RESOLUTION = "resolution";
    public static final String RND_AMOUNT = "cp_amount";

    private DataBaseHandler database_handler;

    private DrawView graphicArea;
    private Button addButton;
    private Button drawButton;
    private Button clearButton;
    private Button rndButton;
    private Button setupButton;
    private ListView curvesListView;
    private ArrayList<Curve> curvesList;

    private List<Point> currentPoints;
    private String settings_background_color;
    private String control_points_color;
    private boolean isCPDrawEnabled;
    private int mid_point_x;
    private int mid_point_y;
    private int curveIndex;
    private int rndAmount;
    private float curvesResolution;

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
        setupButton = (Button) findViewById(R.id.btnSetup);
        curvesListView = findViewById(R.id.curvesListView);

        updateSettingsData();

        graphicArea = findViewById(R.id.drowableArea);
        graphicArea.setBackgroundColor(Color.parseColor(settings_background_color));

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

        setupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenSetupActivity();
            }
        });

        drawButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableUi(false);
                clearDrawArea();
                curveIndex = 0;
                saveMiddlePoint();
                if(isCPDrawEnabled) graphicArea.setControlPointsColor(control_points_color);


                for (Curve currentLevyCurve: curvesList) {
                    Point startPoint = new Point(mid_point_x + (int) currentLevyCurve.curveX,mid_point_y + -((int) currentLevyCurve.curveY));
                    BezierCurve bezierCurve = new BezierCurve(currentLevyCurve.curveWidth, currentLevyCurve.curveColor);

                    graphicArea.addBezierCurve(bezierCurve);
                    if(isCPDrawEnabled) graphicArea.addControlPoint(startPoint);
                    currentPoints.add(startPoint);

                    recursiveCCurve(currentLevyCurve.curveN, currentLevyCurve.curveLineLength,
                            currentLevyCurve.curveRotation, startPoint.x,
                            startPoint.y);

                    cubicBezierMultiCurve(curveIndex, currentPoints);
                    currentPoints.clear();
                    curveIndex++;
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
                generateRandomsCurves();
            }
        });
    }

    private void disableUi (boolean enabled) {
        if(enabled){
            curvesListView.setEnabled(true);
            clearButton.setEnabled(true);
            rndButton.setEnabled(true);
            drawButton.setEnabled(true);
            addButton.setEnabled(true);
        } else {
            curvesListView.setEnabled(false);
            clearButton.setEnabled(false);
            rndButton.setEnabled(false);
            drawButton.setEnabled(false);
            addButton.setEnabled(false);
        }
    }

    private void OpenAddActivity() {
        Intent intent = new Intent(this, AddCurveActivity.class);
        startActivity(intent);
    }

    private void OpenSetupActivity() {
        Intent intent = new Intent(this, SetupActivity.class);
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

    private void recursiveCCurve(int curveN, double curveLength, int curveRotation, double curveX, double curveY) {
        double x = curveX;
        double y = curveY;
        double length = curveLength;

        int iteration = curveN;

        if (iteration > 0) {
            length = length / Math.sqrt(2);

            recursiveCCurve(iteration - 1, length, curveRotation + 45, x, y);

            x = x + (length * Math.cos(Math.toRadians(curveRotation + 45)));
            y = y + (length * Math.sin(Math.toRadians(curveRotation + 45)));

            recursiveCCurve(iteration - 1, length, curveRotation - 45, x, y);
        } else {
            Point controlPoint = new Point((int) (x + (length * Math.cos(Math.toRadians(curveRotation)))), (int) (y + (length * Math.sin(Math.toRadians(curveRotation)))));
            if(isCPDrawEnabled) graphicArea.addControlPoint(controlPoint);
            currentPoints.add(controlPoint);
        }
    }

    public void cubicBezierMultiCurve (int curveIndex, List<Point> pointsList) {
        BezierCurveCalculator bezierCalc = new BezierCurveCalculator(curvesResolution);
        Point startPoint, endPoint, controlPoint;
        double control_point_x, control_point_y;

        startPoint = pointsList.get(0);

        for(int i=1; i<(pointsList.size()-2); i++) {

            controlPoint = pointsList.get(i);
            endPoint = pointsList.get(i + 1);

            control_point_x = (controlPoint.x + endPoint.x)/2;
            control_point_y = (controlPoint.y + endPoint.y)/2;
            Point newEndPoint = new Point((int) control_point_x, (int) control_point_y);

            List<Point> points_list = bezierCalc.bezierPoints(startPoint, newEndPoint, controlPoint);
            graphicArea.addSetOfBezierPoints(curveIndex, points_list);
            startPoint = newEndPoint;
        }

        Point lastStartPoint = pointsList.get(pointsList.size() - 2);
        Point lastEndPoint = pointsList.get(pointsList.size() - 1);
        List<Point> points_list = bezierCalc.bezierPoints(startPoint, lastEndPoint, lastStartPoint);
        graphicArea.addSetOfBezierPoints(curveIndex, points_list);
    }

    private void generateRandomsCurves() {
        database_handler = new DataBaseHandler(this);

        for(int i=0; i<rndAmount; i++) {
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

    public void updateSettingsData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        settings_background_color = sharedPreferences.getString(BG_COLOR, "#000000");
        mid_point_x = sharedPreferences.getInt(SP_MIDDLE_POINT_X, 0);
        mid_point_y = sharedPreferences.getInt(SP_MIDDLE_POINT_Y, 0);
        curvesResolution = sharedPreferences.getFloat(RESOLUTION, 0.1f);
        rndAmount = sharedPreferences.getInt(RND_AMOUNT, 10);
        isCPDrawEnabled = sharedPreferences.getBoolean(CP_DRAW_CHECKBOX, false);
        control_points_color = sharedPreferences.getString(CP_COLOR, "#440000");
    }

    public void saveMiddlePoint() {

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(SP_MIDDLE_POINT_X, graphicArea.getWidth()/2);
        editor.putInt(SP_MIDDLE_POINT_Y, graphicArea.getHeight()/2);
        editor.apply();

        updateSettingsData();
    }

    private void clearDrawArea() {
        graphicArea.setBackgroundColor(Color.parseColor(settings_background_color));
        graphicArea.clear();
        currentPoints.clear();
    }
}