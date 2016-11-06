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
import ejb.loan.entity.RefinancingApplication;
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
@Named(value = "loanOfficerProcessMortgageRefinancingApplicationManagedBean")
@ViewScoped
public class LoanOfficerProcessMortgageRefinancingApplicationManagedBean implements Serializable {

    @EJB
    private LoanApplicationSessionBeanLocal loanApplicationSessionBeanLocal;

    private Long applicationId;
    private String loanType;

    private RefinancingApplication ra;
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

    //loan - refinancing
    private String customerExistingFinancer;
    private Double customerOutstandingLoan;
    private Integer customerOutstandingYear;
    private Integer customerOutstandingMonth;
    private Double customerTotalCPFWithdrawal;
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

    //joint applicant
    private boolean hasJoint;
    private boolean noJoint;
    private CustomerBasic joint;
    private String relationship;
    private CustomerAdvanced jointCA;
    private List<CustomerDebt> jointDebts;

    //joint basic information
    private String jointSalutation;
    private String jointName;
    private String jointDateOfBirth;
    private String jointGender;
    private String jointNationality;
    private String jointIdentificationNum;
    private String jointCountryOfResidence;
    private String jointRace;
    private String jointMobile;
    private String jointEmail;

    //joint personal details
    private String jointEducation;
    private String jointMaritalStatus;
    private Integer jointNumOfDependents;
    private String jointAddress;
    private String jointPostal;
    private String jointResidentialStatus;
    private String jointResidentialType;
    private Integer jointLengthOfResidence;

    //joint employment details
    private String jointEmploymentStatus;
    private String jointOccupation;
    private String jointCompanyName;
    private String jointCompanyAddress;
    private String jointCompanyPostal;
    private String jointIndustryType;
    private String jointCurrentPosition;
    private String jointCurrentJobTitle;
    private Integer jointLengthOfCurrentJob;
    private String jointPreviousCompany;
    private Integer jointLengthOfPreviousJob;
    private Double jointMonthlyFixedIncome;
    private Double jointOtherMonthlyIncome;
    private String jointOtherMonthlyIncomeSource;

    //joint credit report
    CreditReportBureauScore jointCR;
    private List<CreditReportAccountStatus> jointAccountStatus;
    private List<CreditReportDefaultRecords> jointDefaultRecords;
    private Double jointBureauScore;
    private String jointRiskGrade;
    private Double jointProbabilityOfDefault;

    //decision support
    private int customerAge;
    private int jointAge;
    private double riskRatio;
    private int averageAge;
    private double maxTDSRInstalment;
    private double maxMSRInstalment;
    private double totalAmountOverdue;
    private double jointAmountOverdue;

    private String suggestedAction;
    private int minTenure;
    private double maxInstalment;

    /**
     * Creates a new instance of
     * LoanOfficerProcessMortgageRefinancingApplicationManagedBean
     */
    public LoanOfficerProcessMortgageRefinancingApplicationManagedBean() {
    }

    @PostConstruct
    public void init() {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        applicationId = (Long) ec.getFlash().get("applicationId");
        System.out.println(applicationId);

        ra = loanApplicationSessionBeanLocal.getRefinancingApplicationById(applicationId);
        loanType = ra.getLoanType();
        customer = ra.getCustomerBasic();
        ca = customer.getCustomerAdvanced();
        debts = customer.getCustomerDebt();
        property = customer.getCustomerProperty();
        cr = customer.getBureauScore();
        if (cr != null) {
            accountStatus = cr.getAccountStatus();
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

        customerExistingFinancer = ra.getExistingFinancer();
        customerOutstandingLoan = ra.getOutstandingBalance();
        customerOutstandingYear = ra.getOutstandingYear();
        customerOutstandingMonth = ra.getOutstandingMonth();
        customerTotalCPFWithdrawal = ra.getTotalCPFWithdrawal();
        customerLoanTenure = ra.getPeriodRequired();
        customerInterestPackage = ra.getLoanInterestPackage().getPackageName();
        docs = ra.getUploads();

        applicationDate = ra.getApplicationDate();

        //joint applicant        
        joint = ra.getCustomer();
        if (joint == null) {
            hasJoint = false;
            noJoint = true;
        } else {
            hasJoint = true;
            noJoint = false;
            relationship = ra.getRelationship();
            jointCA = joint.getCustomerAdvanced();
            jointDebts = joint.getCustomerDebt();
            jointCR = joint.getBureauScore();
            if (jointCR != null) {
                jointAccountStatus = loanApplicationSessionBeanLocal.getAccountStatusByBureauScoreId(jointCR.getId());
                jointDefaultRecords = jointCR.getDefaultRecords();
                jointBureauScore = jointCR.getBureauScore();
                jointRiskGrade = jointCR.getRiskGrade();
                jointProbabilityOfDefault = jointCR.getProbabilityOfDefault();
            }

            jointSalutation = joint.getCustomerSalutation();
            jointName = joint.getCustomerName();
            jointDateOfBirth = joint.getCustomerDateOfBirth();
            jointGender = joint.getCustomerGender();
            jointNationality = joint.getCustomerNationality();
            jointIdentificationNum = joint.getCustomerIdentificationNum();
            jointCountryOfResidence = joint.getCustomerCountryOfResidence();
            jointRace = joint.getCustomerRace();
            jointMobile = joint.getCustomerMobile();
            jointEmail = joint.getCustomerEmail();
            jointEducation = jointCA.getEducation();
            jointMaritalStatus = joint.getCustomerMaritalStatus();
            jointNumOfDependents = jointCA.getNumOfDependent();
            jointAddress = joint.getCustomerAddress();
            jointPostal = joint.getCustomerPostal();
            jointResidentialStatus = jointCA.getResidentialStatus();
            jointResidentialType = jointCA.getResidentialType();
            jointLengthOfResidence = jointCA.getYearInResidence();
            jointEmploymentStatus = jointCA.getEmploymentStatus();
            jointOccupation = joint.getCustomerOccupation();
            jointCompanyName = joint.getCustomerCompany();
            jointCompanyAddress = jointCA.getCompanyAddress();
            jointCompanyPostal = jointCA.getCompanyPostal();
            jointIndustryType = jointCA.getIndustryType();
            jointCurrentPosition = jointCA.getCurrentPosition();
            jointCurrentJobTitle = jointCA.getCurrentJobTitle();
            jointLengthOfCurrentJob = jointCA.getLengthOfCurrentJob();
            jointPreviousCompany = jointCA.getPreviousCompanyName();
            jointLengthOfPreviousJob = jointCA.getLengthOfPreviousJob();
            jointMonthlyFixedIncome = jointCA.getMonthlyFixedIncome();
            jointOtherMonthlyIncome = jointCA.getOtherMonthlyIncome();
            jointOtherMonthlyIncomeSource = jointCA.getOtherMonthlyIncomeSource();

            //Decison support
            jointAge = Integer.valueOf(joint.getCustomerAge());
            averageAge = loanApplicationSessionBeanLocal.getApplicantsAverageAge(customer, joint);
            if (jointCR != null) {
                for (CreditReportAccountStatus account : jointAccountStatus) {
                    jointAmountOverdue += account.getOverdueBalance();
                }
            }

        }

        // decision support
        customerAge = Integer.valueOf(customer.getCustomerAge());
        riskRatio = loanApplicationSessionBeanLocal.getRiskRatio(customer, joint);

        maxTDSRInstalment = loanApplicationSessionBeanLocal.getTDSRRemaining(customer, joint);
        if (loanType.contains("HDB")) {
            maxMSRInstalment = loanApplicationSessionBeanLocal.getMSRRemaining(customer, joint);
        }

        if (cr != null) {
            for (CreditReportAccountStatus account : accountStatus) {
                totalAmountOverdue += account.getOverdueBalance();
            }
        }

        if (riskRatio > 0.35) {
            suggestedAction = "Reject";
            minTenure = 0;
            maxInstalment = 0;
        } else {
            suggestedAction = "Approve";

            if (loanType.contains("HDB")) {
                maxInstalment = Math.min(maxTDSRInstalment, maxMSRInstalment);
            } else {
                maxInstalment = maxTDSRInstalment;
            }
            minTenure = loanApplicationSessionBeanLocal.calculateMortgageTenure(customerOutstandingLoan, maxInstalment);
            if (minTenure > 30) {
                suggestedAction = "Reject";
            }
            if (minTenure < customerLoanTenure) {
                minTenure = customerLoanTenure;
            }
        }
    }

    public void approveLoanRequest() throws IOException {
        loanApplicationSessionBeanLocal.approveRefinancingLoanRequest(ra.getLoanApplicationId(), periodSuggested, instalmentSuggested);
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(ec.getRequestContextPath() + "/web/internalSystem/loan/loanOfficerViewApplications.xhtml?faces-redirect=true");
    }

    public void rejectLoanRequest() throws IOException {
        loanApplicationSessionBeanLocal.rejectMortgageLoanRequest(ra.getLoanApplicationId());
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(ec.getRequestContextPath() + "/web/internalSystem/loan/loanOfficerViewApplications.xhtml?faces-redirect=true");
    }

    public void calculateInstalment() {
        instalmentSuggested = (0.035 / 12 * customerOutstandingLoan) / (1 - Math.pow((1 + 0.035 / 12), -periodSuggested * 12));
        DecimalFormat df = new DecimalFormat("0.00");
        instalmentSuggested = Double.valueOf(df.format(instalmentSuggested));
    }

    public LoanApplicationSessionBeanLocal getLoanApplicationSessionBeanLocal() {
        return loanApplicationSessionBeanLocal;
    }

    public void setLoanApplicationSessionBeanLocal(LoanApplicationSessionBeanLocal loanApplicationSessionBeanLocal) {
        this.loanApplicationSessionBeanLocal = loanApplicationSessionBeanLocal;
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public Long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }

    public RefinancingApplication getRa() {
        return ra;
    }

    public void setRa(RefinancingApplication ra) {
        this.ra = ra;
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

    public String getCustomerExistingFinancer() {
        return customerExistingFinancer;
    }

    public void setCustomerExistingFinancer(String customerExistingFinancer) {
        this.customerExistingFinancer = customerExistingFinancer;
    }

    public Double getCustomerOutstandingLoan() {
        return customerOutstandingLoan;
    }

    public void setCustomerOutstandingLoan(Double customerOutstandingLoan) {
        this.customerOutstandingLoan = customerOutstandingLoan;
    }

    public Integer getCustomerOutstandingYear() {
        return customerOutstandingYear;
    }

    public void setCustomerOutstandingYear(Integer customerOutstandingYear) {
        this.customerOutstandingYear = customerOutstandingYear;
    }

    public Integer getCustomerOutstandingMonth() {
        return customerOutstandingMonth;
    }

    public void setCustomerOutstandingMonth(Integer customerOutstandingMonth) {
        this.customerOutstandingMonth = customerOutstandingMonth;
    }

    public Double getCustomerTotalCPFWithdrawal() {
        return customerTotalCPFWithdrawal;
    }

    public void setCustomerTotalCPFWithdrawal(Double customerTotalCPFWithdrawal) {
        this.customerTotalCPFWithdrawal = customerTotalCPFWithdrawal;
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

    public boolean isHasJoint() {
        return hasJoint;
    }

    public void setHasJoint(boolean hasJoint) {
        this.hasJoint = hasJoint;
    }

    public boolean isNoJoint() {
        return noJoint;
    }

    public void setNoJoint(boolean noJoint) {
        this.noJoint = noJoint;
    }

    public CustomerBasic getJoint() {
        return joint;
    }

    public void setJoint(CustomerBasic joint) {
        this.joint = joint;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public CustomerAdvanced getJointCA() {
        return jointCA;
    }

    public void setJointCA(CustomerAdvanced jointCA) {
        this.jointCA = jointCA;
    }

    public List<CustomerDebt> getJointDebts() {
        return jointDebts;
    }

    public void setJointDebts(List<CustomerDebt> jointDebts) {
        this.jointDebts = jointDebts;
    }

    public String getJointSalutation() {
        return jointSalutation;
    }

    public void setJointSalutation(String jointSalutation) {
        this.jointSalutation = jointSalutation;
    }

    public String getJointName() {
        return jointName;
    }

    public void setJointName(String jointName) {
        this.jointName = jointName;
    }

    public String getJointDateOfBirth() {
        return jointDateOfBirth;
    }

    public void setJointDateOfBirth(String jointDateOfBirth) {
        this.jointDateOfBirth = jointDateOfBirth;
    }

    public String getJointGender() {
        return jointGender;
    }

    public void setJointGender(String jointGender) {
        this.jointGender = jointGender;
    }

    public String getJointNationality() {
        return jointNationality;
    }

    public void setJointNationality(String jointNationality) {
        this.jointNationality = jointNationality;
    }

    public String getJointIdentificationNum() {
        return jointIdentificationNum;
    }

    public void setJointIdentificationNum(String jointIdentificationNum) {
        this.jointIdentificationNum = jointIdentificationNum;
    }

    public String getJointCountryOfResidence() {
        return jointCountryOfResidence;
    }

    public void setJointCountryOfResidence(String jointCountryOfResidence) {
        this.jointCountryOfResidence = jointCountryOfResidence;
    }

    public String getJointRace() {
        return jointRace;
    }

    public void setJointRace(String jointRace) {
        this.jointRace = jointRace;
    }

    public String getJointMobile() {
        return jointMobile;
    }

    public void setJointMobile(String jointMobile) {
        this.jointMobile = jointMobile;
    }

    public String getJointEmail() {
        return jointEmail;
    }

    public void setJointEmail(String jointEmail) {
        this.jointEmail = jointEmail;
    }

    public String getJointEducation() {
        return jointEducation;
    }

    public void setJointEducation(String jointEducation) {
        this.jointEducation = jointEducation;
    }

    public String getJointMaritalStatus() {
        return jointMaritalStatus;
    }

    public void setJointMaritalStatus(String jointMaritalStatus) {
        this.jointMaritalStatus = jointMaritalStatus;
    }

    public Integer getJointNumOfDependents() {
        return jointNumOfDependents;
    }

    public void setJointNumOfDependents(Integer jointNumOfDependents) {
        this.jointNumOfDependents = jointNumOfDependents;
    }

    public String getJointAddress() {
        return jointAddress;
    }

    public void setJointAddress(String jointAddress) {
        this.jointAddress = jointAddress;
    }

    public String getJointPostal() {
        return jointPostal;
    }

    public void setJointPostal(String jointPostal) {
        this.jointPostal = jointPostal;
    }

    public String getJointResidentialStatus() {
        return jointResidentialStatus;
    }

    public void setJointResidentialStatus(String jointResidentialStatus) {
        this.jointResidentialStatus = jointResidentialStatus;
    }

    public String getJointResidentialType() {
        return jointResidentialType;
    }

    public void setJointResidentialType(String jointResidentialType) {
        this.jointResidentialType = jointResidentialType;
    }

    public Integer getJointLengthOfResidence() {
        return jointLengthOfResidence;
    }

    public void setJointLengthOfResidence(Integer jointLengthOfResidence) {
        this.jointLengthOfResidence = jointLengthOfResidence;
    }

    public String getJointEmploymentStatus() {
        return jointEmploymentStatus;
    }

    public void setJointEmploymentStatus(String jointEmploymentStatus) {
        this.jointEmploymentStatus = jointEmploymentStatus;
    }

    public String getJointOccupation() {
        return jointOccupation;
    }

    public void setJointOccupation(String jointOccupation) {
        this.jointOccupation = jointOccupation;
    }

    public String getJointCompanyName() {
        return jointCompanyName;
    }

    public void setJointCompanyName(String jointCompanyName) {
        this.jointCompanyName = jointCompanyName;
    }

    public String getJointCompanyAddress() {
        return jointCompanyAddress;
    }

    public void setJointCompanyAddress(String jointCompanyAddress) {
        this.jointCompanyAddress = jointCompanyAddress;
    }

    public String getJointCompanyPostal() {
        return jointCompanyPostal;
    }

    public void setJointCompanyPostal(String jointCompanyPostal) {
        this.jointCompanyPostal = jointCompanyPostal;
    }

    public String getJointIndustryType() {
        return jointIndustryType;
    }

    public void setJointIndustryType(String jointIndustryType) {
        this.jointIndustryType = jointIndustryType;
    }

    public String getJointCurrentPosition() {
        return jointCurrentPosition;
    }

    public void setJointCurrentPosition(String jointCurrentPosition) {
        this.jointCurrentPosition = jointCurrentPosition;
    }

    public String getJointCurrentJobTitle() {
        return jointCurrentJobTitle;
    }

    public void setJointCurrentJobTitle(String jointCurrentJobTitle) {
        this.jointCurrentJobTitle = jointCurrentJobTitle;
    }

    public Integer getJointLengthOfCurrentJob() {
        return jointLengthOfCurrentJob;
    }

    public void setJointLengthOfCurrentJob(Integer jointLengthOfCurrentJob) {
        this.jointLengthOfCurrentJob = jointLengthOfCurrentJob;
    }

    public String getJointPreviousCompany() {
        return jointPreviousCompany;
    }

    public void setJointPreviousCompany(String jointPreviousCompany) {
        this.jointPreviousCompany = jointPreviousCompany;
    }

    public Integer getJointLengthOfPreviousJob() {
        return jointLengthOfPreviousJob;
    }

    public void setJointLengthOfPreviousJob(Integer jointLengthOfPreviousJob) {
        this.jointLengthOfPreviousJob = jointLengthOfPreviousJob;
    }

    public Double getJointMonthlyFixedIncome() {
        return jointMonthlyFixedIncome;
    }

    public void setJointMonthlyFixedIncome(Double jointMonthlyFixedIncome) {
        this.jointMonthlyFixedIncome = jointMonthlyFixedIncome;
    }

    public Double getJointOtherMonthlyIncome() {
        return jointOtherMonthlyIncome;
    }

    public void setJointOtherMonthlyIncome(Double jointOtherMonthlyIncome) {
        this.jointOtherMonthlyIncome = jointOtherMonthlyIncome;
    }

    public String getJointOtherMonthlyIncomeSource() {
        return jointOtherMonthlyIncomeSource;
    }

    public void setJointOtherMonthlyIncomeSource(String jointOtherMonthlyIncomeSource) {
        this.jointOtherMonthlyIncomeSource = jointOtherMonthlyIncomeSource;
    }

    public CreditReportBureauScore getJointCR() {
        return jointCR;
    }

    public void setJointCR(CreditReportBureauScore jointCR) {
        this.jointCR = jointCR;
    }

    public List<CreditReportAccountStatus> getJointAccountStatus() {
        return jointAccountStatus;
    }

    public void setJointAccountStatus(List<CreditReportAccountStatus> jointAccountStatus) {
        this.jointAccountStatus = jointAccountStatus;
    }

    public List<CreditReportDefaultRecords> getJointDefaultRecords() {
        return jointDefaultRecords;
    }

    public void setJointDefaultRecords(List<CreditReportDefaultRecords> jointDefaultRecords) {
        this.jointDefaultRecords = jointDefaultRecords;
    }

    public Double getJointBureauScore() {
        return jointBureauScore;
    }

    public void setJointBureauScore(Double jointBureauScore) {
        this.jointBureauScore = jointBureauScore;
    }

    public String getJointRiskGrade() {
        return jointRiskGrade;
    }

    public void setJointRiskGrade(String jointRiskGrade) {
        this.jointRiskGrade = jointRiskGrade;
    }

    public Double getJointProbabilityOfDefault() {
        return jointProbabilityOfDefault;
    }

    public void setJointProbabilityOfDefault(Double jointProbabilityOfDefault) {
        this.jointProbabilityOfDefault = jointProbabilityOfDefault;
    }

    public int getCustomerAge() {
        return customerAge;
    }

    public void setCustomerAge(int customerAge) {
        this.customerAge = customerAge;
    }

    public int getJointAge() {
        return jointAge;
    }

    public void setJointAge(int jointAge) {
        this.jointAge = jointAge;
    }

    public double getRiskRatio() {
        return riskRatio;
    }

    public void setRiskRatio(double riskRatio) {
        this.riskRatio = riskRatio;
    }

    public int getAverageAge() {
        return averageAge;
    }

    public void setAverageAge(int averageAge) {
        this.averageAge = averageAge;
    }

    public double getMaxTDSRInstalment() {
        return maxTDSRInstalment;
    }

    public void setMaxTDSRInstalment(double maxTDSRInstalment) {
        this.maxTDSRInstalment = maxTDSRInstalment;
    }

    public double getMaxMSRInstalment() {
        return maxMSRInstalment;
    }

    public void setMaxMSRInstalment(double maxMSRInstalment) {
        this.maxMSRInstalment = maxMSRInstalment;
    }

    public double getTotalAmountOverdue() {
        return totalAmountOverdue;
    }

    public void setTotalAmountOverdue(double totalAmountOverdue) {
        this.totalAmountOverdue = totalAmountOverdue;
    }

    public double getJointAmountOverdue() {
        return jointAmountOverdue;
    }

    public void setJointAmountOverdue(double jointAmountOverdue) {
        this.jointAmountOverdue = jointAmountOverdue;
    }

    public String getSuggestedAction() {
        return suggestedAction;
    }

    public void setSuggestedAction(String suggestedAction) {
        this.suggestedAction = suggestedAction;
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

}
