/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.loan.customer;

import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 *
 * @author hanfengwei
 */
@Named(value = "publicChooseMortgageLoanManagedBean")
@RequestScoped
public class PublicChooseMortgageLoanManagedBean {

    /**
     * Creates a new instance of PublicChooseMortgageLoanManagedBean
     */
    public PublicChooseMortgageLoanManagedBean() {
    }
    
    public void chooseMortgageLoan(String loanType) throws IOException{
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.getFlash().put("loanType", loanType);
        ec.redirect(ec.getRequestContextPath() + "/web/merlionBank/loan/publicMortgageLoanApplication.xhtml?faces-redirect=true");
    }
}
