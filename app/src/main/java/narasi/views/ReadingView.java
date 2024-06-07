package narasi.views;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import narasi.models.Work;
import narasi.models.DBManager;
import narasi.models.User;
import narasi.models.Comment;
import java.util.List;

public class ReadingView {
    private BorderPane root;
    private int kudosCount = 0;
    private Label kudosLabel;
    private TextArea readingArea;
    private Stage primaryStage;
    private MainView mainView;
    private boolean isKudosClicked = false;

    public ReadingView(Stage primaryStage, Work work, MainView mainView) {
        this.primaryStage = primaryStage;
        this.mainView = mainView;
        this.kudosCount = DBManager.getKudosCount(work.getId());
        initializeUI(work);
    }

    private void initializeUI(Work work) {
        root = new BorderPane();
        root.setPadding(new Insets(10));

        Label titleLabel = new Label(work.getTitle());
        titleLabel.setFont(Font.font("Arial", 24));
        HBox headerBox = new HBox(titleLabel);
        headerBox.setAlignment(Pos.CENTER);
        headerBox.setPadding(new Insets(10, 0, 10, 0));
        root.setTop(headerBox);

        VBox centerBox = new VBox(10);
        centerBox.setPadding(new Insets(10));
        String content = work.getContent();
        int workId = DBManager.getWorkIdByContent(content);

        if (workId == -1) {
            System.out.println("No work found with the provided content.");
        } else if (!DBManager.workExists(workId)) {
            System.out.println("Work with ID: " + workId + " does not exist.");
        } else {
            User user = DBManager.getUserByWorkId(workId);
            if (user == null) {
                System.out.println("User is null for work with ID: " + workId);
            } else {
                System.out.println("User information: " + user.getFullName() + ", " + user.getEmail());

                String userInfo = "Di publish oleh:\n" + user.getFullName() + "\n" + user.getEmail();
                String combinedContent = userInfo + "\n-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n" + content;

                Label readingAreaLabel = new Label();
                readingAreaLabel.setFont(Font.font("Arial", 20));

                readingArea = new TextArea(combinedContent);
                readingArea.setEditable(false);
                readingArea.setFont(Font.font("Arial", 14));
                readingArea.setWrapText(true);

                StringBuilder contentWithComments = new StringBuilder(combinedContent);
                List<Comment> comments = DBManager.getCommentsByWorkId(workId);
                for (Comment comment : comments) {
                    contentWithComments.append("\n-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n")
                                      .append(comment.displayComment());
                }
                readingArea.setText(contentWithComments.toString());
            }
        }

        ScrollPane readingScroll = new ScrollPane(readingArea);
        readingScroll.setFitToWidth(true);
        readingScroll.setFitToHeight(true);

        VBox readingBox = new VBox(10);
        readingBox.setPadding(new Insets(10));
        readingBox.getChildren().addAll(readingScroll);
        VBox.setVgrow(readingScroll, Priority.ALWAYS);

        centerBox.getChildren().addAll(readingBox);
        VBox.setVgrow(readingBox, Priority.ALWAYS);
        root.setCenter(centerBox);

        kudosLabel = new Label(String.valueOf(kudosCount));
        Button kudosButton = new Button("Like");
        Button commentButton = new Button("Comment");
        Button closeButton = new Button("Close");

        kudosButton.setOnAction(event -> {
            if (!isKudosClicked) {
                kudosCount++;
                kudosLabel.setText(String.valueOf(kudosCount));
                System.out.println("Updating kudos count to: " + kudosCount);
                DBManager.updateKudosCount(work.getId(), kudosCount);
                isKudosClicked = true;
            }
        });

        commentButton.setOnAction(event -> {
            CommentView commentView = new CommentView(work, this);
            commentView.show();
        });

        closeButton.setOnAction(event -> {
            primaryStage.close();
        });

        HBox leftBox = new HBox(10);
        leftBox.setAlignment(Pos.CENTER_LEFT);
        leftBox.getChildren().add(commentButton);

        HBox rightBox = new HBox(5);
        rightBox.setAlignment(Pos.CENTER_RIGHT);
        rightBox.getChildren().addAll(kudosButton, kudosLabel);

        HBox bottomBox = new HBox(10);
        bottomBox.setPadding(new Insets(10));
        bottomBox.setAlignment(Pos.CENTER);
        HBox.setHgrow(leftBox, Priority.ALWAYS);
        HBox.setHgrow(rightBox, Priority.ALWAYS);
        bottomBox.getChildren().addAll(leftBox, closeButton, rightBox);

        root.setBottom(bottomBox);

        Scene scene = new Scene(root);
        primaryStage.setTitle("N A R A S I - Platform Karya Tulis Mahasiswa");
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        scene.getStylesheets().add(getClass().getResource("/ReadingStyle.css").toExternalForm());
        primaryStage.show();
    }

    public void addCommentToReadingArea(Comment comment) {
        String currentText = readingArea.getText();
        readingArea.setText(currentText + "\n-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n" + comment.displayComment());
    }

    public BorderPane getView() {
        return root;
    }
}
