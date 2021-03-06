package com.lcb.goodnote.Course;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.widget.Toast;

import com.lcb.goodnote.R;
import com.lcb.goodnote.activityManger.BaseActivity;
import com.lcb.goodnote.db.CourseData;
import com.zhuangfei.timetable.TimetableView;
import com.zhuangfei.timetable.listener.ISchedule;
import com.zhuangfei.timetable.listener.IWeekView;
import com.zhuangfei.timetable.listener.OnSlideBuildAdapter;
import com.zhuangfei.timetable.model.Schedule;
import com.zhuangfei.timetable.view.WeekView;
import com.lcb.goodnote.Course.CourseInit;

import org.litepal.LitePal;

import java.util.List;

public class CourseActivity extends BaseActivity  implements View.OnClickListener{

    private static final String TAG = "CourseActivity";

    //控件
    TimetableView mTimetableView;
    WeekView mWeekView;

    Button moreButton;
    LinearLayout layout;
    TextView titleTextView;
    List<MySubject> mySubjects;



    //记录切换的周次，不一定是当前周
    int target = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        moreButton = findViewById(R.id.id_more);
        moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopmenu();
            }
        });

        //初始化数据
        mySubjects = SubjectRepertory.loadByLitePal();
//        mySubjects.addAll(SubjectRepertory.loadByLitePal());
        titleTextView = findViewById(R.id.id_title);
        layout = findViewById(R.id.id_layout);
        layout.setOnClickListener(this);
        initTimetableView();
    }
    /**
     * 初始化课程控件
     */
    private void initTimetableView() {
        //获取控件
        mWeekView = findViewById(R.id.id_weekView);
        mTimetableView = findViewById(R.id.id_timetableView);

        //设置周次选择属性
        mWeekView.source(mySubjects)
                .curWeek(16)
                .callback(new IWeekView.OnWeekItemClickedListener() {
                    @Override
                    public void onWeekClicked(int week) {
                        int cur = mTimetableView.curWeek();
                        //更新切换后的日期，从当前周cur->切换的周week
                        mTimetableView.onDateBuildListener()
                                .onUpdateDate(cur, week);
                        mTimetableView.changeWeekOnly(week);
                    }
                })
                .callback(new IWeekView.OnWeekLeftClickedListener() {
                    @Override
                    public void onWeekLeftClicked() {
                        onWeekLeftLayoutClicked();
                    }
                })
                .isShow(false)//设置隐藏，默认显示
                .showView();

        mTimetableView.source(mySubjects)
                .curWeek(16)
                .curTerm("大二下学期")//大三下
                .maxSlideItem(10)
                .monthWidthDp(30)
                //透明度
                //日期栏0.1f、侧边栏0.1f，周次选择栏0.6f
                //透明度范围为0->1，0为全透明，1为不透明
//                .alpha(0.1f, 0.1f, 0.6f)
                .callback(new ISchedule.OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, List<Schedule> scheduleList) {
                        Intent intent = new Intent(CourseActivity.this,ChangeTimeTable.class);
                        String name = "";
                        String teacher ="";
                        String room = "";
                        String weeks = "";
                        int start = -1,step=-1,day=-1;
                        for (Schedule s:scheduleList){
                            name = s.getName();
                            teacher = s.getTeacher();
                            start =s.getStart();
                            room = s.getRoom();
                            step = s.getStep();
                            day = s.getDay();
                            weeks = CourseInit.getWeekListFromInt(s.getWeekList());
                        }
                       intent.putExtra(ChangeTimeTable.NAME,name);
                       intent.putExtra(ChangeTimeTable.START,start);
                       intent.putExtra(ChangeTimeTable.ROOM,room);
                       intent.putExtra(ChangeTimeTable.DAY,day);
                       intent.putExtra(ChangeTimeTable.STEP,step);
                       intent.putExtra(ChangeTimeTable.TEACHER,teacher);
                       intent.putExtra(ChangeTimeTable.WEEKS,weeks);
                        startActivity(intent);
//                        display(scheduleList);
                    }
                })
                .callback(new ISchedule.OnItemLongClickListener() {
                    @Override
                    public void onLongClick(View v, int day, int start) {

                        List<CourseData> dataList = LitePal.where("day = ? and start = ?",""+day,""+start).find(CourseData.class);
                        for (CourseData data:dataList){
                            data.delete();
                        }
                        mySubjects = SubjectRepertory.loadByLitePal();
                        initTimetableView();
                        mTimetableView.updateView();
                        Toast.makeText(CourseActivity.this,
                                "长按删除:周" + day  + ",第" + start + "节",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .callback(new ISchedule.OnWeekChangedListener() {
                    @Override
                    public void onWeekChanged(int curWeek) {
                        titleTextView.setText("第" + curWeek + "周");
                    }
                })
                //旗标布局点击监听
                .callback(new ISchedule.OnFlaglayoutClickListener() {
                    @Override
                    public void onFlaglayoutClick(int day, int start) {
                        mTimetableView.hideFlaglayout();
                        Intent intent = new Intent(CourseActivity.this,AddTimeTable.class);
                        startActivity(intent);
//                        Toast.makeText(CourseActivity.this,
//                                "点击了旗标:周" + (day + 1) + ",第" + start + "节",
//                                Toast.LENGTH_SHORT).show();
                    }
                })
                .showView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mySubjects = SubjectRepertory.loadByLitePal();
        initTimetableView();
        mTimetableView.updateView();
    }

    /**
     * 更新一下，防止因程序在后台时间过长（超过一天）而导致的日期或高亮不准确问题。
     */
    @Override
    protected void onStart() {
        super.onStart();
        mTimetableView.onDateBuildListener()
                .onHighLight();
    }

    /**
     * 周次选择布局的左侧被点击时回调<br/>
     * 对话框修改当前周次
     */
    protected void onWeekLeftLayoutClicked() {
        final String items[] = new String[20];
        int itemCount = mWeekView.itemCount();
        for (int i = 0; i < itemCount; i++) {
            items[i] = "第" + (i + 1) + "周";
        }
        target = -1;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("设置当前周");
        builder.setSingleChoiceItems(items, mTimetableView.curWeek() - 1,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        target = i;
                    }
                });
        builder.setPositiveButton("设置为当前周", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (target != -1) {
                    mWeekView.curWeek(target + 1).updateView();
                    mTimetableView.changeWeekForce(target + 1);
                }
            }
        });
        builder.setNegativeButton("取消", null);
        builder.create().show();
    }

    /**
     * 显示内容
     *
     * @param beans
     */
    protected void display(List<Schedule> beans) {
        String str = "";
        for (Schedule bean : beans) {
            str += bean.getName() + ","+bean.getWeekList().toString()+","+bean.getStart()+","+bean.getStep()+"\n";
        }
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    /**
     * 显示弹出菜单
     */
    public void showPopmenu() {
        PopupMenu popup = new PopupMenu(this, moreButton);
        popup.getMenuInflater().inflate(R.menu.popmenu_base_func, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
//                    case R.id.top1:
//                        addSubject();
//                        break;
//                    case R.id.top2:
//                        deleteSubject();
//                        break;

                    case R.id.top4:
                        hideNonThisWeek();
                        break;
                    case R.id.top5:
                        showNonThisWeek();
                        break;
                    case R.id.top6://最大节次 10
                        setMaxItem(10);
                        break;
                    case R.id.top7:
                        setMaxItem(12);
                        break;
                    case R.id.top8://冬令时
                        showTime("winter");
                        break;
                    case R.id.top9://夏令时
                        showTime("summer");
                        break;
                    case R.id.top10:
                        hideTime();
                        break;
//                    case R.id.top11:
//                        showWeekView();
//                        break;
//                    case R.id.top12:
//                        hideWeekView();
//                        break;
//                    case R.id.top13://增大月份宽度
//                        setMonthWidth();
//                        break;
//                    case R.id.top16://恢复月份宽度
//                        resetMonthWidth();
//                        break;
                    case R.id.top14://隐藏周末
                        hideWeekends();
                        break;
                    case R.id.top15://显示周末
                        showWeekends();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

        popup.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_layout:
                //如果周次选择已经显示了，那么将它隐藏，更新课程、日期
                //否则，显示
                if (mWeekView.isShowing()) {
                    mWeekView.isShow(false);
                    titleTextView.setTextColor(getResources().getColor(R.color.app_course_textcolor_blue));
                    int cur = mTimetableView.curWeek();
                    mTimetableView.onDateBuildListener()
                            .onUpdateDate(cur, cur);
                    mTimetableView.changeWeekOnly(cur);
                } else {
                    mWeekView.isShow(true);
                    titleTextView.setTextColor(getResources().getColor(R.color.app_red));
                }
                break;
        }
    }

    /**
     * 删除课程
     * 内部使用集合维护课程数据，操作集合的方法来操作它即可
     * 最后更新一下视图（全局更新）
     */
    protected void deleteSubject() {
        int size = mTimetableView.dataSource().size();
        int pos = (int) (Math.random() * size);
        if (size > 0) {
            mTimetableView.dataSource().remove(pos);
            mTimetableView.updateView();
        }
    }

    /**
     * 添加课程
     * 内部使用集合维护课程数据，操作集合的方法来操作它即可
     * 最后更新一下视图（全局更新）
     */
    protected void addSubject() {
        List<Schedule> dataSource = mTimetableView.dataSource();
        int size = dataSource.size();
        if (size > 0) {
            Schedule schedule = dataSource.get(0);
            dataSource.add(schedule);
            mTimetableView.updateView();
        }
    }

    /**
     * 隐藏非本周课程
     * 修改了内容的显示，所以必须更新全部（性能不高）
     * 建议：在初始化时设置该属性
     * <p>
     * updateView()被调用后，会重新构建课程，课程会回到当前周
     */
    protected void hideNonThisWeek() {
        mTimetableView.isShowNotCurWeek(false).updateView();
    }

    /**
     * 显示非本周课程
     * 修改了内容的显示，所以必须更新全部（性能不高）
     * 建议：在初始化时设置该属性
     */
    protected void showNonThisWeek() {
        mTimetableView.isShowNotCurWeek(true).updateView();
    }

    /**
     * 设置侧边栏最大节次，只影响侧边栏的绘制，对课程内容无影响
     *
     * @param num
     */
    protected void setMaxItem(int num) {
        mTimetableView.maxSlideItem(num).updateSlideView();
    }

    /**
     * 显示时间
     * 设置侧边栏构建监听，TimeSlideAdapter是控件实现的可显示时间的侧边栏
     */
    protected void showTime(String time) {
        String[] times;
        if (time.equals("summer")){//夏令时
            times = new String[]{
                    "8:30", "9:20", "10:20", "11:00",
                    "14:30", "15:10", "16:10", "17:00",
                    "19:00", "19:40", "20:30", "21:10"
            };
        }else {//冬令时
            times = new String[]{
                    "8:30", "9:20", "10:20", "11:00",
                    "14:00", "14:40", "15:40", "16:30",
                    "19:00", "19:40", "20:30", "21:10"
            };
        }
        OnSlideBuildAdapter listener = (OnSlideBuildAdapter) mTimetableView.onSlideBuildListener();
        listener.setTimes(times)
                .setTimeTextColor(Color.BLACK);
        mTimetableView.updateSlideView();
    }

    /**
     * 隐藏时间
     * 将侧边栏监听置Null后，会默认使用默认的构建方法，即不显示时间
     * 只修改了侧边栏的属性，所以只更新侧边栏即可（性能高），没有必要更新全部（性能低）
     */
    protected void hideTime() {
        mTimetableView.callback((ISchedule.OnSlideBuildListener) null);
        mTimetableView.updateSlideView();
    }

    /**
     * 显示WeekView
     */
    protected void showWeekView() {
        mWeekView.isShow(true);
    }

    /**
     * 隐藏WeekView
     */
    protected void hideWeekView() {
        mWeekView.isShow(false);
    }

    /**
     * 设置月份宽度
     */
    private void setMonthWidth() {
        mTimetableView.monthWidthDp(50).updateView();
    }

    /**
     * 设置月份宽度,默认40dp
     */
    private void resetMonthWidth() {
        mTimetableView.monthWidthDp(40).updateView();
    }

    /**
     * 隐藏周末
     */
    private void hideWeekends() {
        mTimetableView.isShowWeekends(false).updateView();
    }

    /**
     * 显示周末
     */
    private void showWeekends() {
        mTimetableView.isShowWeekends(true).updateView();
    }
}
