package com.example.examplepiton;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    //private String[] mDataSet;
    //private ArrayList<TaskPlan>  mDataSet;
    private ArrayList<Object>  mDataSet;
    private static final int TASK_ITEM=0;
    private static final int HEADER_ITEM=1;


    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(ArrayList<Object> myDataset) {
        mDataSet = myDataset;
    }




    //find list_item and send to MyViewHolder
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // create a new view
//        View view =  LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.list_item, parent, false);
//        MyViewHolder viewHolder = new MyViewHolder(view);
//        return viewHolder;

        View view;
        if (viewType == TASK_ITEM ) { // for task layout
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item, parent, false);
            return new TaskViewHolder(view);
        }
        else {  // for header layout
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.task_header_view, parent, false);
            return  new HeaderViewHolder(view);
        }
    }


    // Task's layout
    class TaskViewHolder extends RecyclerView.ViewHolder{
        public TextView textViewTaskString, textViewTaskTime, textViewTaskStartTime, textViewTaskEndTime;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTaskString = (TextView) itemView.findViewById(R.id.text_view_task_string);
            textViewTaskTime = (TextView) itemView.findViewById(R.id.text_view_task_time);
            textViewTaskStartTime = (TextView) itemView.findViewById(R.id.text_view_task_start_time);
            textViewTaskEndTime = (TextView) itemView.findViewById(R.id.text_view_task_end_time);
        }

        private void setTaskDetails(Object task){
            TaskPlan currentTask=(TaskPlan) task;

            textViewTaskString.setText(currentTask.getmTaskString());
            textViewTaskTime.setText(currentTask.getmTaskTime());
            textViewTaskStartTime.setText(currentTask.getmStartDate().toString());
            textViewTaskEndTime.setText(currentTask.getmEndDate().toString());
        }
    }

    //Header's layout
    class HeaderViewHolder extends  RecyclerView.ViewHolder{
        public TextView textViewHeader;

        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewHeader = (TextView) itemView.findViewById(R.id.task_header);
        }

        public  void setHeaderetails(Object headerTag){

            textViewHeader.setText(headerTag.toString());

        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Log.v("Adapter", ""+position);
        if(getItemViewType(position) == TASK_ITEM){
            Log.v("ViewType", "TASK");

            ((TaskViewHolder) holder).setTaskDetails(mDataSet.get(position));
        }
        else{
            Log.v("ViewType", "HEADER");

            ((HeaderViewHolder) holder).setHeaderetails(mDataSet.get(position));

        }

    }

    @Override
    public int getItemViewType(int position) {
        if(mDataSet.get(position) instanceof  TaskPlan) return TASK_ITEM;
        else return  HEADER_ITEM;
    }


    public Object getItem(int position) {
        return mDataSet.get(position);
    }

    @Override
    public int getItemCount() {

        return mDataSet.size();
    }
}
