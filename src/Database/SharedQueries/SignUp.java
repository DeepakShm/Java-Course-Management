package Database.SharedQueries;

import CustomException.ValidationException;
import User.User;

import java.sql.*;

public class SignUp {
    Connection conn = null;
    Statement stm = null;
    ResultSet rs = null;

    public SignUp(Connection c){
        conn = c;
    }

    public boolean userSignUp(User user) throws SQLException, ValidationException {

        checkUserExists(user);

        String regt = "INSERT INTO users (userId,userName,email,userPassword,userRole) VALUES (?,?,?,?,?)";
        PreparedStatement tpstm = conn.prepareStatement(regt);
        tpstm.setString(1, user.getUserId());
        tpstm.setString(2, user.getUserName());
        tpstm.setString(3, user.getEmail());
        tpstm.setString(4, user.getPassword());
        tpstm.setString(5, user.getRoleName());

        int res = tpstm.executeUpdate();

        if(user.getRole()==3 && res==1){
            setStudentCourseCount(user.getUserId());
        }

        return  res == 1;

    }

    void checkUserExists(User user) throws SQLException, ValidationException {
        Statement stm = conn.createStatement();
        ResultSet rs = stm.executeQuery("SELECT userName FROM users WHERE email = '" + user.getEmail() + "'");
        if (rs.next()) {
            throw new ValidationException(user.getRoleName()+" Already Exists");
        }
    }

    public boolean updateUser(User user) throws SQLException, ValidationException{

        checkUserExists(user);

        String regt = "UPDATE users SET email = '"+
                user.getEmail() +"', userName = '"+user.getUserName()+"', userPassword = '"+
                user.getPassword()+"' WHERE userId = '"+user.getUserId()+"'";

        stm = conn.createStatement();
        return (stm.executeUpdate(regt)==1);
    }

    void setStudentCourseCount(String sid) throws SQLException {
        String scq = "INSERT INTO studentcount (sId,courseCount) VALUES ('"+sid+"',5)";
        Statement stm = conn.createStatement();
        stm.executeUpdate(scq);
    }
}
