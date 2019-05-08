/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view_controller;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.InhousePart;
import model.Inventory;
import model.OutsourcedPart;
import model.Part;

/**
 * FXML Controller class
 *
 * @author derricksouthworth
 */
public class AddPartController implements Initializable {
    
    // Declare fields
    private Stage stage;
    private Parent scene;
    private boolean isOutsourced = true;
    private String errorMessage = new String();
    private int partId;
    
    @FXML
    private RadioButton inhouseRBtn;

    @FXML
    private RadioButton outsourcedRBtn;
    
    @FXML
    private Label addPartDynamicLbl;

    @FXML
    private TextField partIdTxt;

    @FXML
    private TextField nameTxt;

    @FXML
    private TextField inventoryTxt;

    @FXML
    private TextField priceTxt;
    
    @FXML
    private TextField addPartDynamicTxt;

    @FXML
    private TextField maxTxt;

    @FXML
    private TextField minTxt;
    
    /**
     * Method to add to button handler to switch to scene passed as source
     * @param event
     * @param source
     * @throws IOException 
     */
    @FXML
    private void displayScene(ActionEvent event, String source) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource(source));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * Ask user for confirmation before canceling part addition
     * and switching scene to Main Screen
     * @param event
     * @throws IOException 
     */
    @FXML
    void handleAddPartCancel(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirmation Needed");
        alert.setHeaderText("Confirm Cancelation");
        alert.setContentText("Are you sure you want to cancel adding part?");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK) {
            System.out.println("Ok selected. Part addition canceled.");
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        } else {
            System.out.println("Cancel clicked.");
        }
    }
    
    /**
     * If in-house radio button is selected set isOutsourced boolean
     * to false and modify dynamic label to Machine ID
     * @param event 
     */
    @FXML
    void handleInhouseRBtn(ActionEvent event) {
        isOutsourced = false;
        addPartDynamicLbl.setText("Machine ID");
    }

    /**
     * If outsourced radio button is selected set isOutsourced boolean
     * to true and modify dynamic label to Company Name
     * @param event 
     */
    @FXML
    void handleOutsourcedRBtn(ActionEvent event) {
        isOutsourced = true;
        addPartDynamicLbl.setText("Company Name");
    }

    /**
     * Validate given part parameters.  If valid, add part to inventory,
     * else give user an error message explaining why the part is invalid.
     * @param event
     * @throws IOException 
     */
    @FXML
    void handleAddPartSave(ActionEvent event) throws IOException {
        String name = nameTxt.getText();
        String price = priceTxt.getText();
        String inStock = inventoryTxt.getText();
        String min = minTxt.getText();
        String max = maxTxt.getText();
        String partDynamicValue = addPartDynamicTxt.getText();
        errorMessage = "";
        
        try {
            errorMessage = Part.isValidPart(name, Double.parseDouble(price), Integer.parseInt(inStock), Integer.parseInt(min), Integer.parseInt(max), errorMessage);
            if(errorMessage.length() > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error Adding Part!");
                alert.setHeaderText("Error!");
                alert.setContentText(errorMessage);
                alert.showAndWait();
            } else {
                if(isOutsourced == true) {
                    OutsourcedPart outsourcedPart = new OutsourcedPart(partId, name, Double.parseDouble(price), Integer.parseInt(inStock), Integer.parseInt(min), Integer.parseInt(max), partDynamicValue);
                    Inventory.addPart(outsourcedPart);
                } else {
                    InhousePart inhousePart = new InhousePart(partId, name, Double.parseDouble(price), Integer.parseInt(inStock), Integer.parseInt(min), Integer.parseInt(max), Integer.parseInt(partDynamicValue));
                    Inventory.addPart(inhousePart);
                }
                stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
            }
            
        } catch (NumberFormatException e) {
            System.out.println("Form contains blank field.");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error Adding Part!");
            alert.setHeaderText("Error!");
            alert.setContentText("Form contains blank field.");
            alert.showAndWait();
        }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Increment part ID
        partId = Inventory.getAutoPartId();
        outsourcedRBtn.setSelected(true);
    }    
    
}
