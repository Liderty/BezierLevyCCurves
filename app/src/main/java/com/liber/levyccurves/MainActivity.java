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

public class MainActivity extends AppCompatActivity {
    DataBaseHandler database_handler;

    private DrawView graphicArea;
    private Button addCurve;
    private Button drawButton;
    private Button clearButton;
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
        addCurve = findViewById(R.id.btnAdd);
        drawButton = (Button) findViewById(R.id.btnDraw);
        clearButton = (Button) findViewById(R.id.btnClear);
        curvesListView = findViewById(R.id.curvesListView);

        graphicArea = findViewById(R.id.drowableArea);
        graphicArea.setBackgroundColor(Color.WHITE);

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


        addCurve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenAddActivity();
            }
        });

        drawButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int iter = 0; iter < curvesList.size(); iter++) {
                    Curve currentCurve = curvesList.get(iter);
                    recursiveCCurve(currentCurve.curveN, currentCurve.curveLineLength, currentCurve.curveRotation, currentCurve.curveX, currentCurve.curveY, currentCurve.curveWidth, currentCurve.getColor());
                }
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showClearAlert();
            }
        });
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
            drawLine((int) x, (int) y, (int) (x + (length * Math.cos(Math.toRadians(curveRotation)))), (int) (y + (length * Math.sin(Math.toRadians(curveRotation)))), curveWidth, curveColor);
        }
    }

    private void drawLine(int start_x, int start_y, int end_x, int end_y, int width, int color) {
        graphicArea.clear();
        graphicArea.drawLine(start_x, start_y, end_x, end_y, width, color);
    }
}