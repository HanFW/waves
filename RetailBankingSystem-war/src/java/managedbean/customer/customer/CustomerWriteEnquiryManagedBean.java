/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.customer.customer;

import ejb.customer.entity.CustomerBasic;
import ejb.customer.session.EnquirySessionBeanLocal;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 *
 * @author aaa
 */
@Named(value = "customerWriteEnquiryManagedBean")
@RequestScoped
public class CustomerWriteEnquiryManagedBean {

    @EJB
    private EnquirySessionBeanLocal enquirySessionBeanLocal;

    private Long caseId;
    private String caseType;
    private String caseDetail;

    private String followUpDetail;
    private String caseStatus;
    private String onlineBankingAccountNum;

    private ExternalContext ec;

    private CustomerBasic cb = new CustomerBasic();

    public CustomerWriteEnquiryManagedBean() {
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

    public String getFollowUpDetail() {
        return followUpDetail;
    }

    public void setFollowUpDetail(String followUpDetail) {
        this.followUpDetail = followUpDetail;
    }

    public String getCaseStatus() {
        return caseStatus;
    }

    public void setCaseStatus(String caseStatus) {
        this.caseStatus = caseStatus;
    }

    public String getOnlineBankingAccountNum() {
        return onlineBankingAccountNum;
    }

    public void setOnlineBankingAccountNum(String onlineBankingAccountNum) {
        this.onlineBankingAccountNum = onlineBankingAccountNum;
    }

    public ExternalContext getEc() {
        return ec;
    }

    public void setEc(ExternalContext ec) {
        this.ec = ec;
    }

    public CustomerBasic getCb() {
        return cb;
    }

    public void setCb(CustomerBasic cb) {
        this.cb = cb;
    }
 
    public void saveEnquiryCase() {
        ec = FacesContext.getCurrentInstance().getExternalContext();
        cb = (CustomerBasic) ec.getSessionMap().get("customer");
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(enquirySessionBeanLocal.addNewCase(cb.getCustomerBasicId(), caseType, caseDetail), " "));
    }
}
