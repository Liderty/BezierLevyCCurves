package com.liber.levyccurves;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class CurvesListViewAdapter extends ArrayAdapter<Curve> {
    public CurvesListViewAdapter(@NonNull Context context, int resource, @NonNull List<Curve> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {



        return  convertView;
    }
}
