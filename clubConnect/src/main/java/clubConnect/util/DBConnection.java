package clubConnect.util;
import java.sql.*;


public final class DBConnection {
    private static final String URL =
            System.getProperty("TEST_DB_URL",
                    "jdbc:mysql://localhost:3306/clubConnect");
    private static final String USER = "root";
    private static final String PASS = "root@123";


    private static Connection instance;          // <— Singleton

    private DBConnection() {}                    // hide ctor

    public static synchronized Connection getConnection() throws SQLException {
        if (instance==null || instance.isClosed())
            instance = DriverManager.getConnection(URL,USER,PASS);
        return instance;
    }
}
