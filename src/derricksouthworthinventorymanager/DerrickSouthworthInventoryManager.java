/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package derricksouthworthinventorymanager;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.InhousePart;
import model.Inventory;
import model.OutsourcedPart;
import model.Part;
import model.Product;

/**
 *
 * @author derricksouthworth
 */
public class DerrickSouthworthInventoryManager extends Application {
    
    private static int partId;
    private static ObservableList<Part> addParts = FXCollections.observableArrayList();
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view_controller/MainScreen.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Part part1 = new InhousePart(Inventory.getAutoPartId(), "Cog", 3.75, 14, 4, 40, 12);
        Part part2 = new OutsourcedPart(Inventory.getAutoPartId(), "Spring", 2.30, 7, 5, 20, "Springs'R'Us");
        Part part3 = new OutsourcedPart(Inventory.getAutoPartId(), "Screw", 0.11, 280, 100, 1000, "Fastener's Unlimited");
        
        Inventory.addPart(part1);
        Inventory.addPart(part2);
        Inventory.addPart(part3);
        
        Product product1 = new Product(Inventory.getAutoProductId(), "Clock", 7.45, 7, 2, 20);
        addParts.add(part1);
        addParts.add(part2);
        addParts.add(part3);
        product1.setAssociatedParts(addParts);
        
        Inventory.addProduct(product1);
        
        launch(args);
    }
    
}
