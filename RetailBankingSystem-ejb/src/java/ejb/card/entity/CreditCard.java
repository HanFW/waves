/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.card.entity;

import ejb.customer.entity.CustomerBasic;
import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

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


//    public HashMap getUploads() {
//        return uploads;
//    }
//
//    public void setUploads(HashMap uploads) {
//        this.uploads = uploads;
//    }

}
