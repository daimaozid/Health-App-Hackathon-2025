//Imports

import java.io.IOException;
import java.util.Objects;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.Node;

public class DrugsController {

    //Variables
    private Stage stage;
    private Scene scene;
    private Parent root;

    public void switchSceneHome(ActionEvent e) throws IOException {
        //Load new scene
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("main2.fxml")));

        //Get the same window the event occured in
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();

        //Create and set stage to the new scene
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
}
