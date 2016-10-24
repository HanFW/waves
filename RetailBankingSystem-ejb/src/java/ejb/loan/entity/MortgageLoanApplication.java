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
import javax.persistence.Temporal;

/**
 *
 * @author hanfengwei
 */
@Entity
public class MortgageLoanApplication extends LoanApplication implements Serializable {

    private static final long serialVersionUID = 1L;

    private double propertyPurchasePrice;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date propertyDateOfPurchase;
    private String propertySource;
    private String propertyWithOTP;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date propertyOTPDate;
    private String propertyWithTenancy;
    private double propertyTenancyIncome;
    private int propertyTenancyExpiryYear;
    private String withBenifits;
    private double benefitsFromVendor;
    private double cashDownPayment;
    private double cpfDownPayment;

    public void create(String loanType, double amountRequired, int periodRequired, double propertyPurchasePrice, Date propertyDateOfPurchase,
            String propertySource, String propertyWithOTP, Date propertyOTPDate, String propertyWithTenancy,
            double propertyTenancyIncome, int propertyTenancyExpiryYear, String withBenifits, double benefitsFromVendor,
            double cashDownPayment, double cpfDownPayment, String employmentStatus) {
        this.setApplicationDate(new Date());
        this.setApplicationStatus("pending");
        this.setLoanType(loanType);
        this.setAmountRequired(amountRequired);
        this.setPeriodRequired(periodRequired);
        this.setPropertyPurchasePrice(propertyPurchasePrice);
        this.setPropertyDateOfPurchase(propertyDateOfPurchase);
        this.setPropertySource(propertySource);
        this.setPropertyWithOTP(propertyWithOTP);
        this.setPropertyOTPDate(propertyOTPDate);
        this.setPropertyWithTenancy(propertyWithTenancy);
        this.setPropertyTenancyIncome(propertyTenancyIncome);
        this.setPropertyTenancyExpiryYear(propertyTenancyExpiryYear);
        this.setWithBenifits(withBenifits);
        this.setBenefitsFromVendor(benefitsFromVendor);
        this.setCashDownPayment(cashDownPayment);
        this.setCpfDownPayment(cpfDownPayment);
        HashMap docs = new HashMap();
        docs.put("identification", true);
        docs.put("otp", true);
        docs.put("purchaseAgreement", true);
        docs.put("existingLoan", false);
        docs.put("cpfWithdrawal", false);

        if (propertyWithTenancy.equals("yes")) {
            docs.put("tenancy", true);
        } else {
            docs.put("tenancy", false);
        }

        docs.put("evidenceOfSale", true);

        if (employmentStatus.equals("Self-Employed")) {
            docs.put("selfEmployedTax", true);
            docs.put("employeeTax", false);
            docs.put("employeeCPF", false);
        } else if (employmentStatus.equals("Employee")) {
            docs.put("selfEmployedTax", false);
            docs.put("employeeTax", true);
            docs.put("employeeCPF", true);
        } else {
            docs.put("selfEmployedTax", false);
            docs.put("employeeTax", false);
            docs.put("employeeCPF", false);
        }

        this.setUploads(docs);
    }

    public double getPropertyPurchasePrice() {
        return propertyPurchasePrice;
    }

    public void setPropertyPurchasePrice(double propertyPurchasePrice) {
        this.propertyPurchasePrice = propertyPurchasePrice;
    }

    public Date getPropertyDateOfPurchase() {
        return propertyDateOfPurchase;
    }

    public void setPropertyDateOfPurchase(Date propertyDateOfPurchase) {
        this.propertyDateOfPurchase = propertyDateOfPurchase;
    }

    public String getPropertySource() {
        return propertySource;
    }

    public void setPropertySource(String propertySource) {
        this.propertySource = propertySource;
    }

    public String getPropertyWithOTP() {
        return propertyWithOTP;
    }

    public void setPropertyWithOTP(String propertyWithOTP) {
        this.propertyWithOTP = propertyWithOTP;
    }

    public Date getPropertyOTPDate() {
        return propertyOTPDate;
    }

    public void setPropertyOTPDate(Date propertyOTPDate) {
        this.propertyOTPDate = propertyOTPDate;
    }

    public String getPropertyWithTenancy() {
        return propertyWithTenancy;
    }

    public void setPropertyWithTenancy(String propertyWithTenancy) {
        this.propertyWithTenancy = propertyWithTenancy;
    }

    public double getPropertyTenancyIncome() {
        return propertyTenancyIncome;
    }

    public void setPropertyTenancyIncome(double propertyTenancyIncome) {
        this.propertyTenancyIncome = propertyTenancyIncome;
    }

    public int getPropertyTenancyExpiryYear() {
        return propertyTenancyExpiryYear;
    }

    public void setPropertyTenancyExpiryYear(int propertyTenancyExpiryYear) {
        this.propertyTenancyExpiryYear = propertyTenancyExpiryYear;
    }

    public String getWithBenifits() {
        return withBenifits;
    }

    public void setWithBenifits(String withBenifits) {
        this.withBenifits = withBenifits;
    }

    public double getBenefitsFromVendor() {
        return benefitsFromVendor;
    }

    public void setBenefitsFromVendor(double benefitsFromVendor) {
        this.benefitsFromVendor = benefitsFromVendor;
    }

    public double getCashDownPayment() {
        return cashDownPayment;
    }

    public void setCashDownPayment(double cashDownPayment) {
        this.cashDownPayment = cashDownPayment;
    }

    public double getCpfDownPayment() {
        return cpfDownPayment;
    }

    public void setCpfDownPayment(double cpfDownPayment) {
        this.cpfDownPayment = cpfDownPayment;
    }

}
