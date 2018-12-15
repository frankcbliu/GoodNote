package com.lcb.goodnote;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lcb.goodnote.activityManger.BaseActivity;
import com.lcb.goodnote.db.ActivityData;

import org.litepal.LitePal;

public class NoteActivity extends BaseActivity implements DatePicker.OnDateChangedListener{

    private String TAG = "NoteActivity";
    public static final String NOTE_NAME = "note_name";
    public static final String NOTE_IMAGE_ID = "note_image_id";
    public static final String NOTE_CONTENT = "note_ed_content";
    public static final String NOTE_ADDRESS = "note_ed_address";
    public static final String NOTE_THEME   = "note_theme";
    public static final String NOTE_YEAR    = "note_year";
    public static final String NOTE_MONTH   = "note_month";
    public static final String NOTE_DAY     = "note_day";
    public static final String ACTIVITY_ID = "activity_id";

    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbar;
    private ImageView noteImageView;
    private EditText editNoteContent;//内容
    private EditText editNoteAddress;//地点
    private EditText editNoteTitle;//主题
    private int year,month,day;
    private DatePicker note_dp;
    private int thisID;
    private String temp_title,temp_address;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        intent = getIntent();

        int noteImageId = intent.getIntExtra(NOTE_IMAGE_ID,0);
        init();//初始化各种变量


        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        collapsingToolbar.setTitle(editNoteTitle.getText());//列表标题
        Glide.with(this).load(noteImageId).into(noteImageView);

        //悬浮按钮点击事件
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.note_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //保存数据
                ActivityData data = new ActivityData();
                String address = editNoteAddress.getText().toString();
                data.setActivity_theme(editNoteTitle.getText().toString());
                data.setActivity_address(editNoteAddress.getText().toString());
                data.setActivity_content(editNoteContent.getText().toString());
                data.setActivity_year(year);
                data.setActivity_month(month);
                data.setActivity_day(day);
                data.updateAll("activity_theme = ? and activity_address = ?",temp_title,temp_address);
                Log.d(TAG,"id: "+thisID);
                Log.d(TAG,"address: "+address+"t_address: "+temp_address);
                finish();
            }
        });

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

    @Override //监测日期变化
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        this.year=year;
        this.month=monthOfYear+1;
        this.day =dayOfMonth;
//        Toast.makeText(MainActivity.this, mYear+"-"+mMonth+"-"+mDay, Toast.LENGTH_SHORT).show();

    }

    private void init(){
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

        //列表标题栏图片
        noteImageView = (ImageView) findViewById(R.id.image_bar_view);
//        TextView noteContentText = (TextView) findViewById(R.id.note_content_text);
        //列表主题
        editNoteTitle = (EditText) findViewById(R.id.note_et_theme);
        //列表地点
        editNoteAddress = (EditText) findViewById(R.id.note_et_address);
        //列表内容
        editNoteContent = (EditText) findViewById(R.id.note_et_content);
//        //截止日期
        note_dp = (DatePicker) findViewById(R.id.note_dp_ddl);
//
        //从intent中传递内容过来
        year = intent.getIntExtra(NOTE_YEAR,0);
        month= intent.getIntExtra(NOTE_MONTH,0);
        day = intent.getIntExtra(NOTE_DAY,0);

        thisID = intent.getIntExtra(ACTIVITY_ID,0);
        temp_title = intent.getStringExtra(NOTE_THEME);//利用临时变量保存标题和地址
        temp_address = intent.getStringExtra(NOTE_ADDRESS);//用于更新数据库时查找到对应的活动
        editNoteTitle.setText(temp_title);
        editNoteAddress.setText(temp_address);
        editNoteContent.setText(intent.getStringExtra(NOTE_CONTENT));
        note_dp.init(year,month,day,this);
    }
}

