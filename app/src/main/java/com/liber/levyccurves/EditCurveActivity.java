package com.liber.levyccurves;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import yuku.ambilwarna.AmbilWarnaDialog;

public class EditCurveActivity extends AppCompatActivity implements View.OnClickListener {
    Context context = this;
    DataBaseHandler database_handler;
    Curve editedCurve;

    EditText fieldN;
    EditText fieldRotation;
    EditText fieldX;
    EditText fieldY;
    EditText fieldColor;
    EditText fieldLineLength;
    EditText fieldWidth;

    ImageView colorBox;
    int currentColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_curve);

        Button saveButton = findViewById(R.id.btnSave);
        Button cancelButton = findViewById(R.id.btnCancel);

        fieldN = findViewById(R.id.etvN);
        fieldRotation = findViewById(R.id.etvRotation);
        fieldX = findViewById(R.id.etvX);
        fieldY = findViewById(R.id.etvY);
        fieldLineLength = findViewById(R.id.etvLineLength);
        fieldWidth = findViewById(R.id.etvWidth);
        fieldColor = findViewById(R.id.etvColor);
        colorBox = findViewById(R.id.colorBox);

        database_handler = new DataBaseHandler(context);

        Intent mIntent = getIntent();
        int curveId = mIntent.getIntExtra("curveId", 0);

        editedCurve = database_handler.getCurveById(curveId);

        fieldN.setText(Integer.toString(editedCurve.curveN));
        fieldRotation.setText(Double.toString(editedCurve.curveRotation));
        fieldX.setText(Double.toString(editedCurve.curveX));
        fieldY.setText(Double.toString(editedCurve.curveY));
        fieldLineLength.setText(Integer.toString(editedCurve.curveLineLength));
        fieldWidth.setText(Integer.toString(editedCurve.curveWidth));
        fieldColor.setText(editedCurve.curveColor);

        currentColor = editedCurve.getColor();
        colorBox.setBackground(new ColorDrawable(currentColor));
        String strColor = String.format("#%06X", 0xFFFFFF & currentColor);
        fieldColor.setText(strColor);

        saveButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSave:
                Toast.makeText(this, "Edited", Toast.LENGTH_SHORT).show();
                finish();
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
}