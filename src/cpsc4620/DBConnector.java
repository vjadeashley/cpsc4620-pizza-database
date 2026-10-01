package cpsc4620;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnector {

    // Connection settings. Override with environment variables if needed.
    protected static String user = System.getenv().getOrDefault("PIZZA_DB_USER", "root");
    protected static String password = System.getenv().getOrDefault("PIZZA_DB_PASSWORD", "");
    private static String database_name = System.getenv().getOrDefault("PIZZA_DB_NAME", "PizzaDB");
    private static String url = "jdbc:mysql://localhost:3306";
    private static Connection conn;

    /**
     * Opens a connection to the MySQL database.
     *
     * @return the open connection, or null if the JDBC driver could not be loaded
     */
    public static Connection make_connection() throws SQLException, IOException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Could not load the driver");
            System.out.println("Message     : " + e.getMessage());
            return null;
        }

        conn = DriverManager.getConnection(url + "/" + database_name, user, password);
        return conn;
    }
}
