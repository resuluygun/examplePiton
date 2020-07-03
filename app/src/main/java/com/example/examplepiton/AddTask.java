package com.example.examplepiton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

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
        public void onClick(View view) {
            taskString = textInputLayout.getEditText().getText().toString().trim();
            Log.v(TAG,"taskString" + taskString);
            Log.v(TAG,"startDate" + startDate);
            Log.v(TAG,"endDate" + endDate);

            if(taskString.isEmpty() || startDate==null || endDate == null ){
                Log.v(TAG,"The task or time can't be empty");

            }
            else{

                if(startDate.after(endDate)) Log.v(TAG,"wrong");
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


                    DBOperations.documentReference.set(task)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                Log.v(TAG,"SUCCESS SAVE");
                                };
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
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