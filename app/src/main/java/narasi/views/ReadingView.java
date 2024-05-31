package narasi.views;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import narasi.models.Work;

public class ReadingView {
    private BorderPane root;
    private int currentPage = 0;
    private final int totalPages = 5; 
    private int kudosCount = 0;
    private Label kudosLabel;
    private TextArea readingArea;
    private Stage primaryStage;

    public ReadingView(Stage primaryStage, Work work) {
        this.primaryStage = primaryStage;
        initializeUI(work);
    }

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

    private void initializeUI(Work work) {
        root = new BorderPane();
        root.setPadding(new Insets(10));

        // Top section: Header with title
        Label titleLabel = new Label(work.getTitle());
        titleLabel.setFont(Font.font("Arial", 24));
        HBox headerBox = new HBox(titleLabel);
        headerBox.setAlignment(Pos.CENTER);
        headerBox.setPadding(new Insets(10, 0, 10, 0));
        root.setTop(headerBox);

    
        VBox centerBox = new VBox(10);
        centerBox.setPadding(new Insets(10));

        // Reading area
        Label readingAreaLabel = new Label();
        readingAreaLabel.setFont(Font.font("Arial", 20));
        readingArea = new TextArea(work.getContent());
        readingArea.setEditable(false);
        readingArea.setFont(Font.font("Arial", 14));
        readingArea.setWrapText(true); 

        ScrollPane readingScroll = new ScrollPane(readingArea);
        readingScroll.setFitToWidth(true);
        readingScroll.setFitToHeight(true);

        VBox readingBox = new VBox(10);
        readingBox.setPadding(new Insets(10));
        readingBox.getChildren().addAll(readingAreaLabel, readingScroll);
        VBox.setVgrow(readingScroll, Priority.ALWAYS);

        centerBox.getChildren().addAll(readingBox);
        VBox.setVgrow(readingBox, Priority.ALWAYS);
        root.setCenter(centerBox);

        // Bottom section: Kudos, Comment, Previous, Next buttons
        Button kudosButton = new Button("Like");
        Button commentButton = new Button("Comment");
        Button previousButton = new Button("Previous");
        Button nextButton = new Button("Next");

        // Left-aligned box for the comment button
        HBox leftBox = new HBox(10);
        leftBox.setAlignment(Pos.CENTER_LEFT);
        leftBox.getChildren().add(commentButton);

        // Right-aligned box for the kudos, previous, and next buttons
        HBox rightBox = new HBox(10);
        rightBox.setAlignment(Pos.CENTER_RIGHT);
        rightBox.getChildren().addAll(kudosButton, previousButton, nextButton);

        // Container for both left and right boxes
        HBox bottomBox = new HBox(10);
        bottomBox.setPadding(new Insets(10));
        bottomBox.setAlignment(Pos.CENTER);
        HBox.setHgrow(leftBox, Priority.ALWAYS);
        HBox.setHgrow(rightBox, Priority.ALWAYS);
        bottomBox.getChildren().addAll(leftBox, rightBox);

        root.setBottom(bottomBox);

        // Create and configure the scene
        Scene scene = new Scene(root, 1000, 800);

        // Display the window
        primaryStage.setTitle("N A R A S I - Platform Karya Tulis Mahasiswa");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
