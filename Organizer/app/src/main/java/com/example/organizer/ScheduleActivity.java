package com.example.organizer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class ScheduleActivity extends AppCompatActivity {
    public String chosenDay = "";
    private ArrayList<Button> daysBtn = new ArrayList<>();
    private Button addNewSubject;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);


        daysBtn.add((Button) findViewById(R.id.mondayID));
        daysBtn.add((Button) findViewById(R.id.tuesdayID));
        daysBtn.add((Button) findViewById(R.id.wednesdayID));
        daysBtn.add((Button) findViewById(R.id.thursdayID));
        daysBtn.add((Button) findViewById(R.id.fridayID));

        addNewSubject = (Button) findViewById(R.id.addNewSubjectBtn);


        daysBtn.get(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToScheduleSubjectListActivity("Monday");
            }
        });

        daysBtn.get(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToScheduleSubjectListActivity("Tuesday");
            }
        });

        daysBtn.get(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToScheduleSubjectListActivity("Wednesday");

            }
        });

        daysBtn.get(3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToScheduleSubjectListActivity("Thursday");
            }
        });

        daysBtn.get(4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToScheduleSubjectListActivity("Friday");
            }
        });

        addNewSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScheduleActivity.this, AddToScheduleActivity.class);
                startActivity(intent);
            }
        });

    }


    private void goToScheduleSubjectListActivity(String chosenDay) {
        Intent intent = new Intent(ScheduleActivity.this, ScheduleSubjectListActivity.class);
        Bundle b = new Bundle();
        b.putString("day",chosenDay);
        intent.putExtras(b);
        startActivity(intent);
    }
}
