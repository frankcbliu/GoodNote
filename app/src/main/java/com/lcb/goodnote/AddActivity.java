package com.lcb.goodnote;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.lcb.goodnote.activityManger.BaseActivity;
import com.lcb.goodnote.db.ActivityData;
import com.lcb.goodnote.db.UserData;

import org.litepal.LitePal;
import java.util.Calendar;
import java.util.List;

public class AddActivity extends BaseActivity implements DatePicker.OnDateChangedListener{

    private static final String TAG = "AddActivity";
    private DatePicker add_dp_ddl;
    private int mYear,mMonth,mDay;
    private EditText add_et_theme;
    private EditText add_et_address;
    private EditText add_et_content;
    private Button add_bt_add;
    private Button add_bt_cancel;
    private String currentUserName;//当前用户名

    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        LitePal.initialize(this);
        init();

//        button_create= (Button) findViewById(R.id.button_create);
        setTitle("时间日期控件测试");

        add_bt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,add_et_theme.toString());
                //将各个输入窗的内容添加到数据库中
                ActivityData activityData = new ActivityData();
                activityData.setUsername(currentUserName);
                activityData.setActivity_address(add_et_address.getText().toString());
                activityData.setActivity_content(add_et_content.getText().toString());
                activityData.setActivity_theme(add_et_theme.getText().toString());
                activityData.setActivity_year(mYear);
                activityData.setActivity_month(mMonth);
                activityData.setActivity_day(mDay);
                activityData.save();
                Log.d(TAG,"db:activity: "+activityData.toString());
                Log.d(TAG,"--------------------------------------");
                finish();//结束当前活动
            }
        });

        add_bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<ActivityData> list = LitePal.findAll(ActivityData.class);
                for (ActivityData data:list){
                    Log.d(TAG,"id: "+data.getId()+" "+data.toString());
                }
                finish();//结束当前活动

            }
        });
    }


    @Override//返回键
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
        mYear=year;
        mMonth=monthOfYear+1;
        mDay=dayOfMonth;
//        Toast.makeText(MainActivity.this, mYear+"-"+mMonth+"-"+mDay, Toast.LENGTH_SHORT).show();
        Log.d(TAG,mYear+"-"+mMonth+"-"+mDay);

    }

    private void init(){

        toolbar = (Toolbar)findViewById(R.id.add_toolbar);
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.add_collapsing_toolbar);
        /**
         * 添加活动界面
         */
        //活动主题
        add_et_theme = (EditText) findViewById(R.id.add_et_theme);
        //活动地址
        add_et_address = (EditText) findViewById(R.id.add_et_address);
        //活动内容
        add_et_content = (EditText) findViewById(R.id.add_et_content);
        //添加按钮
        add_bt_add = (Button) findViewById(R.id.add_bt_add);
        //取消按钮
        add_bt_cancel = (Button) findViewById(R.id.add_bt_cancel);
        //选择日期
        add_dp_ddl=(DatePicker) findViewById(R.id.add_dp_ddl);

        //初始化日期
        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        //初始化日期，并设置日期被改变后的监听事件
        add_dp_ddl.init(mYear, mMonth, mDay, this);

        //当前用户名
        currentUserName = getSharedPreferences("userInfo",Activity.MODE_PRIVATE).getString("username","");

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
//        collapsingToolbar.setTitle("添加活动");//列表标题
    }

}