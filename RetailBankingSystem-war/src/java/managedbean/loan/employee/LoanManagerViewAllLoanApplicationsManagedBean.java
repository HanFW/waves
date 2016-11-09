/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.loan.employee;

import ejb.loan.entity.CashlineApplication;
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
    
    private List<LoanApplication> pendingRenovationApplications;
    private List<LoanApplication> inprogressRenovationApplications;
    private List<LoanApplication> approvedRenovationApplications;
    
    private List<LoanApplication> pendingCarApplications;
    private List<LoanApplication> inprogressCarApplications;
    private List<LoanApplication> approvedCarApplications;
    
    private List<LoanApplication> pendingEducationApplications;
    private List<LoanApplication> inprogressEducationApplications;
    private List<LoanApplication> approvedEducationApplications;
    
    private List<CashlineApplication> pendingCashlineApplications;
    private List<CashlineApplication> inprogressCashlineApplications;
    private List<CashlineApplication> approvedCashlineApplications;
    

    /**
     * Creates a new instance of LoanManagerStartNewLoanManagedBean
     */
    public LoanManagerViewAllLoanApplicationsManagedBean() {
    }
    
    @PostConstruct
    public void init() {
        ArrayList<String> status = new ArrayList<String>();
        
        //get all mortgage loan applications
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
        
        //get all renovation loan applications
        status = new ArrayList<String>();
        status.add("pending");
        pendingRenovationApplications = loanApplicationSessionBeanLocal.getLoanApplications(status, "Renovation Loan");
        status = new ArrayList<String>();
        status.add("in progress");
        inprogressRenovationApplications = loanApplicationSessionBeanLocal.getLoanApplications(status, "Renovation Loan");
        status = new ArrayList<String>();
        status.add("approved");
        approvedRenovationApplications = loanApplicationSessionBeanLocal.getLoanApplications(status, "Renovation Loan");
        
        status = new ArrayList<String>();
        status.add("pending");
        pendingCarApplications = loanApplicationSessionBeanLocal.getLoanApplications(status, "Car Loan");
        status = new ArrayList<String>();
        status.add("in progress");
        inprogressCarApplications = loanApplicationSessionBeanLocal.getLoanApplications(status, "Car Loan");
        status = new ArrayList<String>();
        status.add("approved");
        approvedCarApplications = loanApplicationSessionBeanLocal.getLoanApplications(status, "Car Loan");
        
        status = new ArrayList<String>();
        status.add("pending");
        pendingEducationApplications = loanApplicationSessionBeanLocal.getLoanApplications(status, "Education Loan");
        status = new ArrayList<String>();
        status.add("in progress");
        inprogressEducationApplications = loanApplicationSessionBeanLocal.getLoanApplications(status, "Education Loan");
        status = new ArrayList<String>();
        status.add("approved");
        approvedEducationApplications = loanApplicationSessionBeanLocal.getLoanApplications(status, "Education Loan");
        
        status = new ArrayList<String>();
        status.add("pending");
        pendingCashlineApplications = loanApplicationSessionBeanLocal.getCashlineApplications(status);
        status = new ArrayList<String>();
        status.add("in progress");
        inprogressCashlineApplications = loanApplicationSessionBeanLocal.getCashlineApplications(status);
        status = new ArrayList<String>();
        status.add("approved");
        approvedCashlineApplications = loanApplicationSessionBeanLocal.getCashlineApplications(status);
    }
    
    public void startNewLoan(Long applicationId, String loanType){
        loanApplicationSessionBeanLocal.startNewLoan(applicationId, loanType);
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
   
        status = new ArrayList<String>();
        status.add("pending");
        pendingCarApplications = loanApplicationSessionBeanLocal.getLoanApplications(status, "Car Loan");
        status = new ArrayList<String>();
        status.add("in progress");
        inprogressCarApplications = loanApplicationSessionBeanLocal.getLoanApplications(status, "Car Loan");
        status = new ArrayList<String>();
        status.add("approved");
        approvedCarApplications = loanApplicationSessionBeanLocal.getLoanApplications(status, "Car Loan");
        
        //get all renovation loan applications
        status = new ArrayList<String>();
        status.add("pending");
        pendingRenovationApplications = loanApplicationSessionBeanLocal.getLoanApplications(status, "Renovation Loan");
        status = new ArrayList<String>();
        status.add("in progress");
        inprogressRenovationApplications = loanApplicationSessionBeanLocal.getLoanApplications(status, "Renovation Loan");
        status = new ArrayList<String>();
        status.add("approved");
        approvedRenovationApplications = loanApplicationSessionBeanLocal.getLoanApplications(status, "Renovation Loan");
        
        status = new ArrayList<String>();
        status.add("pending");
        pendingEducationApplications = loanApplicationSessionBeanLocal.getLoanApplications(status, "Education Loan");
        status = new ArrayList<String>();
        status.add("in progress");
        inprogressEducationApplications = loanApplicationSessionBeanLocal.getLoanApplications(status, "Education Loan");
        status = new ArrayList<String>();
        status.add("approved");
        approvedEducationApplications = loanApplicationSessionBeanLocal.getLoanApplications(status, "Education Loan");
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

    public List<LoanApplication> getPendingRenovationApplications() {
        return pendingRenovationApplications;
    }

    public void setPendingRenovationApplications(List<LoanApplication> pendingRenovationApplications) {
        this.pendingRenovationApplications = pendingRenovationApplications;
    }

    public List<LoanApplication> getInprogressRenovationApplications() {
        return inprogressRenovationApplications;
    }

    public void setInprogressRenovationApplications(List<LoanApplication> inprogressRenovationApplications) {
        this.inprogressRenovationApplications = inprogressRenovationApplications;
    }

    public List<LoanApplication> getApprovedRenovationApplications() {
        return approvedRenovationApplications;
    }

    public void setApprovedRenovationApplications(List<LoanApplication> approvedRenovationApplications) {
        this.approvedRenovationApplications = approvedRenovationApplications;
    }

    public List<LoanApplication> getPendingCarApplications() {
        return pendingCarApplications;
    }

    public void setPendingCarApplications(List<LoanApplication> pendingCarApplications) {
        this.pendingCarApplications = pendingCarApplications;
    }

    public List<LoanApplication> getInprogressCarApplications() {
        return inprogressCarApplications;
    }

    public void setInprogressCarApplications(List<LoanApplication> inprogressCarApplications) {
        this.inprogressCarApplications = inprogressCarApplications;
    }

    public List<LoanApplication> getApprovedCarApplications() {
        return approvedCarApplications;
    }

    public void setApprovedCarApplications(List<LoanApplication> approvedCarApplications) {
        this.approvedCarApplications = approvedCarApplications;
    }

    public List<LoanApplication> getPendingEducationApplications() {
        return pendingEducationApplications;
    }

    public void setPendingEducationApplications(List<LoanApplication> pendingEducationApplications) {
        this.pendingEducationApplications = pendingEducationApplications;
    }

    public List<LoanApplication> getInprogressEducationApplications() {
        return inprogressEducationApplications;
    }

    public void setInprogressEducationApplications(List<LoanApplication> inprogressEducationApplications) {
        this.inprogressEducationApplications = inprogressEducationApplications;
    }

    public List<LoanApplication> getApprovedEducationApplications() {
        return approvedEducationApplications;
    }

    public void setApprovedEducationApplications(List<LoanApplication> approvedEducationApplications) {
        this.approvedEducationApplications = approvedEducationApplications;
    }

    public List<CashlineApplication> getPendingCashlineApplications() {
        return pendingCashlineApplications;
    }

    public void setPendingCashlineApplications(List<CashlineApplication> pendingCashlineApplications) {
        this.pendingCashlineApplications = pendingCashlineApplications;
    }

    public List<CashlineApplication> getInprogressCashlineApplications() {
        return inprogressCashlineApplications;
    }

    public void setInprogressCashlineApplications(List<CashlineApplication> inprogressCashlineApplications) {
        this.inprogressCashlineApplications = inprogressCashlineApplications;
    }

    public List<CashlineApplication> getApprovedCashlineApplications() {
        return approvedCashlineApplications;
    }

    public void setApprovedCashlineApplications(List<CashlineApplication> approvedCashlineApplications) {
        this.approvedCashlineApplications = approvedCashlineApplications;
    }
    
}
