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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Inventory;
import model.Part;
import model.Product;

/**
 * FXML Controller class
 *
 * @author derricksouthworth
 */
public class MainScreenController implements Initializable {
    
     // Declare fields
    private Stage stage;
    private Parent scene;
    private static Part modifyPart;
    private static Product modifyProduct;
    private static int modifyPartIndex;
    private static int modifyProductIndex;
    
    // Declare methods
    public static int getModifyPartIndex() {
        return modifyPartIndex;
    }
    
    public static int getModifyProductIndex() {
        return modifyProductIndex;
    }

    @FXML
    private TableView<Part> partsTableView;

    @FXML
    private TableColumn<Part, Integer> partsIdCol;

    @FXML
    private TableColumn<Part, String> partsNameCol;

    @FXML
    private TableColumn<Part, Integer> partsInventoryCol;

    @FXML
    private TableColumn<Part, Double> partsPriceCol;


    @FXML
    private TableView<Product> productsTableView;

    @FXML
    private TableColumn<Product, Integer> productsIdCol;

    @FXML
    private TableColumn<Product, String> productsNameCol;

    @FXML
    private TableColumn<Product, Integer> productsInventoryCol;

    @FXML
    private TableColumn<Product, Double> productsPriceCol;
    
    @FXML
    private TextField partsSearchTxt;
    
    @FXML
    private TextField productsSearchTxt;
    
    /**
     * Method to add to button handler to switch to scene passed as source
     * @param event
     * @param source
     * @throws IOException 
     */
    private void displayScene(ActionEvent event, String source) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource(source));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * Ask user for confirmation before deleting selected part from list of parts.
     * @param event 
     */
    @FXML
    void handleDeletePart(ActionEvent event) {
        Part part = partsTableView.getSelectionModel().getSelectedItem();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Confirm Part Deletion?");
        alert.setContentText("Are you sure you want to delete part " + part.getName() + " from parts?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            System.out.println("Part deleted.");
            Inventory.deletePart(part);
        } else {
            System.out.println("Canceled part deletion.");
        }
    }

    /**
     * Ask user for confirmation before deleting selected product from list of products.
     * @param event 
     */
    @FXML
    void handleDeleteProduct(ActionEvent event) {
        Product product = productsTableView.getSelectionModel().getSelectedItem();
        
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Confirm Product Deletion?");
        alert.setContentText("Are you sure you want to delete product " + product.getName() + " from products?");
        Optional<ButtonType> result = alert.showAndWait();
        
        if (result.get() == ButtonType.OK) {
            Inventory.removeProduct(product);
            //updateProductTableView();
            System.out.println("Product " + product.getName() + " was removed.");
        } else {
            System.out.println("Product " + product.getName() + " was not removed.");
        }
    }

    /**
     * Switch scene to Add Part
     * @param event
     * @throws IOException 
     */
    @FXML
    void handleAddPart(ActionEvent event) throws IOException {
        displayScene(event, "AddPart.fxml");
    }

    /**
     * Switch scene to Add Product
     * @param event
     * @throws IOException 
     */
    @FXML
    void handleAddProduct(ActionEvent event) throws IOException {
        displayScene(event, "AddProduct.fxml");
    }

    /**
     * Changes scene to Modify Part screen and passes values of selected part
     * and its index
     * @param event
     * @throws IOException 
     */
    @FXML
    void handleModifyPart(ActionEvent event) throws IOException {
        modifyPart = partsTableView.getSelectionModel().getSelectedItem();
        modifyPartIndex = Inventory.getAllParts().indexOf(modifyPart);
        
        displayScene(event, "ModifyPart.fxml");
    }

    /**
     * Switch scene to Modify Product
     * @param event
     * @throws IOException 
     */
    @FXML
    void handleModifyProduct(ActionEvent event) throws IOException {
        modifyProduct = productsTableView.getSelectionModel().getSelectedItem();
        modifyProductIndex = Inventory.getProducts().indexOf(modifyProduct);
        
        displayScene(event, "ModifyProduct.fxml");
    }

    /**
     * Ask user for confirmation before exiting
     * @param event 
     */
    @FXML
    void handleExit(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirmation Needed");
        alert.setHeaderText("Confirm Exit");
        alert.setContentText("Are you sure you want to exit?");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK) {
            System.out.println("Ok selected. Program exited");
            System.exit(0);
        } else {
            System.out.println("Cancel clicked.");
        }
    }

    /**
     * Gets search text and inputs into lookupPart method to highlight desired part
     * @param event 
     */
    @FXML
    void handlePartsSearchBtn(ActionEvent event) {
        String x = partsSearchTxt.getText();
        partsTableView.getSelectionModel().select(Inventory.lookupPart(x));
    }

    /**
     * Gets search text and inputs into lookupProduct method to highlight desired product
     * @param event 
     */
    @FXML
    void handleProductsSearchBtn(ActionEvent event) {
        String x = productsSearchTxt.getText();
        productsTableView.getSelectionModel().select(Inventory.lookupProduct(x));
    }

    /**
     * Initializes the controller class and populate table views.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Populate parts table view
        partsTableView.setItems(Inventory.getAllParts());
        
        partsIdCol.setCellValueFactory(new PropertyValueFactory<>("partId"));
        partsNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partsInventoryCol.setCellValueFactory(new PropertyValueFactory<>("inStock"));
        partsPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        
        // Populate products table view
        productsTableView.setItems(Inventory.getProducts());
        
        productsIdCol.setCellValueFactory(new PropertyValueFactory<>("productId"));
        productsNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        productsInventoryCol.setCellValueFactory(new PropertyValueFactory<>("inStock"));
        productsPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    }    
    
}
