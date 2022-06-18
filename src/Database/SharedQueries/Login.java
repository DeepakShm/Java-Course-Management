package Database.SharedQueries;


import CustomException.ValidationException;
import User.User;
import Utilities.PasswordGen;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Login {

    Connection conn;
    Statement stm = null;
    ResultSet rs = null;

    public Login(Connection c){
        conn = c;
    }

    public boolean userAuth(User user) throws ValidationException, SQLException{
        return (PasswordGen.passwordDecoder(user.getPassword(),getDbPassword(user.getEmail(),user.getRoleName())));
    }

    String getDbPassword(String e,String r) throws SQLException, ValidationException {
        User user = getUserDetails(e,r);
        return user.getPassword();
    }


    public User getUserDetails(String e,String r) throws SQLException, ValidationException {
        String query = "SELECT * FROM users WHERE email = '"+e+"' AND userRole = '"+r+"'";
        stm = conn.createStatement();
        rs = stm.executeQuery(query);

        if(rs.next()){
            User u = new User();
            u.setUserName(rs.getString("userName"));
            u.setUserId(rs.getString("userId"));
            u.setEmail(rs.getString("email"));
            u.setPassword(rs.getString("userPassword"));
            u.setConn(conn);
            return u;
        }else{
            throw new ValidationException("This "+r+" Doesn't Exists. Register first.");
        }
    }

}
