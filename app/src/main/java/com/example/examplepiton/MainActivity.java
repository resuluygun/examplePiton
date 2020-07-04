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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    // TAG for MainActivity class.
    private static String TAG=MainActivity.class.getSimpleName();

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private ArrayList<TaskPlan> taskArrayList = new ArrayList<>();
    //private ArrayList<TaskPlan> taskArrayList = new ArrayList<>();

    private String[] stringList = {"A", "B", "C", "D", "E","..."};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //taskArrayList.add(new TaskPlan("dfsfd","dsfdfsdf","dfdfgd", new Date(), new Date()));

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        //mAdapter = new MyAdapter(taskArrayList);
        mAdapter = new MyAdapter(taskArrayList);

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

        DBOperations.collectionReference.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {


                                taskArrayList.add(new TaskPlan(document.get("id").toString(), document.get("taskString").toString(),
                                        document.get("taskTime").toString(), document.getTimestamp("startDate").toDate(),
                                        document.getTimestamp("endDate").toDate()));
                                Log.d(TAG, "TempDocument "+ taskArrayList);
                            }
                            mAdapter.notifyDataSetChanged();
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }

                    }
                });
    }
}