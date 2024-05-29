
package main.java.narasi.views;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ReadingView {

    private BorderPane root;
    private int currentPage = 0;
    private final int totalPages = 5; // Example total number of pages
    private int kudosCount = 0;
    private Label kudosLabel;
    private TextArea readingArea;
    private Stage primaryStage;
    
    public int getKudosCount() {
        return kudosCount;
    }
    public void setKudosCount(int kudosCount) {
        this.kudosCount = kudosCount;
    }
    public Label getKudosLabel() {
        return kudosLabel;
    }
    public void setKudosLabel(Label kudosLabel) {
        this.kudosLabel = kudosLabel;
    }
    public TextArea getReadingArea() {
        return readingArea;
    }
    public void setReadingArea(TextArea readingArea) {
        this.readingArea = readingArea;
    }
    public Stage getPrimaryStage() {
        return primaryStage;
    }
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
}

class Chapter {
    private String title;
    private String content;

    public Chapter(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getChapterName() {
        return title;
    }

    public String getContent() {
        return content;
    }
}

// ini adalah importannya (note : tidak semua digunakan)
// import javafx.scene.layout.ColumnConstraints;
// import javafx.application.Application;
// import javafx.geometry.Insets;
// import javafx.geometry.Pos;
// import javafx.scene.Scene;
// import javafx.scene.control.Button;
// import javafx.scene.control.ListView;
// import javafx.scene.control.ScrollPane;
// import javafx.scene.control.TextField;
// import javafx.scene.layout.BorderPane;
// import javafx.scene.layout.HBox;
// import javafx.scene.layout.Priority;
// import javafx.scene.layout.VBox;
// import javafx.stage.Stage;
// import narasi.controllers.MainController;
// import narasi.models.SearchEngine;
// import narasi.models.Work;
// import javafx.scene.layout.GridPane;
// import javafx.scene.layout.RowConstraints; // class R.View
// import javafx.stage.Modality;

// //R.view
// import javafx.application.Application;
// import javafx.geometry.Insets;
// import javafx.geometry.Pos;
// import javafx.scene.Scene;
// import javafx.scene.control.Button;
// import javafx.scene.control.Label;
// import javafx.scene.control.ScrollPane;
// import javafx.scene.control.TextArea;
// import javafx.scene.layout.BorderPane;
// import javafx.scene.layout.HBox;
// import javafx.scene.layout.VBox;
// import javafx.scene.text.Font;
// import javafx.stage.Stage;
// import main.java.narasi.views.ReadingView;


// INI ADALAH KODE REVIEW CODE

// public void startViewing(Stage primaryStage) {
//     startReadingView(primaryStage);
// }

// public void startReadingView(Stage primaryStage) {
//     BorderPane root = new BorderPane();
//     root.setPadding(new Insets(10));

//     // Top section: Header with title
//     Label titleLabel = new Label("Insert Judul here");
//     titleLabel.setFont(javafx.scene.text.Font.font("Arial", 24));
//     HBox headerBox = new HBox(titleLabel);
//     headerBox.setAlignment(Pos.CENTER);
//     headerBox.setPadding(new Insets(10, 0, 10, 0));
//     root.setTop(headerBox);

//     // Center section: Reading Area only
//     VBox centerBox = new VBox(10);
//     centerBox.setPadding(new Insets(10));

//     // Reading area
//     Label readingAreaLabel = new Label("");
//     readingAreaLabel.setFont(javafx.scene.text.Font.font("Arial", 18));
//     TextArea readingArea = new TextArea();
//     readingArea.setEditable(false);
//     readingArea.setFont(javafx.scene.text.Font.font("Arial", 14));
//     readingArea.setWrapText(true); // This will wrap the text and remove the horizontal scrollbar

//     javafx.scene.control.ScrollPane readingScroll = new javafx.scene.control.ScrollPane(readingArea);
//     readingScroll.setFitToWidth(true);
//     readingScroll.setFitToHeight(true);

//     VBox readingBox = new VBox(10);
//     readingBox.setPadding(new Insets(10));
//     readingBox.getChildren().addAll(readingAreaLabel, readingScroll);
//     VBox.setVgrow(readingScroll, Priority.ALWAYS);

//     centerBox.getChildren().addAll(readingBox);
//     VBox.setVgrow(readingBox, Priority.ALWAYS);
//     root.setCenter(centerBox);

//     // Bottom section: Kudos, Comment, Previous, Next buttons
//     Button kudosButton = new Button("Like");
//     Button commentButton = new Button("Comment");
//     Button previousButton = new Button("Previous");
//     Button nextButton = new Button("Next");

//     // Left-aligned box for the comment button
//     HBox leftBox = new HBox(10);
//     leftBox.setAlignment(Pos.CENTER_LEFT);
//     leftBox.getChildren().add(commentButton);

//     // Right-aligned box for the kudos, previous, and next buttons
//     HBox rightBox = new HBox(10);
//     rightBox.setAlignment(Pos.CENTER_RIGHT);
//     rightBox.getChildren().addAll(kudosButton, previousButton, nextButton);

//     // Container for both left and right boxes
//     HBox bottomBox = new HBox(10);
//     bottomBox.setPadding(new Insets(10));
//     bottomBox.setAlignment(Pos.CENTER);
//     HBox.setHgrow(leftBox, Priority.ALWAYS);
//     HBox.setHgrow(rightBox, Priority.ALWAYS);
//     bottomBox.getChildren().addAll(leftBox, rightBox);

//     root.setBottom(bottomBox);

//     // Create and configure the scene
//     Scene scene = new Scene(root, 1000, 800);

//     // Display the window
//     primaryStage.setTitle("N A R A S I - Platform Karya Tulis Mahasiswa");
//     primaryStage.setScene(scene);
//     primaryStage.show();
// }

