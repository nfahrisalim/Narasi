


// Class comment with String
public class Comment {
    private String content;
    private String author;
    private String timeStamp;
    
    public Comment(String content, String author, String timeStamp) {
        this.content = content;
        this.author = author;
        this.timeStamp = timeStamp;
    }

    //setter
    public void setContent(String content) { 
        this.content = content;
    }
    
    public void setAuthor(String author) {
        this.author = author;
    }
    
    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
    
    //getter
    public String getContent() {
        return content;
    }
    
    public String getAuthor() {
        return author;
    }
    
    public String getTimeStamp() {
        return timeStamp;
    }
    
    public void displayComment() {
        // method
    }


    // //use if the class Author: User
    // import java.util.Date;
    
    // public class Comment {
    //     private String content;
    //     private User author;
    //     private Date timeStamp;
        
    //     public Comment(String content, User author) {
    //         this.content = content;
    //         this.author = author;
    //         this.timeStamp = new Date(); // for date
    //     }
        
    //     // getter
    //     public String getContent() {
    //         return content;
    //     }
        
    //     public User getAuthor() {
    //         return author;
    //     }
        
    //     public Date getTimeStamp() {
    //         return timeStamp;
    //     }
    
    //     // setter
    //     public void setContent(String content) { 
    //         this.content = content;
    //     }
        
    //     public void setAuthor(String author) {
    //         this.author = author;
    //     }
        
    //     public void setTimeStamp(String timeStamp) {
    //         this.timeStamp = timeStamp;
    //     }
    
    //     public void displayComment() {
    //         // method comment
    //     }
    // }
}