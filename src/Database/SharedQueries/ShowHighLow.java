package Database.SharedQueries;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ShowHighLow {
    Connection conn;

    public ShowHighLow(Connection c){
        conn = c;
    }

    public void showHighestScorer(String cid) throws SQLException {
        Statement stm = conn.createStatement();
        ResultSet rs = stm.executeQuery("SELECT  sc.cId,us.email,us.userName,st.top FROM studentcourse as sc\n" +
                "JOIN (SELECT MAX(marks)as top,cId  FROM studentcourse WHERE cId = '"+cid+"') as st ON st.top = sc.marks AND st.cId = sc.cId\n" +
                "JOIN users as us ON us.userId = sc.sId;");

        int i=0;

        System.out.println("Highest Scorer Students : ");
        System.out.printf("%-20s %-20s %-20s %-20s\n","Student Name","Student Email","Marks","Course ID");
        while(rs.next()){
            i++;
            System.out.printf("%-20s %-20s %-20s %-20s\n",
                    rs.getString("userName"),
                    rs.getString("email"),
                    rs.getString("top"),
                    rs.getString("cId"));
        }

        if(i==0)
            System.out.println("No Student Enrolled in this Course");

    }

    public void showLowestScorer(String cid) throws SQLException{
        Statement stm = conn.createStatement();
        ResultSet rs = stm.executeQuery("SELECT  sc.cId,us.email,us.userName,st.bottom FROM studentcourse as sc\n" +
                "JOIN (SELECT MIN(marks)as bottom,cId  FROM studentcourse WHERE cId = '"+cid+"') as st ON st.bottom = sc.marks AND st.cId = sc.cId\n" +
                "JOIN users as us ON us.userId = sc.sId;");

        int i=0;

        System.out.println("Lowest Scorer Students : ");
        System.out.printf("%-20s %-20s %-20s %-20s\n","Student Name","Student Email","Marks","Course ID");
        while(rs.next()){
            i++;
            System.out.printf("%-20s %-20s %-20s %-20s\n",
                    rs.getString("userName"),
                    rs.getString("email"),
                    rs.getString("bottom"),
                    rs.getString("cId"));
        }

        if(i==0)
            System.out.println("No Course Available");

    }
}
