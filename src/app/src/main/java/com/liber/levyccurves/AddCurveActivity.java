package com.liber.levyccurves;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import yuku.ambilwarna.AmbilWarnaDialog;

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
    ImageView colorBox;
    int currentColor;

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
        colorBox = findViewById(R.id.colorBox);

        currentColor = colorBox.getSolidColor();
        String strColor = String.format("#%06X", 0xFFFFFF & currentColor);
        fieldColor.setText(strColor);

        database_handler = new DataBaseHandler(context);

        addButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        colorBox.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAdd:
                if(isDataCompleted()) {
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
                } else Toast.makeText(context, "Fill Data!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btnCancel:
                Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.colorBox:
                openColorPicker();
                break;
        }
    }

    public void openColorPicker() {
        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(this, currentColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
            }
            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                currentColor = color;
                colorBox.setBackgroundColor(currentColor);
                String strColor = String.format("#%06X", 0xFFFFFF & currentColor);

                fieldColor.setText(strColor);
            }
        });
        colorPicker.show();
    }

    private boolean isDataCompleted() {
        if (String.valueOf(fieldN.getText()).isEmpty() ||
                String.valueOf(fieldRotation.getText()).isEmpty() ||
                String.valueOf(fieldX.getText()).isEmpty() ||
                String.valueOf(fieldY.getText()).isEmpty() ||
                String.valueOf(fieldLineLength.getText()).isEmpty() ||
                String.valueOf(fieldWidth.getText()).isEmpty()) {
            return false;
        } else return true;
    }
}
