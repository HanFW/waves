/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.card.customer;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 *
 * @author aaa
 */
@Named(value = "publicCreditCardApplicationDoneManagedBean")
@RequestScoped
public class PublicCreditCardApplicationDoneManagedBean implements Serializable {

    private String creditCardTypeName;
    private String customerName;

    public PublicCreditCardApplicationDoneManagedBean() {
    }

    @PostConstruct
    public void init() {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        creditCardTypeName = (String) ec.getFlash().get("cardTypeName");
        customerName = (String) ec.getFlash().get("customerName");
    }

    public String getCreditCardTypeName() {
        return creditCardTypeName;
    }

    public void setCreditCardTypeName(String creditCardTypeName) {
        this.creditCardTypeName = creditCardTypeName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

}
