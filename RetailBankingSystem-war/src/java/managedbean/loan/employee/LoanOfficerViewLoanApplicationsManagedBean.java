/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.loan.employee;

import ejb.loan.entity.LoanApplication;
import ejb.loan.session.LoanApplicationSessionBeanLocal;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author hanfengwei
 */
@Named(value = "loanOfficerViewLoanApplicationsManagedBean")
@RequestScoped
public class LoanOfficerViewLoanApplicationsManagedBean {
    @EJB
    private LoanApplicationSessionBeanLocal loanApplicationSessionBeanLocal;
    
    private List<LoanApplication> loanApplications;

    /**
     * Creates a new instance of LoanOfficerViewLoanApplicationsManagedBean
     */
    public LoanOfficerViewLoanApplicationsManagedBean() {
    }
    
    @PostConstruct
    public void init(){
        loanApplications = loanApplicationSessionBeanLocal.getAllLoanApplications();
    }
    
    public void viewApplication(Long customerId){
        System.out.println("====== loan/LoanOfficerViewLoanApplicationsManagedBean: viewApplication() ======");
        System.out.println(customerId);
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
