/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.infrastructure.customer;

import ejb.customer.entity.CustomerBasic;
import ejb.infrastructure.session.CustomerAdminSessionBeanLocal;
import ejb.infrastructure.session.LoggingSessionBeanLocal;
import java.io.IOException;
import java.util.ArrayList;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

/**
 *
 * @author hanfengwei
 */
@Named(value = "customerDeleteIBAccountManagedBean")
@RequestScoped
public class CustomerDeleteIBAccountManagedBean {
    @EJB
    private LoggingSessionBeanLocal loggingSessionBeanLocal;

    @EJB
    private CustomerAdminSessionBeanLocal customerAdminSessionBeanLocal;

    private boolean hasServices;
    private ArrayList<String> displayMessage;
    
    /**
     * Creates a new instance of CustomerDeleteIBAccountManagedBean
     */
    public CustomerDeleteIBAccountManagedBean() {
    }

    public void deleteIBAccount(ActionEvent event) throws IOException {
        System.out.println("====== infrastructure/CustomerDeleteIBAccountManagedBean: deleteIBAccount() ======");
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        CustomerBasic customer = (CustomerBasic) ec.getSessionMap().get("customer");
        customerAdminSessionBeanLocal.deleteOnlineBankingAccount(customer.getCustomerBasicId());
        loggingSessionBeanLocal.createNewLogging("customer", customer.getCustomerBasicId(), "delete online banking account", "successful", null);
        ec.redirect(ec.getRequestContextPath() + "/web/onlineBanking/infrastructure/customerLoggedOut.xhtml");
    }

    public ArrayList<String> getDisplayMessage() {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        CustomerBasic customer = (CustomerBasic) ec.getSessionMap().get("customer");
        displayMessage = customerAdminSessionBeanLocal.checkExistingService(customer.getCustomerBasicId());
        return displayMessage;
    }
}
