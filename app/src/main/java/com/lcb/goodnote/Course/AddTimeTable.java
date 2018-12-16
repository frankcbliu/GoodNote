package com.lcb.goodnote.Course;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.lcb.goodnote.R;

public class AddTimeTable extends AppCompatActivity {

    private TextView cvText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_time_table);
    }


    public void addItemView(View view) {
//        setContentView(R.layout.item_add);
    }
}
