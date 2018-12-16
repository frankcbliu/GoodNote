package com.lcb.goodnote.server;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.WindowManager;



import java.io.File;
import java.io.IOException;
import java.util.Date;


public class LongRunningService extends Service {

    public int anHour; //记录间隔时间

    public int number = 0; //记录alertdialog出现次数

    private MediaPlayer mediaPlayer = new MediaPlayer();

    AlarmManager manager;
    PendingIntent pi;


    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    if (!mediaPlayer.isPlaying()){
                        mediaPlayer.start();
                    }
                    AlertDialog.Builder builder = new AlertDialog.Builder(LongRunningService.this);
                    builder.setTitle("提醒");
                    builder.setMessage("该上课啦" + (number-1));
                    builder.setCancelable(false);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mediaPlayer.reset();
                            initMediaPlayer();
                        }
                    });
                    final AlertDialog dialog = builder.create();
                    dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                    dialog.show();
            }
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initMediaPlayer();

    }

    private void initMediaPlayer() {
        File file = new File("/storage/emulated/0/naoling","music.mp3");
        try {
            mediaPlayer.setDataSource(file.getPath());
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (number!=0) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Log.e("bai", "executed at " + new Date().toString());
                    mHandler.sendEmptyMessage(1);
                }
            }).start();
        }
        manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int time = intent.getIntExtra("Time",2);
        anHour = time*60*1000;
        Log.e("bai","Time:"+time+"anhour:"+anHour);
        long triggerAtTime = SystemClock.elapsedRealtime()+(anHour);
        Intent i = new Intent(this,AlarmReceiver.class);
        pi = PendingIntent.getBroadcast(this,0,i,0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pi);
        number++;
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.release();
        manager.cancel(pi);
    }
}
