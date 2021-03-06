/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.loan.entity;

import ejb.card.entity.PrincipalCard;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;

/**
 *
 * @author hanfengwei
 */
@Entity
public class LoanRepaymentTransaction implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(javax.persistence.TemporalType.DATE)
    private Date transactionDate;
    private Long transactionMillis;
    private double accountBalance;
    private String description;
    private double accountDebit;
    private double accountCredit;
    
    @ManyToOne(fetch = FetchType.EAGER)
    LoanRepaymentAccount loanRepaymentAccount;
    
    @ManyToOne(fetch = FetchType.EAGER)
    LoanPayableAccount loanPayableAccount;
    
    @ManyToOne(fetch = FetchType.EAGER)
    PrincipalCard principalCard;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public double getAccountDebit() {
        return accountDebit;
    }

    public void setAccountDebit(double accountDebit) {
        this.accountDebit = accountDebit;
    }

    public double getAccountCredit() {
        return accountCredit;
    }

    public void setAccountCredit(double accountCredit) {
        this.accountCredit = accountCredit;
    }

    public Long getTransactionMillis() {
        return transactionMillis;
    }

    public void setTransactionMillis(Long transactionMillis) {
        this.transactionMillis = transactionMillis;
    }

    public double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(double accountBalance) {
        this.accountBalance = accountBalance;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LoanRepaymentAccount getLoanRepaymentAccount() {
        return loanRepaymentAccount;
    }

    public void setLoanRepaymentAccount(LoanRepaymentAccount loanRepaymentAccount) {
        this.loanRepaymentAccount = loanRepaymentAccount;
    }

    public LoanPayableAccount getLoanPayableAccount() {
        return loanPayableAccount;
    }

    public void setLoanPayableAccount(LoanPayableAccount loanPayableAccount) {
        this.loanPayableAccount = loanPayableAccount;
    }

    public PrincipalCard getPrincipalCard() {
        return principalCard;
    }

    public void setPrincipalCard(PrincipalCard principalCard) {
        this.principalCard = principalCard;
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
        if (!(object instanceof LoanRepaymentTransaction)) {
            return false;
        }
        LoanRepaymentTransaction other = (LoanRepaymentTransaction) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ejb.loan.entity.LoanRepaymentTransaction[ id=" + id + " ]";
    }

}
