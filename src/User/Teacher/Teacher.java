package User.Teacher;

import CustomException.ValidationException;
import Database.CourseQueries.CourseQueries;
import Database.SharedQueries.ShowHighLow;
import Database.TeacherQueries.TeacherQueries;
import Course.Course;
import User.Student.Student;
import User.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Teacher extends User {

    TeacherQueries tq;
    CourseQueries cq;
    Scanner sc = new Scanner(System.in);

    public Teacher(){
        super();
    }

    public Teacher(User u){
        super(u);
//        this.setUserId(u.getUserId());
        tq = new TeacherQueries(u.getConn());
        cq = new CourseQueries(u.getConn());
    }


    public void showHighLow() throws SQLException, ValidationException, InputMismatchException {
        ArrayList<Course> courses = cq.getTeacherCourses(this.getUserId());
        cq.display(courses);

        System.out.print("Select a course for which you want Highest and Lowest : ");
        int ch = sc.nextInt();
        sc.nextLine();

        if(ch>=1 && ch<=courses.size()){
            Course cs = courses.get(ch-1);
            ShowHighLow shl = new ShowHighLow(this.getConn());

            System.out.println("\n");
            shl.showHighestScorer(cs.getCourseId());
            System.out.println("\n");
            shl.showLowestScorer(cs.getCourseId());
            System.out.println("\n");

        }else{
            System.out.println("Wrong choice.");
        }
    }

    public void enterMarks() throws SQLException,ValidationException, InputMismatchException {

        ArrayList<Course> courses = cq.getTeacherCourses(this.getUserId());
        cq.display(courses);

        System.out.print("Select a Course for which you want to enter marks : ");
        int ch = sc.nextInt();
        sc.nextLine();

        if(ch>=1 && ch<=courses.size()){
            Course cs = courses.get(ch-1);

            ArrayList<Student> stds = tq.getStudents(cs.getCourseId());
            System.out.println("Enter Marks only from 0 to 100");
            System.out.println("Total "+stds.size()+ " Students in "+cs.getCourseName());
            for (Student std : stds) {
                System.out.printf("Enter marks of %s [ %s ] : ",
                        std.getUserName(),
                        std.getEmail());
                std.setMarks(sc.nextFloat());
            }

            tq.setMarks(stds,cs.getCourseId());
            System.out.println("Marks Added");
        }else{
            System.out.println("Wrong choice.");
        }

    }



}
