package CubiCup;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Pane;

import java.util.Optional;

public class Controller {

    @FXML
    private Pane gamePane;
    private CubiCup game;

    private int gameSize = 4;

    public void initialize() {

        newGame();

    }

    public void newGame() {

        int gameSize;
        Optional<String> result;

        TextInputDialog sizeInput = new TextInputDialog("7");
        sizeInput.setContentText("Enter board size");
        sizeInput.setHeaderText("");
        sizeInput.setTitle("CubiCup Size Picker");

        while(true) {

            result = sizeInput.showAndWait();

            if( !result.isPresent() ) {
                if( game == null ) {
                    Platform.exit();
                }
                break;
            }

            try {
                gameSize = Integer.parseInt(result.get());

                if( gameSize <= 1 ) {
                    sizeInput = new TextInputDialog("7");
                    sizeInput.setHeaderText("");
                    sizeInput.setTitle("CubiCup Size Picker");
                    sizeInput.setContentText("Pick something more than 1 ...");
                } else {
                    this.gameSize = gameSize;
                    game = new CubiCup(gamePane,gameSize);
                    break;
                }
            } catch (Exception e) {
                sizeInput = new TextInputDialog("7");
                sizeInput.setHeaderText("");
                sizeInput.setTitle("CubiCup Size Picker");
                sizeInput.setContentText("You want a board size of '" + result.get() + "'?");
            }

        }
    }

    public void reset() {
        game = new CubiCup(gamePane,gameSize);
    }

    public void exit() {
        Platform.exit();
    }

}
