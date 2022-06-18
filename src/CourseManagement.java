import CustomException.ValidationException;
import Database.DBConnection;
import User.User;
import Utilities.PasswordGen;

import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.UUID;

public class CourseManagement {

    DBConnection dbconn;
    Scanner sc = new Scanner(System.in);

    boolean appStart;

    CourseManagement(){
        dbconn = new DBConnection();
        appStart = true;
    }

    void  mainMenu() throws NullPointerException, ValidationException , SQLException, InputMismatchException {
        System.out.println("Main Menu : \n" +
                "1. Login \n" +
                "2. Register as Admin \n"+
                "3. Register as Student \n" +
                "4. Exit \n");

        int choice = sc.nextInt();
        sc.nextLine();
        switch (choice){
            case 1 : loginUser();
                break;
            case 2 : registerUser(1);
                break;
            case 3 : registerUser(3);
                break;
            case 4 : appStart = false;
                break;
            default :
                System.out.println("Wrong Choice. Try Again.");
        }


    }


    void loginUser() throws ValidationException, SQLException, InputMismatchException {
        User user = new User();
        System.out.print("Enter Email : ");
        user.setEmail(sc.next());

        System.out.print("Enter Password : ");
        user.setPassword(sc.next());

        System.out.print("Login as : \n" +
                    "1. ADMIN \n" +
                    "2. TEACHER \n" +
                    "3. STUDENT \n");
        user.setRole(sc.nextInt());
        sc.nextLine();
        user.setConn(dbconn.getConn());
        user.loginUser();
    }

    void registerUser(int role) throws ValidationException, SQLException{
        User user = new User();
        System.out.print("Enter Name : ");
        user.setUserName(sc.nextLine());

        System.out.print("Enter Email : ");
        user.setEmail(sc.next());

        System.out.print("Enter Password : ");
        user.setPassword(PasswordGen.passwordEncoder(sc.next()));

        UUID uuid = UUID.randomUUID();
        user.setUserId(uuid.toString());
        user.setRole(role);
        user.setConn(dbconn.getConn());
        user.registerUser();
        System.out.println("\nYour ID is : "+user.getUserId());
    }



    public static void main(String[] args) {
        CourseManagement cm = new CourseManagement();
        try {
            cm.dbconn.startConnection();

            do{
                try {
                    cm.mainMenu();
                }catch (NullPointerException | ValidationException | SQLException e){
                    System.out.println(e.getMessage());
                }catch (InputMismatchException ime){
                    System.out.println("Wrong Input Value \n");
                    cm.sc.nextLine();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }while (cm.appStart);

            cm.dbconn.closeConnection();

        }catch (ClassNotFoundException | SQLException e){
            System.out.println(e.getMessage());
        }catch (InputMismatchException ime){
            System.out.println("Wrong Input Value \n ");
            cm.sc.nextLine();
        }catch (Exception e){
            System.out.println(e.toString());
        }
    }
}