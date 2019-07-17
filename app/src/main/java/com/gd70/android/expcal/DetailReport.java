package com.gd70.android.expcal;

import android.app.DatePickerDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import static com.gd70.android.expcal.MainActivity.checkList;
import static com.gd70.android.expcal.MainActivity.days;
import static com.gd70.android.expcal.MainActivity.months;

public class DetailReport extends AppCompatActivity{

    private DatePickerDialog datePickerDialog;
    private TextView textView;
    private RecyclerView.Adapter adapter;
    Context context;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_report);
        datePickerDialog = new DatePickerDialog(this);
        datePickerDialog.setOnDateSetListener(dateSetListener);
        textView = findViewById(R.id.date);
        DatePicker datePicker = new DatePicker(this);
        int date = datePicker.getDayOfMonth();
        String month = months[datePicker.getMonth()];
        int year = datePicker.getYear();
        String day = days[(datePicker.getDayOfMonth()-1)%7];
        String dateText = date +" "+ month +" "+ year +" ("+ day +")";
        textView.setText(dateText);
        context=this;
        recyclerView = findViewById(R.id.reportRecyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerAdapter(month,date,this);
        recyclerView.setAdapter(adapter);
    }

    public void showCalender(View view)
    {
        datePickerDialog.show();
    }

    DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            String dateText=i2+" "+months[i1]+" "+i+" ("+days[(i2-1)%7]+")";
            textView.setText(dateText);
            DatabaseHandler databaseHandler = new DatabaseHandler(context,months[i1]);
            Cursor datesCursor = databaseHandler.getTables();
            if(datesCursor.moveToFirst())
            {
                do{
                    String name=datesCursor.getString(0);
                    if(name.equals("android_metadata") || name.equals("sqlite_sequence"))
                    {

                    }
                    else
                    {
                        checkList.add(Integer.parseInt(name.split("_")[1]));
                    }
                }while (datesCursor.moveToNext());
            }
            if(checkList.contains(i2)) {
                adapter = new RecyclerAdapter(months[i1],i2,context);
                recyclerView.setAdapter(adapter);
                recyclerView.invalidate();
            }
            else
            {
                recyclerView.setAdapter(null);
            }
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MainActivity mainActivity=MainActivity.getInstance();
        mainActivity.getGraphRecyclerView().smoothScrollToPosition(mainActivity.getCalenderInstance().getIntMonth());

    }

    public void goBack(View view)
    {
       onBackPressed();
    }

    public void displayFilterOptions(View view)
    {

    }
}
