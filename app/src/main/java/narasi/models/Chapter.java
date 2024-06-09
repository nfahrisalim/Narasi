package narasi.models;

import java.sql.Timestamp;

public class Chapter {
    private int id;
    private int workId;
    private int number;
    private String title;
    private String content;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Default constructor
    public Chapter() {
    }

    // Parameterized constructor
    public Chapter(int id, int workId, int number, String title, String content) {
        this.id = id;
        this.workId = workId;
        this.number = number;
        this.title = title;
        this.content = content;
    }

    // Getters and setters
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

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
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

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
}
