/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author Nicole
 */
@Entity
public class CustomerAdvanced implements Serializable {
    //private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerAdvancedId;
    private String employmentDetails;
    private String familyInfo;
    private String creditReport;
    private String financialRiskRating;
    private String finanacialGoals;
    private String finanacialAssets;
    private String onlineBankingAccountNum;


    public Long getCustomerAdvancedId() {
        return customerAdvancedId;
    }

    public void setCustomerAdvancedId(Long customerAdvancedId) {
        this.customerAdvancedId = customerAdvancedId;
    }

    public String getEmploymentDetails() {
        return employmentDetails;
    }

    public void setEmploymentDetails(String employmentDetails) {
        this.employmentDetails = employmentDetails;
    }

    public String getFamilyInfo() {
        return familyInfo;
    }

    public void setFamilyInfo(String familyInfo) {
        this.familyInfo = familyInfo;
    }

    public String getCreditReport() {
        return creditReport;
    }

    public void setCreditReport(String creditReport) {
        this.creditReport = creditReport;
    }

    public String getFinancialRiskRating() {
        return financialRiskRating;
    }

    public void setFinancialRiskRating(String financialRiskRating) {
        this.financialRiskRating = financialRiskRating;
    }

    public String getFinanacialGoals() {
        return finanacialGoals;
    }

    public void setFinanacialGoals(String finanacialGoals) {
        this.finanacialGoals = finanacialGoals;
    }

    public String getFinanacialAssets() {
        return finanacialAssets;
    }

    public void setFinanacialAssets(String finanacialAssets) {
        this.finanacialAssets = finanacialAssets;
    }

    public String getOnlineBankingAccountNum() {
        return onlineBankingAccountNum;
    }

    public void setOnlineBankingAccountNum(String onlineBankingAccountNum) {
        this.onlineBankingAccountNum = onlineBankingAccountNum;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (customerAdvancedId != null ? customerAdvancedId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CustomerAdvanced)) {
            return false;
        }
        CustomerAdvanced other = (CustomerAdvanced) object;
        if ((this.customerAdvancedId == null && other.customerAdvancedId != null) || (this.customerAdvancedId != null && !this.customerAdvancedId.equals(other.customerAdvancedId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.CustomerAdvanced[ id=" + customerAdvancedId + " ]";
    }
    
}
