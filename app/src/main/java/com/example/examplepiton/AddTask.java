package com.example.examplepiton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class AddTask extends AppCompatActivity implements DatePickerFragment.OnDataPass {
    // TAG for MainActivity class.
    private static String TAG=AddTask.class.getSimpleName();

    //RadioGroup radioGroup;
    Button addTaskButton;
    String taskString, taskTime="";
    TextInputLayout textInputLayout;
    TextView startTextView, endTextView;
    Date startDate, endDate;

    FirebaseFirestore db;
    DocumentReference documentReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        db = FirebaseFirestore.getInstance();
         documentReference = db.collection("tasks").document("userId").
                collection("task_list").document();

        startTextView = findViewById(R.id.text_input_start_date);
        endTextView = findViewById(R.id.text_input_end_date);

        //radioGroup = (RadioGroup) findViewById(R.id.radio_group);

        //textInputLayout
        textInputLayout = findViewById(R.id.text_input_task);

        //for add task button
        addTaskButton = findViewById(R.id.button_add_task);
        //set clickListener for task button
        addTaskButton.setOnClickListener(saveTaskToDB);
    }

    private View.OnClickListener saveTaskToDB = new View.OnClickListener(){
        @Override
        public void onClick(final View view) {
            taskString = textInputLayout.getEditText().getText().toString().trim();


            if(taskString.isEmpty() || startDate==null || endDate == null ){
                Log.v(TAG,"The task or time can't be empty");
                Snackbar.make(view, "Please fill all fields.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
            else{

                if(startDate.after(endDate)) {

                    Snackbar.make(view, "Please choose valid date", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                else{

                    long diffInMillies = Math.abs(endDate.getTime() - startDate.getTime());
                    long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

                    Log.v(TAG,"correct "+ diff);

                    if( diff == 1 || diff==0) taskTime = "Daily";
                    else if (1< diff && diff< 8) taskTime="Weekly";
                    else taskTime = "Monthly";

                    Map<String, Object> task = new HashMap<>();
                    task.put("taskString", taskString);
                    task.put("taskTime", taskTime);
                    task.put("id",DBOperations.documentReference.getId());
                    task.put("startDate", startDate);
                    task.put("endDate", endDate);


                    documentReference.set(task)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Intent intent = new Intent(AddTask.this, MainActivity.class);
                                    startActivity(intent);
                                    Log.v(TAG,"SUCCESS SAVE");
                                };
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Snackbar.make(view, "There's a problem in server. Please try again.", Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                    Log.v(TAG,"failure SAVE");
                                }
                            });



                }

                //create a new task with task content and task time
                Map<String, Object> task = new HashMap<>();


            }
        }
    };

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void startShowDatePickerDialog(View v){

        Bundle bundle = new Bundle();
        bundle.putString("key", "start");

        DialogFragment newFragment = new DatePickerFragment();
        newFragment.setArguments(bundle);
        newFragment.show(getSupportFragmentManager(), "datePicker");


    }

    public void endShowDatePickerDialog(View v){
        Bundle bundle = new Bundle();
        bundle.putString("key", "end");

        DialogFragment newFragment = new DatePickerFragment();
        newFragment.setArguments(bundle);
        newFragment.show(getSupportFragmentManager(), "datePicker");

    }




    /*public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_daily:
                if (checked) Log.v(TAG, "daily");
                taskTime = "daily";
            break;
            case R.id.radio_weeklkly:
                if (checked) Log.v(TAG, "weekly");
                taskTime = "weekly";
                    break;
            case R.id.radio_monthly:
                if (checked) Log.v(TAG, "monthly");
                taskTime = "monthly";
                    break;
        }
    }*/

    @Override
    public void onDataPass(String key, Date date) {
        switch (key){
            case "start":
                startDate =date;
                startTextView.setText(date.toString());
                break;
            case "end":
                endDate = date;
                endTextView.setText(date.toString());
                break;
        }

        Log.d("LOG","hello " + key + " "+ date);

    }
}