package narasi.models;

import java.util.Date;

public class Comment {
    private String content;
    private User author;
    private Date timestamp;

    public Comment(String content, User author) {
        this.content = content;
        this.author = author;
        this.timestamp = new Date();
    }

    public String displayComment() {
        return "[" + timestamp.toString() + "] " + author.username + ": " + content;
    }
}
