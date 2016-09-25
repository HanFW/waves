/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.infrastructure;

import ejb.customer.entity.CustomerBasic;
import java.io.IOException;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.primefaces.context.RequestContext;
import ejb.infrastructure.session.CustomerAdminSessionBeanLocal;
import ejb.infrastructure.session.LoggingSessionBeanLocal;
import ejb.infrastructure.session.SMSSessionBeanLocal;
import org.jboss.aerogear.security.otp.Totp;
import org.jboss.aerogear.security.otp.api.Clock;

/**
 *
 * @author hanfengwei
 */
@Named(value = "customerLoginManagedBean")
@SessionScoped
public class CustomerLoginManagedBean implements Serializable {

    @EJB
    private LoggingSessionBeanLocal loggingSessionBeanLocal;

    @EJB
    private SMSSessionBeanLocal sMSSessionBeanLocal;

    @EJB
    private CustomerAdminSessionBeanLocal adminSessionBeanLocal;

    private String customerAccount;
    private String customerPassword;
    private String newCustomerAccount;
    private String newCustomerPassword;
    private CustomerBasic customer;
    private int loginAttempts;
    private String customerStatus;
    private String customerNationality;
    private String customerIdentification;
    private String customerOTP;

    /**
     * Creates a new instance of CustomerLoginManagedBean
     */
    public CustomerLoginManagedBean() {
    }

    /**
     *
     * @param event
     */
    // customer log in to online banking
    public void doLogin(ActionEvent event) throws IOException, NoSuchAlgorithmException {
        System.out.println("=");
        System.out.println("====== infrastructure/CustomerLoginManagedBean: doLogin() ======");

        FacesMessage message = null;
        FacesContext context = FacesContext.getCurrentInstance();
        //retrieve customer by user ID
        customer = adminSessionBeanLocal.getCustomerByOnlineBankingAccount(customerAccount);

        if (customer == null) {
            //if no customer found in database
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid account number: ", "Please check your account number.");
            context.addMessage(null, message);
            System.out.println("====== infrastructure/CustomerLoginManagedBean: doLogin(): login failed: invalid account number");
        } else {
            //found customer by userID
            //encrypt the customerPassword first
            customerPassword = md5Hashing(customerPassword + customer.getCustomerIdentificationNum().substring(0, 3));
            String status = adminSessionBeanLocal.login(customerAccount, customerPassword);

            switch (status) {
                case "loggedIn":
                    RequestContext rc = RequestContext.getCurrentInstance();
                    rc.execute("PF('otpDialog').show();");
                    sMSSessionBeanLocal.sendOTP("customer", customer);
                    customerAccount = null;
                    customerPassword = null;
                    System.out.println("====== infrastructure/CustomerLoginManagedBean: doLogin(): login userID and PIN correct");
                    break;
                case "invalidPassword":
                    customerPassword = null;
                    message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid password: ", "Please enter your password again.");
                    context.addMessage(null, message);
                    loginAttempts++;
                    System.out.println("====== infrastructure/CustomerLoginManagedBean: doLogin(): login failed: invalid password. login attepts: " + loginAttempts);
                    break;
                default:
                    message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid account: ", "Please check your account number.");
                    context.addMessage(null, message);
                    customerAccount = null;
                    customerPassword = null;
                    System.out.println("====== infrastructure/CustomerLoginManagedBean: doLogin(): login failed: invalid account");
                    break;
            }
        }

    }

    public void verifyLoginOTP(ActionEvent event) throws IOException {
        System.out.println("=");
        System.out.println("====== infrastructure/CustomerLoginManagedBean: verifyLoginOTP() ======");
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext ec = context.getExternalContext();

        if (customerOTP == null || customerOTP.trim().length() == 0) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid input: ", "Please enter your OTP"));
        } else {
            Clock clock = new Clock(120);
            Totp totp = new Totp(customer.getCustomerOTPSecret(), clock);
            if (totp.verify(customerOTP)) {
                System.out.println("====== infrastructure/CustomerLoginManagedBean: verifyLoginOTP(): customer verified with OTP");
                ec.getSessionMap().put("customer", getCustomer());
                loggingSessionBeanLocal.createNewLogging("customer", customer.getCustomerBasicId(), "login to online banking", "successful", null);
                customerOTP = null;

                if (customer.getCustomerStatus().equals("new")) {
                    System.out.println("====== infrastructure/CustomerLoginManagedBean: verifyLoginOTP(): new customer: redirect to activation page");
                    ec.redirect(ec.getRequestContextPath() + "/web/onlineBanking/infrastructure/customerUserIdActivation.xhtml?faces-redirect=true");
                } else if (customer.getCustomerStatus().equals("reset")) {
                    System.out.println("====== infrastructure/CustomerLoginManagedBean: verifyLoginOTP(): customer reset password: redirect to reset password page");
                    ec.redirect(ec.getRequestContextPath() + "/web/onlineBanking/infrastructure/customerPINReset.xhtml?faces-redirect=true");
                }else {
                    System.out.println("====== infrastructure/CustomerLoginManagedBean: verifyLoginOTP(): existing customer: redirect to online banking home page");
                    context.getExternalContext().redirect(ec.getRequestContextPath() + "/web/onlineBanking/deposit/customerDepositIndex.xhtml?faces-redirect=true");
                }
            } else {
                System.out.println("====== infrastructure/CustomerLoginManagedBean: verifyLoginOTP(): customer entered wrong OTP");
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "OTP does not match: ", "That is an invalid online banking OTP. Please re-enter."));
                customerOTP = null;
            }
        }
    }

    // send OTP to customer when login
    public void sendOTP(ActionEvent event) {
        System.out.println("=");
        System.out.println("====== infrastructure/CustomerLoginManagedBean: sendOTP() ======");
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        sMSSessionBeanLocal.sendOTP("customer", customer);
    }

    //send OTP to customer when retrieving user ID or reset PIN
    public void forgetOTP(ActionEvent event) {
        System.out.println("=");
        System.out.println("====== infrastructure/CustomerLoginManagedBean: forgetOTP() ======");
        FacesContext context = FacesContext.getCurrentInstance();

        customer = adminSessionBeanLocal.getCustomerByIdentificationNum(customerIdentification);
        if (customer == null) {
            System.out.println("====== infrastructure/CustomerLoginManagedBean: forgetOTP(): no customer found");
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid identification number: ", "Please check your identification number."));
        } else {
            System.out.println("====== infrastructure/CustomerLoginManagedBean: forgetOTP(): customer id verified");
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, null, "OTP has been sent to your registered mobile."));
            sMSSessionBeanLocal.sendOTP("customer", customer);
        }
    }

    public void doLogout(ActionEvent event) throws IOException {
        System.out.println("=");
        System.out.println("====== infrastructure/CustomerLoginManagedBean: doLogout() ======");
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.invalidateSession();
        System.out.println("====== infrastructure/CustomerLoginManagedBean: doLogout(): customer logged out");
        loggingSessionBeanLocal.createNewLogging("customer", customer.getCustomerBasicId(), "log out", "successful", null);
        customerAccount = null;
        customerPassword = null;
        newCustomerAccount = null;
        newCustomerPassword = null;
        customer = null;
        loginAttempts = 0;
        customerStatus = null;
        customerNationality = null;
        customerIdentification = null;
        customerOTP = null;
        String serverName = FacesContext.getCurrentInstance().getExternalContext().getRequestServerName();
        String serverPort = "8080";

        ec.redirect(
                "http://" + serverName + ":" + serverPort + ec.getRequestContextPath() + "/web/index.xhtml?faces-redirect=true");
    }

    public void timeoutLogout() throws IOException {
        System.out.println("=");
        System.out.println("====== infrastructure/CustomerLoginManagedBean: timeoutLogout() ======");
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.invalidateSession();
        System.out.println("====== infrastructure/CustomerLoginManagedBean: doLogout(): customer timeout logout");
        loggingSessionBeanLocal.createNewLogging("customer", customer.getCustomerBasicId(), "log out - session tiemout", "successful", null);
        customerAccount = null;
        customerPassword = null;
        newCustomerAccount = null;
        newCustomerPassword = null;
        customer = null;
        loginAttempts = 0;
        customerStatus = null;
        customerNationality = null;
        customerIdentification = null;
        customerOTP = null;
        String serverName = FacesContext.getCurrentInstance().getExternalContext().getRequestServerName();
        String serverPort = "8080";
        ec.redirect("http://" + serverName + ":" + serverPort + ec.getRequestContextPath() + "/web/merlionBank/customerTimeout.xhtml?faces-redirect=true");
    }

    public void activateOnlineBankingAccount(ActionEvent event) throws NoSuchAlgorithmException {
        System.out.println("=");
        System.out.println("====== infrastructure/CustomerLoginManagedBean: activateOnlineBankingAccount() ======");
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext ec = context.getExternalContext();
        newCustomerPassword = md5Hashing(newCustomerPassword + customer.getCustomerIdentificationNum().substring(0, 3));
        if (newCustomerAccount.equals(customerAccount)) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Unsecured account number: ", "For your safety, please enter a new account number different from the initial account number."));
            newCustomerAccount = "";
            newCustomerPassword = "";
        } else if (newCustomerPassword.equals(customerPassword)) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Unsecured account password: ", "For your safety, please enter a new account password different from the initial password."));
            newCustomerPassword = "";
        } else {
            customerStatus = adminSessionBeanLocal.updateOnlineBankingAccount(newCustomerAccount, newCustomerPassword, customer.getCustomerBasicId());
            if (customerStatus.equals("invalidAccountNum")) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Duplicate user ID", "Pleaes create your own unique user ID"));
                newCustomerAccount = "";
                newCustomerPassword = "";
            } else {
                RequestContext rc = RequestContext.getCurrentInstance();
                rc.execute("PF('activationDialog').show();");
                CustomerBasic currentCustomer = (CustomerBasic) ec.getSessionMap().get("customer");
                currentCustomer.setCustomerOnlineBankingAccountNum(newCustomerAccount);
                currentCustomer.setCustomerOnlineBankingPassword(newCustomerPassword);
                newCustomerPassword = null;
                System.out.println("====== infrastructure/CustomerLoginManagedBean: activateOnlineBankingAccount(): account activated");
                loggingSessionBeanLocal.createNewLogging("customer", customer.getCustomerBasicId(), "activate online banking account", "successful", null);
            }
        }
    }

    // customer change PIN after reset
    public void activateNewPIN(ActionEvent event) throws NoSuchAlgorithmException {
        System.out.println("=");
        System.out.println("====== infrastructure/CustomerLoginManagedBean: activateNewPIN() ======");
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext ec = context.getExternalContext();
        newCustomerPassword = md5Hashing(newCustomerPassword + customer.getCustomerIdentificationNum().substring(0, 3));

        if (newCustomerPassword.equals(customerPassword)) {
            context.addMessage("activationMessage", new FacesMessage(FacesMessage.SEVERITY_WARN, "Unsecured account password: ", "For your safety, please enter a new account password different from the initial password."));
            newCustomerPassword = "";
        } else {
            RequestContext rc = RequestContext.getCurrentInstance();
            rc.execute("PF('pinActivationDialog').show();");
            adminSessionBeanLocal.updateOnlineBankingPIN(newCustomerPassword, customer.getCustomerBasicId());
            CustomerBasic currentCustomer = (CustomerBasic) ec.getSessionMap().get("customer");
            currentCustomer.setCustomerOnlineBankingPassword(newCustomerPassword);
            newCustomerPassword = null;
            System.out.println("====== infrastructure/CustomerLoginManagedBean: activateNewPIN(): password updated");
            loggingSessionBeanLocal.createNewLogging("customer", customer.getCustomerBasicId(), "update online banking PIN after password reset", "successful", null);
        }
    }

    public void retrieveCustomerAccount(ActionEvent event) throws IOException {
        System.out.println("=");
        System.out.println("====== infrastructure/LoginBean: retrieveCustomerAccount() ======");
        FacesMessage message = null;
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext ec = context.getExternalContext();

        if (customerOTP == null || customerOTP.trim().length() == 0) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid input: ", "Please enter your OTP"));
        } else {
            if (customer == null) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Retrive user ID failed", "Please enter correct identification number and OTP"));
            } else {
                Clock clock = new Clock(120);
                Totp totp = new Totp(customer.getCustomerOTPSecret(), clock);
                if (totp.verify(customerOTP)) {
                    System.out.println("====== infrastructure/CustomerLoginManagedBean: retrieveCustomerAccount(): customer verified with OTP");
                    customerAccount = customer.getCustomerOnlineBankingAccountNum();
                    System.out.println("====== infrastructure/CustomerLoginManagedBean: retrieveCustomerAccount(): customer online banking account number retrieved: " + customerAccount);
                    customerOTP = null;
                    ec.redirect(ec.getRequestContextPath() + "/web/onlineBanking/infrastructure/customerRetrieveUserId.xhtml");
                } else {
                    System.out.println("====== infrastructure/CustomerLoginManagedBean: retrieveCustomerAccount(): customer entered wrong OTP");
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "OTP does not match: ", "That is an invalid online banking OTP. Please re-enter."));
                    customerOTP = null;
                }
            }
        }
    }

    public void resetCustomerPassword(ActionEvent event) {
        System.out.println("=");
        System.out.println("====== infrastructure/CustomerLoginManagedBean: resetCustomerPassword() ======");
        FacesMessage message = null;
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext ec = context.getExternalContext();

        if (customerOTP == null || customerOTP.trim().length() == 0) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid input: ", "Please enter your OTP"));
        } else if (customer == null) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Retrive user ID failed", "Please enter correct identification number and OTP"));
        } else {
            Clock clock = new Clock(120);
            Totp totp = new Totp(customer.getCustomerOTPSecret(), clock);
            if (totp.verify(customerOTP)) {
                System.out.println("====== infrastructure/CustomerLoginManagedBean: resetCustomerPassword(): customer verified with OTP");
                RequestContext rc = RequestContext.getCurrentInstance();
                rc.execute("PF('resetConfirmation').show();");
                Boolean reset = adminSessionBeanLocal.resetPassword(customerIdentification);
                customerOTP = null;
                System.out.println("====== infrastructure/CustomerLoginManagedBean: resetCustomerPassword(): PIN reset successful");
                loggingSessionBeanLocal.createNewLogging("customer", customer.getCustomerBasicId(), "reset online banking PIN", "successful", null);
            } else {
                System.out.println("====== infrastructure/CustomerLoginManagedBean: resetCustomerPassword(): customer entered wrong OTP");
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "OTP does not match: ", "That is an invalid online banking OTP. Please re-enter."));
                customerOTP = null;
            }
        }

    }
    
    public void deleteIBAccount(){
        customer = null;
    }

    /**
     * @return the customerAccount
     */
    public String getCustomerAccount() {
        return customerAccount;
    }

    /**
     * @param customerAccount the customerAccount to set
     */
    public void setCustomerAccount(String customerAccount) {
        this.customerAccount = customerAccount;
    }

    /**
     * @return the customerPassword
     */
    public String getCustomerPassword() {
        return customerPassword;
    }

    /**
     * @param customerPassword the customerPassword to set
     */
    public void setCustomerPassword(String customerPassword) {
        this.customerPassword = customerPassword;
    }

    /**
     * @return the customer
     */
    public CustomerBasic getCustomer() {
        return customer;
    }

    public String getNewCustomerAccount() {
        return newCustomerAccount;
    }

    public void setNewCustomerAccount(String newCustomerAccount) {
        this.newCustomerAccount = newCustomerAccount;
    }

    public String getNewCustomerPassword() {
        return newCustomerPassword;
    }

    public void setNewCustomerPassword(String newCustomerPassword) {
        this.newCustomerPassword = newCustomerPassword;
    }

    /**
     * @param customer the customer to set
     */
    public void setCustomer(CustomerBasic customer) {
        this.customer = customer;
    }

    public int getLoginAttempts() {
        return loginAttempts;
    }

    public void setLoginAttempts(int loginAttempts) {
        this.loginAttempts = loginAttempts;
    }

    public String getCustomerStatus() {
        return customerStatus;
    }

    public void setCustomerStatus(String customerStatus) {
        this.customerStatus = customerStatus;
    }

    public String getCustomerNationality() {
        return customerNationality;
    }

    public void setCustomerNationality(String customerNationality) {
        this.customerNationality = customerNationality;
    }

    public String getCustomerIdentification() {
        return customerIdentification;
    }

    public void setCustomerIdentification(String customerIdentification) {
        this.customerIdentification = customerIdentification;
    }

    private String md5Hashing(String stringToHash) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        return Arrays.toString(md.digest(stringToHash.getBytes()));
    }

    public String getCustomerOTP() {
        return customerOTP;
    }

    public void setCustomerOTP(String customerOTP) {
        this.customerOTP = customerOTP;
    }

}
