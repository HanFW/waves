/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.customer.customer;

import ejb.customer.session.EnquirySessionBeanLocal;
import java.io.IOException;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@Named(value = "customerFollowUpManagedBean")
@SessionScoped

public class CustomerFollowUpManagedBean implements Serializable {

    @EJB
    private EnquirySessionBeanLocal enquirySessionBeanLocal;

    private Long caseId;
    private String followUpDetail;

    public CustomerFollowUpManagedBean() {
    }

    public EnquirySessionBeanLocal getEnquirySessionBeanLocal() {
        return enquirySessionBeanLocal;
    }

    public void setEnquirySessionBeanLocal(EnquirySessionBeanLocal enquirySessionBeanLocal) {
        this.enquirySessionBeanLocal = enquirySessionBeanLocal;
    }

    public Long getCaseId() {
        return caseId;
    }

    public void setCaseId(Long caseId) {
        this.caseId = caseId;
        System.out.println("set case id: " + caseId);
    }

    public String getFollowUpDetail() {
        return followUpDetail;
    }

    public void setFollowUpDetail(String followUpDetail) {
        this.followUpDetail = followUpDetail;
    }

    public void saveFollowUp() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(enquirySessionBeanLocal.addFollowUp(caseId, followUpDetail), " "));
        caseId = null;
        followUpDetail = null;
        
        ExternalContext ec = context.getExternalContext();
        ec.redirect(ec.getRequestContextPath() + "/web/onlineBanking/customer/customerEnquiryDone.xhtml?faces-redirect=true");
    }
}
