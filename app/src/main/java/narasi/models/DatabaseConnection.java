package narasi.models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:sqlite:C:\\Users\\LENOVO\\Documents\\PBO semester 2\\Projek pbo after seminar\\Narasi\\app\\src\\main\\java\\narasi\\db\\database.db";

    public static Connection getConnection() throws SQLException {
        System.setProperty("sqlite.pragma.trace", "true");
        return DriverManager.getConnection(URL);
    }
}
