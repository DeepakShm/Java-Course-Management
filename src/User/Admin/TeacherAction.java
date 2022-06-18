package User.Admin;

import CustomException.ValidationException;
import Database.TeacherQueries.TeacherQueries;
import User.Teacher.Teacher;
import Utilities.PasswordGen;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

public class TeacherAction {

    TeacherQueries teachq;
    Connection conn;

    Scanner sc = new Scanner(System.in);

    public TeacherAction(Connection c){
        teachq = new TeacherQueries(c);
        conn = c;
    }

    void createTeacher() throws SQLException, ValidationException {
        Teacher tc = new Teacher();

        System.out.print("Enter Teacher Name : ");
        tc.setUserName(sc.nextLine());

        System.out.print("Enter Teacher Email : ");
        tc.setEmail(sc.next());

        UUID uuid = UUID.randomUUID();
        tc.setUserId(uuid.toString());

        System.out.print("Enter the password : ");
        tc.setPassword(PasswordGen.passwordEncoder(sc.next()));

        tc.setRole(2);
        tc.setConn(conn);
        tc.registerUser();

    }

    void updateTeacherInput() throws SQLException, ValidationException, InputMismatchException {
        ArrayList<Teacher> tchs = teachq.getTeachers();
        teachq.display(tchs);
        int ch;
        System.out.println("Select a teacher to update");
        ch = sc.nextInt();
        sc.nextLine();

        if(ch>=1 && ch<=tchs.size()){
            Teacher teacher = tchs.get(ch-1);
            String yn;

            System.out.print("Want to update email (Y/N) : ");
            yn = sc.next().toLowerCase(Locale.ROOT);
            if(yn.equals("y")){
                System.out.print("Enter new Email : ");
                teacher.setEmail(sc.next());
            }

            System.out.print("Want to update name (Y/N) : ");
            yn = sc.next().toLowerCase(Locale.ROOT);
            if(yn.equals("y")){
                System.out.print("Enter new Teacher Name : ");
                teacher.setUserName(sc.nextLine());
            }

            System.out.print("Want to update Password (Y/N) : ");
            yn = sc.next().toLowerCase(Locale.ROOT);
            if(yn.equals("y")){
                System.out.print("Enter new Password : ");
                teacher.setPassword(sc.next());
            }

            teacher.setConn(conn);
            teacher.updateUser();

        }else{
            System.out.println("Wrong Choice. Try Again");
        }

    }

    void removeTeacher() throws SQLException, ValidationException,InputMismatchException {
        ArrayList<Teacher> tchs = teachq.getTeachers();
        teachq.display(tchs);
        int ch;
        System.out.println("Select a teacher to remove");
        ch = sc.nextInt();
        sc.nextLine();

        if (ch >= 1 && ch <= tchs.size()) {
            if (teachq.deleteTeacher(tchs.get(ch - 1))) System.out.println("Teacher is Removed");
            else System.out.println("Teacher is not Removed");
        } else {
            System.out.println("Wrong Choice. Try Again");
        }
    }
}
