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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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
import static view_controller.MainScreenController.getModifyProductIndex;

/**
 * FXML Controller class
 *
 * @author derricksouthworth
 */
public class ModifyProductController implements Initializable {
    
    // Declare fields
    private Stage stage;
    private Parent scene;
    private ObservableList<Part> addParts = FXCollections.observableArrayList();
    private String errorMessage = new String();
    private int productId;
    private int productIndex = getModifyProductIndex();
    
    @FXML
    private TextField minTxt;

    @FXML
    private TextField maxTxt;

    @FXML
    private TextField productIdTxt;

    @FXML
    private TextField nameTxt;

    @FXML
    private TextField inventoryTxt;

    @FXML
    private TextField priceTxt;

    @FXML
    private TextField productSearchTxt;

    @FXML
    private TableView<Part> addProductTableView;

    @FXML
    private TableColumn<Part, Integer> addProductIdCol;

    @FXML
    private TableColumn<Part, String> addProductNameCol;

    @FXML
    private TableColumn<Part, Integer> addProductInventoryCol;

    @FXML
    private TableColumn<Part, Double> addProductPriceCol;

    @FXML
    private TableView<Part> deleteProductTableView;

    @FXML
    private TableColumn<Part, Integer> deleteProductIdCol;

    @FXML
    private TableColumn<Part, String> deleteProductNameCol;

    @FXML
    private TableColumn<Part, Integer> deleteProductInventoryCol;

    @FXML
    private TableColumn<Part, Double> deleteProductPriceCol;
    
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
     * Method to add values of addParts to the bottom table view of the scene.
     */
    public void updateDeleteProductTableView() {
        deleteProductTableView.setItems(addParts);
        
        deleteProductIdCol.setCellValueFactory(new PropertyValueFactory<>("partId"));
        deleteProductNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        deleteProductInventoryCol.setCellValueFactory(new PropertyValueFactory<>("inStock"));
        deleteProductPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /**
     * Ask user for confirmation before deleting selected part from current product.
     * @param event 
     */
    @FXML
    void handleDeleteProduct(ActionEvent event) {
        Part part = deleteProductTableView.getSelectionModel().getSelectedItem();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Confirm Part Deletion!");
        alert.setContentText("Are you sure you want to delete part " + part.getName() + " from parts?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            System.out.println("Part deleted.");
            addParts.remove(part);
        } else {
            System.out.println("Canceled part deletion.");
        }
    }
    
    /**
     * Add selected part from top table view to bottom table view in order to create
     * new product
     * @param event 
     */
    @FXML
    void handleAddProduct(ActionEvent event) {
        Part part = addProductTableView.getSelectionModel().getSelectedItem();
        addParts.add(part);
        updateDeleteProductTableView();   
    }

    /**
     * Ask user for confirmation before canceling product modification
     * and switching scene to Main Screen
     * @param event
     * @throws IOException 
     */
    @FXML
    void handleCancelProduct(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirmation Needed");
        alert.setHeaderText("Confirm Cancelation");
        alert.setContentText("Are you sure you want to cancel modifying product?");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK) {
            System.out.println("Ok selected. Product modification canceled.");
            displayScene(event, "MainScreen.fxml");
        } else {
            System.out.println("Cancel clicked.");
        }
    }

    /**
     * Validate given product parameters.  If valid, update product in inventory,
     * else give user an error message explaining why the product is invalid.
     * @param event
     * @throws IOException 
     */
    @FXML
    void handleSaveProduct(ActionEvent event) throws IOException {
        String name = nameTxt.getText();
        String price = priceTxt.getText();
        String inStock = inventoryTxt.getText();
        String min = minTxt.getText();
        String max = maxTxt.getText();
        errorMessage = "";
        
        try {
            errorMessage = Product.isValidProduct(name, Double.parseDouble(price), Integer.parseInt(inStock), Integer.parseInt(min), Integer.parseInt(max), addParts, errorMessage);
            if(errorMessage.length() > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error Adding Part!");
                alert.setHeaderText("Error!");
                alert.setContentText(errorMessage);
                alert.showAndWait();
            } else {
                System.out.println("Added product: " + name);
                Product product = new Product(productId, name, Double.parseDouble(price), Integer.parseInt(inStock), Integer.parseInt(min), Integer.parseInt(max));
                product.setAssociatedParts(addParts);
                Inventory.updateProduct(productIndex, product);
                displayScene(event, "MainScreen.fxml");
            }
        } catch (NumberFormatException e) {
            System.out.println("Form contains blank field.");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error Adding Product!");
            alert.setHeaderText("Error!");
            alert.setContentText("Form contains blank field.");
            alert.showAndWait();
        }
    }

    /**
     * Gets search text and inputs into lookupAssociatedPart method to highlight desired part
     * @param event 
     */
    @FXML
    void handleSearchProduct(ActionEvent event) {
        String x = productSearchTxt.getText();
        addProductTableView.getSelectionModel().select(Inventory.lookupPart(x));
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Populate add product table view
        addProductTableView.setItems(Inventory.getAllParts());
        
        addProductIdCol.setCellValueFactory(new PropertyValueFactory<>("partId"));
        addProductNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        addProductInventoryCol.setCellValueFactory(new PropertyValueFactory<>("inStock"));
        addProductPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        
        // Populate modify product form
        Product product = Inventory.getProducts().get(productIndex);
        
        productId = Inventory.getProducts().get(productIndex).getProductId();
        productIdTxt.setText(Integer.toString(product.getProductId()));
        nameTxt.setText(product.getName());
        inventoryTxt.setText(Integer.toString(product.getInStock()));
        priceTxt.setText(Double.toString(product.getPrice()));
        maxTxt.setText(Integer.toString(product.getMax()));
        minTxt.setText(Integer.toString(product.getMin()));
        
        // Populate delete product table view
        addParts = product.getAssociatedParts();
        updateDeleteProductTableView();
    }    
    
}
