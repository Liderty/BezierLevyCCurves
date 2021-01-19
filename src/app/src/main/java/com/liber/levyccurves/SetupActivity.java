package com.liber.levyccurves;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import yuku.ambilwarna.AmbilWarnaDialog;

public class SetupActivity extends AppCompatActivity {
    private CheckBox cpDrawCheckBox;
    private ImageView backgroundColorBox;
    private ImageView controlPointsColorBox;
    private EditText bgColorText;
    private EditText resolutionText;
    private EditText rndAmountText;
    private EditText cpColorText;
    private Button saveSettingsButton;
    private Button cancelButton;

    public static final String SHARED_PREFS = "mainSettings";
    public static final String CP_DRAW_CHECKBOX = "cp_draw_checkbox";
    public static final String CP_COLOR = "cp_color";
    public static final String BG_COLOR = "bg_color";
    public static final String RESOLUTION = "resolution";
    public static final String RND_AMOUNT = "cp_amount";

    private String controlPointsColor;
    private String backgroundColor;
    private int randomsAmount;
    private boolean shouldDrawCP;
    private float curvesResolution;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        cpDrawCheckBox = (CheckBox) findViewById(R.id.checkboxShowControlPoints);
        backgroundColorBox = (ImageView) findViewById(R.id.bgColorBox);
        controlPointsColorBox = (ImageView) findViewById(R.id.cpColorBox);
        bgColorText = (EditText) findViewById(R.id.etvBgColor);
        resolutionText = (EditText) findViewById(R.id.etvResolution);
        cpColorText = (EditText) findViewById(R.id.etvCPColor);
        rndAmountText = (EditText) findViewById(R.id.etvRndAmount);
        saveSettingsButton = (Button) findViewById(R.id.btnSave);
        cancelButton = (Button) findViewById(R.id.btnCancel);


        backgroundColorBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openColorPicker(backgroundColorBox, backgroundColorBox.getSolidColor(), bgColorText);
            }
        });

        controlPointsColorBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openColorPicker(controlPointsColorBox, controlPointsColorBox.getSolidColor(), cpColorText);
            }
        });

        saveSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
                finish();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        loadData();
        updateViews();
    }

    public void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(CP_COLOR, cpColorText.getText().toString());
        editor.putString(BG_COLOR, bgColorText.getText().toString());
        editor.putInt(RND_AMOUNT, Integer.parseInt(rndAmountText.getText().toString()));
        editor.putFloat(RESOLUTION, Float.parseFloat(resolutionText.getText().toString()));
        editor.putBoolean(CP_DRAW_CHECKBOX, cpDrawCheckBox.isChecked());

        editor.apply();
        Toast.makeText(this, "Data saved", Toast.LENGTH_SHORT).show();
    }

    public void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        controlPointsColor = sharedPreferences.getString(CP_COLOR, "#FF0000");
        backgroundColor = sharedPreferences.getString(BG_COLOR, "#000000");
        shouldDrawCP = sharedPreferences.getBoolean(CP_DRAW_CHECKBOX, false);
        curvesResolution = sharedPreferences.getFloat(RESOLUTION, 0.1f);
        randomsAmount = sharedPreferences.getInt(RND_AMOUNT, 10);
    }

    public void updateViews() {
        cpDrawCheckBox.setChecked(shouldDrawCP);

        backgroundColorBox.setBackground(new ColorDrawable(Color.parseColor(backgroundColor)));
        controlPointsColorBox.setBackground(new ColorDrawable(Color.parseColor(controlPointsColor)));
        bgColorText.setText(backgroundColor);
        cpColorText.setText(controlPointsColor);
        resolutionText.setText(Float.toString(curvesResolution));
        rndAmountText.setText(Integer.toString(randomsAmount));
    }

    public void openColorPicker(ImageView colorBox, Integer currentColor, TextView colorField) {
        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(this, currentColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
            }
            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                int currentColor = color;
                colorBox.setBackgroundColor(currentColor);
                String strColor = String.format("#%06X", 0xFFFFFF & currentColor);

                colorField.setText(strColor);
            }
        });
        colorPicker.show();
    }
}