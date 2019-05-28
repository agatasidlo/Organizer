package com.example.organizer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class MenuActivity extends AppCompatActivity {
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Button listButton = (Button) findViewById(R.id.listButton);
        Button calendarButton = (Button) findViewById(R.id.calendarButton);
        Button scheduleButton = (Button) findViewById(R.id.scheduleButton);
        Button logoutButton = (Button) findViewById(R.id.logoutBtn);


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

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MenuActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        });


    }

    private void goToListActivity() {

        Intent intent = new Intent(MenuActivity.this, ListActivity.class);

        startActivity(intent);

    }

    private void goToCalendarActivity() {

        Intent intent = new Intent(MenuActivity.this, CalendarActivity.class);

        startActivity(intent);

    }

    private void goToScheduleActivity() {

        Intent intent = new Intent(MenuActivity.this, ScheduleSubjectListActivity.class);
        Bundle b = new Bundle();
        b.putString("day","Monday");
        intent.putExtras(b);
        startActivity(intent);

    }

}
