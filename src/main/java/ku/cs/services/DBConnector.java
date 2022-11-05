package ku.cs.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnector {

    public static Connection getConnection() throws SQLException {

        String url = "jdbc:mysql://localhost:3306/dormitory";
        String user = "root";
        String pass = "";

        Connection connection = DriverManager.getConnection(url,user,pass);

        return connection;

    }

}
