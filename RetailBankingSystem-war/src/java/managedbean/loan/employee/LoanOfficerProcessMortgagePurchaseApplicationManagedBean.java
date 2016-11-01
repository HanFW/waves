/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.loan.employee;

import ejb.customer.entity.CustomerAdvanced;
import ejb.customer.entity.CustomerBasic;
import ejb.loan.entity.CreditReportAccountStatus;
import ejb.loan.entity.CreditReportBureauScore;
import ejb.loan.entity.CreditReportDefaultRecords;
import ejb.loan.entity.CustomerDebt;
import ejb.loan.entity.CustomerProperty;
import ejb.loan.entity.MortgageLoanApplication;
import ejb.loan.session.LoanApplicationSessionBeanLocal;
import java.io.IOException;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
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
 * @author hanfengwei
 */
@Named(value = "loanOfficerProcessMortgagePurchaseApplicationManagedBean")
@ViewScoped
public class LoanOfficerProcessMortgagePurchaseApplicationManagedBean implements Serializable{
    @EJB
    private LoanApplicationSessionBeanLocal loanApplicationSessionBeanLocal;
    
    private Long applicationId;
    
    private MortgageLoanApplication ma;
    private CustomerBasic customer;
    private CustomerAdvanced ca;
    private List<CustomerDebt> debts;
    private CustomerProperty property;
    
    private Date applicationDate;
    private double amountGranted;
    private int periodSuggested;
    private double instalmentSuggested;
    
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
    private String customerPreviousCompany;
    private Integer customerLengthOfPreviousJob;
    private Double customerMonthlyFixedIncome;
    private Double customerOtherMonthlyIncome;
    private String customerOtherMonthlyIncomeSource;

    //property details
    private String customerPropertyAddress;
    private String customerPropertyPostal;
    private ArrayList<String> customerPropertyOwners = new ArrayList<String>();
    private String customerPropertyType;
    private Double customerPropertyBuiltUpArea;
    private Double customerPropertyLandArea;
    private String customerPropertyStatus;
    private Date customerPropertyTOPDate;
    private String customerPropertyUsage;
    private String customerPropertyTenureType;
    private Integer customerPropertyTenureDuration;
    private Integer customerPropertyTenureFromYear;

    //loan - new purchase
    private Double customerPropertyPurchasePrice;
    private Date customerPropertyDateOfPurchase;
    private String customerPropertySource;
    private String customerPropertyWithOTP;
    private Date customerPropertyOTPDate;
    private String customerPropertyWithTenancy;
    private Double customerPropertyTenancyIncome;
    private Integer customerPropertyTenancyExpiryYear;
    private String customerWithBenefitsFromVendor;
    private Double customerBenefitsFromVendor;
    private Double customerCashDownpayment;
    private Double customerCPFDownpayment;
    private String customerFinancialRequest;
    private Double customerLoanAmountRequired;
    private Integer customerLoanTenure;
    private String customerInterestPackage;
    
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
    
    /**
     * Creates a new instance of LoanOfficerProcessApplicationManagedBean
     */
    public LoanOfficerProcessMortgagePurchaseApplicationManagedBean() {
    }
    
    @PostConstruct
    public void init(){
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        applicationId = (Long) ec.getFlash().get("applicationId"); 
        
        ma = loanApplicationSessionBeanLocal.getMortgageLoanApplicationById(applicationId);
        
        customer = ma.getCustomerBasic();
        ca = customer.getCustomerAdvanced();
        debts = customer.getCustomerDebt();
        property = customer.getCustomerProperty();
        cr = customer.getBureauScore();
        accountStatus = loanApplicationSessionBeanLocal.getAccountStatusByBureauScoreId(cr.getId());
        defaultRecords = cr.getDefaultRecords();
        
        customerSalutation = customer.getCustomerSalutation();
        customerName = customer.getCustomerName();
        customerDateOfBirth = customer.getCustomerDateOfBirth();
        customerGender = customer.getCustomerGender();
        customerNationality = customer.getCustomerNationality();
        customerIdentificationNum = customer.getCustomerIdentificationNum();
        customerCountryOfResidence = customer.getCustomerCountryOfResidence();
        customerRace = customer.getCustomerRace();
        customerMobile = customer.getCustomerMobile();
        customerEmail = customer.getCustomerEmail();
        customerEducation = ca.getEducation();
        customerMaritalStatus = customer.getCustomerMaritalStatus();
        customerNumOfDependents = ca.getNumOfDependent();
        customerAddress = customer.getCustomerAddress();
        customerPostal = customer.getCustomerPostal();
        customerResidentialStatus = ca.getResidentialStatus();
        customerResidentialType = ca.getResidentialType();
        customerLengthOfResidence = ca.getYearInResidence();
        customerEmploymentStatus = ca.getEmploymentStatus();
        customerOccupation = customer.getCustomerOccupation();
        customerCompanyName = customer.getCustomerCompany();
        customerCompanyAddress = ca.getCompanyAddress();
        customerCompanyPostal = ca.getCompanyPostal();
        customerIndustryType = ca.getIndustryType();
        customerCurrentPosition = ca.getCurrentPosition();
        customerCurrentJobTitle = ca.getCurrentJobTitle();
        customerLengthOfCurrentJob = ca.getLengthOfCurrentJob();
        customerPreviousCompany = ca.getPreviousCompanyName();
        customerLengthOfPreviousJob = ca.getLengthOfPreviousJob();
        customerMonthlyFixedIncome = ca.getMonthlyFixedIncome();
        customerOtherMonthlyIncome = ca.getOtherMonthlyIncome();
        customerOtherMonthlyIncomeSource = ca.getOtherMonthlyIncomeSource();
        
        customerPropertyAddress = property.getPropertyAddress();
        customerPropertyPostal = property.getPropertyPostal();
        customerPropertyOwners = property.getPropertyOwners();
        customerPropertyType = property.getPropertyType();
        customerPropertyBuiltUpArea = property.getPropertyBuiltUpArea();
        customerPropertyLandArea = property.getPropertyLandArea();
        customerPropertyStatus = property.getPropertyStatus();
        customerPropertyTOPDate = property.getPropertyTOPDate();
        customerPropertyUsage = property.getPropertyUsage();
        customerPropertyTenureType = property.getPropertyTenureType();
        customerPropertyTenureDuration = property.getPropertyTenureDuration();
        customerPropertyTenureFromYear = property.getPropertyTenureStartYear();
        
        customerPropertyPurchasePrice = ma.getPropertyPurchasePrice();
        customerPropertyDateOfPurchase = ma.getPropertyDateOfPurchase();
        customerPropertySource = ma.getPropertySource();
        customerPropertyWithOTP = ma.getPropertyWithOTP();
        customerPropertyOTPDate = ma.getPropertyOTPDate();
        customerPropertyWithTenancy = ma.getPropertyWithTenancy();
        customerPropertyTenancyIncome = ma.getPropertyTenancyIncome();
        customerPropertyTenancyExpiryYear = ma.getPropertyTenancyExpiryYear();
        customerWithBenefitsFromVendor = ma.getWithBenifits();
        customerBenefitsFromVendor = ma.getBenefitsFromVendor();
        customerCashDownpayment = ma.getCashDownPayment();
        customerCPFDownpayment = ma.getCpfDownPayment();
        customerLoanAmountRequired = ma.getAmountRequired();
        customerLoanTenure = ma.getPeriodRequired();
        customerInterestPackage = ma.getLoanInterestPackage().getPackageName();
        docs = ma.getUploads();
        
        applicationDate = ma.getApplicationDate();
        
        
        bureauScore = cr.getBureauScore();
        riskGrade = cr.getRiskGrade();
        probabilityOfDefault = cr.getProbabilityOfDefault();
        
        appraisedValue = ma.getPropertyAppraisedValue();
        maxInterval = loanApplicationSessionBeanLocal.getMortgagePurchaseLoanMaxInterval();
        maxMin = maxInterval[0];
        maxMax = maxInterval[1];
        riskRatio = loanApplicationSessionBeanLocal.getMortgagePurchaseLoanRiskRatio();
        suggestedInterval = loanApplicationSessionBeanLocal.getMortgagePurchaseLoanSuggestedInterval();
        suggestedMin = suggestedInterval[0];
        suggestedMax = suggestedInterval[1];
    }
    
    public void approveLoanRequest() throws IOException{
        loanApplicationSessionBeanLocal.approveMortgageLoanRequest(ma.getLoanApplicationId(), amountGranted, periodSuggested, instalmentSuggested);
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(ec.getRequestContextPath() + "/web/internalSystem/loan/loanOfficerViewApplications.xhtml?faces-redirect=true");
    }
    
    public void rejectLoanRequest() throws IOException{
        loanApplicationSessionBeanLocal.rejectMortgageLoanRequest(ma.getLoanApplicationId());
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(ec.getRequestContextPath() + "/web/internalSystem/loan/loanOfficerViewApplications.xhtml?faces-redirect=true");    
    }
    
    public void calculateInstalment(){
        instalmentSuggested = (0.035/12*amountGranted) / (1 - Math.pow((1+0.035/12),-periodSuggested*12));
        DecimalFormat df = new DecimalFormat("0.00");
        instalmentSuggested = Double.valueOf(df.format(instalmentSuggested));
    }

    public LoanApplicationSessionBeanLocal getLoanApplicationSessionBeanLocal() {
        return loanApplicationSessionBeanLocal;
    }

    public void setLoanApplicationSessionBeanLocal(LoanApplicationSessionBeanLocal loanApplicationSessionBeanLocal) {
        this.loanApplicationSessionBeanLocal = loanApplicationSessionBeanLocal;
    }

    public Long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }

    public MortgageLoanApplication getMa() {
        return ma;
    }

    public void setMa(MortgageLoanApplication ma) {
        this.ma = ma;
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

    public List<CustomerDebt> getDebts() {
        return debts;
    }

    public void setDebts(List<CustomerDebt> debts) {
        this.debts = debts;
    }

    public CustomerProperty getProperty() {
        return property;
    }

    public void setProperty(CustomerProperty property) {
        this.property = property;
    }

    public Date getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(Date applicationDate) {
        this.applicationDate = applicationDate;
    }

    public double getAmountGranted() {
        return amountGranted;
    }

    public void setAmountGranted(double amountGranted) {
        this.amountGranted = amountGranted;
    }

    public int getPeriodSuggested() {
        return periodSuggested;
    }

    public void setPeriodSuggested(int periodSuggested) {
        this.periodSuggested = periodSuggested;
    }

    public double getInstalmentSuggested() {
        return instalmentSuggested;
    }

    public void setInstalmentSuggested(double instalmentSuggested) {
        this.instalmentSuggested = instalmentSuggested;
    }

    public String getCustomerSalutation() {
        return customerSalutation;
    }

    public void setCustomerSalutation(String customerSalutation) {
        this.customerSalutation = customerSalutation;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerDateOfBirth() {
        return customerDateOfBirth;
    }

    public void setCustomerDateOfBirth(String customerDateOfBirth) {
        this.customerDateOfBirth = customerDateOfBirth;
    }

    public String getCustomerGender() {
        return customerGender;
    }

    public void setCustomerGender(String customerGender) {
        this.customerGender = customerGender;
    }

    public String getCustomerNationality() {
        return customerNationality;
    }

    public void setCustomerNationality(String customerNationality) {
        this.customerNationality = customerNationality;
    }

    public String getCustomerIdentificationNum() {
        return customerIdentificationNum;
    }

    public void setCustomerIdentificationNum(String customerIdentificationNum) {
        this.customerIdentificationNum = customerIdentificationNum;
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

    public String getCustomerMobile() {
        return customerMobile;
    }

    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerEducation() {
        return customerEducation;
    }

    public void setCustomerEducation(String customerEducation) {
        this.customerEducation = customerEducation;
    }

    public String getCustomerMaritalStatus() {
        return customerMaritalStatus;
    }

    public void setCustomerMaritalStatus(String customerMaritalStatus) {
        this.customerMaritalStatus = customerMaritalStatus;
    }

    public Integer getCustomerNumOfDependents() {
        return customerNumOfDependents;
    }

    public void setCustomerNumOfDependents(Integer customerNumOfDependents) {
        this.customerNumOfDependents = customerNumOfDependents;
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

    public String getCustomerResidentialStatus() {
        return customerResidentialStatus;
    }

    public void setCustomerResidentialStatus(String customerResidentialStatus) {
        this.customerResidentialStatus = customerResidentialStatus;
    }

    public String getCustomerResidentialType() {
        return customerResidentialType;
    }

    public void setCustomerResidentialType(String customerResidentialType) {
        this.customerResidentialType = customerResidentialType;
    }

    public Integer getCustomerLengthOfResidence() {
        return customerLengthOfResidence;
    }

    public void setCustomerLengthOfResidence(Integer customerLengthOfResidence) {
        this.customerLengthOfResidence = customerLengthOfResidence;
    }

    public String getCustomerEmploymentStatus() {
        return customerEmploymentStatus;
    }

    public void setCustomerEmploymentStatus(String customerEmploymentStatus) {
        this.customerEmploymentStatus = customerEmploymentStatus;
    }

    public String getCustomerOccupation() {
        return customerOccupation;
    }

    public void setCustomerOccupation(String customerOccupation) {
        this.customerOccupation = customerOccupation;
    }

    public String getCustomerCompanyName() {
        return customerCompanyName;
    }

    public void setCustomerCompanyName(String customerCompanyName) {
        this.customerCompanyName = customerCompanyName;
    }

    public String getCustomerCompanyAddress() {
        return customerCompanyAddress;
    }

    public void setCustomerCompanyAddress(String customerCompanyAddress) {
        this.customerCompanyAddress = customerCompanyAddress;
    }

    public String getCustomerCompanyPostal() {
        return customerCompanyPostal;
    }

    public void setCustomerCompanyPostal(String customerCompanyPostal) {
        this.customerCompanyPostal = customerCompanyPostal;
    }

    public String getCustomerIndustryType() {
        return customerIndustryType;
    }

    public void setCustomerIndustryType(String customerIndustryType) {
        this.customerIndustryType = customerIndustryType;
    }

    public String getCustomerCurrentPosition() {
        return customerCurrentPosition;
    }

    public void setCustomerCurrentPosition(String customerCurrentPosition) {
        this.customerCurrentPosition = customerCurrentPosition;
    }

    public String getCustomerCurrentJobTitle() {
        return customerCurrentJobTitle;
    }

    public void setCustomerCurrentJobTitle(String customerCurrentJobTitle) {
        this.customerCurrentJobTitle = customerCurrentJobTitle;
    }

    public Integer getCustomerLengthOfCurrentJob() {
        return customerLengthOfCurrentJob;
    }

    public void setCustomerLengthOfCurrentJob(Integer customerLengthOfCurrentJob) {
        this.customerLengthOfCurrentJob = customerLengthOfCurrentJob;
    }

    public String getCustomerPreviousCompany() {
        return customerPreviousCompany;
    }

    public void setCustomerPreviousCompany(String customerPreviousCompany) {
        this.customerPreviousCompany = customerPreviousCompany;
    }

    public Integer getCustomerLengthOfPreviousJob() {
        return customerLengthOfPreviousJob;
    }

    public void setCustomerLengthOfPreviousJob(Integer customerLengthOfPreviousJob) {
        this.customerLengthOfPreviousJob = customerLengthOfPreviousJob;
    }

    public Double getCustomerMonthlyFixedIncome() {
        return customerMonthlyFixedIncome;
    }

    public void setCustomerMonthlyFixedIncome(Double customerMonthlyFixedIncome) {
        this.customerMonthlyFixedIncome = customerMonthlyFixedIncome;
    }

    public Double getCustomerOtherMonthlyIncome() {
        return customerOtherMonthlyIncome;
    }

    public void setCustomerOtherMonthlyIncome(Double customerOtherMonthlyIncome) {
        this.customerOtherMonthlyIncome = customerOtherMonthlyIncome;
    }

    public String getCustomerOtherMonthlyIncomeSource() {
        return customerOtherMonthlyIncomeSource;
    }

    public void setCustomerOtherMonthlyIncomeSource(String customerOtherMonthlyIncomeSource) {
        this.customerOtherMonthlyIncomeSource = customerOtherMonthlyIncomeSource;
    }

    public String getCustomerPropertyAddress() {
        return customerPropertyAddress;
    }

    public void setCustomerPropertyAddress(String customerPropertyAddress) {
        this.customerPropertyAddress = customerPropertyAddress;
    }

    public String getCustomerPropertyPostal() {
        return customerPropertyPostal;
    }

    public void setCustomerPropertyPostal(String customerPropertyPostal) {
        this.customerPropertyPostal = customerPropertyPostal;
    }

    public ArrayList<String> getCustomerPropertyOwners() {
        return customerPropertyOwners;
    }

    public void setCustomerPropertyOwners(ArrayList<String> customerPropertyOwners) {
        this.customerPropertyOwners = customerPropertyOwners;
    }

    public String getCustomerPropertyType() {
        return customerPropertyType;
    }

    public void setCustomerPropertyType(String customerPropertyType) {
        this.customerPropertyType = customerPropertyType;
    }

    public Double getCustomerPropertyBuiltUpArea() {
        return customerPropertyBuiltUpArea;
    }

    public void setCustomerPropertyBuiltUpArea(Double customerPropertyBuiltUpArea) {
        this.customerPropertyBuiltUpArea = customerPropertyBuiltUpArea;
    }

    public Double getCustomerPropertyLandArea() {
        return customerPropertyLandArea;
    }

    public void setCustomerPropertyLandArea(Double customerPropertyLandArea) {
        this.customerPropertyLandArea = customerPropertyLandArea;
    }

    public String getCustomerPropertyStatus() {
        return customerPropertyStatus;
    }

    public void setCustomerPropertyStatus(String customerPropertyStatus) {
        this.customerPropertyStatus = customerPropertyStatus;
    }

    public Date getCustomerPropertyTOPDate() {
        return customerPropertyTOPDate;
    }

    public void setCustomerPropertyTOPDate(Date customerPropertyTOPDate) {
        this.customerPropertyTOPDate = customerPropertyTOPDate;
    }

    public String getCustomerPropertyUsage() {
        return customerPropertyUsage;
    }

    public void setCustomerPropertyUsage(String customerPropertyUsage) {
        this.customerPropertyUsage = customerPropertyUsage;
    }

    public String getCustomerPropertyTenureType() {
        return customerPropertyTenureType;
    }

    public void setCustomerPropertyTenureType(String customerPropertyTenureType) {
        this.customerPropertyTenureType = customerPropertyTenureType;
    }

    public Integer getCustomerPropertyTenureDuration() {
        return customerPropertyTenureDuration;
    }

    public void setCustomerPropertyTenureDuration(Integer customerPropertyTenureDuration) {
        this.customerPropertyTenureDuration = customerPropertyTenureDuration;
    }

    public Integer getCustomerPropertyTenureFromYear() {
        return customerPropertyTenureFromYear;
    }

    public void setCustomerPropertyTenureFromYear(Integer customerPropertyTenureFromYear) {
        this.customerPropertyTenureFromYear = customerPropertyTenureFromYear;
    }

    public Double getCustomerPropertyPurchasePrice() {
        return customerPropertyPurchasePrice;
    }

    public void setCustomerPropertyPurchasePrice(Double customerPropertyPurchasePrice) {
        this.customerPropertyPurchasePrice = customerPropertyPurchasePrice;
    }

    public Date getCustomerPropertyDateOfPurchase() {
        return customerPropertyDateOfPurchase;
    }

    public void setCustomerPropertyDateOfPurchase(Date customerPropertyDateOfPurchase) {
        this.customerPropertyDateOfPurchase = customerPropertyDateOfPurchase;
    }

    public String getCustomerPropertySource() {
        return customerPropertySource;
    }

    public void setCustomerPropertySource(String customerPropertySource) {
        this.customerPropertySource = customerPropertySource;
    }

    public String getCustomerPropertyWithOTP() {
        return customerPropertyWithOTP;
    }

    public void setCustomerPropertyWithOTP(String customerPropertyWithOTP) {
        this.customerPropertyWithOTP = customerPropertyWithOTP;
    }

    public Date getCustomerPropertyOTPDate() {
        return customerPropertyOTPDate;
    }

    public void setCustomerPropertyOTPDate(Date customerPropertyOTPDate) {
        this.customerPropertyOTPDate = customerPropertyOTPDate;
    }

    public String getCustomerPropertyWithTenancy() {
        return customerPropertyWithTenancy;
    }

    public void setCustomerPropertyWithTenancy(String customerPropertyWithTenancy) {
        this.customerPropertyWithTenancy = customerPropertyWithTenancy;
    }

    public Double getCustomerPropertyTenancyIncome() {
        return customerPropertyTenancyIncome;
    }

    public void setCustomerPropertyTenancyIncome(Double customerPropertyTenancyIncome) {
        this.customerPropertyTenancyIncome = customerPropertyTenancyIncome;
    }

    public Integer getCustomerPropertyTenancyExpiryYear() {
        return customerPropertyTenancyExpiryYear;
    }

    public void setCustomerPropertyTenancyExpiryYear(Integer customerPropertyTenancyExpiryYear) {
        this.customerPropertyTenancyExpiryYear = customerPropertyTenancyExpiryYear;
    }

    public String getCustomerWithBenefitsFromVendor() {
        return customerWithBenefitsFromVendor;
    }

    public void setCustomerWithBenefitsFromVendor(String customerWithBenefitsFromVendor) {
        this.customerWithBenefitsFromVendor = customerWithBenefitsFromVendor;
    }

    public Double getCustomerBenefitsFromVendor() {
        return customerBenefitsFromVendor;
    }

    public void setCustomerBenefitsFromVendor(Double customerBenefitsFromVendor) {
        this.customerBenefitsFromVendor = customerBenefitsFromVendor;
    }

    public Double getCustomerCashDownpayment() {
        return customerCashDownpayment;
    }

    public void setCustomerCashDownpayment(Double customerCashDownpayment) {
        this.customerCashDownpayment = customerCashDownpayment;
    }

    public Double getCustomerCPFDownpayment() {
        return customerCPFDownpayment;
    }

    public void setCustomerCPFDownpayment(Double customerCPFDownpayment) {
        this.customerCPFDownpayment = customerCPFDownpayment;
    }

    public String getCustomerFinancialRequest() {
        return customerFinancialRequest;
    }

    public void setCustomerFinancialRequest(String customerFinancialRequest) {
        this.customerFinancialRequest = customerFinancialRequest;
    }

    public Double getCustomerLoanAmountRequired() {
        return customerLoanAmountRequired;
    }

    public void setCustomerLoanAmountRequired(Double customerLoanAmountRequired) {
        this.customerLoanAmountRequired = customerLoanAmountRequired;
    }

    public Integer getCustomerLoanTenure() {
        return customerLoanTenure;
    }

    public void setCustomerLoanTenure(Integer customerLoanTenure) {
        this.customerLoanTenure = customerLoanTenure;
    }

    public String getCustomerInterestPackage() {
        return customerInterestPackage;
    }

    public void setCustomerInterestPackage(String customerInterestPackage) {
        this.customerInterestPackage = customerInterestPackage;
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

}
