package narasi.controllers;

import narasi.models.User;
import narasi.views.MainView;
import narasi.models.DBManager;

public class AuthController {
    private boolean isLoggedIn = false;
    private User loggedInUser;

    // Singleton instance
    private static AuthController instance;

    private AuthController() {
        // Private constructor to enforce singleton pattern
    }

    public static AuthController getInstance() {
        if (instance == null) {
            instance = new AuthController();
        }
        return instance;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public void login(String username, String password) {
        User user = DBManager.loginUser(username, password);
        if (user != null) {
            isLoggedIn = true;
            loggedInUser = user;
          
            MainView mainView = new MainView(); 
            mainView.setCurrentUser(loggedInUser);
        } else {
            isLoggedIn = false;
            loggedInUser = null;
        }
    }


    public void logout() {
        isLoggedIn = false;
        loggedInUser = null;
    }
}
