package com.example.organizer;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.io.FileOutputStream;

public class ListActivity extends AppCompatActivity {


    String [] notesList = {"item1", "item2", "it"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);


        String fileContents = "Itemno1\nItem 2\nitem33";
        FileOutputStream outputStream;

        try {
            //File notesFile = getFilesDir();
            outputStream = openFileOutput("notes", Context.MODE_PRIVATE);
            outputStream.write(fileContents.getBytes());
            notesList[0] = outputStream.toString();
            //notesList = outputStream.toString().split("\\n");
            outputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        ListAdapter adap = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, notesList);
        ListView viewList = (ListView) findViewById(R.id.viewList);
        viewList.setAdapter(adap);
    }




}
