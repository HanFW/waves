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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import session.stateless.AdminSessionBeanLocal;

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
    private CustomerBasic customer;

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
//        adminSessionBeanLocal.createOnlineBankingAccount(Long.valueOf(1));

        FacesMessage message = null;
        FacesContext context = FacesContext.getCurrentInstance();
        customer = adminSessionBeanLocal.getCustomerByOnlineBankingAccount(customerAccount);
        customerPassword = md5Hashing(customerPassword + customer.getCustomerIdentificationNum().substring(0, 3));;
        
        //encrypt the customerPassword first
        String status = adminSessionBeanLocal.login(customerAccount, customerPassword);   
        switch (status) {
            case "loggedIn":
                System.out.println("*** loginBean: loggedIn");
                context.getExternalContext().getSessionMap().put("customer", getCustomer());
                context.getExternalContext().redirect("home.xhtml?faces-redirect=true");
                break;
            case "invalidPassword":
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, status, "Invalid customerPassword/account.");
                context.addMessage(null, message);
                System.out.println("*** loginBean: invalid password");
                break;
            default:
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, status, "Please check your account number.");
                context.addMessage(null, message);
                System.out.println("*** loginBean: invalid account");
                break;
        }
    }
    
    public void doLogout(ActionEvent event) throws IOException{
        System.out.println("*** loginBean: doLogout");
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.invalidateSession();
        
        String serverName = FacesContext.getCurrentInstance().getExternalContext().getRequestServerName();
        String serverPort = "8080";
        ec.redirect("http://" + serverName + ":" + serverPort + ec.getRequestContextPath() + "/index.xhtml?faces-redirect=true");
    }
    
    public void timeoutLogout() throws IOException{
        System.out.println("*** loginBean: logout");
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.invalidateSession();
        String serverName = FacesContext.getCurrentInstance().getExternalContext().getRequestServerName();
        String serverPort = "8080";
        ec.redirect("http://" + serverName + ":" + serverPort + ec.getRequestContextPath() + "/timeout.xhtml?faces-redirect=true");
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
        return adminSessionBeanLocal.getCustomerByOnlineBankingAccount(customerAccount);
    }

    /**
     * @param customer the customer to set
     */
    public void setCustomer(CustomerBasic customer) {
        this.customer = customer;
    }
    
    private String md5Hashing(String stringToHash) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        return Arrays.toString(md.digest(stringToHash.getBytes()));
    }
}
