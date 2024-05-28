package narasi.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DBManager {

    public static boolean addUser(RegisteredUser user) {
        String query = "INSERT INTO users (username, password, fullName, email, anonymousId) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getFullName());
            statement.setString(4, user.getEmail());
            statement.setString(5, user.getAnonymousId());
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updateUser(RegisteredUser user) {
        String query = "UPDATE users SET username = ?, password = ?, fullName = ?, email = ?, anonymousId = ? WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getFullName());
            statement.setString(4, user.getEmail());
            statement.setString(5, user.getAnonymousId());
            statement.setInt(6, user.getId());
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public static List<RegisteredUser> getAllUsers() {
        List<RegisteredUser> users = new ArrayList<>();
        String query = "SELECT * FROM users";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                RegisteredUser user = new RegisteredUser(
                    resultSet.getString("username"),
                    resultSet.getString("password"),
                    resultSet.getString("fullName"),
                    resultSet.getString("email"),
                    resultSet.getString("anonymousId")
                );
                user.setId(resultSet.getInt("id"));
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

   public static List<Work> searchWorksByTitle(String title) {
        List<Work> works = new ArrayList<>();
        String query = "SELECT * FROM works WHERE title LIKE ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, "%" + title + "%");
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Work work = new Work(
                        resultSet.getString("title"),
                        resultSet.getString("content"),
                        resultSet.getString("tags")
                    );
                    work.setKudosCount(resultSet.getInt("kudosCount"));
                    // Populate comments if necessary
                    works.add(work);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return works;
    }

    public static boolean updateUser(User user) {
        String query = "UPDATE users SET username = ?, email = ?, fullname = ? WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getFullName());
            statement.setInt(4, user.getId());
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean insertWork(Work work) {
        String query = "INSERT INTO works (title, content, tags, kudosCount) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, work.getTitle());
            statement.setString(2, work.getContent());
            statement.setString(3, work.getTags());
            statement.setInt(4, work.getKudosCount());
            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updateWork(Work work) {
        String query = "UPDATE works SET content = ?, tags = ?, kudosCount = ? WHERE title = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, work.getContent());
            statement.setString(2, work.getTags());
            statement.setInt(3, work.getKudosCount());
            statement.setString(4, work.getTitle());
            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteWork(String title) {
        String query = "DELETE FROM works WHERE title = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, title);
            int rowsDeleted = statement.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static List<Work> searchWorksByTag(String tag) {
        List<Work> works = new ArrayList<>();
        String query = "SELECT * FROM works WHERE tags LIKE ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, "%" + tag + "%");
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Work work = new Work();
                    // Populate Work object with data from resultSet
                    work.setTitle(resultSet.getString("title"));
                    work.setContent(resultSet.getString("content"));
                    work.setTags(resultSet.getString("tags"));
                    // Add Work object to the list
                    works.add(work);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return works;
    }

    public static boolean registerUser(String username, String password, String fullName, String email, String anonymousId) {
        String query = "INSERT INTO users (username, password, fullName, email, anonymousId) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            statement.setString(2, password);
            statement.setString(3, fullName);
            statement.setString(4, email);
            statement.setString(5, anonymousId);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean loginUser(String username, String password) {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            statement.setString(2, password);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next(); // Mengembalikan true jika pengguna ditemukan
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public static RegisteredUser getUserByUsername(String username) {
        String query = "SELECT * FROM users WHERE username = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    RegisteredUser user = new RegisteredUser(
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getString("fullName"),
                        resultSet.getString("email"),
                        resultSet.getString("anonymousId")
                    );
                    user.setId(resultSet.getInt("id"));
                    return user;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
