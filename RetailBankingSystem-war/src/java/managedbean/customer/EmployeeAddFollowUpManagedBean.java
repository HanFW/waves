/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.customer;

import ejb.customer.entity.CustomerBasic;
import ejb.customer.entity.EnquiryCase;
import ejb.customer.session.EnquirySessionBeanLocal;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 *
 * @author aaa
 */
@Named(value = "employeeAddFollowUpManagedBean")
@SessionScoped
public class EmployeeAddFollowUpManagedBean implements Serializable {

    @EJB
    private EnquirySessionBeanLocal enquirySessionBeanLocal;

    private Long caseId;
    private String caseIdStr;
    private String caseType;
    private String caseDetail;
    private String identificationNum;
    private String followUpDetail;

    private CustomerBasic cb = new CustomerBasic();
    private EnquiryCase enquiry = new EnquiryCase();

    public EmployeeAddFollowUpManagedBean() {
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
    }

    public String getCaseIdStr() {
        return caseIdStr;
    }

    public void setCaseIdStr(String caseIdStr) {
        this.caseIdStr = caseIdStr;
    }

    public String getCaseType() {
        return caseType;
    }

    public void setCaseType(String caseType) {
        this.caseType = caseType;
    }

    public String getCaseDetail() {
        return caseDetail;
    }

    public void setCaseDetail(String caseDetail) {
        this.caseDetail = caseDetail;
    }

    public String getIdentificationNum() {
        return identificationNum;
    }

    public void setIdentificationNum(String identificationNum) {
        this.identificationNum = identificationNum;
    }

    public String getFollowUpDetail() {
        return followUpDetail;
    }

    public void setFollowUpDetail(String followUpDetail) {
        this.followUpDetail = followUpDetail;
    }

    public CustomerBasic getCb() {
        return cb;
    }

    public void setCb(CustomerBasic cb) {
        this.cb = cb;
    }

    public EnquiryCase getEnquiry() {
        return enquiry;
    }

    public void setEnquiry(EnquiryCase enquiry) {
        this.enquiry = enquiry;
    }

    
    public void saveFollowUp() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(enquirySessionBeanLocal.addFollowUp(caseId, followUpDetail), " "));
        ExternalContext ec = context.getExternalContext();
        ec.redirect(ec.getRequestContextPath() + "/web/internalSystem/enquiry/counterTellerAddEnquiryDone.xhtml");
        caseId = null;
        caseIdStr = "";
        followUpDetail = null;
        identificationNum = null;
        cb = null;
        enquiry = null;
    }
}
