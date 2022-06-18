package User.Admin;

import CustomException.ValidationException;
import User.User;

import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class AdminModule{

    Admin admin;
    Scanner sc = new Scanner(System.in);

    public AdminModule(User u){
        admin = new Admin(u);
    }

    public void menu(){
        int choice = 0;

        do {
            try {
                System.out.println("Admin Menu : \n" +
                        "1. Add Course \n" +
                        "2. Update Course \n"+
                        "3. View the Highest and Lowest scorer in a Course \n" +
                        "4. Add Teacher \n" +
                        "5. Update Teacher \n" +
                        "6. Remove Teacher \n" +
                        "7. Logout \n");

                choice = sc.nextInt();
                sc.nextLine();

                switch (choice){
                    case 1 : admin.addCourse(); break;
                    case 2 : admin.selectCourse(); break;
                    case 3 : admin.showHighLow(); break;
                    case 4 : admin.createTeacher(); break;
                    case 5 : admin.updateTeacherInput(); break;
                    case 6 : admin.removeTeacher(); break;
                    case 7 :
                        System.out.println("Logging Out"); break;
                    default:
                        System.out.println("Wrong Choice");
                }
            }catch (SQLException | ValidationException ex){
                System.out.println("\n"+ex.getMessage()+"\n");
            }catch (InputMismatchException ime){
                System.out.println("Wrong Input Value \n ");
                sc.nextLine();
            }catch (Exception e){
                e.printStackTrace();
            }
        }while(choice!=7);
    }

}
