/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.loan.employee;

import ejb.loan.entity.LoanApplication;
import ejb.loan.session.LoanApplicationSessionBeanLocal;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 *
 * @author hanfengwei
 */
@Named(value = "loanOfficerViewLoanApplicationsManagedBean")
@RequestScoped
public class LoanOfficerViewApplicationsManagedBean {

    @EJB
    private LoanApplicationSessionBeanLocal loanApplicationSessionBeanLocal;

    private List<LoanApplication> mortgagePurchaseApplications;
    private List<LoanApplication> mortgageRefinancingApplications;
    private List<LoanApplication> renovationLoanApplications;
    private List<LoanApplication> carLoanApplications;
    private List<LoanApplication> studyLoanApplications;

    /**
     * Creates a new instance of LoanOfficerViewLoanApplicationsManagedBean
     */
    public LoanOfficerViewApplicationsManagedBean() {
    }

    @PostConstruct
    public void init() {
        ArrayList appStatus = new ArrayList<String>();
        appStatus.add("in progress");
        mortgagePurchaseApplications = loanApplicationSessionBeanLocal.getLoanApplications(appStatus, "HDB - New Purchase");
        mortgagePurchaseApplications.addAll(loanApplicationSessionBeanLocal.getLoanApplications(appStatus, "Private Property - New Purchase"));
        mortgageRefinancingApplications = loanApplicationSessionBeanLocal.getLoanApplications(appStatus, "HDB - Refinancing");
        mortgageRefinancingApplications.addAll(loanApplicationSessionBeanLocal.getLoanApplications(appStatus, "Private Property - Refinancing"));
        appStatus = new ArrayList<String>();
        appStatus.add("pending");
        mortgagePurchaseApplications.addAll(loanApplicationSessionBeanLocal.getLoanApplications(appStatus, "HDB - New Purchase"));
        mortgagePurchaseApplications.addAll(loanApplicationSessionBeanLocal.getLoanApplications(appStatus, "Private Property - New Purchase"));
        mortgageRefinancingApplications.addAll(loanApplicationSessionBeanLocal.getLoanApplications(appStatus, "HDB - Refinancing"));
        mortgageRefinancingApplications.addAll(loanApplicationSessionBeanLocal.getLoanApplications(appStatus, "Private Property - Refinancing"));
    }

    public void viewApplication(Long loanApplicationId, String loanType) throws IOException {
        System.out.println("====== loan/LoanOfficerViewLoanApplicationsManagedBean: viewApplication() ======");
        loanApplicationSessionBeanLocal.updateLoanStatus("in progress", loanApplicationId);
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.getFlash().put("applicationId", loanApplicationId);
        if (loanType.equals("HDB - New Purchase") || loanType.equals("Private Property - New Purchase")) {
            ec.redirect(ec.getRequestContextPath() + "/web/internalSystem/loan/loanOfficerProcessMortgagePurchaseApplication.xhtml?faces-redirect=true");
        }else if(loanType.equals("HDB - Refinancing") || loanType.equals("Private Property - Refinancing")){
            ec.redirect(ec.getRequestContextPath() + "/web/internalSystem/loan/loanOfficerProcessMortgageRefinancingApplication.xhtml?faces-redirect=true");
        }
    }

    public LoanApplicationSessionBeanLocal getLoanApplicationSessionBeanLocal() {
        return loanApplicationSessionBeanLocal;
    }

    public void setLoanApplicationSessionBeanLocal(LoanApplicationSessionBeanLocal loanApplicationSessionBeanLocal) {
        this.loanApplicationSessionBeanLocal = loanApplicationSessionBeanLocal;
    }

    public List<LoanApplication> getMortgagePurchaseApplications() {
        return mortgagePurchaseApplications;
    }

    public void setMortgagePurchaseApplications(List<LoanApplication> mortgagePurchaseApplications) {
        this.mortgagePurchaseApplications = mortgagePurchaseApplications;
    }

    public List<LoanApplication> getMortgageRefinancingApplications() {
        return mortgageRefinancingApplications;
    }

    public void setMortgageRefinancingApplications(List<LoanApplication> mortgageRefinancingApplications) {
        this.mortgageRefinancingApplications = mortgageRefinancingApplications;
    }

    public List<LoanApplication> getRenovationLoanApplications() {
        return renovationLoanApplications;
    }

    public void setRenovationLoanApplications(List<LoanApplication> renovationLoanApplications) {
        this.renovationLoanApplications = renovationLoanApplications;
    }

    public List<LoanApplication> getCarLoanApplications() {
        return carLoanApplications;
    }

    public void setCarLoanApplications(List<LoanApplication> carLoanApplications) {
        this.carLoanApplications = carLoanApplications;
    }

    public List<LoanApplication> getStudyLoanApplications() {
        return studyLoanApplications;
    }

    public void setStudyLoanApplications(List<LoanApplication> studyLoanApplications) {
        this.studyLoanApplications = studyLoanApplications;
    }


}
