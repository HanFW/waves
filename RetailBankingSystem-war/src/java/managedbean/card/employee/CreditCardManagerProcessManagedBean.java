/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.card.employee;

import ejb.card.entity.CreditCard;
import ejb.card.session.CreditCardSessionBeanLocal;
import ejb.customer.entity.CustomerAdvanced;
import ejb.customer.entity.CustomerBasic;
import ejb.customer.session.CRMCustomerSessionBeanLocal;
import ejb.infrastructure.session.CustomerAdminSessionBeanLocal;
import ejb.infrastructure.session.MessageSessionBeanLocal;
import ejb.loan.entity.CreditReportAccountStatus;
import ejb.loan.entity.CreditReportBureauScore;
import ejb.loan.entity.CreditReportDefaultRecords;
import java.io.IOException;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author aaa
 */
@Named(value = "creditCardManagerProcessManagedBean")
@ViewScoped
public class CreditCardManagerProcessManagedBean implements Serializable {

    @EJB
    private CreditCardSessionBeanLocal creditCardSessionLocal;

    @EJB
    private CustomerAdminSessionBeanLocal customerAdminSessionBeanLocal;

    @EJB
    private CRMCustomerSessionBeanLocal cRMCustomerSessionBeanLocal;
    
    @EJB
    private MessageSessionBeanLocal messageSessionBeanLocal;

    private CustomerBasic customer;
    private CustomerAdvanced ca;
    private CreditCard cc;

    //basic information
    private String customerSalutation;
    private String customerName;
    private String customerDateOfBirth;
    private String customerGender;
    private String customerNationality;
    private String customerIdentificationNum;
    private String customerCountryOfResidence;
    private String customerRace;
    private String customerMobile;
    private String customerEmail;

    //personal details
    private String customerEducation;
    private String customerMaritalStatus;
    private Integer customerNumOfDependents;
    private String customerAddress;
    private String customerPostal;
    private String customerResidentialStatus;
    private String customerResidentialType;
    private Integer customerLengthOfResidence;

    //employment details
    private String customerEmploymentStatus;
    private String customerOccupation;
    private String customerCompanyName;
    private String customerCompanyAddress;
    private String customerCompanyPostal;
    private String customerIndustryType;
    private String customerCurrentPosition;
    private String customerCurrentJobTitle;
    private Integer customerLengthOfCurrentJob;
    private Double customerMonthlyFixedIncome;

    //credit card details
    private Double creditLimit;
    private String hasCreditLimit;
    private String creditCardTypeName;
    private Long creditCardTypeId;
    private String cardHolderName;

    private HashMap docs;

    //credit report
    CreditReportBureauScore cr;
    private List<CreditReportAccountStatus> accountStatus;
    private List<CreditReportDefaultRecords> defaultRecords;
    private Double bureauScore;
    private String riskGrade;
    private Double probabilityOfDefault;

    private Double appraisedValue;
    private double[] maxInterval;
    private double maxMin;
    private double maxMax;
    private double[] suggestedInterval;
    private double suggestedMin;
    private double suggestedMax;
    private double riskRatio;

    public CreditCardManagerProcessManagedBean() {
    }

    @PostConstruct
    public void init() {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        Long creditCardId = (Long) ec.getFlash().get("creditCardId");
        cc = creditCardSessionLocal.getCardByCardId(creditCardId);
        customer = cc.getCustomerBasic();
        ca = customer.getCustomerAdvanced();
        System.out.println("@@@@@@@@@@@@IC num " + customer.getCustomerIdentificationNum());

//        docs = cc.getUploads();
//        cr = customer.getBureauScore();
//        accountStatus = cr.getAccountStatus();
//        defaultRecords = cr.getDefaultRecords();
//        bureauScore = cr.getBureauScore();
//        riskGrade = cr.getRiskGrade();
//        probabilityOfDefault = cr.getProbabilityOfDefault();
        maxInterval = creditCardSessionLocal.getCreditLimitMaxInterval();
        maxMin = maxInterval[0];
        maxMax = maxInterval[1];
        riskRatio = creditCardSessionLocal.getCreditLimitRiskRatio();
        suggestedInterval = creditCardSessionLocal.getCreditLimitSuggestedInterval();
        suggestedMin = suggestedInterval[0];
        suggestedMax = suggestedInterval[1];
    }

    public void approveRequest() throws IOException {
        creditCardSessionLocal.approveRequest(cc.getCardId(), creditLimit);

        if (!cRMCustomerSessionBeanLocal.hasOnlineBankingAcc(cc.getCustomerBasic().getCustomerBasicId())) {
            customerAdminSessionBeanLocal.createOnlineBankingAccount(cc.getCustomerBasic().getCustomerBasicId());
        }

        Calendar cal = Calendar.getInstance();
        Date receivedDate = cal.getTime();
        
        String subject = "Your "+cc.getCreditCardType().getCreditCardTypeName()+" has been approved.";
        String messageContent = "Your "+cc.getCreditCardType().getCreditCardTypeName()+ " has been approved by one of our card managers. \n"
                + "Please activate your credit card in 15 days. \n"
                + "https://localhost:8181/RetailBankingSystem-war/web/onlineBanking/card/creditCard/customerActivateCreditCard.xhtml \n"
                + "Thank you. \n";

        messageSessionBeanLocal.sendMessage("Merlion Bank", "Credit Card", subject, receivedDate.toString(),
                messageContent, customer.getCustomerBasicId());

        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(ec.getRequestContextPath() + "/web/internalSystem/card/creditCard/creditCardManagerViewApplication.xhtml?faces-redirect=true");
    }

    public void rejectRequest() throws IOException {
        creditCardSessionLocal.rejectRequest(cc.getCardId());
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(ec.getRequestContextPath() + "/web/internalSystem/card/creditCard/creditCardManagerViewApplication.xhtml?faces-redirect=true");
    }

    public CustomerBasic getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerBasic customer) {
        this.customer = customer;
    }

    public CustomerAdvanced getCa() {
        return ca;
    }

    public void setCa(CustomerAdvanced ca) {
        this.ca = ca;
    }

    public String getCustomerSalutation() {
        return customer.getCustomerSalutation();
    }

    public void setCustomerSalutation(String customerSalutation) {
        this.customerSalutation = customerSalutation;
    }

    public String getCustomerName() {
        return customer.getCustomerName();
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerDateOfBirth() {
        return customer.getCustomerDateOfBirth();
    }

    public void setCustomerDateOfBirth(String customerDateOfBirth) {
        this.customerDateOfBirth = customerDateOfBirth;
    }

    public String getCustomerGender() {
        return customer.getCustomerGender();
    }

    public void setCustomerGender(String customerGender) {
        this.customerGender = customerGender;
    }

    public String getCustomerNationality() {
        return customer.getCustomerNationality();
    }

    public void setCustomerNationality(String customerNationality) {
        this.customerNationality = customerNationality;
    }

    public String getCustomerIdentificationNum() {
        return customer.getCustomerIdentificationNum();
    }

    public void setCustomerIdentificationNum(String customerIdentificationNum) {
        this.customerIdentificationNum = customerIdentificationNum;
    }

    public String getCustomerCountryOfResidence() {
        return customer.getCustomerCountryOfResidence();
    }

    public void setCustomerCountryOfResidence(String customerCountryOfResidence) {
        this.customerCountryOfResidence = customerCountryOfResidence;
    }

    public String getCustomerRace() {
        return customer.getCustomerRace();
    }

    public void setCustomerRace(String customerRace) {
        this.customerRace = customerRace;
    }

    public String getCustomerMobile() {
        return customer.getCustomerMobile();
    }

    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
    }

    public String getCustomerEmail() {
        return customer.getCustomerEmail();
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerEducation() {
        return ca.getEducation();
    }

    public void setCustomerEducation(String customerEducation) {
        this.customerEducation = customerEducation;
    }

    public String getCustomerMaritalStatus() {
        return customer.getCustomerMaritalStatus();
    }

    public void setCustomerMaritalStatus(String customerMaritalStatus) {
        this.customerMaritalStatus = customerMaritalStatus;
    }

    public Integer getCustomerNumOfDependents() {
        return ca.getNumOfDependent();
    }

    public void setCustomerNumOfDependents(Integer customerNumOfDependents) {
        this.customerNumOfDependents = customerNumOfDependents;
    }

    public String getCustomerAddress() {
        return customer.getCustomerAddress();
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getCustomerPostal() {
        return customer.getCustomerPostal();
    }

    public void setCustomerPostal(String customerPostal) {
        this.customerPostal = customerPostal;
    }

    public String getCustomerResidentialStatus() {
        return ca.getResidentialStatus();
    }

    public void setCustomerResidentialStatus(String customerResidentialStatus) {
        this.customerResidentialStatus = customerResidentialStatus;
    }

    public String getCustomerResidentialType() {
        return ca.getResidentialType();
    }

    public void setCustomerResidentialType(String customerResidentialType) {
        this.customerResidentialType = customerResidentialType;
    }

    public Integer getCustomerLengthOfResidence() {
        return ca.getYearInResidence();
    }

    public void setCustomerLengthOfResidence(Integer customerLengthOfResidence) {
        this.customerLengthOfResidence = customerLengthOfResidence;
    }

    public String getCustomerEmploymentStatus() {
        return ca.getEmploymentStatus();
    }

    public void setCustomerEmploymentStatus(String customerEmploymentStatus) {
        this.customerEmploymentStatus = customerEmploymentStatus;
    }

    public String getCustomerOccupation() {
        return customer.getCustomerOccupation();
    }

    public void setCustomerOccupation(String customerOccupation) {
        this.customerOccupation = customerOccupation;
    }

    public String getCustomerCompanyName() {
        return customer.getCustomerCompany();
    }

    public void setCustomerCompanyName(String customerCompanyName) {
        this.customerCompanyName = customerCompanyName;
    }

    public String getCustomerCompanyAddress() {
        return ca.getCompanyAddress();
    }

    public void setCustomerCompanyAddress(String customerCompanyAddress) {
        this.customerCompanyAddress = customerCompanyAddress;
    }

    public String getCustomerCompanyPostal() {
        return ca.getCompanyPostal();
    }

    public void setCustomerCompanyPostal(String customerCompanyPostal) {
        this.customerCompanyPostal = customerCompanyPostal;
    }

    public String getCustomerIndustryType() {
        return ca.getIndustryType();
    }

    public void setCustomerIndustryType(String customerIndustryType) {
        this.customerIndustryType = customerIndustryType;
    }

    public String getCustomerCurrentPosition() {
        return ca.getCurrentPosition();
    }

    public void setCustomerCurrentPosition(String customerCurrentPosition) {
        this.customerCurrentPosition = customerCurrentPosition;
    }

    public String getCustomerCurrentJobTitle() {
        return ca.getCurrentJobTitle();
    }

    public void setCustomerCurrentJobTitle(String customerCurrentJobTitle) {
        this.customerCurrentJobTitle = customerCurrentJobTitle;
    }

    public Integer getCustomerLengthOfCurrentJob() {
        return ca.getLengthOfCurrentJob();
    }

    public void setCustomerLengthOfCurrentJob(Integer customerLengthOfCurrentJob) {
        this.customerLengthOfCurrentJob = customerLengthOfCurrentJob;
    }

    public Double getCustomerMonthlyFixedIncome() {
        return ca.getMonthlyFixedIncome();
    }

    public void setCustomerMonthlyFixedIncome(Double customerMonthlyFixedIncome) {
        this.customerMonthlyFixedIncome = customerMonthlyFixedIncome;
    }

    public Double getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(Double creditLimit) {
        this.creditLimit = creditLimit;
    }

    public String getHasCreditLimit() {
        return hasCreditLimit;
    }

    public void setHasCreditLimit(String hasCreditLimit) {
        this.hasCreditLimit = hasCreditLimit;
    }

    public String getCreditCardTypeName() {
        return creditCardTypeName;
    }

    public void setCreditCardTypeName(String creditCardTypeName) {
        this.creditCardTypeName = creditCardTypeName;
    }

    public Long getCreditCardTypeId() {
        return creditCardTypeId;
    }

    public void setCreditCardTypeId(Long creditCardTypeId) {
        this.creditCardTypeId = creditCardTypeId;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public HashMap getDocs() {
        return docs;
    }

    public void setDocs(HashMap docs) {
        this.docs = docs;
    }

    public CreditReportBureauScore getCr() {
        return cr;
    }

    public void setCr(CreditReportBureauScore cr) {
        this.cr = cr;
    }

    public List<CreditReportAccountStatus> getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(List<CreditReportAccountStatus> accountStatus) {
        this.accountStatus = accountStatus;
    }

    public List<CreditReportDefaultRecords> getDefaultRecords() {
        return defaultRecords;
    }

    public void setDefaultRecords(List<CreditReportDefaultRecords> defaultRecords) {
        this.defaultRecords = defaultRecords;
    }

    public Double getBureauScore() {
        return bureauScore;
    }

    public void setBureauScore(Double bureauScore) {
        this.bureauScore = bureauScore;
    }

    public String getRiskGrade() {
        return riskGrade;
    }

    public void setRiskGrade(String riskGrade) {
        this.riskGrade = riskGrade;
    }

    public Double getProbabilityOfDefault() {
        return probabilityOfDefault;
    }

    public void setProbabilityOfDefault(Double probabilityOfDefault) {
        this.probabilityOfDefault = probabilityOfDefault;
    }

    public Double getAppraisedValue() {
        return appraisedValue;
    }

    public void setAppraisedValue(Double appraisedValue) {
        this.appraisedValue = appraisedValue;
    }

    public double[] getMaxInterval() {
        return maxInterval;
    }

    public void setMaxInterval(double[] maxInterval) {
        this.maxInterval = maxInterval;
    }

    public double getMaxMin() {
        return maxMin;
    }

    public void setMaxMin(double maxMin) {
        this.maxMin = maxMin;
    }

    public double getMaxMax() {
        return maxMax;
    }

    public void setMaxMax(double maxMax) {
        this.maxMax = maxMax;
    }

    public double[] getSuggestedInterval() {
        return suggestedInterval;
    }

    public void setSuggestedInterval(double[] suggestedInterval) {
        this.suggestedInterval = suggestedInterval;
    }

    public double getSuggestedMin() {
        return suggestedMin;
    }

    public void setSuggestedMin(double suggestedMin) {
        this.suggestedMin = suggestedMin;
    }

    public double getSuggestedMax() {
        return suggestedMax;
    }

    public void setSuggestedMax(double suggestedMax) {
        this.suggestedMax = suggestedMax;
    }

    public double getRiskRatio() {
        return riskRatio;
    }

    public void setRiskRatio(double riskRatio) {
        this.riskRatio = riskRatio;
    }

    public CreditCard getCc() {
        return cc;
    }

    public void setCc(CreditCard cc) {
        this.cc = cc;
    }

}
