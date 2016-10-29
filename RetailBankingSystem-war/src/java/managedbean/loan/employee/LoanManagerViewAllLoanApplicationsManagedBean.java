/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.loan.employee;

import ejb.loan.entity.LoanApplication;
import ejb.loan.session.LoanApplicationSessionBeanLocal;
import java.io.Serializable;
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
@Named(value = "loanManagerStartNewLoanManagedBean")
@ViewScoped
public class LoanManagerViewAllLoanApplicationsManagedBean implements Serializable{
    @EJB
    private LoanApplicationSessionBeanLocal loanApplicationSessionBeanLocal;
    private List<LoanApplication> approvedLoanApplications;
    private List<LoanApplication> startedLoanApplications;
    private List<LoanApplication> pendingLoanApplications;
    private List<LoanApplication> inProgressLoanApplications;
    
    /**
     * Creates a new instance of LoanManagerStartNewLoanManagedBean
     */
    public LoanManagerViewAllLoanApplicationsManagedBean() {
    }
    
    @PostConstruct
    public void init() {
        approvedLoanApplications = loanApplicationSessionBeanLocal.getAllApprovedLoans();
        startedLoanApplications = loanApplicationSessionBeanLocal.getAllStartedLoans();
        pendingLoanApplications = loanApplicationSessionBeanLocal.getAllLoanApplications();
        inProgressLoanApplications = loanApplicationSessionBeanLocal.getAllInProgressLoans();
    }
    
    public void startNewLoan(Long applicationId){
        loanApplicationSessionBeanLocal.startNewLoan(applicationId);
        FacesContext context = FacesContext.getCurrentInstance();
        String msg = "Loan - " + applicationId + " start successfully";
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null);
        context.addMessage(null, message);
        approvedLoanApplications = loanApplicationSessionBeanLocal.getAllApprovedLoans();
        startedLoanApplications = loanApplicationSessionBeanLocal.getAllStartedLoans();
        pendingLoanApplications = loanApplicationSessionBeanLocal.getAllLoanApplications();
        inProgressLoanApplications = loanApplicationSessionBeanLocal.getAllInProgressLoans();
    }

    public LoanApplicationSessionBeanLocal getLoanApplicationSessionBeanLocal() {
        return loanApplicationSessionBeanLocal;
    }

    public void setLoanApplicationSessionBeanLocal(LoanApplicationSessionBeanLocal loanApplicationSessionBeanLocal) {
        this.loanApplicationSessionBeanLocal = loanApplicationSessionBeanLocal;
    }

    public List<LoanApplication> getApprovedLoanApplications() {
        return approvedLoanApplications;
    }

    public void setApprovedLoanApplications(List<LoanApplication> approvedLoanApplications) {
        this.approvedLoanApplications = approvedLoanApplications;
    }

    public List<LoanApplication> getStartedLoanApplications() {
        return startedLoanApplications;
    }

    public void setStartedLoanApplications(List<LoanApplication> startedLoanApplications) {
        this.startedLoanApplications = startedLoanApplications;
    }

    public List<LoanApplication> getPendingLoanApplications() {
        return pendingLoanApplications;
    }

    public void setPendingLoanApplications(List<LoanApplication> pendingLoanApplications) {
        this.pendingLoanApplications = pendingLoanApplications;
    }

    public List<LoanApplication> getInProgressLoanApplications() {
        return inProgressLoanApplications;
    }

    public void setInProgressLoanApplications(List<LoanApplication> inProgressLoanApplications) {
        this.inProgressLoanApplications = inProgressLoanApplications;
    }

}
