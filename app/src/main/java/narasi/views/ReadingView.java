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
import java.util.Random;


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
        root.setPadding(new Insets(20));


        Label titleLabel = new Label(work.getTitle());
        titleLabel.setFont(Font.font("Arial", 28));
        titleLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #333;");
        HBox headerBox = new HBox(titleLabel);
        headerBox.setAlignment(Pos.CENTER);
        headerBox.setPadding(new Insets(20, 0, 20, 0));
        root.setTop(headerBox);


        VBox centerBox = new VBox(15);
        centerBox.setPadding(new Insets(20));
        updateContent(centerBox);


        kudosLabel = new Label(String.valueOf(kudosCount));
        kudosLabel.setFont(Font.font("Arial", 16));
        kudosLabel.setStyle("-fx-text-fill: #007BFF;");
        Button kudosButton = new Button("Like");
        kudosButton.setStyle("-fx-background-color: #007BFF; -fx-text-fill: white; -fx-font-size: 14px;");
        Button commentButton = new Button("Comment");
        commentButton.setStyle("-fx-background-color: #28A745; -fx-text-fill: white; -fx-font-size: 14px;");
        Button closeButton = new Button("Close");
        closeButton.setStyle("-fx-background-color: #DC3545; -fx-text-fill: white; -fx-font-size: 14px;");



        kudosButton.setOnAction(event -> {
            if (!isKudosClicked) {
                kudosCount++;
                kudosLabel.setText(String.valueOf(kudosCount));
                System.out.println("Updating kudos count to: " + kudosCount);
                DBManager.updateKudosCount(work.getId(), kudosCount);
                isKudosClicked = true;
                kudosButton.setDisable(true); 
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



        HBox leftBox = new HBox(15);
        leftBox.setAlignment(Pos.CENTER_LEFT);
        leftBox.getChildren().addAll(commentButton, closeButton);




        HBox rightBox = new HBox(10);
        rightBox.setAlignment(Pos.CENTER_RIGHT);
        rightBox.getChildren().addAll(kudosButton, kudosLabel);


        HBox bottomBox = new HBox(15);
        bottomBox.setPadding(new Insets(20));
        bottomBox.setAlignment(Pos.CENTER);
        HBox.setHgrow(leftBox, Priority.ALWAYS);
       
        HBox.setHgrow(rightBox, Priority.ALWAYS);
        bottomBox.getChildren().addAll(leftBox, rightBox);


        root.setBottom(bottomBox);


        Scene scene = new Scene(root);
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
        content = work.getContent();
    } else {
        if (currentChapterIndex <= work.getPageMarker()) {
            content = "Chapter " + currentChapterIndex + ":\n\n" + DBManager.getChapterByPageMarker(work.getId(), currentChapterIndex);
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

  
    List<String> tags = DBManager.getTagsByWorkId(workId);
    StringBuilder tagsInfo = new StringBuilder("Selamat membaca   ");
    for (String tag : tags) {
        tagsInfo.append(tag).append(", ");
    }
    tagsInfo.delete(tagsInfo.length() - 2, tagsInfo.length()); 
    String tagInfoString = tagsInfo.toString();

    String userInfo = String.format(
            "Di publikasikan oleh:\n\n%s\n%s\n\n%s",
            user.getFullName(),
            user.getEmail(),
            tagInfoString 
    );

    String separator = "\n\n---------------------------------------------\n\n";
    String combinedContent = userInfo + separator + content;
    
                readingArea = new TextArea(combinedContent);
                readingArea.setEditable(false);
                readingArea.setFont(Font.font("Arial", 16));
                readingArea.setWrapText(true);
                readingArea.setStyle("-fx-background-color: #F9F9F9; -fx-border-color: lightgray; -fx-border-width: 1px;");
    
                StringBuilder contentWithComments = new StringBuilder(combinedContent);
                List<Comment> comments = DBManager.getCommentsByWorkId(workId);
                for (Comment comment : comments) {
                    contentWithComments.append(separator).append(comment.displayComment());
                }
                readingArea.setText(contentWithComments.toString());
            }
    
            ScrollPane readingScroll = new ScrollPane(readingArea);
            readingScroll.setFitToWidth(true);
            readingScroll.setFitToHeight(true);
    
            VBox readingBox = new VBox(15);
            readingBox.setPadding(new Insets(20));
            readingBox.getChildren().addAll(readingScroll);
            VBox.setVgrow(readingScroll, Priority.ALWAYS);
    
            centerBox.getChildren().addAll(readingBox);
            VBox.setVgrow(readingBox, Priority.ALWAYS);
            root.setCenter(centerBox);
        }
    }
    
    public void addCommentToReadingArea(Comment comment) {
        String currentText = readingArea.getText();
        
    
        String randomUsername = generateRandomUsername();
        
        
        readingArea.setText(currentText + "\n\n" + randomUsername + ": " + comment.displayComment());
    }
    
    private String generateRandomUsername() {
        String[] names = {"Ambatukam", "Siimut", "Masfuad", "Sayang", "Jamet", "Oliv", "Worker", "Avatuh", "Heleh", "Mamam"};
        String[] suffixes = {"123", "007", "99", "X", "_user"};
        
        Random random = new Random();
        String username = names[random.nextInt(names.length)] + suffixes[random.nextInt(suffixes.length)];
        return username;
    }
    
    public BorderPane getView() {
        return root;
    }
}