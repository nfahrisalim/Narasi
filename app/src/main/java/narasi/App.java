package narasi;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import narasi.views.MainView;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        String iconPath = "file:/D:/College/Programming/Java/Project/Narasi/app/src/main/resources/Narasi.png";
        Image icon = new Image(iconPath);

    
        primaryStage.getIcons().add(icon);

    
        ImageView iconView = new ImageView(icon);
        MainView mainView = new MainView();
        mainView.start(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
