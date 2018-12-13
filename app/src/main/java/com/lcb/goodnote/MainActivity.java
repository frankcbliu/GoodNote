package com.lcb.goodnote;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import android.support.design.widget.Snackbar;
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
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;





public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private DrawerLayout mDrawerLayout;

    //note list 内容
    private Note[] notes = {new Note("Note1","2018-12-6"),new Note("Note2","2018-12-11"),
            new Note("I'm Three","2018-12-12"),new Note("I'm 4","2018-1d-6"),new Note("I'm Three5","2018-10-6"),new Note("Note 6","2018-11-6")};

    private List<Note> noteList = new ArrayList<>();
    private NoteAdapter adapter;



    //调用相机
    public static final int TAKE_PHOTO = 3;
    public static final int GET_PHOTO = 4;
    private ImageView headImage;
    private Uri imageUri;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //上方标题栏
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //滑动菜单栏
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }

//        navView.setCheckedItem(R.id.nav_task);//设置task为默认选中
        navView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener(){
                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.nav_changeIcon://修改头像
                                changeImage();
                                break;
                            case R.id.nav_task:
                                Intent intent2 = new Intent(MainActivity.this,AddActivity.class);
                                startActivity(intent2);
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
                Snackbar.make(view,"Data delete",Snackbar.LENGTH_SHORT)
                        .setAction("取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(MainActivity.this,"Data restored",Toast.LENGTH_SHORT).show();
                            }
                        }).show();
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



    }

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
                Toast.makeText(this,"You click Backup.",Toast.LENGTH_SHORT).show();
                break;
            case R.id.delete:
                Toast.makeText(this,"You click Delete.",Toast.LENGTH_SHORT).show();
                break;
            case R.id.settings:
                Toast.makeText(this,"You click Settings.",Toast.LENGTH_SHORT).show();
                break;
            default:
        }
        return true;
    }
    //初始化笔记note
    private void initNotes(){
        noteList.clear();
        for (int i = 0; i < 15; i++) {
            Random random = new Random();
            int index = random.nextInt(notes.length);
            noteList.add(notes[index]);
        }
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
                                    "com.example.cameraalbumtest.fileprovider",outputImage);
                        }else {
                            imageUri = Uri.fromFile(outputImage);
                        }
                        //启动相机程序
                        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                        startActivityForResult(intent,TAKE_PHOTO);
                        break;
                    case "从图库选择":
                        Intent i = new Intent(
                                Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(i, GET_PHOTO);

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


}

