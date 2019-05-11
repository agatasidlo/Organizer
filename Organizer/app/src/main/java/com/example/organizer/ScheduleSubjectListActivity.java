package com.example.organizer;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.solver.widgets.ConstraintHorizontalLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
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

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_schedule_subject_list);
        Bundle b = getIntent().getExtras();
        String chosenDay = "";
        if (b != null)
            chosenDay = b.getString("day");
        scheduleDataBase = FirebaseDatabase.getInstance().getReference().child("Schedule").child(chosenDay);


        dayNameTextView = (TextView) findViewById(R.id.dayNameID);
        dayName = dayNameTextView.getText().toString().trim();
        dayNameTextView.setText(chosenDay);

        final CustomScheduleAdapter adapter = new CustomScheduleAdapter(this, subjectArray, descpArray, fromArray, toArray);
        subjectsListView = (ListView) findViewById(R.id.subjectsID);
        subjectsListView.setAdapter(adapter);

        subjectsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final EditText editText = new EditText(ScheduleSubjectListActivity.this);
                final EditText editDescp = new EditText(ScheduleSubjectListActivity.this);
                final EditText editFromTime = new EditText(ScheduleSubjectListActivity.this);
                final EditText editToTime = new EditText(ScheduleSubjectListActivity.this);
                editText.setText(subjectArray.get(position), TextView.BufferType.EDITABLE);
                editDescp.setText(descpArray.get(position), TextView.BufferType.EDITABLE);
                editFromTime.setText(fromArray.get(position), TextView.BufferType.EDITABLE);
                editToTime.setText(toArray.get(position), TextView.BufferType.EDITABLE);
                LinearLayout ll=new LinearLayout(ScheduleSubjectListActivity.this);
                ll.setOrientation(LinearLayout.VERTICAL);
                ll.addView(editText);
                ll.addView(editDescp);
                ll.addView(editFromTime);
                ll.addView(editToTime);
                AlertDialog dialogShowItem = new AlertDialog.Builder(ScheduleSubjectListActivity.this)
                        .setTitle(subjectArray.get(position)).setView(editText)
                        .setView(ll)
                        .setNeutralButton("Save", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                scheduleDataBase.child(keysList.get(position)).child("Name").getRef().setValue(editText.getText().toString());
                                scheduleDataBase.child(keysList.get(position)).child("Description").getRef().setValue(editDescp.getText().toString());
                                scheduleDataBase.child(keysList.get(position)).child("From").getRef().setValue(editFromTime.getText().toString());
                                scheduleDataBase.child(keysList.get(position)).child("To").getRef().setValue(editToTime.getText().toString());
                                //reload activity to get proper ordered view:
                                finish();
                                startActivity(getIntent());
                            }
                        })
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                scheduleDataBase.child(keysList.get(position)).getRef().removeValue();
                            }
                        }).setNegativeButton("Close", null).create();
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
                String fromData = dataSnapshot.child("From").getValue(String.class);
                String toData = dataSnapshot.child("To").getValue(String.class);
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
                String fromData = dataSnapshot.child("From").getValue(String.class);
                String toData = dataSnapshot.child("To").getValue(String.class);
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
}
