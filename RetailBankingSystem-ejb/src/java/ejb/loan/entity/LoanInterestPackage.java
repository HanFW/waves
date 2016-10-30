/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.loan.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;


/**
 *
 * @author hanfengwei
 */
@Entity
public class LoanInterestPackage implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long packageId;
    
    private String packageName;
    private double interestRate;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date updatedDate; 

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER, mappedBy = "loanInterestPackage")
    private List<LoanApplication> loanApplication;
    
    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER, mappedBy = "loanInterestPackage")
    private List<CashlineApplication> cashlineApplication;
    
    public void addLoanApplication(LoanApplication application){
        loanApplication.add(application);
    }
    
    public void addCashlineApplication(CashlineApplication application){
        cashlineApplication.add(application);
    }

    public Long getPackageId() {
        return packageId;
    }

    public void setPackageId(Long packageId) {
        this.packageId = packageId;
    }

    public List<LoanApplication> getLoanApplication() {
        return loanApplication;
    }

    public void setLoanApplication(List<LoanApplication> loanApplication) {
        this.loanApplication = loanApplication;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public List<CashlineApplication> getCashlineApplication() {
        return cashlineApplication;
    }

    public void setCashlineApplication(List<CashlineApplication> cashlineApplication) {
        this.cashlineApplication = cashlineApplication;
    }
    
}
