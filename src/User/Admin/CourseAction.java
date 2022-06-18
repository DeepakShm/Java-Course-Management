package User.Admin;

import CustomException.ValidationException;
import Database.AdminQueries.AdminQueries;
import Database.CourseQueries.CourseQueries;
import Database.SharedQueries.ShowHighLow;
import Database.TeacherQueries.TeacherQueries;
import Course.Course;
import User.Teacher.Teacher;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Scanner;

public class CourseAction {

    AdminQueries adminq;
    TeacherQueries teachq;

    CourseQueries courseq;

    ShowHighLow shl;

    Scanner sc = new Scanner(System.in);

    public CourseAction(Connection c){
        adminq = new AdminQueries(c);
        teachq = new TeacherQueries(c);
        courseq = new CourseQueries(c);
        shl = new ShowHighLow(c);
    }

    public void  addCourse() throws SQLException, ValidationException, InputMismatchException {

        ArrayList<Teacher> teachers = teachq.getTeachers();

        Course cs = new Course();

        System.out.print("Enter Course Name : ");
        cs.setCourseName(sc.nextLine());

        System.out.print("Enter Course Code : ");
        cs.setCourseId(sc.next());

        System.out.print("Enter Student Limit in course : ");
        cs.setMaxLimit(sc.nextInt());
        sc.nextLine();
        if(teachers.size()>0){
            System.out.println("Teacher List");
            teachq.display(teachers);

            int[] nums = assignCourseTeacher();

            if(nums[0]>=1 && nums[1]>=1 && nums[0]==nums[1]){
                System.out.println("Primary and Secondary Teacher cannot be same");
            }

            if(nums[0]>=1 && nums[0] != nums[1] && nums[0]<=teachers.size() ){
                cs.setPmTeacher(teachers.get(nums[0]-1).getUserId());
            }

            if(nums[1]>=1 && nums[0] != nums[1] && nums[1]<=teachers.size() ){
                cs.setScTeacher(teachers.get(nums[1]-1).getUserId());
            }
        }

        if(adminq.addCourse(cs)){
            System.out.println("Course Added");
        }else{
            System.out.println("Course is not Added.");
        }

    }

    public void selectCourse() throws SQLException, ValidationException,InputMismatchException {
        ArrayList<Course> courses = courseq.getCourses(1);
        courseq.display(courses);

        System.out.print("Select a course which you want to Update : ");
        int ch = sc.nextInt();
        sc.nextLine();
        if(ch>=1 && ch<=courses.size()){
            Course cs = courses.get(ch-1);

            updateCourse(cs);

        }else{
            System.out.println("Wrong choice.");
        }
    }

    void updateCourse(Course cs) throws SQLException, ValidationException,InputMismatchException{

        ArrayList<Teacher> teachers = teachq.getTeachers();

        System.out.print("Enter Updated Course Name : ");
        cs.setCourseName(sc.nextLine());


        System.out.print("Enter Student Limit in course : ");
        int limit = sc.nextInt();
        sc.nextLine();
        if(limit < cs.getStudentCount()){
            System.out.println("Max Limit should be greater than Student enrolled");
        }
        else cs.setMaxLimit(limit);

        if(teachers.size()>0){
            System.out.println("Teacher List");
            teachq.display(teachers);

            int[] nums = assignCourseTeacher();

            if(nums[0]>=1 && nums[1]>=1 && nums[0]==nums[1]){
                System.out.println("Primary and Secondary Teacher cannot be same");
            }

            if(nums[0]>=1 && nums[0] != nums[1] && nums[0]<=teachers.size() ){
                cs.setPmTeacher(teachers.get(nums[0]-1).getUserId());
            }

            if(nums[1]>=1 && nums[0] != nums[1] && nums[1]<=teachers.size() ){
                cs.setScTeacher(teachers.get(nums[1]-1).getUserId());
            }
        }

        if(adminq.updateCourse(cs)){
            System.out.println("Course Updated");
        }else{
            System.out.println("Course is not Updated.");
        }

    }



    int[] assignCourseTeacher() throws InputMismatchException{
        String ch;
        int pmNum=-1,scNum=-1;

        System.out.print("Want to assign Primary Teacher (Y/N) : ");
        ch = sc.next().toLowerCase(Locale.ROOT);
        if(ch.equals("y")){
            System.out.print("Enter choice for Primary teacher from above list : ");
            pmNum = sc.nextInt();
            sc.nextLine();
        }

        System.out.print("Want to assign Secondary Teacher (Y/N) : ");
        ch = sc.next().toLowerCase(Locale.ROOT);
        if(ch.equals("y")){
            System.out.print("Enter choice for Secondary teacher from above list : ");
            scNum = sc.nextInt();
            sc.nextLine();
        }

        return new int[]{pmNum, scNum};
    }

    void showHighLow() throws SQLException, ValidationException , InputMismatchException{
       ArrayList<Course> courses = courseq.getCourses(1);
       courseq.display(courses);

        System.out.print("Select a course for which you want Highest and Lowest : ");
        int ch = sc.nextInt();
        sc.nextLine();

        if(ch>=1 && ch<=courses.size()){
            Course cs = courses.get(ch-1);

            System.out.println("\n");
            shl.showHighestScorer(cs.getCourseId());
            System.out.println("\n");
            shl.showLowestScorer(cs.getCourseId());
            System.out.println("\n");

        }else{
            System.out.println("Wrong choice.");
        }

    }
}
