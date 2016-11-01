/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.loan.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import javax.persistence.Entity;

/**
 *
 * @author hanfengwei
 */
@Entity
public class CarLoanApplication extends LoanApplication implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String isNewCar;
    private String make;
    private String model;
    private String chassis;
    private double purchasePrice;
    private int yearOfManufacture;
    
    public void create(double amountRequired, int periodRequired, String employmentStatus,
            String isNewCar, String make, String model, String chassis, double purchasePrice, int yearOfManufacture){
        this.setAmountRequired(amountRequired);
        this.setPeriodRequired(periodRequired);
        this.setLoanType("Car Loan");
        this.setApplicationDate(new Date());
        this.setApplicationStatus("pending");
        
        this.setIsNewCar(isNewCar);
        this.setMake(make);
        this.setModel(model);
        this.setChassis(chassis);
        this.setPurchasePrice(purchasePrice);
        this.setYearOfManufacture(yearOfManufacture);
        
        HashMap docs = new HashMap();
        docs.put("identification", true);
        docs.put("order", true);
        docs.put("registration", true);
        if (employmentStatus.equals("Self-Employed")) {
            docs.put("selfEmployedTax", true);
            docs.put("employeeTax", false);
        } else {
            docs.put("selfEmployedTax", false);
            docs.put("employeeTax", true);
        } 
        this.setUploads(docs);
    }

    public String getIsNewCar() {
        return isNewCar;
    }

    public void setIsNewCar(String isNewCar) {
        this.isNewCar = isNewCar;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getChassis() {
        return chassis;
    }

    public void setChassis(String chassis) {
        this.chassis = chassis;
    }

    public double getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public int getYearOfManufacture() {
        return yearOfManufacture;
    }

    public void setYearOfManufacture(int yearOfManufacture) {
        this.yearOfManufacture = yearOfManufacture;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }
    
    
}
