import java.util.ArrayList;
import java.util.List;

abstract class User {
    String username;
    String password;
    List<Work> works = new ArrayList<>();

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public abstract void publishWork(Work work);
    public abstract void manageWork(Work work);

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public List<Work> getWorks() {
        return works;
    }
}

class RegisteredUser extends User {
    public RegisteredUser(String username, String password) {
        super(username, password);
    }

    @Override
    public void publishWork(Work work) {
        works.add(work);
    }

    @Override
    public void manageWork(Work work) {
        if (works.contains(work)) {
        }
    }
}

class Work {
    String title;
    String content;
    String tags;
    List<Comment> comments = new ArrayList<>();
    int kudosCount;

    public Work(String title, String content, String tags, List<Comment> comments, int kudosCount) {
        this.title = title;
        this.content = content;
        this.tags = tags;
        this.comments = comments;
        this.kudosCount = kudosCount;
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
