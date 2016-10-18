/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.infrastructure.customer;

import ejb.customer.entity.CustomerBasic;
import ejb.infrastructure.session.CustomerAdminSessionBeanLocal;
import ejb.infrastructure.session.LoggingSessionBeanLocal;
import ejb.infrastructure.session.SMSSessionBeanLocal;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import org.jboss.aerogear.security.otp.Totp;
import org.jboss.aerogear.security.otp.api.Clock;
import org.primefaces.context.RequestContext;

/**
 *
 * @author hanfengwei
 */
@Named(value = "customerReopenIBAccountManagedBean")
@ViewScoped
public class CustomerReopenIBAccountManagedBean implements Serializable {

    @EJB
    private LoggingSessionBeanLocal loggingSessionBeanLocal;
    @EJB
    private SMSSessionBeanLocal sMSSessionBeanLocal;
    @EJB
    private CustomerAdminSessionBeanLocal customerAdminSessionBeanLocal;

    private String customerNationality;
    private String customerIdentification;
    private String customerOTP;
    private CustomerBasic customer;

    /**
     * Creates a new instance of CustomerReopenIBAccountManagedBean
     */
    public CustomerReopenIBAccountManagedBean() {
    }

    public void recreateIBAccount(ActionEvent event) {
        System.out.println("=");
        System.out.println("====== infrastructure/CustomerReopenIBAccountManagedBean: recreateIBAccount() ======");
        FacesMessage message = null;
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext ec = context.getExternalContext();

        customer = customerAdminSessionBeanLocal.getCustomerByIdentificationNum(customerIdentification);
        if (customer == null) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid ID:", "Sorry we cannot find your information in our database. Your online banking account will be automatically created when you use Merlion online banking services."));
        } else if (customer.getCustomerOnlineBankingAccountNum() != null) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Account exists:", "Your online banking account has already been created. Please proceed to login."));
        } else if (customerOTP == null || customerOTP.trim().length() == 0) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid input: ", "Please enter your OTP"));
        } else {
            Clock clock = new Clock(120);
            Totp totp = new Totp(customer.getCustomerOTPSecret(), clock);
            if (totp.verify(customerOTP)) {
                System.out.println("====== infrastructure/CustomerReopenIBAccountManagedBean: recreateIBAccount(): customer verified with OTP");
                RequestContext rc = RequestContext.getCurrentInstance();
                rc.execute("PF('recreateConfirmation').show();");
                customerAdminSessionBeanLocal.recreateOnlineBankingAccount(customer.getCustomerBasicId());
                customerOTP = null;
                System.out.println("====== infrastructure/CustomerReopenIBAccountManagedBean: recreateIBAccount(): online banking account recreated");
                loggingSessionBeanLocal.createNewLogging("customer", customer.getCustomerBasicId(), "recreate online banking account", "successful", null);
            } else {
                System.out.println("====== infrastructure/CustomerReopenIBAccountManagedBean: recreateIBAccount(): customer entered wrong OTP");
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "OTP does not match: ", "That is an invalid online banking OTP. Please re-enter."));
                customerOTP = null;
            }
        }
    }

    public void sendOTP(ActionEvent event) {
        System.out.println("=");
        System.out.println("====== infrastructure/CustomerReopenIBAccountManagedBean: sendOTP() ======");
        FacesContext context = FacesContext.getCurrentInstance();

        customer = customerAdminSessionBeanLocal.getCustomerByIdentificationNum(customerIdentification);

        if (customer == null) {
            System.out.println("====== infrastructure/CustomerReopenIBAccountManagedBean: sendOTP(): no customer found");
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid ID:", "Sorry we cannot find your information in our database. Your online banking account will be automatically created when you use Merlion online banking services."));
        } else if (customer.getCustomerOnlineBankingAccountNum() != null) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Account exists:", "Your online banking account has already been created. Please proceed to login."));
        } else {
            System.out.println("====== infrastructure/CustomerReopenIBAccountManagedBean: sendOTP(): customer id verified");
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, null, "OTP has been sent to your registered mobile."));
            sMSSessionBeanLocal.sendOTP("customer", customer);
        }
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

    public String getCustomerOTP() {
        return customerOTP;
    }

    public void setCustomerOTP(String customerOTP) {
        this.customerOTP = customerOTP;
    }

}
