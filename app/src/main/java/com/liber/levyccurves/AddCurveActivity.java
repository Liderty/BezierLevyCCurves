package com.liber.levyccurves;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class AddCurveActivity extends AppCompatActivity implements View.OnClickListener {
    Context context = this;
    DataBaseHandler database_handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_curve);

        Button addButton = findViewById(R.id.btnAdd);
        Button cancelButton = findViewById(R.id.btnCancel);

        database_handler = new DataBaseHandler(context);

        addButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAdd:
                Toast.makeText(this, "Add", Toast.LENGTH_SHORT).show();

                Curve newCurve = new Curve(0, 90, 1.0, 1.0, "alpha");
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