package com.example.organizer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {

    private Button addToDbBtn;
    private DatabaseReference listDataBase;
    private ListView viewList;
    private ArrayList<String> keysList = new ArrayList<>();
    private ArrayList<String> notesList = new ArrayList<>();
    private ArrayList<String> descpList = new ArrayList<>();
    private ArrayList<String> statusList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        listDataBase = FirebaseDatabase.getInstance().getReference().child("List");


        addToDbBtn = (Button) findViewById(R.id.addToDbBtn);
        addToDbBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open form
                Intent intent = new Intent(ListActivity.this, AddToListActivity.class);
                startActivity(intent);

            }

        });


        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, notesList);
        viewList = (ListView) findViewById(R.id.viewList);
        viewList.setAdapter(adapter);


        listDataBase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String id = dataSnapshot.getKey();
                //DatabaseReference keyRef = FirebaseDatabase.getInstance().getReference().child("List").child(id);
                keysList.add(id);
                String nameData = dataSnapshot.child("Name").getValue(String.class);
                notesList.add(nameData);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String value = dataSnapshot.child("Name").getValue(String.class);
                String key = dataSnapshot.getKey();
                int index = keysList.indexOf(key);
                notesList.set(index,value);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

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
