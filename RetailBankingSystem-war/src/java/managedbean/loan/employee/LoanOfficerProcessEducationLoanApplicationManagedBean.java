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
import ejb.loan.entity.EducationLoanApplication;
import ejb.loan.entity.EducationLoanGuarantor;
import ejb.loan.session.LoanApplicationSessionBeanLocal;
import java.io.IOException;
import java.io.Serializable;
import java.text.DecimalFormat;
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
@Named(value = "loanOfficerProcessEducationLoanManagedBean")
@ViewScoped
public class LoanOfficerProcessEducationLoanApplicationManagedBean implements Serializable {

    @EJB
    private LoanApplicationSessionBeanLocal loanApplicationSessionBeanLocal;

    private Long applicationId;
    private String loanType;
    private CustomerProperty property;

    private EducationLoanApplication application;
    private CustomerBasic customer;
    private CustomerAdvanced ca;
    private List<CustomerDebt> debts;

    private Date applicationDate;

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

    //loan
    private Double customerLoanAmountRequired;
    private Integer customerLoanTenure;
    private String customerEducationInstitution;
    private Date customerEducationStarton;
    private Date customerEducationEndon;
    private Integer customerCourseDuration;
    private Integer customerCourseFee;

    //guarantor basic information
    private String relationship;
    private EducationLoanGuarantor guarantor;

    private String guarantorSalutation;
    private String guarantorName;
    private String guarantorDateOfBirth;
    private String guarantorGender;
    private String guarantorNationality;
    private String guarantorIdentificationNum;
    private String guarantorCountryOfResidence;
    private String guarantorRace;
    private String guarantorMobile;
    private String guarantorEmail;

    //guarantor personal details
    private String guarantorEducation;
    private String guarantorMaritalStatus;
    private Integer guarantorNumOfDependents;
    private String guarantorAddress;
    private String guarantorPostal;
    private String guarantorResidentialStatus;
    private String guarantorResidentialType;
    private Integer guarantorLengthOfResidence;

    //guarantor employment details
    private String guarantorEmploymentStatus;
    private String guarantorOccupation;
    private String guarantorCompanyName;
    private String guarantorCompanyAddress;
    private String guarantorCompanyPostal;
    private String guarantorIndustryType;
    private String guarantorCurrentPosition;
    private String guarantorCurrentJobTitle;
    private Integer guarantorLengthOfCurrentJob;
    private String guarantorPreviousCompany;
    private Integer guarantorLengthOfPreviousJob;
    private Double guarantorMonthlyFixedIncome;
    private Double guarantorOtherMonthlyIncome;
    private String guarantorOtherMonthlyIncomeSource;

    //documents
    private HashMap docs;

    //credit report
    CreditReportBureauScore cr;
    private List<CreditReportAccountStatus> accountStatus;
    private List<CreditReportDefaultRecords> defaultRecords;
    private Double bureauScore;
    private String riskGrade;
    private Double probabilityOfDefault;

    //decision support
    private double riskRatio;
    private double totalAmountOverdue;

    private String suggestedAction;
    private double maxAmountGranted;
    private int minTenure;
    private double maxInstalment;
    private double maxAllowableAmount;

    private double amountGranted;
    private int periodSuggested;
    private double instalmentSuggested;
    private double interest;

    /**
     * Creates a new instance of LoanOfficerProcessEducationLoanManagedBean
     */
    public LoanOfficerProcessEducationLoanApplicationManagedBean() {
    }

    @PostConstruct
    public void init() {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        applicationId = (Long) ec.getFlash().get("applicationId");

        application = loanApplicationSessionBeanLocal.getEducationLoanApplicationById(applicationId);
        loanType = application.getLoanType();
        interest = application.getLoanInterestPackage().getInterestRate();

        customer = application.getCustomerBasic();
        ca = customer.getCustomerAdvanced();
        debts = customer.getCustomerDebt();
        property = customer.getCustomerProperty();
        cr = customer.getBureauScore();
        if (cr != null) {
            accountStatus = loanApplicationSessionBeanLocal.getAccountStatusByBureauScoreId(cr.getId());
            defaultRecords = cr.getDefaultRecords();
            bureauScore = cr.getBureauScore();
            riskGrade = cr.getRiskGrade();
            probabilityOfDefault = cr.getProbabilityOfDefault();
        }

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

        customerLoanAmountRequired = application.getAmountRequired();
        customerLoanTenure = application.getPeriodRequired();
        customerEducationInstitution = application.getEducationInstitution();
        customerEducationStarton = application.getEducationStarton();
        customerEducationEndon = application.getEducationEndon();
        customerCourseDuration = application.getCourseDuration();
        customerCourseFee = application.getCourseFee();

        guarantor = application.getEducationLoanGuarantor();
        relationship = application.getRelationship();

        guarantorSalutation = guarantor.getSalutation();
        guarantorName = guarantor.getName();
        guarantorDateOfBirth = guarantor.getDateOfBirth();
        guarantorGender = guarantor.getGender();
        guarantorNationality = guarantor.getNationality();
        guarantorIdentificationNum = guarantor.getIdentificationNum();
        guarantorCountryOfResidence = guarantor.getCountryOfResidence();
        guarantorRace = guarantor.getRace();
        guarantorMobile = guarantor.getMobile();
        guarantorEmail = guarantor.getEmail();
        guarantorEducation = guarantor.getEducation();
        guarantorMaritalStatus = guarantor.getMaritalStatus();
        guarantorNumOfDependents = guarantor.getNumOfDependent();
        guarantorAddress = guarantor.getAddress();
        guarantorPostal = guarantor.getPostal();
        guarantorResidentialStatus = guarantor.getResidentialStatus();
        guarantorResidentialType = guarantor.getResidentialType();
        guarantorLengthOfResidence = guarantor.getYearInResidence();
        guarantorEmploymentStatus = guarantor.getEmploymentStatus();
        guarantorOccupation = guarantor.getOccupation();
        guarantorCompanyName = guarantor.getCompany();
        guarantorCompanyAddress = guarantor.getCompanyAddress();
        guarantorCompanyPostal = guarantor.getCompanyPostal();
        guarantorIndustryType = guarantor.getIndustryType();
        guarantorCurrentPosition = guarantor.getCurrentPosition();
        guarantorCurrentJobTitle = guarantor.getCurrentJobTitle();
        guarantorLengthOfCurrentJob = guarantor.getLengthOfCurrentJob();
        guarantorPreviousCompany = guarantor.getPreviousCompanyName();
        guarantorLengthOfPreviousJob = guarantor.getLengthOfPreviousJob();
        guarantorMonthlyFixedIncome = guarantor.getMonthlyFixedIncome();
        guarantorOtherMonthlyIncome = guarantor.getOtherMonthlyIncome();
        guarantorOtherMonthlyIncomeSource = guarantor.getOtherMonthlyIncomeSource();

        docs = application.getUploads();

        applicationDate = application.getApplicationDate();

        // decision support
        riskRatio = loanApplicationSessionBeanLocal.getRiskRatio(customer, null);

        if (cr != null) {
            for (CreditReportAccountStatus account : accountStatus) {
                totalAmountOverdue += account.getOverdueBalance();
            }
        }

        if (riskRatio > 0.35) {
            suggestedAction = "Reject";
            maxAmountGranted = 0;
            minTenure = 0;
            maxInstalment = 0;
        } else {
            suggestedAction = "Approve";

            maxAllowableAmount = customerCourseFee * 0.9;
            if (customerLoanAmountRequired < maxAllowableAmount) {
                maxAllowableAmount = customerLoanAmountRequired;
            }

            maxAmountGranted = maxAllowableAmount * (1 - riskRatio);
            minTenure = customerLoanTenure;
        }

    }

    public void approveLoanRequest() throws IOException {
        loanApplicationSessionBeanLocal.approveLoanRequest(application.getLoanApplicationId(), amountGranted, periodSuggested, instalmentSuggested, "Education Loan");
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(ec.getRequestContextPath() + "/web/internalSystem/loan/loanOfficerViewApplications.xhtml?faces-redirect=true");
    }

    public void rejectLoanRequest() throws IOException {
        loanApplicationSessionBeanLocal.rejectLoanRequest(application.getLoanApplicationId());
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(ec.getRequestContextPath() + "/web/internalSystem/loan/loanOfficerViewApplications.xhtml?faces-redirect=true");
    }

    public void calculateInstalment() {
        instalmentSuggested = (interest / 12 * amountGranted) / (1 - Math.pow((1 + interest / 12), -periodSuggested * 12));
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

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public CustomerProperty getProperty() {
        return property;
    }

    public void setProperty(CustomerProperty property) {
        this.property = property;
    }

    public EducationLoanApplication getApplication() {
        return application;
    }

    public void setApplication(EducationLoanApplication application) {
        this.application = application;
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

    public Date getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(Date applicationDate) {
        this.applicationDate = applicationDate;
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

    public String getCustomerEducationInstitution() {
        return customerEducationInstitution;
    }

    public void setCustomerEducationInstitution(String customerEducationInstitution) {
        this.customerEducationInstitution = customerEducationInstitution;
    }

    public Date getCustomerEducationStarton() {
        return customerEducationStarton;
    }

    public void setCustomerEducationStarton(Date customerEducationStarton) {
        this.customerEducationStarton = customerEducationStarton;
    }

    public Date getCustomerEducationEndon() {
        return customerEducationEndon;
    }

    public void setCustomerEducationEndon(Date customerEducationEndon) {
        this.customerEducationEndon = customerEducationEndon;
    }

    public Integer getCustomerCourseDuration() {
        return customerCourseDuration;
    }

    public void setCustomerCourseDuration(Integer customerCourseDuration) {
        this.customerCourseDuration = customerCourseDuration;
    }

    public Integer getCustomerCourseFee() {
        return customerCourseFee;
    }

    public void setCustomerCourseFee(Integer customerCourseFee) {
        this.customerCourseFee = customerCourseFee;
    }

    public String getGuarantorSalutation() {
        return guarantorSalutation;
    }

    public void setGuarantorSalutation(String guarantorSalutation) {
        this.guarantorSalutation = guarantorSalutation;
    }

    public String getGuarantorName() {
        return guarantorName;
    }

    public void setGuarantorName(String guarantorName) {
        this.guarantorName = guarantorName;
    }

    public String getGuarantorDateOfBirth() {
        return guarantorDateOfBirth;
    }

    public void setGuarantorDateOfBirth(String guarantorDateOfBirth) {
        this.guarantorDateOfBirth = guarantorDateOfBirth;
    }

    public String getGuarantorGender() {
        return guarantorGender;
    }

    public void setGuarantorGender(String guarantorGender) {
        this.guarantorGender = guarantorGender;
    }

    public String getGuarantorNationality() {
        return guarantorNationality;
    }

    public void setGuarantorNationality(String guarantorNationality) {
        this.guarantorNationality = guarantorNationality;
    }

    public String getGuarantorIdentificationNum() {
        return guarantorIdentificationNum;
    }

    public void setGuarantorIdentificationNum(String guarantorIdentificationNum) {
        this.guarantorIdentificationNum = guarantorIdentificationNum;
    }

    public String getGuarantorCountryOfResidence() {
        return guarantorCountryOfResidence;
    }

    public void setGuarantorCountryOfResidence(String guarantorCountryOfResidence) {
        this.guarantorCountryOfResidence = guarantorCountryOfResidence;
    }

    public String getGuarantorRace() {
        return guarantorRace;
    }

    public void setGuarantorRace(String guarantorRace) {
        this.guarantorRace = guarantorRace;
    }

    public String getGuarantorMobile() {
        return guarantorMobile;
    }

    public void setGuarantorMobile(String guarantorMobile) {
        this.guarantorMobile = guarantorMobile;
    }

    public String getGuarantorEmail() {
        return guarantorEmail;
    }

    public void setGuarantorEmail(String guarantorEmail) {
        this.guarantorEmail = guarantorEmail;
    }

    public String getGuarantorEducation() {
        return guarantorEducation;
    }

    public void setGuarantorEducation(String guarantorEducation) {
        this.guarantorEducation = guarantorEducation;
    }

    public String getGuarantorMaritalStatus() {
        return guarantorMaritalStatus;
    }

    public void setGuarantorMaritalStatus(String guarantorMaritalStatus) {
        this.guarantorMaritalStatus = guarantorMaritalStatus;
    }

    public Integer getGuarantorNumOfDependents() {
        return guarantorNumOfDependents;
    }

    public void setGuarantorNumOfDependents(Integer guarantorNumOfDependents) {
        this.guarantorNumOfDependents = guarantorNumOfDependents;
    }

    public String getGuarantorAddress() {
        return guarantorAddress;
    }

    public void setGuarantorAddress(String guarantorAddress) {
        this.guarantorAddress = guarantorAddress;
    }

    public String getGuarantorPostal() {
        return guarantorPostal;
    }

    public void setGuarantorPostal(String guarantorPostal) {
        this.guarantorPostal = guarantorPostal;
    }

    public String getGuarantorResidentialStatus() {
        return guarantorResidentialStatus;
    }

    public void setGuarantorResidentialStatus(String guarantorResidentialStatus) {
        this.guarantorResidentialStatus = guarantorResidentialStatus;
    }

    public String getGuarantorResidentialType() {
        return guarantorResidentialType;
    }

    public void setGuarantorResidentialType(String guarantorResidentialType) {
        this.guarantorResidentialType = guarantorResidentialType;
    }

    public Integer getGuarantorLengthOfResidence() {
        return guarantorLengthOfResidence;
    }

    public void setGuarantorLengthOfResidence(Integer guarantorLengthOfResidence) {
        this.guarantorLengthOfResidence = guarantorLengthOfResidence;
    }

    public String getGuarantorEmploymentStatus() {
        return guarantorEmploymentStatus;
    }

    public void setGuarantorEmploymentStatus(String guarantorEmploymentStatus) {
        this.guarantorEmploymentStatus = guarantorEmploymentStatus;
    }

    public String getGuarantorOccupation() {
        return guarantorOccupation;
    }

    public void setGuarantorOccupation(String guarantorOccupation) {
        this.guarantorOccupation = guarantorOccupation;
    }

    public String getGuarantorCompanyName() {
        return guarantorCompanyName;
    }

    public void setGuarantorCompanyName(String guarantorCompanyName) {
        this.guarantorCompanyName = guarantorCompanyName;
    }

    public String getGuarantorCompanyAddress() {
        return guarantorCompanyAddress;
    }

    public void setGuarantorCompanyAddress(String guarantorCompanyAddress) {
        this.guarantorCompanyAddress = guarantorCompanyAddress;
    }

    public String getGuarantorCompanyPostal() {
        return guarantorCompanyPostal;
    }

    public void setGuarantorCompanyPostal(String guarantorCompanyPostal) {
        this.guarantorCompanyPostal = guarantorCompanyPostal;
    }

    public String getGuarantorIndustryType() {
        return guarantorIndustryType;
    }

    public void setGuarantorIndustryType(String guarantorIndustryType) {
        this.guarantorIndustryType = guarantorIndustryType;
    }

    public String getGuarantorCurrentPosition() {
        return guarantorCurrentPosition;
    }

    public void setGuarantorCurrentPosition(String guarantorCurrentPosition) {
        this.guarantorCurrentPosition = guarantorCurrentPosition;
    }

    public String getGuarantorCurrentJobTitle() {
        return guarantorCurrentJobTitle;
    }

    public void setGuarantorCurrentJobTitle(String guarantorCurrentJobTitle) {
        this.guarantorCurrentJobTitle = guarantorCurrentJobTitle;
    }

    public Integer getGuarantorLengthOfCurrentJob() {
        return guarantorLengthOfCurrentJob;
    }

    public void setGuarantorLengthOfCurrentJob(Integer guarantorLengthOfCurrentJob) {
        this.guarantorLengthOfCurrentJob = guarantorLengthOfCurrentJob;
    }

    public String getGuarantorPreviousCompany() {
        return guarantorPreviousCompany;
    }

    public void setGuarantorPreviousCompany(String guarantorPreviousCompany) {
        this.guarantorPreviousCompany = guarantorPreviousCompany;
    }

    public Integer getGuarantorLengthOfPreviousJob() {
        return guarantorLengthOfPreviousJob;
    }

    public void setGuarantorLengthOfPreviousJob(Integer guarantorLengthOfPreviousJob) {
        this.guarantorLengthOfPreviousJob = guarantorLengthOfPreviousJob;
    }

    public Double getGuarantorMonthlyFixedIncome() {
        return guarantorMonthlyFixedIncome;
    }

    public void setGuarantorMonthlyFixedIncome(Double guarantorMonthlyFixedIncome) {
        this.guarantorMonthlyFixedIncome = guarantorMonthlyFixedIncome;
    }

    public Double getGuarantorOtherMonthlyIncome() {
        return guarantorOtherMonthlyIncome;
    }

    public void setGuarantorOtherMonthlyIncome(Double guarantorOtherMonthlyIncome) {
        this.guarantorOtherMonthlyIncome = guarantorOtherMonthlyIncome;
    }

    public String getGuarantorOtherMonthlyIncomeSource() {
        return guarantorOtherMonthlyIncomeSource;
    }

    public void setGuarantorOtherMonthlyIncomeSource(String guarantorOtherMonthlyIncomeSource) {
        this.guarantorOtherMonthlyIncomeSource = guarantorOtherMonthlyIncomeSource;
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

    public double getRiskRatio() {
        return riskRatio;
    }

    public void setRiskRatio(double riskRatio) {
        this.riskRatio = riskRatio;
    }

    public double getTotalAmountOverdue() {
        return totalAmountOverdue;
    }

    public void setTotalAmountOverdue(double totalAmountOverdue) {
        this.totalAmountOverdue = totalAmountOverdue;
    }

    public String getSuggestedAction() {
        return suggestedAction;
    }

    public void setSuggestedAction(String suggestedAction) {
        this.suggestedAction = suggestedAction;
    }

    public double getMaxAmountGranted() {
        return maxAmountGranted;
    }

    public void setMaxAmountGranted(double maxAmountGranted) {
        this.maxAmountGranted = maxAmountGranted;
    }

    public int getMinTenure() {
        return minTenure;
    }

    public void setMinTenure(int minTenure) {
        this.minTenure = minTenure;
    }

    public double getMaxInstalment() {
        return maxInstalment;
    }

    public void setMaxInstalment(double maxInstalment) {
        this.maxInstalment = maxInstalment;
    }

    public double getMaxAllowableAmount() {
        return maxAllowableAmount;
    }

    public void setMaxAllowableAmount(double maxAllowableAmount) {
        this.maxAllowableAmount = maxAllowableAmount;
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

    public double getInterest() {
        return interest;
    }

    public void setInterest(double interest) {
        this.interest = interest;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public EducationLoanGuarantor getGuarantor() {
        return guarantor;
    }

    public void setGuarantor(EducationLoanGuarantor guarantor) {
        this.guarantor = guarantor;
    }

}
