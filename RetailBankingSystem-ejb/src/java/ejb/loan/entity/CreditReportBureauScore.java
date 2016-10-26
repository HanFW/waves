/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.loan.entity;

import ejb.customer.entity.CustomerBasic;
import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 *
 * @author hanfengwei
 */
@Entity
public class CreditReportBureauScore implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private double bureauScore;
    private String riskGrade;
    private double probabilityOfDefault;
    
    @OneToOne(mappedBy = "bureauScore")
    CustomerBasic customer;
    
    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    List<CreditReportAccountStatus> accountStatus;
    
    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    List<CreditReportDefaultRecords> defaultRecords;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getBureauScore() {
        return bureauScore;
    }

    public void setBureauScore(double bureauScore) {
        this.bureauScore = bureauScore;
    }

    public String getRiskGrade() {
        return riskGrade;
    }

    public void setRiskGrade(String riskGrade) {
        this.riskGrade = riskGrade;
    }

    public double getProbabilityOfDefault() {
        return probabilityOfDefault;
    }

    public void setProbabilityOfDefault(double probabilityOfDefault) {
        this.probabilityOfDefault = probabilityOfDefault;
    }

    public CustomerBasic getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerBasic customer) {
        this.customer = customer;
    }

    public List<CreditReportAccountStatus> getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(List<CreditReportAccountStatus> accountStatus) {
        this.accountStatus = accountStatus;
    }

    public List<CreditReportDefaultRecords> getDefaultRecords() {
        return defaultRecords;
    }

    public void setDefaultRecords(List<CreditReportDefaultRecords> defaultRecords) {
        this.defaultRecords = defaultRecords;
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
        if (!(object instanceof CreditReportBureauScore)) {
            return false;
        }
        CreditReportBureauScore other = (CreditReportBureauScore) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ejb.loan.entity.CreditReportBureauScore[ id=" + id + " ]";
    }
    
}
