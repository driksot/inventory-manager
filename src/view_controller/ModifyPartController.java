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
import static view_controller.MainScreenController.getModifyPartIndex;

/**
 * FXML Controller class
 *
 * @author derricksouthworth
 */
public class ModifyPartController implements Initializable {
    
    // Declare field
    private Stage stage;
    private Parent scene;
    private int partIndex = getModifyPartIndex();
    private String errorMessage = new String();
    private boolean isOutsourced;
    private int partId;
    
    @FXML
    private RadioButton inhouseRBtn;

    @FXML
    private RadioButton outsourcedRBtn;
    
    @FXML
    private Label modifyPartDynamicLbl;
    
    @FXML
    private TextField modifyPartDynamicTxt;

    @FXML
    private TextField partIdTxt;

    @FXML
    private TextField nameTxt;

    @FXML
    private TextField inventoryTxt;

    @FXML
    private TextField priceTxt;

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
     * If in-house radio button is selected set isOutsourced boolean
     * to false and modify dynamic label to Machine ID
     * @param event 
     */
    @FXML
    void handleInhouseRBtn(ActionEvent event) {
        isOutsourced = false;
        modifyPartDynamicLbl.setText("Machine ID");
    }
    
    /**
     * If outsourced radio button is selected set isOutsourced boolean
     * to true and modify dynamic label to Company Name
     * @param event 
     */
    @FXML
    void handleOutsourcedRBtn(ActionEvent event) {
        isOutsourced = true;
        modifyPartDynamicLbl.setText("Company Name");
    }

    /**
     * Seek user confirmation before canceling modifications and
     * switching scene to MainScreen
     * @param event
     * @throws IOException 
     */
    @FXML
    void handleModifyPartCancel(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirmation Needed");
        alert.setHeaderText("Confirm Cancellation");
        alert.setContentText("Are you sure you want to cancel modifying part " + nameTxt.getText() + "?");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK) {
            System.out.println("Ok selected. Part modification cancelled.");
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        } else {
            System.out.println("Cancel clicked. Please complete part modification.");
        }
    }

    /**
     * Validate part attributes and save modifications to chosen
     * Part object then switch scene to MainScreen
     * @param event
     * @throws IOException 
     */
    @FXML
    void handleModifyPartSave(ActionEvent event) throws IOException {
        String partId = partIdTxt.getText();
        String name = nameTxt.getText();
        String price = priceTxt.getText();
        String inStock = inventoryTxt.getText();
        String min = minTxt.getText();
        String max = maxTxt.getText();
        String partDynamicValue = modifyPartDynamicTxt.getText();
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
                    OutsourcedPart outsourcedPart = new OutsourcedPart(Integer.parseInt(partId), name, Double.parseDouble(price), Integer.parseInt(inStock), Integer.parseInt(min), Integer.parseInt(max), partDynamicValue);
                    Inventory.updatePart(partIndex, outsourcedPart);
                } else {
                    InhousePart inhousePart = new InhousePart(Integer.parseInt(partId), name, Double.parseDouble(price), Integer.parseInt(inStock), Integer.parseInt(min), Integer.parseInt(max), Integer.parseInt(partDynamicValue));
                    Inventory.updatePart(partIndex, inhousePart);
                }
                stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
            }
            
        } catch (NumberFormatException e) {
            System.out.println("Blank Fields");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error Adding Part!");
            alert.setHeaderText("Error");
            alert.setContentText("Form contains blank field.");
            alert.showAndWait();
        }

    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Part part = Inventory.getAllParts().get(partIndex);
        
        partId = Inventory.getAllParts().get(partIndex).getPartId();
        partIdTxt.setText(Integer.toString(part.getPartId()));
        nameTxt.setText(part.getName());
        inventoryTxt.setText(Integer.toString(part.getInStock()));
        priceTxt.setText(Double.toString(part.getPrice()));
        maxTxt.setText(Integer.toString(part.getMax()));
        minTxt.setText(Integer.toString(part.getMin()));
        
        if(part instanceof InhousePart) {
            modifyPartDynamicTxt.setText(Integer.toString(((InhousePart) Inventory.getAllParts().get(partIndex)).getMachineId()));
            modifyPartDynamicLbl.setText("Machine ID");
            inhouseRBtn.setSelected(true);
            isOutsourced = false;
        } else {
            modifyPartDynamicTxt.setText(((OutsourcedPart) Inventory.getAllParts().get(partIndex)).getCompanyName());
            modifyPartDynamicLbl.setText("Company Name");
            outsourcedRBtn.setSelected(true);
            isOutsourced = true;
        }
    }    
    
}
