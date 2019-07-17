package com.gd70.android.expcal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private View new_expense_view;
    private String expense_type;
    private Spinner expenseTypeSpinner;
    private Context context;
    private CharSequence time;
    private TextClock textClock;
    static ArrayList<Integer> checkList = new ArrayList<>();
    private EditText to;
    private calender cal;
    private RecyclerView recyclerView;
    private static MainActivity instance;
    private GraphRecyclerAdapter graphRecyclerAdapter;

    static String[] months = new String[]{"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
    static String[] days = new String[]{"Mon","Tue","Wed","Thu","Fri","Sat","Sun"};
    private TextView avgAmt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FrameLayout new_expense_frame = findViewById(R.id.new_expense_Frame);
        new_expense_view = FrameLayout.inflate(this,R.layout.new_expense_layout, new_expense_frame);
        expenseTypeSpinner = new_expense_view.findViewById(R.id.type);
        expenseTypeSpinner.setOnItemSelectedListener(itemSelectedListener);
        Button addButton = new_expense_view.findViewById(R.id.add_button);
        addButton.setOnClickListener(onClickListener);
        textClock = new_expense_view.findViewById(R.id.clock);
        to = new_expense_view.findViewById(R.id.toEditText);
        avgAmt = findViewById(R.id.avgExp);
        context=this;
        cal = new calender();
        instance=this;
        recyclerView = findViewById(R.id.graphRecyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,true);
        recyclerView.setLayoutManager(layoutManager);
        graphRecyclerAdapter = new GraphRecyclerAdapter(context,avgAmt);
        recyclerView.smoothScrollToPosition(cal.getIntMonth());
        recyclerView.setAdapter(graphRecyclerAdapter);
        avgAmt.setText(Float.toString(getAvgAmt()));
    }

    public RecyclerView getGraphRecyclerView()
    {
        return recyclerView;
    }

    public static MainActivity getInstance()
    {
        return instance;
    }

    public calender getCalenderInstance()
    {
        return cal;
    }


    public float getAmount()
    {
        EditText amountText = new_expense_view.findViewById(R.id.amountEditText);
        String amount =amountText.getText().toString();
        if(!amount.isEmpty())
        {
            time= textClock.getText();
            return Float.parseFloat(amount);
        }
        else
        {
            Toast toast = Toast.makeText(context,"Enter the amount",Toast.LENGTH_LONG);
            toast.show();
            return 0;
        }
    }

    AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            expense_type=expenseTypeSpinner.getItemAtPosition(i).toString();
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(getAmount()!=0 && !to.getText().toString().isEmpty()) {
                String month = cal.getMonth();
                int date = cal.getDate();
                String day = cal.getDay();
                DatabaseHandler databaseHandler = new DatabaseHandler(context,month);
                if(!checkList.contains(date)) {
                    databaseHandler.AddDate( day+"_"+date);
                    checkList.add(date);
                }
                databaseHandler.AddExpense(expense_type,getAmount(),time.toString(),to.getText().toString());
                graphRecyclerAdapter.notifyDataSetChanged();
                Toast toast = Toast.makeText(context, "Expense Added Successfully", Toast.LENGTH_LONG);
                toast.show();
            }
        }
    };

    public void getReport(View view)
    {
        Intent intent = new Intent(this,DetailReport.class);
        startActivity(intent);
    }

    public void onSettingsClick(View view)
    {
        Intent intent = new Intent(MainActivity.this,SettingsActivity.class);
        startActivity(intent);
    }

    public class calender
    {

        private final Calendar cal;

        calender()
        {
            cal = Calendar.getInstance();
        }

        public String  getMonth()
        {
            return months[cal.get(Calendar.MONTH)];
        }

        public int getDate()
        {
            return cal.get(Calendar.DAY_OF_MONTH);
        }

        public String getDay()
        {
            return days[(this.getDate()-1)%7];
        }

        public int getIntMonth()
        {
            return cal.get(Calendar.MONTH);
        }

        public int getYear()
        {
            return cal.get(Calendar.YEAR);
        }
    }

    public float getAvgAmt()
    {
        SharedPreferences sharedPreferences = this.getSharedPreferences("AvgAmt",Context.MODE_PRIVATE);
        return sharedPreferences.getFloat("Avg",1.2f);
    }
}
