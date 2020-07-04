package com.example.examplepiton;

import java.util.Date;

public class TaskPlan {

    private  String mId;
    private  String mTaskString;
    private  String mTaskTime;
    private Date mStartDate;
    private  Date mEndDate;

    public TaskPlan(){}

    public TaskPlan(String id, String taskString, String taskTime, Date startDate, Date endDate){

        mId = id;
        mTaskString = taskString;
        mTaskTime = taskTime;
        mStartDate = startDate;
        mEndDate = endDate;
    }

    public String getmId() { return mId; }

    public String getmTaskString() { return mTaskString; }

    public String getmTaskTime() { return mTaskTime; }

    public Date getmStartDate() { return mStartDate; }

    public Date getmEndDate() { return mEndDate; }


}
