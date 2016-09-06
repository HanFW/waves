/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import entity.CustomerBasic;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.primefaces.context.RequestContext;
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
    public void doLogin(ActionEvent event) {
//        adminSessionBeanLocal.createOnlineBankingAccount(Long.valueOf(5));

        FacesMessage message = null;
        FacesContext context = FacesContext.getCurrentInstance();
        
        //encrypt the customerPassword first
        String status = adminSessionBeanLocal.login(customerAccount, customerPassword);   
        switch (status) {
            case "loggedIn":
//                message = new FacesMessage(FacesMessage.SEVERITY_INFO, status, "Welcome back!");
                System.out.println("*** loginBean: loggedIn");
                customer = adminSessionBeanLocal.getCustomerByOnlineBankingAccount(customerAccount);
                context.getExternalContext().getSessionMap().put("customer", customer);
                try {
                    context.getExternalContext().redirect("home.xhtml");
                } catch (IOException ex) {
                    Logger.getLogger(LoginBean.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println("******************* test sessionMap");
                System.out.println(context.getExternalContext().getSessionMap().get("customer"));
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

    /**
     * @param customer the customer to set
     */
    public void setCustomer(CustomerBasic customer) {
        this.customer = customer;
    }
}
