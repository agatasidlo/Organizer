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
import android.widget.Button;
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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class ListActivity extends AppCompatActivity {

    private Button addToDbBtn;
    private Button removeBtn;
    private DatabaseReference listDataBase;
    private ListView viewList;
    private ArrayList<String> keysList = new ArrayList<>();
    private ArrayList<String> notesList = new ArrayList<>();
    private ArrayList<String> descpList = new ArrayList<>();
    private static ArrayList<String> statusList = new ArrayList<>();




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

        //  ---------------------- remove all button -----------------------
        removeBtn = (Button) findViewById(R.id.removeAllBtn);
        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statusList = CustomListAdapter.getStatusArray();
                AlertDialog dialogShowItem = new AlertDialog.Builder(ListActivity.this)
                        .setTitle("Are you sure?")
                        .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                removeAll();
                            }
                        })
                        .setNegativeButton("No", null).create();
                dialogShowItem.show();
            }

        });

        //  ---------------------- remove checked button -----------------------
        removeBtn = (Button) findViewById(R.id.removeCheckedBtn);
        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialogShowItem = new AlertDialog.Builder(ListActivity.this)
                        .setTitle("Are you sure?")
                        .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                removeChecked();
                            }
                        })
                        .setNegativeButton("No", null).create();
                dialogShowItem.show();
            }

        });

// ------------------------- list --------------------------

        final CustomListAdapter adapter = new CustomListAdapter(this, notesList, descpList, statusList, keysList);
        viewList = (ListView) findViewById(R.id.viewList);
        viewList.setAdapter(adapter);


        // ------------------------- click on list item --------------------------

        viewList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                  //  Toast.makeText(ListActivity.this,  "checked", Toast.LENGTH_LONG).show();

                final EditText editText = new EditText(ListActivity.this);
                final EditText editDescp = new EditText(ListActivity.this);
                editText.setText(notesList.get(position), TextView.BufferType.EDITABLE);
                editDescp.setText(descpList.get(position), TextView.BufferType.EDITABLE);
                LinearLayout ll=new LinearLayout(ListActivity.this);
                ll.setOrientation(LinearLayout.VERTICAL);
                ll.addView(editText);
                ll.addView(editDescp);
                AlertDialog dialogShowItem = new AlertDialog.Builder(ListActivity.this)
                        .setTitle(notesList.get(position)).setView(editText)
                        .setView(ll)
                        .setNeutralButton("Save", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {                   // save button
                                HashMap<String, String> dataMap = new HashMap<String, String>();
                                dataMap.put("Name", editText.getText().toString());
                                dataMap.put("Description", editDescp.getText().toString());
                                dataMap.put("Status", "Not done");
                                listDataBase.push().setValue(dataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        //check if stored correctly
                                        if(task.isSuccessful()) {
                                            Toast.makeText(ListActivity.this,  "Edited", Toast.LENGTH_LONG).show();
                                        }
                                        else {
                                            Toast.makeText(ListActivity.this,  "Error", Toast.LENGTH_LONG).show();

                                        }
                                    }
                                });

                                //listDataBase.updateChildren(map); //czy to potrzebne?
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
                keysList.add(id);
                String nameData = dataSnapshot.child("Name").getValue(String.class);
                String descData = dataSnapshot.child("Description").getValue(String.class);
                String statusData = dataSnapshot.child("Status").getValue(String.class);
                notesList.add(nameData);
                descpList.add(descData);
                statusList.add(statusData);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String name = dataSnapshot.child("Name").getValue(String.class);
                String desc = dataSnapshot.child("Description").getValue(String.class);
                String status = dataSnapshot.child("Status").getValue(String.class);
                String key = dataSnapshot.getKey();
                int index = keysList.indexOf(key);
                notesList.set(index, name);
                descpList.set(index, desc);
                statusList.set(index, status);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                String key = dataSnapshot.getKey();
                int index = keysList.indexOf(key);
                keysList.remove(index);
                notesList.remove(index);
                descpList.remove(index);
                statusList.remove(index);
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

    static void setStatusList(ArrayList<String> newList){
        statusList = newList;
    }

    public void removeAll(){
        DatabaseReference dataBase = FirebaseDatabase.getInstance().getReference();
        dataBase.child("List").removeValue();
    }

    public void removeChecked(){
        FirebaseDatabase.getInstance().getReference().child("List")
        .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String status = snapshot.child("Status").getValue(String.class);
                    if(status.equals("Done")){
                        String key = snapshot.getKey();
                        listDataBase.child(key).removeValue();
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }



}
