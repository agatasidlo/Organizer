package com.example.organizer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class ScheduleSubjectListActivity extends AppCompatActivity {

    private ListView subjectsListView;
    private TextView dayNameTextView;
    private String dayName;
    private ArrayList<String> subjectArray;
    private ArrayList<String> fromArray;
    private ArrayList<String> toArray;
    private DatabaseReference scheduleDataBase;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_subject_list);
        scheduleDataBase = FirebaseDatabase.getInstance().getReference().child("Schedule");

        Bundle b = getIntent().getExtras();
        String chosenDay = "";
        if (b != null)
            chosenDay = b.getString("day");

        dayNameTextView = (TextView) findViewById(R.id.dayNameID);
        subjectsListView = (ListView) findViewById(R.id.subjectsID);

        //Toast.makeText(ScheduleSubjectListActivity.this, chosenDay+" chosen", Toast.LENGTH_SHORT).show();

        dayName = dayNameTextView.getText().toString().trim();
        dayNameTextView.setText(chosenDay);

        /*final CustomScheduleAdapter adapter = new CustomScheduleAdapter(this, subjectArray, fromArray, toArray);
        subjectsListView = (ListView) findViewById(R.id.viewList);
        subjectsListView.setAdapter(adapter);*/
    }
}
