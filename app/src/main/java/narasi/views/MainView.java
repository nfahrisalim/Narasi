package narasi.views;

import java.util.List;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import narasi.controllers.MainController;
import narasi.models.DBManager;
import narasi.models.RegisteredUser;
import narasi.models.User;
import narasi.models.Work;

public class MainView extends Application {

    private ListView<Work> workListView;
    private boolean isLoggedIn = false;
    private Button loginButton;
    private User loggedInUser;
    private Button accountButton;
    private HBox topBar;
    private User currentUser;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Narasi - Platform Karya Tulis Mahasiswa");

        BorderPane root = new BorderPane();

        topBar = new HBox();
        topBar.setPadding(new Insets(20));
        topBar.setSpacing(15);
        topBar.setAlignment(Pos.TOP_RIGHT);

        loginButton = new Button("Login");
        loginButton.setOnAction(event -> {
            LoginView loginView = new LoginView(new Stage(), this);
            loginView.showLogin();
        });

        accountButton = new Button("Account");
        accountButton.setOnAction(event -> {
            if (isLoggedIn) {
                AccountManage accountManage = new AccountManage(new Stage(), loggedInUser);
                accountManage.showManage();
            } else {
                System.out.println("Please log in to access your account.");
            }
        });

        TextField searchField = new TextField();
        searchField.setPromptText("Search...");

        Button searchButton = new Button("Search");
        searchButton.setOnAction(event -> {
            String query = searchField.getText();
            List<Work> searchResults = DBManager.searchWorksByTitle(query);
            workListView.getItems().setAll(searchResults);
        });

        topBar.getChildren().addAll(searchField, searchButton, loginButton);

        VBox sidebar = new VBox();
        sidebar.setPadding(new Insets(20));
        sidebar.setSpacing(20);
        sidebar.setPrefWidth(200);

        Button jenisKaryaButton = new Button("Jenis Karya");
        jenisKaryaButton.setMinWidth(120);
        VBox jenisKaryaSubButtons = new VBox();
        jenisKaryaSubButtons.setPadding(new Insets(7));
        jenisKaryaSubButtons.setSpacing(5);
        Button novelButton = new Button("Novel");
        novelButton.setMinWidth(120);
        Button cerpenButton = new Button("Cerpen");
        cerpenButton.setMinWidth(120);
        Button puisiButton = new Button("Puisi");
        puisiButton.setMinWidth(120);

        jenisKaryaButton.setOnAction(event -> {
            if (jenisKaryaSubButtons.getChildren().isEmpty()) {
                jenisKaryaSubButtons.getChildren().addAll(novelButton, cerpenButton, puisiButton);
            } else {
                jenisKaryaSubButtons.getChildren().clear();
            }
        });

        Button genreButton = new Button("Genre");
        genreButton.setMinWidth(120);
        VBox genreSubButtons = new VBox();
        genreSubButtons.setPadding(new Insets(5));
        genreSubButtons.setSpacing(5);
        Button fantasiButton = new Button("Fantasi");
        fantasiButton.setMinWidth(120);
        Button romantisButton = new Button("Romantis");
        romantisButton.setMinWidth(120);
        Button misteriButton = new Button("Misteri");
        misteriButton.setMinWidth(120);
        Button thrillerButton = new Button("Thriller");
        thrillerButton.setMinWidth(120);
        Button komediButton = new Button("Komedi");
        komediButton.setMinWidth(120);
        Button dramaButton = new Button("Drama");
        dramaButton.setMinWidth(120);

        fantasiButton.setOnAction(event -> {
            List<Work> searchResults = DBManager.searchWorksByTag("Fantasi");
            workListView.getItems().setAll(searchResults);
        });

        romantisButton.setOnAction(event -> {
            List<Work> searchResults = DBManager.searchWorksByTag("Romantis");
            workListView.getItems().setAll(searchResults);
        });

        misteriButton.setOnAction(event -> {
            List<Work> searchResults = DBManager.searchWorksByTag("Misteri");
            workListView.getItems().setAll(searchResults);
        });

        thrillerButton.setOnAction(event -> {
            List<Work> searchResults = DBManager.searchWorksByTag("Thriller");
            workListView.getItems().setAll(searchResults);
        });

        komediButton.setOnAction(event -> {
            List<Work> searchResults = DBManager.searchWorksByTag("Komedi");
            workListView.getItems().setAll(searchResults);
        });

        dramaButton.setOnAction(event -> {
            List<Work> searchResults = DBManager.searchWorksByTag("Drama");
            workListView.getItems().setAll(searchResults);
        });

        genreButton.setOnAction(event -> {
            if (genreSubButtons.getChildren().isEmpty()) {
                genreSubButtons.getChildren().addAll(fantasiButton, romantisButton, misteriButton, thrillerButton, komediButton, dramaButton);
            } else {
                genreSubButtons.getChildren().clear();
            }
        });


        sidebar.getChildren().addAll(jenisKaryaButton, createScrollPane(jenisKaryaSubButtons),
                                  genreButton, createScrollPane(genreSubButtons));

        ScrollPane contentScrollPane = new ScrollPane();
        contentScrollPane.setFitToWidth(true);
        contentScrollPane.setFitToHeight(true);
        contentScrollPane.setPadding(new Insets(10, 10, 10, 15));

        workListView = new ListView<>();
        contentScrollPane.setContent(workListView);

        root.setTop(topBar);
        root.setLeft(sidebar);
        root.setCenter(contentScrollPane);

        MainController controller = new MainController(searchField, workListView);
        controller.init();

        Scene scene = new Scene(root, 1000, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private ScrollPane createScrollPane(VBox content) {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        return scrollPane;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public void setLoggedIn(boolean isLoggedIn, User user) {
    this.isLoggedIn = isLoggedIn;
    if (isLoggedIn) {
        topBar.getChildren().remove(loginButton);
        accountButton = new Button("Account");
        accountButton.setOnAction(event -> {
            AccountManage accountManage = new AccountManage(new Stage(), (RegisteredUser) user);
            accountManage.showManage();
        });
        topBar.getChildren().add(accountButton);
    } else {
        topBar.getChildren().remove(accountButton);
        topBar.getChildren().add(loginButton);
    }
}

    public static void main(String[] args) {
        launch(args);
    }
}