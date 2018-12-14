package com.lcb.goodnote;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lcb.goodnote.activityManger.BaseActivity;

public class NoteActivity extends BaseActivity {

    public static final String NOTE_NAME = "note_name";
    public static final String NOTE_IMAGE_ID = "note_image_id";

    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbar;
    private ImageView noteImageView;
    private EditText editNoteContent;
    private EditText editNoteTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        Intent intent = getIntent();

        String noteName = intent.getStringExtra(NOTE_NAME);
        int noteImageId = intent.getIntExtra(NOTE_IMAGE_ID,0);
        init();//初始化各种变量


        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        collapsingToolbar.setTitle(noteName);//列表标题
        Glide.with(this).load(noteImageId).into(noteImageView);
        String nameContent = generateNoteContent(noteName);
//        noteContentText.setText(nameContent);//列表内容


    }


    private String generateNoteContent(String noteName){
        StringBuilder noteContent = new StringBuilder();
        for (int i = 0; i < 500; i++) {
            noteContent.append(noteName);
        }
        return noteContent.toString();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void init(){
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        //列表标题栏图片
        noteImageView = (ImageView) findViewById(R.id.image_bar_view);
//        TextView noteContentText = (TextView) findViewById(R.id.note_content_text);
        //列表标题
        editNoteTitle = (EditText) findViewById(R.id.edit_note_title);

        //列表内容
//        editNoteContent = (EditText) findViewById(R.id.edit_note_content);
    }
}

