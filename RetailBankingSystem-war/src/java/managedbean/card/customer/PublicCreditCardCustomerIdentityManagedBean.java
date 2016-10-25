/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.card.customer;

import ejb.card.session.CreditCardSessionBeanLocal;
import java.io.IOException;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

/**
 *
 * @author aaa
 */
@Named(value = "publicCreditCardCustomerIdentityManagedBean")
@ViewScoped
public class PublicCreditCardCustomerIdentityManagedBean implements Serializable{

    private String hasMerlionAcc;
    private boolean existingCustomerVisible;
    private boolean newCustomerVisible;
    private Long cardTypeId;
    private String creditCardTypeName;

    @EJB
    private CreditCardSessionBeanLocal creditCardSessionLocal;
    


    public PublicCreditCardCustomerIdentityManagedBean() {
    }

    public void showPanal() {
        if (hasMerlionAcc.equals("Yes")) {
            existingCustomerVisible = true;
        }
        if (hasMerlionAcc.equals("No")) {
            newCustomerVisible = true;
        }
    }
    
    public void redirectToApplication() throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
//        cardTypeId = (Long) ec.getSessionMap().get("cardTypeId");
        
//        System.out.println("%%%%%%%%%%%%customer identity type id = " + cardTypeId);
//        ec.getFlash().keep("cardTypeId");
//        ec.getFlash().put("creditCardTypeId", cardTypeId);
        ec.redirect(ec.getRequestContextPath() + "/web/merlionBank/creditCard/publicCreditCardApplication.xhtml?faces-redirect=true");
    }

    public String getHasMerlionAcc() {
        return hasMerlionAcc;
    }

    public void setHasMerlionAcc(String hasMerlionAcc) {
        this.hasMerlionAcc = hasMerlionAcc;
    }

    public boolean isExistingCustomerVisible() {
        return existingCustomerVisible;
    }

    public void setExistingCustomerVisible(boolean existingCustomerVisible) {
        this.existingCustomerVisible = existingCustomerVisible;
    }

    public boolean isNewCustomerVisible() {
        return newCustomerVisible;
    }

    public void setNewCustomerVisible(boolean newCustomerVisible) {
        this.newCustomerVisible = newCustomerVisible;
    }

    public Long getCardTypeId() {
        return cardTypeId;
    }

    public void setCardTypeId(Long cardTypeId) {
        this.cardTypeId = cardTypeId;
    }

    public String getCreditCardTypeName() {
        return creditCardTypeName;
    }

    public void setCreditCardTypeName(String creditCardTypeName) {
        this.creditCardTypeName = creditCardTypeName;
    }

}
