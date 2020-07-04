package com.example.examplepiton;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {


    //private String[] mDataSet;
    private ArrayList<TaskPlan>  mDataSet;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView textViewTaskString, textViewTaskTime, textViewTaskStartTime, textViewTaskEndTime;

        public MyViewHolder(View itemView) {
            super(itemView);
            textViewTaskString = (TextView) itemView.findViewById(R.id.text_view_task_string);
            textViewTaskTime = (TextView) itemView.findViewById(R.id.text_view_task_time);
            textViewTaskStartTime = (TextView) itemView.findViewById(R.id.text_view_task_start_time);
            textViewTaskEndTime = (TextView) itemView.findViewById(R.id.text_view_task_end_time);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(ArrayList<TaskPlan> myDataset) {
        mDataSet = myDataset;
    }


    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // create a new view
        View view =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, int position) {
        holder.textViewTaskString.setText(mDataSet.get(position).getmTaskString());
        holder.textViewTaskTime.setText(mDataSet.get(position).getmTaskTime());
        holder.textViewTaskStartTime.setText(mDataSet.get(position).getmStartDate().toString());
        holder.textViewTaskEndTime.setText(mDataSet.get(position).getmEndDate().toString());


    }

    @Override
    public int getItemCount() {

        return mDataSet.size();
    }
}
