package narasi.models;

public class RegisteredUser extends User {

    public RegisteredUser(String username, String password, String fullName, String email) {
        super(username, password, fullName, email);
    }

    // Konstruktor tambahan dengan parameter ID
    public RegisteredUser(String username, String password, String fullName, String email, int id) {
        super(username, password, fullName, email);
        this.id = id;
    }

    // Konstruktor tambahan tanpa parameter ID
    public RegisteredUser() {
        super("", "", "", ""); // Atau Anda dapat menggunakan nilai default lainnya
    }

    // Metode untuk mengatur ID pengguna
    public void setUserId(int id) {
        this.id = id;
    }

    @Override
    public void publishWork(Work work) {
        // Implementasi logika untuk mempublikasikan pekerjaan
    }

    @Override
    public void manageWork(Work work) {
        // Implementasi logika untuk mengelola pekerjaan
    }
}
