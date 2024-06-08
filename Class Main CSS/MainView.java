package narasi.views;

import java.util.List;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import narasi.controllers.MainController;
import narasi.models.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class MainView extends Application {


    private ListView<Work> workListView;
    private boolean isLoggedIn = false;
    private Button loginButton;
    private User loggedInUser;
    private Button accountButton;
    private BorderPane root;
    private HBox topBar;
    private User currentUser;
    private Stage primaryStage; 


    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage; 
        primaryStage.setTitle("Narasi - Platform Karya Tulis Mahasiswa");


        root = new BorderPane();


        topBar = new HBox();
        topBar.setPadding(new Insets(20));
        topBar.setSpacing(15);
        topBar.setAlignment(Pos.TOP_RIGHT);


        loginButton = new Button("Login");
        loginButton.setOnAction(event -> {
            Stage loginStage = new Stage();
            loginStage.initOwner(primaryStage);
            LoginView loginView = new LoginView(loginStage, this);
            loginView.showLogin();
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
        jenisKaryaButton.setMinWidth(130);
        VBox jenisKaryaSubButtons = new VBox();
        jenisKaryaSubButtons.setPadding(new Insets(7));
        jenisKaryaSubButtons.setSpacing(5);
        Button novelButton = new Button("Novel");
        novelButton.setMinWidth(120);
        Button cerpenButton = new Button("Cerpen");
        cerpenButton.setMinWidth(120);
        Button puisiButton = new Button("Puisi");
        puisiButton.setMinWidth(120);


        novelButton.setOnAction(event -> {
            List<Work> searchResults = DBManager.searchWorksByTag("Novel");
            workListView.getItems().setAll(searchResults);
        });
        cerpenButton.setOnAction(event -> {
            List<Work> searchResults = DBManager.searchWorksByTag("Cerpen");
            workListView.getItems().setAll(searchResults);
        });
        puisiButton.setOnAction(event -> {
            List<Work> searchResults = DBManager.searchWorksByTag("Puisi");
            workListView.getItems().setAll(searchResults);
        });


        jenisKaryaButton.setOnAction(event -> {
            if (jenisKaryaSubButtons.getChildren().isEmpty()) {
                jenisKaryaSubButtons.getChildren().addAll(novelButton, cerpenButton, puisiButton);
            } else {
                jenisKaryaSubButtons.getChildren().clear();
            }
        });


        Button genreButton = new Button("Genre");
        genreButton.setMinWidth(130);
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


        sidebar.getChildren().addAll(jenisKaryaButton, (jenisKaryaSubButtons),
                genreButton, (genreSubButtons));


        ScrollPane contentScrollPane = new ScrollPane();
        contentScrollPane.setFitToWidth(true);
        contentScrollPane.setFitToHeight(true);
        contentScrollPane.setPadding(new Insets(10, 10, 10, 15));


        workListView = new ListView<>();
        workListView.setCellFactory(param -> new CustomListCell());


        workListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && workListView.getSelectionModel().getSelectedItem() != null) {
                Work selectedWork = workListView.getSelectionModel().getSelectedItem();
                showReadingView(selectedWork);
            }
        });


        contentScrollPane.setContent(workListView);


        root.setTop(topBar);
        root.setLeft(sidebar);
        root.setCenter(contentScrollPane);


        MainController controller = new MainController(searchField, workListView);
        controller.init();


        List<Work> allWorks = DBManager.getAllPublishedWorks();
        workListView.getItems().setAll(allWorks);


        Scene scene = new Scene(root, 1000, 600);
        scene.getStylesheets().add(getClass().getResource("/MainStyle.css").toExternalForm());
        primaryStage.setFullScreen(true);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    private void showReadingView(Work selectedWork) {
        Stage readingStage = new Stage();
        ReadingView readingView = new ReadingView(readingStage, selectedWork, this);
        Scene readingScene = new Scene(readingView.getView(), primaryStage.getWidth(), primaryStage.getHeight());
        readingStage.initOwner(primaryStage);
        readingStage.initModality(Modality.APPLICATION_MODAL);
        readingStage.setFullScreen(true);
        readingStage.setScene(readingScene);
        readingStage.show();
    }


    public BorderPane getRoot() {
        return root;
    }


    public Scene createScene() {
        return primaryStage.getScene();
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
                AccountManage accountManage = new AccountManage(new Stage(), (RegisteredUser) user, this);
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


    static class CustomListCell extends ListCell<Work> {
        private VBox content;
        private Text title;
        private Text author;
        private Text tags;
        private Text preview;
        private Text timestamp;


        public CustomListCell() {
            super();
            title = new Text();
            tags = new Text();
            preview = new Text();
            timestamp = new Text();


            content = new VBox(title, tags, preview, timestamp);
            content.setSpacing(5);
            content.setPadding(new Insets(10, 10, 10, 10));
        }


        @Override
        protected void updateItem(Work item, boolean empty) {
            super.updateItem(item, empty);
            if (item != null && !empty) {
                title.setText(item.getTitle());
                title.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-padding: 5px;");
                
                tags.setText("Tags: " + item.getTags());
                tags.setStyle("-fx-font-size: 14px; -fx-padding: 5px; -fx-text-fill: gray;");
                
                preview.setText(getPreviewContent(item.getContent()));
                preview.setStyle("-fx-font-size: 14px; -fx-padding: 5px;");
                
                LocalDateTime releaseDateTime = item.getTimestamp().toLocalDateTime();
                String formattedTimestamp = releaseDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                timestamp.setText("Tanggal Rilis: " + formattedTimestamp);
                timestamp.setStyle("-fx-font-size: 12px; -fx-padding: 5px; -fx-text-fill: darkgray;");
                
                VBox contentBox = new VBox(title, tags, preview, timestamp);
                contentBox.setStyle("-fx-padding: 10px; -fx-border-color: lightgray; -fx-border-width: 1px; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-background-color: #f9f9f9;");
                setGraphic(contentBox);
            } else {
                setGraphic(null);
            }
        }


        private String getPreviewContent(String content) {
            String[] sentences = content.split("(?<!\\w\\.\\w.)(?<![A-Z][a-z]\\.)(?<=\\.|\\?|!)\\s+");
            if (sentences.length > 3) {
                return String.join(" ", java.util.Arrays.copyOfRange(sentences, 0, 3)) + "...";
            } else {
                return content;
            }
        }
    }
}