package com.liber.levyccurves;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
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

            holder.curveId = (TextView) convertView.findViewById(R.id.curveId);
            holder.curveN = (TextView) convertView.findViewById(R.id.curveN);
            holder.curveRotataion = (TextView) convertView.findViewById(R.id.curveRotation);
            holder.curveX = (TextView) convertView.findViewById(R.id.curveX);
            holder.curveY = (TextView) convertView.findViewById(R.id.curveY);
            holder.curveLength = (TextView) convertView.findViewById(R.id.curveLength);
            holder.curveWidth = (TextView) convertView.findViewById(R.id.curveWidth);
            holder.curveColor = (LinearLayout) convertView.findViewById(R.id.curveColor);

            holder.btnEdit = (Button) convertView.findViewById(R.id.btnEdit);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.curveId.setText(Integer.toString(data.get(position).curveId));
        holder.curveN.setText(Integer.toString(data.get(position).curveN));
        holder.curveRotataion.setText(Integer.toString(data.get(position).curveRotation));
        holder.curveX.setText(Double.toString(data.get(position).curveX));
        holder.curveY.setText(Double.toString(data.get(position).curveY));
        holder.curveLength.setText(Integer.toString(data.get(position).curveLineLength));
        holder.curveWidth.setText(Integer.toString(data.get(position).curveWidth));
        holder.curveColor.setBackground(new ColorDrawable(data.get(position).getColor()));

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
        private TextView curveId;
        private TextView curveN;
        private TextView curveRotataion;
        private TextView curveX;
        private TextView curveY;
        private TextView curveLength;
        private TextView curveWidth;
        private LinearLayout curveColor;
    }
}
