package com.example.organizer;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class CalendarActivity extends AppCompatActivity {
    private CalendarView calendar;
    private DatabaseReference calendarDataBase;
    private ArrayList<String> keysArray = new ArrayList<>();
    private ArrayList<String> daysArray= new ArrayList<>();
    private ArrayList<String> monthsArray = new ArrayList<>();
    private ArrayList<String> yearsArray = new ArrayList<>();
    private ArrayList<String> notesArray = new ArrayList<>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        calendarDataBase = FirebaseDatabase.getInstance().getReference().child("Calendar");


       // final CustomCalendarAdapter adapter = new CustomCalendarAdapter(this, notesArray, daysArray, monthsArray,yearsArray);
        calendar = (CalendarView) findViewById(R.id.calendarViewId);
       // calendar.setAdapter(adapter);

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, final int year, final int month, final int dayOfMonth) {
                final EditText editText = new EditText(CalendarActivity.this);
                editText.setText("", TextView.BufferType.EDITABLE);
                String date = dayOfMonth + "/" + (month+1) + "/" + year;
                AlertDialog dialogShowItem = new AlertDialog.Builder(CalendarActivity.this)
                        .setTitle(date)
                        .setView(editText)
                        .setMessage("Make a note!")
                        .setNeutralButton("Save", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                HashMap<String, String> dataMap = new HashMap<>();
                                dataMap.put("Note", editText.getText().toString());
                                dataMap.put("Day", Integer.toString(dayOfMonth));
                                dataMap.put("Month", Integer.toString(month));
                                dataMap.put("Year", Integer.toString(year));

                                calendarDataBase.push().setValue(dataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        //check if stored correctly
                                        if(task.isSuccessful()) {
                                            Toast.makeText(CalendarActivity.this,  "Added", Toast.LENGTH_LONG).show();
                                        }
                                        else {
                                            Toast.makeText(CalendarActivity.this,  "Error", Toast.LENGTH_LONG).show();

                                        }
                                    }
                                });
                              //?  calendarDataBase.child(keysArray.get(which)).getRef().removeValue();
                            }
                        })
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                               //? calendarDataBase.child(keysArray.get(which)).getRef().removeValue();
                            }
                        })
                        .setNegativeButton("Close", null).create();

                dialogShowItem.show();

        }

     });


       calendarDataBase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String id = dataSnapshot.getKey();
                keysArray.add(id);
                String nameData = dataSnapshot.child("Note").getValue(String.class);
                String dayData = dataSnapshot.child("Day").getValue(String.class);
                String monthData = dataSnapshot.child("Month").getValue(String.class);
                String yearData = dataSnapshot.child("Year").getValue(String.class);
                notesArray.add(nameData);
                daysArray.add(dayData);
                monthsArray.add(monthData);
                yearsArray.add(yearData);
               // adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String nameData = dataSnapshot.child("Note").getValue(String.class);
                String dayData = dataSnapshot.child("Day").getValue(String.class);
                String monthData = dataSnapshot.child("Month").getValue(String.class);
                String yearData = dataSnapshot.child("Year").getValue(String.class);
                String key = dataSnapshot.getKey();
                int index = keysArray.indexOf(key);
                notesArray.set(index, nameData);
                daysArray.set(index, dayData);
                monthsArray.set(index, monthData);
                yearsArray.set(index, yearData);
                //adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                String key = dataSnapshot.getKey();
                int index = keysArray.indexOf(key);
                keysArray.remove(index);
                notesArray.remove(index);
                daysArray.remove(index);
                monthsArray.remove(index);
                yearsArray.remove(index);
               // adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
