/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.card.customer;

import ejb.card.session.CreditCardManagementSessionBeanLocal;
import ejb.card.session.CreditCardSessionBeanLocal;
import ejb.card.session.DebitCardManagementSessionBeanLocal;
import ejb.card.session.DebitCardSessionBeanLocal;
import ejb.customer.entity.CustomerBasic;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

/**
 *
 * @author aaa
 */
@Named(value = "customerReplaceDamagedCardManagedBean")
@RequestScoped
public class CustomerReplaceDamagedCardManagedBean {

    /**
     * Creates a new instance of CustomerReplaceDamagedCardManagedBean
     */
    @EJB
    private DebitCardManagementSessionBeanLocal debitCardManagementSessionBeanLocal;

    @EJB
    private DebitCardSessionBeanLocal debitCardSessionBeanLocal;

    @EJB
    private CreditCardManagementSessionBeanLocal creditCardManagementSessionBeanLocal;

    @EJB
    private CreditCardSessionBeanLocal creditCardSessionBeanLocal;

    private String cardType;
    private String debitCardPwd;

    private List<String> debitCards = new ArrayList<String>();
    private String selectedDebitCard;
    private CustomerBasic customer;

    private List<String> creditCards = new ArrayList<String>();
    private String selectedCreditCard;
    private String securityCode;

    private boolean creditPanelVisible;
    private boolean debitPanelVisible;

    public CustomerReplaceDamagedCardManagedBean() {
    }

    public void requestForCardReplacement(ActionEvent event) {

        System.out.println("debug: replace card");
        if (cardType.equals("debit")) {
            replaceDebitCard();
        }
        if (cardType.equals("credit")) {
            replaceCreditCard();
        }
    }

    public void showCards() {
        if (cardType.equals("credit")) {
            creditPanelVisible = true;
            debitPanelVisible = false;
        }
        if (cardType.equals("debit")) {
            creditPanelVisible = false;
            debitPanelVisible = true;
        }
    }

    public void replaceDebitCard() {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext ec = context.getExternalContext();
        FacesMessage message = null;

        String[] debitCardInfo = selectedDebitCard.split("-");
        String debitCardNum = debitCardInfo[1];

        debitCardManagementSessionBeanLocal.replaceDamagedDebitCard(debitCardNum);
        message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Replacement successful! Please wait for 2-3 days and your new card will be mailed to your preferred address.", null);
        context.addMessage(null, message);
    }

    public void replaceCreditCard() {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext ec = context.getExternalContext();
        FacesMessage message = null;

        String[] creditCardInfo = selectedCreditCard.split("-");
        String creditCardNum = creditCardInfo[2];
        
        creditCardManagementSessionBeanLocal.replaceDamagedCreditCard(creditCardNum);
        message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Replacement successful! Please wait for 2-3 days and your new card will be mailed to your preferred address.", null);
        context.addMessage(null, message);
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getDebitCardPwd() {
        return debitCardPwd;
    }

    public void setDebitCardPwd(String debitCardPwd) {
        this.debitCardPwd = debitCardPwd;
    }

    public List<String> getDebitCards() {
        if (debitCards.isEmpty()) {

            customer = getCustomerViaSessionMap();
            Long id = customer.getCustomerBasicId();
            debitCards = debitCardSessionBeanLocal.getAllActivatedDebitCards(id);

        }
        return debitCards;
    }

    public void setDebitCards(List<String> debitCards) {
        this.debitCards = debitCards;
    }

    public String getSelectedDebitCard() {
        return selectedDebitCard;
    }

    public void setSelectedDebitCard(String selectedDebitCard) {
        this.selectedDebitCard = selectedDebitCard;
    }

    public CustomerBasic getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerBasic customer) {
        this.customer = customer;
    }

    public List<String> getCreditCards() {
        customer = getCustomerViaSessionMap();
        Long id = customer.getCustomerBasicId();
        creditCards = creditCardSessionBeanLocal.getAllActivatedCreditCards(id);
        return creditCards;
    }

    public void setCreditCards(List<String> creditCards) {
        this.creditCards = creditCards;
    }

    public String getSelectedCreditCard() {
        return selectedCreditCard;
    }

    public void setSelectedCreditCard(String selectedCreditCard) {
        this.selectedCreditCard = selectedCreditCard;
    }

    public String getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(String securityCode) {
        this.securityCode = securityCode;
    }

    public boolean isCreditPanelVisible() {
        return creditPanelVisible;
    }

    public void setCreditPanelVisible(boolean creditPanelVisible) {
        this.creditPanelVisible = creditPanelVisible;
    }

    public boolean isDebitPanelVisible() {
        return debitPanelVisible;
    }

    public void setDebitPanelVisible(boolean debitPanelVisible) {
        this.debitPanelVisible = debitPanelVisible;
    }
    public CustomerBasic getCustomerViaSessionMap() {
        FacesContext context = FacesContext.getCurrentInstance();
        customer = (CustomerBasic) context.getExternalContext().getSessionMap().get("customer");

        return customer;

    }
    
}
