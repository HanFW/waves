/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean;

import ejb.customer.entity.CustomerBasic;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import ejb.customer.session.EnquirySessionBeanLocal;

/**
 *
 * @author aaa
 */
@Named(value = "enquiryManagedBean")
@Dependent
public class EnquiryManagedBean {

    @EJB
    private EnquirySessionBeanLocal enquirySessionBeanLocal;


    private Long caseId;
    private String caseType;
    private String caseDetail;
    private List<String> caseFollowUp;
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

    public List<String> getCaseFollowUp() {
        return caseFollowUp;
    }

    public void setCaseFollowUp(List<String> caseFollowUp) {
        this.caseFollowUp = caseFollowUp;
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

    public void saveEnquiryCase() {
        ec = FacesContext.getCurrentInstance().getExternalContext();
        cb = (CustomerBasic) ec.getSessionMap().get("customer");
     
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(enquirySessionBeanLocal.addNewCase(cb.getCustomerOnlineBankingAccountNum(), caseType, caseDetail), " "));
        
    }
    
    
}
