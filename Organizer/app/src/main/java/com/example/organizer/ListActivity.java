package com.example.organizer;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

//  ---------------------- add button -----------------------
        addToDbBtn = (Button) findViewById(R.id.addToDbBtn);
        addToDbBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open form
                Intent intent = new Intent(ListActivity.this, AddToListActivity.class);
                startActivity(intent);

            }

        });

// ------------------------- list --------------------------

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, notesList);
        viewList = (ListView) findViewById(R.id.viewList);
        viewList.setAdapter(adapter);

        // ------------------------- click on list item --------------------------
        viewList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final EditText editText = new EditText(ListActivity.this);
                editText.setText(notesList.get(position), TextView.BufferType.EDITABLE);
                AlertDialog dialogShowItem = new AlertDialog.Builder(ListActivity.this)
                        .setTitle(notesList.get(position)).setView(editText)
                        .setNeutralButton("Save", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {                   // save button
                                HashMap<String, String> dataMap = new HashMap<String, String>();
                                dataMap.put("Name", editText.getText().toString());
                                dataMap.put("Description", "");
                                dataMap.put("Status", "Not done");
                                SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");
                                String currenthora = time.format(new Date());
                                Map<String, Object> map = new HashMap<>();
                                map.put(currenthora, dataMap);
                                listDataBase.updateChildren(map);
                                listDataBase.child(keysList.get(position)).getRef().removeValue();
                            }
                        })
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {                // delete button
                                listDataBase.child(keysList.get(position)).getRef().removeValue();
                            }
                        }).setNegativeButton("Close", null).create();            // close button
                dialogShowItem.show();
            }
        });


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
                notesList.set(index, value);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                String key = dataSnapshot.getKey();
                int index = keysList.indexOf(key);
                keysList.remove(index);
                notesList.remove(index);
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
