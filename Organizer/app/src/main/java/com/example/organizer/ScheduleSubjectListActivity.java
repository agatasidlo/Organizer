package com.example.organizer;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.solver.widgets.ConstraintHorizontalLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class ScheduleSubjectListActivity extends AppCompatActivity {

    private ListView subjectsListView;
    private TextView dayNameTextView;
    private String dayName;
    private ArrayList<String> subjectArray = new ArrayList<>();
    private ArrayList<String> descpArray = new ArrayList<>();
    private ArrayList<String> fromArray = new ArrayList<>();
    private ArrayList<String> toArray = new ArrayList<>();
    private ArrayList<String> keysList = new ArrayList<>();
    private DatabaseReference scheduleDataBase;
    private TabLayout tablayout;
    private String chosenDay;
    private String chosenDayEng;
    private boolean timeSlotsResult;


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.schedule_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.addId){
            finish();
            Intent intent = new Intent(ScheduleSubjectListActivity.this, AddToScheduleActivity.class);
            Bundle b = new Bundle();
            b.putString("day",chosenDayEng);
            intent.putExtras(b);
            startActivity(intent);

        }
        else if(item.getItemId() == R.id.removeId){
            AlertDialog dialogShowItem = new AlertDialog.Builder(ScheduleSubjectListActivity.this)
                    .setTitle("Czy jesteś pewien?")
                    .setPositiveButton("Tak",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            removeAll();
                        }
                    })
                    .setNegativeButton("Nie", null).create();
            dialogShowItem.show();

        }
        else return super.onOptionsItemSelected(item);
        return true;
    }

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_subject_list);

        Bundle b = getIntent().getExtras();
        if (b != null)
            chosenDayEng = b.getString("day");
        switch (chosenDayEng){
            case "Monday":
                chosenDay = "Poniedziałek";
                break;
            case "Tuesday":
                chosenDay = "Wtorek";
                break;
            case "Wednesday":
                chosenDay = "Środa";
                break;
            case "Thursday":
                chosenDay = "Czwartek";
                break;
            case "Friday":
                chosenDay = "Piątek";
                break;
        }


        scheduleDataBase = FirebaseDatabase.getInstance().getReference().child("Schedule").child(chosenDayEng);
        dayNameTextView = (TextView) findViewById(R.id.dayNameID);
        dayName = dayNameTextView.getText().toString().trim();
        dayNameTextView.setText(chosenDay);
        tablayout = findViewById(R.id.tabId);
        int tabIndex=0;
        switch (chosenDayEng){
            case "Monday":
                tabIndex = 0;
                break;
            case "Tuesday":
                tabIndex = 1;
                break;
            case "Wednesday":
                tabIndex = 2;
                break;
            case "Thursday":
                tabIndex = 3;
                break;
            case "Friday":
                tabIndex = 4;
                break;
        }
        TabLayout.Tab tab = tablayout.getTabAt(tabIndex);
        tab.select();
        final CustomScheduleAdapter adapter = new CustomScheduleAdapter(this, subjectArray, descpArray, fromArray, toArray);
        subjectsListView = (ListView) findViewById(R.id.subjectsID);
        subjectsListView.setAdapter(adapter);

        tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String day = tab.getText().toString();
                switch (day){
                    case "Pon":
                        chosenDayEng = "Monday";
                        break;
                    case "Wt":
                        chosenDayEng = "Tuesday";
                        break;
                    case "Śr":
                        chosenDayEng = "Wednesday";
                        break;
                    case "Czw":
                        chosenDayEng = "Thursday";
                        break;
                    case "Pt":
                        chosenDayEng = "Friday";
                        break;
                }
                Intent intent = new Intent(ScheduleSubjectListActivity.this, ScheduleSubjectListActivity.class);
                Bundle b = new Bundle();
                b.putString("day",chosenDayEng);
                intent.putExtras(b);
                finish();
                startActivity(intent);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        subjectsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final EditText editText = new EditText(ScheduleSubjectListActivity.this);
                final EditText editDescp = new EditText(ScheduleSubjectListActivity.this);
                final TextView fromTimeText = new TextView(ScheduleSubjectListActivity.this);
                final TextView editFromTime = new TextView(ScheduleSubjectListActivity.this);
                final TextView toTimeText = new TextView(ScheduleSubjectListActivity.this);
                final TextView editToTime = new TextView(ScheduleSubjectListActivity.this);
                final TextView textWarning = new TextView(ScheduleSubjectListActivity.this);
                editText.setText(subjectArray.get(position), TextView.BufferType.EDITABLE);
                editDescp.setText(descpArray.get(position), TextView.BufferType.EDITABLE);
                fromTimeText.setText("Od:   ", TextView.BufferType.NORMAL);
                fromTimeText.setTextSize(25);
                editFromTime.setText(fromArray.get(position), TextView.BufferType.EDITABLE);
                editFromTime.setTextSize(25);
                toTimeText.setText("   Do:   ", TextView.BufferType.NORMAL);
                toTimeText.setTextSize(25);
                editToTime.setText(toArray.get(position), TextView.BufferType.EDITABLE);
                editToTime.setTextSize(25);
                LinearLayout ll=new LinearLayout(ScheduleSubjectListActivity.this);
                LinearLayout hl=new LinearLayout(ScheduleSubjectListActivity.this);
                ll.setOrientation(LinearLayout.VERTICAL);
                hl.setOrientation(LinearLayout.HORIZONTAL);
                ll.addView(editText);
                ll.addView(editDescp);
                hl.addView(fromTimeText);
                hl.addView(editFromTime);
                hl.addView(toTimeText);
                hl.addView(editToTime);
                ll.addView(hl);
                ll.addView(textWarning);
                final AlertDialog dialogShowItem = new AlertDialog.Builder(ScheduleSubjectListActivity.this)
                        .setTitle(subjectArray.get(position)).setView(editText)
                        .setView(ll)
                        .setNeutralButton("Zapisz", null)
                        .setPositiveButton("Usuń", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                scheduleDataBase.child(keysList.get(position)).getRef().removeValue();
                            }
                        }).setNegativeButton("Zamknij", null)
                        .create();

                editFromTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar mcurrentTime = Calendar.getInstance();
                        final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                        final int minute = mcurrentTime.get(Calendar.MINUTE);
                        TimePickerDialog mTimePicker;
                        mTimePicker = new TimePickerDialog(ScheduleSubjectListActivity.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                String hourStr = Integer.toString(selectedHour);
                                String minuteStr = Integer.toString(selectedMinute);
                                if (selectedHour<10) hourStr="0"+hourStr;
                                if (selectedMinute<10) minuteStr = "0"+minuteStr;
                                editFromTime.setText(hourStr + ":" + minuteStr);
                            }
                        }, hour+2, minute, true);
                        mTimePicker.show();
                    }
                });

                editToTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar mcurrentTime = Calendar.getInstance();
                        final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                        final int minute = mcurrentTime.get(Calendar.MINUTE);
                        TimePickerDialog mTimePicker;
                        mTimePicker = new TimePickerDialog(ScheduleSubjectListActivity.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                String hourStr = Integer.toString(selectedHour);
                                String minuteStr = Integer.toString(selectedMinute);
                                if (selectedHour<10) hourStr="0"+hourStr;
                                if (selectedMinute<10) minuteStr = "0"+minuteStr;
                                editToTime.setText(hourStr + ":" + minuteStr);
                            }
                        }, hour+2, minute, true);
                        mTimePicker.show();
                    }
                });

                dialogShowItem.setOnShowListener(new DialogInterface.OnShowListener() {
                                                     @Override
                                                     public void onShow(final DialogInterface dialog) {
                                                         Button neuBtn = ((AlertDialog) dialogShowItem).getButton(AlertDialog.BUTTON_NEUTRAL);
                                                         neuBtn.setOnClickListener(new View.OnClickListener() {
                                                             @Override
                                                             public void onClick(View v) {
                                                                 final String name = editText.getText().toString();
                                                                 final String timeFrom = editFromTime.getText().toString();
                                                                 final String timeTo = editToTime.getText().toString();

                                                                 scheduleDataBase.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                     @Override
                                                                     public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                         timeSlotsResult = true;
                                                                         if(name.equals("")) {
                                                                             editText.setHint("Nazwa jest wymagana!");
                                                                             editText.setHintTextColor(Color.RED);
                                                                             dialogShowItem.show();
                                                                         }
                                                                         else {
                                                                             if (changeTimeToInt(timeFrom) >= changeTimeToInt(timeTo))
                                                                                 timeSlotsResult = false;
                                                                             else {
                                                                                 for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                                                     int timeFromDb = snapshot.child("From").getValue(Integer.class);
                                                                                     int timeToDb = snapshot.child("To").getValue(Integer.class);
                                                                                     if (!(changeTimeToInt(timeTo) <= timeFromDb || changeTimeToInt(timeFrom) >= timeToDb) && timeFromDb != changeTimeToInt(fromArray.get(position)) && timeToDb != changeTimeToInt(toArray.get(position)))
                                                                                         timeSlotsResult = false;
                                                                                 }
                                                                             }

                                                                             if (!timeSlotsResult) {
                                                                                 textWarning.setText("Nieprawidłowy czas!");
                                                                                 textWarning.setTextColor(Color.RED);
                                                                                 dialogShowItem.show();
                                                                             } else {
                                                                                 dialog.cancel();
                                                                                 scheduleDataBase.child(keysList.get(position)).child("Name").getRef().setValue(editText.getText().toString());
                                                                                 scheduleDataBase.child(keysList.get(position)).child("Description").getRef().setValue(editDescp.getText().toString());
                                                                                 scheduleDataBase.child(keysList.get(position)).child("From").getRef().setValue(changeTimeToInt(editFromTime.getText().toString()));
                                                                                 scheduleDataBase.child(keysList.get(position)).child("To").getRef().setValue(changeTimeToInt(editToTime.getText().toString()));
                                                                                 //reload activity to get proper ordered view:
                                                                                 finish();
                                                                                 startActivity(getIntent());
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
                                                 });
                dialogShowItem.show();
            }
        });

        scheduleDataBase.orderByChild("From").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String id = dataSnapshot.getKey();
                keysList.add(id);
                String nameData = dataSnapshot.child("Name").getValue(String.class);
                String descData = dataSnapshot.child("Description").getValue(String.class);
                String fromData = changeTimeToString(dataSnapshot.child("From").getValue(Integer.class));
                String toData = changeTimeToString(dataSnapshot.child("To").getValue(Integer.class));
                subjectArray.add(nameData);
                descpArray.add(descData);
                fromArray.add(fromData);
                toArray.add(toData);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String name = dataSnapshot.child("Name").getValue(String.class);
                String desc = dataSnapshot.child("Description").getValue(String.class);
                String fromData = changeTimeToString(dataSnapshot.child("From").getValue(Integer.class));
                String toData = changeTimeToString(dataSnapshot.child("To").getValue(Integer.class));
                String key = dataSnapshot.getKey();
                int index = keysList.indexOf(key);
                subjectArray.set(index, name);
                descpArray.set(index, desc);
                fromArray.set(index, fromData);
                toArray.set(index, toData);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                String key = dataSnapshot.getKey();
                int index = keysList.indexOf(key);
                keysList.remove(index);
                subjectArray.remove(index);
                descpArray.remove(index);
                toArray.remove(index);
                fromArray.remove(index);
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

    public String changeTimeToString(int time){
        String newTime;
        newTime = Integer.toString(time);
        if(newTime.length()==1) newTime = "000" + newTime;
        else if(newTime.length()==2) newTime = "00" + newTime;
        else if(newTime.length()==3) newTime = "0" + newTime;
        newTime = newTime.substring(0,2) + ":" + newTime.substring(2,4);
        return newTime;
    }

    public int changeTimeToInt(String time){
        int newTime;
        String newTimeStr;
        newTimeStr = time.substring(0,2)+time.substring(3,5);
        newTime=Integer.parseInt(newTimeStr);
        return newTime;
    }

    public void removeAll(){
        DatabaseReference dataBase = FirebaseDatabase.getInstance().getReference();
        dataBase.child("Schedule").removeValue();
    }

}
