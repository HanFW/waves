/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.customer.employee;

import ejb.customer.entity.CustomerAdvanced;
import static ejb.customer.entity.CustomerAdvanced_.customerAdvancedId;
import static ejb.customer.entity.CustomerAdvanced_.education;
import static ejb.customer.entity.CustomerAdvanced_.residentialStatus;
import static ejb.customer.entity.CustomerAdvanced_.yearInResidence;
import ejb.customer.session.CRMCustomerSessionBeanLocal;
import java.io.IOException;
import javax.inject.Named;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

/**
 *
 * @author aaa
 */
@Named(value = "rMUpdateCustomerAdvancedManagedBean")
@RequestScoped
public class RMUpdateCustomerAdvancedManagedBean implements Serializable {

    @EJB
    private CRMCustomerSessionBeanLocal customerSessionBeanLocal;

    private CustomerAdvanced customer;
    private Long customerAdvancedId;

    private String jobStatus;
    private String occupation;
    private String jobIndustry;
    private double fixedIncomeMonthly;
    private double otherIncomeMonthly;
    private int numOfDependents;

    public RMUpdateCustomerAdvancedManagedBean() {
    }

    public CustomerAdvanced getCustomer() {
        customerAdvancedId = getCustomerAdvancedId();
        customer = customerSessionBeanLocal.retrieveCustomerAdvancedByAdId(customerAdvancedId);

        return customer;

    }

    public Long getCustomerAdvancedId() {
        FacesContext context = FacesContext.getCurrentInstance();
        customerAdvancedId = (Long) context.getExternalContext().getSessionMap().get("customerAdvancedId");

        return customerAdvancedId;
    }

    public String getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(String jobStatus) {
        this.jobStatus = jobStatus;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getJobIndustry() {
        return jobIndustry;
    }

    public void setJobIndustry(String jobIndustry) {
        this.jobIndustry = jobIndustry;
    }

    public double getFixedIncomeMonthly() {
        return fixedIncomeMonthly;
    }

    public void setFixedIncomeMonthly(double fixedIncomeMonthly) {
        this.fixedIncomeMonthly = fixedIncomeMonthly;
    }

    public double getOtherIncomeMonthly() {
        return otherIncomeMonthly;
    }

    public void setOtherIncomeMonthly(double otherIncomeMonthly) {
        this.otherIncomeMonthly = otherIncomeMonthly;
    }

    public int getNumOfDependents() {
        return numOfDependents;
    }

    public void setNumOfDependents(int numOfDependents) {
        this.numOfDependents = numOfDependents;
    }

    public void updateCustomerAdvanced(ActionEvent event) {
        FacesMessage message = null;
        FacesContext context = FacesContext.getCurrentInstance();

        customerAdvancedId = getCustomerAdvancedId();
        customerSessionBeanLocal.RMUpdateCustomerAdvancedInfo(customerAdvancedId, jobStatus, occupation, jobIndustry, fixedIncomeMonthly, otherIncomeMonthly, numOfDependents);

        message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Advanced profile of customer has been successfully updated!", null);
        context.addMessage(null, message);

    }

    public void redirectToViewAdvanced() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext ec = context.getExternalContext();
        ec.redirect(ec.getRequestContextPath() + "/web/internalSystem/CRM/RMUpdateCustomerAdvanced.xhtml?faces-redirect=true");
    }
}
