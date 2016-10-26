/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.card.customer;

import ejb.card.session.CreditCardSessionBeanLocal;
import ejb.customer.entity.CustomerBasic;
import ejb.infrastructure.session.SMSSessionBeanLocal;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.jboss.aerogear.security.otp.Totp;
import org.jboss.aerogear.security.otp.api.Clock;

/**
 *
 * @author aaa
 */
@Named(value = "customerActivateCreditCardManagedBean")
@ViewScoped
public class CustomerActivateCreditCardManagedBean implements Serializable {

    /**
     * Creates a new instance of CustomerActivateCreditCardManagedBean
     */
    @EJB
    private CreditCardSessionBeanLocal creditCardSessionBeanLocal;

    @EJB
    private SMSSessionBeanLocal smsSessionBeanLocal;

    

    private CustomerBasic customer;
    private String cardHolderName;
    private String creditCardSecurityCode;

    private List<String> creditCards = new ArrayList<String>();
    private String selectedCreditCard;

    private String customerOTP;


    public CustomerActivateCreditCardManagedBean() {
    }

    public void verifActivationOTP(ActionEvent event) throws IOException {
        System.out.println("====== card/creditCard/CustomerActivateCreditCardManagedBean: verifyOTP() ======");
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext ec = context.getExternalContext();

        if (customerOTP == null || customerOTP.trim().length() == 0) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid input: ", "Please enter your OTP"));
        } else {
            Clock clock = new Clock(120);
            CustomerBasic customer = getCustomerViaSessionMap();
            System.out.println("debug: " + customer);
            Totp totp = new Totp(customer.getCustomerOTPSecret(), clock);
            if (totp.verify(customerOTP)) {
                System.out.println("====== card/creditCard/CustomerActivateCreditCardManagedBean: verifyOTP(): customer verified with OTP");
                customerOTP = null;
                ec.redirect(ec.getRequestContextPath() + "/web/onlineBanking/card/creditCard/customerActivateCreditCardDone.xhtml?faces-redirect=true");
            } else {
                System.out.println("====== card/creditCard/CustomerActivateCreditCardManagedBean: verifyOTP(): customer entered wrong OTP");
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "OTP does not match: ", "That is an invalid online banking OTP. Please re-enter."));
                customerOTP = null;
            }
        }
    }

    public CustomerBasic getCustomerViaSessionMap() {
        FacesContext context = FacesContext.getCurrentInstance();
        customer = (CustomerBasic) context.getExternalContext().getSessionMap().get("customer");

        return customer;

    }

    public CustomerBasic getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerBasic customer) {
        this.customer = customer;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public String getCreditCardSecurityCode() {
        return creditCardSecurityCode;
    }

    public void setCreditCardSecurityCode(String creditCardSecurityCode) {
        this.creditCardSecurityCode = creditCardSecurityCode;
    }

    public void checkCreditCardNum(ActionEvent event) throws IOException {
        FacesMessage message = null;
        FacesContext context = FacesContext.getCurrentInstance();

        String[] creditCardInfo = selectedCreditCard.split("-");
        String creditCardNum = creditCardInfo[2];
        System.out.println("!!!!!!!!!!!!!!!first split"+ creditCardNum);
        
        
        System.out.println("!!!!!!!!!!!!!!!!cardNUm"+creditCardNum+"!!!!!!!!!!!!!");

        String result = creditCardSessionBeanLocal.creditCardNumValiadation(creditCardNum, cardHolderName, creditCardSecurityCode);

        switch (result) {
            case "credit card not exist":
                message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Credit card does not exist! Please check the card number input", null);
                context.addMessage(null, message);
                System.out.println("*** ActivateCreditCardManagedBean:  Credit card does not exist!");
                break;
            case "cardHolderName not match":
                message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Card holder name does not match with the Credit card!", null);
                context.addMessage(null, message);
                System.out.println("*** ActivateCreditCardManagedBean: card holer name does not match!");
                break;
            case "csc not match":
                message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Card Security Code is wrong! Please check the card security code input", null);
                context.addMessage(null, message);
                System.out.println("*** ActivateCreditCardManagedBean: card security code is wrong!");
                break;
            case "valid":
                context.getExternalContext().getSessionMap().put("creditCardNumber", creditCardNum);
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Your credit card has been successfully activated!", null);
                context.addMessage(null, message);
                System.out.println("*** ActivateCreditCardManagedBean: correct!");
                break;
        }

    }

    public String getCustomerOTP() {
        return customerOTP;
    }

    public void setCustomerOTP(String customerOTP) {
        this.customerOTP = customerOTP;
    }

    public List<String> getCreditCards() {
        if (creditCards.isEmpty()) {

            customer = getCustomerViaSessionMap();
            Long id = customer.getCustomerBasicId();
            creditCards = creditCardSessionBeanLocal.getAllNonActivatedCreditCards(id);

        }
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

}
