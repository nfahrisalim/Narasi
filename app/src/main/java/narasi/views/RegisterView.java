package narasi.views;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import narasi.models.DBManager;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterView {

    private final Stage stage;
    private final LoginView loginView;

    public RegisterView(Stage stage, LoginView loginView) {
        this.stage = stage;
        this.loginView = loginView;
    }
    public class RegisterValidator {

        public static boolean isValidUsername(String username) {
            // Username harus terdiri dari huruf, angka, dengan panjang 4-16 karakter
            String regex = "^[a-zA-Z0-9]{4,16}$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(username);
            return matcher.matches();
        }
    
        public static boolean isValidPassword(String password) {
            // Password harus memiliki panjang minimal 8 karakter, minimal satu huruf besar, satu huruf kecil, dan satu angka
            String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(password);
            return matcher.matches();
        }
    
        public static boolean isValidEmail(String email) {
            // Validasi sederhana untuk alamat email
            String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(email);
            return matcher.matches();
        }
    }

    public void showRegister() {
        stage.setTitle("Register");
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setVgap(10);
        grid.setHgap(10);

        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();
        Label passwordLabel = new Label("Password:");
        TextField passwordField = new TextField();
        Label fullNameLabel = new Label("Full Name:");
        TextField fullNameField = new TextField();
        Label emailLabel = new Label("Email:");
        TextField emailField = new TextField();

        Button registerButton = new Button("Register");

        grid.add(usernameLabel, 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(passwordLabel, 0, 1);
        grid.add(passwordField, 1, 1);
        grid.add(fullNameLabel, 0, 2);
        grid.add(fullNameField, 1, 2);
        grid.add(emailLabel, 0, 3);
        grid.add(emailField, 1, 3);
        grid.add(registerButton, 0, 5, 2, 1);

        registerButton.setOnAction(event -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            String fullName = fullNameField.getText();
            String email = emailField.getText();

            if (!RegisterValidator.isValidUsername(username)) {
                showAlert(Alert.AlertType.ERROR, "Invalid Username", "Username tidak valid. Username harus terdiri dari huruf, angka, dengan panjang 4-16 karakter.");
            } else if (!RegisterValidator.isValidPassword(password)) {
                showAlert(Alert.AlertType.ERROR, "Invalid Password", "Password tidak valid. Password harus memiliki panjang minimal 8 karakter, minimal satu huruf besar, satu huruf kecil, dan satu angka.");
            } else if (!RegisterValidator.isValidEmail(email)) {
                showAlert(Alert.AlertType.ERROR, "Invalid Email", "Email tidak valid.");
            } else {
                boolean success = DBManager.registerUser(username, password, fullName, email);
                if (success) {
                    showAlert(Alert.AlertType.INFORMATION, "Registration Successful", "Registrasi berhasil!");
                    stage.close();
                    loginView.showLogin();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Registration Failed", "Registrasi gagal. Silakan coba lagi.");
                }
            }
        });

        Scene scene = new Scene(grid, 1000, 600);
        scene.getStylesheets().add(getClass().getResource("/RegisterStyle.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
