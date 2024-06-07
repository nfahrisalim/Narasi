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
import narasi.models.Chapter;

import java.util.List;

public class ReadingView {
    private BorderPane root;
    private int kudosCount = 0;
    private Label kudosLabel;
    private TextArea readingArea;
    private Stage primaryStage;
    private MainView mainView;
    private boolean isKudosClicked = false;
    private int currentChapterIndex = 0;
    private Work work;

    public ReadingView(Stage primaryStage, Work work, MainView mainView) {
        this.primaryStage = primaryStage;
        this.mainView = mainView;
        this.work = work;
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
        updateContent(centerBox);

        kudosLabel = new Label(String.valueOf(kudosCount));
        Button kudosButton = new Button("Like");
        Button commentButton = new Button("Comment");
        Button closeButton = new Button("Close");
        Button prevButton = new Button("Prev");
        Button nextButton = new Button("Next");
        Label indexLabel = new Label((currentChapterIndex + 1) + " / " + work.getChapters().size());

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
            mainView.start(new Stage());
        });

        prevButton.setOnAction(event -> {
            if (currentChapterIndex > 0) {
                currentChapterIndex--;
                indexLabel.setText((currentChapterIndex + 1) + " / " + work.getChapters().size());
                updateContent(centerBox);
            }
        });

        nextButton.setOnAction(event -> {
            if (currentChapterIndex < work.getChapters().size() - 1) {
                currentChapterIndex++;
                indexLabel.setText((currentChapterIndex + 1) + " / " + work.getChapters().size());
                updateContent(centerBox);
            }
        });

        HBox leftBox = new HBox(10);
        leftBox.setAlignment(Pos.CENTER_LEFT);
        leftBox.getChildren().addAll(commentButton, closeButton);

        HBox centerBoxBottom = new HBox(10);
        centerBoxBottom.setAlignment(Pos.CENTER);
        centerBoxBottom.getChildren().addAll(prevButton, indexLabel, nextButton);

        HBox rightBox = new HBox(5);
        rightBox.setAlignment(Pos.CENTER_RIGHT);
        rightBox.getChildren().addAll(kudosButton, kudosLabel);

        HBox bottomBox = new HBox(10);
        bottomBox.setPadding(new Insets(10));
        bottomBox.setAlignment(Pos.CENTER);
        HBox.setHgrow(leftBox, Priority.ALWAYS);
        HBox.setHgrow(centerBoxBottom, Priority.ALWAYS);
        HBox.setHgrow(rightBox, Priority.ALWAYS);
        bottomBox.getChildren().addAll(leftBox, centerBoxBottom, rightBox);

        root.setBottom(bottomBox);

        Scene scene = new
        Scene(root);
        primaryStage.setTitle("N A R A S I - Platform Karya Tulis Mahasiswa");
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        scene.getStylesheets().add(getClass().getResource("/ReadingStyle.css").toExternalForm());
        primaryStage.show();
    }

    private void updateContent(VBox centerBox) {
        centerBox.getChildren().clear();

        String content;
        if (currentChapterIndex == 0) {
            // Chapter 1: Get content from the work itself
            content = work.getContent();
        } else {
            // Chapter 2 and beyond: Get content from the chapters list
            if (currentChapterIndex < work.getChapters().size()) {
                Chapter currentChapter = work.getChapters().get(currentChapterIndex);
                content = currentChapter.getTitle() + "\n\n" + currentChapter.getContent();
            } else {
                System.out.println("Chapter index out of bounds.");
                return;
            }
        }

        int workId = work.getId();

        if (!DBManager.workExists(workId)) {
            System.out.println("Work with ID: " + workId + " does not exist.");
        } else {
            User user = DBManager.getUserByWorkId(workId);
            if (user == null) {
                System.out.println("User is null for work with ID: " + workId);
            } else {
                System.out.println("User information: " + user.getFullName() + ", " + user.getEmail());

                String userInfo = "Di publish oleh:\n" + user.getFullName() + "\n" + user.getEmail();
                String combinedContent = userInfo + "\n-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n" + content;

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
    }

    public void addCommentToReadingArea(Comment comment) {
        String currentText = readingArea.getText();
        readingArea.setText(currentText + "\n-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n" + comment.displayComment());
    }

    public BorderPane getView() {
        return root;
    }
}
