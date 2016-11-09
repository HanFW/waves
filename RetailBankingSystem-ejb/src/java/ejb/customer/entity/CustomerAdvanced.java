/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.customer.entity;

import ejb.infrastructure.entity.Employee;
import ejb.wealth.entity.Portfolio;
import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 *
 * @author Nicole
 */
@Entity
public class CustomerAdvanced implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerAdvancedId;
    private String customerOnlineBankingAccountNum;
    
    //credit card
    private int numOfDependent;
    private String education;
    private String residentialStatus;
    private int yearInResidence;
    private String industryType;
    private int lengthOfCurrentJob;
    private String employmentStatus;
    private double monthlyFixedIncome;
    
    //loan 
    private String residentialType;
    private String companyAddress;
    private String companyPostal;
    private String currentPosition;
    private String currentJobTitle;
    private String previousCompanyName;
    private int lengthOfPreviousJob;
    private double otherMonthlyIncome;
    private String otherMonthlyIncomeSource;
    
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

    public int getNumOfDependent() {
        return numOfDependent;
    }

    public void setNumOfDependent(int numOfDependent) {
        this.numOfDependent = numOfDependent;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getResidentialStatus() {
        return residentialStatus;
    }

    public void setResidentialStatus(String residentialStatus) {
        this.residentialStatus = residentialStatus;
    }

    public int getYearInResidence() {
        return yearInResidence;
    }

    public void setYearInResidence(int yearInResidence) {
        this.yearInResidence = yearInResidence;
    }

    public String getIndustryType() {
        return industryType;
    }

    public void setIndustryType(String industryType) {
        this.industryType = industryType;
    }

    public int getLengthOfCurrentJob() {
        return lengthOfCurrentJob;
    }

    public void setLengthOfCurrentJob(int lengthOfCurrentJob) {
        this.lengthOfCurrentJob = lengthOfCurrentJob;
    }

    public String getEmploymentStatus() {
        return employmentStatus;
    }

    public void setEmploymentStatus(String employmentStatus) {
        this.employmentStatus = employmentStatus;
    }

    public double getMonthlyFixedIncome() {
        return monthlyFixedIncome;
    }

    public void setMonthlyFixedIncome(double monthlyFixedIncome) {
        this.monthlyFixedIncome = monthlyFixedIncome;
    }

    public String getResidentialType() {
        return residentialType;
    }

    public void setResidentialType(String residentialType) {
        this.residentialType = residentialType;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getCompanyPostal() {
        return companyPostal;
    }

    public void setCompanyPostal(String companyPostal) {
        this.companyPostal = companyPostal;
    }

    public String getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(String currentPosition) {
        this.currentPosition = currentPosition;
    }

    public String getCurrentJobTitle() {
        return currentJobTitle;
    }

    public void setCurrentJobTitle(String currentJobTitle) {
        this.currentJobTitle = currentJobTitle;
    }

    public String getPreviousCompanyName() {
        return previousCompanyName;
    }

    public void setPreviousCompanyName(String previousCompanyName) {
        this.previousCompanyName = previousCompanyName;
    }

    public int getLengthOfPreviousJob() {
        return lengthOfPreviousJob;
    }

    public void setLengthOfPreviousJob(int lengthOfPreviousJob) {
        this.lengthOfPreviousJob = lengthOfPreviousJob;
    }

    public double getOtherMonthlyIncome() {
        return otherMonthlyIncome;
    }

    public void setOtherMonthlyIncome(double otherMonthlyIncome) {
        this.otherMonthlyIncome = otherMonthlyIncome;
    }

    public String getOtherMonthlyIncomeSource() {
        return otherMonthlyIncomeSource;
    }

    public void setOtherMonthlyIncomeSource(String otherMonthlyIncomeSource) {
        this.otherMonthlyIncomeSource = otherMonthlyIncomeSource;
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
