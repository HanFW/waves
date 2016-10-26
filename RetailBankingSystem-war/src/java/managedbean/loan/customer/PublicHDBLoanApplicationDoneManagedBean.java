/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.loan.customer;

import java.math.BigDecimal;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 *
 * @author hanfengwei
 */
@Named(value = "publicHDBLoanApplicationDoneManagedBean")
@RequestScoped
public class PublicHDBLoanApplicationDoneManagedBean {
    
    private String loanType;
    private BigDecimal loanAmount;
    private int tenure;

    /**
     * Creates a new instance of PublicHDBLoanApplicationDoneManagedBean
     */
    public PublicHDBLoanApplicationDoneManagedBean() {
    }
    
    @PostConstruct
    public void init(){
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        loanType = (String) ec.getFlash().get("loanType");
        loanAmount = (BigDecimal) ec.getFlash().get("amountRequired");
        tenure = (int) ec.getFlash().get("tenure");
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public BigDecimal getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(BigDecimal loanAmount) {
        this.loanAmount = loanAmount;
    }

    public int getTenure() {
        return tenure;
    }

    public void setTenure(int tenure) {
        this.tenure = tenure;
    }
    
    
}
