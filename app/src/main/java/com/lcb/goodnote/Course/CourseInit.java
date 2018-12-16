package com.lcb.goodnote.Course;

import com.lcb.goodnote.db.CourseData;

import java.util.ArrayList;
import java.util.List;

public class CourseInit {
    public static List<String> strings = new ArrayList<>();

    /**
     * 将字符串解析，加入数据库中
     * @param newCourse
     * @return
     */
    public static CourseData loadCourses(String newCourse){
        CourseData course = new CourseData();
        course.setTerm(getTerm(newCourse));
        course.setName(getName(newCourse));
        course.setTeacher(getTeacher(newCourse));
        course.setWeekList(getWeekList(newCourse));
        course.setDay(getDay(newCourse));
        course.setStart(getStart(newCourse));
        course.setStep(getStep(newCourse));
        course.setRoom(getRoom(newCourse));
        course.save();
        return course;
    }


    public CourseInit(){
        //init strings
        String s1 = "2018-2019学年春,数据库系统（实验）,庄华,[1.2.3.4.5],1,7,2,计软楼实验室326";
        String s2 = "2018-2019学年春,数据库系统,庄华,[1.2.3.4.5],3,3,2,教学楼A208";
        String s3 = "2018-2019学年春,数据结构（实验）,张艳,[1.2.3.4.5.6.7],2,5,4,计软楼325";
        String s4 = "2018-2019学年春,数据结构,张艳,[1.3.5.7],2,1,2,理工楼L1-606";
        strings.add(s1);
        strings.add(s2);
        strings.add(s3);
        strings.add(s4);
        loadCourses(s1);//将字符串加入数据库中
        loadCourses(s2);
        loadCourses(s3);
        loadCourses(s4);
    }

    /**
     * 获取学期 ———— 0
     * @param course
     * @return
     */
    public static String getTerm(String course) {
        return course.split("[,]")[0];
    }

    /**
     * 从字符串中获取课程名 ———— 1
     * @param course
     * @return
     */
    public static String getName(String course){
        return course.split(",")[1];
    }


    /**
     * 获取老师姓名 ———— 2
     * @param course
     * @return
     */
    public static String getTeacher(String course) {
        return course.split("[,]")[2];
    }

    /**
     * 从字符串中获取上课周数 ———— 3
     * @param course
     * @return
     */
    public static List<Integer> getWeekList(String course){
        String s = course.split(",")[3];//将[1.2.3.4.5]取出
        s =s.substring(1,s.length()-1);//将[]去掉
        String[] list = s.split("[.]");
        //将数字存到List<Integer>中
        List<Integer> integers = new ArrayList<>();
        for (int i = 0; i < list.length; i++) {
            integers.add(Integer.parseInt(list[i]));
        }
        return integers;
    }

    public static String getWeekListFromInt(List<Integer> list){
        if (list.size()==1){
            return list.get(0)+"";
        }else {
            return list.get(0)+"-"+list.get(list.size()-1);
        }
    }
    /**
     * 获取周几上课 ———— 4
     * @param course
     * @return
     */
    public static int getDay(String course) {
        return Integer.parseInt(course.split("[,]")[4]);
    }

    /**
     * 获取开始上课的节数 ———— 5
     * @param course
     * @return
     */
    public static int getStart(String course) {
        return Integer.parseInt(course.split("[,]")[5]);
    }

    /**
     * 获取开始上课的时长，上几节课 ———— 6
     * @param course
     * @return
     */
    public static int getStep(String course) {
        return Integer.parseInt(course.split("[,]")[6]);
    }

    /**
     * 获取地点 ———— 7
     * @param course
     * @return
     */
    public static String getRoom(String course) {
        return course.split("[,]")[7];
    }

}
