//Imports

import java.io.IOException;
import java.util.Objects;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SceneController implements Initializable {

    //Variables
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private ComboBox<String> modeComboBox;
    @FXML
    private ListView<String> prescriptionListView;
    @FXML
    private TextField prescriptionTextField;

    //Methods
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<String> modes = FXCollections.observableArrayList("Setup", "Modify", "End");
        modeComboBox.setItems(modes);
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

    public void switchSceneHome(ActionEvent e) throws IOException {
        //Load new scene
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("main.fxml")));

        //Get the same window the event occured in
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();

        //Create and set stage to the new scene
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchSceneReminder(ActionEvent e) throws IOException {
        // Load new scene
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("reminder.fxml")));

        // Get the same window the event occurred in
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();

        // Create and set stage to the new scene
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void addPrescription() {
        String prescription = prescriptionTextField.getText();
        if (!prescription.isEmpty()) {
            prescriptionListView.getItems().add(prescription);
            prescriptionTextField.clear();
        }
    }

    @FXML
    public void removePrescription() {
        String selectedPrescription = prescriptionListView.getSelectionModel().getSelectedItem();
        if (selectedPrescription != null) {
            prescriptionListView.getItems().remove(selectedPrescription);
        }
    }

    @FXML
    public void executeReminder() {
        String mode = modeComboBox.getValue();
        ObservableList<String> prescriptions = prescriptionListView.getItems();
        if (mode != null) {
            if (mode.equalsIgnoreCase("end") || !prescriptions.isEmpty()) {
                Reminder reminder = new Reminder(mode, prescriptions);
                reminder.start();
            } else {
                System.out.println("Please enter at least one prescription for setup or modify mode.");
            }
        }
    }
}
