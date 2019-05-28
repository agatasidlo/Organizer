package com.example.organizer;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import static java.util.logging.Logger.global;

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
    private boolean timeSlotsResult;
    private FirebaseAuth dataBaseAuth;
    private String userId;

    //if back button clicked return to previous day
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Intent intent = new Intent(AddToScheduleActivity.this, ScheduleSubjectListActivity.class);
            intent.putExtras(getIntent().getExtras());
            finish();
            startActivity(intent);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_schedule);
        dataBaseAuth = FirebaseAuth.getInstance();
        userId = dataBaseAuth.getCurrentUser().getUid();
        scheduleDataBase = FirebaseDatabase.getInstance().getReference().child(userId).child("Schedule");
        nameReq = (TextView) findViewById(R.id.nameReqId);
        timeReq = (TextView) findViewById(R.id.timeReqId);
        fromTxt = (EditText) findViewById(R.id.fromId);
        toTxt = (EditText) findViewById(R.id.toId);


        fromTxt.setKeyListener(null);
        Button addFrom = findViewById(R.id.addFromBtn);
        addFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeSlotsResult = true;
                Calendar mcurrentTime = Calendar.getInstance();
                final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                final int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(AddToScheduleActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String hourStr = Integer.toString(selectedHour);
                        String minuteStr = Integer.toString(selectedMinute);
                        if (selectedHour<10) hourStr="0"+hourStr;
                        if (selectedMinute<10) minuteStr = "0"+minuteStr;
                        fromTxt.setText(hourStr + ":" + minuteStr);
                    }
                }, hour+2, minute, true);
                mTimePicker.show();
            }
        });

        toTxt.setKeyListener(null);
        Button addTo = findViewById(R.id.addToBtn);
        addTo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                timeSlotsResult = true;
                Calendar mcurrentTime = Calendar.getInstance();
                final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                final int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(AddToScheduleActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String hourStr = Integer.toString(selectedHour);
                        String minuteStr = Integer.toString(selectedMinute);
                        if (selectedHour<10) hourStr="0"+hourStr;
                        if (selectedMinute<10) minuteStr = "0"+minuteStr;
                        toTxt.setText(hourStr + ":" + minuteStr);
                    }
                }, hour+2, minute, true);
                mTimePicker.show();
            }
        });

        addBtn = (Button) findViewById(R.id.addTaskBtn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameReq.setVisibility(View.INVISIBLE);
                timeReq.setVisibility(View.INVISIBLE);
                nameTxt = (EditText) findViewById(R.id.txtNameId);
                descpTxt = (EditText) findViewById(R.id.txtDescpId);
                daySpinner = (Spinner) findViewById(R.id.weekDayId);
                final String taskName = nameTxt.getText().toString().trim();
                final String taskDescp = descpTxt.getText().toString().trim();
                final String timeFrom = fromTxt.getText().toString().trim();
                final String timeTo = toTxt.getText().toString().trim();
                String weekDay = daySpinner.getSelectedItem().toString();
                final String weekDayEng;

                switch (weekDay) {
                    case "Poniedziałek": weekDayEng = "Monday"; break;
                    case "Wtorek": weekDayEng = "Tuesday"; break;
                    case "Środa": weekDayEng = "Wednesday"; break;
                    case "Czwartek": weekDayEng = "Thursday"; break;
                    default: weekDayEng = "Friday"; break;
                }

                scheduleDataBase.child(weekDayEng).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        timeSlotsResult=true;
                        if (taskName.equals("") || timeFrom.equals("") || timeTo.equals("")) {
                            if (taskName.equals("")) nameReq.setVisibility(View.VISIBLE);
                            if (timeFrom.equals("") || timeTo.equals("")) timeReq.setVisibility(View.VISIBLE);
                        } else {
                                if(changeTimeToInt(timeFrom)>=changeTimeToInt(timeTo)) timeSlotsResult = false;
                                else {
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        int timeFromDb = snapshot.child("From").getValue(Integer.class);
                                        int timeToDb = snapshot.child("To").getValue(Integer.class);
                                        if (!(changeTimeToInt(timeTo) <= timeFromDb || changeTimeToInt(timeFrom) >= timeToDb))
                                            timeSlotsResult = false;
                                    }
                                }

                               if (!timeSlotsResult) {
                                    timeReq.setVisibility(View.VISIBLE);
                                    timeReq.setText("Nieprawidłowy czas!");

                               }
                                else{

                                //add to db
                                HashMap<String, Object> dataMap = new HashMap<>();
                                dataMap.put("Name", taskName);
                                dataMap.put("Description", taskDescp);
                                dataMap.put("From", changeTimeToInt(timeFrom));
                                dataMap.put("To", changeTimeToInt(timeTo));

                                scheduleDataBase.child(weekDayEng).push().setValue(dataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        //check if stored correctly
                                        if (task.isSuccessful()) {
                                            Toast.makeText(AddToScheduleActivity.this, "Dodano", Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(AddToScheduleActivity.this, "Błąd", Toast.LENGTH_LONG).show();

                                        }
                                    }
                                });

                                finish();
                                Intent intent = new Intent(AddToScheduleActivity.this, ScheduleSubjectListActivity.class);
                                Bundle b = new Bundle();
                                b.putString("day", weekDayEng);
                                intent.putExtras(b);
                                finish();
                                startActivity(intent);

                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


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
