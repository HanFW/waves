/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.customer.entity;

import ejb.infrastructure.entity.Employee;
import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

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
    private String customerEmploymentDetails;
    private String customerFamilyInfo;
    private String customerCreditReport;
    private String customerFinancialRiskRating;
    private String customerFinanacialGoals;
    private String customerFinanacialAssets;
    private String customerOnlineBankingAccountNum;
    
    @OneToOne(cascade={CascadeType.ALL},fetch=FetchType.EAGER)
    private CustomerBasic customerBasic;

    @ManyToOne(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    private Employee employee;
    
    public String getCustomerEmploymentDetails() {
        return customerEmploymentDetails;
    }

    public void setCustomerEmploymentDetails(String customerEmploymentDetails) {
        this.customerEmploymentDetails = customerEmploymentDetails;
    }

    public String getCustomerFamilyInfo() {
        return customerFamilyInfo;
    }

    public void setCustomerFamilyInfo(String customerFamilyInfo) {
        this.customerFamilyInfo = customerFamilyInfo;
    }

    public String getCustomerCreditReport() {
        return customerCreditReport;
    }

    public void setCustomerCreditReport(String customerCreditReport) {
        this.customerCreditReport = customerCreditReport;
    }

    public String getCustomerFinancialRiskRating() {
        return customerFinancialRiskRating;
    }

    public void setCustomerFinancialRiskRating(String customerFinancialRiskRating) {
        this.customerFinancialRiskRating = customerFinancialRiskRating;
    }

    public String getCustomerFinanacialGoals() {
        return customerFinanacialGoals;
    }

    public void setCustomerFinanacialGoals(String customerFinanacialGoals) {
        this.customerFinanacialGoals = customerFinanacialGoals;
    }

    public String getCustomerFinanacialAssets() {
        return customerFinanacialAssets;
    }

    public void setCustomerFinanacialAssets(String customerFinanacialAssets) {
        this.customerFinanacialAssets = customerFinanacialAssets;
    }

    public String getCustomerOnlineBankingAccountNum() {
        return customerOnlineBankingAccountNum;
    }

    public void setCustomerOnlineBankingAccountNum(String customerOnlineBankingAccountNum) {
        this.customerOnlineBankingAccountNum = customerOnlineBankingAccountNum;
    }


    public Long getCustomerAdvancedId() {
        return customerAdvancedId;
    }

    public void setCustomerAdvancedId(Long customerAdvancedId) {
        this.customerAdvancedId = customerAdvancedId;
    }

    public CustomerBasic getCustomerBasic() {
        return customerBasic;
    }

    public void setCustomerBasic(CustomerBasic customerBasic) {
        this.customerBasic = customerBasic;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
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
