package com.example.organizer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AddToScheduleActivity extends AppCompatActivity {

    private Button addBtn;
    private EditText nameTxt;
    private EditText descpTxt;
    private EditText fromTxt;
    private EditText toTxt;
    private TextView nameReq;
    private TextView timeReq;
    private Spinner daySpinner;
    private DatabaseReference scheduleDataBase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_schedule);
        scheduleDataBase = FirebaseDatabase.getInstance().getReference().child("Schedule");
        nameReq = (TextView) findViewById(R.id.nameReqId);
        timeReq = (TextView) findViewById(R.id.timeReqId);

        addBtn = (Button) findViewById(R.id.addTaskBtn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameReq.setVisibility(View.INVISIBLE);
                timeReq.setVisibility(View.INVISIBLE);
                nameTxt = (EditText) findViewById(R.id.txtNameId);
                descpTxt = (EditText) findViewById(R.id.txtDescpId);
                fromTxt = (EditText) findViewById(R.id.fromId);
                toTxt = (EditText) findViewById(R.id.toId);
                daySpinner = (Spinner) findViewById(R.id.weekDayId);
                String taskName = nameTxt.getText().toString().trim();
                String taskDescp = descpTxt.getText().toString().trim();
                String timeFrom = fromTxt.getText().toString().trim();
                String timeTo = toTxt.getText().toString().trim();
                String weekDay = daySpinner.getSelectedItem().toString();

                if (taskName.equals("") || timeFrom.equals("") || timeTo.equals("")) {
                    if (taskName.equals("")) nameReq.setVisibility(View.VISIBLE);
                    if (timeFrom.equals("")) timeReq.setVisibility(View.VISIBLE);
                    if (timeTo.equals("")) timeReq.setVisibility(View.VISIBLE);
                } else {

                    //add to db
                    HashMap<String, Object> dataMap = new HashMap<>();
                    dataMap.put("Name", taskName);
                    dataMap.put("Description", taskDescp);
                    dataMap.put("From", changeTimeToInt(timeFrom));
                    dataMap.put("To", changeTimeToInt(timeTo));
                    scheduleDataBase.child(weekDay).push().setValue(dataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            //check if stored correctly
                            if (task.isSuccessful()) {
                                Toast.makeText(AddToScheduleActivity.this, "Added", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(AddToScheduleActivity.this, "Error", Toast.LENGTH_LONG).show();

                            }
                        }
                    });

                    finish();
                    Intent intent = new Intent(AddToScheduleActivity.this, ScheduleSubjectListActivity.class);
                    Bundle b = new Bundle();
                    b.putString("day",weekDay);
                    intent.putExtras(b);
                    finish();
                    startActivity(intent);

                }
            }
        });
    }

    public int changeTimeToInt(String time){
        int newTime;
        String newTimeStr;
        newTimeStr = time.substring(0,2)+time.substring(3,5);
        newTime=Integer.parseInt(newTimeStr);
        return newTime;
    }
}
