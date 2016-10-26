/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.loan.customer;

import ejb.customer.entity.CustomerBasic;
import ejb.loan.entity.LoanApplication;
import ejb.loan.entity.LoanPayableAccount;
import ejb.loan.session.LoanManagementSessionBeanLocal;
import java.io.IOException;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 *
 * @author hanfengwei
 */
@Named(value = "customerViewAllLoansManagedBean")
@RequestScoped
public class CustomerViewAllLoansManagedBean {
    @EJB
    private LoanManagementSessionBeanLocal loanManagementSessionBeanLocal;
    
    private List<LoanPayableAccount> accounts;
    private List<LoanApplication> applications;

    /**
     * Creates a new instance of CustomerViewAllLoansManagedBean
     */
    public CustomerViewAllLoansManagedBean() {
    }
    
    public void viewLoan(Long loanId) throws IOException{
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.getFlash().put("loanId", loanId);
        ec.redirect(ec.getRequestContextPath() + "/web/onlineBanking/loan/customerViewLoan.xhtml?faces-redirect=true");
    }

    public List<LoanPayableAccount> getAccounts() {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        CustomerBasic customer = (CustomerBasic) ec.getSessionMap().get("customer");
        accounts = loanManagementSessionBeanLocal.getLoanPayableAccountByIdentification(customer.getCustomerIdentificationNum());
        return accounts;
    }
    
    public List<LoanApplication> getApplications() {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        CustomerBasic customer = (CustomerBasic) ec.getSessionMap().get("customer");
        applications = loanManagementSessionBeanLocal.getLoanApplicationsByIdentification(customer.getCustomerIdentificationNum());
        return applications;
    }
    
}
