package com.example.organizer;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AddToListActivity extends AppCompatActivity {

    private Button addBtn;
    private EditText nameTxt;
    private EditText descpTxt;
    private DatabaseReference listDataBase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_list);
        listDataBase = FirebaseDatabase.getInstance().getReference().child("List");

        addBtn = (Button) findViewById(R.id.addTaskBtn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nameTxt = (EditText) findViewById(R.id.txtName);
                descpTxt = (EditText) findViewById(R.id.txtDescp);
                //TODO exceptions!
                String taskName = nameTxt.getText().toString().trim();
                String taskDescp = descpTxt.getText().toString().trim();
                //add to db
                HashMap<String, String> dataMap = new HashMap<>();
                dataMap.put("Name", taskName);
                dataMap.put("Description", taskDescp);
                dataMap.put("Status", "Not done");
                //TODO: get key and update its values
                listDataBase.push().setValue(dataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //check if stored correctly
                        if(task.isSuccessful()) {
                            Toast.makeText(AddToListActivity.this,  "Added", Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(AddToListActivity.this,  "Error", Toast.LENGTH_LONG).show();

                        }
                    }
                });

                //back to the list
                finish();

            }
        });
    }
}
