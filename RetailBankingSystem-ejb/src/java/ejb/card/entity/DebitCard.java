/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.card.entity;

import ejb.deposit.entity.BankAccount;
import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

/**
 *
 * @author Jingyuan
 */
@Entity
public class DebitCard extends Card implements Serializable {

    private static final long serialVersionUID = 1L;

    @ManyToOne(cascade = {CascadeType.PERSIST}, fetch = FetchType.EAGER)
    private BankAccount bankAccount;

    @ManyToOne(cascade = {CascadeType.PERSIST}, fetch = FetchType.EAGER)
    private DebitCardType debitCardType;

    
    private String debitCardPwd;
    private String debitCardExpiryDate;
    private String cardHolderName;
    private String cardSecurityCode;
    private String status;
    private int transactionLimit;

    public Long getDebitCardId() {
        return debitCardId;
    }

    public void setDebitCardId(Long debitCardId) {
        this.debitCardId = debitCardId;
    }

    public BankAccount getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getDebitCardPwd() {
        return debitCardPwd;
    }

    public void setDebitCardPwd(String debitCardPwd) {
        this.debitCardPwd = debitCardPwd;
    }


    public DebitCardType getDebitCardType() {
        return debitCardType;
    }

    public void setDebitCardType(DebitCardType debitCardType) {
        this.debitCardType = debitCardType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTransactionLimit() {
        return transactionLimit;
    }

    public void setTransactionLimit(int transactionLimit) {
        this.transactionLimit = transactionLimit;
    }     

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (debitCardId != null ? debitCardId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DebitCard)) {
            return false;
        }
        DebitCard other = (DebitCard) object;
        if ((this.debitCardId == null && other.debitCardId != null) || (this.debitCardId != null && !this.debitCardId.equals(other.debitCardId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ejb.card.entity.DebitCard[ id=" + debitCardId + " ]";
    }

}
