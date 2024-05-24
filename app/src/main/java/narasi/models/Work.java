package narasi.models;

import java.util.ArrayList;
import java.util.List;

public class Work {
    private String title;
    private String content;
    private String tags;
    private List<Comment> comments;
    private int kudosCount;

    public Work() {
        this.title = "";
        this.content = "";
        this.tags = "";
        this.comments = new ArrayList<>();
        this.kudosCount = 0;
    }

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public int getKudosCount() {
        return kudosCount;
    }

    public void setKudosCount(int kudosCount) {
        this.kudosCount = kudosCount;
    }
}
