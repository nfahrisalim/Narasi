package narasi.views;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import narasi.models.RegisteredUser;

public class RegisterView {

    private final Stage stage;
    private final LoginView loginView;

    public RegisterView(Stage stage, LoginView loginView) {
        this.stage = stage;
        this.loginView = loginView;
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
        Label anonymousIdLabel = new Label("Anonymous ID:");
        TextField anonymousIdField = new TextField(); 
 
        Button registerButton = new Button("Register");

        grid.add(usernameLabel, 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(passwordLabel, 0, 1);
        grid.add(passwordField, 1, 1);
        grid.add(fullNameLabel, 0, 2);
        grid.add(fullNameField, 1, 2);
        grid.add(emailLabel, 0, 3);
        grid.add(emailField, 1, 3);
        grid.add(anonymousIdLabel, 0, 4);
        grid.add(anonymousIdField, 1, 4);
        grid.add(registerButton, 0, 5, 2, 1);

        registerButton.setOnAction(event -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            String fullName = fullNameField.getText();
            String email = emailField.getText();
            String anonymousId = anonymousIdField.getText();

            RegisteredUser newUser = new RegisteredUser(username, password, fullName, email, anonymousId);

            stage.close();

            loginView.showLogin();
        });

        Scene scene = new Scene(grid, 300, 250);
        stage.setScene(scene);
        stage.show();
    }
}
