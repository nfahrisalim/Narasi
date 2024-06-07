package narasi.models;

import java.sql.Timestamp;

public class Chapter {
    private int id;
    private int workId;
    private int chapterNumber;
    private String title;
    private String content;
    private Timestamp timestamp;

    public Chapter(int id, int workId, int chapterNumber, String title, String content, Timestamp timestamp) {
        this.id = id;
        this.workId = workId;
        this.chapterNumber = chapterNumber;
        this.title = title;
        this.content = content;
        this.timestamp = timestamp;
    }

    // Getters dan Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWorkId() {
        return workId;
    }

    public void setWorkId(int workId) {
        this.workId = workId;
    }

    public int getChapterNumber() {
        return chapterNumber;
    }

    public void setChapterNumber(int chapterNumber) {
        this.chapterNumber = chapterNumber;
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

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
