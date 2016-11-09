/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.loan.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 *
 * @author hanfengwei
 */
@Entity
public class LoanRepaymentAccount implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String accountNumber;
    private double currentPrincipal;
    private double currentInterest;
    private double currentInstalment;
    private double totalPrincipal;
    private double totalInterest;
    private double overdueBalance;
    private double fees;
    private String paymentStatus;
    private int repaymentMonths;
    private String depositAccountNumber;
    private double accountBalance;
    private int defaultMonths;
    
    @OneToOne(mappedBy = "loanRepaymentAccount")
    private LoanPayableAccount loanPayableAccount;
    
    @OneToMany (cascade = {CascadeType.ALL}, fetch = FetchType.EAGER, mappedBy ="loanRepaymentAccount")
    private List<LoanRepaymentTransaction> loanRepaymentTransactions;
    
    @OneToOne(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    private LoanStatement loanStatement;

    public LoanStatement getLoanStatement() {
        return loanStatement;
    }

    public void setLoanStatement(LoanStatement loanStatement) {
        this.loanStatement = loanStatement;
    }    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LoanPayableAccount getLoanPayableAccount() {
        return loanPayableAccount;
    }

    public void setLoanPayableAccount(LoanPayableAccount loanPayableAccount) {
        this.loanPayableAccount = loanPayableAccount;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public double getOverdueBalance() {
        return overdueBalance;
    }

    public void setOverdueBalance(double overdueBalance) {
        this.overdueBalance = overdueBalance;
    }

    public double getFees() {
        return fees;
    }

    public void setFees(double fees) {
        this.fees = fees;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public List<LoanRepaymentTransaction> getLoanRepaymentTransactions() {
        return loanRepaymentTransactions;
    }

    public void setLoanRepaymentTransactions(List<LoanRepaymentTransaction> loanRepaymentTransactions) {
        this.loanRepaymentTransactions = loanRepaymentTransactions;
    }

    public int getRepaymentMonths() {
        return repaymentMonths;
    }

    public void setRepaymentMonths(int repaymentMonths) {
        this.repaymentMonths = repaymentMonths;
    }

    public String getDepositAccountNumber() {
        return depositAccountNumber;
    }

    public void setDepositAccountNumber(String depositAccountNumber) {
        this.depositAccountNumber = depositAccountNumber;
    }

    public double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(double accountBalance) {
        this.accountBalance = accountBalance;
    }

    public double getCurrentPrincipal() {
        return currentPrincipal;
    }

    public void setCurrentPrincipal(double currentPrincipal) {
        this.currentPrincipal = currentPrincipal;
    }

    public double getCurrentInterest() {
        return currentInterest;
    }

    public void setCurrentInterest(double currentInterest) {
        this.currentInterest = currentInterest;
    }

    public double getCurrentInstalment() {
        return currentInstalment;
    }

    public void setCurrentInstalment(double currentInstalment) {
        this.currentInstalment = currentInstalment;
    }

    public double getTotalPrincipal() {
        return totalPrincipal;
    }

    public void setTotalPrincipal(double totalPrincipal) {
        this.totalPrincipal = totalPrincipal;
    }

    public double getTotalInterest() {
        return totalInterest;
    }

    public void setTotalInterest(double totalInterest) {
        this.totalInterest = totalInterest;
    }

    public int getDefaultMonths() {
        return defaultMonths;
    }

    public void setDefaultMonths(int defaultMonths) {
        this.defaultMonths = defaultMonths;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LoanRepaymentAccount)) {
            return false;
        }
        LoanRepaymentAccount other = (LoanRepaymentAccount) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ejb.loan.entity.LoanRepaymentAccount[ id=" + id + " ]";
    }
    
}
