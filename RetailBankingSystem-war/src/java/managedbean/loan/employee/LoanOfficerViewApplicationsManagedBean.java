/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.loan.employee;

import ejb.loan.entity.LoanApplication;
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
@Named(value = "loanOfficerViewLoanApplicationsManagedBean")
@RequestScoped
public class LoanOfficerViewApplicationsManagedBean {

    @EJB
    private LoanApplicationSessionBeanLocal loanApplicationSessionBeanLocal;

    private List<LoanApplication> loanApplications;

    /**
     * Creates a new instance of LoanOfficerViewLoanApplicationsManagedBean
     */
    public LoanOfficerViewApplicationsManagedBean() {
    }

    @PostConstruct
    public void init() {
        loanApplications = loanApplicationSessionBeanLocal.getAllLoanApplications();
    }

    public void viewApplication(Long loanApplicationId, String loanType) throws IOException {
        System.out.println("====== loan/LoanOfficerViewLoanApplicationsManagedBean: viewApplication() ======");
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.getFlash().put("applicationId", loanApplicationId);
        if (loanType.equals("HDB - New Purchase")) {
            ec.redirect(ec.getRequestContextPath() + "/web/internalSystem/loan/loanOfficerProcessMortgagePurchaseApplicaion.xhtml?faces-redirect=true");
        }else if(loanType.equals("HDB - Refinancing")){
            ec.redirect(ec.getRequestContextPath() + "/web/internalSystem/loan/loanOfficerProcessMortgageRefinancingApplicaion.xhtml?faces-redirect=true");
        }
    }

    public LoanApplicationSessionBeanLocal getLoanApplicationSessionBeanLocal() {
        return loanApplicationSessionBeanLocal;
    }

    public void setLoanApplicationSessionBeanLocal(LoanApplicationSessionBeanLocal loanApplicationSessionBeanLocal) {
        this.loanApplicationSessionBeanLocal = loanApplicationSessionBeanLocal;
    }

    public List<LoanApplication> getLoanApplications() {
        return loanApplications;
    }

    public void setLoanApplications(List<LoanApplication> loanApplications) {
        this.loanApplications = loanApplications;
    }

}
