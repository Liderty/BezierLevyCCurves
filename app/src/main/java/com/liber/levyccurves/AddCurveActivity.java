package com.liber.levyccurves;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddCurveActivity extends AppCompatActivity implements View.OnClickListener {
    Context context = this;
    DataBaseHandler database_handler;
    EditText fieldN;
    EditText fieldRotation;
    EditText fieldX;
    EditText fieldY;
    EditText fieldLineLength;
    EditText fieldWidth;
    EditText fieldColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_curve);

        Button addButton = findViewById(R.id.btnAdd);
        Button cancelButton = findViewById(R.id.btnCancel);

        fieldN = findViewById(R.id.etvN);
        fieldRotation = findViewById(R.id.etvRotation);
        fieldX = findViewById(R.id.etvX);
        fieldY = findViewById(R.id.etvY);
        fieldLineLength = findViewById(R.id.etvLineLength);
        fieldWidth = findViewById(R.id.etvWidth);
        fieldColor = findViewById(R.id.etvColor);

        database_handler = new DataBaseHandler(context);

        addButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAdd:
                Toast.makeText(this, "Add", Toast.LENGTH_SHORT).show();

                int newN = Integer.parseInt(String.valueOf(fieldN.getText()));
                int newRotation = Integer.parseInt(String.valueOf(fieldRotation.getText()));
                double newX = Double.parseDouble(String.valueOf(fieldX.getText()));
                double newY = Double.parseDouble(String.valueOf(fieldY.getText()));
                int newLineLength = Integer.parseInt(String.valueOf(fieldLineLength.getText()));
                int newWidth = Integer.parseInt(String.valueOf(fieldWidth.getText()));
                String newColor = (String.valueOf(fieldColor.getText()));

                Curve newCurve = new Curve(newN, newRotation, newX, newY, newLineLength, newWidth, newColor);
                database_handler.addCurve(newCurve);
                finish();
                break;
            case R.id.btnCancel:
                Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
    }
}