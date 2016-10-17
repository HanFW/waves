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
    private int transactionLimit;
   

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

    public int getTransactionLimit() {
        return transactionLimit;
    }

    public void setTransactionLimit(int transactionLimit) {
        this.transactionLimit = transactionLimit;
    }



}
