package com.example.examplepiton;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Object>  mDataSet;

    //Determine the layouts according this Integer
    private static final int TASK_ITEM=0;
    private static final int HEADER_ITEM=1;

    //instance of ItemClickListener
    private final ItemClickListener myOnClickHandler;


    //Adapter constructor
    public MyAdapter(ArrayList<Object> myDataset, ItemClickListener onClickHandler) {
        mDataSet = myDataset;
        myOnClickHandler = onClickHandler;
    }

    
    //
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

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
    class TaskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView textViewTaskString, textViewTaskTime, textViewTaskStartTime, textViewTaskEndTime;
        //constructor
        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTaskString = (TextView) itemView.findViewById(R.id.text_view_task_string);
            textViewTaskTime = (TextView) itemView.findViewById(R.id.text_view_task_time);
            textViewTaskStartTime = (TextView) itemView.findViewById(R.id.text_view_task_start_time);
            textViewTaskEndTime = (TextView) itemView.findViewById(R.id.text_view_task_end_time);

            itemView.setOnClickListener(this);
        }
        //set fields
        private void setTaskDetails(Object task){
            TaskPlan currentTask=(TaskPlan) task;
            String startDate = currentTask.getmStartDate().toString().substring(0,11) + currentTask.getmStartDate().toString().substring(30,34) ;
            String endDate = currentTask.getmEndDate().toString().substring(0,11) +  currentTask.getmEndDate().toString().substring(30,34);

            textViewTaskString.setText(currentTask.getmTaskString());
            textViewTaskTime.setText(currentTask.getmTaskTime());
            textViewTaskStartTime.setText(startDate);
            textViewTaskEndTime.setText(endDate);
        }
        //Show dialog if user click to task in the RecyclerView/MainActivity
        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            myOnClickHandler.onClick(mDataSet.get(adapterPosition), adapterPosition);
        }
    }

    //Pass the clicked object to MainActiviy from adapter
    public interface ItemClickListener {
        void onClick(Object data, Integer position);
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
        // set the layout according object's type(Header or Task)
        if(getItemViewType(position) == TASK_ITEM){

            ((TaskViewHolder) holder).setTaskDetails(mDataSet.get(position));
        }
        else{

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
