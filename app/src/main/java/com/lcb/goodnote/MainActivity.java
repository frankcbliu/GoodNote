package com.lcb.goodnote;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements DatePicker.OnDateChangedListener {

    private DatePicker datePicker1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        setContentView(R.layout.get_date);
        setTitle("时间日期控件测试");
        datePicker1=(DatePicker) findViewById(R.id.datePicker1);
        //初始化日期，并设置日期被改变后的监听事件
        datePicker1.init(2017, 8, 7, this);
        Button button_add = (Button) findViewById(R.id.button_add);
        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //将各个输入窗的内容添加到数据库中

            }
        });
    }
    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
        // TODO Auto-generated method stub
        Toast.makeText(this, "日期被改变为： "+year+"."+(monthOfYear+1)+"."+dayOfMonth, Toast.LENGTH_SHORT).show();
    }
}
