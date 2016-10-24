/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.loan.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;

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

    public void create(double amountRequired, int periodRequired, String existingFinancer, double outstandingBalance,
            int outstandingYear, int outstandingMonth, double totalCPFWithdrawal){
        this.setApplicationDate(new Date());
        this.setApplicationStatus("pending");
        this.setAmountRequired(amountRequired);
        this.setPeriodRequired(periodRequired);
        this.setExistingFinancer(existingFinancer);
        this.setOutstandingBalance(outstandingBalance);
        this.setOutstandingYear(outstandingYear);
        this.setOutstandingMonth(outstandingMonth);
        this.setTotalCPFWithdrawal(totalCPFWithdrawal);
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
    
    
}
