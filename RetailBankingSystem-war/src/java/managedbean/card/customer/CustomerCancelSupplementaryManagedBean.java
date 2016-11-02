/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.card.customer;

import ejb.card.entity.SupplementaryCard;
import ejb.card.session.CreditCardManagementSessionBeanLocal;
import ejb.card.session.CreditCardSessionBeanLocal;
import ejb.customer.entity.CustomerBasic;
import java.io.IOException;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 *
 * @author aaa
 */
@Named(value = "customerCancelSupplementaryManagedBean")
@RequestScoped
public class CustomerCancelSupplementaryManagedBean {

    @EJB
    private CreditCardManagementSessionBeanLocal creditCardManagementSessionBeanLocal;

    @EJB
    private CreditCardSessionBeanLocal creditCardSessionBeanLocal;   

    public CustomerCancelSupplementaryManagedBean() {
    }

    public void cancelSupplementaryCard(Long cardId) {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext ec = context.getExternalContext();
        FacesMessage message = null;

        creditCardManagementSessionBeanLocal.cancelSupplementaryCard(cardId);
        message = new FacesMessage(FacesMessage.SEVERITY_INFO, "The supplementary card has been succesfully canceled!", null);
        context.addMessage(null, message);
    }

    public List<SupplementaryCard> getAllSupplementaryCard() {
        List<SupplementaryCard> supplementaryCards = creditCardSessionBeanLocal.getAllSupplementaryCardByCustomer(getCustomerViaSessionMap());
        return supplementaryCards;
    }

    public void redirectToAddSupplementaryCard() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext ec = context.getExternalContext();
        ec.redirect(ec.getRequestContextPath() + "/web/onlineBanking/card/creditCard/customerAddSupplementaryCard.xhtml?faces-redirect=true");
    }

    public CustomerBasic getCustomerViaSessionMap() {
        FacesContext context = FacesContext.getCurrentInstance();
        CustomerBasic customer = (CustomerBasic) context.getExternalContext().getSessionMap().get("customer");

        return customer;

    }  
    
}
