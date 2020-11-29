package com.liber.levyccurves;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    DataBaseHandler database_handler;
    private Button addCurve;
    private Button drawButton;
    private ListView curvesListView;
    ArrayList curvesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database_handler = new DataBaseHandler(this);

        addCurve = findViewById(R.id.btnAdd);
        drawButton = findViewById(R.id.btnDraw);
        curvesListView = findViewById(R.id.curvesListView);

        curvesList = database_handler.getAllPosts();
        //CurvesListViewAdapter adapter = new CurvesListViewAdapter(this, R.layout.listview_curve_row, curvesList);
        //curvesListView.setAdapter(adapter);

        addCurve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenAddActivity();
            }
        });

        drawButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastSomething();
            }
        });
    }

    private void OpenAddActivity() {
        Intent intent = new Intent(this, AddCurveActivity.class);
        startActivity(intent);
    }

    private void ToastSomething() {
        Toast.makeText(this, curvesList.get(0).toString(), Toast.LENGTH_SHORT).show();
    }
}