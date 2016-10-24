/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.loan.entity;

import ejb.customer.entity.CustomerBasic;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;

/**
 *
 * @author hanfengwei
 */
@Entity
public class LoanApplication implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long loanApplicationId;

    private double amountRequired;
    private int periodRequired;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date applicationDate;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date finalActionDate;
    private String applicationStatus;
    private double amountGranted;
    private int periodSuggested;
    private double instalment;
    
    @ManyToOne(fetch = FetchType.EAGER)
    private LoanInterestPackage loanInterestPackage;
    
    @ManyToOne(fetch = FetchType.EAGER)
    private CustomerBasic customerBasic;

    public Long getLoanApplicationId() {
        return loanApplicationId;
    }

    public void setLoanApplicationId(Long loanApplicationId) {
        this.loanApplicationId = loanApplicationId;
    }

    public double getAmountRequired() {
        return amountRequired;
    }

    public void setAmountRequired(double amountRequired) {
        this.amountRequired = amountRequired;
    }

    public int getPeriodRequired() {
        return periodRequired;
    }

    public void setPeriodRequired(int periodRequired) {
        this.periodRequired = periodRequired;
    }

    public Date getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(Date applicationDate) {
        this.applicationDate = applicationDate;
    }

    public Date getFinalActionDate() {
        return finalActionDate;
    }

    public void setFinalActionDate(Date finalActionDate) {
        this.finalActionDate = finalActionDate;
    }

    public String getApplicationStatus() {
        return applicationStatus;
    }

    public void setApplicationStatus(String applicationStatus) {
        this.applicationStatus = applicationStatus;
    }

    public double getAmountGranted() {
        return amountGranted;
    }

    public void setAmountGranted(double amountGranted) {
        this.amountGranted = amountGranted;
    }

    public int getPeriodSuggested() {
        return periodSuggested;
    }

    public void setPeriodSuggested(int periodSuggested) {
        this.periodSuggested = periodSuggested;
    }

    public double getInstalment() {
        return instalment;
    }

    public void setInstalment(double instalment) {
        this.instalment = instalment;
    }

    public LoanInterestPackage getLoanInterestPackage() {
        return loanInterestPackage;
    }

    public void setLoanInterestPackage(LoanInterestPackage loanInterestPackage) {
        this.loanInterestPackage = loanInterestPackage;
    }

    public CustomerBasic getCustomerBasic() {
        return customerBasic;
    }

    public void setCustomerBasic(CustomerBasic customerBasic) {
        this.customerBasic = customerBasic;
    }
    
}
