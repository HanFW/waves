/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.customer.employee;

import ejb.customer.entity.CustomerBasic;
import ejb.customer.session.CRMCustomerSessionBeanLocal;
import java.io.IOException;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 *
 * @author aaa
 */
@Named(value = "counterTellerSearchCustomerManagedBean")
@SessionScoped
public class CounterTellerSearchCustomerManagedBean implements Serializable {

    @EJB
    private CRMCustomerSessionBeanLocal cRMCustomerSessionBeanLocal;

    private String identificationNum;
    private boolean visible = false;

    private CustomerBasic cb = new CustomerBasic();

    public CounterTellerSearchCustomerManagedBean() {
    }

    public CRMCustomerSessionBeanLocal getcRMCustomerSessionBeanLocal() {
        return cRMCustomerSessionBeanLocal;
    }

    public void setcRMCustomerSessionBeanLocal(CRMCustomerSessionBeanLocal cRMCustomerSessionBeanLocal) {
        this.cRMCustomerSessionBeanLocal = cRMCustomerSessionBeanLocal;
    }

    public String getIdentificationNum() {
        return identificationNum;
    }

    public void setIdentificationNum(String identificationNum) {
        this.identificationNum = identificationNum;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public CustomerBasic getCb() {
        return cb;
    }

    public void setCb(CustomerBasic cb) {
        this.cb = cb;
    }

    public void retieveCustomerByIdentification() {
        FacesMessage message = null;
        FacesContext context = FacesContext.getCurrentInstance();
        cb = cRMCustomerSessionBeanLocal.retrieveCustomerBasicByIC(identificationNum);
        if (cb.getCustomerBasicId() != null) {
            visible = true;
            identificationNum = null;
        } else {
            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Incorrect identification number!", "No customer found");
            context.addMessage(null, message);
        }
    }

    public void helpCustomerRecordEnquiry() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext ec = context.getExternalContext();

        ec.getRequestMap().put("customerBasic", cb);
        ec.redirect(ec.getRequestContextPath() + "/web/internalSystem/enquiry/counterTellerAddNewCase.xhtml?faces-redirect=true");
        visible = false;
    }

    public void helpCustomerChangeBasicInfo() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext ec = context.getExternalContext();
        ec.getRequestMap().put("customerBasic", cb);
        ec.redirect(ec.getRequestContextPath() + "/web/internalSystem/CRM/counterTellerUpdateCustomerBasic.xhtml?faces-redirect=true");
        visible = false;
    }
}
