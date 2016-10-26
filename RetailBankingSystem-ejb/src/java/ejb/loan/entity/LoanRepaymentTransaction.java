/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.loan.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.CascadeType;
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
    private String transactionCode;
    private String transactionRefrence;
    private String accountDebit;
    private String accountCredit;
    
    
    @ManyToOne (cascade = {CascadeType.PERSIST}, fetch = FetchType.EAGER)
    private LoanRepaymentAccount loanRepaymentAccount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LoanRepaymentAccount getLoanRepaymentAccount() {
        return loanRepaymentAccount;
    }

    public void setLoanRepaymentAccount(LoanRepaymentAccount loanRepaymentAccount) {
        this.loanRepaymentAccount = loanRepaymentAccount;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getTransactionCode() {
        return transactionCode;
    }

    public void setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode;
    }

    public String getTransactionRefrence() {
        return transactionRefrence;
    }

    public void setTransactionRefrence(String transactionRefrence) {
        this.transactionRefrence = transactionRefrence;
    }

    public String getAccountDebit() {
        return accountDebit;
    }

    public void setAccountDebit(String accountDebit) {
        this.accountDebit = accountDebit;
    }

    public String getAccountCredit() {
        return accountCredit;
    }

    public void setAccountCredit(String accountCredit) {
        this.accountCredit = accountCredit;
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
