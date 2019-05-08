/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 *
 * @author derricksouthworth
 */
public class Inventory {
    
    // Declare fields
    private static ObservableList<Product> products = FXCollections.observableArrayList();
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static int autoPartId = 0;
    private static int autoProductId = 0;
    
    // Declare methods
    
    /**
     * Add new product to observable list products
     * @param product 
     */
    public static void addProduct(Product product) {
        products.add(product);
    }
    
    /**
     * Remove product from observable list products
     * @param product 
     */
    public static void removeProduct(Product product) {
        products.remove(product);
    }
    
    /**
     * Accepts search parameter and if an ID or name matches input, that product is returned
     * @param searchItem
     * @return 
     */
    public static Product lookupProduct(String searchItem) {
        boolean isFound = false;
        for(Product p: products) {
            if(p.getName().contains(searchItem) || new Integer(p.getProductId()).toString().equals(searchItem)) return p;
            isFound = true;
        }
        if(isFound == false) {
            Product product = new Product(0, null, 0.0, 0, 0, 0);
            return product;
        }
        return null;
    }
    
    /**
     * Update product at given index
     * @param index
     * @param product 
     */
    public static void updateProduct(int index, Product product) {
        products.set(index, product);
    }
    
    /**
     * Getter for Product Observable List
     * @return 
     */
    public static ObservableList<Product> getProducts() {
        return products;
    }
    
    /**
     * Add new part to observable list allParts
     * @param part 
     */
    public static void addPart(Part part) {
        allParts.add(part);
    }
    
    /**
     * Removes part passed as parameter from allParts
     * @param part 
     */
    public static void deletePart(Part part) {
        allParts.remove(part);
    }
    
    /**
     * Accepts search parameter and if an ID or name matches input, that part is returned
     * @param searchItem
     * @return 
     */
    public static Part lookupPart(String searchItem) {
        for(Part p:allParts) {
            if(p.getName().contains(searchItem) || new Integer(p.getPartId()).toString().equals(searchItem)) return p;
        }
        return null;
    }
    
    /**
     * Update part at given index
     * @param index
     * @param part 
     */
    public static void updatePart(int index, Part part) {
        allParts.set(index, part);
    }
    
    /**
     * Getter for allParts Observable List
     * @return 
     */
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }
    
    /**
     * Method for incrementing part ID to be used to automatically
     * assign ID numbers to parts
     * @return 
     */
    public static int getAutoPartId() {
        autoPartId++;
        return autoPartId;
    }
    
    /**
     * Method for incrementing product ID to be used to automatically
     * assign ID numbers to products
     * @return 
     */
    public static int getAutoProductId() {
        autoProductId++;
        return autoProductId;
    }
    
}
