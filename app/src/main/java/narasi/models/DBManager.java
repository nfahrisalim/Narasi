package narasi.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DBManager {

    // Contoh method untuk melakukan query ke database
    public static List<Work> searchWorksByTitle(String title) {
        List<Work> works = new ArrayList<>();
        String query = "SELECT * FROM works WHERE title LIKE ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, "%" + title + "%");
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Work work = new Work();
                    // Populate Work object with data from resultSet
                    works.add(work);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return works;
    }

    public static boolean updateUser(User user) {
        String query = "UPDATE users SET username = ?, email = ? fullname id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getFullName());
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void connect() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'connect'");
    }

    // Metode lain untuk operasi database lainnya
}
