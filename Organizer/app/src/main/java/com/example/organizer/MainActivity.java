package com.example.organizer;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.io.File;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {


    String [] notesList = {"item1", "item2", "it"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

    //                  TODO - schedule
    protected void showSchedule() {

    }


    //                  TODO - notes
    protected void showNotes() {

    }

    public void addNote(View addButton) {
        // adding note to the list of notes
        // refresh view
    }


    //                  TODO - todo list
    protected void showTodoList() {

    }
}
