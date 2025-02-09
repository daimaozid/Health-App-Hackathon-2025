//Imports
import java.io.IOException;
import java.util.Objects;


import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class LoginController {

    //Variables
    private Parent root;
    private Stage stage;
    private Scene scene;

    @FXML
    private TextField usernameField;

    //Add functions for buttons below
    public void loginButton(ActionEvent e) throws IOException {
        //Get username from textfield
        String name = usernameField.getText();

        //Load main screen
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("main.fxml")));
        root = loader.load();

        //Initialize controller for main screen
        MainController mc = loader.getController();
        mc.displayName(name);

        //Get the same window the event occured in
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();

        //Create and set stage to the new scene
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
