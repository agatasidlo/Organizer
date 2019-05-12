package com.example.organizer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button listButton = (Button) findViewById(R.id.listButton);
        Button calendarButton = (Button) findViewById(R.id.calendarButton);
        Button scheduleButton = (Button) findViewById(R.id.scheduleButton);

        listButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                goToListActivity();

            }

        });

        calendarButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                goToCalendarActivity();

            }

        });

        scheduleButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                goToScheduleActivity();

            }

        });


    }

    private void goToListActivity() {

        Intent intent = new Intent(MainActivity.this, ListActivity.class);

        startActivity(intent);

    }

    private void goToCalendarActivity() {

        Intent intent = new Intent(MainActivity.this, CalendarActivity.class);

        startActivity(intent);

    }

    private void goToScheduleActivity() {

        Intent intent = new Intent(MainActivity.this, ScheduleSubjectListActivity.class);
        Bundle b = new Bundle();
        b.putString("day","Monday");
        intent.putExtras(b);
        startActivity(intent);

    }

}
