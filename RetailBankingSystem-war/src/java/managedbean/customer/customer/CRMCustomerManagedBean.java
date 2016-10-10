/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.customer.customer;

import ejb.customer.entity.CustomerBasic;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import ejb.customer.session.CRMCustomerSessionBeanLocal;
import javax.faces.event.ActionEvent;
import ejb.infrastructure.session.CustomerAdminSessionBean;
import ejb.infrastructure.session.SMSSessionBeanLocal;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import org.jboss.aerogear.security.otp.Totp;
import org.jboss.aerogear.security.otp.api.Clock;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Nicole
 */
@Named(value = "cRMCustomerManagedBean")
@RequestScoped

public class CRMCustomerManagedBean {

    @EJB
    private SMSSessionBeanLocal sMSSessionBeanLocal;

    @EJB
    private CRMCustomerSessionBeanLocal customerSessionBean;
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
    private String customerOTP;
    private String updatedCustomerMobile;

//advanced profile
    private String customerEmploymentDetails;
    private String customerFamilyInfo;
    private String customerCreditReport;
    private String customerFinancialRiskRating;
    private String customerFinanacialGoals;
    private String customerFinanacialAssets;

    private String customerOnlineBankingNewPassword;
    private String customerOnlineBankingNewPasswordConfirm;
    private String replacedCustomerEmail;
    private String replacedCustomerMobile;

    private CustomerBasic cb = new CustomerBasic();

    private ExternalContext ec;
    private String hashedPassword;
    private String hashedNewPassword;

    public String getCustomerAge() {
        return customerAge;
    }

    public void setCustomerAge(String customerAge) {
        this.customerAge = customerAge;
    }

    public byte[] getCustomerSignature() {
        return customerSignature;
    }

    public void setCustomerSignature(byte[] customerSignature) {
        this.customerSignature = customerSignature;
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

    public Long getNewCustomerBasicId() {
        return newCustomerBasicId;
    }

    public void setNewCustomerBasicId(Long newCustomerBasicId) {
        this.newCustomerBasicId = newCustomerBasicId;
    }

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

    public String getCustomerOnlineBankingNewPassword() {
        return customerOnlineBankingNewPassword;
    }

    public void setCustomerOnlineBankingNewPassword(String customerOnlineBankingNewPassword) {
        this.customerOnlineBankingNewPassword = customerOnlineBankingNewPassword;
    }

    public String getCustomerOnlineBankingNewPasswordConfirm() {
        return customerOnlineBankingNewPasswordConfirm;
    }

    public void setCustomerOnlineBankingNewPasswordConfirm(String customerOnlineBankingNewPasswordConfirm) {
        this.customerOnlineBankingNewPasswordConfirm = customerOnlineBankingNewPasswordConfirm;
    }

    public CustomerBasic getCb() {
        return cb;
    }

    public void setCb(CustomerBasic cb) {
        this.cb = cb;
    }

    public String getReplacedCustomerMobile() {
        return replacedCustomerMobile;
    }

    public void setReplacedCustomerMobile(String replacedCustomerMobile) {
        this.replacedCustomerMobile = replacedCustomerMobile;
    }

    public String getReplacedCustomerEmail() {
        return replacedCustomerEmail;
    }

    public void setReplacedCustomerEmail(String replacedCustomerEmail) {
        this.replacedCustomerEmail = replacedCustomerEmail;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public CRMCustomerManagedBean() {
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

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public String getHashedNewPassword() {
        return hashedNewPassword;
    }

    public void setHashedNewPassword(String hashedNewPassword) {
        this.hashedNewPassword = hashedNewPassword;
    }

    public String getCustomerOTP() {
        return customerOTP;
    }

    public void setCustomerOTP(String customerOTP) {
        this.customerOTP = customerOTP;
    }

    public Long saveNewCustomerBasic(ActionEvent customerBasic) {

        try {
            newCustomerBasicId = customerSessionBean.addNewCustomerBasic(customerName, customerSalutation, customerIdentificationNum, customerGender, customerEmail, customerMobile, customerDateOfBirth, customerNationality, customerCountryOfResidence, customerRace, customerMaritalStatus, customerOccupation, customerCompany, customerAddress, customerPostal, customerOnlineBankingAccountNum, customerOnlineBankingPassword, customerSignature,"No");
            return newCustomerBasicId;
        } catch (Exception ex) {
            ex.printStackTrace();
            return -1L;
        }
    }

    public CustomerBasic getCustomerBasicInfo() {

        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        cb = (CustomerBasic) ec.getSessionMap().get("customer");
//        cb = customerSessionBean.getAllCustomerBasicProfile().get(0);

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

    //    public String customerAccountNumReplaceWithStar(String inputCustomerAccountNumber) {
    //
    //        String customerAccountNumAfterReplaced = "";
    //
    //        if (inputCustomerAccountNumber != null) {
    //            customerAccountNumAfterReplaced = inputCustomerAccountNumber.substring(0, 2) + "****" + inputCustomerAccountNumber.substring(6);
    //        }
    //        return customerAccountNumAfterReplaced;
    //    }
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

    public void updateCustomerBasicProfile() {
        System.out.println("=");
        System.out.println("====== onlineBanking/CRMCustomerMangedBean: updateCustomerBasicProfile() ======");
        ec = FacesContext.getCurrentInstance().getExternalContext();
        CustomerBasic customer = (CustomerBasic) ec.getSessionMap().get("customer");

        updatedCustomerMobile = customerMobile;
        String updatedCustomerEmail = customerEmail;

        if (!replacedCustomerMobile.contains("*")) {
            updatedCustomerMobile = replacedCustomerMobile;
            customer.setCustomerMobile(updatedCustomerMobile);
        }
        if (!replacedCustomerEmail.contains("*")) {
            updatedCustomerEmail = replacedCustomerEmail;
            customer.setCustomerEmail(customerEmail);
        }
        customerAddress = streetName + ", " + blockNum + ", " + unitNum + ", " + customerPostal;
        
        customer.setCustomerNationality(customerNationality);
        customer.setCustomerCountryOfResidence(customerCountryOfResidence);
        customer.setCustomerMaritalStatus(customerMaritalStatus);
        customer.setCustomerOccupation(customerOccupation);
        customer.setCustomerCompany(customerCompany);
        customer.setCustomerAddress(customerAddress);
        customer.setCustomerPostal(customerPostal);

        FacesContext.getCurrentInstance().addMessage("updateMessage", new FacesMessage(customerSessionBean.updateCustomerBasicProfile(customerOnlineBankingAccountNum, customerNationality, customerCountryOfResidence, customerMaritalStatus, customerOccupation, customerCompany, updatedCustomerEmail, replacedCustomerMobile, customerAddress, customerPostal), " "));
        if (!replacedCustomerMobile.contains("*")) {
            System.out.println("====== onlineBanking/CRMCustomerMangedBean: updateCustomerBasicProfile(): mobile changed");
            RequestContext rc = RequestContext.getCurrentInstance();
            rc.execute("PF('otpDialog').show();");
            sMSSessionBeanLocal.sendOTP("customer", customer);
        }
    }

    public void validateMobile() {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext ec = context.getExternalContext();

        if (customerOTP == null || customerOTP.trim().length() == 0) {
            context.addMessage("otpMessage", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid input: ", "Please enter your OTP"));
        } else {
            Clock clock = new Clock(120);
            CustomerBasic customer = (CustomerBasic) ec.getSessionMap().get("customer");
            Totp totp = new Totp(customer.getCustomerOTPSecret(), clock);
            if (totp.verify(customerOTP)) {
                customerSessionBean.updateCustomerMobile(customer.getCustomerBasicId(), customer.getCustomerMobile());
                RequestContext rc = RequestContext.getCurrentInstance();
                context.addMessage("updateMessage", new FacesMessage(FacesMessage.SEVERITY_INFO, "Mobile number updated", null));
                rc.execute("PF('confirmDialog').show();");
            } else {
                context.addMessage("otpMessage", new FacesMessage(FacesMessage.SEVERITY_ERROR, "OTP does not match: ", "That is an invalid online banking OTP. Please re-enter."));
            }
        }
    }

    public void updateOnlineBankingAccountPIN() {

        ec = FacesContext.getCurrentInstance().getExternalContext();
        cb = (CustomerBasic) ec.getSessionMap().get("customer");
        System.out.println(cb.getCustomerOnlineBankingAccountNum());

        try {
            hashedPassword = md5Hashing(customerOnlineBankingPassword + cb.getCustomerIdentificationNum().substring(0, 3));
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(CustomerAdminSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            hashedNewPassword = md5Hashing(customerOnlineBankingNewPassword + cb.getCustomerIdentificationNum().substring(0, 3));
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(CustomerAdminSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(customerSessionBean.updateCustomerOnlineBankingAccountPIN(cb.getCustomerOnlineBankingAccountNum(), hashedPassword, hashedNewPassword), " "));
    }

    private String md5Hashing(String stringToHash) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        return Arrays.toString(md.digest(stringToHash.getBytes()));
    }
}
