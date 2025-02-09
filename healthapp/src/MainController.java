//Imports

import java.io.IOException;
import java.util.Objects;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.scene.control.Label;

public class MainController {

    //Variables
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private Label usernameLabel;

    //Methods

    public void displayName(String s) {
        usernameLabel.setText(s);
    }

    public void switchSceneBody(ActionEvent e) throws IOException {
        //Load new scene
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("body.fxml")));

        //Get the same window the event occured in
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();

        //Create and set stage to the new scene
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchSceneDrugs(ActionEvent e) throws IOException {
        //Load new scene
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("drugs.fxml")));

        //Get the same window the event occured in
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();

        //Create and set stage to the new scene
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
}
