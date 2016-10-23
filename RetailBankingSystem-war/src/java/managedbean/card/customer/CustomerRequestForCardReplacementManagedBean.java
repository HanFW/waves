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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
@Named(value = "customerRequestForCardReplacementManagedBean")
@RequestScoped
public class CustomerRequestForCardReplacementManagedBean implements Serializable {

    /**
     * Creates a new instance of CustomerReportLossManagedBean
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

    public CustomerRequestForCardReplacementManagedBean() {
    }

    public void requestForCardReplacement(ActionEvent event) {

        System.out.println("debug: cancel card");
        if (cardType.equals("debit")) {
            requestForDebitCardReplacement();
        }
    }

    public void requestForDebitCardReplacement() {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext ec = context.getExternalContext();
        FacesMessage message = null;

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Date requestCardReplacementDate1 = new Date();
        String requestCardReplacementDate = df.format(requestCardReplacementDate1);

        String[] debitCardInfo = selectedDebitCard.split("-");
        String debitCardNum = debitCardInfo[1];

        System.out.println("debug: ReportDebitCardLoss- debit card num " + debitCardNum);
        System.out.println("debug: ReportDebitCardLoss- debit card Pwd " + debitCardPwd);
        System.out.println("debug: ReportDebitCardLoss- request card replacement date " + requestCardReplacementDate);
        String result = debitCardManagementSessionBeanLocal.requestForDebitCardReplacement(debitCardNum, debitCardPwd, requestCardReplacementDate);

        switch (result) {
            case "success":
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "We will send a new card to your mailing address in 2-3 working days", null);
                context.addMessage(null, message);
                System.out.println("debit card request card replacement");
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
    }

    public String getDebitCardPwd() {
        return debitCardPwd;
    }

    public void setDebitCardPwd(String debitCardPwd) {
        this.debitCardPwd = debitCardPwd;
    }

    public List<String> getDebitCards() {
        System.out.println("test " + debitCards);

        customer = getCustomerViaSessionMap();
        Long id = customer.getCustomerBasicId();
        debitCards = debitCardSessionBeanLocal.getAllActivatedDebitCards(id);
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
