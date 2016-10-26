/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.card.customer;

import java.io.IOException;
import java.io.Serializable;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 *
 * @author aaa
 */
@Named(value = "publicCreditCardsInfoManagedBean")
@RequestScoped
public class PublicCreditCardsInfoManagedBean{

    private Long creditCardTypeId;
    

    public PublicCreditCardsInfoManagedBean() {
    }

    public Long getCreditCardTypeId() {
        return creditCardTypeId;
    }

    public void setCreditCardTypeId(Long creditCardTypeId) {
        this.creditCardTypeId = creditCardTypeId;
    }

    public void redirectToCustomerIdentity(Long creditCardTypeId) throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
//        ec.getFlash().put("cardTypeId", creditCardTypeId);
        ec.getSessionMap().put("cardTypeId", creditCardTypeId);
//        ec.getFlash().keep("cardTypeId");
        System.out.println("$$$$$$$$$$$$$$$$$cards info typeid = " + creditCardTypeId);
        ec.redirect(ec.getRequestContextPath() + "/web/merlionBank/creditCard/publicCustomerExistingInfo.xhtml?faces-redirect=true");
    }
}
