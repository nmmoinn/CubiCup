package CubiCup;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("MainWindow.fxml"));
        primaryStage.setTitle("CubiCup");
        primaryStage.setScene(new Scene(root, 1024, 850));
        primaryStage.show();

        primaryStage.setMinHeight(500);
        primaryStage.setMinWidth(500);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
