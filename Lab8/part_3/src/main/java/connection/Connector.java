package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public final class Connector {
    private static final String DB_URL = System.getenv("DB_URL");
    private static final String DB_USER = System.getenv("DB_USER");
    private static final String DB_PASSWORD = System.getenv("DB_PASSWORD");
    private static final String JDBC_DRIVER = System.getenv("JDBC_DRIVER");
    private static Connection connection;

    private Connector() {}

    public static Connection getConnection() {

        try {
            if (connection == null || connection.isClosed()) {
                Class.forName(JDBC_DRIVER);
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            }

            return connection;
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}