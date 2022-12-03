package hashFunction.Connection;



import java.sql.DriverManager;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.exceptions.MySQLDataException;

public class mySqlConnection {
    Connection con = null;

    public Connection getConnection() throws SQLException {
        try {
            con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost/test_db", "root", "");
            System.out.println("Success to connect mySQL!");
            return con;

        } catch (MySQLDataException ex) {
            System.out.println("Failed to connect mySQL");
            System.out.println(ex);
        }

        return null;
    }
}
