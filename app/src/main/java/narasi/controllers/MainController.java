package narasi.controllers;

import java.util.List;
import javafx.scene.control.TextField;
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
