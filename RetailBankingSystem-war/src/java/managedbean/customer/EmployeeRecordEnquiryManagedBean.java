/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean;

import entity.CustomerBasic;
import entity.EnquiryCase;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import session.stateless.CRMCustomerSessionBeanLocal;
import session.stateless.EnquirySessionBeanLocal;

/**
 *
 * @author aaa
 */
@Named(value = "employeeRecordEnquiryManagedBean")
@SessionScoped
public class EmployeeRecordEnquiryManagedBean implements Serializable {

    @EJB
    private CRMCustomerSessionBeanLocal cRMCustomerSessionBeanLocal;

    @EJB
    private EnquirySessionBeanLocal enquirySessionBeanLocal;

    private Long caseId;
    private String caseIdStr;
    private String caseType;
    private String caseDetail;
    private String identificationNum;
    private String followUpDetail;

    private boolean visible = false;

    private CustomerBasic cb = new CustomerBasic();
    private EnquiryCase ec = new EnquiryCase();

    public EmployeeRecordEnquiryManagedBean() {
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

    public CustomerBasic getCb() {
        return cb;
    }

    public void setCb(CustomerBasic cb) {
        this.cb = cb;
    }

    public EnquiryCase getEc() {
        return ec;
    }

    public void setEc(EnquiryCase ec) {
        this.ec = ec;
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

    public String getFollowUpDetail() {
        return followUpDetail;
    }

    public void setFollowUpDetail(String followUpDetail) {
        this.followUpDetail = followUpDetail;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public List<EnquiryCase> getEnquiryCase() {
        List<EnquiryCase> enquiryCases = enquirySessionBeanLocal.getCustomerEnquiry(cb.getCustomerBasicId());
        return enquiryCases;
    }

    public void saveEnquiryCase() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(enquirySessionBeanLocal.addNewCase(cb.getCustomerBasicId(), caseType, caseDetail), " "));
        ExternalContext ec = context.getExternalContext();
        ec.redirect(ec.getRequestContextPath() + "/enquiry/counterTellerAddEnquiryDone.xhtml");
        caseType = null;
        caseDetail = null;
        identificationNum = null;
        cb = null;
    }

    public void saveFollowUp() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();
        System.out.println("///////////caseId//////"+caseId+"*******followup**** "+followUpDetail);
        context.addMessage(null, new FacesMessage(enquirySessionBeanLocal.addFollowUp(caseId, followUpDetail), " "));
        ExternalContext ec = context.getExternalContext();
        ec.redirect(ec.getRequestContextPath() + "/enquiry/counterTellerAddEnquiryDone.xhtml");
        caseId = null;
        caseIdStr = "";
        followUpDetail = null;
        identificationNum = null;
        cb = null;
        ec = null;
    }

    public void retieveCustomerByIdentification() {
        cb = cRMCustomerSessionBeanLocal.retrieveCustomerBasicByIC(identificationNum);
        visible = true;
    }

    public void retrieveCaseByCaseRef() throws IOException {
        caseId = Long.valueOf(caseIdStr);
        ec = enquirySessionBeanLocal.getEnquiryByCaseId(caseId);
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext ec = context.getExternalContext();
        ec.redirect(ec.getRequestContextPath() + "/enquiry/counterTellerSearchCaseDone.xhtml");
    }

    public void helpCustomerRecordEnquiry() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext ec = context.getExternalContext();
        ec.redirect(ec.getRequestContextPath() + "/enquiry/counterTellerAddNewCase.xhtml");
        visible = false;
    }

//    public void redirectToSearchCaseDone() throws IOException {
//        FacesContext context = FacesContext.getCurrentInstance();
//        ExternalContext ec = context.getExternalContext();
//        ec.redirect(ec.getRequestContextPath() + "/enquiry/counterTellerSearchCaseDone.xhtml");
//    }

}
