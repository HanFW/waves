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
import java.io.Serializable;
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
 * @author Jingyuan
 */
@Named(value = "customerCancelCardManagedBean")
@RequestScoped
public class CustomerCancelCardManagedBean implements Serializable {

    /**
     * Creates a new instance of CustomerCancelCardManagedBean
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

    public CustomerCancelCardManagedBean() {
    }

    public void cancelCard(ActionEvent event) {

        System.out.println("debug: cancel card");
        if (cardType.equals("debit")) {
            cancelDebitCard();
        }
        if (cardType.equals("credit")) {
            cancelCreditCard();
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

    private void cancelDebitCard() {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext ec = context.getExternalContext();
        FacesMessage message = null;
        
        String[] debitCardInfo=selectedDebitCard.split("-");
        String debitCardNum=debitCardInfo[1];

        System.out.println("debug:cancelDebitCard- debit card num " + debitCardNum);
        System.out.println("debug: cancelDebitCard- debit card num " + debitCardPwd);
        String result = debitCardManagementSessionBeanLocal.CancelDebitCard(debitCardNum, debitCardPwd);

        switch (result) {
            case "success":
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Your debit card has been succesfully deleted!", null);
                context.addMessage(null, message);
                System.out.println("debit card deleted");
                break;
            case "debit card not exist":
                message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Card not exist! Please check the card number input", null);
                context.addMessage(null, message);
                break;
            case "wrong pwd":
                message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Password is wrong! Please check the card password input", null);
                context.addMessage(null, message);
                break;
        }

    }
    
        private void cancelCreditCard() {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext ec = context.getExternalContext();
        FacesMessage message = null;
        
        String[] creditCardInfo=selectedCreditCard.split("-");
        String creditCardNum=creditCardInfo[2];

        System.out.println("debug:cancelCreditCard- credit card num " + creditCardNum);
        System.out.println("debug: cancelCreditCard- credit card security code " + securityCode);
        String result = creditCardManagementSessionBeanLocal.cancelCreditCard(creditCardNum, securityCode);

        switch (result) {
            case "success":
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Your credit card has been succesfully canceled!", null);
                context.addMessage(null, message);
                System.out.println("credit card deleted");
                break;
            case "credit card not exist":
                message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Card not exist! Please check the card number input", null);
                context.addMessage(null, message);
                break;
            case "wrong pwd":
                message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Security code is wrong! Please check security code at the back of your credit card", null);
                context.addMessage(null, message);
                break;
            case "credit limit unpaid":
                message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "You have outstanding unpaid! Please clear repayments before canceling this credit card.", null);
                context.addMessage(null, message);
                break;
        }

    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
        System.out.println("debug: set card type " + cardType);
    }

    public String getDebitCardPwd() {
        return debitCardPwd;
    }

    public void setDebitCardPwd(String debitCardPwd) {
        this.debitCardPwd = debitCardPwd;
        System.out.println("debug: set debit Card Pwd " + debitCardPwd);
    }

    public List<String> getDebitCards() {
        System.out.println("test " + debitCards);
        if (debitCards.isEmpty()) {

            customer = getCustomerViaSessionMap();
            Long id=customer.getCustomerBasicId();
            debitCards = debitCardSessionBeanLocal.getAllDebitCards(id);

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
        System.out.println("debug: set selectedDebitCard " + selectedDebitCard);
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
