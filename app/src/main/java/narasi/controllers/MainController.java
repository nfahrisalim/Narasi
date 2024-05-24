package narasi.controllers;

import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import narasi.models.Work;

import java.util.ArrayList;
import java.util.List;

public class MainController {

    private final TextField searchField;
    private final ListView<Work> workListView;

    public MainController(TextField searchField, ListView<Work> workListView) {
        this.searchField = searchField;
        this.workListView = workListView;
    }

    public void init() {
        // Implement initialization logic here
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

    private List<Work> getMockSearchResults(String query) {
        // Mock implementation to return some dummy search results
        List<Work> mockResults = new ArrayList<>();
        mockResults.add(new Work("Title 1", "Content 1", "Tag 1, Tag 2"));
        mockResults.add(new Work("Title 2", "Content 2", "Tag 2, Tag 3"));
        // Add more mock results as needed
        return mockResults;
    }
}
