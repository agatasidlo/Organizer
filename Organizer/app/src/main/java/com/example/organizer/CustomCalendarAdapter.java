package com.example.organizer;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
//?
public class CustomCalendarAdapter extends BaseAdapter {
    private final Context context;
    private final ArrayList<String> notesArray;
    private final ArrayList<String> daysArray;
    private static ArrayList<String> monthsArray;
    private final ArrayList<String> yearArray;

    public CustomCalendarAdapter(Context context, ArrayList<String> notesArrayParam, ArrayList<String> daysArrayParam, ArrayList<String> monthsArrayParam, ArrayList<String> yearsArrayParam){
        this.context = context;
        this.notesArray = notesArrayParam;
        this.daysArray = daysArrayParam;
        this.monthsArray = monthsArrayParam;
        this.yearArray = yearsArrayParam;

    }

    @Override
    public int getCount() {
        return notesArray.size();
    }

    @Override
    public Object getItem(int position) {
        return notesArray.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent){

        return view;
    }
}
