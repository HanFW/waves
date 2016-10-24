/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.loan.entity;

import ejb.customer.entity.CustomerBasic;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;

/**
 *
 * @author hanfengwei
 */
@Entity
public class CustomerProperty implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerPropertyId;
    
    private String propertyAddress;
    private String propertyPostal;
    private ArrayList<String> propertyOwners;
    private String propertyType;
    private double propertyBuiltUpArea;
    private double propertyLandArea;
    private String propertyStatus;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date propertyTOPDate;
    private String propertyUsage;
    private String propertyTenureType;
    private int propertyTenureDuration;
    private int propertyTenureStartYear;
    
    @OneToOne(fetch = FetchType.EAGER, mappedBy = "customerProperty")
    private CustomerBasic customerBasic;
    
    public void create(String propertyAddress, String propertyPostal, ArrayList<String> propertyOwners, 
            String propertyType, double propertyBuiltUpArea, double propertyLandArea, String propertyStatus, 
            Date propertyTOPDate, String propertyUsage, String propertyTenureType, int propertyTenureDuration, 
            int propertyTenureStartYear, CustomerBasic customerBasic){
        this.setPropertyAddress(propertyAddress);
        this.setPropertyPostal(propertyPostal);
        this.setPropertyOwners(propertyOwners);
        this.setPropertyType(propertyType);
        this.setPropertyBuiltUpArea(propertyBuiltUpArea);
        this.setPropertyLandArea(propertyLandArea);
        this.setPropertyStatus(propertyStatus);
        this.setPropertyTOPDate(propertyTOPDate);
        this.setPropertyUsage(propertyUsage);
        this.setPropertyTenureType(propertyTenureType);
        this.setPropertyTenureDuration(propertyTenureDuration);
        this.setPropertyTenureStartYear(propertyTenureStartYear);
        this.setCustomerBasic(customerBasic);
    }

    public Long getCustomerPropertyId() {
        return customerPropertyId;
    }

    public void setCustomerPropertyId(Long customerPropertyId) {
        this.customerPropertyId = customerPropertyId;
    }

    public String getPropertyAddress() {
        return propertyAddress;
    }

    public void setPropertyAddress(String propertyAddress) {
        this.propertyAddress = propertyAddress;
    }

    public String getPropertyPostal() {
        return propertyPostal;
    }

    public void setPropertyPostal(String propertyPostal) {
        this.propertyPostal = propertyPostal;
    }

    public ArrayList<String> getPropertyOwners() {
        return propertyOwners;
    }

    public void setPropertyOwners(ArrayList<String> propertyOwners) {
        this.propertyOwners = propertyOwners;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public double getPropertyBuiltUpArea() {
        return propertyBuiltUpArea;
    }

    public void setPropertyBuiltUpArea(double propertyBuiltUpArea) {
        this.propertyBuiltUpArea = propertyBuiltUpArea;
    }

    public double getPropertyLandArea() {
        return propertyLandArea;
    }

    public void setPropertyLandArea(double propertyLandArea) {
        this.propertyLandArea = propertyLandArea;
    }

    public String getPropertyStatus() {
        return propertyStatus;
    }

    public void setPropertyStatus(String propertyStatus) {
        this.propertyStatus = propertyStatus;
    }

    public Date getPropertyTOPDate() {
        return propertyTOPDate;
    }

    public void setPropertyTOPDate(Date propertyTOPDate) {
        this.propertyTOPDate = propertyTOPDate;
    }

    public String getPropertyUsage() {
        return propertyUsage;
    }

    public void setPropertyUsage(String propertyUsage) {
        this.propertyUsage = propertyUsage;
    }

    public String getPropertyTenureType() {
        return propertyTenureType;
    }

    public void setPropertyTenureType(String propertyTenureType) {
        this.propertyTenureType = propertyTenureType;
    }

    public int getPropertyTenureDuration() {
        return propertyTenureDuration;
    }

    public void setPropertyTenureDuration(int propertyTenureDuration) {
        this.propertyTenureDuration = propertyTenureDuration;
    }

    public int getPropertyTenureStartYear() {
        return propertyTenureStartYear;
    }

    public void setPropertyTenureStartYear(int propertyTenureStartYear) {
        this.propertyTenureStartYear = propertyTenureStartYear;
    }

    public CustomerBasic getCustomerBasic() {
        return customerBasic;
    }

    public void setCustomerBasic(CustomerBasic customerBasic) {
        this.customerBasic = customerBasic;
    }
    
    
}
