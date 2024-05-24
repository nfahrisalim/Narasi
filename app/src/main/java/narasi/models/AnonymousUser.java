package narasi.models;

import java.util.List;

public class AnonymousUser extends User {
    private String anonymousId;

    public AnonymousUser(String anonymousId) {
        super("Anonymous", "", "", "", anonymousId); // Pass anonymousId to the superclass constructor
        this.anonymousId = anonymousId;
    }

    @Override
    public void publishWork(Work work) {
        throw new UnsupportedOperationException("Anonymous users cannot publish works");
    }

    @Override
    public void manageWork(Work work) {
        throw new UnsupportedOperationException("Anonymous users cannot manage works");
    }
}
