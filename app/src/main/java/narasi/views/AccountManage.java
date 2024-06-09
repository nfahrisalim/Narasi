package narasi.views;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import narasi.models.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AccountManage {

    private final Stage stage;
    private MainView mainView;
    private final ObservableList<Work> works;
    private Work selectedWork;
    private final User currentUser;
    private TextArea contentArea;
    private TextField titleField;
    private Stage primaryStage;
    private final ObservableList<String> selectedTags = FXCollections.observableArrayList();
    private Label chapterIndicator; 
    private ComboBox<Integer> chapterNavigation;

    public AccountManage(Stage stage, User currentUser, MainView mainView) {
        this.stage = stage;
        this.currentUser = currentUser;
        this.works = FXCollections.observableArrayList(DBManager.getWorksByCurrentUser(currentUser.getId()));
        this.mainView = mainView;
    }

    public void showManage() {
    stage.setTitle("Manage dan Publish Content");

    BorderPane root = new BorderPane();

    ListView<String> workListView = new ListView<>();
    ObservableList<String> workTitles = FXCollections.observableArrayList();
    for (Work work : works) {
        workTitles.add(work.getTitle() + (work.isDraft() ? " (Draft)" : ""));
    }
    chapterNavigation = new ComboBox<>();
    workListView.setItems(workTitles);
    workListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
        int selectedIndex = workListView.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0 && selectedIndex < works.size()) {
            selectedWork = works.get(selectedIndex);
            selectedWork.setChapters(DBManager.getChaptersByWorkId(selectedWork.getId()));
            System.out.println("Selected work chapters: " + selectedWork.getChapters().size());
            showWorkDetails(selectedWork);
            refreshChapterNavigation();
            System.out.println("Selected work: " + (selectedWork != null ? selectedWork.getTitle() : "None"));
        }
    });
        
       
        VBox leftPane = new VBox(10);
        leftPane.setPadding(new Insets(10));
        leftPane.getChildren().add(workListView);

        Button closeButton = new Button("Close");
        closeButton.setOnAction(event -> stage.close());

        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Logout Confirmation");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to logout? Any unsaved changes will be lost.");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                mainView.setLoggedIn(false, currentUser);
                stage.close();
                System.out.println("User logged out.");
            }
        });

        leftPane.getChildren().addAll(closeButton, logoutButton);

        GridPane rightPane = new GridPane();
        rightPane.setPadding(new Insets(10));
        rightPane.setVgap(10);
        rightPane.setHgap(10);

        titleField = new TextField();
        titleField.setPromptText("Title");
        titleField.setPrefHeight(40); 

        contentArea = new TextArea();
        contentArea.setPromptText("Write your content here...");
        contentArea.setPrefRowCount(90);

        chapterIndicator = new Label(); 
        updateChapterIndicator(); 
        chapterIndicator = new Label();
        chapterNavigation.setOnAction(event -> {
            if (selectedWork != null) {
                int selectedIndex = chapterNavigation.getSelectionModel().getSelectedIndex();
                System.out.println("Selected chapter index: " + selectedIndex);
                System.out.println("Total chapters: " + selectedWork.getChapters().size());
    
                if (selectedIndex == 0) {
                    // Handle the case for the main work content
                    titleField.setText(selectedWork.getTitle());
                    contentArea.setText(selectedWork.getContent());
                } else if (selectedIndex > 0) {
                    int chapterIndex = selectedIndex - 1; // Adjust index for chapter
                    if (chapterIndex < selectedWork.getChapters().size()) {
                        Chapter chapter = selectedWork.getChapters().get(chapterIndex);
                        if (chapter != null) {
                            titleField.setText(chapter.getTitle());
                            contentArea.setText(chapter.getContent());
                        } else {
                            System.out.println("Chapter not found.");
                        }
                    } else {
                        System.out.println("Invalid chapter index: " + chapterIndex);
                    }
                } else {
                    System.out.println("Invalid chapter selection.");
                }
            }
        });
        
        Button saveDraftButton = new Button("Save Draft");
        saveDraftButton.setOnAction(event -> {
            if (selectedWork != null) {
                int selectedIndex = chapterNavigation.getSelectionModel().getSelectedIndex();
                if (selectedIndex == 0) {
                    selectedWork.setTitle(titleField.getText());
                    selectedWork.setContent(contentArea.getText());
                    selectedWork.setTags(String.join(", ", selectedTags));
                    selectedWork.setDraft(true);
                    saveDraft(selectedWork);
                } else if (selectedIndex > 0) {
                    Chapter currentChapter = selectedWork.getChapters().get(selectedIndex - 1); // Adjust index for chapter
                    currentChapter.setTitle(titleField.getText());
                    currentChapter.setContent(contentArea.getText());
                    DBManager.saveChapter(currentChapter);
                }
                updateWorkListView(workListView, workTitles);
            } else {
                if (!titleField.getText().isEmpty() && !contentArea.getText().isEmpty()) {
                    Work newWork = new Work();
                    newWork.setTitle(titleField.getText());
                    newWork.setContent(contentArea.getText());
                    newWork.setDraft(true);
                    newWork.setUserId(currentUser.getId());
                    boolean success = DBManager.addWork(newWork);
                    if (success) {
                        System.out.println("New work created and draft saved successfully.");
                        works.add(newWork);
                        workListView.getItems().add(newWork.getTitle() + " (Draft)");
                    } else {
                        System.out.println("Failed to create new work and save draft.");
                    }
                } else {
                    System.out.println("Title and content cannot be empty.");
                }
            }
        });
        
Button publishButton = new Button("Publish");
publishButton.setOnAction(event -> {
    if (selectedWork != null) {
        int selectedIndex = chapterNavigation.getSelectionModel().getSelectedIndex();
        if (selectedIndex == 0) {
            selectedWork.setTitle(titleField.getText());
            selectedWork.setContent(contentArea.getText());
            selectedWork.setTags(String.join(", ", selectedTags));
            selectedWork.setDraft(false); 
            boolean success = DBManager.updateWork(selectedWork);
            if (success) {
                System.out.println("Work published successfully.");
            } else {
                System.out.println("Failed to publish work.");
            }
        } else {
            Chapter currentChapter = selectedWork.getChapters().get(selectedIndex + 1);
            currentChapter.setTitle(titleField.getText());
            currentChapter.setContent(contentArea.getText());
            DBManager.saveChapter(currentChapter);
        }
        updateWorkListView(workListView, workTitles);
    } else {
        if (!titleField.getText().isEmpty() && !contentArea.getText().isEmpty() && !selectedTags.isEmpty()) {
            Work newWork = new Work();
            newWork.setTitle(titleField.getText());
            newWork.setContent(contentArea.getText());
            newWork.setTags(String.join(", ", selectedTags));
            newWork.setDraft(false); 
            newWork.setUserId(currentUser.getId());
            boolean success = DBManager.publishWork(newWork);
            if (success) {
                System.out.println("New work created and published successfully.");
                works.add(newWork);
                workListView.getItems().add(newWork.getTitle());
            } else {
                System.out.println("Failed to create and publish new work.");
            }
        } else {
            System.out.println("Title, content, and tags cannot be empty.");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Publish Warning");
            alert.setHeaderText(null);
            alert.setContentText("Please ensure title, content, and tags are filled in before publishing.");
            alert.showAndWait();
        }
    }
});

Button addChapterButton = new Button("Add Chapter");
addChapterButton.setOnAction(event -> {
    if (selectedWork != null) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Chapter");
        dialog.setHeaderText("Create a new chapter");
        dialog.setContentText("Please enter the chapter title:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            String chapterTitle = result.get();
            int nextChapterNumber = selectedWork.getChapters().size() + 2; // Adjust for chapter number
            Chapter newChapter = new Chapter(0, selectedWork.getId(), nextChapterNumber, chapterTitle, "");
            if (DBManager.addChapter(newChapter)) {
                selectedWork.getChapters().add(newChapter);
                refreshChapterNavigation();
                System.out.println("Chapter added successfully.");
            } else {
                System.out.println("Failed to add chapter.");
            }
        }
    }
});

        Button deleteWorkButton = new Button("Delete Work");
        deleteWorkButton.setOnAction(event -> {
            if (selectedWork != null) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Delete Confirmation");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to delete this work? This action cannot be undone.");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    int workId = selectedWork.getId();
                    System.out.println("Deleting work with id: " + workId);
                    if (DBManager.deleteWork(workId)) {
                        works.remove(selectedWork);
                        workTitles.remove(selectedWork.getTitle() + (selectedWork.isDraft() ? " (Draft)" : ""));
                        selectedWork = null;
                        titleField.clear();
                        contentArea.clear();
                        selectedTags.clear();
                        System.out.println("Work deleted successfully.");
                        updateWorkListView(workListView, workTitles);
                    } else {
                        System.out.println("Failed to delete work.");
                    }
                }
            }
        });


        Button deleteChapterButton = new Button("Delete Chapter");
        deleteChapterButton.setOnAction(event -> {
            if (selectedWork != null && selectedWork.getChapters().size() > 0) {
                Chapter lastChapter = selectedWork.getChapters().get(selectedWork.getChapters().size() + 1);
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Delete Chapter Confirmation");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to delete the last chapter?");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    if (DBManager.deleteChapter(lastChapter.getId())) {
                        selectedWork.getChapters().remove(lastChapter);
                        refreshChapterNavigation();
                        System.out.println("Chapter deleted successfully.");
                        updateChapterIndicator(); 
                    } else {
                        System.out.println("Failed to delete chapter.");
                    }
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Delete Chapter Warning");
                alert.setHeaderText(null);
                alert.setContentText("No chapters to delete, or deletion not possible. Consider deleting the entire work.");
                alert.showAndWait();
            }
        });

        contentArea.textProperty().addListener((observable, oldValue, newValue) -> saveChapter());
        titleField.textProperty().addListener((observable, oldValue, newValue) -> saveChapter());


        rightPane.add(new Label("Title:"), 0, 0);
        rightPane.add(titleField, 1, 0);
        rightPane.add(new Label("Content:"), 0, 1);
        rightPane.add(contentArea, 1, 1);
        rightPane.add(saveDraftButton, 0, 2);
        rightPane.add(publishButton, 1, 2);
        rightPane.add(addChapterButton, 0, 3);
        rightPane.add(deleteWorkButton, 1, 3);
        rightPane.add(deleteChapterButton, 0, 4); 
        rightPane.add(chapterIndicator, 1, 5); 
        rightPane.add(chapterNavigation, 1, 5);

        VBox taggingBox = createTaggingButtons();
        rightPane.add(taggingBox, 2, 1); 

        root.setLeft(leftPane);
        root.setCenter(rightPane);

        Scene scene = new Scene(root);
        stage.setFullScreen(true);
        scene.getStylesheets().add(getClass().getResource("/AccountStyle.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    private void saveChapter() {
        if (selectedWork != null) {
            int selectedIndex = chapterNavigation.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0 && selectedIndex < selectedWork.getChapters().size()) {
                Chapter chapter = selectedWork.getChapters().get(selectedIndex);
                chapter.setTitle(titleField.getText());
                chapter.setContent(contentArea.getText());
                DBManager.updateChapter(chapter); // Pastikan ada metode ini untuk menyimpan chapter
            }
        }
    }
    

    private VBox createTaggingButtons() {
        VBox taggingBox = new VBox(20);

        Button jenisKaryaButton = new Button("Jenis Karya");
        jenisKaryaButton.setMinWidth(120);
        VBox jenisKaryaSubButtons = new VBox();
        jenisKaryaSubButtons.setPadding(new Insets(7));
        jenisKaryaSubButtons.setSpacing(5);
        Button novelButton = createTagButton("Novel");
        Button cerpenButton = createTagButton("Cerpen");
        Button puisiButton = createTagButton("Puisi");
        jenisKaryaButton.setOnAction(event -> toggleSubButtons(jenisKaryaSubButtons, novelButton, cerpenButton, puisiButton));

        // Genre
        Button genreButton = new Button("Genre");
        genreButton.setMinWidth(120);
        VBox genreSubButtons = new VBox();
        genreSubButtons.setPadding(new Insets(5));
        genreSubButtons.setSpacing(5);
        Button fantasiButton = createTagButton("Fantasi");
        Button romantisButton = createTagButton("Romantis");
        Button misteriButton = createTagButton("Misteri");
        Button thrillerButton = createTagButton("Thriller");
        genreButton.setOnAction(event -> toggleSubButtons(genreSubButtons, fantasiButton, romantisButton, misteriButton, thrillerButton));

        taggingBox.getChildren().addAll(jenisKaryaButton, jenisKaryaSubButtons, genreButton, genreSubButtons);

        return taggingBox;
    }

    private Button createTagButton(String tag) {
        Button button = new Button(tag);
        button.setMinWidth(120);
        button.setOnAction(event -> {
            if (selectedTags.contains(tag)) {
                selectedTags.remove(tag);
                button.setStyle("");
            } else {
                selectedTags.add(tag);
                button.setStyle("-fx-background-color: #87CEEB;"); // Contoh gaya untuk tombol yang dipilih
            }
            System.out.println("Selected tags: " + selectedTags);
        });
        return button;
    }

    private void toggleSubButtons(VBox subButtons, Button... buttons) {
        if (subButtons.getChildren().isEmpty()) {
            subButtons.getChildren().addAll(buttons);
        } else {
            subButtons.getChildren().clear();
        }
    }

    private void showWorkDetails(Work work) {
        if (work != null) {
            System.out.println("Selected work: " + work.getTitle());
            System.out.println("Chapters: " + work.getChapters());
            titleField.setText(work.getTitle());
            contentArea.setText(work.getContent());
            selectedTags.setAll(work.getTags().split(", "));
            refreshChapterNavigation();
        }
    }

    
    

    private void refreshChapterNavigation() {
        if (chapterNavigation != null) {
            chapterNavigation.getItems().clear();
            chapterNavigation.getItems();
            
            if (selectedWork != null) {
                for (int i = 0; i < selectedWork.getChapters().size(); i++) {
                    Integer chapterIndex = i + 1;
                    chapterNavigation.getItems().add(chapterIndex); 
                }
            }
            chapterNavigation.getSelectionModel().selectFirst();
        }
    }

    private void addChapter(Work work, String title, String content) {
        int nextChapterNumber = work.getChapters().size() + 2;
        Chapter newChapter = new Chapter(0, work.getId(), nextChapterNumber, title, content);
        if (DBManager.addChapter(newChapter)) {
            work.getChapters().add(newChapter);
            System.out.println("Chapter added successfully.");
        } else {
            System.out.println("Failed to add chapter.");
        }
    }

    private void updateWorkListView(ListView<String> workListView, ObservableList<String> workTitles) {
        workTitles.clear();
        for (Work work : works) {
            workTitles.add(work.getTitle() + (work.isDraft() ? " (Draft)" : ""));
        }
        workListView.setItems(workTitles);
    }

    private void updateChapterNavigation() {
        if (selectedWork != null) {
            ObservableList<Integer> chapterNumbers = FXCollections.observableArrayList();
            chapterNumbers.add(1); // Add chapter 1
            for (int i = 2; i <= selectedWork.getChapters().size() + 1; i++) {
                chapterNumbers.add(i);
            }
            chapterNavigation.setItems(chapterNumbers);
            chapterNavigation.getSelectionModel().selectFirst();
        } else {
            chapterNavigation.getItems().clear();
        }
    }    

    private void saveDraft(Work work) {
        work.setUserId(currentUser.getId());
        if (DBManager.updateWork(work)) {
            System.out.println("Draft saved successfully.");
        } else {
            System.out.println("Failed to save draft.");
        }
    }

    private void publishWork(Work work) {
        work.setUserId(currentUser.getId());
        if (DBManager.updateWork(work)) {
            System.out.println("Work published successfully.");
        } else {
            System.out.println("Failed to publish work.");
        }
    }

    private void updateChapterIndicator() {
        if (selectedWork != null) {
            int currentPage = selectedWork.getChapters().size();
            int totalPages = selectedWork.getChapters().size();
            chapterIndicator.setText("Chapter: " + currentPage + " / " + totalPages);
        } else {
            chapterIndicator.setText("No chapters available.");
        }
    }
}