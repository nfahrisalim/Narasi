package narasi.models;

import java.util.ArrayList;
import java.util.List;

public class SearchEngine implements Searchable {
    private List<Work> workList; 

    public List<Work> search(String query) {
        List<Work> searchResults = new ArrayList<>();

        for (Work work : workList) {
            if (work.getTitle().toLowerCase().contains(query.toLowerCase())) {
                searchResults.add(work);
            }
     
        }

        return searchResults;
    }

    @Override
    public List<Work> searchByTitle(String title) {
     
        return null;
    }

    @Override
    public List<Work> searchByTag(String tag) {
       
        return null;
    }
}

