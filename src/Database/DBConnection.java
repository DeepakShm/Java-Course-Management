package Database;

import java.sql.*;
import java.util.Scanner;


import Utilities.PasswordGen;

public class DBConnection {

    static  final String  JDBC_DRIVE = "com.mysql.cj.jdbc.Driver";
    static final  String DB_URL = "jdbc:mysql://localhost:3306";

    static String DB_USER = "root";
//    static final String DB_NAME = "courseManagementDeepak";
    static final String DB_NAME = "sample";

    static String DB_PASSWORD = "sydniv@73";


    Connection conn = null;
    Statement stm = null;
    ResultSet rs = null;

    DatabaseMetaData dbMeta = null;

    public DBConnection(){
    }

    public Connection getConn() {
        return conn;
    }

    boolean dbExistence() throws SQLException{

        rs = dbMeta.getCatalogs();

        while (rs.next()){
            if(rs.getString(1).equals(DB_NAME)){
                // connection established and found the required database
                System.out.println("Database Already Present");
                return true;
            }
        }
        return false;
    }

    void dbCreation() throws SQLException{

        String[] queries = new String[6];
        queries[0] = "CREATE DATABASE "+DB_NAME;
        queries[1] = "USE "+DB_NAME;
        queries[2] = "CREATE TABLE users(\n" +
                "\tuserId varchar(36) NOT NULL UNIQUE,\n" +
                "    email varchar(36) NOT NULL UNIQUE,\n" +
                "    userName varchar(255) NOT NULL,\n" +
                "    userPassword varchar(255) NOT NULL,\n" +
                "    userRole varchar(10) NOT NULL,\n" +
                "    constraint PK_U_ID PRIMARY KEY(userId)\n" +
                ");";


        queries[3] = "CREATE TABLE studentcount(\n" +
                "\tsId varchar(36) NOT NULL UNIQUE,\n" +
                "    courseCount INT DEFAULT 5,\n" +
                "    constraint PK_S_ID PRIMARY KEY(sId),\n" +
                "\tconstraint FK_sid FOREIGN KEY (sId) REFERENCES users(userId)\n" +
                ");";

        queries[4] = "CREATE TABLE courses(\n" +
                "\tcId varchar(36) NOT NULL UNIQUE,\n" +
                "    courseName varchar(255) NOT NULL UNIQUE,\n" +
                "    pmTeacher varchar(36) NULL,\n" +
                "    scTeacher varchar(36) NULL,\n" +
                "    maxLimit INT DEFAULT 0,\n" +
                "    studentCount INT DEFAULT 0,\n" +
                "    constraint PK_C_ID PRIMARY KEY(cId),\n" +
                "    constraint FK_pmid FOREIGN KEY (pmTeacher) REFERENCES users(userId),\n" +
                "\tconstraint FK_scid FOREIGN KEY (scTeacher) REFERENCES users(userId)\n" +
                ");";

        queries[5] = "CREATE TABLE studentcourse(\n" +
                "\tcId varchar(36) NOT NULL,\n" +
                "\tsId varchar(36) NOT NULL,\n" +
                "    marks FLOAT(5) DEFAULT 0,\n" +
                "    constraint PK_SC_ID PRIMARY KEY(sId,cId),\n" +
                "    constraint FK_student_id FOREIGN KEY (sId) REFERENCES users(userId),\n" +
                "\tconstraint FK_course_id FOREIGN KEY (cId) REFERENCES courses(cId)\n" +
                ");";


        for(int i=0;i<6;i++){
           stm.execute(queries[i]);
        }

    }

    void dbConnect() throws ClassNotFoundException, SQLException {

        Class.forName(JDBC_DRIVE);

        Scanner sc = new Scanner(System.in);

        System.out.println("Enter DB User : ");
        DB_USER = sc.next();

        System.out.println("Enter DB Password : ");
        DB_PASSWORD = sc.next();

        conn = DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWORD);

        stm = conn.createStatement();

        System.out.println("Connection established and Checking for Database existence");

        dbMeta = conn.getMetaData();

        if(!dbExistence()) {
            // call create DB and Tables.
            System.out.println("Required Database doesn't exists");
            System.out.println("Creating DB and Tables");
            dbCreation();
            System.out.println("Database and tables created");

        }
        stm.execute("USE "+DB_NAME);
    }

    public void startConnection() throws SQLException, ClassNotFoundException {
        dbConnect();
    }

    public void closeConnection(){
        try {
            conn.close();
            System.out.println("Connection Closed.");
        }catch (SQLException se){
            System.out.println(se.getMessage());
        }
    }
}
