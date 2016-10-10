/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.infrastructure.customer;

import ejb.customer.entity.CustomerBasic;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.event.ActionEvent;
import ejb.infrastructure.session.SMSSessionBeanLocal;
import java.io.IOException;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import org.jboss.aerogear.security.otp.Totp;
import org.jboss.aerogear.security.otp.api.Clock;

/**
 *
 * @author hanfengwei
 */
@Named(value = "sMSManagedBean")
@ViewScoped
public class SMSManagedBean implements Serializable {

    @EJB
    private SMSSessionBeanLocal sMSSessionBeanLocal;
    private String customerOTP;

    /**
     * Creates a new instance of SMSManagedBean
     */
    public SMSManagedBean() {
    }

    public void sendOTP(ActionEvent event) {
        System.out.println("=");
        System.out.println("====== infrastructure/SMSManagedBean: sendOTP() ======");
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        sMSSessionBeanLocal.sendOTP("customer", (CustomerBasic) ec.getSessionMap().get("customer"));
    }

    public String getCustomerOTP() {
        return customerOTP;
    }

    public void setCustomerOTP(String customerOTP) {
        this.customerOTP = customerOTP;
    }

    public void verifyOTP(ActionEvent event) throws IOException {
        System.out.println("=");
        System.out.println("====== infrastructure/SMSManagedBean: verifyOTP() ======");

        FacesContext context = FacesContext.getCurrentInstance();

        if (customerOTP == null || customerOTP.trim().length() == 0) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid input: ", "Please enter your OTP"));
        } else {
            CustomerBasic customer = (CustomerBasic) context.getExternalContext().getSessionMap().get("customer");
            Clock clock = new Clock(120);
            Totp totp = new Totp(customer.getCustomerOTPSecret(), clock);
            if (totp.verify(customerOTP)) {
                System.out.println("====== infrastructure/SMSManagedBean: verifyOTP(): OTP correct - redirect customer to destination page");
                customerOTP = null;
                //redirect customer to the destination page
                context.getExternalContext().getSessionMap().put("isVerified", "true");
                System.out.println("====== infrastructure/SMSManagedBean: verifyOTP(): customer is verified");
                context.getExternalContext().redirect(context.getExternalContext().getRequestContextPath() + "/web/onlineBanking/OTP");
            } else {
                System.out.println("====== infrastructure/SMSManagedBean: verifyOTP(): OTP does not match");
                customerOTP = null;
                context.getExternalContext().getSessionMap().put("isVerified", "false");
                System.out.println("====== infrastructure/SMSManagedBean: verifyOTP(): set customer verified status to false");
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "OTP does not match: ", "That is an invalid online banking OTP. Please re-enter."));
            }
        }
    }

}
