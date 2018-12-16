package com.lcb.goodnote;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.content.FileProvider;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lcb.goodnote.Course.CourseActivity;
import com.lcb.goodnote.Course.CourseInit;
import com.lcb.goodnote.activityManger.ActivityCollector;
import com.lcb.goodnote.activityManger.BaseActivity;
import com.lcb.goodnote.db.ActivityData;
import com.lcb.goodnote.db.CourseData;
import com.lcb.goodnote.db.UserData;
import com.lcb.goodnote.login.ChangePWActivity;
import com.lcb.goodnote.server.LongRunningService;

import org.litepal.LitePal;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;





public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";
    private DrawerLayout mDrawerLayout;

    //note list 内容
    private String currentUserName;
    private List<Note> noteList = new ArrayList<>();
    private NoteAdapter adapter;
//    private Note[] notes = {new Note("note1","2017-02-05"),new Note("notr2","45")};


    public static int TIME; //记录时间间隔

    //调用相机
    public static final int TAKE_PHOTO = 3;
    public static final int GET_PHOTO = 4;
    private ImageView headImage;
    private Uri imageUri;
    //修改用户名
    private NavigationView navView;
    private TextView username;

    //登陆与退出功能
    private SharedPreferences back_login;

    //下拉刷新
    private SwipeRefreshLayout swipeRefresh;
    //删除、拖动
    private RecyclerView mRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //下拉刷新
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        //上方标题栏
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //滑动菜单栏
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navView = (NavigationView) findViewById(R.id.nav_view);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);

        }
        changeUserName();


//        navView.setCheckedItem(R.id.nav_task);//设置task为默认选中
        navView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener(){
                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.nav_changeIcon://修改头像
                                changeImage();
                                break;
                            case R.id.nav_task://我的课表
                                Intent intent = new Intent(MainActivity.this, CourseActivity.class);
                                startActivity(intent);
                                break;
                            case R.id.nav_friends://修改密码
                                Intent intent_c_psw = new Intent(MainActivity.this,ChangePWActivity.class);
                                startActivity(intent_c_psw);
                                break;
                            case R.id.nav_exit://退出程序
                                ActivityCollector.finishAll();
                                android.os.Process.killProcess(android.os.Process.myPid());
                                break;
                        }
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                }


        );

        //悬浮按钮点击事件
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floatAButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
                // Toast.makeText(MainActivity.this,"FAB clicked",Toast.LENGTH_SHORT).show();
            }
        });

        //内容区间
        initNotes();
        RecyclerView recyclerView =(RecyclerView)findViewById(R.id.recycler_view);
        GridLayoutManager layoutmanger = new GridLayoutManager(this,1);//一列
        recyclerView.setLayoutManager(layoutmanger);
        adapter = new NoteAdapter(noteList);
        recyclerView.setAdapter(adapter);

//        下拉刷新
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshNote();
            }
        });

        //删除、拖动
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        ItemTouchHelper.Callback callback = new myItemTouchHelperCallBack(adapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(mRecyclerView);
    }//onCreate

    //加载toolbar.xml -- 菜单文件
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }
    //监听菜单中的各个按钮的点击事件

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.backup:
//                showUserData();
                CourseInit courseInit = new CourseInit();
                ActivityData activityData = new ActivityData();
                currentUserName = getSharedPreferences("userInfo",Activity.MODE_PRIVATE).getString("username","");
                activityData.setUsername(currentUserName);
                activityData.setActivity_theme("实验室搬砖");
                activityData.setActivity_address("信工楼N106");
                activityData.setActivity_content("做项目、预习期末考试科目");
                activityData.setActivity_year(2018);
                activityData.setActivity_month(12);
                activityData.setActivity_day(19);
                activityData.save();
                refreshNote();
                Toast.makeText(this,"初始化预设课程和活动.",Toast.LENGTH_SHORT).show();
                break;
            case R.id.delete://删除所有课程
                Snackbar.make(mRecyclerView,"你将会清空所有课程和活动，是否继续？",Snackbar.LENGTH_LONG)
                        .setAction("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                List<CourseData> courseDataList = LitePal.findAll(CourseData.class);
                                for (int i = 0; i < courseDataList.size(); i++) {
                                    courseDataList.get(i).delete();
                                }
                                List<ActivityData> activityData = LitePal.findAll(ActivityData.class);
                                for (int i = 0; i < activityData.size(); i++) {
                                    activityData.get(i).delete();
                                }
                                Toast.makeText(MainActivity.this,"成功清空所有课程表",Toast.LENGTH_SHORT).show();
                                refreshNote();
                            }
                        }).show();
                break;
            case R.id.settings:
                Intent startIntent = new Intent(this,LongRunningService.class);
                TIME = Integer.parseInt("1");
                //通过Intent将时间间隔传递给Service
                startIntent.putExtra("Time",TIME);
//                Toast.makeText(MainActivity.this,"开始提醒",Toast.LENGTH_SHORT).show();
                startService(startIntent);
                Toast.makeText(this,"打开提醒功能.",Toast.LENGTH_SHORT).show();
                break;
            case R.id.close:
                Intent stopIntent = new Intent(this,LongRunningService.class);
                Toast.makeText(MainActivity.this,"关闭提醒功能",Toast.LENGTH_SHORT).show();
                stopService(stopIntent);
                break;
            case R.id.exit:
                //退出账号功能
                back_login = getSharedPreferences("back_login",MODE_PRIVATE);
                SharedPreferences.Editor editor = back_login.edit();
                editor.putBoolean("back_login", false);
                editor.apply();
                //强制下线广播
                Intent intent = new Intent("com.lcb.goodnote.activityManger.FORCE_OFFLINE");
                sendBroadcast(intent);
            default:
        }
        return true;
    }
    //初始化笔记note
    private void initNotes(){
        noteList.clear();
        //当前用户名
        currentUserName = getSharedPreferences("userInfo",Activity.MODE_PRIVATE).getString("username","");
        List<ActivityData> list = LitePal.findAll(ActivityData.class);
        for (ActivityData data:list){
            if (data.getUsername().equals(currentUserName)){//判断是否当前用户名
                String date = data.getActivity_year()+"-"+data.getActivity_month()+"-"+data.getActivity_day();
                Note note = new Note(data.getActivity_theme(),date,data);
                noteList.add(note);
            }
        }
        showUserData();

    }

    private void refreshNote(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(1000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initNotes();
                        adapter.notifyDataSetChanged();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }

        }).start();
    }

    public void changeImage(){//修改头像功能
        final String font[] = {"拍照", "从图库选择"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("上传头像");
        builder.setItems(font, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch(font[which]){
                    case "拍照":
                        File outputImage = new File(getExternalCacheDir(),
                                "output_image.jpg");
                        try{
                            if (outputImage.exists()){
                                outputImage.delete();
                            }
                            outputImage.createNewFile();
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                        if (Build.VERSION.SDK_INT >=24){
                            imageUri = FileProvider.getUriForFile(MainActivity.this,
                                    "com.lcb.goodnote.fileprovider",outputImage);
                        }else {
                            imageUri = Uri.fromFile(outputImage);
                        }
                        //启动相机程序
                        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                        startActivityForResult(intent,TAKE_PHOTO);
                        break;
                    case "从图库选择":
                        //添加运行时权限，否则会报错
                        if (ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                        }else {
                            Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(i, GET_PHOTO);
                        }
                }
            }
        }).setNegativeButton("取消", null).create().show();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == TAKE_PHOTO){ //调用相机成功
            if (resultCode == RESULT_OK){
                try{
                    Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                    headImage = (ImageView) findViewById(R.id.icon_image);//尝试了许多方法，只有这种方法不会闪退
                    headImage.setImageBitmap(comp(bitmap));
                }catch (Exception e){
                    Toast.makeText(MainActivity.this,"bitmap error",Toast.LENGTH_SHORT).show();
                }
            }

        }else if(requestCode == GET_PHOTO) {
            //从图库选择照片
            try {
//            Log.d(TAG,"photo_by_get");
                if(data.getData() != null) {  //不选中直接返回
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    headImage = (ImageView) findViewById(R.id.icon_image);
                    headImage.setImageBitmap(comp(BitmapFactory.decodeFile(picturePath)));
                    cursor.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private static Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while ( baos.toByteArray().length / 1024>100) { //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }
    private static Bitmap comp(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        if( baos.toByteArray().length / 1024>1024) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 50, baos);//这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
    }

    /**
     * 获得照片路径
     *
     * @return
     */
    private String getPhotoPath() {
        return Environment.getExternalStorageDirectory() + "/DCIM/";
    }

    private void changeUserName(){
        //获取同户名
        String user_name = getSharedPreferences("userInfo",Activity.MODE_PRIVATE).getString("username","");
        //打印日志
//        Log.d(TAG,"user name = "+user_name);
        //修改用户名
        View headview = navView.getHeaderView(0);
        username = (TextView)headview.findViewById(R.id.username);
        username.setText("用户名："+user_name);
    }

    private void showUserData()
    {
        List<UserData> users = LitePal.findAll(UserData.class);
        for (UserData user:users){
            Log.d(TAG,"ID: "+user.getId());
            Log.d(TAG,"UserName: "+user.getUser_name().toString());
            Log.d(TAG,"PassWord: "+user.getPass_word().toString());
            Log.d(TAG,user.getActivities().toString());
        }
        List<ActivityData> list = LitePal.findAll(ActivityData.class);
        for (ActivityData data:list){
            Log.d(TAG,"id: "+data.getId()+" "+data.toString());
        }
    }
}

