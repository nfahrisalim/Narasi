package narasi.controllers;

import java.util.List;
import javafx.scene.control.TextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import narasi.models.Work;
import narasi.models.DBManager;

public class MainController {

    private final TextField searchField;
    private final ListView<Work> workListView;

    public MainController(TextField searchField, ListView<Work> workListView) {
        this.searchField = searchField;
        this.workListView = workListView;
    }

    public void init() {
        searchField.setOnAction(event -> {
            String query = searchField.getText();
            List<Work> searchResults = DBManager.searchWorksByTitle(query);
            workListView.getItems().setAll(searchResults);
        });
    }

    public void refreshWorkList() {
        List<Work> works = DBManager.getAllPublishedWorks();
        ObservableList<Work> observableWorks = FXCollections.observableArrayList(works);
        workListView.setItems(observableWorks);
    }

    public void deleteWork(int workId) {
        System.out.println("Attempting to delete work with id: " + workId);
        boolean isDeleted = DBManager.deleteWork(workId);
        if (isDeleted) {
            System.out.println("Work deleted successfully from the database.");
            refreshWorkList();
        } else {
            System.out.println("Failed to delete work from the database.");
        }
    }

    public void handleDeleteButtonAction() {
        Work selectedWork = workListView.getSelectionModel().getSelectedItem();
        if (selectedWork != null) {
            deleteWork(selectedWork.getId());
        }
    }


    public void showJenisKarya() {
        // Implement logic to show jenis karya
    }

    public void showGenre() {
        // Implement logic to show genre
    }

    public void showTema() {
        // Implement logic to show tema
    }
}
