package narasi.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Statement;
import javafx.util.Callback;
import java.sql.Timestamp;
import java.util.Date;




public class DBManager {

    private static String currentLoggedInUsername;
    private static User currentUser;

    public static String getCurrentLoggedInUsername() {
        return currentLoggedInUsername;
    }

    public static void setCurrentLoggedInUsername(String username) {
        currentLoggedInUsername = username;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User user) {
        currentUser = user;
    }
    


    public static boolean addUser(RegisteredUser user) {
        String query = "INSERT INTO users (username, password, fullName, email) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getFullName());
            statement.setString(4, user.getEmail());
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean workExists(int workId) {
        String checkSQL = "SELECT 1 FROM works WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(checkSQL)) {
    
            pstmt.setInt(1, workId);
    
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    

    public static User getUserByWorkId(int workId) {
        String selectSQL = "SELECT users.fullName, users.email FROM users " +
                           "INNER JOIN works ON users.id = works.user_id " +
                           "WHERE works.id = ?";
        User user = null;
    
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(selectSQL)) {
    
            pstmt.setInt(1, workId);
    
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String fullName = rs.getString("fullName");
                    String email = rs.getString("email");
                    user = new User(null, null, fullName, email) {
                        @Override
                        public void publishWork(Work work) {}
    
                        @Override
                        public void manageWork(Work work) {}
                    };
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public static void addCommentToWork(int workId, Comment comment) {
        String sql = "INSERT INTO comments(work_id, content, timestamp) VALUES(?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, workId);
            pstmt.setString(2, comment.getContent());
            pstmt.setTimestamp(3, new Timestamp(comment.getTimestamp().getTime()));
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static boolean addChapter(Chapter chapter) {
        String sql = "INSERT INTO chapters (work_id, number, title, content) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, chapter.getWorkId());
            pstmt.setInt(2, chapter.getNumber());
            pstmt.setString(3, chapter.getTitle());
            pstmt.setString(4, chapter.getContent());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public static List<Comment> getCommentsByWorkId(int workId) {
        String sql = "SELECT * FROM comments WHERE work_id = ?";
        List<Comment> comments = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, workId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                String content = rs.getString("content");
                Timestamp timestamp = rs.getTimestamp("timestamp");
                Comment comment = new Comment(content, new Date(timestamp.getTime()));
                comments.add(comment);
            }
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        return comments;
    }
    
    public static int getWorkIdByContent(String content) {
        String selectSQL = "SELECT id FROM works WHERE content = ?";
        int workId = -1;
    
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(selectSQL)) {
    
            pstmt.setString(1, content);
    
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    workId = rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return workId;
    }
    

    public static int getNextChapterNumber(int workId) {
        String query = "SELECT MAX(chapter_number) FROM chapters WHERE work_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, workId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int maxChapterNumber = resultSet.getInt(1);
                    return maxChapterNumber + 1;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Jika tidak ada bab sebelumnya, kembalikan 1
        return 1;
    }

    public static boolean addWork(Work work) {
        String sql = "INSERT INTO works (title, content, tags, isDraft, user_id) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, work.getTitle());
            statement.setString(2, work.getContent());
            statement.setString(3, work.getTags());
            statement.setBoolean(4, work.isDraft());

            if (work.getUserId() == 0) {
                System.out.println("User ID in Work object is 0. Retrieving userID from database...");
                RegisteredUser currentUser = (RegisteredUser) getCurrentUser();
                if (currentUser != null) {
                    work.setUserId(currentUser.getId());
                    System.out.println("User ID set to: " + currentUser.getId());
                } else {
                    System.out.println("Failed to retrieve current user ID from database.");
                    return false;
                }
            }
        
        
        statement.setInt(5, work.getUserId());
        statement.executeUpdate();
        
        try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                work.setId(generatedKeys.getInt(1));
            } else {
                System.out.println("Failed to retrieve generated key for new work.");
                return false;
            }
        }
        return true;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}

    public static RegisteredUser getCurrentRegisteredUser() {
        User user = getCurrentUser();
        if (user instanceof RegisteredUser) {
            return (RegisteredUser) user;
        } else {
            return null; 
        }
    }
    public static boolean updateUser(RegisteredUser user) {
        String query = "UPDATE users SET username = ?, password = ?, fullName = ?, email = ? WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getFullName());
            statement.setString(4, user.getEmail());
            statement.setInt(5, user.getId());
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
                RegisteredUser user = new RegisteredUser(); 
                user.setUsername(resultSet.getString("username"));
                user.setPassword(resultSet.getString("password"));
                user.setFullName(resultSet.getString("fullName"));
                user.setEmail(resultSet.getString("email"));
                user.setUserId(resultSet.getInt("id")); 
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public static List<Work> getAllPublishedWorks() {
        List<Work> works = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseConnection.getConnection();
            String sql = "SELECT works.id, works.title, works.content, works.tags, works.kudosCount, works.user_id, users.fullName, works.isDraft, works.timestamp " +
                         "FROM works " +
                         "INNER JOIN users ON works.user_id = users.id " +
                         "WHERE works.isDraft = 0 " +
                         "ORDER BY works.timestamp DESC";
            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                String content = resultSet.getString("content");
                String tags = resultSet.getString("tags");
                int kudosCount = resultSet.getInt("kudosCount");
                int userId = resultSet.getInt("user_id");
                String authorFullName = resultSet.getString("fullName");
                boolean isDraft = resultSet.getBoolean("isDraft");
                Timestamp timestamp = resultSet.getTimestamp("timestamp");

                Work work = new Work(id, title, content, tags, kudosCount, userId, authorFullName, isDraft, timestamp);
                works.add(work);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return works;
    }



    public static List<Work> searchWorksByTitle(String title) {
        List<Work> works = new ArrayList<>();
        String query = "SELECT * FROM works WHERE title LIKE ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, "%" + title + "%");
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Work work = new Work();
                    work.setId(resultSet.getInt("id"));
                    work.setTitle(resultSet.getString("title"));
                    work.setContent(resultSet.getString("content"));
                    work.setTags(resultSet.getString("tags"));
                    work.setKudosCount(resultSet.getInt("kudosCount"));
                    work.setUserId(resultSet.getInt("user_id"));
                    work.setDraft(resultSet.getBoolean("isDraft"));
                    work.setTimestamp(resultSet.getTimestamp("timestamp"));
                    works.add(work);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return works;
    }
    
    public static int getKudosCount(int workId) {
        String selectSQL = "SELECT kudosCount FROM works WHERE id = ?";
        int kudos = 0;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(selectSQL)) {

            pstmt.setInt(1, workId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    kudos = rs.getInt("kudosCount");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return kudos;
    }

    public static void updateKudosCount(int workId, int newKudosCount) {
        String updateSQL = "UPDATE works SET kudosCount = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {

            pstmt.setInt(1, newKudosCount);
            pstmt.setInt(2, workId);

            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Rows affected: " + rowsAffected); // Debug statement
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

   public static boolean publishWork(Work work) {
    String query = "INSERT INTO works (title, content, tags, isDraft, user_id) VALUES (?, ?, ?, ?, ?)";
    try (Connection connection = DatabaseConnection.getConnection();
         PreparedStatement statement = connection.prepareStatement(query)) {
        statement.setString(1, work.getTitle());
        statement.setString(2, work.getContent());
        statement.setString(3, work.getTags());
        statement.setBoolean(4, false); // Set isDraft ke false saat dipublikasikan
        statement.setInt(5, work.getUserId());
        int rowsInserted = statement.executeUpdate();
        return rowsInserted > 0;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}
   

public static boolean updateWork(Work work) {
    String query = "UPDATE works SET title = ?, content = ?, tags = ?, isDraft = ? WHERE id = ?";
    try (Connection connection = DatabaseConnection.getConnection();
         PreparedStatement statement = connection.prepareStatement(query)) {
        statement.setString(1, work.getTitle());
        statement.setString(2, work.getContent());
        statement.setString(3, work.getTags());
        statement.setBoolean(4, work.isDraft());
        statement.setInt(5, work.getId());
        int rowsUpdated = statement.executeUpdate();
        return rowsUpdated > 0;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}


    public static boolean saveChapterToDatabase(int workId, String chapterTitle, int chapterNumber) {
        String sql = "INSERT INTO chapters (work_id, chapter_number, title) VALUES (?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, workId);
            statement.setInt(2, chapterNumber);
            statement.setString(3, chapterTitle);
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void saveChapter(Chapter chapter) {
        String query = "UPDATE chapters SET title = ?, content = ? WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, chapter.getTitle());
            statement.setString(2, chapter.getContent());
            statement.setInt(3, chapter.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }    
    

    public static boolean deleteWork(int workId) {
        String query = "DELETE FROM works WHERE id = ?";
        Connection connection = null;
        try {
            connection = DatabaseConnection.getConnection();
            connection.setAutoCommit(false); // Nonaktifkan auto-commit untuk mengelola transaksi secara manual

            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, workId);
                System.out.println("Executing delete statement...");
                int rowsAffected = statement.executeUpdate();
                System.out.println("Rows affected: " + rowsAffected);
                if (rowsAffected > 0) {
                    connection.commit(); // Commit perubahan
                    System.out.println("Work with id " + workId + " deleted successfully.");
                    return true;
                } else {
                    System.out.println("No work found with id " + workId + ".");
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL error when deleting work with id " + workId + ": " + e.getMessage());
            if (connection != null) {
                try {
                    connection.rollback(); // Rollback jika terjadi kesalahan
                    System.out.println("Rolled back changes due to error.");
                } catch (SQLException rollbackEx) {
                    System.err.println("Error during rollback: " + rollbackEx.getMessage());
                }
            }
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true); // Aktifkan kembali auto-commit
                    connection.close();
                } catch (SQLException closeEx) {
                    System.err.println("Error when closing connection: " + closeEx.getMessage());
                }
            }
        }
        return false;
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
                    work.setTitle(resultSet.getString("title"));
                    work.setContent(resultSet.getString("content"));
                    work.setTags(resultSet.getString("tags"));
                
                    works.add(work);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return works;
    }

    public static boolean addChapter(int workId, int chapterNumber, String title, String content) {
        String sql = "INSERT INTO chapters (work_id, number, title, content) VALUES (?, ?, ?, ?)";
    
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, workId);
            pstmt.setInt(2, chapterNumber);
            pstmt.setString(3, title);
            pstmt.setString(4, content);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
    

    public static List<Chapter> getChaptersByWorkId(int workId) {
        List<Chapter> chapters = new ArrayList<>();
        String query = "SELECT * FROM chapters WHERE work_id = ? ORDER BY number ASC";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, workId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Chapter chapter = new Chapter(
                        resultSet.getInt("id"),
                        resultSet.getInt("work_id"),
                        resultSet.getInt("number"), 
                        resultSet.getString("title"),
                        resultSet.getString("content")
                    );
                    chapters.add(chapter);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return chapters;
    }
    
    public static Chapter getChapterById(int chapterId) {
        String query = "SELECT * FROM chapters WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, chapterId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Chapter(
                        resultSet.getInt("id"),
                        resultSet.getInt("work_id"),
                        resultSet.getInt("chapter_number"),
                        resultSet.getString("title"),
                        resultSet.getString("content")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void updateChapter(Chapter chapter) {
        String query = "UPDATE chapters SET title = ?, content = ? WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, chapter.getTitle());
            statement.setString(2, chapter.getContent());
            statement.setInt(3, chapter.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
    public static boolean deleteChapter(int chapterId) {
        String query = "DELETE FROM chapters WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, chapterId);
            int rowsDeleted = statement.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean registerUser(String username, String password, String fullName, String email) {
        String query = "INSERT INTO users (username, password, fullName, email) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            statement.setString(2, password);
            statement.setString(3, fullName);
            statement.setString(4, email);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<Work> getWorksByCurrentUser(int userId) {
        List<Work> works = new ArrayList<>();
        String query = "SELECT works.*, users.fullName " +
                       "FROM works " +
                       "INNER JOIN users ON works.user_id = users.id " +
                       "WHERE works.user_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Work work = new Work();
                    work.setId(resultSet.getInt("id"));
                    work.setTitle(resultSet.getString("title"));
                    work.setContent(resultSet.getString("content"));
                    work.setTags(resultSet.getString("tags"));
                    work.setKudosCount(resultSet.getInt("kudosCount"));
                    work.setUserId(resultSet.getInt("user_id"));
                    work.setDraft(resultSet.getBoolean("isDraft"));
                    work.setTimestamp(resultSet.getTimestamp("timestamp"));
                    work.setAuthorFullName(resultSet.getString("fullName")); 
                    works.add(work);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return works;
    }
    

    public static List<String> getWorkTitlesByCurrentUser(int userId) {
        List<String> workTitles = new ArrayList<>();
        String query = "SELECT title, isDraft FROM works WHERE user_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String title = resultSet.getString("title");
                    boolean isDraft = resultSet.getBoolean("isDraft");
                    if (isDraft) {
                        title += " (Draft)";
                    }
                    workTitles.add(title);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return workTitles;
    }
    
    
    public static List<Work> getDraftWorksByCurrentUser(int userId) {
        List<Work> works = new ArrayList<>();
        String query = "SELECT * FROM works WHERE user_id = ? AND is_draft = 1";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Work work = new Work();
                    work.setId(resultSet.getInt("id"));
                    work.setTitle(resultSet.getString("title"));
                    work.setContent(resultSet.getString("content"));
                    work.setTags(resultSet.getString("tags"));
                    work.setKudosCount(resultSet.getInt("kudosCount"));
                    work.setUserId(resultSet.getInt("user_id"));
                    work.setDraft(resultSet.getBoolean("is_draft"));
                    works.add(work);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return works;
    }
    
    
    public static RegisteredUser loginUser(String username, String password) {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            statement.setString(2, password);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String fullName = resultSet.getString("fullName");
                    String email = resultSet.getString("email");
                    return new RegisteredUser(username, password, fullName, email, id); // Gunakan konstruktor tambahan
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public class LoginController {
    public User login(String username, String password) {
        User loggedInUser = DBManager.loginUser(username, password);
        if (loggedInUser != null) {
            DBManager.setCurrentLoggedInUsername(username);
        }
        return loggedInUser;
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
                        resultSet.getString("email")
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
