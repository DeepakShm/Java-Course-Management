package User.Teacher;

import CustomException.ValidationException;
import User.User;

import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class TeacherModule {

    Teacher teacher;
    Scanner sc = new Scanner(System.in);

    public TeacherModule(User u){
        teacher = new Teacher(u);
    }

    public void menu(){
        int choice = 0;

        do {
            try {
                System.out.println("Teacher Menu : \n" +
                        "1. Enter marks for all the students in a course\n" +
                        "2. View the Highest and Lowest scorer in a Course \n" +
                        "3. Logout \n");

                choice = sc.nextInt();
                sc.nextLine();

                switch (choice){
                    case 1 : teacher.enterMarks(); break;
                    case 2 : teacher.showHighLow(); break;
                    case 3 :
                        System.out.println("Logging Out"); break;
                    default:
                        System.out.println("Wrong Choice");
                }
            } catch (SQLException | ValidationException ex){
                System.out.println("\n"+ex.getMessage()+"\n");
            }catch (InputMismatchException ime){
                System.out.println("Wrong Input Value \n ");
                sc.nextLine();
            }catch (Exception e){
                e.printStackTrace();
            }
        }while(choice!=3);
    }
}
