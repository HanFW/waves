/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.loan.entity;

import ejb.customer.entity.CustomerBasic;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;

/**
 *
 * @author hanfengwei
 */
@Entity
public class CashlineApplication implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private int amountRequired;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date applicationDate;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date finalActionDate;
    private String applicationStatus;
    private int amountGranted;
    private HashMap uploads;
    
    @ManyToOne(fetch = FetchType.EAGER)
    private CustomerBasic customerBasic;
    
    @ManyToOne(fetch = FetchType.EAGER)
    private LoanInterestPackage loanInterestPackage;
    
    @OneToOne(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    private CashlineAccount cashlineAccount;
    
    public void create(int amount, String employmentStatus){
        this.setAmountRequired(amount);
        this.setApplicationDate(new Date());
        this.setApplicationStatus("pending");
        HashMap docs = new HashMap();
        docs.put("identification", true);
        if (employmentStatus.equals("Self-Employed")) {
            docs.put("selfEmployedTax", true);
            docs.put("employeeTax", false);
        } else {
            docs.put("selfEmployedTax", false);
            docs.put("employeeTax", true);
        } 
        this.setUploads(docs);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getAmountRequired() {
        return amountRequired;
    }

    public void setAmountRequired(int amountRequired) {
        this.amountRequired = amountRequired;
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

    public int getAmountGranted() {
        return amountGranted;
    }

    public void setAmountGranted(int amountGranted) {
        this.amountGranted = amountGranted;
    }

    public HashMap getUploads() {
        return uploads;
    }

    public void setUploads(HashMap uploads) {
        this.uploads = uploads;
    }

    public CustomerBasic getCustomerBasic() {
        return customerBasic;
    }

    public void setCustomerBasic(CustomerBasic customerBasic) {
        this.customerBasic = customerBasic;
    }

    public LoanInterestPackage getLoanInterestPackage() {
        return loanInterestPackage;
    }

    public void setLoanInterestPackage(LoanInterestPackage loanInterestPackage) {
        this.loanInterestPackage = loanInterestPackage;
    }

    public CashlineAccount getCashlineAccount() {
        return cashlineAccount;
    }

    public void setCashlineAccount(CashlineAccount cashlineAccount) {
        this.cashlineAccount = cashlineAccount;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CashlineApplication)) {
            return false;
        }
        CashlineApplication other = (CashlineApplication) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ejb.loan.entity.CashlineApplication[ id=" + id + " ]";
    }
    
}
