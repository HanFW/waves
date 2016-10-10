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

    private String customerEmploymentDetails;
    private String customerFamilyInfo;
    private String customerCreditReport;
    private String customerFinancialRiskRating;
    private String customerFinanacialGoals;
    private String customerFinanacialAssets;

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

    public String getCustomerEmploymentDetails() {
        if (customerEmploymentDetails == null) {
            customerAdvanced = customerSessionBeanLocal.retrieveCustomerAdvancedByAdId(customerAdvancedId);
        }
        customerEmploymentDetails = customerAdvanced.getCustomerEmploymentDetails();
        return customerEmploymentDetails;
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

    public void setCustomerEmploymentDetails(String customerEmploymentDetails) {
        this.customerEmploymentDetails = customerEmploymentDetails;
    }

    public String getCustomerFamilyInfo() {
        if (customerFamilyInfo == null) {
            customerAdvanced = customerSessionBeanLocal.retrieveCustomerAdvancedByAdId(customerAdvancedId);
        }
        customerFamilyInfo = customerAdvanced.getCustomerFamilyInfo();
        return customerFamilyInfo;
    }

    public void setCustomerFamilyInfo(String customerFamilyInfo) {
        this.customerFamilyInfo = customerFamilyInfo;
    }

    public String getCustomerCreditReport() {
        if (customerCreditReport == null) {
            customerAdvanced = customerSessionBeanLocal.retrieveCustomerAdvancedByAdId(customerAdvancedId);
        }
        customerCreditReport = customerAdvanced.getCustomerCreditReport();
        return customerCreditReport;
    }

    public void setCustomerCreditReport(String customerCreditReport) {
        this.customerCreditReport = customerCreditReport;
    }

    public String getCustomerFinancialRiskRating() {
        if (customerFinancialRiskRating == null) {
            customerAdvanced = customerSessionBeanLocal.retrieveCustomerAdvancedByAdId(customerAdvancedId);
        }
        customerFinancialRiskRating = customerAdvanced.getCustomerFinancialRiskRating();
        return customerFinancialRiskRating;
    }

    public void setCustomerFinancialRiskRating(String customerFinancialRiskRating) {
        this.customerFinancialRiskRating = customerFinancialRiskRating;
    }

    public String getCustomerFinanacialGoals() {
        if (customerFinanacialGoals == null) {
            customerAdvanced = customerSessionBeanLocal.retrieveCustomerAdvancedByAdId(customerAdvancedId);
        }
        customerFinanacialGoals = customerAdvanced.getCustomerFinanacialGoals();
        return customerFinanacialGoals;
    }

    public void setCustomerFinanacialGoals(String customerFinanacialGoals) {
        this.customerFinanacialGoals = customerFinanacialGoals;
    }

    public String getCustomerFinanacialAssets() {
        if (customerFinanacialAssets == null) {
            customerAdvanced = customerSessionBeanLocal.retrieveCustomerAdvancedByAdId(customerAdvancedId);
        }
        customerFinanacialAssets = customerAdvanced.getCustomerFinanacialAssets();
        return customerFinanacialAssets;
    }

    public void setCustomerFinanacialAssets(String customerFinanacialAssets) {
        this.customerFinanacialAssets = customerFinanacialAssets;
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
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(customerSessionBeanLocal.updateCustomerAdvancedProfile(customerAdvancedId, customerEmploymentDetails, customerFamilyInfo, customerCreditReport, customerFinancialRiskRating, customerFinanacialAssets, customerFinanacialGoals), " "));
        customerCreditReport = null;
        customerEmploymentDetails = null;
        customerFamilyInfo = null;
        customerFinanacialGoals = null;
        customerFinanacialAssets = null;
        customerFinancialRiskRating = null;
    }

    public void redirectToViewAdvanced() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext ec = context.getExternalContext();
        ec.redirect(ec.getRequestContextPath() + "/web/internalSystem/CRM/RMUpdateCustomerAdvanced.xhtml?faces-redirect=true");
    }
}
