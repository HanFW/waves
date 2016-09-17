/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean;

import entity.CustomerBasic;
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
import session.stateless.AdminSessionBeanLocal;
import session.stateless.CustomerEmailSessionBeanLocal;

/**
 *
 * @author hanfengwei
 */
@Named(value = "loginBean")
@SessionScoped
public class LoginBean implements Serializable {

    @EJB
    private AdminSessionBeanLocal adminSessionBeanLocal;
    
    private String customerAccount;
    private String customerPassword;
    private String newCustomerAccount;
    private String newCustomerPassword;
    private CustomerBasic customer;
    private int loginAttempts;
    private String customerStatus;
    private String customerNationality;
    private String customerIdentification;

    /**
     * Creates a new instance of LoginBean
     */
    public LoginBean() {
    }

    /**
     *
     * @param event
     */
    public void doLogin(ActionEvent event) throws IOException, NoSuchAlgorithmException {
        System.out.println("=== infrastructure/loginBean: doLogin() ===");

        FacesMessage message = null;
        FacesContext context = FacesContext.getCurrentInstance();
        customer = adminSessionBeanLocal.getCustomerByOnlineBankingAccount(customerAccount);

        if (customer == null) {
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Invalid account number: ", "Please check your account number.");
            context.addMessage(null, message);
            System.out.println("=== infrastructure/loginBean: doLogin(): login failed: invalid account number");
        } else {
            //encrypt the customerPassword first
            customerPassword = md5Hashing(customerPassword + customer.getCustomerIdentificationNum().substring(0, 3));
            String status = adminSessionBeanLocal.login(customerAccount, customerPassword);

            switch (status) {
                case "loggedIn":
                    System.out.println("=== infrastructure/loginBean: doLogin(): login successful");
                    context.getExternalContext().getSessionMap().put("customer", getCustomer());
                    if (customer.getCustomerStatus().equals("new")) {
                        context.getExternalContext().redirect("accountActivation.xhtml?faces-redirect=true");
                    } else {
                        context.getExternalContext().redirect("home.xhtml?faces-redirect=true");
                    }
                    break;
                case "invalidPassword":
                    System.out.println("=== infrastructure/loginBean: doLogin(): login failed: invalid password");
                    customerPassword = "";
                    message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Invalid password: ", "Please enter your passsword again.");
                    context.addMessage(null, message);
                    loginAttempts++;
                    System.out.println("=== infrastructure/loginBean: doLogin(): login attempts: " + loginAttempts);
                    break;
                default:
                    System.out.println("=== infrastructure/loginBean: doLogin(): login failed: invalid account");
                    message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Invalid account: ", "Please check your account number.");
                    context.addMessage(null, message);
                    break;
            }
        }

    }

    public void doLogout(ActionEvent event) throws IOException {
        System.out.println("*** loginBean: doLogout");
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.invalidateSession();

        String serverName = FacesContext.getCurrentInstance().getExternalContext().getRequestServerName();
        String serverPort = "8080";
        ec.redirect("http://" + serverName + ":" + serverPort + ec.getRequestContextPath() + "/index.xhtml?faces-redirect=true");
    }

    public void timeoutLogout() throws IOException {
        System.out.println("*** loginBean: logout");
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.invalidateSession();
        String serverName = FacesContext.getCurrentInstance().getExternalContext().getRequestServerName();
        String serverPort = "8080";
        ec.redirect("http://" + serverName + ":" + serverPort + ec.getRequestContextPath() + "/timeout.xhtml?faces-redirect=true");
    }

    public void activateOnlineBankingAccount(ActionEvent event) throws NoSuchAlgorithmException {
        System.out.println("*** loginBean: activateOnlineBankingAccount");
        FacesContext context = FacesContext.getCurrentInstance();
        newCustomerPassword = md5Hashing(newCustomerPassword + customer.getCustomerIdentificationNum().substring(0, 3));
        if (newCustomerAccount.equals(customerAccount)) {
            context.addMessage("activationMessage", new FacesMessage(FacesMessage.SEVERITY_WARN, "Unsecured account number: ", "For your safety, please enter a new account number different from the initial account number."));
            newCustomerAccount = "";
            newCustomerPassword = "";
        } else if (newCustomerPassword.equals(customerPassword)) {
            context.addMessage("activationMessage", new FacesMessage(FacesMessage.SEVERITY_WARN, "Unsecured account password: ", "For your safety, please enter a new account password different from the initial password."));
            newCustomerPassword = "";
        } else {
            customerStatus = adminSessionBeanLocal.updateOnlineBankingAccount(newCustomerAccount, newCustomerPassword, customer.getCustomerBasicId());
        }
    }
    
    public void retrieveCustomerAccount(ActionEvent event) throws IOException{
        System.out.println("=== infrastructure/LoginBean: retrieveCustomerAccount() ===");
        CustomerBasic retrieveCustomer = adminSessionBeanLocal.getCustomerByIdentificationNum(customerIdentification);
        FacesContext context = FacesContext.getCurrentInstance();
        if(retrieveCustomer == null){
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Invalid identification number: ", "Please check your identification number."));
        }else{
            customerAccount = retrieveCustomer.getCustomerOnlineBankingAccountNum();
            context.getExternalContext().redirect("customerRetrieveIBAccount.xhtml");
        }
    }
    
    public void resetCustomerPassword(ActionEvent event){
        System.out.println("=== infrastructure/LoginBean: resetPassword() ===");
        Boolean reset = adminSessionBeanLocal.resetPassword(customerIdentification);
        FacesContext context = FacesContext.getCurrentInstance();
        if(reset){
            System.out.println("=== infrastructure/LoginBean: resetPassword(): password reset successful");
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Password reset successful", "Your new password has been sent to your registered email"));
        }else{
            System.out.println("=== infrastructure/LoginBean: resetPassword(): password reset failed");
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Password reset failed", "Please try again"));
        }
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
        customer = adminSessionBeanLocal.getCustomerByOnlineBankingAccount(customerAccount);
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
}
