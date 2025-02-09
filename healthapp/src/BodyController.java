//Imports
import java.io.IOException;
import java.util.Objects;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.fxml.FXML;

public class BodyController {

    //Variables
    private Parent root;
    private Stage stage;
    private Scene scene;
    private Image skin = new Image("skin.png");
    private Image meat = new Image("meat.png");
    private Image bones = new Image("bones.png");

    @FXML
    private ImageView bodyImage;

    //Add functions for buttons below
    public void skinButton(ActionEvent e) {
        //System.out.println("Left has been pressed!");
        bodyImage.setImage(skin);
    }
    
    public void muscleButton(ActionEvent e) {
        //System.out.println("Middle has been pressed!");
        bodyImage.setImage(meat);
    }

    public void bonesButton(ActionEvent e) {
        // //System.out.println("Right has been pressed!");
        bodyImage.setImage(bones);
    }

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
