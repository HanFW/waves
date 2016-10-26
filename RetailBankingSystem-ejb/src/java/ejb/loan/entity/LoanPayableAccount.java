/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.loan.entity;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Date;
import javax.persistence.CascadeType;
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
public class LoanPayableAccount implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String accountNumber;
    private double initialAmount;
    private double accountBalance;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date startDate;
    private String accountStatus;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date endDate;
    private double overdueBalance;
    
    @OneToOne(mappedBy = "loanPayableAccount")
    private LoanApplication loanApplication;
    
    @OneToOne(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    private LoanRepaymentAccount loanRepaymentAccount;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LoanApplication getLoanApplication() {
        return loanApplication;
    }

    public void setLoanApplication(LoanApplication loanApplication) {
        this.loanApplication = loanApplication;
    }

    public LoanRepaymentAccount getLoanRepaymentAccount() {
        return loanRepaymentAccount;
    }

    public void setLoanRepaymentAccount(LoanRepaymentAccount loanRepaymentAccount) {
        this.loanRepaymentAccount = loanRepaymentAccount;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public double getInitialAmount() {
        return initialAmount;
    }

    public void setInitialAmount(double initialAmount) {
        this.initialAmount = initialAmount;
    }

    public double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(double accountBalance) {
        this.accountBalance = accountBalance;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public double getOverdueBalance() {
        return overdueBalance;
    }

    public void setOverdueBalance(double overdueBalance) {
        this.overdueBalance = overdueBalance;
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
        if (!(object instanceof LoanPayableAccount)) {
            return false;
        }
        LoanPayableAccount other = (LoanPayableAccount) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ejb.loan.entity.LoanPayableAccount[ id=" + id + " ]";
    }
    
}
