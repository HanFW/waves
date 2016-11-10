/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.infrastructure.employee;

import ejb.customer.entity.CustomerBasic;
import ejb.infrastructure.entity.Employee;
import ejb.infrastructure.session.CustomerAdminSessionBeanLocal;
import ejb.infrastructure.session.LoggingSessionBeanLocal;
import java.io.IOException;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

/**
 *
 * @author hanfengwei
 */
@Named(value = "employeeUnlockIBAccountManagedBean")
@SessionScoped
public class EmployeeUnlockIBAccountManagedBean implements Serializable {

    @EJB
    private CustomerAdminSessionBeanLocal customerAdminSessionBeanLocal;
    @EJB
    private LoggingSessionBeanLocal loggingSessionBeanLocal;

    private String customerIdentificationNum;
    private CustomerBasic customer;
    private boolean displayCustomer;
    private Employee employee;

    /**
     * Creates a new instance of EmployeeUnlockIBAccountManagedBean
     */
    public EmployeeUnlockIBAccountManagedBean() {
    }

    public void showCustomer(ActionEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext ec = context.getExternalContext();
        customer = customerAdminSessionBeanLocal.getCustomerByIdentificationNum(customerIdentificationNum);
        if (customer == null) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Cannot find customer", null));
        } else {
            displayCustomer = true;
        }
    }

    public void unlockAccount(ActionEvent event) throws IOException {
        System.out.println("====== infrastructure/EmployeeUnlockIBAccountManagedBean: unlockAccount() ======");
        System.out.println(customer);

        FacesContext context = FacesContext.getCurrentInstance();

        customerAdminSessionBeanLocal.unlockCustomerOnlineBankingAccount(customer.getCustomerBasicId());
        customer = customerAdminSessionBeanLocal.getCustomerByIdentificationNum(customerIdentificationNum);
        loggingSessionBeanLocal.createNewLogging("employee", getEmployeeViaSessionMap(), "employee unclock customer account", "success", customer.getCustomerName());
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Online banking account unlocked", null));
        customerIdentificationNum = null;
        customer = null;

    }

    public String getCustomerIdentificationNum() {
        return customerIdentificationNum;
    }

    public void setCustomerIdentificationNum(String customerIdentificationNum) {
        this.customerIdentificationNum = customerIdentificationNum;
    }

    public CustomerAdminSessionBeanLocal getCustomerAdminSessionBeanLocal() {
        return customerAdminSessionBeanLocal;
    }

    public void setCustomerAdminSessionBeanLocal(CustomerAdminSessionBeanLocal customerAdminSessionBeanLocal) {
        this.customerAdminSessionBeanLocal = customerAdminSessionBeanLocal;
    }

    public CustomerBasic getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerBasic customer) {
        this.customer = customer;
    }

    public boolean isDisplayCustomer() {
        return displayCustomer;
    }

    public void setDisplayCustomer(boolean displayCustomer) {
        this.displayCustomer = displayCustomer;
    }

    private Long getEmployeeViaSessionMap() {
        Long employeeId;
        FacesContext context = FacesContext.getCurrentInstance();
        employee = (Employee) context.getExternalContext().getSessionMap().get("employee");
        employeeId = employee.getEmployeeId();

        return employeeId;
    }
}
