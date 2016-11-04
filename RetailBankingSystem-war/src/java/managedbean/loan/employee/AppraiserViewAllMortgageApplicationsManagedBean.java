/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.loan.employee;

import ejb.loan.entity.MortgageLoanApplication;
import ejb.loan.session.LoanApplicationSessionBeanLocal;
import java.io.IOException;
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
@Named(value = "appraiserViewAllMortgageApplicationsManagedBean")
@RequestScoped
public class AppraiserViewAllMortgageApplicationsManagedBean {

    @EJB
    private LoanApplicationSessionBeanLocal loanApplicationSessionBeanLocal;

    private List<MortgageLoanApplication> applications;

    /**
     * Creates a new instance of AppraiserViewAllMortgageApplicationsManagedBean
     */
    public AppraiserViewAllMortgageApplicationsManagedBean() {
    }

    @PostConstruct
    public void init() {
        applications = loanApplicationSessionBeanLocal.getAllMortgageApplicationsPendingAppraisal();
    }

    public void viewApplication(Long loanApplicationId) throws IOException {
        System.out.println("====== loan/AppraiserViewAllMortgageApplicationsManagedBean: viewApplication() ======");
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.getFlash().put("applicationId", loanApplicationId);
        ec.redirect(ec.getRequestContextPath() + "/web/internalSystem/loan/appraiserSubmitValuation.xhtml?faces-redirect=true");
    }

    public LoanApplicationSessionBeanLocal getLoanApplicationSessionBeanLocal() {
        return loanApplicationSessionBeanLocal;
    }

    public void setLoanApplicationSessionBeanLocal(LoanApplicationSessionBeanLocal loanApplicationSessionBeanLocal) {
        this.loanApplicationSessionBeanLocal = loanApplicationSessionBeanLocal;
    }

    public List<MortgageLoanApplication> getApplications() {
        return applications;
    }

    public void setApplications(List<MortgageLoanApplication> applications) {
        this.applications = applications;
    }

}
