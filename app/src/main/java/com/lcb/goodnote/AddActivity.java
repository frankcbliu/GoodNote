package com.lcb.goodnote;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.lcb.goodnote.db.ActivityData;

import org.litepal.LitePal;

import java.util.Calendar;
import java.util.List;

public class AddActivity extends AppCompatActivity implements DatePicker.OnDateChangedListener{

    private static final String TAG = "AddActivity";
    private DatePicker datePicker1;
    private int mYear,mMonth,mDay;
    private EditText edit_text_theme;
    private EditText edit_text_address;
    private Button button_add;
    private Button button_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LitePal.initialize(this);
//        setContentView(R.layout.activity_add);

        setContentView(R.layout.activity_add);
        init();
//        button_create= (Button) findViewById(R.id.button_create);
        setTitle("时间日期控件测试");


        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,edit_text_theme.toString());
                //将各个输入窗的内容添加到数据库中
                ActivityData activityData = new ActivityData();
                activityData.setActivity_address("1"+edit_text_address.getText().toString());
                activityData.setActivity_theme("1"+edit_text_theme.getText().toString());
                activityData.setActivity_year(mYear+"2");
                activityData.setActivity_month(mMonth+"2");
                activityData.setActivity_day(mDay+"2");
                activityData.save();
                Log.d(TAG,"db:activity"+activityData.toString());
            }
        });

        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<ActivityData> list = LitePal.findAll(ActivityData.class);
                for (ActivityData data:list){
                    Log.d(TAG,data.toString());
                }
                finish();//结束当前活动

            }
        });
    }


    @Override //监测日期变化
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        mYear=year;
        mMonth=monthOfYear+1;
        mDay=dayOfMonth;
//        Toast.makeText(MainActivity.this, mYear+"-"+mMonth+"-"+mDay, Toast.LENGTH_SHORT).show();
        Log.d(TAG,mYear+"-"+mMonth+"-"+mDay);

    }

    public void init(){
        /**
         * 添加活动界面
         */
        //活动主题
        edit_text_theme = (EditText) findViewById(R.id.edit_text_theme);
        //活动地址
        edit_text_address = (EditText) findViewById(R.id.edit_text_address);
        //添加按钮
        button_add = (Button) findViewById(R.id.button_add);
        //取消按钮
        button_cancel = (Button) findViewById(R.id.button_cancel);
        //选择日期
        datePicker1=(DatePicker) findViewById(R.id.datePicker1);

        //初始化日期
        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        //初始化日期，并设置日期被改变后的监听事件
        datePicker1.init(mYear, mMonth, mDay, this);


    }

}
