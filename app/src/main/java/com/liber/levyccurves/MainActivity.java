package com.liber.levyccurves;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    DataBaseHandler database_handler;

    private TextView graphicArea;
    private Button addCurve;
    private Button drawButton;
    private Button clearButton;
    private ListView curvesListView;
    ArrayList curvesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database_handler = new DataBaseHandler(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        graphicArea = findViewById(R.id.drowableArea);
        addCurve = findViewById(R.id.btnAdd);
        drawButton = findViewById(R.id.btnDraw);
        clearButton = findViewById(R.id.btnClear);
        curvesListView = findViewById(R.id.curvesListView);

        curvesList = database_handler.getAllCuves();
        CurvesListViewAdapter adapter = new CurvesListViewAdapter(this, R.layout.listview_curve_row, curvesList);

        curvesListView.setAdapter(adapter);

        graphicArea.setText("");
        for(int i=0; i<curvesList.size(); i++) {
            graphicArea.append(curvesList.get(i).toString() + "\n");
        }

        addCurve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenAddActivity();
            }
        });

        drawButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastMessage(curvesList.get(0).toString());
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database_handler.clearCurves();
                ToastMessage("All Cleared");
                onResume();
            }
        });
    }

    private void OpenAddActivity() {
        Intent intent = new Intent(this, AddCurveActivity.class);
        startActivity(intent);
    }

    private void OpenEditActivity(int curveId) {
        Intent intent = new Intent(this, EditCurveActivity.class);
        intent.putExtra("curveId", curveId);
        startActivity(intent);
    }

    private void ToastMessage(String toastText) {
        Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show();
    }
}