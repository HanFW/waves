/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean;

import entity.CustomerBasic;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import session.stateless.CRMCustomerSessionBean;
import javax.faces.event.ActionEvent;

/**
 *
 * @author Nicole
 */
@Named(value = "cRMCustomerManagedBean")
@RequestScoped
public class CRMCustomerManagedBean {

    @EJB
    private CRMCustomerSessionBean customerSessionBean;
    private String customerName;
    private String salutation;
    private String identificationType;
    private String identificationNum;
    private String gender;
    private String email;
    private String mobile;
    private String dateOfBirth;
    private String nationality;
    private String countryOfResidence;
    private String Race;
    private String maritalStatus;
    private String occupation;
    private String company;
    private String address;
    private String postal;
    private String onlineBankingAccountNum;
    private String onlineBankingPassword;
    private Long newCustomerBasicId;

    private String employmentDetails;
    private String familyInfo;
    private String creditReport;
    private String financialRiskRating;
    private String finanacialGoals;
    private String finanacialAssets;
    
    private String onlineBankingNewPassword;
    private String onlineBankingNewPasswordConfirm;
    
    private String replacedCustomerAccountNum;
    private String replacedCustomerEmail;
    private String replacedCustomerMobile;

    public String getReplacedCustomerMobile() {
        return replacedCustomerMobile;
    }

    public void setReplacedCustomerMobile(String replacedCustomerMobile) {
        this.replacedCustomerMobile = replacedCustomerMobile;
    }
    

    public String getReplacedCustomerAccountNum() {
        return replacedCustomerAccountNum;
    }

    public void setReplacedCustomerAccountNum(String replacedCustomerAccountNum) {
        this.replacedCustomerAccountNum = replacedCustomerAccountNum;
    }

    public String getReplacedCustomerEmail() {
        return replacedCustomerEmail;
    }

    public void setReplacedCustomerEmail(String replacedCustomerEmail) {
        this.replacedCustomerEmail = replacedCustomerEmail;
    }
    
    private CustomerBasic cb = new CustomerBasic();
    
   

    public String getOnlineBankingNewPassword() {
        return onlineBankingNewPassword;
    }

    public void setOnlineBankingNewPassword(String onlineBankingNewPassword) {
        this.onlineBankingNewPassword = onlineBankingNewPassword;
    }

    public String getOnlineBankingNewPasswordConfirm() {
        return onlineBankingNewPasswordConfirm;
    }

    public void setOnlineBankingNewPasswordConfirm(String onlineBankingNewPasswordConfirm) {
        this.onlineBankingNewPasswordConfirm = onlineBankingNewPasswordConfirm;
    }

    public CRMCustomerSessionBean getCustomerSessionBean() {
        return customerSessionBean;
    }

    public void setCustomerSessionBean(CRMCustomerSessionBean customerSessionBean) {
        this.customerSessionBean = customerSessionBean;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getSalutation() {
        return salutation;
    }

    public void setSalutation(String salutation) {
        this.salutation = salutation;
    }

    public String getIdentificationType() {
        return identificationType;
    }

    public void setIdentificationType(String identificationType) {
        this.identificationType = identificationType;
    }

    public String getIdentificationNum() {
        return identificationNum;
    }

    public void setIdentificationNum(String identificationNum) {
        this.identificationNum = identificationNum;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getCountryOfResidence() {
        return countryOfResidence;
    }

    public void setCountryOfResidence(String countryOfResidence) {
        this.countryOfResidence = countryOfResidence;
    }

    public String getRace() {
        return Race;
    }

    public void setRace(String Race) {
        this.Race = Race;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostal() {
        return postal;
    }

    public void setPostal(String postal) {
        this.postal = postal;
    }

    public String getOnlineBankingAccountNum() {
        return onlineBankingAccountNum;
    }

    public void setOnlineBankingAccountNum(String onlineBankingAccountNum) {
        this.onlineBankingAccountNum = onlineBankingAccountNum;
    }

    public String getOnlineBankingPassword() {
        return onlineBankingPassword;
    }

    public void setOnlineBankingPassword(String onlineBankingPassword) {
        this.onlineBankingPassword = onlineBankingPassword;
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

    public CRMCustomerManagedBean() {
    }

    public Long saveNewCustomerBasic(ActionEvent customerBasic) {

        try {
            newCustomerBasicId = customerSessionBean.addNewCustomerBasic(customerName, salutation, identificationType, identificationNum, gender, email, mobile, dateOfBirth, nationality, countryOfResidence,Race, maritalStatus, occupation, company, address, postal, onlineBankingAccountNum, onlineBankingPassword);
            return newCustomerBasicId;
        } catch (Exception ex) {
            ex.printStackTrace();
            return -1L;
        }
    }
    
    public String updateOnlineBankingAccountPIN(ActionEvent customerBasic) {
        System.err.print("called");
        return customerSessionBean.updateCustomerOnlineBankingAccountPIN(onlineBankingAccountNum, onlineBankingPassword, onlineBankingNewPassword);
    }
    
    public CustomerBasic getCustomerBasicInfo() {
  
          System.err.print("Get company:" + company);
          
          cb = customerSessionBean.getAllCustomerBasicProfile().get(0);
          if(customerName == null){
          customerName = cb.getCustomerName();
          onlineBankingAccountNum = cb.getOnlineBankingAccountNum();
          replacedCustomerAccountNum = customerAccountNumReplaceWithStar(onlineBankingAccountNum);
          gender = cb.getGender();
          dateOfBirth = cb.getDateOfBirth();
          Race = cb.getRace();
          nationality = cb.getNationality();
          countryOfResidence = cb.getCountryOfResidence();
          maritalStatus = cb.getMaritalStatus();
          occupation = cb.getOccupation();
          company = cb.getCompany();
          mobile = cb.getMobile();
          replacedCustomerMobile = customerMobileNumReplaceWithStar(mobile);
          email = cb.getEmail();
          replacedCustomerEmail = customerEmailReplaceWithStar(email);
          address = cb.getAddress();
          postal = cb.getPostal();
          }
          
          return cb;
        
    }
    
    public String customerAccountNumReplaceWithStar(String inputCustomerAccountNumber) {

        String customerAccountNumAfterReplaced = "";

        if (inputCustomerAccountNumber != null) {
            customerAccountNumAfterReplaced = inputCustomerAccountNumber.substring(0, 2) + "****" + inputCustomerAccountNumber.substring(6);
        }
        return customerAccountNumAfterReplaced;
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
    
    public String updateCustomerBasicProfile() {
        
        System.err.print("Update company:" + company);
        
        String updatedReplacedCustomerMobile = replacedCustomerMobile;
        String updatedReplacedCustomerEmail = replacedCustomerEmail;
        
        if(!replacedCustomerMobile.equals(customerMobileNumReplaceWithStar(mobile)) || !replacedCustomerEmail.equals(customerEmailReplaceWithStar(email))){
            replacedCustomerMobile = customerMobileNumReplaceWithStar(updatedReplacedCustomerMobile);
            replacedCustomerEmail = customerEmailReplaceWithStar(updatedReplacedCustomerEmail);
            return customerSessionBean.updateCustomerBasicProfile(onlineBankingAccountNum,nationality,countryOfResidence,maritalStatus,occupation, company, updatedReplacedCustomerEmail, updatedReplacedCustomerMobile,address, postal);
        }else{
            return customerSessionBean.updateCustomerBasicProfile(onlineBankingAccountNum,nationality,countryOfResidence,maritalStatus,occupation, company, email, mobile,address, postal);
        }          
    }
}
