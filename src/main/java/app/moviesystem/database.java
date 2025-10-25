package app.moviesystem;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;

public class database {
    public static Connection connectDb(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection connect = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/movieticketbookingsystem","root","root");

            return connect;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
