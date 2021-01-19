package com.liber.levyccurves;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
    int curveId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_curve);

        Button saveButton = findViewById(R.id.btnSave);
        Button cancelButton = findViewById(R.id.btnCancel);
        Button removeButton = findViewById(R.id.btnRemove);

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
        curveId = mIntent.getIntExtra("curveId", 0);

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
        colorBox.setOnClickListener(this);
        removeButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSave:
                if (saveEdition(curveId)) {
                    Toast.makeText(this, "Edited", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            case R.id.btnCancel:
                Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.colorBox:
                openColorPicker();
                break;
            case R.id.btnRemove:
                openRemoveDialog();
                break;
        }
    }

    private void openColorPicker() {
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

    private void openRemoveDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirm");
        builder.setMessage("Are you sure to delete this curve?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                DataBaseHandler database_handler = new DataBaseHandler(context);
                database_handler.deleteCurve(editedCurve.getId());
                Toast.makeText(context, "Successfuly Deleted" + String.valueOf(editedCurve.getId()), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                finish();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
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

    private boolean saveEdition(int curveId) {
        if (isDataCompleted()) {
            int editedN = Integer.parseInt(String.valueOf(fieldN.getText()));
            int editedRotation = (int) Double.parseDouble(String.valueOf(fieldRotation.getText()));
            double editedX = Double.parseDouble(String.valueOf(fieldX.getText()));
            double editedY = Double.parseDouble(String.valueOf(fieldY.getText()));
            int editedLineLength = Integer.parseInt(String.valueOf(fieldLineLength.getText()));
            int editedWidth = Integer.parseInt(String.valueOf(fieldWidth.getText()));
            String editedColor = (String.valueOf(fieldColor.getText()));

            Curve editedCurve = new Curve(editedN, editedRotation, editedX, editedY, editedLineLength, editedWidth, editedColor);
            System.out.println("CURVE READY");
            database_handler.updateCurve(curveId, editedCurve);
            return true;
        } else {
            Toast.makeText(context, "Fill Data!", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}