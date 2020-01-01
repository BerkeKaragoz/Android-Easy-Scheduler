package com.durukanYukselKaragoz.EasyReminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyRecyclerViewAdapter2 extends RecyclerView.Adapter<MyRecyclerViewAdapter2.MyViewHolder> {

    Context context;

    MainActivity mainActivity;
    ArrayList<Event> events;
    DatabaseHelper db;
    boolean allTasks; //if true, use it for all tasks and keep the delete button, if not hide it

    public MyRecyclerViewAdapter2(Context context, ArrayList<Event> events) {
        this.context = context;
        mainActivity = (MainActivity)context;
        this.events = events;
        db = new DatabaseHelper(context);
    }

    public void modifyData(){
        notifyDataSetChanged();
        notifyItemRangeChanged(0, events.size());
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.recycler_layout2, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        //BIND DATA
        final Event event = events.get(position);
        holder.tvName.setText(event.getEventName());
        String date = event.getYear() + "/" + event.getMonth() + "/" + event.getDay() + "   " + String.format("%02d", event.getHour()) + ":" + String.format("%02d", event.getMinute());
        holder.tvDate.setText(date);
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tvName, tvDate;

        MyViewHolder(View viewItem){
            super(viewItem);
            tvName = viewItem.findViewById(R.id.recycNameToday);
            tvDate = viewItem.findViewById(R.id.recycDateToday);
        }
    }
}