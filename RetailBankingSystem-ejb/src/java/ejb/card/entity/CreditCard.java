/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.card.entity;

import ejb.customer.entity.CustomerBasic;
import java.io.Serializable;
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

    @ManyToOne(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    private CreditCardType creditCardType;
    
    @ManyToOne(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    private CustomerBasic customerBasic;
    
    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER, mappedBy = "creditCard")
    private List<SupplementaryCard> supplementaryCard;
    
    private Double creditLimit;
    private Double outstandingBalance; 


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

    public Double getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(Double creditLimit) {
        this.creditLimit = creditLimit;
    }

    public Double getOutstandingBalance() {
        return outstandingBalance;
    }

    public void setOutstandingBalance(Double outstandingBalance) {
        this.outstandingBalance = outstandingBalance;
    }

    
   

}
