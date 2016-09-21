/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean;

import entity.CustomerBasic;
import java.io.IOException;
import java.io.Serializable;
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
public class EmployeeRecordEnquiryManagedBean implements Serializable{

    @EJB
    private CRMCustomerSessionBeanLocal cRMCustomerSessionBeanLocal;
    
    @EJB
    private EnquirySessionBeanLocal enquirySessionBeanLocal;
    
    private String caseType;
    private String caseDetail;
    private String identificationNum;
    
    private CustomerBasic cb = new CustomerBasic();

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
    
    
    

    public void saveEnquiryCase() {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(enquirySessionBeanLocal.addNewCase(cb.getCustomerBasicId(), caseType, caseDetail), " "));
        caseType = null;
        caseDetail = null;
        cb = null;
    }
    
    public void retieveCustomerByIdentification () throws IOException {
        cb = cRMCustomerSessionBeanLocal.retrieveCustomerBasicByIC(identificationNum);
        System.out.println("////////"+cb.getCustomerName() +"////"+cb.getCustomerIdentificationNum()+"////////");
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext ec = context.getExternalContext();
        ec.redirect(ec.getRequestContextPath() + "/enquiry/counterTellerAddNewCase.xhtml");
    }
}
