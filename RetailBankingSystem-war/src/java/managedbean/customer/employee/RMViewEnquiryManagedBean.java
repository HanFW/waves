/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.customer.employee;

import ejb.customer.entity.EnquiryCase;
import ejb.customer.session.EnquirySessionBeanLocal;
import ejb.infrastructure.entity.Employee;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.inject.Named;
import javax.faces.context.FacesContext;

/**
 *
 * @author aaa
 */
@Named(value = "rMViewEnquiryManagedBean")
@SessionScoped
public class RMViewEnquiryManagedBean implements Serializable {

    /**
     * Creates a new instance of RMViewEnquiryManagedBean
     */
    @EJB
    private EnquirySessionBeanLocal enquirySessionBeanLocal;

    private Employee rm;
    private Long caseId;
    private String solution;

    public RMViewEnquiryManagedBean() {
    }

    public List<EnquiryCase> getPendingWMCase() {
        Long rmId = getEmployeeViaSessionMap().getEmployeeId();
        List<EnquiryCase> pendingCase = enquirySessionBeanLocal.getCustomerWMEnquiry(rmId);
        return pendingCase;
    }

    public Employee getEmployeeViaSessionMap() {
        FacesContext context = FacesContext.getCurrentInstance();
        rm = (Employee) context.getExternalContext().getSessionMap().get("employee");

        return rm;
    }

    public void redirectToReplyPage() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext ec = context.getExternalContext();
        ec.redirect(ec.getRequestContextPath() + "/web/internalSystem/enquiry/rMReplyEnquiryCase.xhtml?faces-redirect=true");
    }
    
    public void rmReplyCase() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext ec = context.getExternalContext();
        String msg = enquirySessionBeanLocal.replyWMCase(caseId, solution);
        context.addMessage(null, new FacesMessage(msg, " "));
        caseId = null;
        solution = null;
        ec.redirect(ec.getRequestContextPath() + "/web/internalSystem/enquiry/rMViewPendingCase.xhtml?faces-redirect=true");
               
    }
    
    public String getCaseDetail(){
        String detail = enquirySessionBeanLocal.getCustomerEnquiryDetail(caseId);
        return detail;
    }

    public EnquirySessionBeanLocal getEnquirySessionBeanLocal() {
        return enquirySessionBeanLocal;
    }

    public void setEnquirySessionBeanLocal(EnquirySessionBeanLocal enquirySessionBeanLocal) {
        this.enquirySessionBeanLocal = enquirySessionBeanLocal;
    }

    public Employee getRm() {
        return rm;
    }

    public void setRm(Employee rm) {
        this.rm = rm;
    }

    public Long getCaseId() {
        return caseId;
    }

    public void setCaseId(Long caseId) {
        this.caseId = caseId;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }
    
    

}
