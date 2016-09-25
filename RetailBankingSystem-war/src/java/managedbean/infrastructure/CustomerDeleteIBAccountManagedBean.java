/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.infrastructure;

import ejb.customer.entity.CustomerBasic;
import ejb.infrastructure.session.CustomerAdminSessionBeanLocal;
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
    private CustomerAdminSessionBeanLocal customerAdminSessionBeanLocal;

    private boolean hasServices;

    /**
     * Creates a new instance of CustomerDeleteIBAccountManagedBean
     */
    public CustomerDeleteIBAccountManagedBean() {
    }

    public void deleteIBAccount(ActionEvent event) throws IOException {
        System.out.println("====== infrastructure/CustomerDeleteIBAccountManagedBean: deleteIBAccount() ======");
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(ec.getRequestContextPath() + "/web/onlineBanking/infrastructure/customerLoggedOut.xhtml");
    }

    public String getDisplayMessage() {
        System.out.println("====== infrastructure/CustomerDeleteIBAccountManagedBean: getDisplayMessage() ======");
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        String displayMessage = "We are still serving you on the following services: \n";
        CustomerBasic customer = (CustomerBasic) ec.getSessionMap().get("customer");
        ArrayList services = customerAdminSessionBeanLocal.checkExistingService(Long.getLong("1"));
        
        if (services.isEmpty()) {
            hasServices = false;
        } else {
            hasServices = true;
        }
        return displayMessage;
    }

    public boolean isHasServices() {
        return hasServices;
    }

    public void setHasServices(boolean hasServices) {
        this.hasServices = hasServices;
    }

}
