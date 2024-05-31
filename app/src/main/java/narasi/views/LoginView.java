package narasi.views;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import narasi.models.DBManager;
import narasi.models.User;

public class LoginView {

    private final Stage stage;
    private final MainView mainView;

    public LoginView(Stage stage, MainView mainView) {
        this.stage = stage;
        this.mainView = mainView;
    }

    public void showLogin() {
        stage.setTitle("Login");
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setVgap(10);
        grid.setHgap(10);

        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();
        Label passwordLabel = new Label("Password:");
        TextField passwordField = new TextField();

        Button loginButton = new Button("Login");
        Button registerButton = new Button("Register");

        grid.add(usernameLabel, 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(passwordLabel, 0, 1);
        grid.add(passwordField, 1, 1);
        grid.add(loginButton, 0, 2);
        grid.add(registerButton, 1, 2);
        loginButton.setOnAction(event -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
        
            User user = DBManager.loginUser(username, password);
            if (user != null) {
                System.out.println("Login berhasil!");
                mainView.setLoggedIn(true, user);
                stage.close();
                // Setel pengguna saat ini di DBManager
                DBManager.setCurrentUser(user);
            } else {
                System.out.println("Login gagal. Silakan coba lagi.");
            }
        });
        registerButton.setOnAction(event -> {
            RegisterView registrasiView = new RegisterView(new Stage(), this);
            registrasiView.showRegister();
        });

        Scene scene = new Scene(grid, 300, 200);
        stage.setScene(scene);
        stage.show();
    }
}
