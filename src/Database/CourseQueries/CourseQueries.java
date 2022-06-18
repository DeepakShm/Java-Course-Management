package Database.CourseQueries;

import CustomException.ValidationException;
import Course.Course;
import User.Student.Student;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class CourseQueries {
    Connection conn;

    public CourseQueries(Connection c){
        conn = c;
    }

    public ArrayList<Course> getCourses(int role) throws SQLException {
        ArrayList<Course> courses = new ArrayList<>();

        Statement stm = conn.createStatement();
        String q = "SELECT * FROM courses";
        if(role==3){
            // for students
            q = q + " WHERE studentCount < maxLimit";
        }

        ResultSet rs = stm.executeQuery(q);

        return addCourseList(rs,courses);
    }

    public ArrayList<Course> getEnrolledCourses(String sid) throws SQLException {
        ArrayList<Course> courses = new ArrayList<>();

        Statement stm = conn.createStatement();
        String q = "SELECT * FROM courses as cs\n" +
                    "INNER JOIN studentcourse as sc ON sc.sId = '"+sid+"' AND cs.cId = sc.cId;";
        ResultSet rs = stm.executeQuery(q);

        return addCourseList(rs,courses);
    }

    public ArrayList<Course> getTeacherCourses(String tid) throws  SQLException{
        ArrayList<Course> courses = new ArrayList<>();

        Statement stm = conn.createStatement();
        String q = "SELECT * FROM courses WHERE pmTeacher = '"+tid+"' OR scTeacher = '"+tid+"';";
        ResultSet rs = stm.executeQuery(q);

        return addCourseList(rs,courses);
    }

    ArrayList<Course> addCourseList(ResultSet rs, ArrayList<Course> courses) throws SQLException {
        while(rs.next()){
            Course course = new Course();
            course.setCourseId(rs.getString("cId"));
            course.setCourseName(rs.getString("courseName"));
            course.setPmTeacher(rs.getString("pmTeacher"));
            course.setScTeacher(rs.getString("scTeacher"));
            course.setMaxLimit(rs.getInt("maxLimit"));
            course.setStudentCount(rs.getInt("studentCount"));

            courses.add(course);
        }

        return courses;
    }


    public void display(ArrayList<Course> courses) throws ValidationException {
        if(courses.size()==0){
            throw  new ValidationException("No Courses Available");
        }
        System.out.printf("%-10s %-20s %-20s %-20s %-20s\n", "S.NO","Course ID","Name","Student Limit","Student Count");

        for (int i = 0; i < courses.size(); i++) {
            System.out.printf("%-10s %-20s %-20s %-20s %-20s\n",
                                i+1,courses.get(i).getCourseId(),
                                courses.get(i).getCourseName(),
                                courses.get(i).getMaxLimit(),
                                courses.get(i).getStudentCount()
                                );
        }
    }

    public boolean enrollStudent(Course cs, Student st) throws SQLException, ValidationException {
        // check for student already enrolled.
        // add the student to student_course table
        // then update the student_count in courses table
        Statement stm = conn.createStatement();
        ResultSet rs;

        String checkq = "SELECT * FROM studentcourse WHERE cId = '"+cs.getCourseId()+"' AND sId = '"+st.getUserId()+"';";
        rs = stm.executeQuery(checkq);
        if(rs.next()){
            throw new ValidationException("Already Enrolled in this Course");
        }

        String insCourse = "INSERT INTO studentcourse (sId,cId,marks) VALUES ( '"+st.getUserId()+"' , '"+cs.getCourseId()+"' , 0 )";
        int res = stm.executeUpdate(insCourse);
        if(res==0) return false;

        res = stm.executeUpdate("UPDATE courses SET studentCount = '"+cs.getStudentCount()+"' WHERE cId = '"+cs.getCourseId()+"'");
        if(res==0) return  false;

        res = stm.executeUpdate("UPDATE studentcount SET courseCount = courseCount - 1 WHERE courseCount > 0 AND sId = '"+st.getUserId()+"';");
        return res == 1;
    }

    public boolean leaveCourse(Course cs, Student st) throws SQLException, ValidationException{
        // delete the row for cid,sid match
        Statement stm = conn.createStatement();

        if(stm.executeUpdate("DELETE FROM studentcourse WHERE sId='"+st.getUserId()+"' AND cId='"+cs.getCourseId()+"'") != 1)
            throw  new ValidationException("Could Not Delete student_course record");

        if(stm.executeUpdate("UPDATE courses SET studentCount="+cs.getStudentCount()+" WHERE cId='"+cs.getCourseId()+"';") != 1)
            throw  new ValidationException("Could Not Delete student_course record");

        return stm.executeUpdate("UPDATE studentcount SET courseCount = courseCount + 1 WHERE courseCount < 5 AND sId = '"+st.getUserId()+"';")==1;


    }

    public void checkCourseCount(Student st) throws SQLException, ValidationException {
        Statement stm = conn.createStatement();
        ResultSet rs;
        rs = stm.executeQuery("SELECT * FROM studentcount WHERE sId = '"+st.getUserId()+"' AND courseCount = 0");
        if(rs.next())
            throw new ValidationException("You can only enroll in 5 courses");
    }

    public void showStudentMarks(Student st) throws SQLException {
        Statement stm = conn.createStatement();
        String ssmq = "SELECT cs.cId,cs.courseName,sc.marks FROM courses as cs\n" +
                "INNER JOIN studentcourse as sc ON sc.sId = '"+st.getUserId()+"' AND cs.cId = sc.cId;";

        ResultSet rs = stm.executeQuery(ssmq);
        int i=0;

        System.out.printf("%-20s %-20s %-20s\n","Course ID","Name","Marks");
        while(rs.next()){
            i++;
            System.out.printf("%-20s %-20s %-20s\n",
                                rs.getString("cId"),
                                rs.getString("courseName"),
                                rs.getString("marks"));
        }

        if(i==0)
            System.out.println("You have not enrolled in any Course");
    }
}
