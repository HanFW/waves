/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.card.customer;

import ejb.card.session.DebitCardPasswordSessionBeanLocal;
import ejb.card.session.DebitCardSessionBeanLocal;
import ejb.customer.entity.CustomerBasic;
import ejb.infrastructure.session.SMSSessionBeanLocal;
import java.io.IOException;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.jboss.aerogear.security.otp.Totp;
import org.jboss.aerogear.security.otp.api.Clock;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Jingyuan
 */
@Named(value = "customerActivateDebitCardManagedBean")
@ViewScoped
public class CustomerActivateDebitCardManagedBean implements Serializable {

    /**
     * Creates a new instance of CustomerActivateDebitCardManagedBean
     */
    @EJB
    private DebitCardSessionBeanLocal debitCardSessionBeanLocal;

    @EJB
    private SMSSessionBeanLocal smsSessionBeanLocal;

    @EJB
    DebitCardPasswordSessionBeanLocal debitCardPasswordSessionBeanLocal;

    private CustomerBasic customer;
    private String debitCardNum;
    private String cardHolderName;
    private String debitCardSecurityCode;

    private String customerOTP;

    private String debitCardPwd;

    public CustomerActivateDebitCardManagedBean() {
    }

    public void verifActivationOTP(ActionEvent event) throws IOException {
        System.out.println("=");
        System.out.println("====== card/debitCard/CustomerActivateDebitCardManagedBean: verifyOTP() ======");
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
                System.out.println("====== card/debitCard/CustomerActivateDebitCardManagedBean: verifyOTP(): customer verified with OTP");
                customerOTP = null;
                ec.redirect(ec.getRequestContextPath() + "/web/onlineBanking/card/debitCard/customerActivateDebitCardDone.xhtml?faces-redirect=true");
            } else {
                System.out.println("====== card/debitCard/CustomerActivateDebitCardManagedBean: verifyOTP(): customer entered wrong OTP");
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "OTP does not match: ", "That is an invalid online banking OTP. Please re-enter."));
                customerOTP = null;
            }
        }
    }

    public void setDebitCardPassword(ActionEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext ec = context.getExternalContext();
        FacesMessage message = null;

        String debitCardNumber = (String) ec.getSessionMap().get("debitCardNumber");

        System.out.println("test " + debitCardPwd);
        System.out.println("test 2 " + debitCardNumber);

        debitCardPasswordSessionBeanLocal.setPassword(debitCardPwd, debitCardNumber);

        System.out.println("====== card/debitCard/CustomerActivateDebitCardManagedBean: set password for debit Card");
        message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Password has been successfully set for your debit card!", null);
        context.addMessage(null, message);

    }

    public String getDebitCardNum() {
        return debitCardNum;
    }

    public void setDebitCardNum(String debitCardNum) {
        this.debitCardNum = debitCardNum;
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

    public String getDebitCardSecurityCode() {
        return debitCardSecurityCode;
    }

    public void setDebitCardSecurityCode(String debitCardSecurityCode) {
        this.debitCardSecurityCode = debitCardSecurityCode;
    }

    public void checkDebitCardNum(ActionEvent event) {
        FacesMessage message = null;
        FacesContext context = FacesContext.getCurrentInstance();

        System.out.println("check debit card num when activating debit card");
        String result = debitCardSessionBeanLocal.debitCardNumValiadation(debitCardNum, cardHolderName, debitCardSecurityCode);

        switch (result) {
            case "debit card not exist":
                message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Debit card does not exist! Please check the card number input", null);
                context.addMessage(null, message);
                System.out.println("*** ActivateDebitCardManagedBean:  debit card does not exist!");
                break;
            case "cardHolderName not match":
                message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Card holder name does not match with the debit card!", null);
                context.addMessage(null, message);
                System.out.println("*** ActivateDebitCardManagedBean: card holer name does not match!");
                break;
            case "csc not match":
                message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Card Security Code is wrong! Please check the card security code input", null);
                context.addMessage(null, message);
                System.out.println("*** ActivateDebitCardManagedBean: card security code is wrong!");
                break;
            case "valid":
                System.out.println("put in map: "+debitCardNum);
                context.getExternalContext().getSessionMap().put("debitCardNumber", debitCardNum);
                RequestContext rc = RequestContext.getCurrentInstance();
                rc.execute("PF('otpDialog').show();");
                System.out.println("*** ActivateDebitCardManagedBean: card holer name does not match!");
                break;
        }

    }

    public String getCustomerOTP() {
        return customerOTP;
    }

    public void setCustomerOTP(String customerOTP) {
        this.customerOTP = customerOTP;
    }

    public String getDebitCardPwd() {
        return debitCardPwd;
    }

    public void setDebitCardPwd(String debitCardPwd) {
        this.debitCardPwd = debitCardPwd;
        System.out.println("debug set debit card pwd: " + debitCardPwd);
    }

}
