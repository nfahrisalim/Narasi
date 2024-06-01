package narasi.models;

public class RegisteredUser extends User {

    public RegisteredUser(String username, String password, String fullName, String email) {
        super(username, password, fullName, email);
    }

    public RegisteredUser(String username, String password, String fullName, String email, int id) {
        super(username, password, fullName, email);
        this.id = id;
    }

    public RegisteredUser() {
        super("", "", "", ""); 
    }

    public void setUserId(int id) {
        this.id = id;
    }

    @Override
    public void publishWork(Work work) {
    }

    @Override
    public void manageWork(Work work) {
    }
}
