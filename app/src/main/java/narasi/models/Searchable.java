package narasi.models;

import java.util.List;

public interface Searchable {
    List<Work> searchByTitle(String title);
    List<Work> searchByTag(String tag);
}
