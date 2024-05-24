package narasi.views;

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
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import narasi.controllers.MainController;
import narasi.models.Work;

public class MainView extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Narasi - Platform Karya Tulis Mahasiswa");

        BorderPane root = new BorderPane();

        HBox topBar = new HBox();
        topBar.setPadding(new Insets(10));
        topBar.setSpacing(10);
        topBar.setAlignment(Pos.TOP_RIGHT); // Menempatkan elemen di pojok kanan atas

        Button loginButton = new Button("Login");
        loginButton.setOnAction(event -> {
            LoginView loginView = new LoginView(new Stage());
            loginView.showLogin();
        });

        TextField searchField = new TextField();
        searchField.setPromptText("Search...");

        topBar.getChildren().addAll(searchField, loginButton); 

        VBox sidebar = new VBox();
        sidebar.setPadding(new Insets(10));
        sidebar.setSpacing(10);

        Button jenisKaryaButton = new Button("Jenis Karya");
        VBox jenisKaryaSubButtons = new VBox();
        jenisKaryaSubButtons.setPadding(new Insets(5));
        jenisKaryaSubButtons.setSpacing(5);
        Button novelButton = new Button("Novel");
        Button cerpenButton = new Button("Cerpen");
        Button puisiButton = new Button("Puisi");

        jenisKaryaButton.setOnAction(event -> {
            if (jenisKaryaSubButtons.getChildren().isEmpty()) {
                jenisKaryaSubButtons.getChildren().addAll(novelButton, cerpenButton, puisiButton);
            } else {
                jenisKaryaSubButtons.getChildren().clear();
            }
        });

        Button genreButton = new Button("Genre");
        VBox genreSubButtons = new VBox();
        genreSubButtons.setPadding(new Insets(5));
        genreSubButtons.setSpacing(5);
        Button fantasiButton = new Button("Fantasi");
        Button romantisButton = new Button("Romantis");
        Button misteriButton = new Button("Misteri");
        Button thrillerButton = new Button("Thriller");
        Button komediButton = new Button("Komedi");
        Button dramaButton = new Button("Drama");

        genreButton.setOnAction(event -> {
            if (genreSubButtons.getChildren().isEmpty()) {
                genreSubButtons.getChildren().addAll(fantasiButton, romantisButton, misteriButton, thrillerButton, komediButton, dramaButton);
            } else {
                genreSubButtons.getChildren().clear();
            }
        });

        Button temaButton = new Button("Tema");
        VBox temaSubButtons = new VBox();
        temaSubButtons.setPadding(new Insets(5));
        temaSubButtons.setSpacing(5);
        Button cintaButton = new Button("Cinta");
        Button kehilanganButton = new Button("Kehilangan");
        Button penemuanDiriButton = new Button("Penemuan diri");
        Button keadilanSosialButton = new Button("Keadilan sosial");
        Button sejarahButton = new Button("Sejarah");
        Button budayaButton = new Button("Budaya");

        temaButton.setOnAction(event -> {
            if (temaSubButtons.getChildren().isEmpty()) {
                temaSubButtons.getChildren().addAll(cintaButton, kehilanganButton, penemuanDiriButton, keadilanSosialButton, sejarahButton, budayaButton);
            } else {
                temaSubButtons.getChildren().clear();
            }
        });

        sidebar.getChildren().addAll(jenisKaryaButton, createScrollPane(jenisKaryaSubButtons),
                                      genreButton, createScrollPane(genreSubButtons),
                                      temaButton, createScrollPane(temaSubButtons));

       
        ScrollPane contentScrollPane = new ScrollPane();
        contentScrollPane.setFitToWidth(true);
        VBox.setVgrow(contentScrollPane, Priority.ALWAYS); 

        ListView<Work> workListView = new ListView<>();

        contentScrollPane.setContent(workListView);

        root.setTop(topBar);
        root.setLeft(sidebar);
        root.setCenter(contentScrollPane);

        MainController controller = new MainController(searchField, workListView);
        controller.init();

        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }

    private ScrollPane createScrollPane(VBox content) {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(content);
        scrollPane.setFitToWidth(true);
        return scrollPane;
    }
    
    

    public static void main(String[] args) {
        launch(args);
    }
    
}
