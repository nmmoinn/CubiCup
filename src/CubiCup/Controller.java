package CubiCup;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.Optional;

public class Controller {

    @FXML
    private Pane gamePane;
    private CubiCup game;

    @FXML
    VBox sidePane;

    ArrayList<BufferedWriter> engineOutputs = new ArrayList<>();
    ArrayList<Process> engineProcesses = new ArrayList<>();

    private int gameSize = 4;

    private Stage primaryStage;

    public void initialize() {

        startNewGame();

        initSidePanel();
    }

    public void setPrimaryStage( Stage stage ) {

        primaryStage = stage;

        primaryStage.setOnCloseRequest( e -> {
            for( Process engineProcess : engineProcesses ) {
                engineProcess.destroy();
                //System.out.println("killing process " + i);
            }
        });
    }

    public void initSidePanel() {

        Button butt = new Button("+");
        butt.setMaxWidth(3e8);

        butt.setOnAction( event -> {

            try {
                EngineDisplay eng = new EngineDisplay();
                int pos = sidePane.getChildren().size() - 1 ;

                sidePane.getChildren().add(pos,eng.getDropDown());
                eng.getDropDown().setExpanded(true);
                engineOutputs.add(eng.getOutputStream());
                engineProcesses.add(eng.getEngineProcess());
            } catch( Exception e ) {
                System.out.println(e.getMessage());
            }
        });

        sidePane.getChildren().add( butt );
    }

    public void startNewGame() {

        int gameSize;
        Optional<String> result;

        TextInputDialog sizeInput = new TextInputDialog("7");
        sizeInput.setContentText("Enter board size");
        sizeInput.setHeaderText("");
        sizeInput.setTitle("CubiCup Size Picker");

        while(true) {

            result = sizeInput.showAndWait();

            if( result.isEmpty() ) {
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
                    game.setEngineOutputs(engineOutputs);
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
        game.setEngineOutputs(engineOutputs);
    }

    public void exit() {
        Platform.exit();
    }

}
