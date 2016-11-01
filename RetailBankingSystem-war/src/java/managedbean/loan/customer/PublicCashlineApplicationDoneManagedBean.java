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
@Named(value = "publicCashlineApplicationDoneManagedBean")
@RequestScoped
public class PublicCashlineApplicationDoneManagedBean {
    private String loanType;
    private Integer loanAmount;
    /**
     * Creates a new instance of PublicCashlineApplicationDoneManagedBean
     */
    public PublicCashlineApplicationDoneManagedBean() {
    }
    
    @PostConstruct
    public void init(){
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        loanType = (String) ec.getFlash().get("loanType");
        loanAmount = (Integer) ec.getFlash().get("amountRequired");
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public Integer getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(Integer loanAmount) {
        this.loanAmount = loanAmount;
    }
    
}
