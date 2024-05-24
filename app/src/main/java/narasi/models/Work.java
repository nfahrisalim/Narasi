package narasi.models;

import java.util.ArrayList;
import java.util.List;

public class Work {
    private String title;
    private String content;
    private String tags;
    private List<Comment> comments;
    private int kudosCount;

    public Work(String title, String content, String tags) {
        this.title = title;
        this.content = content;
        this.tags = tags;
        this.comments = new ArrayList<>();
        this.kudosCount = 0;
    }

    public void addComment(Comment comment) {
        comments.add(comment);
    }

    public void addKudos() {
        kudosCount++;
    }
}
