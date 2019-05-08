/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author derricksouthworth
 */
public class OutsourcedPart extends Part {
    
    // Declare fields
    private String companyName;

    // Constructor
    public OutsourcedPart(int partId, String name, double price, int inStock, int min, int max, String companyName) {
        super(partId, name, price, inStock, min, max);
        this.companyName = companyName;
    }
    
    // Getter
    public String getCompanyName() {
        return companyName;
    }
    
    // Setter
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    
}
