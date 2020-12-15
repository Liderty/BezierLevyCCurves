package com.liber.levyccurves;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class CurvesListViewAdapter extends ArrayAdapter<Curve> {
    Context context;
    int layoutResourceId;
    List<Curve> data;

    public CurvesListViewAdapter(@NonNull Context context, int resource, @NonNull List<Curve> data) {
        super(context, resource, data);

        this.context = context;
        this.layoutResourceId = resource;
        this.data = data;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final ViewHolder holder;
        if(convertView==null){
            holder = new ViewHolder();
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);

            holder.curveInfo = (TextView) convertView.findViewById(R.id.curveInfo);
            holder.btnEdit = (Button) convertView.findViewById(R.id.btnEdit);
            holder.chbDraw = (CheckBox) convertView.findViewById(R.id.chbDraw);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.curveInfo.setText(data.get(position).toString());
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, String.valueOf(position), Toast.LENGTH_SHORT).show();
                Intent intent  = new Intent(context, EditCurveActivity.class);
                intent.putExtra("curveId", data.get(position).getId());
                context.startActivity(intent);
            }
        });
        return  convertView;
    }

    private class ViewHolder {
        protected Button btnEdit;
        private TextView curveInfo;
        private CheckBox chbDraw;
    }
}
