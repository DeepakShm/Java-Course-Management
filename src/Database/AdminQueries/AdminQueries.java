package Database.AdminQueries;

import CustomException.ValidationException;
import Course.Course;

import java.sql.*;

public class AdminQueries {
    Connection conn;

    public AdminQueries(Connection c){
        conn = c;
    }

    public boolean addCourse(Course cs) throws SQLException,ValidationException {

        Statement stm = conn.createStatement();
        ResultSet rs = stm.executeQuery("SELECT cId FROM courses WHERE cId = '"+cs.getCourseId()+"' OR courseName = '"+cs.getCourseName()+"';");
        if(rs.next()){
            throw new ValidationException("Course Already Exists");
        }

        String q = "INSERT INTO courses (cId,courseName,pmTeacher,scTeacher,maxLimit) VALUES(?,?,?,?,?)";
        PreparedStatement pstm = conn.prepareStatement(q);
        pstm.setString(1,cs.getCourseId());
        pstm.setString(2,cs.getCourseName());
        pstm.setString(3,cs.getPmTeacher());
        pstm.setString(4,cs.getScTeacher());
        pstm.setInt(5,cs.getMaxLimit());

        return pstm.executeUpdate() == 1;

    }

    public boolean updateCourse(Course cs) throws SQLException {

        String q = "UPDATE courses SET courseName=?,pmTeacher=?,scTeacher=?,maxLimit=? WHERE cId=?";
        PreparedStatement pstm = conn.prepareStatement(q);
        pstm.setString(1,cs.getCourseName());
        pstm.setString(2,cs.getPmTeacher());
        pstm.setString(3,cs.getScTeacher());
        pstm.setInt(4,cs.getMaxLimit());
        pstm.setString(5,cs.getCourseId());

        return pstm.executeUpdate() == 1;
    }


}
