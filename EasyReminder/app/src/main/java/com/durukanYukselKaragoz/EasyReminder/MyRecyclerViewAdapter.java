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

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder> {

    Context context;

    MainActivity mainActivity;
    ArrayList<Event> events;
    DatabaseHelper db;
    boolean allTasks; //if true, use it for all tasks and keep the delete button, if not hide it

    private final int TITLE_MAX_CHARACTERS = 25;

    public MyRecyclerViewAdapter(Context context, ArrayList<Event> events) {
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
        View v = LayoutInflater.from(context).inflate(R.layout.recycler_layout, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        //BIND DATA
        final Event event = events.get(position);
        holder.tvName.setText(ExtendedStringFormatter.ellipsize(event.getEventName(), TITLE_MAX_CHARACTERS));

        holder.tvDate.setText(event.getTime());
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = event.getId();
                EventDB.deleteEvent(db, event.getId());
                mainActivity.callRefresh();
                Intent notificationIntent = new Intent(context, AlarmReceiver.class);
                PendingIntent broadcast = PendingIntent.getBroadcast(context, (int) id, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                alarmManager.cancel(broadcast);
            }
        });
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        Button btnDelete;
        TextView tvName, tvDate, tvDetails;

        MyViewHolder(View viewItem){
            super(viewItem);
            tvName = viewItem.findViewById(R.id.recycName);
            tvDate = viewItem.findViewById(R.id.recycDate);
            btnDelete = viewItem.findViewById(R.id.recycDelete2);
        }
    }
}