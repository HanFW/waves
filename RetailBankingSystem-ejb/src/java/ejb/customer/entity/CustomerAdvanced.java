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
    private String customerOnlineBankingAccountNum;
    
    //credit card
    private String numOfDependent;
    private String education;
    private String residencialStatus;
    private String yearInResidence;
    private String jobType;
    private String jobIndustry;
    private String jobDuration;
    private String jobStatus;
    private String incomeMonthly;
    
    //loan 
    
    
    //wealthManagement
    
    
    @OneToOne(cascade={CascadeType.ALL},fetch=FetchType.EAGER)
    private CustomerBasic customerBasic;

    @ManyToOne(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    private Employee employee;
    

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

    public String getNumOfDependent() {
        return numOfDependent;
    }

    public void setNumOfDependent(String numOfDependent) {
        this.numOfDependent = numOfDependent;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getResidencialStatus() {
        return residencialStatus;
    }

    public void setResidencialStatus(String residencialStatus) {
        this.residencialStatus = residencialStatus;
    }

    public String getYearInResidence() {
        return yearInResidence;
    }

    public void setYearInResidence(String yearInResidence) {
        this.yearInResidence = yearInResidence;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getJobIndustry() {
        return jobIndustry;
    }

    public void setJobIndustry(String jobIndustry) {
        this.jobIndustry = jobIndustry;
    }

    public String getJobDuration() {
        return jobDuration;
    }

    public void setJobDuration(String jobDuration) {
        this.jobDuration = jobDuration;
    }

    public String getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(String jobStatus) {
        this.jobStatus = jobStatus;
    }

    public String getIncomeMonthly() {
        return incomeMonthly;
    }

    public void setIncomeMonthly(String incomeMonthly) {
        this.incomeMonthly = incomeMonthly;
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
