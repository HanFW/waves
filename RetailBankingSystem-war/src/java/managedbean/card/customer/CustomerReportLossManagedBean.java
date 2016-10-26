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
@Named(value = "customerReportLossManagedBean")
@RequestScoped
public class CustomerReportLossManagedBean implements Serializable {

    /**
     * Creates a new instance of CustomerReportLossManagedBean
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
    private String identificationNum;

    private boolean creditPanelVisible;
    private boolean debitPanelVisible;

    public CustomerReportLossManagedBean() {
    }

    public void reportCardLoss(ActionEvent event) {

        System.out.println("debug: cancel card");
        if (cardType.equals("debit")) {
            reportDebitCardLoss();
        }
        if (cardType.equals("credit")){
            reportCreditCardLoss();
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

    public void reportDebitCardLoss() {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext ec = context.getExternalContext();
        FacesMessage message = null;

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Date reportLossDate1 = new Date();
        String reportLossDate = df.format(reportLossDate1);

        String[] debitCardInfo = selectedDebitCard.split("-");
        String debitCardNum = debitCardInfo[1];

        System.out.println("debug: ReportDebitCardLoss- debit card num " + debitCardNum);
        System.out.println("debug: ReportDebitCardLoss- debit card Pwd " + debitCardPwd);
        System.out.println("debug: ReportDebitCardLoss- report loss date " + reportLossDate);
        String result = debitCardManagementSessionBeanLocal.reportDebitCardLoss(debitCardNum, debitCardPwd, reportLossDate);

        switch (result) {
            case "success":
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Your card has been suceesfully reported as loss, we will send a new card to your mailing address in 2-3 working days", null);
                context.addMessage(null, message);
                System.out.println("debit card report loss");
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

        public void reportCreditCardLoss() {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext ec = context.getExternalContext();
        FacesMessage message = null;

        String[] creditCardInfo = selectedCreditCard.split("-");
        String creditCardNum = creditCardInfo[2];

        System.out.println("debug: ReportCreditCardLoss- credit card num " + creditCardNum);
        System.out.println("debug: ReportCreditCardLoss- ID Num " + identificationNum);
        String result = creditCardManagementSessionBeanLocal.reportCreditCardLoss(creditCardNum, identificationNum);

        switch (result) {
            case "success":
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Your card has been suceesfully reported as loss, we will send a new card to your mailing address in 2-3 working days", null);
                context.addMessage(null, message);
                System.out.println("credit card report loss");
                break;
            case "credit card not exist":
                message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Card not exist! Please check the card number input", null);
                context.addMessage(null, message);
                break;
            case "wrong id":
                message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "The identification number provided does not match with our records. Please check the ID input", null);
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

    public String getIdentificationNum() {
        return identificationNum;
    }

    public void setIdentificationNum(String identificationNum) {
        this.identificationNum = identificationNum;
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
