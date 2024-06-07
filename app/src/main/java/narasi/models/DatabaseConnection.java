package narasi.models;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    public static Connection getConnection() throws SQLException {
        try {
            InputStream inputStream = DatabaseConnection.class.getClassLoader().getResourceAsStream("database.db");
            if (inputStream == null) {
                throw new SQLException("Database file not found in resources");
            }
            Path tempFile = Files.createTempFile("database", ".db");
            Files.copy(inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING);
            String url = "jdbc:sqlite:" + tempFile.toString();
            System.setProperty("sqlite.pragma.trace", "true");
            return DriverManager.getConnection(url);
        } catch (Exception e) {
            throw new SQLException("Failed to load database file", e);
        }
    }
}
