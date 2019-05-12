package com.example.organizer;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;



public class CalendarActivity extends AppCompatActivity {
    private CompactCalendarView calendar;
    private TextView dataView;
    private TextView monthView;
    private Button addNoteBtn;
    private ListView calendarList;
    private DatabaseReference calendarDataBase;
    private ArrayList<String> keysArray = new ArrayList<>();
    private ArrayList<String> daysArray= new ArrayList<>();
    private ArrayList<String> monthsArray = new ArrayList<>();
    private ArrayList<String> yearsArray = new ArrayList<>();
    private ArrayList<String> notesArray = new ArrayList<>();
    private Date visiblePosition = Calendar.getInstance().getTime();
    private ArrayList<String> notesArrayVisible = new ArrayList<>();
    private ArrayList<String> daysArrayVisible = new ArrayList<>();
    private ArrayList<String> monthsArrayVisible = new ArrayList<>();
    private ArrayList<String> yearArrayVisible = new ArrayList<>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        calendarDataBase = FirebaseDatabase.getInstance().getReference().child("Calendar");


        //display current date
        dataView = (TextView) findViewById(R.id.dataTextId);
        Date currentTime = Calendar.getInstance().getTime();
        String currentDate = DateFormat.getDateInstance(DateFormat.FULL, Locale.ENGLISH).format(currentTime);
        dataView.setText(currentDate);

        //display current month
        monthView = (TextView) findViewById(R.id.monthViewId);
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentTime);
        final int month = cal.get(Calendar.MONTH);
        String[] monthTab = {"January", "February",
                "March", "April", "May", "June", "July",
                "August", "September", "October", "November",
                "December"};
        String monthTxt = monthTab[month];
        monthView.setText(monthTxt);


        setVisible();
        calendar = (CompactCalendarView) findViewById(R.id.calendarViewId);
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        String[] daysTab = {"M","T","W","T","F","S","S"};
        calendar.setDayColumnNames(daysTab);
        final CustomCalendarAdapter adapter = new CustomCalendarAdapter(this, notesArrayVisible, daysArrayVisible, monthsArrayVisible,yearArrayVisible);

        calendarList = (ListView) findViewById(R.id.listCalendarId);
        calendarList.setAdapter(adapter);


        calendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(final Date dateClicked) {
                visiblePosition = dateClicked;
                setVisible();
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                monthView = (TextView) findViewById(R.id.monthViewId);
                Calendar cal = Calendar.getInstance();
                cal.setTime(firstDayOfNewMonth);
                final int month = cal.get(Calendar.MONTH);
                String[] monthTab = {"January","February",
                        "March", "April", "May", "June", "July",
                        "August", "September", "October", "November",
                        "December"};
                String monthTxt = monthTab[month];
                monthView.setText(monthTxt);

            }
        });

        //add note
        addNoteBtn = (Button) findViewById(R.id.addNoteBtn);
        addNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText editText = new EditText(CalendarActivity.this);
                editText.setText("", TextView.BufferType.EDITABLE);
                Calendar cal = Calendar.getInstance();
                cal.setTime(visiblePosition);
                final int year = cal.get(Calendar.YEAR);
                final int month = cal.get(Calendar.MONTH);
                final int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
                String date = dayOfMonth + "/" + (month+1) + "/" + year;
                final AlertDialog dialogShowItem = new AlertDialog.Builder(CalendarActivity.this)
                        .setTitle(date)
                        .setView(editText)
                        .setMessage("Make a note!")
                        .setPositiveButton("Add", null)
                        .setNegativeButton("Close", null)
                        .create();
                dialogShowItem.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(final DialogInterface dialog) {
                        Button posBtn = ((AlertDialog)dialogShowItem).getButton(AlertDialog.BUTTON_POSITIVE);
                        posBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final String note = editText.getText().toString();
                                boolean exists=false;
                                String monthOfAddedNote="0";

                                switch (visiblePosition.toString().substring(4,7)){
                                    case "Jan": { monthOfAddedNote="1"; break; }
                                    case "Feb": { monthOfAddedNote="2"; break; }
                                    case "Mar": { monthOfAddedNote="3"; break; }
                                    case "Apr": { monthOfAddedNote="4"; break; }
                                    case "May": { monthOfAddedNote="5"; break; }
                                    case "Jun": { monthOfAddedNote="6"; break; }
                                    case "Jul": { monthOfAddedNote="7"; break; }
                                    case "Aug": { monthOfAddedNote="8"; break; }
                                    case "Sep": { monthOfAddedNote="9"; break; }
                                    case "Oct": { monthOfAddedNote="10"; break; }
                                    case "Nov": { monthOfAddedNote="11"; break; }
                                    case "Dec": { monthOfAddedNote="12"; break; }
                                }

                                for (int node = 0; node < notesArray.size(); node++) {
                                    if (notesArray.get(node).equals(note)
                                            && daysArray.get(node).equals(visiblePosition.toString().substring(8,10))
                                            && monthsArray.get(node).equals(monthOfAddedNote)
                                            && yearsArray.get(node).equals(visiblePosition.toString().substring(visiblePosition.toString().length()-4)))
                                        exists = true;
                                }

                                if (note.equals("")) {
                                    editText.setHint("Note is required!");
                                    editText.setHintTextColor(Color.RED);
                                    dialogShowItem.show();

                                }else if(exists==true){
                                    editText.setText("");
                                    editText.setHint("Note already exists!");
                                    editText.setHintTextColor(Color.RED);
                                    dialogShowItem.show();
                                }
                                else {
                                    dialog.cancel();
                                    //add to db
                                    HashMap<String, String> dataMap = new HashMap<>();
                                    dataMap.put("Note", note);
                                    dataMap.put("Day", Integer.toString(dayOfMonth));
                                    dataMap.put("Month", Integer.toString(month + 1));
                                    dataMap.put("Year", Integer.toString(year));

                                    calendarDataBase.push().setValue(dataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            //check if stored correctly
                                            if (task.isSuccessful()) {
                                                Toast.makeText(CalendarActivity.this, "Added", Toast.LENGTH_LONG).show();
                                            } else {
                                                Toast.makeText(CalendarActivity.this, "Error", Toast.LENGTH_LONG).show();

                                            }
                                        }
                                    });
                                }
                            }
                        });
                    }
                });
                dialogShowItem.show();

            }

        });

        //edit or delete note
        calendarList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                //TODO: fix wrong position
                final EditText editText = new EditText(CalendarActivity.this);
                editText.setText(notesArrayVisible.get(position), TextView.BufferType.EDITABLE);
                final AlertDialog dialogShowItem = new AlertDialog.Builder(CalendarActivity.this)
                        .setTitle(notesArrayVisible.get(position)).setView(editText)
                        .setNeutralButton("Save", null)
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int newPosition = getPosition(position);
                                calendarDataBase.child(keysArray.get(newPosition)).getRef().removeValue();
                            }
                        })
                        .setNegativeButton("Close", null)
                        .create();

                dialogShowItem.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(final DialogInterface dialog) {
                        Button neuBtn = ((AlertDialog) dialogShowItem).getButton(AlertDialog.BUTTON_NEUTRAL);
                        neuBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String note = editText.getText().toString();
                                boolean exists=false;
                                String monthOfAddedNote="0";
                                switch (visiblePosition.toString().substring(5,8)){
                                    case "Jan": { monthOfAddedNote="1"; break; }
                                    case "Feb": { monthOfAddedNote="2"; break; }
                                    case "Mar": { monthOfAddedNote="3"; break; }
                                    case "Apr": { monthOfAddedNote="4"; break; }
                                    case "May": { monthOfAddedNote="5"; break; }
                                    case "Jun": { monthOfAddedNote="6"; break; }
                                    case "Jul": { monthOfAddedNote="7"; break; }
                                    case "Aug": { monthOfAddedNote="8"; break; }
                                    case "Sep": { monthOfAddedNote="9"; break; }
                                    case "Oct": { monthOfAddedNote="10"; break; }
                                    case "Nov": { monthOfAddedNote="11"; break; }
                                    case "Dec": { monthOfAddedNote="12"; break; }
                                }
                                for (int node = 0; node < notesArray.size(); node++) {

                                    if (notesArray.get(node).equals(note)
                                            && daysArray.get(node).equals(visiblePosition.toString().substring(8,10))
                                            && monthsArray.get(node).equals(monthOfAddedNote)
                                            && yearsArray.get(node).equals(visiblePosition.toString().substring(visiblePosition.toString().length()-4,visiblePosition.toString().length()-1)))
                                        exists = true;
                                }

                                if (note.equals("")) {
                                    editText.setHint("Note is required!");
                                    editText.setHintTextColor(Color.RED);
                                    dialogShowItem.show();

                                }
                                else if(exists==true){
                                    editText.setText("");
                                    editText.setHint("Note already exists!");
                                    editText.setHintTextColor(Color.RED);
                                    dialogShowItem.show();
                                }
                                else {
                                    dialog.cancel();
                                    calendarDataBase.child(keysArray.get(getPosition(position))).child("Note").getRef().setValue(note);
                                }
                            }
                        });
                    }
                });
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
                setVisible();
                adapter.notifyDataSetChanged();


                Date date = new GregorianCalendar(Integer.parseInt(yearData), Integer.parseInt(monthData)-1, Integer.parseInt(dayData)).getTime();
                Event ev = new Event(Color.RED, date.getTime(), id);


                if (calendar.getEvents(date).isEmpty())
                    calendar.addEvent(ev);

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
                setVisible();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                String nameData = dataSnapshot.child("Note").getValue(String.class);
                String dayData = dataSnapshot.child("Day").getValue(String.class);
                String monthData = dataSnapshot.child("Month").getValue(String.class);
                String yearData = dataSnapshot.child("Year").getValue(String.class);
                String key = dataSnapshot.getKey();
                int index = keysArray.indexOf(key);

                Date date = new GregorianCalendar(Integer.parseInt(yearData), Integer.parseInt(monthData)-1, Integer.parseInt(dayData)).getTime();
                boolean ifDelete = true;

                for (int i=0; i<daysArray.size(); i++) {
                    if (index != i
                            && daysArray.get(index).equals(daysArray.get(i))
                            && monthsArray.get(index).equals(monthsArray.get(i))
                            && yearsArray.get(index).equals(yearsArray.get(i)))
                        ifDelete = false;
                }


                if (ifDelete)
                    calendar.removeEvents(date);

                keysArray.remove(index);
                notesArray.remove(index);
                daysArray.remove(index);
                monthsArray.remove(index);
                yearsArray.remove(index);
                setVisible();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    //get position of note in db
    public int getPosition(int position) {
        int newPosition=0;
        for (String i : keysArray) {
            int k = keysArray.indexOf(i);
            if (yearArrayVisible.get(position).equals(yearsArray.get(k)) &&
                    monthsArrayVisible.get(position).equals(monthsArray.get(k)) &&
                    daysArrayVisible.get(position).equals(daysArray.get(k)) &&
                    notesArrayVisible.get(position).equals(notesArray.get(k))) {
                newPosition = k;
            }
        }

        return newPosition;
    }

    //sets which notes should be visible in listview
    public void setVisible(){
        notesArrayVisible.clear();
        daysArrayVisible.clear();
        monthsArrayVisible.clear();
        yearArrayVisible.clear();
        Calendar cal = Calendar.getInstance();
        cal.setTime(visiblePosition);
        final int year = cal.get(Calendar.YEAR);
        final int month = cal.get(Calendar.MONTH)+1;
        final int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
        for(String i : keysArray){
            int k = keysArray.indexOf(i);
            if(Integer.toString(year).equals(yearsArray.get(k)) && Integer.toString(month).equals(monthsArray.get(k)) && Integer.toString(dayOfMonth).equals(daysArray.get(k))) {
                notesArrayVisible.add(notesArray.get(k));
                daysArrayVisible.add(daysArray.get(k));
                monthsArrayVisible.add(monthsArray.get(k));
                yearArrayVisible.add(yearsArray.get(k));
            }
        }
    }

}