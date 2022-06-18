package User.Admin;

import CustomException.ValidationException;
import User.User;

import java.sql.SQLException;

public class Admin extends User {

    TeacherAction ta;
    CourseAction ca;

    public Admin(){
        super();
    }

    public Admin(User u){
        super(u);
        ta = new TeacherAction(u.getConn());
        ca = new CourseAction(u.getConn());
    }

    void addCourse() throws SQLException, ValidationException {
        ca.addCourse();
    }

    void selectCourse() throws SQLException, ValidationException{
        ca.selectCourse();
    }

    void showHighLow() throws SQLException, ValidationException{
        ca.showHighLow();
    }

    void createTeacher() throws SQLException, ValidationException{
        ta.createTeacher();
    }

    void updateTeacherInput() throws SQLException, ValidationException{
        ta.updateTeacherInput();
    }

    void removeTeacher() throws SQLException, ValidationException{
        ta.removeTeacher();
    }

}
