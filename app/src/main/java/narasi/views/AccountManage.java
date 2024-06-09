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
import java.util.Collections;


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
                System.out.println("Selected work: " + (selectedWork != null ? selectedWork.getTitle() : "None"));
                if (selectedWork.getPageMarker() >= 2) {
                    titleField.setEditable(false);
                } else {
                    titleField.setEditable(true);
                }
                showWorkDetails(selectedWork);
                refreshChapterNavigation(selectedWork);
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
            Integer selectedPageMarker = chapterNavigation.getValue(); 
        
            if (selectedWork != null && selectedPageMarker != null) { 
                saveChapter();
                int selectedIndex = selectedPageMarker.intValue(); 
                System.out.println("Selected chapter index: " + selectedIndex);
        
                if (selectedIndex == 0 || selectedWork.getPageMarker() < 2) {
              
                    titleField.setText(selectedWork.getTitle());
                    contentArea.setText(selectedWork.getContent());
                } else {
                    int targetPageMarker = selectedIndex; 
                    Work selectedChapter = works.stream()
                            .filter(work -> work.getTitle().equals(selectedWork.getTitle()) && work.getPageMarker() == targetPageMarker)
                            .findFirst()
                            .orElse(null);
                    if (selectedChapter != null) {
                        titleField.setText(selectedChapter.getTitle());
                        contentArea.setText(selectedChapter.getContent());
                    } else {
                        System.out.println("Invalid chapter selection.");
                    }
                }
            }
        }); 
        

        Button saveDraftButton = new Button("Save Draft");
        saveDraftButton.setOnAction(event -> {
            if (!titleField.getText().isEmpty() && !contentArea.getText().isEmpty()) {
                saveChapter();
                if (selectedWork != null) {
                    selectedWork.setTitle(titleField.getText());
                    selectedWork.setContent(contentArea.getText());
                    selectedWork.setDraft(true);
                    DBManager.updateWork(selectedWork);
                } else {
                    Work newWork = new Work();
                    newWork.setTitle(titleField.getText());
                    newWork.setContent(contentArea.getText());
                    newWork.setDraft(true);
                    newWork.setUserId(currentUser.getId());
                    newWork.setPageMarker(1); 
                    boolean success = DBManager.addWork(newWork);
                    if (success) {
                        works.add(newWork);
                        workListView.getItems().add(newWork.getTitle() + " (Draft)");
                    }
                }
                updateWorkListView(workListView, workTitles);
            } else {
                System.out.println("Title and content cannot be empty.");
            }
        });
        
        
        Button publishButton = new Button("Publish");
        publishButton.setOnAction(event -> {
            if (!titleField.getText().isEmpty() && !contentArea.getText().isEmpty() && !selectedTags.isEmpty()) {
                saveChapter();
                if (selectedWork != null) {
                    if (selectedWork.getPageMarker() >= 2) {
                        Work mainWork = works.stream()
                                .filter(work -> work.getTitle().equals(selectedWork.getTitle()) && work.getPageMarker() == 1)
                                .findFirst()
                                .orElse(null);
                        if (mainWork != null) {
                            Work updatedWork = new Work();
                            updatedWork.setId(selectedWork.getId());
                            updatedWork.setTitle(selectedWork.getTitle());
                            updatedWork.setContent(contentArea.getText());
                            updatedWork.setTags(String.join(", ", selectedTags));
                            DBManager.updateWork(selectedWork);
                            updatedWork.setDraft(false);
                            updatedWork.setPageMarker(selectedWork.getPageMarker());
                            updatedWork.setUserId(currentUser.getId());
        
                            boolean success = DBManager.updateWork(updatedWork);
                            if (success) {
                                System.out.println("Chapter published successfully.");
                            } else {
                                System.out.println("Failed to publish chapter.");
                            }
                        } else {
                            System.out.println("Main work not found.");
                        }
                    } else {
                        Work updatedWork = new Work();
                        updatedWork.setId(selectedWork.getId());
                        updatedWork.setTitle(titleField.getText());
                        updatedWork.setContent(contentArea.getText());
                        updatedWork.setTags(String.join(", ", selectedTags));
                        updatedWork.setDraft(false);
                        updatedWork.setPageMarker(selectedWork.getPageMarker());
                        updatedWork.setUserId(currentUser.getId());
        
                        boolean success = DBManager.updateWork(updatedWork);
                        if (success) {
                            System.out.println("Work published successfully.");
                        } else {
                            System.out.println("Failed to publish work.");
                        }
                    }
                } else {
                    Work newWork = new Work();
                    newWork.setTitle(titleField.getText());
                    newWork.setContent(contentArea.getText());
                    newWork.setTags(String.join(", ", selectedTags));
                    newWork.setDraft(false);
                    newWork.setPageMarker(1);
                    newWork.setUserId(currentUser.getId());
        
                    boolean success = DBManager.addWork(newWork);
                    if (success) {
                        System.out.println("New work created and published successfully.");
                        works.add(newWork);
                        workListView.getItems().add(newWork.getTitle());
                    } else {
                        System.out.println("Failed to create and publish new work.");
                    }
                }
                updateWorkListView(workListView, workTitles);
            } else {
                System.out.println("Title, content, and tags cannot be empty.");
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Publish Warning");
                alert.setHeaderText(null);
                alert.setContentText("Please fill in the title, content, and tags before publishing.");
                alert.showAndWait();
            }
        });
        

        Button addChapterButton = new Button("Add Chapter");
        addChapterButton.setOnAction(event -> {
            if (selectedWork != null) {
                saveChapter();
                
                int newPageMarker = selectedWork.getPageMarker() + 1;
                Work newChapter = new Work();
                newChapter.setTitle(selectedWork.getTitle());
                newChapter.setPageMarker(newPageMarker);
                newChapter.setUserId(currentUser.getId());
                newChapter.setDraft(true);
        
                boolean success = DBManager.addWork(newChapter);
                if (success) {
                    works.add(newChapter);
                    refreshChapterNavigation(selectedWork);
                    System.out.println("New chapter added successfully.");
                } else {
                    System.out.println("Failed to add new chapter.");
                }
            } else {
                System.out.println("No work selected.");
            }
        });
        

        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(event -> {
            if (selectedWork != null) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Delete Confirmation");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to delete this work?");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    DBManager.deleteWork(selectedWork.getId());
                    works.remove(selectedWork);
                    selectedWork = null;
                    workListView.getItems().remove(workListView.getSelectionModel().getSelectedIndex());
                    titleField.clear();
                    contentArea.clear();
                    selectedTags.clear();
                    System.out.println("Work deleted.");
                }
            }
        });

        rightPane.add(new Label("Title:"), 0, 0);
        rightPane.add(titleField, 1, 0);
        rightPane.add(new Label("Content:"), 0, 1);
        rightPane.add(contentArea, 1, 1);
        rightPane.add(saveDraftButton, 0, 2);
        rightPane.add(publishButton, 1, 2);
        rightPane.add(deleteButton, 1, 3);

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
            if (selectedIndex >= 0) {
                if (selectedIndex == 0) {
                    
                    selectedWork.setTitle(titleField.getText());
                    selectedWork.setContent(contentArea.getText());
                    DBManager.updateWork(selectedWork); 
                } else {
                    
                    Work chapterWork = null;
                    int pageMarker = selectedWork.getPageMarker() + selectedIndex; 
                    
                    for (Work work : works) {
                        if (work.getTitle().equals(selectedWork.getTitle()) && work.getPageMarker() == pageMarker) {
                            chapterWork = work;
                            break;
                        }
                    }
                    if (chapterWork != null) {
                        chapterWork.setTitle(titleField.getText());
                        chapterWork.setContent(contentArea.getText());
                        DBManager.updateWork(chapterWork); 
                    } else {
                        System.out.println("Chapter not found.");
                    }
                }
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
                button.setStyle("-fx-background-color: #87CEEB;"); 
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
            System.out.println("Chapters: " + work.getPageMarker());
            titleField.setText(work.getTitle());
            contentArea.setText(work.getContent());
            selectedTags.setAll(work.getTags().split(", "));
            refreshChapterNavigation(work);
        }
    }

    private void refreshChapterNavigation(Work work) {
        chapterNavigation.getItems().clear();
        chapterNavigation.getItems().add(0); // Tambahkan bab pertama
        for (int i = 2; i <= work.getPageMarker(); i++) {
            chapterNavigation.getItems().add(i); // Tambahkan bab berdasarkan pageMarker
        }
    }
    

    private void showChapterContent(Work selectedChapter) {
        if (selectedChapter != null) {
            titleField.setText(selectedChapter.getTitle());
            contentArea.setText(selectedChapter.getContent());
            chapterIndicator.setText("Page: " + selectedChapter.getPageMarker());
        }
    }
    
    

    private void addChapter(Work work, String title, String content) {
        int nextChapterNumber = work.getPageMarker() + 1; 
        Chapter newChapter = new Chapter(0, work.getId(), nextChapterNumber, title, content);
        if (DBManager.addChapter(newChapter)) {
            System.out.println("Chapter added successfully.");
        } else {
            System.out.println("Failed to add chapter.");
        }
    }
    

    private void updateWorkListView(ListView<String> workListView, ObservableList<String> workTitles) {
        workTitles.clear();
        for (Work work : works) {
            if (work.getPageMarker() == 1) {
                workTitles.add(work.getTitle() + (work.isDraft() ? " (Draft)" : ""));
            }
        }
        workListView.setItems(workTitles);
    }
    

    private void updateChapterNavigation() {
        if (selectedWork != null) {
            ObservableList<Integer> chapterNumbers = FXCollections.observableArrayList();
            chapterNumbers.add(1); 
            int lastPage = selectedWork.getPageMarker();
            for (int i = 2; i <= lastPage; i++) {
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
            chapterIndicator.setText("Chapter: " + selectedWork.getPageMarker());
        } else {
            chapterIndicator.setText("Chapter: None");
        }
    }
}    