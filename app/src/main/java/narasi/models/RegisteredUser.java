package narasi.models;

import java.util.List;

public class RegisteredUser extends User {

    public RegisteredUser(String username, String password, String fullName, String email, String anonymousId) {
        super(username, password, fullName, email, anonymousId); 
    }

    @Override
    public void publishWork(Work work) {
  
    }

    @Override
    public void manageWork(Work work) {
    }
}
