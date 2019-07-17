package com.gd70.android.expcal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper {

    static private int DATABASE_VERSION=1;
    static private String table_name;
    private Context context;

    DatabaseHandler(Context context, String month)
    {
        super(context,month,null,DATABASE_VERSION);
        this.context=context;
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void AddDate(String table)
    {
        Log.d("main", "AddDate: "+table);
        this.getWritableDatabase().execSQL("create table if not exists "+table+"( _id INTEGER PRIMARY KEY AUTOINCREMENT , expense_type TEXT ,amount FLOAT, time TEXT, sent_to TEXT )");
        table_name=table;
    }

    public void AddExpense(String expense_type,float amount,String time,String to)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put("expense_type",expense_type);
        contentValues.put("amount",amount);
        contentValues.put("time",time);
        contentValues.put("sent_to",to);
        this.getWritableDatabase().insert(table_name,null,contentValues);
        Log.d("main", "AddExpense: Successful");
    }

    public Cursor getReport(String table) {
        if (!table.equals("android_metadata") && !table.equals("sqlite_sequence")) {
            try {
                return this.getReadableDatabase().query(table, new String[]{"expense_type", "amount", "time", "sent_to"}, null, null, null, null, null);
            }
            catch (SQLException e)
            {

            }
        }
        return null;
    }

    public Cursor getTables()
    {
        return this.getReadableDatabase().rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
    }
}
