package narasi.models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.nio.file.Paths;

public class DatabaseConnection {
    private static final String DATABASE_PATH = "src/main/resources/database.db"; 

    public static Connection getConnection() throws SQLException {
        try {
            String basePath = System.getProperty("user.dir");
            String absoluteDbPath = Paths.get(basePath, DATABASE_PATH).toString();
            String url = "jdbc:sqlite:" + absoluteDbPath;
            System.out.println("Connecting to database at " + url);
            return DriverManager.getConnection(url);
        } catch (Exception e) {
            throw new SQLException("Failed to load database file", e);
        }
    }
}
