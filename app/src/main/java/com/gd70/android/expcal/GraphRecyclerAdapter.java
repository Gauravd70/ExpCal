package com.gd70.android.expcal;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import static com.gd70.android.expcal.MainActivity.months;

public class GraphRecyclerAdapter extends RecyclerView.Adapter<GraphRecyclerAdapter.MyViewHolder> {

    private Context context;
    private TextView avgText;
    private float avgAmt;

    public GraphRecyclerAdapter(Context context, TextView avgText)
    {
        this.context=context;
        this.avgText=avgText;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        GraphView graphView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            graphView = itemView.findViewById(R.id.graphView);
        }
    }

    @NonNull
    @Override
    public GraphRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View cardView = LayoutInflater.from(parent.getContext()).inflate(R.layout.graph_card_layout,parent,false);
        return new MyViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(@NonNull GraphRecyclerAdapter.MyViewHolder myViewHolder, int i) {
        myViewHolder.setIsRecyclable(false);
        String month=months[i];
        myViewHolder.graphView.setTitle(month);
        myViewHolder.graphView.getViewport().setMinX(1);
        myViewHolder.graphView.getViewport().setMaxX(31);
        myViewHolder.graphView.getViewport().setMinY(1000);
        myViewHolder.graphView.getViewport().setMaxY(10000);
        myViewHolder.graphView.getViewport().setXAxisBoundsManual(true);
        myViewHolder.graphView.getViewport().setYAxisBoundsManual(true);
        try {
            BarGraphSeries<DataPoint> series = new BarGraphSeries<>(dataPoints(month));
            series.setDataWidth(1);
            myViewHolder.graphView.addSeries(series);
            myViewHolder.graphView.addSeries(addAvgDataPoints(i));
        } catch (NullPointerException e) {
        }
    }

    @Override
    public int getItemCount() {
        return 12;
    }

    public DataPoint[] dataPoints(String month)
    {
        DatabaseHandler databaseHandler = new DatabaseHandler(context,month);
        Cursor datesCursor = databaseHandler.getTables();
        int num;
        DataPoint[] dataPoints;
        if(datesCursor.getCount()==1)
        {
            dataPoints=new DataPoint[0];
        }
        else {
            float totalDays=0;
            float totalAmt=0;
            num = datesCursor.getCount()-2;
            dataPoints = new DataPoint[num];
            Cursor cursor;
            int cnt = 0;
            datesCursor.moveToFirst();
            do {
                float amt = 0;
                String name = datesCursor.getString(0);
                if (name.equals("android_metadata") || name.equals("sqlite_sequence")) {

                } else {
                    cursor = databaseHandler.getReport(name);
                    if (cursor.moveToFirst()) {
                        do {
                            amt = amt + cursor.getInt(1);
                        } while (cursor.moveToNext());
                        dataPoints[cnt] = new DataPoint(Integer.parseInt(name.split("_")[1]), amt);
                        totalAmt=totalAmt+amt;
                        cnt++;
                        totalDays++;
                    }
                }
            } while (datesCursor.moveToNext());
            float avgAmt=totalAmt/totalDays;
            saveAvgAmt(avgAmt);
        }
        return dataPoints;
    }

    private void saveAvgAmt(float avgAmt)
    {
        Log.d("main", "saveAvgAmt: "+avgAmt);
        SharedPreferences sharedPreferences = context.getSharedPreferences("AvgAmt",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat("Avg",avgAmt);
        editor.apply();
        avgText.setText(Float.toString(avgAmt));
        this.avgAmt=avgAmt;
    }

    public LineGraphSeries addAvgDataPoints(int monthNum)
    {
        DataPoint[] avgDataPoints;
        if(monthNum==0||monthNum==2||monthNum==4||monthNum==6||monthNum==7||monthNum==9||monthNum==11)
        {
            avgDataPoints=new DataPoint[31];

        }
        else if(monthNum==3||monthNum==5||monthNum==8||monthNum==10)
        {
            avgDataPoints=new DataPoint[30];
        }
        else
        {
            if(MainActivity.getInstance().getCalenderInstance().getYear()%4==0)
            {
                avgDataPoints=new DataPoint[29];
            }
            else
            {
                avgDataPoints=new DataPoint[28];
            }
        }
        for(int i=0;i<avgDataPoints.length;i++)
        {
            avgDataPoints[i]=new DataPoint(i+1,avgAmt);
        }
        LineGraphSeries lineGraphSeries = new LineGraphSeries(avgDataPoints);
        lineGraphSeries.setColor(ResourcesCompat.getColor(context.getResources(),R.color.colorAccent,null));
        return lineGraphSeries;
    }
}
