package com.example.examplepiton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // TAG for MainActivity class.
    private static String TAG=MainActivity.class.getSimpleName();

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private ArrayList<TaskPlan> taskArrayList = new ArrayList<>();
    private ArrayList<Object> objectTaskList = new ArrayList<>();

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference collectionReference;

    //private ArrayList<TaskPlan> taskArrayList = new ArrayList<>();

    private String[] stringList = {"A", "B", "C", "D", "E","..."};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        collectionReference = db.collection("tasks").document("userId").
                collection("task_list");
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        //mAdapter = new MyAdapter(taskArrayList);
        mAdapter = new MyAdapter(objectTaskList);

        recyclerView.setAdapter(mAdapter);

        getDataFromDB();



        // under this lineeeeeee
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addTaskIntent = new Intent(MainActivity.this, AddTask.class);
                startActivity(addTaskIntent);

            }
        });
    }


    public void getDataFromDB(){
        taskArrayList.clear();
        objectTaskList.clear();

        collectionReference.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {


                                taskArrayList.add(new TaskPlan(document.get("id").toString(), document.get("taskString").toString(),
                                        document.get("taskTime").toString(), document.getTimestamp("startDate").toDate(),
                                        document.getTimestamp("endDate").toDate()));
                                //Log.d(TAG, "TempDocument "+ taskArrayList);
                            }
                            List<String> times = new ArrayList<>(Arrays.asList("Daily","Weekly","Monthly"));
                            Log.d(TAG, "TaskArrayList  "+ taskArrayList);

                            //counter for delete a category header that don't have any items
                            int counter;
                            for(String time : times){
                                Log.d(TAG, "Time  "+ time);

                                objectTaskList.add(new String(time));
                                counter = 0;
                                //iterate each clothes and add it to the obj list as ordered
                                for(TaskPlan t: taskArrayList){
                                    //Log.d(TAG, "TaskPlanTime  "+ t.getmTaskTime());

                                    if(t.getmTaskTime().equals(time)){
                                        objectTaskList.add(t);
                                        counter++;
                                    }

                                }
                                //if category don't have any clothes, delete it from list
                                //that will appear on listview
                                if(counter==0) objectTaskList.remove(objectTaskList.size()-1);
                            }
                         //   Log.d(TAG, "objectTaskList--->  "+ objectTaskList);
//                            for(Object item : objectTaskList ){
//                                Log.d(TAG, "objectTaskList item--->  "+ item);
//                            }

                            mAdapter.notifyDataSetChanged();
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }

                    }
                });



    }
}