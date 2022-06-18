package User;

import CustomException.ValidationException;
import Database.SharedQueries.Login;
import Database.SharedQueries.SignUp;
import User.Admin.AdminModule;
import User.Student.StudentModule;
import User.Teacher.TeacherModule;

import java.sql.Connection;
import java.sql.SQLException;

public class User {

    String userId;
    String userName;
    String email;
    String password;
    int role;
    final int passwordLimit = 6;
    final String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." + "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z" + "A-Z]{2,7}$";
    final String[] roles = new String[]{"ADMIN", "TEACHER", "STUDENT"};

    Connection conn = null;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    public String getRoleName(){
        return roles[role-1];
    }

    public User(){
        this.setUserName(null);
        this.setUserId(null);
        this.setEmail(null);
        this.setPassword(null);
        this.setConn(null);
        this.setRole(0);
    }

    public  User(User u){
        this.setUserName(u.getUserName());
        this.setUserId(u.getUserId());
        this.setEmail(u.getEmail());
        this.setPassword(u.getPassword());
        this.setConn(u.getConn());
        this.setRole(u.getRole());
    }


    public boolean validateUserName() throws ValidationException {
        if(userName.matches("^[a-zA-Z ]*$") && userName.length() >= passwordLimit)
            return true;

        throw new ValidationException("Invalid Username. It's too short ( Minimum 6 characters ).");

    }


    public boolean validateEmail() throws NullPointerException, ValidationException {
        if(email.matches(emailRegex)){
            return true;
        }
        else{ throw new ValidationException("Invalid Email");}
    }

    public boolean checkPasswordLen() throws ValidationException {
        if(password.length() < passwordLimit)
            throw new ValidationException("Invalid Password. It's too short ( Minimum 6 characters ).");
        return true;
    }

    public boolean checkRole() throws ValidationException{
        if(role>3 || role<1) throw new ValidationException("Invalid Role Selected");
        return true;
    }

    public boolean validateUser(){
        try{
            return validateEmail() && checkPasswordLen() && checkRole();
        }catch (NullPointerException | ValidationException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public void loginUser() throws ValidationException, SQLException {

        if(!validateUser()){
            throw new ValidationException("Invalid "+getRoleName()+" Details. Try Again.");
        }

        Login login = new Login(getConn());

        if(login.userAuth(this)){
            System.out.println("Logged In as "+getRoleName() +" : "+getEmail());
            navigateUser(login.getUserDetails(this.email,this.getRoleName()));
        }else {
            System.out.println("Password is incorrect");
        }

    }

    public void registerUser() throws ValidationException, SQLException {
        if(!validateUser() || !validateUserName()){
            throw  new ValidationException("Invalid "+getRoleName()+" Details. Try Again.");
        }

        SignUp signup = new SignUp(getConn());
        if(signup.userSignUp(this)){
            System.out.println("New "+getRoleName()+" Added");
        }else {
            System.out.println("Registration Failed");
        }
    }

    public void updateUser()throws ValidationException, SQLException {
        if(!validateUser() || !validateUserName()){
            throw  new ValidationException("Invalid "+getRoleName()+" Details. Try Again.");
        }

        SignUp signup = new SignUp(getConn());
        if(signup.updateUser(this)){
            System.out.println(getRoleName()+" Updated");
        }else {
            System.out.println("Could not update "+getRoleName());
        }
    }

    void navigateUser(User u){
        switch (role){
            case 1 :
                    AdminModule adminModule = new AdminModule(u);
                    adminModule.menu();
                    break;
            case 2 :
                    TeacherModule teacherModule = new TeacherModule(u);
                    teacherModule.menu();
                    System.out.println("Teacher Logged In");// TEACHER
                    break;
            case 3 :
                    StudentModule studentModule = new StudentModule(u);
                    studentModule.menu();
                    break;
            default :
                System.out.println("Wrong Choice");
        }
    }

}
