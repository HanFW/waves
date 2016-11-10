/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.card.entity;

import ejb.loan.entity.LoanRepaymentTransaction;
import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

/**
 *
 * @author aaa
 */
@Entity
public class PrincipalCard extends CreditCard implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private double creditLimit;
    private double outstandingBalance; 
    private double currentExpense;
    private double overduePrincipal;
    private double overdueInterest;
    private int defaultMonths;
       
    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER, mappedBy = "principalCard")
    private List<SupplementaryCard> supplementaryCards;

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER, mappedBy = "principalCard")
    private List<CreditCardTransaction> creditCardTransactions;
    
    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER, mappedBy = "principalCard")
    private List<CreditCardReport> creditCardReport;
    
    @OneToMany (cascade = {CascadeType.ALL}, fetch = FetchType.EAGER, mappedBy ="principalCard")
    private List<LoanRepaymentTransaction> loanRepaymentTransactions;
    
    public List<SupplementaryCard> getSupplementaryCards() {
        return supplementaryCards;
    }
    
    public void addRepaymentTransactions(LoanRepaymentTransaction transaction){
        loanRepaymentTransactions.add(transaction);
    }

    public void setSupplementaryCards(List<SupplementaryCard> supplementaryCards) {
        this.supplementaryCards = supplementaryCards;
    }
    
    public void addSupplementaryCard (SupplementaryCard sc){
        supplementaryCards.add(sc);
    }

    public double getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(double creditLimit) {
        this.creditLimit = creditLimit;
    }

    public double getOutstandingBalance() {
        return outstandingBalance;
    }

    public void setOutstandingBalance(double outstandingBalance) {
        this.outstandingBalance = outstandingBalance;
    }

    public List<CreditCardTransaction> getCreditCardTransactions() {
        return creditCardTransactions;
    }

    public void setCreditCardTransactions(List<CreditCardTransaction> creditCardTransactions) {
        this.creditCardTransactions = creditCardTransactions;
    }
    
    public void addTransaction(CreditCardTransaction transaction){
        this.creditCardTransactions.add(transaction);
    }
    
    public void removeTransaction(CreditCardTransaction transaction){
        this.creditCardTransactions.remove(transaction);
    }

    public List<CreditCardReport> getCreditCardReport() {
        return creditCardReport;
    }

    public void setCreditCardReport(List<CreditCardReport> creditCardReport) {
        this.creditCardReport = creditCardReport;
    }

    public double getCurrentExpense() {
        return currentExpense;
    }

    public void setCurrentExpense(double currentExpense) {
        this.currentExpense = currentExpense;
    }

    public double getOverduePrincipal() {
        return overduePrincipal;
    }

    public void setOverduePrincipal(double overduePrincipal) {
        this.overduePrincipal = overduePrincipal;
    }

    public double getOverdueInterest() {
        return overdueInterest;
    }

    public void setOverdueInterest(double overdueInterest) {
        this.overdueInterest = overdueInterest;
    }

    public int getDefaultMonths() {
        return defaultMonths;
    }

    public void setDefaultMonths(int defaultMonths) {
        this.defaultMonths = defaultMonths;
    }

    public List<LoanRepaymentTransaction> getLoanRepaymentTransactions() {
        return loanRepaymentTransactions;
    }

    public void setLoanRepaymentTransactions(List<LoanRepaymentTransaction> loanRepaymentTransactions) {
        this.loanRepaymentTransactions = loanRepaymentTransactions;
    }
    
}
