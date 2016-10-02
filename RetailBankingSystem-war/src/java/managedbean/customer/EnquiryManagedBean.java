/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.customer;

import ejb.customer.entity.CustomerBasic;
import ejb.customer.entity.EnquiryCase;
import ejb.customer.entity.FollowUp;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import ejb.customer.session.EnquirySessionBeanLocal;

/**
 *
 * @author aaa
 */
@Named(value = "enquiryManagedBean")
@SessionScoped
public class EnquiryManagedBean implements Serializable {

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

    public EnquiryManagedBean() {
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



    public String getFollowUpDetail() {
        return followUpDetail;
    }

    public void setFollowUpDetail(String followUpDetail) {
        this.followUpDetail = followUpDetail;
    }
    
        
    public List<FollowUp> retrieveFollowUpByCaseId() {
        List<FollowUp> fu = enquirySessionBeanLocal.getFollowUpByCaseId(caseId);
        return fu;
    }

    public void saveEnquiryCase() {
        ec = FacesContext.getCurrentInstance().getExternalContext();
        cb = (CustomerBasic) ec.getSessionMap().get("customer");
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(enquirySessionBeanLocal.addNewCase(cb.getCustomerBasicId(), caseType, caseDetail), " "));
        caseType = null;
        caseDetail = null;
        cb = null;
    }

    public List<EnquiryCase> getEnquiryCase() {
        ec = FacesContext.getCurrentInstance().getExternalContext();
        cb = (CustomerBasic) ec.getSessionMap().get("customer");
        List<EnquiryCase> enquiryCases = enquirySessionBeanLocal.getCustomerEnquiry(cb.getCustomerBasicId());

        return enquiryCases;
    }

    public void saveFollowUp() {

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(enquirySessionBeanLocal.addFollowUp(caseId, followUpDetail), " "));
        caseId = null;
        followUpDetail = null;
    }

}
