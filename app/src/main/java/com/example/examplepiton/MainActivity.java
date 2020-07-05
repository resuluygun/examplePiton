package com.example.examplepiton;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MyAdapter.ItemClickListener  {

    // TAG for MainActivity class.
    private static String TAG=MainActivity.class.getSimpleName();


    //Recyclerview
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    //Store data came from db
    private ArrayList<TaskPlan> taskArrayList = new ArrayList<>();
    //Adapter data provider
    private ArrayList<Object> objectTaskList = new ArrayList<>();

    //Db
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference collectionReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        collectionReference = db.collection("tasks").document("userId").
                collection("task_list");


        //Recyclerview initilaze
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new MyAdapter(objectTaskList, this);

        recyclerView.setAdapter(mAdapter);

        getDataFromDB();



        //Create floating action button and set onClicklistener
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
        //clear the lists for prevent to duplicate
        taskArrayList.clear();
        objectTaskList.clear();

        //get data from tasks/userId/task_list
            // --this userId static. you can get data dynamically with mAuth.getUserId() if you us authentication
        //task_list is a collection and has document/documents
        collectionReference.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                //add every task to taskArrayList
                                taskArrayList.add(new TaskPlan(document.get("id").toString(), document.get("taskString").toString(),
                                        document.get("taskTime").toString(), document.getTimestamp("startDate").toDate(),
                                        document.getTimestamp("endDate").toDate()));
                            }
                            //Time tags
                            List<String> times = new ArrayList<>(Arrays.asList("Daily","Weekly","Monthly"));

                            //counter for delete a category header that don't have any items
                            int counter;
                            for(String time : times){ // look all tags in list
                                //add the time objectTaskList as a String Object
                                objectTaskList.add(new String(time));
                                counter = 0;
                                for(TaskPlan t: taskArrayList){ // look all tasks list
                                    //if taskTime matches time add to objectTaskList
                                    if(t.getmTaskTime().equals(time)){
                                        objectTaskList.add(t);
                                        counter++;
                                    }
                                }
                                // remove "time" from objectTaskList if counter==0(it means there's no task from this "time")
                                if(counter==0) objectTaskList.remove(objectTaskList.size()-1);
                            }
                            mAdapter.notifyDataSetChanged();
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }

                    }
                });
    }

    @Override
    public void onClick(final Object data, final Integer position) {
        final TaskPlan tempTask = (TaskPlan) data;

        Log.d(TAG, "DELETE ıd"+tempTask.getmId() );

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to delete this task?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        db.collection("tasks").document("userId").
                                collection("task_list").document(tempTask.getmId())
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "DELETE SUCCESS");

                                        objectTaskList.remove(data);

                                        mAdapter.notifyItemRemoved(position);
                                        mAdapter.notifyDataSetChanged();

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {

                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG, "DELETE FAILURE");


                                    }
                                });

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });

        builder.create();
        builder.show();

    }

}