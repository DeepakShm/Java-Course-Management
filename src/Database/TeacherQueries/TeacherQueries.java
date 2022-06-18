package Database.TeacherQueries;

import CustomException.ValidationException;
import User.Student.Student;
import User.Teacher.Teacher;

import java.sql.*;
import java.util.ArrayList;

public class TeacherQueries {
    Connection conn;
    Statement stm = null;

    ResultSet rs = null;

    public TeacherQueries(Connection c) {
        conn = c;
    }

    public ArrayList<Teacher> getTeachers() throws SQLException {

        ArrayList<Teacher> teachers = new ArrayList<>();

        stm = conn.createStatement();
        String allTeachers = "SELECT * FROM users WHERE userRole = 'TEACHER';";

        rs = stm.executeQuery(allTeachers);
        while (rs.next()) {
            Teacher tch = new Teacher();
            tch.setUserId(rs.getString("userId"));
            tch.setUserName(rs.getString("userName"));
            tch.setEmail(rs.getString("email"));
            tch.setPassword(rs.getString("userPassword"));
            tch.setRole(2);
            teachers.add(tch);
        }

        return teachers;
    }

    public void display(ArrayList<Teacher> tchs) throws ValidationException {
        if(tchs.size()==0){
            throw  new ValidationException("No Teachers Available");
        }
        System.out.printf("%-10s %-10s %-10s\n", "S.NO","Name","Email");

        for (int i = 0; i < tchs.size(); i++) {
            System.out.printf("%-10s %-10s %-10s\n",i+1,tchs.get(i).getUserName(),tchs.get(i).getEmail());
        }
    }


    public boolean deleteTeacher(Teacher tc) throws SQLException{
        stm = conn.createStatement();
        String pmq = "UPDATE courses SET pmTeacher = null WHERE pmTeacher = '"+tc.getUserId()+"';";
        String scq = "UPDATE courses SET scTeacher = null WHERE scTeacher = '"+tc.getUserName()+"';";

        stm.executeUpdate(pmq);
        stm.executeUpdate(scq);

        return stm.executeUpdate("DELETE FROM users WHERE userId='"+tc.getUserName()+"';") == 1;

    }

    public ArrayList<Student> getStudents(String cid) throws SQLException{
        stm = conn.createStatement();
        String q = "SELECT sc.sId,sc.marks,us.email,us.userName FROM studentcourse as sc\n" +
                "INNER JOIN users as us ON us.userId = sc.sId\n" +
                "WHERE sc.cId = '"+cid+"';";

        ResultSet rs = stm.executeQuery(q);

        ArrayList<Student> students = new ArrayList<>();
        while (rs.next()){
            Student st = new Student();
            st.setUserId(rs.getString("sId"));
            st.setEmail(rs.getString("email"));
            st.setUserName(rs.getString("userName"));
            st.setMarks(rs.getFloat("marks"));

            students.add(st);
        }

        return students;
    }


    public void setMarks(ArrayList<Student> stds, String cid) throws SQLException{
        String q = "UPDATE studentcourse SET marks=? WHERE cId=? AND sId=?";
        PreparedStatement pstm = conn.prepareStatement(q);
        int res = -1;
        for (Student std : stds){
            pstm.setFloat(1,std.getMarks());
            pstm.setString(2,cid);
            pstm.setString(3,std.getUserId());
            res = pstm.executeUpdate();

            if(res!=1) {
                System.out.println("Could not Update Marks for Student : "
                        + std.getUserName()
                        + " [ " + std.getEmail() + " ]");
            }
        }
    }

}
