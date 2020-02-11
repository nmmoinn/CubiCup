package CubiCup;

import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;

public class EngineDisplay {

    private TitledPane dropDownName = new TitledPane();

    private VBox engineOutput = new VBox();
    private String[] valueNames = { "Value1", "Value2" };
    private Text[] values;

    private Process p;
    BufferedReader reader;
    BufferedWriter output;

    File engineFile;

    public EngineDisplay() throws Exception {

        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(new Stage());

        if( !file.exists() ) {
            throw new Exception();
        }

        engineFile = file;

        values = new Text[valueNames.length];
        for( int i = 0; i < valueNames.length; i++ ) {
            //System.out.println("adding value for " + i);
            values[i] = new Text(valueNames[i] + ": ");
            engineOutput.getChildren().add(values[i]);
        }

        dropDownName.setContent(engineOutput);

        String engineFileName = engineFile.getName();
        String[] cmd;

        if ( engineFileName.contains(".") && engineFileName.substring(engineFileName.lastIndexOf(".")).equals(".bash")) {
            cmd = new String[]{ "bash", engineFile.getPath() };
        } else if ( engineFileName.contains(".") && engineFileName.substring(engineFileName.lastIndexOf(".")).equals(".py")) {
            cmd = new String[]{ "python", engineFile.getPath() };
        } else {
            cmd = new String[]{ engineFile.getPath() };
        }

        dropDownName.setText(engineFile.getName());

        p = Runtime.getRuntime().exec(cmd);

        reader = new BufferedReader( new InputStreamReader( p.getInputStream() ) );
        output = new BufferedWriter( new OutputStreamWriter( p.getOutputStream() ) );

        runMain.start();
    }

    public TitledPane getDropDown() {
        return dropDownName;
    }

    public BufferedWriter getOutputStream() {

        return output;
    }

    public Process getEngineProcess() {

        return p;
    }

    Service<Void> runMain = new Service<>() {
        @Override
        protected Task<Void> createTask() {

            return new Task<>() {
                String line;

                @Override
                protected Void call() {
                    try {

                        while (p.isAlive()) {

                            line = reader.readLine();

                            if (line != null) {

                                String[] lineSplit = line.split(":");

                                //System.out.println(lineSplit[0]);
                                //System.out.println(lineSplit[1]);

                                for (int i = 0; i < valueNames.length; i++) {
                                    int counter = i;
                                    if (lineSplit[0].equals(valueNames[i])) {
                                        Platform.runLater(() -> {
                                            values[counter].setText(valueNames[counter] + ": " + lineSplit[1]);
                                        });
                                    }
                                }
                            }
                        }
                    } catch (IOException e) {
                        //e.printStackTrace();
                        System.out.println("Thread error/close interfacing with engine.");
                    }

                    return null;
                }
            };
        }

        @Override
        protected void succeeded() {
            reset();
        }
    };

}
