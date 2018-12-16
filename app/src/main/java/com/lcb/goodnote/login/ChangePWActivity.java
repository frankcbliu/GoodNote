package com.lcb.goodnote.login;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.lcb.goodnote.MainActivity;
import com.lcb.goodnote.activityManger.BaseActivity;
import com.lcb.goodnote.R;
import com.lcb.goodnote.db.UserData;

import org.litepal.LitePal;

import java.util.List;

public class ChangePWActivity extends BaseActivity {

    private String TAG = "ChangePWActivity";
    private SharedPreferences register_sp;
    private EditText old_pass_word;
    private EditText new_pass_word;//密码
    private EditText new_pass_word_check;//确认密码

    private String old_s,new_s,new_check_s;
    private String currentUserName,currentPassWord;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pw);

        init();
    }

    private void init()
    {
        old_pass_word = (EditText) findViewById(R.id.old_psw_edt);
        new_pass_word = (EditText) findViewById(R.id.new_psw_edt);
        new_pass_word_check = (EditText) findViewById(R.id.new_psw_edt_check);

        old_pass_word.setText("");
        new_pass_word.setText("");
        new_pass_word_check.setText("");

    }
    public void onChange(View v){


        old_s = old_pass_word.getText().toString();
        new_s = new_pass_word.getText().toString();
        new_check_s = new_pass_word_check.getText().toString();


        //当前用户名和密码
        currentUserName = getSharedPreferences("userInfo",Activity.MODE_PRIVATE).getString("username","");
        currentPassWord = getSharedPreferences("userInfo",Activity.MODE_PRIVATE).getString("password","");
        Log.d(TAG,"psw = "+currentPassWord+"get_psw "+old_s);
        if (!old_s.equals(currentPassWord)){
            old_pass_word.setText("");
            Toast.makeText(ChangePWActivity.this,"原密码错误",Toast.LENGTH_SHORT).show();
            return;
        }

        if(old_s.trim().equals("") || new_s.trim().equals("") || new_check_s.trim().equals("")){
            Toast.makeText(ChangePWActivity.this, "原密码或密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }else if (!(new_s.trim().equals(new_check_s.trim()))) {
            new_pass_word.setText("");
            new_pass_word_check.setText("");
            Toast.makeText(ChangePWActivity.this, "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
            return;
        }

        //进入注册的Dialog
        Dialog dialog=new AlertDialog.Builder(ChangePWActivity.this)
                .setTitle("修改密码")
                .setMessage("你确定要修改密码吗？")
                .setPositiveButton("确定", new  DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //将用户名信息保存在数据库中
                        UserData userData = new UserData();
                        userData.setPass_word(new_s);
                        userData.updateAll("user_name = ?",currentUserName);

                        //保存输入的信息     edit.commit();提交
                        register_sp=getSharedPreferences("userInfo",MODE_PRIVATE);
                        SharedPreferences.Editor edit=register_sp.edit();
                        edit.putString("password", new_s);
                        edit.apply();
                        //提示成功注册
                        Toast.makeText(ChangePWActivity.this, "恭喜，修改成功", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ChangePWActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                    }
                }).create();//创建一个dialog
        dialog.show();//显示对话框，否者不成功


    }

    public void onBack(View view){
        finish();
    }
}
