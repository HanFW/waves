/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.customer;

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
            customerAdvanced = customerSessionBeanLocal.getCustomerAdvancedByAccNum(customerOnlineBankingAccountNum);
        }
        customerEmploymentDetails = customerAdvanced.getCustomerEmploymentDetails();
        return customerEmploymentDetails;
    }

    public void setCustomerEmploymentDetails(String customerEmploymentDetails) {
        this.customerEmploymentDetails = customerEmploymentDetails;
    }

    public String getCustomerFamilyInfo() {
        if (customerFamilyInfo == null) {
            customerAdvanced = customerSessionBeanLocal.getCustomerAdvancedByAccNum(customerOnlineBankingAccountNum);
        }
        customerFamilyInfo = customerAdvanced.getCustomerFamilyInfo();
        return customerFamilyInfo;
    }

    public void setCustomerFamilyInfo(String customerFamilyInfo) {
        this.customerFamilyInfo = customerFamilyInfo;
    }

    public String getCustomerCreditReport() {
        if (customerCreditReport == null) {
            customerAdvanced = customerSessionBeanLocal.getCustomerAdvancedByAccNum(customerOnlineBankingAccountNum);
        }
        customerCreditReport = customerAdvanced.getCustomerCreditReport();
        return customerCreditReport;
    }

    public void setCustomerCreditReport(String customerCreditReport) {
        this.customerCreditReport = customerCreditReport;
    }

    public String getCustomerFinancialRiskRating() {
        if (customerFinancialRiskRating == null) {
            customerAdvanced = customerSessionBeanLocal.getCustomerAdvancedByAccNum(customerOnlineBankingAccountNum);
        }
        customerFinancialRiskRating = customerAdvanced.getCustomerFinancialRiskRating();
        return customerFinancialRiskRating;
    }

    public void setCustomerFinancialRiskRating(String customerFinancialRiskRating) {
        this.customerFinancialRiskRating = customerFinancialRiskRating;
    }

    public String getCustomerFinanacialGoals() {
        if (customerFinanacialGoals == null) {
            customerAdvanced = customerSessionBeanLocal.getCustomerAdvancedByAccNum(customerOnlineBankingAccountNum);
        }
        customerFinanacialGoals = customerAdvanced.getCustomerFinanacialGoals();
        return customerFinanacialGoals;
    }

    public void setCustomerFinanacialGoals(String customerFinanacialGoals) {
        this.customerFinanacialGoals = customerFinanacialGoals;
    }

    public String getCustomerFinanacialAssets() {
        if (customerFinanacialAssets == null) {
            customerAdvanced = customerSessionBeanLocal.getCustomerAdvancedByAccNum(customerOnlineBankingAccountNum);
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

        customerAdvanced = customerSessionBeanLocal.getCustomerAdvancedByAccNum(customerOnlineBankingAccountNum);

        return customerAdvanced;

    }

    public void updateCustomerAdvanced() {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(customerSessionBeanLocal.updateCustomerAdvancedProfile(customerOnlineBankingAccountNum, customerEmploymentDetails, customerFamilyInfo, customerCreditReport, customerFinancialRiskRating, customerFinanacialAssets, customerFinanacialGoals), " "));
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
        ec.redirect(ec.getRequestContextPath() + "/web/internalSystem/CRM/RMUpdateCustomerAdvanced.xhtml");
    }
}
