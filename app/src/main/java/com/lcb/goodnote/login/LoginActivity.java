package com.lcb.goodnote.login;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.lcb.goodnote.R;

import com.lcb.goodnote.MainActivity;
import com.lcb.goodnote.activityManger.ActivityCollector;
import com.lcb.goodnote.activityManger.BaseActivity;

public class LoginActivity extends BaseActivity {


    private static final String TAG = "LoginActivity";
    private SharedPreferences firstIn_sp;
    private SharedPreferences register_sp;
    private SharedPreferences auto_login_sp;
    private SharedPreferences back_login;
    private boolean isFirstIn;
    private String input_username;//用户名输入框
    private String input_psw;//密码输入框
    private Button bt_login;//登陆按钮
    private EditText edt_username;
    private EditText edt_password;
    private CheckBox cb_auto;
    private boolean isAutoLogin;//自动登陆
    public static boolean isNotExit;//退出

    private String currentUserName;//当前用户名

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();

        if (isFirstIn) {//判断程序是否是第一次运行如果是跳转到注册界面 否则到登录界面
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
            Editor editor = firstIn_sp.edit();
            editor.putBoolean("isFirstIn", false);
            editor.apply();
            LoginActivity.this.finish();
        }
        //如果自动登录为true则直接进入MainActivity
        Log.d(TAG,"isAutoLogin: "+isAutoLogin+" "+"isNotExit"+" "+isNotExit);
        if (isAutoLogin && isNotExit) {
            cb_auto.setChecked(true);
            Intent intent=new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
            LoginActivity.this.finish();
        } else{
            bt_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //判断自动登录按钮是否选中,选中则将isAutoLogin设置为true
                    if (cb_auto.isChecked()) {
                        Editor edit=auto_login_sp.edit();
                        edit.putBoolean("isAutoLogin", true);
                        edit.apply();
                    }
                    input_username = edt_username.getText().toString();
                    input_psw = edt_password.getText().toString();
                    if(input_username.trim().equals("") || input_psw.trim().equals("")) {
                        Toast.makeText(LoginActivity.this, "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    //获取保存文件中的用户名和密码
                    register_sp=getSharedPreferences("userInfo",Activity.MODE_PRIVATE);
                    String savedUsername = register_sp.getString("username","");
                    String savedPassword = register_sp.getString("password","");
                    //查看输入的密码和名字是否一致
                    if(input_username.trim().equals(savedUsername) && input_psw.trim().equals(savedPassword)) {
                        Toast.makeText(LoginActivity.this, "成功登陆", Toast.LENGTH_SHORT).show();
                        //成功登陆，进入MainActivity界面
                        Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();

                    } else {
                        //错误的话
                        Toast.makeText(LoginActivity.this, "用户名或者密码错误,请确认信息或者去注册", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

            });

        }

    }

    private void init(){
        edt_username = (EditText) findViewById(R.id.login_username);
        edt_password = (EditText) findViewById(R.id.login_psw);
        cb_auto = (CheckBox) findViewById(R.id.auto_login);
        bt_login = (Button) findViewById(R.id.login);
        firstIn_sp = getSharedPreferences("firstIn_spf", MODE_PRIVATE);//（文件名，模式）
        auto_login_sp = getSharedPreferences("auto_login", MODE_PRIVATE);
        back_login = getSharedPreferences("back_login",MODE_PRIVATE);
        isAutoLogin = auto_login_sp.getBoolean("isAutoLogin", false);
        //获取相应的值,如果没有该值,说明还未写入，程序第一次运行 用true作为默认值
        isFirstIn = firstIn_sp.getBoolean("isFirstIn", true);
        isNotExit = back_login.getBoolean("back_login",true);


    }
    public void register(View view) {
        Intent intent = new Intent();
        intent.setClass(LoginActivity.this,RegisterActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /**
         * 添加退出登陆逻辑：是否跳转到主页除了判断是否自动登陆，还需判断 back_login 的真假，
         * 按下“退出登陆”按钮时，将 back_login 写为 false，而 login活动退出时，自动将其改为 true。
         */
        Editor editor = back_login.edit();
        editor.putBoolean("back_login", true);
        editor.apply();
    }
}
