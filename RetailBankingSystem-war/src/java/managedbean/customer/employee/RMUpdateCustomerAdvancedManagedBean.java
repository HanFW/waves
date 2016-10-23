/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.customer.employee;

import ejb.customer.entity.CustomerAdvanced;
import ejb.customer.entity.CustomerBasic;
import ejb.customer.session.CRMCustomerSessionBeanLocal;
import ejb.infrastructure.entity.Employee;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 *
 * @author aaa
 */
@Named(value = "rMUpdateCustomerAdvancedManagedBean")
@SessionScoped
public class RMUpdateCustomerAdvancedManagedBean implements Serializable {

    @EJB
    private CRMCustomerSessionBeanLocal customerSessionBeanLocal;

    private Employee em = new Employee();
    private List<CustomerBasic> customerBasics;
    private List<CustomerAdvanced> customerAdvanceds;
    private CustomerAdvanced customerAdvanced;

    private String customerOnlineBankingAccountNum;
    private Long customerAdvancedId;
    private Long customerBasicId;

    private String numOfDependent;
    private String education;
    private String residentialStatus;
    private String yearInResidence;
    private String jobType;
    private String jobIndustry;
    private String jobDuration;
    private String jobStatus;
    private String incomeMonthly;

    public RMUpdateCustomerAdvancedManagedBean() {
    }

    public Employee getEm() {
        return em;
    }

    public void setEm(Employee em) {
        this.em = em;
    }

    public List<CustomerBasic> getCustomerBasics() {
        return customerBasics;
    }

    public void setCustomerBasics(List<CustomerBasic> customerBasics) {
        this.customerBasics = customerBasics;
    }

    public List<CustomerAdvanced> getCustomerAdvanceds() {
        return customerAdvanceds;
    }

    public void setCustomerAdvanceds(List<CustomerAdvanced> customerAdvanced) {
        this.customerAdvanceds = customerAdvanced;
    }

    public Long getCustomerAdvancedId() {
        return customerAdvancedId;
    }

    public void setCustomerAdvancedId(Long customerAdvancedId) {
        this.customerAdvancedId = customerAdvancedId;
    }

    public Long getCustomerBasicId() {
        return customerBasicId;
    }

    public void setCustomerBasicId(Long customerBasicId) {
        this.customerBasicId = customerBasicId;
    }

    public CustomerAdvanced getCustomerAdvanced() {
        return customerAdvanced;
    }

    public void setCustomerAdvanced(CustomerAdvanced customerAdvanced) {
        this.customerAdvanced = customerAdvanced;
    }

    public String getCustomerOnlineBankingAccountNum() {
        return customerOnlineBankingAccountNum;
    }

    public void setCustomerOnlineBankingAccountNum(String customerOnlineBankingAccountNum) {
        this.customerOnlineBankingAccountNum = customerOnlineBankingAccountNum;
    }

    public String getNumOfDependent() {
        if (numOfDependent == null) {
            customerAdvanced = customerSessionBeanLocal.retrieveCustomerAdvancedByAdId(customerAdvancedId);
        }
        numOfDependent = customerAdvanced.getNumOfDependent();

        return numOfDependent;
    }

    public void setNumOfDependent(String numOfDependent) {
        this.numOfDependent = numOfDependent;
    }

    public String getEducation() {
        if (education == null) {
            customerAdvanced = customerSessionBeanLocal.retrieveCustomerAdvancedByAdId(customerAdvancedId);
        }
        education = customerAdvanced.getEducation();
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getResidentialStatus() {
        if (residentialStatus == null) {
            customerAdvanced = customerSessionBeanLocal.retrieveCustomerAdvancedByAdId(customerAdvancedId);
        }
        residentialStatus = customerAdvanced.getResidentialStatus();
        return residentialStatus;
    }

    public void setResidentialStatus(String residentialStatus) {
        this.residentialStatus = residentialStatus;
    }

    public String getYearInResidence() {
        if (yearInResidence == null) {
            customerAdvanced = customerSessionBeanLocal.retrieveCustomerAdvancedByAdId(customerAdvancedId);
        }
        yearInResidence = customerAdvanced.getYearInResidence();
        return yearInResidence;
    }

    public void setYearInResidence(String yearInResidence) {
        this.yearInResidence = yearInResidence;
    }

    public String getJobType() {
        if (jobType == null) {
            customerAdvanced = customerSessionBeanLocal.retrieveCustomerAdvancedByAdId(customerAdvancedId);
        }
        jobType = customerAdvanced.getJobType();
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getJobIndustry() {
        if (jobIndustry == null) {
            customerAdvanced = customerSessionBeanLocal.retrieveCustomerAdvancedByAdId(customerAdvancedId);
        }
        jobIndustry = customerAdvanced.getJobIndustry();
        return jobIndustry;
    }

    public void setJobIndustry(String jobIndustry) {
        this.jobIndustry = jobIndustry;
    }

    public String getJobDuration() {
        if (jobDuration == null) {
            customerAdvanced = customerSessionBeanLocal.retrieveCustomerAdvancedByAdId(customerAdvancedId);
        }
        jobDuration = customerAdvanced.getJobDuration();
        return jobDuration;
    }

    public void setJobDuration(String jobDuration) {
        this.jobDuration = jobDuration;
    }

    public String getJobStatus() {
        if (jobStatus == null) {
            customerAdvanced = customerSessionBeanLocal.retrieveCustomerAdvancedByAdId(customerAdvancedId);
        }
        jobStatus = customerAdvanced.getJobStatus();
        return jobStatus;
    }

    public void setJobStatus(String jobStatus) {
        this.jobStatus = jobStatus;
    }

    public String getIncomeMonthly() {
        if (incomeMonthly == null) {
            customerAdvanced = customerSessionBeanLocal.retrieveCustomerAdvancedByAdId(customerAdvancedId);
        }
        incomeMonthly = customerAdvanced.getIncomeMonthly();
        return incomeMonthly;
    }

    public void setIncomeMonthly(String incomeMonthly) {
        this.incomeMonthly = incomeMonthly;
    }

    public List<CustomerAdvanced> getCustomerListFromEmployeeId() {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        em = (Employee) ec.getSessionMap().get("employee");
        customerAdvanceds = em.getCustomerAdvanced();
        return customerAdvanceds;
    }

    public CustomerAdvanced getCustomerAdvancedInfo() {

        customerAdvanced = customerSessionBeanLocal.retrieveCustomerAdvancedByAdId(customerAdvancedId);

        return customerAdvanced;

    }

    public void updateCustomerAdvanced() {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(customerSessionBeanLocal.updateCustomerAdvancedProfile(customerAdvancedId,
                education, incomeMonthly, jobDuration, jobStatus, jobIndustry, jobType,
                numOfDependent, residentialStatus, yearInResidence), " "));

        numOfDependent = null;
        education = null;
        residentialStatus = null;
        yearInResidence = null;
        jobType = null;
        jobIndustry = null;
        jobDuration = null;
        jobStatus = null;
        incomeMonthly = null;
    }

    public void redirectToViewAdvanced() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext ec = context.getExternalContext();
        ec.redirect(ec.getRequestContextPath() + "/web/internalSystem/CRM/RMUpdateCustomerAdvanced.xhtml?faces-redirect=true");
    }
}
