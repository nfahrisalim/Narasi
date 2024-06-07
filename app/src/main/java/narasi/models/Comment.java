package narasi.models;

import java.util.Date; 

public class Comment {
    private String content;
    private Date timestamp;

    public Comment(String content) {
        this.content = content;
        this.timestamp = new Date();
    }

    public Comment(String content, Date timestamp) {
        this.content = content;
        this.timestamp = timestamp;
    }

    public String getContent() {
        return content;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String displayComment() {
        return "[" + timestamp.toString() + "]: " + content;
    }
}
