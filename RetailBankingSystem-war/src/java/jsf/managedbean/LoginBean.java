/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
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
@RequestScoped
public class LoginBean {
    @EJB
    private AdminSessionBeanLocal adminSessionBeanLocal;
    
    private String customerAccount;
    private String customerPassword;
    
    /**
     * Creates a new instance of LoginBean
     */
    public LoginBean() {
    }
    
    /**
     *
     * @param event
     */
    public void doLogin(ActionEvent event){
        adminSessionBeanLocal.createOnlineBankingAccount(Long.valueOf(4));
        
        RequestContext context = RequestContext.getCurrentInstance();
        FacesMessage message = null;
        //encrypt the customerPassword first
        String status = adminSessionBeanLocal.login(customerAccount,customerPassword);
        String redirect;
        switch (status) {
            case "loggedIn":
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, status, "Welcome back!");
                redirect = "home.xhtml";
                System.out.println("*** loginBean: loggedIn");
                break;
            case "invalidPassword":
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, status, "Invalid customerPassword/account.");
                redirect = "login.xhtml";
                System.out.println("*** loginBean: invalid password");
                break;
            default:
                message = new FacesMessage(FacesMessage.SEVERITY_INFO, status, "Please check your account number.");
                redirect = "login.xhtml";
                System.out.println("*** loginBean: invalid account");
                break;
        }
        FacesContext.getCurrentInstance().addMessage(null, message);

        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(redirect);
        } catch (IOException ex) {
            Logger.getLogger(LoginBean.class.getName()).log(Level.SEVERE, null, ex);
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
}
