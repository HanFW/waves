/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.customer.employee;

import ejb.customer.entity.CustomerBasic;
import ejb.customer.session.CRMCustomerSessionBeanLocal;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author aaa
 */
@Named(value = "employeeUpdateBasicProfileManagedBean")
@SessionScoped
public class EmployeeUpdateBasicProfileManagedBean implements Serializable {

    
    @EJB
    private CRMCustomerSessionBeanLocal cRMCustomerSessionBeanLocal;

    private String customerName;
    private String customerSalutation;
    private String customerIdentificationNum;
    private String customerGender;
    private String customerEmail;
    private String customerMobile;
    private String customerDateOfBirth;
    private String customerNationality;
    private String customerCountryOfResidence;
    private String customerRace;
    private String customerMaritalStatus;
    private String customerOccupation;
    private String customerCompany;
    private String customerAddress;
    private String customerPostal;
    private String customerOnlineBankingAccountNum;
    private String customerOnlineBankingPassword;
    private byte[] customerSignature;
    private Long newCustomerBasicId;
    private String customerAge;
    private String streetName;
    private String blockNum;
    private String unitNum;
    List<String> addressList;
    private String replacedCustomerEmail;
    private String replacedCustomerMobile;

    private CustomerBasic cb = new CustomerBasic();
    
    @PostConstruct
    public void init()
    {
        cb = (CustomerBasic)FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get("customerBasic");
        System.err.println("*************** EmployeeUpdateBasicProfileManagedBean: " + cb);
    
    }

    public EmployeeUpdateBasicProfileManagedBean() {
    }

    public CRMCustomerSessionBeanLocal getcRMCustomerSessionBeanLocal() {
        return cRMCustomerSessionBeanLocal;
    }

    public void setcRMCustomerSessionBeanLocal(CRMCustomerSessionBeanLocal cRMCustomerSessionBeanLocal) {
        this.cRMCustomerSessionBeanLocal = cRMCustomerSessionBeanLocal;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerSalutation() {
        return customerSalutation;
    }

    public void setCustomerSalutation(String customerSalutation) {
        this.customerSalutation = customerSalutation;
    }

    public String getCustomerIdentificationNum() {
        return customerIdentificationNum;
    }

    public void setCustomerIdentificationNum(String customerIdentificationNum) {
        this.customerIdentificationNum = customerIdentificationNum;
    }

    public String getCustomerGender() {
        return customerGender;
    }

    public void setCustomerGender(String customerGender) {
        this.customerGender = customerGender;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerMobile() {
        return customerMobile;
    }

    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
    }

    public String getCustomerDateOfBirth() {
        return customerDateOfBirth;
    }

    public void setCustomerDateOfBirth(String customerDateOfBirth) {
        this.customerDateOfBirth = customerDateOfBirth;
    }

    public String getCustomerNationality() {
        return customerNationality;
    }

    public void setCustomerNationality(String customerNationality) {
        this.customerNationality = customerNationality;
    }

    public String getCustomerCountryOfResidence() {
        return customerCountryOfResidence;
    }

    public void setCustomerCountryOfResidence(String customerCountryOfResidence) {
        this.customerCountryOfResidence = customerCountryOfResidence;
    }

    public String getCustomerRace() {
        return customerRace;
    }

    public void setCustomerRace(String customerRace) {
        this.customerRace = customerRace;
    }

    public String getCustomerMaritalStatus() {
        return customerMaritalStatus;
    }

    public void setCustomerMaritalStatus(String customerMaritalStatus) {
        this.customerMaritalStatus = customerMaritalStatus;
    }

    public String getCustomerOccupation() {
        return customerOccupation;
    }

    public void setCustomerOccupation(String customerOccupation) {
        this.customerOccupation = customerOccupation;
    }

    public String getCustomerCompany() {
        return customerCompany;
    }

    public void setCustomerCompany(String customerCompany) {
        this.customerCompany = customerCompany;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getCustomerPostal() {
        return customerPostal;
    }

    public void setCustomerPostal(String customerPostal) {
        this.customerPostal = customerPostal;
    }

    public String getCustomerOnlineBankingAccountNum() {
        return customerOnlineBankingAccountNum;
    }

    public void setCustomerOnlineBankingAccountNum(String customerOnlineBankingAccountNum) {
        this.customerOnlineBankingAccountNum = customerOnlineBankingAccountNum;
                
    }

    public String getCustomerOnlineBankingPassword() {
        return customerOnlineBankingPassword;
    }

    public void setCustomerOnlineBankingPassword(String customerOnlineBankingPassword) {
        this.customerOnlineBankingPassword = customerOnlineBankingPassword;
    }

    public byte[] getCustomerSignature() {
        return customerSignature;
    }

    public void setCustomerSignature(byte[] customerSignature) {
        this.customerSignature = customerSignature;
    }

    public Long getNewCustomerBasicId() {
        return newCustomerBasicId;
    }

    public void setNewCustomerBasicId(Long newCustomerBasicId) {
        this.newCustomerBasicId = newCustomerBasicId;
    }

    public String getCustomerAge() {
        return customerAge;
    }

    public void setCustomerAge(String customerAge) {
        this.customerAge = customerAge;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getBlockNum() {
        return blockNum;
    }

    public void setBlockNum(String blockNum) {
        this.blockNum = blockNum;
    }

    public String getUnitNum() {
        return unitNum;
    }

    public void setUnitNum(String unitNum) {
        this.unitNum = unitNum;
    }

    public List<String> getAddressList() {
        return addressList;
    }

    public void setAddressList(List<String> addressList) {
        this.addressList = addressList;
    }

    public String getReplacedCustomerEmail() {
        return replacedCustomerEmail;
    }

    public void setReplacedCustomerEmail(String replacedCustomerEmail) {
        this.replacedCustomerEmail = replacedCustomerEmail;
    }

    public String getReplacedCustomerMobile() {
        return replacedCustomerMobile;
    }

    public void setReplacedCustomerMobile(String replacedCustomerMobile) {
        this.replacedCustomerMobile = replacedCustomerMobile;
    }

    public CustomerBasic getCb() {
        return cb;
    }

    public void setCb(CustomerBasic cb) {
        this.cb = cb;
    }

    public CustomerBasic getCustomerBasicInfo() {

        if (customerName == null) {
            customerName = cb.getCustomerName();
            customerOnlineBankingAccountNum = cb.getCustomerOnlineBankingAccountNum();
            customerGender = cb.getCustomerGender();
            customerDateOfBirth = cb.getCustomerDateOfBirth();
            customerRace = cb.getCustomerRace();
            customerNationality = cb.getCustomerNationality();
            customerCountryOfResidence = cb.getCustomerCountryOfResidence();
            customerMaritalStatus = cb.getCustomerMaritalStatus();
            customerOccupation = cb.getCustomerOccupation();
            customerCompany = cb.getCustomerCompany();
            customerMobile = cb.getCustomerMobile();
            replacedCustomerMobile = customerMobileNumReplaceWithStar(customerMobile);
            customerEmail = cb.getCustomerEmail();
            replacedCustomerEmail = customerEmailReplaceWithStar(customerEmail);
            customerAddress = cb.getCustomerAddress();
            customerPostal = cb.getCustomerPostal();
            addressList = Arrays.asList(customerAddress.split(","));
            streetName = addressList.get(0).trim();
            blockNum = addressList.get(1).trim();
            unitNum = addressList.get(2).trim();
        }
        return cb;
    }

    public void updateCustomerBasicProfile() {

        String updatedCustomerMobile = customerMobile;
        String updatedCustomerEmail = customerEmail;

        if (!replacedCustomerMobile.contains("*")) {
            updatedCustomerMobile = replacedCustomerMobile;
        }

        if (!replacedCustomerEmail.contains("*")) {
            updatedCustomerEmail = replacedCustomerEmail;
        }

        customerAddress = streetName + ", " + blockNum + ", " + unitNum + ", " + customerPostal;
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(cRMCustomerSessionBeanLocal.updateCustomerBasicProfile(customerOnlineBankingAccountNum, customerNationality, customerCountryOfResidence, customerMaritalStatus, customerOccupation, customerCompany, updatedCustomerEmail, updatedCustomerMobile, customerAddress, customerPostal), " "));
    }

    public String customerMobileNumReplaceWithStar(String inputCustomerMobileNum) {
        String customerMobileNumAfterReplaced = "";
        customerMobileNumAfterReplaced = "****" + inputCustomerMobileNum.substring(4);
        return customerMobileNumAfterReplaced;
    }

    public String customerEmailReplaceWithStar(String inputCustomerEmail) {
        String customerEmailAfterReplaced = "";
        customerEmailAfterReplaced = inputCustomerEmail.substring(0, 1) + "**" + inputCustomerEmail.substring(3);
        return customerEmailAfterReplaced;
    }
}
