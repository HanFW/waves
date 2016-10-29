/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.card.entity;

import ejb.customer.entity.CustomerBasic;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author aaa
 */
@Entity
public class CreditCard extends Card implements Serializable {
    private static final long serialVersionUID = 1L;

    @ManyToOne(cascade = {CascadeType.PERSIST}, fetch = FetchType.EAGER)
    private CreditCardType creditCardType;
    
    @ManyToOne(cascade = {CascadeType.PERSIST}, fetch = FetchType.EAGER)
    private CustomerBasic customerBasic;
    
    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER, mappedBy = "creditCard")
    private List<SupplementaryCard> supplementaryCard;
    
    private double creditLimit;
    private double outstandingBalance; 
//    private HashMap uploads;


    public CreditCardType getCreditCardType() {
        return creditCardType;
    }

    public void setCreditCardType(CreditCardType creditCardType) {
        this.creditCardType = creditCardType;
    }

    public CustomerBasic getCustomerBasic() {
        return customerBasic;
    }

    public void setCustomerBasic(CustomerBasic customerBasic) {
        this.customerBasic = customerBasic;
    }

    public List<SupplementaryCard> getSupplementaryCard() {
        return supplementaryCard;
    }

    public void setSupplementaryCard(List<SupplementaryCard> supplementaryCard) {
        this.supplementaryCard = supplementaryCard;
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
//
//    public HashMap getUploads() {
//        return uploads;
//    }
//
//    public void setUploads(HashMap uploads) {
//        this.uploads = uploads;
//    }

}
