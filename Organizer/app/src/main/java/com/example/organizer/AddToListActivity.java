package com.example.organizer;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
    private TextView nameRequired;
    private DatabaseReference listDataBase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_list);
        listDataBase = FirebaseDatabase.getInstance().getReference().child("List");
        nameRequired = (TextView) findViewById(R.id.nameRequiredTxt);

        addBtn = (Button) findViewById(R.id.addTaskBtn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameRequired.setVisibility(View.INVISIBLE);
                nameTxt = (EditText) findViewById(R.id.txtName);
                descpTxt = (EditText) findViewById(R.id.txtDescp);
                String taskName = nameTxt.getText().toString().trim();
                String taskDescp = descpTxt.getText().toString().trim();

                if(taskName.equals("")){
                    nameRequired.setVisibility(View.VISIBLE);
                }
                else {
                    //add to db
                    HashMap<String, String> dataMap = new HashMap<>();
                    dataMap.put("Name", taskName);
                    dataMap.put("Description", taskDescp);
                    dataMap.put("Status", "Not done");
                    listDataBase.push().setValue(dataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            //check if stored correctly
                            if (task.isSuccessful()) {
                                Toast.makeText(AddToListActivity.this, "Dodano", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(AddToListActivity.this, "Błąd", Toast.LENGTH_LONG).show();

                            }
                        }
                    });

                    //back to the list
                    finish();

                }
            }
        });
    }
}
