package com.lcb.goodnote.Course;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.lcb.goodnote.R;
import com.lcb.goodnote.db.CourseData;
import com.lcb.goodnote.activityManger.BaseActivity;

import org.litepal.LitePal;

import java.util.List;

public class ChangeTimeTable extends BaseActivity {

    private String TAG = "ChangeTimeTable";
    private EditText et_name,et_teacher;
    private EditText et_weeks,et_day,et_start_step,et_room;

    private String name,teacher,weeks,day,start_step,room;

    private final String NAME_NULL = "name_null";       //课程名不能为空
    private final String WEEKS_NULL = "weeks_null";     //周数不能为空
    private final String WEEKS_ERROR = "weeks_erros";   //周数输入格式错误
    private final String DAY_NULL = "day_null";         //...以此类推
    private final String DAY_ERROR = "day_error";
    private final String SS_NULL = "ss_null";
    private final String SS_ERROR = "ss_error";
    private final String ROOM_NULL = "room_null";

    //传递信息
    public static final String NAME = "name";
    public static final String START = "start";
    public static final String TEACHER = "teacher";
    public static final String ROOM = "room";
    public static final String STEP = "step";
    public static final String DAY ="day";
    public static final String WEEKS ="weeks";

    private String old_course;
    private int get_start,get_step;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_time_table);
        init();
        TextView cancel = (TextView) findViewById(R.id.tv_back);
        cancel.setText("删除");
        Intent intent = getIntent();
        get_start = intent.getIntExtra(START,0);
        get_step = intent.getIntExtra(STEP,0);
        et_room.setText(intent.getStringExtra(ROOM));
        et_day.setText(intent.getIntExtra(DAY,0)+"");
        et_teacher.setText(intent.getStringExtra(TEACHER));
        et_weeks.setText(intent.getStringExtra(WEEKS));
        et_start_step.setText(get_start+"-"+(get_step+get_start-1));
        et_name.setText(intent.getStringExtra(NAME));

        old_course = "2018-2019学年春,"+et_name.getText().toString()+','+
                et_teacher.getText().toString()+ ','+
                "["+ et_weeks.getText().toString() +"]"+','+
                et_day.getText().toString()+ ','+
                getSS(et_start_step.getText().toString())+','+
                et_room.getText().toString();
//        Toast.makeText(this,old_course,Toast.LENGTH_LONG).show();
    }


    private void init(){
        et_name = (EditText) findViewById(R.id.add_course_name_et);
        et_teacher = (EditText) findViewById(R.id.add_course_teacher_et);
        et_weeks = (EditText)findViewById(R.id.add_course_weeks_et);
        et_day = (EditText) findViewById(R.id.add_course_day_et);
        et_start_step = (EditText) findViewById(R.id.add_course_start_step_et);
        et_room = (EditText)findViewById(R.id.add_course_room_et);
    }

    private String getString(){
        name = et_name.getText().toString();
        teacher = et_teacher.getText().toString();
        weeks = et_weeks.getText().toString();
        day = et_day.getText().toString();
        start_step = et_start_step.getText().toString();
        room = et_room.getText().toString();
        if (name.length()==0){  //增加输入判断
            return NAME_NULL;
        }else if (weeks.length()==0){
            return WEEKS_NULL;
        } else if (start_step.length()==0) {
            return SS_NULL;
        }else if (day.length()==0){
            return DAY_NULL;
        }else if (day.length()>1){
            return DAY_ERROR;
        }else if (room.length()==0){
            return ROOM_NULL;
        }
        weeks = getWeeks(weeks);
        start_step = getSS(start_step);
        if (weeks.equals(WEEKS_ERROR)){
            return WEEKS_ERROR;
        }else if (start_step.equals(SS_ERROR)){
            return SS_ERROR;
        }
        return "2018-2019学年春,"+name+','+teacher+','+weeks+','+day+','+start_step+','+room;
    }

    public void onSave(View view){
        //刷新一遍字符串
        String course = getString();
        //输入错误
        switch (course){
            case NAME_NULL:
                Toast.makeText(ChangeTimeTable.this, "课程名不能为空", Toast.LENGTH_SHORT).show();
                return;
            case WEEKS_NULL:
                Toast.makeText(ChangeTimeTable.this, "周数不能为空", Toast.LENGTH_SHORT).show();
                return;
            case WEEKS_ERROR:
                Toast.makeText(ChangeTimeTable.this, "周数输入格式错误", Toast.LENGTH_SHORT).show();
                return;
            case DAY_NULL:
                Toast.makeText(ChangeTimeTable.this, "周几上课不能为空", Toast.LENGTH_SHORT).show();
                return;
            case DAY_ERROR:
                Toast.makeText(ChangeTimeTable.this, "周几上课输入格式错误", Toast.LENGTH_SHORT).show();
                return;
            case SS_NULL:
                Toast.makeText(ChangeTimeTable.this, "上课节数不能为空", Toast.LENGTH_SHORT).show();
                return;
            case SS_ERROR:
                Toast.makeText(ChangeTimeTable.this, "上课节数输入错误", Toast.LENGTH_SHORT).show();
                return;
            case ROOM_NULL:
                Toast.makeText(ChangeTimeTable.this, "地点不能为空", Toast.LENGTH_SHORT).show();
                return;
        }

        //保存课程
        updateCourse(course);
        finish();
        Intent intent = new Intent(ChangeTimeTable.this,CourseActivity.class);
        startActivity(intent);
    }

    /**
     * 更新课程
     * @param newCourse
     * @return
     */
    private CourseData updateCourse(String newCourse){
//        old_course
        CourseData course = new CourseData();
        course.setTerm(CourseInit.getTerm(newCourse));
        course.setName(CourseInit.getName(newCourse));
        course.setTeacher(CourseInit.getTeacher(newCourse));
        course.setWeekList(CourseInit.getWeekList(newCourse));
        course.setDay(CourseInit.getDay(newCourse));
        course.setStart(CourseInit.getStart(newCourse));
        course.setStep(CourseInit.getStep(newCourse));
        course.setRoom(CourseInit.getRoom(newCourse));
        course.updateAll("name = ? and day = ? and start = ?", CourseInit.getName(old_course), ""+CourseInit.getDay(old_course), ""+CourseInit.getStart(old_course));
        return course;
    }
    public void onCancel(View view){//删除

        //刷新一遍字符串
        String course = getString();

        List<CourseData> datas = LitePal.where("name = ? and day = ? and start = ?", CourseInit.getName(old_course), ""+CourseInit.getDay(old_course), ""+CourseInit.getStart(old_course)).find(CourseData.class);
        for (CourseData data:datas){
            data.delete();
        }
        finish();
        Intent intent = new Intent(ChangeTimeTable.this,CourseActivity.class);
        startActivity(intent);
        Toast.makeText(ChangeTimeTable.this,"成功删除课程："+CourseInit.getName(old_course),Toast.LENGTH_SHORT).show();
    }
    /**
     * 获取周数
     * @param weeks
     * @return
     */
    private String getWeeks(String weeks){
        String[] num = weeks.split("[-]");
        String res = "";
        if (num.length>1){
            for (int i = Integer.parseInt(num[0]); i <= Integer.parseInt(num[1]); i++) {
                res = res + i + '.';
            }
            return "["+res+"]";
        }else {
            if (weeks.length()==1){
                return "["+num[0]+"]";
            }else if (weeks.length()==0){
                return WEEKS_NULL;
            }else {
                return WEEKS_ERROR;
            }

        }
    }

    private String getSS(String start_step){
        String[] num = start_step.split("[-]");
        if (num.length==2){
            int length = Integer.parseInt(num[1]) - Integer.parseInt(num[0])+1;
            return num[0]+","+length;
        }else if (start_step.length()==1){
            return start_step+","+start_step;
        }else {
            return SS_ERROR;
        }
    }
}
