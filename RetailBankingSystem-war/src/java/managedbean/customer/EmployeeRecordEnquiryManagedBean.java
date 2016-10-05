/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.customer;

import ejb.customer.entity.CustomerBasic;
import ejb.customer.entity.EnquiryCase;
import ejb.customer.session.CRMCustomerSessionBeanLocal;
import ejb.customer.session.EnquirySessionBeanLocal;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 *
 * @author aaa
 */
@Named(value = "employeeRecordEnquiryManagedBean")
@SessionScoped
public class EmployeeRecordEnquiryManagedBean implements Serializable {

    @EJB
    private CRMCustomerSessionBeanLocal cRMCustomerSessionBeanLocal;

    @EJB
    private EnquirySessionBeanLocal enquirySessionBeanLocal;

    private Long caseId;
    private String caseIdStr;
    private String caseType;
    private String caseDetail;
    private String identificationNum;
    private String followUpDetail;

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

    private boolean visible = false;

    private CustomerBasic cb = new CustomerBasic();
    private EnquiryCase ec = new EnquiryCase();

    public EmployeeRecordEnquiryManagedBean() {
    }

    public String getCaseType() {
        return caseType;
    }

    public void setCaseType(String caseType) {
        this.caseType = caseType;
    }

    public String getCaseDetail() {
        return caseDetail;
    }

    public void setCaseDetail(String caseDetail) {
        this.caseDetail = caseDetail;
    }

    public String getIdentificationNum() {
        return identificationNum;
    }

    public void setIdentificationNum(String identificationNum) {
        this.identificationNum = identificationNum;
    }

    public CustomerBasic getCb() {
        return cb;
    }

    public void setCb(CustomerBasic cb) {
        this.cb = cb;
    }

    public EnquiryCase getEc() {
        return ec;
    }

    public void setEc(EnquiryCase ec) {
        this.ec = ec;
    }

    public Long getCaseId() {
        return caseId;
    }

    public void setCaseId(Long caseId) {
        this.caseId = caseId;
    }

    public String getCaseIdStr() {
        return caseIdStr;
    }

    public void setCaseIdStr(String caseIdStr) {
        this.caseIdStr = caseIdStr;
    }

    public String getFollowUpDetail() {
        return followUpDetail;
    }

    public void setFollowUpDetail(String followUpDetail) {
        this.followUpDetail = followUpDetail;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
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
    

    public List<EnquiryCase> getEnquiryCase() {
        List<EnquiryCase> enquiryCases = enquirySessionBeanLocal.getCustomerEnquiry(cb.getCustomerBasicId());
        return enquiryCases;
    }

    public void saveEnquiryCase() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(enquirySessionBeanLocal.addNewCase(cb.getCustomerBasicId(), caseType, caseDetail), " "));
        ExternalContext ec = context.getExternalContext();
        ec.redirect(ec.getRequestContextPath() + "/web/internalSystem/enquiry/counterTellerAddEnquiryDone.xhtml");
        caseType = null;
        caseDetail = null;
        identificationNum = null;
        cb = null;
    }

//    public void saveFollowUp() throws IOException {
//        FacesContext context = FacesContext.getCurrentInstance();
//        context.addMessage(null, new FacesMessage(enquirySessionBeanLocal.addFollowUp(caseId, followUpDetail), " "));
//        ExternalContext ec = context.getExternalContext();
//        ec.redirect(ec.getRequestContextPath() + "/web/internalSystem/enquiry/counterTellerAddEnquiryDone.xhtml");
//        caseId = null;
//        caseIdStr = "";
//        followUpDetail = null;
//        identificationNum = null;
//        cb = null;
//        ec = null;
//    }

//    public void retieveCustomerByIdentification() {
//        cb = cRMCustomerSessionBeanLocal.retrieveCustomerBasicByIC(identificationNum);
//        visible = true;
//    }

    public void retrieveCaseByCaseRef() throws IOException {
        caseId = Long.valueOf(caseIdStr);
        ec = enquirySessionBeanLocal.getEnquiryByCaseId(caseId).get(0);
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext ec = context.getExternalContext();
        ec.redirect(ec.getRequestContextPath() + "/web/internalSystem/enquiry/counterTellerSearchCaseDone.xhtml?faces-redirect=true");
    }

//    public void helpCustomerRecordEnquiry() throws IOException {
//        FacesContext context = FacesContext.getCurrentInstance();
//        ExternalContext ec = context.getExternalContext();
//        ec.redirect(ec.getRequestContextPath() + "/web/internalSystem/enquiry/counterTellerAddNewCase.xhtml");
//        visible = false;
//    }
//
//    public void helpCustomerChangeBasicInfo() throws IOException {
//        FacesContext context = FacesContext.getCurrentInstance();
//        ExternalContext ec = context.getExternalContext();
//        ec.redirect(ec.getRequestContextPath() + "/web/internalSystem/CRM/counterTellerUpdateCustomerBasic.xhtml");
//        visible = false;
//    }

//    public CustomerBasic getCustomerBasicInfo() {
//
//        cb = cRMCustomerSessionBeanLocal.retrieveCustomerBasicByIC(identificationNum);
//
//        if (customerName == null) {
//            customerName = cb.getCustomerName();
//            customerOnlineBankingAccountNum = cb.getCustomerOnlineBankingAccountNum();
//            customerGender = cb.getCustomerGender();
//            customerDateOfBirth = cb.getCustomerDateOfBirth();
//            customerRace = cb.getCustomerRace();
//            customerNationality = cb.getCustomerNationality();
//            customerCountryOfResidence = cb.getCustomerCountryOfResidence();
//            customerMaritalStatus = cb.getCustomerMaritalStatus();
//            customerOccupation = cb.getCustomerOccupation();
//            customerCompany = cb.getCustomerCompany();
//            customerMobile = cb.getCustomerMobile();
//            replacedCustomerMobile = customerMobileNumReplaceWithStar(customerMobile);
//            customerEmail = cb.getCustomerEmail();
//            replacedCustomerEmail = customerEmailReplaceWithStar(customerEmail);
//            customerAddress = cb.getCustomerAddress();
//            customerPostal = cb.getCustomerPostal();
//            addressList = Arrays.asList(customerAddress.split(","));
//            streetName = addressList.get(0).trim();
//            blockNum = addressList.get(1).trim();
//            unitNum = addressList.get(2).trim();
//        }
//        return cb;
//    }
//
//    public void updateCustomerBasicProfile() {
//
//        String updatedCustomerMobile = customerMobile;
//        String updatedCustomerEmail = customerEmail;
//
//        if (!replacedCustomerMobile.contains("*")) {
//            updatedCustomerMobile = replacedCustomerMobile;
//        }
//
//        if (!replacedCustomerEmail.contains("*")) {
//            updatedCustomerEmail = replacedCustomerEmail;
//        }
//
//        customerAddress = streetName + ", " + blockNum + ", " + unitNum + ", " + customerPostal;
//        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(cRMCustomerSessionBeanLocal.updateCustomerBasicProfile(customerOnlineBankingAccountNum, customerNationality, customerCountryOfResidence, customerMaritalStatus, customerOccupation, customerCompany, updatedCustomerEmail, updatedCustomerMobile, customerAddress, customerPostal), " "));
//    }
//
//    public String customerMobileNumReplaceWithStar(String inputCustomerMobileNum) {
//        String customerMobileNumAfterReplaced = "";
//        customerMobileNumAfterReplaced = "****" + inputCustomerMobileNum.substring(4);
//        return customerMobileNumAfterReplaced;
//    }
//
//    public String customerEmailReplaceWithStar(String inputCustomerEmail) {
//        String customerEmailAfterReplaced = "";
//        customerEmailAfterReplaced = inputCustomerEmail.substring(0, 1) + "**" + inputCustomerEmail.substring(3);
//        return customerEmailAfterReplaced;
//    }

}
