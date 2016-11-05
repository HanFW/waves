/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.loan.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author hanfengwei
 */
@Entity
public class CustomerDebt implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerDebtId;
    
    private String facilityType;
    private String financialInstitution;
    private double totalAmount;
    private double monthlyInstalment;
    private String collateralDetails;
    private int remainingTenure;
    private double currentInterest;

    public Long getCustomerDebtId() {
        return customerDebtId;
    }

    public void setCustomerDebtId(Long customerDebtId) {
        this.customerDebtId = customerDebtId;
    }

    public String getFacilityType() {
        return facilityType;
    }

    public void setFacilityType(String facilityType) {
        this.facilityType = facilityType;
    }

    public String getFinancialInstitution() {
        return financialInstitution;
    }

    public void setFinancialInstitution(String financialInstitution) {
        this.financialInstitution = financialInstitution;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public double getMonthlyInstalment() {
        return monthlyInstalment;
    }

    public void setMonthlyInstalment(double monthlyInstalment) {
        this.monthlyInstalment = monthlyInstalment;
    }

    public String getCollateralDetails() {
        return collateralDetails;
    }

    public void setCollateralDetails(String collateralDetails) {
        this.collateralDetails = collateralDetails;
    }

    public int getRemainingTenure() {
        return remainingTenure;
    }

    public void setRemainingTenure(int remainingTenure) {
        this.remainingTenure = remainingTenure;
    }

    public double getCurrentInterest() {
        return currentInterest;
    }

    public void setCurrentInterest(double currentInterest) {
        this.currentInterest = currentInterest;
    }
    
}