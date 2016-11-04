/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.loan.employee;

import ejb.loan.entity.LoanApplication;
import ejb.loan.session.LoanApplicationSessionBeanLocal;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author hanfengwei
 */
@Named(value = "loanManagerViewAllLoanApplicationsManagedBean")
@ViewScoped
public class LoanManagerViewAllLoanApplicationsManagedBean implements Serializable{
    @EJB
    private LoanApplicationSessionBeanLocal loanApplicationSessionBeanLocal;
    private List<LoanApplication> appraisalApplications;
    private List<LoanApplication> pendingMortgageApplications;
    private List<LoanApplication> inprogressMortgageApplications;
    private List<LoanApplication> approvedMortgageApplications;

    /**
     * Creates a new instance of LoanManagerStartNewLoanManagedBean
     */
    public LoanManagerViewAllLoanApplicationsManagedBean() {
    }
    
    @PostConstruct
    public void init() {
        ArrayList<String> status = new ArrayList<String>();
        status.add("waiting for valuation");
        appraisalApplications = loanApplicationSessionBeanLocal.getLoanApplications(status, "mortgage");
        status = new ArrayList<String>();
        status.add("pending");
        pendingMortgageApplications = loanApplicationSessionBeanLocal.getLoanApplications(status, "mortgage");
        status = new ArrayList<String>();
        status.add("in progress");
        inprogressMortgageApplications = loanApplicationSessionBeanLocal.getLoanApplications(status, "mortgage");
        status = new ArrayList<String>();
        status.add("approved");
        approvedMortgageApplications = loanApplicationSessionBeanLocal.getLoanApplications(status, "mortgage");
    }
    
    public void startNewLoan(Long applicationId){
        loanApplicationSessionBeanLocal.startNewLoan(applicationId);
        FacesContext context = FacesContext.getCurrentInstance();
        String msg = "Loan - " + applicationId + " start successfully";
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null);
        context.addMessage(null, message);
        ArrayList<String> status = new ArrayList<String>();
        status.add("waiting for valuation");
        appraisalApplications = loanApplicationSessionBeanLocal.getLoanApplications(status, "mortgage");
        status = new ArrayList<String>();
        status.add("pending");
        pendingMortgageApplications = loanApplicationSessionBeanLocal.getLoanApplications(status, "mortgage");
        status = new ArrayList<String>();
        status.add("in progress");
        inprogressMortgageApplications = loanApplicationSessionBeanLocal.getLoanApplications(status, "mortgage");
        status = new ArrayList<String>();
        status.add("approved");
        approvedMortgageApplications = loanApplicationSessionBeanLocal.getLoanApplications(status, "mortgage");
    }

    public LoanApplicationSessionBeanLocal getLoanApplicationSessionBeanLocal() {
        return loanApplicationSessionBeanLocal;
    }

    public void setLoanApplicationSessionBeanLocal(LoanApplicationSessionBeanLocal loanApplicationSessionBeanLocal) {
        this.loanApplicationSessionBeanLocal = loanApplicationSessionBeanLocal;
    }

    public List<LoanApplication> getAppraisalApplications() {
        return appraisalApplications;
    }

    public void setAppraisalApplications(List<LoanApplication> appraisalApplications) {
        this.appraisalApplications = appraisalApplications;
    }

    public List<LoanApplication> getPendingMortgageApplications() {
        return pendingMortgageApplications;
    }

    public void setPendingMortgageApplications(List<LoanApplication> pendingMortgageApplications) {
        this.pendingMortgageApplications = pendingMortgageApplications;
    }

    public List<LoanApplication> getInprogressMortgageApplications() {
        return inprogressMortgageApplications;
    }

    public void setInprogressMortgageApplications(List<LoanApplication> inprogressMortgageApplications) {
        this.inprogressMortgageApplications = inprogressMortgageApplications;
    }

    public List<LoanApplication> getApprovedMortgageApplications() {
        return approvedMortgageApplications;
    }

    public void setApprovedMortgageApplications(List<LoanApplication> approvedMortgageApplications) {
        this.approvedMortgageApplications = approvedMortgageApplications;
    }
    
}
