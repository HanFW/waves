/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.loan.entity;

import ejb.customer.entity.CustomerBasic;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

/**
 *
 * @author hanfengwei
 */
@Entity
public class RefinancingApplication extends LoanApplication implements Serializable {

    private static final long serialVersionUID = 1L;

    private String existingFinancer;
    private double outstandingBalance;
    private int outstandingYear;
    private int outstandingMonth;
    private double totalCPFWithdrawal;
    
    @OneToOne(mappedBy = "refinancingApplication")
    private CustomerBasic customer;

    public void create(String loanType, double amountRequired, int periodRequired, String existingFinancer, double outstandingBalance,
            int outstandingYear, int outstandingMonth, double totalCPFWithdrawal, String employmentStatus) {
        this.setApplicationDate(new Date());
        this.setApplicationStatus("pending");
        this.setLoanType(loanType);
        this.setAmountRequired(amountRequired);
        this.setPeriodRequired(periodRequired);
        this.setExistingFinancer(existingFinancer);
        this.setOutstandingBalance(outstandingBalance);
        this.setOutstandingYear(outstandingYear);
        this.setOutstandingMonth(outstandingMonth);
        this.setTotalCPFWithdrawal(totalCPFWithdrawal);

        HashMap docs = new HashMap();
        docs.put("identification", true);
        docs.put("otp", false);
        docs.put("purchaseAgreement", false);
        docs.put("existingLoan", true);
        docs.put("cpfWithdrawal", true);
        docs.put("tenancy", false);
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

    public String getExistingFinancer() {
        return existingFinancer;
    }

    public void setExistingFinancer(String existingFinancer) {
        this.existingFinancer = existingFinancer;
    }

    public double getOutstandingBalance() {
        return outstandingBalance;
    }

    public void setOutstandingBalance(double outstandingBalance) {
        this.outstandingBalance = outstandingBalance;
    }

    public int getOutstandingYear() {
        return outstandingYear;
    }

    public void setOutstandingYear(int outstandingYear) {
        this.outstandingYear = outstandingYear;
    }

    public int getOutstandingMonth() {
        return outstandingMonth;
    }

    public void setOutstandingMonth(int outstandingMonth) {
        this.outstandingMonth = outstandingMonth;
    }

    public double getTotalCPFWithdrawal() {
        return totalCPFWithdrawal;
    }

    public void setTotalCPFWithdrawal(double totalCPFWithdrawal) {
        this.totalCPFWithdrawal = totalCPFWithdrawal;
    }

    public CustomerBasic getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerBasic customer) {
        this.customer = customer;
    }

    
}
