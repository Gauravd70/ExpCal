package com.gd70.android.expcal;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import static com.gd70.android.expcal.MainActivity.days;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {


    private Cursor cursor;
    private Context context;

    public RecyclerAdapter(String month,int date,Context context)
    {
        DatabaseHandler databaseHandler = new DatabaseHandler(context,month);
        cursor=databaseHandler.getReport(days[(date-1)%7]+"_"+date);
        this.context=context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView expenseType,to,time,amount;
        ImageView typeImage;
        public MyViewHolder(View itemView) {
            super(itemView);
            expenseType=itemView.findViewById(R.id.expenseTypeCardView);
            to=itemView.findViewById(R.id.toTextCardView);
            amount=itemView.findViewById(R.id.amountTextCardView);
            time=itemView.findViewById(R.id.timetextCardView);
            typeImage=itemView.findViewById(R.id.typeImage);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View cardView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_layout,parent,false);
        return new MyViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if(cursor!=null) {
            if (cursor.moveToPosition(position)) {
                holder.expenseType.setText(cursor.getString(0));
                holder.amount.setText(Float.toString(cursor.getFloat(1)));
                holder.time.setText(cursor.getString(2));
                holder.to.setText(cursor.getString(3));
                if(cursor.getString(0).equals("Food"))
                {
                    holder.typeImage.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(),R.drawable.pizza,null));
                    holder.expenseType.setContentDescription("Icon made by Smashicons from www.flaticon.com");
                }
                else if(cursor.getString(0).equals("Drinks"))
                {
                    holder.typeImage.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(),R.drawable.pint,null));
                    holder.expenseType.setContentDescription("Icon made by Smashicons from www.flaticon.com");
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        if(cursor==null)
        {
            return 0;
        }
        return cursor.getCount();
    }

}
