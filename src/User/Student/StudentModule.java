package User.Student;

import CustomException.ValidationException;
import User.User;

import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class StudentModule {

    Student student;
    Scanner sc = new Scanner(System.in);

    public StudentModule(User u){
        student = new Student(u);
    }

    public void menu(){
        int choice = 0;

        do {
            try {
                System.out.println("Student Menu : \n" +
                        "1. Enroll into a Course\n" +
                        "2. Leave a Course \n" +
                        "3. View Marks in a enrolled course \n" +
                        "4. Logout\n");

                choice = sc.nextInt();
                sc.nextLine();

                switch (choice){
                    case 1 : student.enrollCourse(); break;
                    case 2 : student.leaveCourse(); break;
                    case 3 : student.showStudentMarks(); break;
                    case 4 :
                        System.out.println("Logging Out"); break;
                    default:
                        System.out.println("Wrong Choice");
                }
            }
            catch (SQLException | ValidationException ex){
                System.out.println("\n"+ex.getMessage()+"\n");
            }catch (InputMismatchException ime){
                System.out.println("Wrong Input Value \n ");
                sc.nextLine();
            }catch (Exception e){
                e.printStackTrace();
            }
        }while(choice!=4);
    }
}
