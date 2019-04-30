package com.example.organizer;

import android.content.Intent;
import android.database.DataSetObserver;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TableLayout;

import java.util.ArrayList;

public class ScheduleActivity extends AppCompatActivity {
    private String chosenDay;
    private ListView daysList;
    private ArrayList<Button> daysBtn = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        daysBtn.add((Button) findViewById(R.id.mondayID));
        daysBtn.add((Button) findViewById(R.id.tuesdayID));
        daysBtn.add((Button) findViewById(R.id.wednesdayID));
        daysBtn.add((Button) findViewById(R.id.thursdayID));
        daysBtn.add((Button) findViewById(R.id.fridayID));

        for (int i = 0; i<5; i++) {
            final  int whichDay = i;
            daysBtn.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (whichDay) {
                        case 0: chosenDay = "Monday";
                        case 1: chosenDay = "Tuesday";
                        case 2: chosenDay = "Wednesday";
                        case 3: chosenDay = "Thursday";
                        case 4: chosenDay = "Friday";
                    }


                    Intent intent = new Intent(ScheduleActivity.this, ScheduleSubjectListActivity.class);
                    startActivity(intent);
                }
            });
        }
    }
}
