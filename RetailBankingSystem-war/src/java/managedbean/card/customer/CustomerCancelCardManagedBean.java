/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.card.customer;

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

    private String cardType;
    private String debitCardPwd;

    private List<String> debitCards = new ArrayList<String>();
    private String selectedDebitCard;
    private CustomerBasic customer;

    public CustomerCancelCardManagedBean() {
    }

    public void cancelCard(ActionEvent event) {

        System.out.println("debug: cancel card");
        if (cardType.equals("debit")) {
            cancelDebitCard();
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

    public CustomerBasic getCustomerViaSessionMap() {
        FacesContext context = FacesContext.getCurrentInstance();
        customer = (CustomerBasic) context.getExternalContext().getSessionMap().get("customer");

        return customer;

    }

}
