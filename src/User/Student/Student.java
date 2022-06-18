package User.Student;

import CustomException.ValidationException;
import Database.CourseQueries.CourseQueries;
import Course.Course;
import User.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Student extends User {

    CourseQueries cq;
    Scanner sc = new Scanner(System.in);

    float marks;

    public Student(){
        super();
    }

    public  Student(User u){
        super(u);
//        this.setUserId(u.getUserId());
        cq = new CourseQueries(u.getConn());
    }

    public float getMarks() {
        return marks;
    }

    public void setMarks(float marks) {
        if(marks<0 || marks>100){
            System.out.println("Invalid entered marks. Marks are not set for this Student");
            return;
        }
        this.marks = marks;
    }

    public void enrollCourse() throws SQLException, ValidationException, InputMismatchException {
        cq.checkCourseCount(this);
        ArrayList<Course> courses = cq.getCourses(3);
        System.out.println("Course List : ");
        cq.display(courses);

        System.out.print("Select a course you want to enroll in : ");
        int ch = sc.nextInt();
        sc.nextLine();

        if(ch>=1 && ch<=courses.size()){
            Course cs = courses.get(ch-1);
            cs.incStudentCount();
            if(cq.enrollStudent(cs,this))
                System.out.println("Student Enrolled into the Course : "+cs.getCourseName());
            else
                System.out.println("Student is not Enrolled into the Course. Try Again.");
        }else{
            System.out.println("Wrong choice.");
        }
    }

    public void leaveCourse() throws SQLException, ValidationException, InputMismatchException{
        ArrayList<Course> courses = cq.getEnrolledCourses(this.getUserId());
        System.out.println("Enrolled Course List : ");
        cq.display(courses);

        System.out.print("Select a course you want to leave from : ");
        int ch = sc.nextInt();
        sc.nextLine();

        if(ch>=1 && ch<=courses.size()){
            Course cs = courses.get(ch-1);
            cs.dscStudentCount();
            if(cq.leaveCourse(cs,this))
                System.out.println("Student Left the Course : "+cs.getCourseName());
            else
                System.out.println("Student could not leave the Course. Try Again.");
        }else{
            System.out.println("Wrong choice.");
        }
    }

    public void showStudentMarks() throws SQLException{
        cq.showStudentMarks(this);
    }

}
