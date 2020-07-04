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


    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private ArrayList<TaskPlan> taskArrayList = new ArrayList<>();
    private ArrayList<Object> objectTaskList = new ArrayList<>();

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference collectionReference;


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
        mAdapter = new MyAdapter(objectTaskList, this);

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
                                for(TaskPlan t: taskArrayList){

                                    if(t.getmTaskTime().equals(time)){
                                        objectTaskList.add(t);
                                        counter++;
                                    }

                                }
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