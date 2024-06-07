
package narasi.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import narasi.models.Comment;
import narasi.models.Work;
import narasi.models.DBManager;


public class CommentView {
    private Stage commentStage;
    private TextArea commentArea;
    private Work work;
    private ReadingView readingView;

    public CommentView(Work work, ReadingView readingView) {
        this.work = work;
        this.readingView = readingView;
        initializeUI();
    }

    private void initializeUI() {
        commentStage = new Stage();
        commentStage.setTitle("Add Comment");

        commentArea = new TextArea();
        commentArea.setPromptText("Enter your comment here...");
        commentArea.setWrapText(true);

        Button submitButton = new Button("Submit");
        submitButton.setOnAction(event -> submitComment());

        VBox root = new VBox(10, commentArea, submitButton);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(10));

        Scene scene = new Scene(root, 400, 300);
        commentStage.setScene(scene);

        String css = getClass().getResource("/CommentStyle.css").toExternalForm();
        scene.getStylesheets().add(css);
    }

    private void submitComment() {
        String content = commentArea.getText();
        if (content.isEmpty()) {
            return;
        }

        Comment comment = new Comment(content);
        DBManager.addCommentToWork(work.getId(), comment);
        readingView.addCommentToReadingArea(comment);

        commentStage.close();
    }

    public void show() {
        commentStage.show();
    }
}

