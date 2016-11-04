/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.card.customer;

import ejb.customer.entity.CustomerBasic;
import java.io.IOException;
import java.util.Calendar;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 *
 * @author aaa
 */
@Named(value = "publicCreditCardsInfoManagedBean")
@RequestScoped
public class PublicCreditCardsInfoManagedBean {

    private Long creditCardTypeId;

    public PublicCreditCardsInfoManagedBean() {
    }

    public Long getCreditCardTypeId() {
        return creditCardTypeId;
    }

    public void setCreditCardTypeId(Long creditCardTypeId) {
        this.creditCardTypeId = creditCardTypeId;
    }

    public void redirectToCustomerIdentity(Long creditCardTypeId) throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
//        ec.getFlash().put("cardTypeId", creditCardTypeId);
        ec.getSessionMap().put("cardTypeId", creditCardTypeId);
//        ec.getFlash().keep("cardTypeId");
        System.out.println("$$$$$$$$$$$$$$$$$cards info typeid = " + creditCardTypeId);
        ec.redirect(ec.getRequestContextPath() + "/web/merlionBank/creditCard/publicCustomerExistingInfo.xhtml?faces-redirect=true");
    }
    
    public void redirectToApplication(Long creditCardTypeId) throws IOException{
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
               
        CustomerBasic customer = (CustomerBasic) ec.getSessionMap().get("customer");
        String dob = customer.getCustomerDateOfBirth();
        if (getAge(dob) < 21) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "Credit card applicant must be at least 21 years old", "age"));
        } else {
        
        ec.getSessionMap().put("cardTypeId", creditCardTypeId);
        
        System.out.println("$$$$$$$$$$$$$$$$$cards info typeid = " + creditCardTypeId);
        ec.redirect(ec.getRequestContextPath() + "/web/onlineBanking/card/creditCard/customerApplyCreditCard.xhtml?faces-redirect=true");
        }
    }
    
    private int getAge(String dob) {
        Calendar now = Calendar.getInstance();
        String[] dateSplit = dob.split("/");
        System.out.println("@@@@@@@@@@@year of birth"+dateSplit[2]);
        int yearDif = now.get(Calendar.YEAR) - Integer.valueOf(dateSplit[2]);
        System.out.print("@@@@@@@@@@@@@@@now year "+ now.get(Calendar.YEAR));
        System.out.println("@@@@@@@@@@@@@integer year of birth"+ yearDif);
        
        return yearDif;
    }
}
