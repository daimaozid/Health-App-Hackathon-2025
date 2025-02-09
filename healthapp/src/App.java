//Imports
import java.util.Objects;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
 
public class App extends Application {
    public static void main(String[] args) {
        //Launches the window for the program
        launch(args);
    }
    
    @Override
    /**
     * Start method for the program screen
     * Think of it as the main method for the program
     * primaryStage is the main window for the program
     */
    public void start(Stage primaryStage) {

        //Initiates the main screen of the app
        Parent root = new AnchorPane();

        //Try catch to catch IOException that may be thrown by FXMLLOADER
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("main.fxml")));
        } catch (Exception e) {
            System.err.println("THIS FAILED!");
        }

        //Variable that tracks the current scene
        Scene currentScene = new Scene(root);

        //Stage = Window of our program
        //Sets the title of the window to Hello World
        primaryStage.setTitle("DocuMed");

        //Scene = Screen inside program window
        //1st number = width, 2nd number = height
        primaryStage.setScene(currentScene);

        //Displays the scene
        primaryStage.show();
    }

    
    
}