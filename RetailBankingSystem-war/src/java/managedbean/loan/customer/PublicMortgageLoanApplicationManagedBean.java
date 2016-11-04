/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.loan.customer;

import ejb.customer.session.CRMCustomerSessionBean;
import ejb.infrastructure.session.CustomerEmailSessionBeanLocal;
import ejb.loan.entity.CustomerDebt;
import ejb.loan.entity.CustomerProperty;
import ejb.loan.entity.MortgageLoanApplication;
import ejb.loan.entity.RefinancingApplication;
import ejb.loan.session.LoanApplicationSessionBeanLocal;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.FlowEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author hanfengwei
 */
@Named(value = "publicMortgageLoanApplicationManagedBean")
@ViewScoped
public class PublicMortgageLoanApplicationManagedBean implements Serializable {

    @EJB
    private CustomerEmailSessionBeanLocal customerEmailSessionBeanLocal;

    @EJB
    private CRMCustomerSessionBean cRMCustomerSessionBeanLocal;

    @EJB
    private LoanApplicationSessionBeanLocal loanApplicationSessionBeanLocal;

    //basic information
    private String customerSalutation;
    private String customerSalutationOthers;
    private String customerName;
    private Date customerDateOfBirth;
    private String customerGender;
    private String customerNationality;
    private String customerIsPR;
    private String customerIdentificationNum;
    private String customerSingaporeNRIC;
    private String customerForeignNRIC;
    private String customerForeignPassport;
    private String customerCountryOfResidence;
    private String customerRace;
    private String customerMobile;
    private String customerEmail;

    //personal details
    private String customerEducation;
    private String customerMaritalStatus;
    private Integer customerNumOfDependents;
    private String customerStreetName;
    private String customerBlockNum;
    private String customerUnitNum;
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
    private String customerIndustryTypeOthers;
    private String customerCurrentPosition;
    private String customerCurrentPositionOthers;
    private String customerCurrentJobTitle;
    private Integer customerLengthOfCurrentJob;
    private String customerPreviousCompany;
    private Integer customerLengthOfPreviousJob;
    private BigDecimal customerMonthlyFixedIncome;
    private BigDecimal customerOtherMonthlyIncome;
    private String customerOtherMonthlyIncomeSource;

    //commitments
    private ArrayList<HashMap> customerFinancialCommitments = new ArrayList<HashMap>();
    private String customerFacilityType;
    private String customerFinancialInstitution;
    private BigDecimal customerLoanAmount;
    private BigDecimal customerMonthlyInstalment;
    private String customerCollateralDetails;
    private Integer customerRemainingTenure;
    private BigDecimal customerCurrentInterest;
    private HashMap customerFinancialCommitment = new HashMap();

    //property details
    private String customerPropertyStreetName;
    private String customerPropertyBlockNum;
    private String customerPropertyUnitNum;
    private String customerPropertyPostal;
    private ArrayList<String> customerPropertyOwners = new ArrayList<String>();
    private String customerPropertyOwner;
    private String customerPropertyType;
    private BigDecimal customerPropertyBuiltUpArea;
    private BigDecimal customerPropertyLandArea;
    private String customerPropertyStatus;
    private Date customerPropertyTOPDate;
    private String customerPropertyUsage;
    private String customerPropertyTenureType;
    private Integer customerPropertyTenureDuration;
    private Integer customerPropertyTenureFromYear;

    //loan - new purchase
    private BigDecimal customerPropertyPurchasePrice;
    private Date customerPropertyDateOfPurchase;
    private String customerPropertySource;
    private String customerPropertyWithOTP;
    private Date customerPropertyOTPDate;
    private String customerPropertyWithTenancy;
    private BigDecimal customerPropertyTenancyIncome;
    private Integer customerPropertyTenancyExpiryYear;
    private String customerWithBenefitsFromVendor;
    private BigDecimal customerBenefitsFromVendor;
    private BigDecimal customerCashDownpayment;
    private BigDecimal customerCPFDownpayment;
    //loan - refinancing
    private String customerExistingFinancer;
    private BigDecimal customerOutstandingLoan;
    private Integer customerOutstandingYear;
    private Integer customerOutstandingMonth;
    private BigDecimal customerTotalCPFWithdrawal;
    //both
    private String customerFinancialRequest;
    private BigDecimal customerLoanAmountRequired;
    private Integer customerLoanTenure;
    private String interestPackage;

    //confirmation
    private boolean agreement;
    private String customerSignature;

    //documents
    private UploadedFile file;
    private HashMap uploads = new HashMap();

    //basic information
    private boolean salutationPanelVisible;
    private boolean nationalitySGPanelVisible;
    private boolean nationalityOthersPanelVisible;
    private boolean nricPanelVisible;
    private boolean passportPanelVisible;

    //personal details
    private boolean industryTypePanelVisible;
    private boolean currentPositionPanelVisible;

    //employement
    private boolean occupationPanelVisible;
    private boolean employmentPanelVisible;

    //property details
    private boolean propertyTOPPanelVisible;
    private boolean propertyTenurePanelVisible;

    //loan
    private boolean propertyNewPurchasePanelVisible;
    private boolean propertyRefinancingPanelVisible;
    private boolean propertyOTPPanelVisible;
    private boolean propertyTenancyPanelVisible;
    private boolean propertyBenefitsPanelVisible;

    private ArrayList propertyTypes = new ArrayList();
    private String property;

    //joint
    private String hasJointApplicant;
    private boolean jointApplicantPanelVisible;
    private boolean noJointApplicantPanelVisible;

    //joint basic information
    private String jointRelationship;

    private String jointSalutation;
    private String jointSalutationOthers;
    private String jointName;
    private Date jointDateOfBirth;
    private String jointGender;
    private String jointNationality;
    private String jointIsPR;
    private String jointIdentificationNum;
    private String jointSingaporeNRIC;
    private String jointForeignNRIC;
    private String jointForeignPassport;
    private String jointCountryOfResidence;
    private String jointRace;
    private String jointMobile;
    private String jointEmail;

    //joint personal details
    private String jointEducation;
    private String jointMaritalStatus;
    private Integer jointNumOfDependents;
    private String jointStreetName;
    private String jointBlockNum;
    private String jointUnitNum;
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
    private String jointIndustryTypeOthers;
    private String jointCurrentPosition;
    private String jointCurrentPositionOthers;
    private String jointCurrentJobTitle;
    private Integer jointLengthOfCurrentJob;
    private String jointPreviousCompany;
    private Integer jointLengthOfPreviousJob;
    private BigDecimal jointMonthlyFixedIncome;
    private BigDecimal jointOtherMonthlyIncome;
    private String jointOtherMonthlyIncomeSource;

    //commitments
    private ArrayList<HashMap> jointFinancialCommitments = new ArrayList<HashMap>();
    private String jointFacilityType;
    private String jointFinancialInstitution;
    private BigDecimal jointLoanAmount;
    private BigDecimal jointMonthlyInstalment;
    private String jointCollateralDetails;
    private Integer jointRemainingTenure;
    private BigDecimal jointCurrentInterest;
    private HashMap jointFinancialCommitment = new HashMap();

    //confirmation
    private String jointSignature;

    //basic information
    private boolean jointSalutationPanelVisible;
    private boolean jointNationalitySGPanelVisible;
    private boolean jointNationalityOthersPanelVisible;
    private boolean jointNricPanelVisible;
    private boolean jointPassportPanelVisible;

    //personal details
    private boolean jointIndustryTypePanelVisible;
    private boolean jointCurrentPositionPanelVisible;

    //employement
    private boolean jointOccupationPanelVisible;
    private boolean jointEmploymentPanelVisible;

    /**
     * Creates a new instance of PublicHDBLoanApplication
     */
    public PublicMortgageLoanApplicationManagedBean() {
        uploads.put("identification", false);
        uploads.put("jointIdentification", false);
        uploads.put("otp", false);
        uploads.put("purchaseAgreement", false);
        uploads.put("existingLoan", false);
        uploads.put("cpfWithdrawal", false);
        uploads.put("evidenceOfSale", false);
        uploads.put("tenancy", false);
        uploads.put("employeeTax", false);
        uploads.put("employeeCPF", false);
        uploads.put("selfEmployedTax", false);
        uploads.put("jointSelfEmployedTax", false);
        uploads.put("jointEmployeeTax", false);
    }

    @PostConstruct
    public void init() {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        property = (String) ec.getFlash().get("loanType");
        if (property.equals("HDB")) {
            propertyTypes.add("3-Room");
            propertyTypes.add("4-Room");
            propertyTypes.add("5-Room");
            propertyTypes.add("Executive Apartment/Mansionette");
            propertyTypes.add("HUDC");
        } else if (property.equals("Private Property")) {
            propertyTypes.add("Bungalow");
            propertyTypes.add("Semi-D");
            propertyTypes.add("Intermediate/Comer Terrace");
            propertyTypes.add("Executive Condominium");
            propertyTypes.add("Apartment");
            propertyTypes.add("HUDC");
            propertyTypes.add("SOHO");
        }
    }

    public void addLoanApplicationFast() throws IOException {

        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        customerPropertyOwners.add("Han Fengwei");
        customerPropertyOwners.add("Dai Hailang");

        //add HDB - New Purchase
        Long newCustomerBasicId = cRMCustomerSessionBeanLocal.updateCustomerBasic("G11223344",
                "hanfengwei96@gmail.com", "83114121", "China", "China", "Single",
                "Student", "AfterYou", "customer address", "118425");

        Long newCustomerAdvancedId = cRMCustomerSessionBeanLocal.addNewCustomerAdvanced(3, "Degree", "Rented",
                3, "Government", 5, "Employee",
                10000, "HDB", "company address",
                "118426", "Senior Management", "CEO",
                "NTU", 3, 2000,
                "other income source");

        ec.getFlash().put("loanType", "HDB - New Purchase");
        ec.getFlash().put("amountRequired", BigDecimal.valueOf(500000));
        ec.getFlash().put("tenure", 20);

        ArrayList<CustomerDebt> debts = new ArrayList<CustomerDebt>();
        debts.add(loanApplicationSessionBeanLocal.addNewCustomerDebt("car loan", "DBS", 500000, 1000, "collateral", 3, 5.6));
        debts.add(loanApplicationSessionBeanLocal.addNewCustomerDebt("HDB loan", "UOB", 800000, 2000, "collateral", 17, 2.8));

        Long newJointBasicId;
        Long newJointAdvancedId;
        boolean jointIsExistingCustomer = cRMCustomerSessionBeanLocal.checkExistingCustomerByIdentification("F11223344");
        if (jointIsExistingCustomer) {
            newJointBasicId = cRMCustomerSessionBeanLocal.updateCustomerBasic("F11223344",
                "hanfengwei96@gmail.com", "83114121", "China", "China", "Married",
                "Accountant", "AfterYou", "customer address", "118425");
        } else {
            newJointBasicId = cRMCustomerSessionBeanLocal.addNewCustomerBasic("Han Lei",
                    "Mr", "F11223344",
                    "Male", "hanfengwei96@gmail.com", "83114121", "04/Jun/1968",
                    "China", "China", "Chinese",
                    "Married", "Teacher", "NUS",
                    "address", "123123", null, null, null, null);
        }
        
        boolean jointHasCustomerAdvanced = cRMCustomerSessionBeanLocal.checkExistingCustomerAdvanced("F11223344");
        if(jointHasCustomerAdvanced){
            newJointAdvancedId = cRMCustomerSessionBeanLocal.updateCustomerAdvanced("F11223344", 2,
                    "Degree", "Rented", 5, "Government", 6,
                    "Employee", 3000, "HDB", "company address",
                    "123123", "Senior Management", "job title", null,
                    0, 2000, "income source");
        }else{
            newJointAdvancedId = cRMCustomerSessionBeanLocal.addNewCustomerAdvanced(3, "Degree", "Rented",
                3, "Government", 5, "Employee",
                10000, "HDB", "company address",
                "118426", "Senior Management", "CEO",
                "NTU", 3, 2000,
                "other income source");
        }
        
        
        CustomerProperty cp = new CustomerProperty();
        cp.create("property address", "118427", customerPropertyOwners, "3-Room",
                170.8, 200, "Completed",
                null, "Owner Occupation", "Leasehold",
                99, 2012, null);
        MortgageLoanApplication mortgage = new MortgageLoanApplication();
        mortgage.create("HDB - New Purchase", 500000, 20, 550000,
                new Date(), "Developer/HDB", "no", null, "yes",
                5000, 2012, "yes", 30000,
                20000, 30000, "Employee", true, "Father", "Employee");
        loanApplicationSessionBeanLocal.submitLoanApplication(true, false, newCustomerBasicId, newCustomerAdvancedId, debts,
                true, jointIsExistingCustomer, jointHasCustomerAdvanced, newJointBasicId, newJointAdvancedId, debts,
                cp, mortgage, null, "purchase", "HDB-Fixed");

        //add HDB - refinancing
        Long customerId2 = cRMCustomerSessionBeanLocal.addNewCustomerBasic("Dai Hailang",
                "Mr", "G22334455",
                "Male", "hanfengwei96@gmail.com", "83114121", "08/Mar/1993",
                "China", "China", "Chinese",
                "Single", "Student", "AfterYou",
                "customer address", "118425", null, null, null, null);

        Long customerAdvancedId2 = cRMCustomerSessionBeanLocal.addNewCustomerAdvanced(3, "Degree", "Rented",
                3, "CEO", 5, "Self-Employed",
                10000, "HDB", "company address",
                "118426", "Senior Management", "CEO",
                "Monsta BBQ", 3, 2000,
                "other income source");

        ArrayList<CustomerDebt> debts2 = new ArrayList<CustomerDebt>();
        debts2.add(loanApplicationSessionBeanLocal.addNewCustomerDebt("car loan", "DBS", 500000, 1000, "collateral", 3, 5.6));
        debts2.add(loanApplicationSessionBeanLocal.addNewCustomerDebt("HDB loan", "UOB", 800000, 2000, "collateral", 17, 2.8));

        CustomerProperty cp2 = new CustomerProperty();
        cp2.create("property address", "118427", customerPropertyOwners, "3-Room",
                170.8, 200, "Completed",
                null, "Owner Occupation", "Leasehold",
                99, 2012, null);
        RefinancingApplication refinancing2 = new RefinancingApplication();
        refinancing2.create("HDB - Refinancing", 450000, 17, "POSB",
                450000, 15, 3, 50000, "Self-Employed", false, null, null);
        loanApplicationSessionBeanLocal.submitLoanApplication(false, false, customerId2, customerAdvancedId2, debts2,
                false, false, false, null, null, null,
                cp2, null, refinancing2, "refinancing", "HDB-Floating");

        //add Private Property - Refinancing
        Long customerId3 = cRMCustomerSessionBeanLocal.addNewCustomerBasic("Guo Ziyu",
                "Mr", "G33445566",
                "Male", "hanfengwei96@gmail.com", "83114121", "08/Mar/1993",
                "China", "China", "Chinese",
                "Single", "Student", "AfterYou",
                "customer address", "118425", null, null, null, null);

        Long customerAdvancedId3 = cRMCustomerSessionBeanLocal.addNewCustomerAdvanced(3, "Degree", "Rented",
                3, "CEO", 5, "Self-Employed",
                10000, "HDB", "company address",
                "118426", "Senior Management", "CEO",
                "Monsta BBQ", 3, 2000,
                "other income source");

        ArrayList<CustomerDebt> debts3 = new ArrayList<CustomerDebt>();
        debts3.add(loanApplicationSessionBeanLocal.addNewCustomerDebt("car loan", "DBS", 500000, 1000, "collateral", 3, 5.6));
        debts3.add(loanApplicationSessionBeanLocal.addNewCustomerDebt("HDB loan", "UOB", 800000, 2000, "collateral", 17, 2.8));

        CustomerProperty cp3 = new CustomerProperty();
        cp3.create("property address", "118427", customerPropertyOwners, "Bungalow",
                170.8, 200, "Completed",
                null, "Owner Occupation", "Leasehold",
                99, 2012, null);
        RefinancingApplication refinancing3 = new RefinancingApplication();
        refinancing3.create("Private Property - Refinancing", 450000, 17, "POSB",
                450000, 15, 3, 50000, "Self-Employed", false, null, null);
        loanApplicationSessionBeanLocal.submitLoanApplication(false, false, customerId3, customerAdvancedId3, debts3,
                false, false, false, null, null, null,
                cp3, null, refinancing3, "refinancing", "Private Property-Floating");

        //add Private Property - New Purchase
        Long customerId4 = cRMCustomerSessionBeanLocal.addNewCustomerBasic("Yang Shuanghe",
                "Ms", "G44556677",
                "Male", "hanfengwei96@gmail.com", "83114121", "08/Mar/1993",
                "China", "China", "Chinese",
                "Single", "Student", "AfterYou",
                "customer address", "118425", null, null, null, null);

        Long customerAdvancedId4 = cRMCustomerSessionBeanLocal.addNewCustomerAdvanced(3, "Degree", "Rented",
                3, "Government", 5, "Employee",
                10000, "HDB", "company address",
                "118426", "Senior Management", "CEO",
                "NTU", 3, 2000,
                "other income source");

        ArrayList<CustomerDebt> debts4 = new ArrayList<CustomerDebt>();
        debts4.add(loanApplicationSessionBeanLocal.addNewCustomerDebt("car loan", "DBS", 500000, 1000, "collateral", 3, 5.6));
        debts4.add(loanApplicationSessionBeanLocal.addNewCustomerDebt("HDB loan", "UOB", 800000, 2000, "collateral", 17, 2.8));

        CustomerProperty cp4 = new CustomerProperty();
        cp4.create("property address", "118427", customerPropertyOwners, "Bungalow",
                170.8, 200, "Completed",
                null, "Owner Occupation", "Leasehold",
                99, 2012, null);
        MortgageLoanApplication mortgage4 = new MortgageLoanApplication();
        mortgage4.create("Private Property - New Purchase", 500000, 20, 550000,
                new Date(), "Developer/HDB", "no", null, "yes",
                5000, 2012, "yes", 30000,
                20000, 30000, "Employee", false, null, null);
        loanApplicationSessionBeanLocal.submitLoanApplication(false, false, customerId4, customerAdvancedId4, debts4,
                false, false, false, null, null,null,
                cp4, mortgage4, null, "purchase", "Private Property-Fixed");

        ec.redirect(ec.getRequestContextPath() + "/web/merlionBank/loan/publicLoanApplicationDone.xhtml?faces-redirect=true");
    }

    public void addLoanApplication() throws IOException {
        System.out.println("====== loan/PublicHDBLoanApplicationManagedBean: addLoanApplication() ======");
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        customerSignature = ec.getSessionMap().get("customerSignature").toString();
        jointSignature = ec.getSessionMap().get("jointSignature").toString();
        if (customerSignature.equals("") || !agreement) {
            if (customerSignature.equals("")) {
                FacesContext.getCurrentInstance().addMessage("input", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed! Please provide your digital signature", "Failed!"));
            }
            if (!agreement) {
                FacesContext.getCurrentInstance().addMessage("agreement", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed! Please agree to terms to proceed", "Failed!"));
            }
        } else {
            //create or update CustomerBasic
            if (customerSalutation.equals("Others")) {
                customerSalutation = customerSalutationOthers;
            }

            String dateOfBirth = changeDateFormat(customerDateOfBirth);
            String customerAddress = customerStreetName + ", " + customerBlockNum + ", " + customerUnitNum + ", " + customerPostal;

            Long newCustomerBasicId;

            boolean isExistingCustomer = cRMCustomerSessionBeanLocal.checkExistingCustomerByIdentification(customerIdentificationNum);
            if (isExistingCustomer) {
                newCustomerBasicId = cRMCustomerSessionBeanLocal.updateCustomerBasic(customerIdentificationNum, customerEmail, customerMobile,
                        customerNationality, customerCountryOfResidence, customerMaritalStatus, customerOccupation, customerCompanyName,
                        customerName, customerPostal);
            } else {
                newCustomerBasicId = cRMCustomerSessionBeanLocal.addNewCustomerBasic(customerName,
                        customerSalutation, customerIdentificationNum.toUpperCase(),
                        customerGender, customerEmail, customerMobile, dateOfBirth,
                        customerNationality, customerCountryOfResidence, customerRace,
                        customerMaritalStatus, customerOccupation, customerCompanyName,
                        customerAddress, customerPostal, null, null, customerSignature.getBytes(), null);
            }

            //create CustomerAdvanced
            if (customerEmploymentStatus.equals("Employee") || customerEmploymentStatus.equals("Self-Employed")) {
                if (customerIndustryType.equals("Others")) {
                    customerIndustryType = customerIndustryTypeOthers;
                }
                if (customerCurrentPosition.equals("Others")) {
                    customerCurrentPosition = customerCurrentPositionOthers;
                }
            }

            Long newCustomerAdvancedId;
            boolean hasCustomerAdvanced = cRMCustomerSessionBeanLocal.checkExistingCustomerAdvanced(customerIdentificationNum);
            if (customerEmploymentStatus.equals("Unemployed")) {
                if (hasCustomerAdvanced) {
                    newCustomerAdvancedId = cRMCustomerSessionBeanLocal.updateCustomerAdvanced(customerIdentificationNum, customerNumOfDependents,
                            customerEducation, customerResidentialStatus, customerLengthOfResidence, null, 0,
                            customerEmploymentStatus, customerMonthlyFixedIncome.doubleValue(), customerResidentialType, null,
                            null, null, null, null, 0, customerOtherMonthlyIncome.doubleValue(), customerOtherMonthlyIncomeSource);
                } else {
                    newCustomerAdvancedId = cRMCustomerSessionBeanLocal.addNewCustomerAdvanced(customerNumOfDependents, customerEducation, customerResidentialStatus,
                            customerLengthOfResidence, null, 0, customerEmploymentStatus,
                            customerMonthlyFixedIncome.doubleValue(), customerResidentialType, null,
                            null, null, null,
                            null, 0, customerOtherMonthlyIncome.doubleValue(),
                            customerOtherMonthlyIncomeSource);
                }
            } else {
                if (hasCustomerAdvanced) {
                    newCustomerAdvancedId = cRMCustomerSessionBeanLocal.updateCustomerAdvanced(customerIdentificationNum, customerNumOfDependents,
                            customerEducation, customerResidentialStatus, customerLengthOfResidence, customerIndustryType, customerLengthOfCurrentJob,
                            customerEmploymentStatus, customerMonthlyFixedIncome.doubleValue(), customerResidentialType, customerCompanyAddress,
                            customerCompanyPostal, customerCurrentPosition, customerCurrentJobTitle, customerPreviousCompany,
                            customerLengthOfPreviousJob, customerOtherMonthlyIncome.doubleValue(), customerOtherMonthlyIncomeSource);
                } else {
                    newCustomerAdvancedId = cRMCustomerSessionBeanLocal.addNewCustomerAdvanced(customerNumOfDependents, customerEducation, customerResidentialStatus,
                            customerLengthOfResidence, customerIndustryType, customerLengthOfCurrentJob, customerEmploymentStatus,
                            customerMonthlyFixedIncome.doubleValue(), customerResidentialType, customerCompanyAddress,
                            customerCompanyPostal, customerCurrentPosition, customerCurrentJobTitle,
                            customerPreviousCompany, customerLengthOfPreviousJob, customerOtherMonthlyIncome.doubleValue(),
                            customerOtherMonthlyIncomeSource);
                }
            }

            //create customerDebt
            ArrayList<CustomerDebt> debts = new ArrayList<CustomerDebt>();
            for (HashMap debt : customerFinancialCommitments) {
                String facilityType = (String) debt.get("type");
                String financialInstitution = (String) debt.get("institution");
                BigDecimal total = (BigDecimal) debt.get("amount");
                double totalAmount = total.doubleValue();
                BigDecimal monthlyInstalment = (BigDecimal) debt.get("instalment");
                double instalment = monthlyInstalment.doubleValue();
                String collateral = (String) debt.get("collateral");
                int tenure = (Integer) debt.get("tenure");
                BigDecimal rate = (BigDecimal) debt.get("interest");
                debts.add(loanApplicationSessionBeanLocal.addNewCustomerDebt(facilityType, financialInstitution,
                        totalAmount, instalment, collateral, tenure, rate.doubleValue()));
            }

            boolean jointIsExistingCustomer = false;
            boolean jointHasCustomerAdvanced = false;
            Long newJointBasicId = null;
            Long newJointAdvancedId = null;
            ArrayList<CustomerDebt> jointDebts = new ArrayList<CustomerDebt>();

            if (hasJointApplicant.equals("Yes")) {
                //create or update CustomerBasic
                if (jointSalutation.equals("Others")) {
                    jointSalutation = jointSalutationOthers;
                }

                dateOfBirth = changeDateFormat(jointDateOfBirth);
                String jointAddress = jointStreetName + ", " + jointBlockNum + ", " + jointUnitNum + ", " + jointPostal;

                jointIsExistingCustomer = cRMCustomerSessionBeanLocal.checkExistingCustomerByIdentification(jointIdentificationNum);
                if (jointIsExistingCustomer) {
                    newJointBasicId = cRMCustomerSessionBeanLocal.updateCustomerBasic(jointIdentificationNum, jointEmail, jointMobile,
                            jointNationality, jointCountryOfResidence, jointMaritalStatus, jointOccupation, jointCompanyName,
                            jointName, jointPostal);
                } else {
                    newJointBasicId = cRMCustomerSessionBeanLocal.addNewCustomerBasic(jointName,
                            jointSalutation, jointIdentificationNum.toUpperCase(),
                            jointGender, jointEmail, jointMobile, dateOfBirth,
                            jointNationality, jointCountryOfResidence, jointRace,
                            jointMaritalStatus, jointOccupation, jointCompanyName,
                            jointAddress, jointPostal, null, null, jointSignature.getBytes(), null);
                }

                //create CustomerAdvanced
                if (jointEmploymentStatus.equals("Employee") || jointEmploymentStatus.equals("Self-Employed")) {
                    if (jointIndustryType.equals("Others")) {
                        jointIndustryType = jointIndustryTypeOthers;
                    }
                    if (jointCurrentPosition.equals("Others")) {
                        jointCurrentPosition = jointCurrentPositionOthers;
                    }
                }

                jointHasCustomerAdvanced = cRMCustomerSessionBeanLocal.checkExistingCustomerAdvanced(jointIdentificationNum);
                if (jointEmploymentStatus.equals("Unemployed")) {
                    if (jointHasCustomerAdvanced) {
                        newJointAdvancedId = cRMCustomerSessionBeanLocal.updateCustomerAdvanced(jointIdentificationNum, jointNumOfDependents,
                                jointEducation, jointResidentialStatus, jointLengthOfResidence, null, 0,
                                jointEmploymentStatus, jointMonthlyFixedIncome.doubleValue(), jointResidentialType, null,
                                null, null, null, null, 0, jointOtherMonthlyIncome.doubleValue(), jointOtherMonthlyIncomeSource);
                    } else {
                        newJointAdvancedId = cRMCustomerSessionBeanLocal.addNewCustomerAdvanced(jointNumOfDependents, jointEducation, jointResidentialStatus,
                                jointLengthOfResidence, null, 0, jointEmploymentStatus,
                                jointMonthlyFixedIncome.doubleValue(), jointResidentialType, null,
                                null, null, null,
                                null, 0, jointOtherMonthlyIncome.doubleValue(),
                                jointOtherMonthlyIncomeSource);
                    }
                } else {
                    if (jointHasCustomerAdvanced) {
                        newJointAdvancedId = cRMCustomerSessionBeanLocal.updateCustomerAdvanced(jointIdentificationNum, jointNumOfDependents,
                                jointEducation, jointResidentialStatus, jointLengthOfResidence, jointIndustryType, jointLengthOfCurrentJob,
                                jointEmploymentStatus, jointMonthlyFixedIncome.doubleValue(), jointResidentialType, jointCompanyAddress,
                                jointCompanyPostal, jointCurrentPosition, jointCurrentJobTitle, jointPreviousCompany,
                                jointLengthOfPreviousJob, jointOtherMonthlyIncome.doubleValue(), jointOtherMonthlyIncomeSource);
                    } else {
                        newJointAdvancedId = cRMCustomerSessionBeanLocal.addNewCustomerAdvanced(jointNumOfDependents, jointEducation, jointResidentialStatus,
                                jointLengthOfResidence, jointIndustryType, jointLengthOfCurrentJob, jointEmploymentStatus,
                                jointMonthlyFixedIncome.doubleValue(), jointResidentialType, jointCompanyAddress,
                                jointCompanyPostal, jointCurrentPosition, jointCurrentJobTitle,
                                jointPreviousCompany, jointLengthOfPreviousJob, jointOtherMonthlyIncome.doubleValue(),
                                jointOtherMonthlyIncomeSource);
                    }
                }

                //create customerDebt
                for (HashMap debt : jointFinancialCommitments) {
                    String facilityType = (String) debt.get("type");
                    String financialInstitution = (String) debt.get("institution");
                    BigDecimal total = (BigDecimal) debt.get("amount");
                    double totalAmount = total.doubleValue();
                    BigDecimal monthlyInstalment = (BigDecimal) debt.get("instalment");
                    double instalment = monthlyInstalment.doubleValue();
                    String collateral = (String) debt.get("collateral");
                    int tenure = (Integer) debt.get("tenure");
                    BigDecimal rate = (BigDecimal) debt.get("interest");
                    jointDebts.add(loanApplicationSessionBeanLocal.addNewCustomerDebt(facilityType, financialInstitution,
                            totalAmount, instalment, collateral, tenure, rate.doubleValue()));
                }
            }

            //create customerProperty
            if (customerPropertyStatus.equals("Completed")) {
                customerPropertyTOPDate = null;
            }
            if (customerPropertyTenureType.equals("Freehold")) {
                customerPropertyTenureDuration = 0;
                customerPropertyTenureFromYear = 0;
            }
            String customerPropertyAddress = customerPropertyStreetName + ", " + customerPropertyBlockNum + ", " + customerPropertyUnitNum + ", " + customerPropertyPostal;
            CustomerProperty cp = new CustomerProperty();
            cp.create(customerPropertyAddress, customerPropertyPostal, customerPropertyOwners, customerPropertyType,
                    customerPropertyBuiltUpArea.doubleValue(), customerPropertyLandArea.doubleValue(), customerPropertyStatus,
                    customerPropertyTOPDate, customerPropertyUsage, customerPropertyTenureType,
                    customerPropertyTenureDuration, customerPropertyTenureFromYear, null);

            //create loan application
            if (customerFinancialRequest.equals("purchase")) {
                if (customerPropertyWithOTP.equals("no")) {
                    customerPropertyOTPDate = null;
                }
                if (customerPropertyWithTenancy.equals("no")) {
                    customerPropertyTenancyIncome = BigDecimal.valueOf(0);
                    customerPropertyTenancyExpiryYear = 0;
                }
                if (customerWithBenefitsFromVendor.equals("no")) {
                    customerBenefitsFromVendor = BigDecimal.valueOf(0);
                }
                MortgageLoanApplication mortgage = new MortgageLoanApplication();

                if (property.equals("HDB")) {
                    mortgage.create("HDB - New Purchase", customerLoanAmountRequired.doubleValue(), customerLoanTenure, customerPropertyPurchasePrice.doubleValue(),
                            customerPropertyDateOfPurchase, customerPropertySource, customerPropertyWithOTP, customerPropertyOTPDate, customerPropertyWithTenancy,
                            customerPropertyTenancyIncome.doubleValue(), customerPropertyTenancyExpiryYear, customerWithBenefitsFromVendor, customerBenefitsFromVendor.doubleValue(),
                            customerCashDownpayment.doubleValue(), customerCPFDownpayment.doubleValue(), customerEmploymentStatus, hasJointApplicant.equals("Yes"), jointRelationship, jointEmploymentStatus);
                    ec.getFlash().put("loanType", "HDB - New Purchase");
                } else {
                    mortgage.create("Private Property - New Purchase", customerLoanAmountRequired.doubleValue(), customerLoanTenure, customerPropertyPurchasePrice.doubleValue(),
                            customerPropertyDateOfPurchase, customerPropertySource, customerPropertyWithOTP, customerPropertyOTPDate, customerPropertyWithTenancy,
                            customerPropertyTenancyIncome.doubleValue(), customerPropertyTenancyExpiryYear, customerWithBenefitsFromVendor, customerBenefitsFromVendor.doubleValue(),
                            customerCashDownpayment.doubleValue(), customerCPFDownpayment.doubleValue(), customerEmploymentStatus, hasJointApplicant.equals("Yes"), jointRelationship, jointEmploymentStatus);
                    ec.getFlash().put("loanType", "Private Property - New Purchase");
                }

                loanApplicationSessionBeanLocal.submitLoanApplication(isExistingCustomer, hasCustomerAdvanced, newCustomerBasicId, newCustomerAdvancedId, debts,
                        hasJointApplicant.equals("Yes"), jointIsExistingCustomer, jointHasCustomerAdvanced, newJointBasicId, newJointAdvancedId, jointDebts,
                        cp, mortgage, null, customerFinancialRequest, interestPackage);

            } else {
                RefinancingApplication refinancing = new RefinancingApplication();
                if (property.equals("HDB")) {
                    refinancing.create("HDB - Refinancing", customerOutstandingLoan.doubleValue(), customerLoanTenure, customerExistingFinancer,
                            customerOutstandingLoan.doubleValue(), customerOutstandingYear, customerOutstandingMonth, customerTotalCPFWithdrawal.doubleValue(), customerEmploymentStatus,
                            hasJointApplicant.equals("Yes"), jointRelationship, jointEmploymentStatus);
                    ec.getFlash().put("loanType", "HDB - Refinancing");
                } else {
                    refinancing.create("Private Property - Refinancing", customerOutstandingLoan.doubleValue(), customerLoanTenure, customerExistingFinancer,
                            customerOutstandingLoan.doubleValue(), customerOutstandingYear, customerOutstandingMonth, customerTotalCPFWithdrawal.doubleValue(), customerEmploymentStatus,
                            hasJointApplicant.equals("Yes"), jointRelationship, jointEmploymentStatus);
                    ec.getFlash().put("loanType", "Private Property - Refinancing");
                }

                loanApplicationSessionBeanLocal.submitLoanApplication(isExistingCustomer, hasCustomerAdvanced, newCustomerBasicId, newCustomerAdvancedId, debts,
                        hasJointApplicant.equals("Yes"), jointIsExistingCustomer, jointHasCustomerAdvanced, newJointBasicId, newJointAdvancedId, jointDebts,
                        cp, null, refinancing, customerFinancialRequest, interestPackage);
            }

            HashMap emailActions = new HashMap();
            emailActions.put("loanType", "HDB");
            emailActions.put("request", customerFinancialRequest);
            customerEmailSessionBeanLocal.sendEmail(cRMCustomerSessionBeanLocal.getCustomerBasicById(newCustomerBasicId), "mortgageLoanApplication", emailActions);
            ec.getFlash().put("amountRequired", customerLoanAmountRequired);
            ec.getFlash().put("tenure", customerLoanTenure);
            ec.redirect(ec.getRequestContextPath() + "/web/merlionBank/loan/publicLoanApplicationDone.xhtml?faces-redirect=true");
        }
    }

    public String onFlowProcess(FlowEvent event) {
        String nextStep = event.getNewStep();

        if (event.getOldStep().equals("basic")) {
            Date today = new Date();
            int age = today.getYear() - customerDateOfBirth.getYear();
            if (today.getMonth() < customerDateOfBirth.getMonth()) {
                age--;
            } else if (today.getMonth() == customerDateOfBirth.getMonth()) {
                if (today.getDate() <= customerDateOfBirth.getDate()) {
                    age--;
                }
            }

            if (customerNationality.equals("Singapore")) {
                customerIdentificationNum = customerSingaporeNRIC;
            } else if (customerIsPR.equals("Yes")) {
                customerIdentificationNum = customerForeignNRIC;
            } else {
                customerIdentificationNum = customerForeignPassport;
            }

            if (age < 21 || age > 65) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Your age is not qualified to apply for this type of loan", "");
                FacesContext.getCurrentInstance().addMessage(null, message);
                nextStep = event.getOldStep();
            } else if (customerIdentificationNum.length() != 9) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please enter a valid indentification number", "");
                FacesContext.getCurrentInstance().addMessage(null, message);
                nextStep = event.getOldStep();
            }
        } else if (event.getOldStep().equals("employment")) {
            double grossIncome = customerOtherMonthlyIncome.doubleValue() + customerMonthlyFixedIncome.doubleValue();
            if (grossIncome <= 6000) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "The minimum gross monthly income required is S$6,000", "");
                FacesContext.getCurrentInstance().addMessage(null, message);
                nextStep = event.getOldStep();
            }
        } else if (event.getOldStep().equals("property")) {
            if (customerPropertyOwners.isEmpty()) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please enter all the names of owners", "");
                FacesContext.getCurrentInstance().addMessage(null, message);
                nextStep = event.getOldStep();
            }
        } else if (event.getOldStep().equals("documents")) {
            String pendingDocs = "Please submit following documents: ";
            boolean allSubmitted = true;

            for (Object entry : uploads.keySet()) {
                String map = (String) entry;
                boolean uploaded = (boolean) uploads.get(map);
                if (!uploaded) {
                    pendingDocs += map + ", ";
                    allSubmitted = false;
                }
            }
            pendingDocs = pendingDocs.substring(0, pendingDocs.length() - 2);

            if (!allSubmitted) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, pendingDocs, "");
                FacesContext.getCurrentInstance().addMessage(null, message);
                nextStep = event.getOldStep();
            }
        }
        return nextStep;
    }

    public String onFlowProcessJoint(FlowEvent event) {
        String nextStep = event.getNewStep();

        if (event.getOldStep().equals("jointBasic")) {
            Date today = new Date();
            int age = today.getYear() - jointDateOfBirth.getYear();
            if (today.getMonth() < jointDateOfBirth.getMonth()) {
                age--;
            } else if (today.getMonth() == jointDateOfBirth.getMonth()) {
                if (today.getDate() <= jointDateOfBirth.getDate()) {
                    age--;
                }
            }

            if (jointNationality.equals("Singapore")) {
                jointIdentificationNum = jointSingaporeNRIC;
            } else if (jointIsPR.equals("Yes")) {
                jointIdentificationNum = jointForeignNRIC;
            } else {
                jointIdentificationNum = jointForeignPassport;
            }

            if (age < 21 || age > 65) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Your age is not qualified to apply for this type of loan", "");
                FacesContext.getCurrentInstance().addMessage(null, message);
                nextStep = event.getOldStep();
            } else if (jointIdentificationNum.length() != 9) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please enter a valid indentification number", "");
                FacesContext.getCurrentInstance().addMessage(null, message);
                nextStep = event.getOldStep();
            }
        } else if (event.getOldStep().equals("jointEmployment")) {
            double grossIncome = jointOtherMonthlyIncome.doubleValue() + jointMonthlyFixedIncome.doubleValue();
            if (grossIncome <= 6000) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "The minimum gross monthly income required is S$6,000", "");
                FacesContext.getCurrentInstance().addMessage(null, message);
                nextStep = event.getOldStep();
            }
        }

        return nextStep;
    }

    public void addCustomerPropertyOwner() {
        System.out.println("====== loan/PublicHDBLoanApplicationManagedBean: addCustomerPropertyOwner() ======");
        if (!customerPropertyOwner.equals("")) {
            customerPropertyOwners.add(customerPropertyOwner);
            customerPropertyOwner = "";
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Required Field", "");
            FacesContext.getCurrentInstance().addMessage("customerPropertyOwner", message);
        }
    }

    public void addCustomerFinancialCommitment() {
        System.out.println("====== loan/PublicHDBLoanApplicationManagedBean: addCustomerFinancialCommitment() ======");
        if (customerFinancialInstitution == null || customerFacilityType == null || customerLoanAmount == null || customerMonthlyInstalment == null
                || customerCollateralDetails == null || customerRemainingTenure == null || customerCurrentInterest == null) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please fill in all the fields", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        } else {
            customerFinancialCommitment.put("institution", customerFinancialInstitution);
            customerFinancialCommitment.put("type", customerFacilityType);
            customerFinancialCommitment.put("amount", customerLoanAmount);
            customerFinancialCommitment.put("instalment", customerMonthlyInstalment);
            customerFinancialCommitment.put("collateral", customerCollateralDetails);
            customerFinancialCommitment.put("tenure", customerRemainingTenure);
            customerFinancialCommitment.put("interest", customerCurrentInterest);
            customerFinancialCommitments.add(customerFinancialCommitment);
            customerFinancialInstitution = null;
            customerFacilityType = null;
            customerLoanAmount = null;
            customerMonthlyInstalment = null;
            customerCollateralDetails = null;
            customerRemainingTenure = null;
            customerCurrentInterest = null;
            customerFinancialCommitment = new HashMap();
        }
    }

    public void addJointFinancialCommitment() {
        if (jointFinancialInstitution == null || jointFacilityType == null || jointLoanAmount == null || jointMonthlyInstalment == null
                || jointCollateralDetails == null || jointRemainingTenure == null || jointCurrentInterest == null) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please fill in all the fields", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        } else {
            jointFinancialCommitment.put("institution", jointFinancialInstitution);
            jointFinancialCommitment.put("type", jointFacilityType);
            jointFinancialCommitment.put("amount", jointLoanAmount);
            jointFinancialCommitment.put("instalment", jointMonthlyInstalment);
            jointFinancialCommitment.put("collateral", jointCollateralDetails);
            jointFinancialCommitment.put("tenure", jointRemainingTenure);
            jointFinancialCommitment.put("interest", jointCurrentInterest);
            jointFinancialCommitments.add(jointFinancialCommitment);
            jointFinancialInstitution = null;
            jointFacilityType = null;
            jointLoanAmount = null;
            jointMonthlyInstalment = null;
            jointCollateralDetails = null;
            jointRemainingTenure = null;
            jointCurrentInterest = null;
            jointFinancialCommitment = new HashMap();
        }
    }

    public void deleteCustomerPropertyOwner(String owner) {
        System.out.println("====== loan/PublicHDBLoanApplicationManagedBean: deleteCustomerPropertyOwner() ======");
        customerPropertyOwners.remove(owner);
    }

    public void deleteCustomerFinancialCommitment(HashMap commitment) {
        System.out.println("====== loan/PublicHDBLoanApplicationManagedBean: deleteCustomerFinancialCommitment() ======");
        customerFinancialCommitments.remove(commitment);
    }

    public void deleteJointFinancialCommitment(HashMap commitment) {
        jointFinancialCommitments.remove(commitment);
    }

    public void showSalutationPanel() {
        salutationPanelVisible = customerSalutation.equals("Others");
    }

    public void showNationalityPanel() {
        System.out.println("====== loan/PublicHDBLoanApplicationManagedBean: showNationalityPanel() ======");
        customerIsPR = null;
        customerSingaporeNRIC = null;
        if (customerNationality.equals("Singapore")) {
            nationalitySGPanelVisible = true;
            nationalityOthersPanelVisible = false;
            nricPanelVisible = false;
            passportPanelVisible = false;
        } else {
            nationalityOthersPanelVisible = true;
            nationalitySGPanelVisible = false;
            nricPanelVisible = false;
            passportPanelVisible = false;
        }
    }

    public void showIdentificationPanel() {
        System.out.println("====== loan/PublicHDBLoanApplicationManagedBean: showIdentificationPanel() ======");
        customerForeignNRIC = null;
        customerForeignPassport = null;
        if (customerIsPR.equals("Yes")) {
            nricPanelVisible = true;
            passportPanelVisible = false;
        } else if (customerIsPR.equals("No")) {
            passportPanelVisible = true;
            nricPanelVisible = false;
        } else {
            passportPanelVisible = false;
            nricPanelVisible = false;
        }
    }

    public void showIndustryTypePanel() {
        industryTypePanelVisible = customerIndustryType.equals("Others");
    }

    public void showCurrentPositionPanel() {
        currentPositionPanelVisible = customerCurrentPosition.equals("Others");
    }

    public void showPropertyTOPDatePanel() {
        propertyTOPPanelVisible = customerPropertyStatus.equals("Under construction");
    }

    public void showPropertyTenurePanel() {
        propertyTenurePanelVisible = customerPropertyTenureType.equals("Leasehold");
    }

    public void showPropertyOTPPanel() {
        if (customerPropertyWithOTP.equals("yes")) {
            propertyOTPPanelVisible = true;
            uploads.replace("otp", false);
        } else {
            propertyOTPPanelVisible = false;
            uploads.replace("otp", true);
        }
    }

    public void showPropertyTenancyPanel() {
        if (customerPropertyWithTenancy.equals("yes")) {
            propertyTenancyPanelVisible = true;
            uploads.replace("tenancy", false);
        } else {
            propertyTenancyPanelVisible = false;
            uploads.replace("tenancy", true);
        }
    }

    public void showBenefitsPanel() {
        propertyBenefitsPanelVisible = customerWithBenefitsFromVendor.equals("yes");
    }

    public void showFinancialRequestPanel() {
        if (customerFinancialRequest.equals("purchase")) {
            propertyNewPurchasePanelVisible = true;
            propertyRefinancingPanelVisible = false;
            uploads.replace("existingLoan", true);
            uploads.replace("cpfWithdrawal", true);
            uploads.replace("otp", false);
            uploads.replace("purchaseAgreement", false);
            uploads.replace("tenancy", false);
        } else if (customerFinancialRequest.equals("refinancing")) {
            propertyNewPurchasePanelVisible = false;
            propertyRefinancingPanelVisible = true;
            uploads.replace("otp", true);
            uploads.replace("purchaseAgreement", true);
            uploads.replace("existingLoan", false);
            uploads.replace("cpfWithdrawal", false);
            uploads.replace("tenancy", true);
        } else {
            propertyNewPurchasePanelVisible = false;
            propertyRefinancingPanelVisible = false;
        }
    }

    public void changeEmployeeStatus() {
        if (customerEmploymentStatus.equals("Employee")) {
            employmentPanelVisible = true;
            occupationPanelVisible = true;
            uploads.replace("selfEmployedTax", true);
            uploads.replace("employeeTax", false);
            uploads.replace("employeeCPF", false);
        } else if (customerEmploymentStatus.equals("Self-Employed")) {
            employmentPanelVisible = true;
            occupationPanelVisible = false;
            uploads.replace("selfEmployedTax", false);
            uploads.replace("employeeTax", true);
            uploads.replace("employeeCPF", true);
        } else {
            occupationPanelVisible = false;
            employmentPanelVisible = false;
            uploads.replace("selfEmployedTax", true);
            uploads.replace("employeeTax", false);
            uploads.replace("employeeCPF", true);
        }
    }

    public void showPanelJointApplicant() {
        noJointApplicantPanelVisible = hasJointApplicant.equals("No");
        jointApplicantPanelVisible = hasJointApplicant.equals("Yes");
    }

    public void showJointSalutationPanel() {
        jointSalutationPanelVisible = jointSalutation.equals("Others");
    }

    public void showJointNationalityPanel() {
        jointIsPR = null;
        jointSingaporeNRIC = null;
        if (jointNationality.equals("Singapore")) {
            jointNationalitySGPanelVisible = true;
            jointNationalityOthersPanelVisible = false;
            jointNricPanelVisible = false;
            jointPassportPanelVisible = false;
        } else {
            jointNationalityOthersPanelVisible = true;
            jointNationalitySGPanelVisible = false;
            jointNricPanelVisible = false;
            jointPassportPanelVisible = false;
        }
    }

    public void showJointIdentificationPanel() {
        jointForeignNRIC = null;
        jointForeignPassport = null;
        if (jointIsPR.equals("Yes")) {
            jointNricPanelVisible = true;
            jointPassportPanelVisible = false;
        } else if (jointIsPR.equals("No")) {
            jointPassportPanelVisible = true;
            jointNricPanelVisible = false;
        } else {
            jointPassportPanelVisible = false;
            jointNricPanelVisible = false;
        }
    }

    public void showJointIndustryTypePanel() {
        jointIndustryTypePanelVisible = jointIndustryType.equals("Others");
    }

    public void showJointCurrentPositionPanel() {
        jointCurrentPositionPanelVisible = jointCurrentPosition.equals("Others");
    }

    public void changeJointEmployeeStatus() {
        if (jointEmploymentStatus.equals("Employee")) {
            jointEmploymentPanelVisible = true;
            jointOccupationPanelVisible = true;
            uploads.replace("jointSelfEmployedTax", true);
            uploads.replace("jointEmployeeTax", false);
        } else if (jointEmploymentStatus.equals("Self-Employed")) {
            jointEmploymentPanelVisible = true;
            jointOccupationPanelVisible = false;
            uploads.replace("jointSelfEmployedTax", false);
            uploads.replace("jointEmployeeTax", true);
        } else {
            jointOccupationPanelVisible = false;
            jointEmploymentPanelVisible = false;
            uploads.replace("jointSelfEmployedTax", true);
            uploads.replace("jointEmployeeTax", false);
        }
    }

    public void identificationUpload(FileUploadEvent event) throws FileNotFoundException, IOException {
        this.file = event.getFile();

        if (file != null) {
            String newFilePath = System.getProperty("user.dir").replace("config", "docroot") + System.getProperty("file.separator");

            String filename = customerIdentificationNum + ".pdf";
            File newFile = new File(newFilePath, filename);
            FileOutputStream fileOutputStream = new FileOutputStream(newFile);

            int a;
            int BUFFER_SIZE = 8192;
            byte[] buffer = new byte[BUFFER_SIZE];

            InputStream inputStream = file.getInputstream();

            while (true) {
                a = inputStream.read(buffer);

                if (a < 0) {
                    break;
                }

                fileOutputStream.write(buffer, 0, a);
                fileOutputStream.flush();
            }

            fileOutputStream.close();
            inputStream.close();

            uploads.replace("identification", true);
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, file.getFileName() + " uploaded successfully.", "");
            FacesContext.getCurrentInstance().addMessage("identificationUpload", message);
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Cannot find the file, please upload again.", "");
            FacesContext.getCurrentInstance().addMessage("identificationUpload", message);
        }
    }

    public void otpUpload(FileUploadEvent event) throws FileNotFoundException, IOException {
        this.file = event.getFile();
        if (file != null) {
            String newFilePath = System.getProperty("user.dir").replace("config", "docroot") + System.getProperty("file.separator");

            String filename = customerIdentificationNum + "-otp.pdf";
            File newFile = new File(newFilePath, filename);
            FileOutputStream fileOutputStream = new FileOutputStream(newFile);

            int a;
            int BUFFER_SIZE = 8192;
            byte[] buffer = new byte[BUFFER_SIZE];

            InputStream inputStream = file.getInputstream();

            while (true) {
                a = inputStream.read(buffer);

                if (a < 0) {
                    break;
                }

                fileOutputStream.write(buffer, 0, a);
                fileOutputStream.flush();
            }

            fileOutputStream.close();
            inputStream.close();

            uploads.replace("otp", true);
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, file.getFileName() + " uploaded successfully.", "");
            FacesContext.getCurrentInstance().addMessage("otpUpload", message);
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Cannot find the file, please upload again.", "");
            FacesContext.getCurrentInstance().addMessage("otpUpload", message);
        }
    }

    public void purchaseAgreementUpload(FileUploadEvent event) throws FileNotFoundException, IOException {
        this.file = event.getFile();
        if (file != null) {
            String newFilePath = System.getProperty("user.dir").replace("config", "docroot") + System.getProperty("file.separator");

            String filename = customerIdentificationNum + "-purchase_agreement.pdf";
            File newFile = new File(newFilePath, filename);
            FileOutputStream fileOutputStream = new FileOutputStream(newFile);

            int a;
            int BUFFER_SIZE = 8192;
            byte[] buffer = new byte[BUFFER_SIZE];

            InputStream inputStream = file.getInputstream();

            while (true) {
                a = inputStream.read(buffer);

                if (a < 0) {
                    break;
                }

                fileOutputStream.write(buffer, 0, a);
                fileOutputStream.flush();
            }

            fileOutputStream.close();
            inputStream.close();

            uploads.replace("purchaseAgreement", true);
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, file.getFileName() + " uploaded successfully.", "");
            FacesContext.getCurrentInstance().addMessage("purchaseAgreementUpload", message);
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Cannot find the file, please upload again.", "");
            FacesContext.getCurrentInstance().addMessage("purchaseAgreementUpload", message);
        }
    }

    public void existingLoanUpload(FileUploadEvent event) throws FileNotFoundException, IOException {
        this.file = event.getFile();
        if (file != null) {
            String newFilePath = System.getProperty("user.dir").replace("config", "docroot") + System.getProperty("file.separator");

            String filename = customerIdentificationNum + "-existing_loan.pdf";
            File newFile = new File(newFilePath, filename);
            FileOutputStream fileOutputStream = new FileOutputStream(newFile);

            int a;
            int BUFFER_SIZE = 8192;
            byte[] buffer = new byte[BUFFER_SIZE];

            InputStream inputStream = file.getInputstream();

            while (true) {
                a = inputStream.read(buffer);

                if (a < 0) {
                    break;
                }

                fileOutputStream.write(buffer, 0, a);
                fileOutputStream.flush();
            }

            fileOutputStream.close();
            inputStream.close();

            uploads.replace("existingLoan", true);
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, file.getFileName() + " uploaded successfully.", "");
            FacesContext.getCurrentInstance().addMessage("existingLoanUpload", message);
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Cannot find the file, please upload again.", "");
            FacesContext.getCurrentInstance().addMessage("existingLoanUpload", message);
        }
    }

    public void cpfWithdrawalUpload(FileUploadEvent event) throws FileNotFoundException, IOException {
        this.file = event.getFile();
        if (file != null) {
            String newFilePath = System.getProperty("user.dir").replace("config", "docroot") + System.getProperty("file.separator");

            String filename = customerIdentificationNum + "-cpf_withdrawal.pdf";
            File newFile = new File(newFilePath, filename);
            FileOutputStream fileOutputStream = new FileOutputStream(newFile);

            int a;
            int BUFFER_SIZE = 8192;
            byte[] buffer = new byte[BUFFER_SIZE];

            InputStream inputStream = file.getInputstream();

            while (true) {
                a = inputStream.read(buffer);

                if (a < 0) {
                    break;
                }

                fileOutputStream.write(buffer, 0, a);
                fileOutputStream.flush();
            }

            fileOutputStream.close();
            inputStream.close();

            uploads.replace("cpfWithdrawal", true);
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, file.getFileName() + " uploaded successfully.", "");
            FacesContext.getCurrentInstance().addMessage("cpfWithdrawalUpload", message);
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Cannot find the file, please upload again.", "");
            FacesContext.getCurrentInstance().addMessage("cpfWithdrawalUpload", message);
        }
    }

    public void evidenceOfSaleUpload(FileUploadEvent event) throws FileNotFoundException, IOException {
        this.file = event.getFile();
        if (file != null) {
            String newFilePath = System.getProperty("user.dir").replace("config", "docroot") + System.getProperty("file.separator");

            String filename = customerIdentificationNum + "-evidence_of_sale.pdf";
            File newFile = new File(newFilePath, filename);
            FileOutputStream fileOutputStream = new FileOutputStream(newFile);

            int a;
            int BUFFER_SIZE = 8192;
            byte[] buffer = new byte[BUFFER_SIZE];

            InputStream inputStream = file.getInputstream();

            while (true) {
                a = inputStream.read(buffer);

                if (a < 0) {
                    break;
                }

                fileOutputStream.write(buffer, 0, a);
                fileOutputStream.flush();
            }

            fileOutputStream.close();
            inputStream.close();

            uploads.replace("evidenceOfSale", true);
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, file.getFileName() + " uploaded successfully.", "");
            FacesContext.getCurrentInstance().addMessage("evidenceOfSaleUpload", message);
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Cannot find the file, please upload again.", "");
            FacesContext.getCurrentInstance().addMessage("evidenceOfSaleUpload", message);
        }
    }

    public void tenancyUpload(FileUploadEvent event) throws FileNotFoundException, IOException {
        this.file = event.getFile();
        if (file != null) {
            String newFilePath = System.getProperty("user.dir").replace("config", "docroot") + System.getProperty("file.separator");

            String filename = customerIdentificationNum + "-tenancy.pdf";
            File newFile = new File(newFilePath, filename);
            FileOutputStream fileOutputStream = new FileOutputStream(newFile);

            int a;
            int BUFFER_SIZE = 8192;
            byte[] buffer = new byte[BUFFER_SIZE];

            InputStream inputStream = file.getInputstream();

            while (true) {
                a = inputStream.read(buffer);

                if (a < 0) {
                    break;
                }

                fileOutputStream.write(buffer, 0, a);
                fileOutputStream.flush();
            }

            fileOutputStream.close();
            inputStream.close();

            uploads.replace("tenancy", true);
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, file.getFileName() + " uploaded successfully.", "");
            FacesContext.getCurrentInstance().addMessage("tenancyUpload", message);
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Cannot find the file, please upload again.", "");
            FacesContext.getCurrentInstance().addMessage("tenancyUpload", message);
        }
    }

    public void employeeTaxUpload(FileUploadEvent event) throws FileNotFoundException, IOException {
        this.file = event.getFile();
        if (file != null) {
            String newFilePath = System.getProperty("user.dir").replace("config", "docroot") + System.getProperty("file.separator");

            String filename = customerIdentificationNum + "-employee_tax.pdf";
            File newFile = new File(newFilePath, filename);
            FileOutputStream fileOutputStream = new FileOutputStream(newFile);

            int a;
            int BUFFER_SIZE = 8192;
            byte[] buffer = new byte[BUFFER_SIZE];

            InputStream inputStream = file.getInputstream();

            while (true) {
                a = inputStream.read(buffer);

                if (a < 0) {
                    break;
                }

                fileOutputStream.write(buffer, 0, a);
                fileOutputStream.flush();
            }

            fileOutputStream.close();
            inputStream.close();

            uploads.replace("employeeTax", true);
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, file.getFileName() + " uploaded successfully.", "");
            FacesContext.getCurrentInstance().addMessage("employeeTaxUpload", message);
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Cannot find the file, please upload again.", "");
            FacesContext.getCurrentInstance().addMessage("employeeTaxUpload", message);
        }
    }

    public void employeeCPFUpload(FileUploadEvent event) throws FileNotFoundException, IOException {
        this.file = event.getFile();
        if (file != null) {
            String newFilePath = System.getProperty("user.dir").replace("config", "docroot") + System.getProperty("file.separator");

            String filename = customerIdentificationNum + "-employee_cpf.pdf";
            File newFile = new File(newFilePath, filename);
            FileOutputStream fileOutputStream = new FileOutputStream(newFile);

            int a;
            int BUFFER_SIZE = 8192;
            byte[] buffer = new byte[BUFFER_SIZE];

            InputStream inputStream = file.getInputstream();

            while (true) {
                a = inputStream.read(buffer);

                if (a < 0) {
                    break;
                }

                fileOutputStream.write(buffer, 0, a);
                fileOutputStream.flush();
            }

            fileOutputStream.close();
            inputStream.close();

            uploads.replace("employeeCPF", true);
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, file.getFileName() + " uploaded successfully.", "");
            FacesContext.getCurrentInstance().addMessage("employeeCPFUpload", message);
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Cannot find the file, please upload again.", "");
            FacesContext.getCurrentInstance().addMessage("employeeCPFUpload", message);
        }
    }

    public void selfEmployedTaxUpload(FileUploadEvent event) throws FileNotFoundException, IOException {
        this.file = event.getFile();
        if (file != null) {
            String newFilePath = System.getProperty("user.dir").replace("config", "docroot") + System.getProperty("file.separator");

            String filename = customerIdentificationNum + "-self-employed_tax.pdf";
            File newFile = new File(newFilePath, filename);
            FileOutputStream fileOutputStream = new FileOutputStream(newFile);

            int a;
            int BUFFER_SIZE = 8192;
            byte[] buffer = new byte[BUFFER_SIZE];

            InputStream inputStream = file.getInputstream();

            while (true) {
                a = inputStream.read(buffer);

                if (a < 0) {
                    break;
                }

                fileOutputStream.write(buffer, 0, a);
                fileOutputStream.flush();
            }

            fileOutputStream.close();
            inputStream.close();

            uploads.replace("selfEmployedTax", true);
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, file.getFileName() + " uploaded successfully.", "");
            FacesContext.getCurrentInstance().addMessage("selfEmployedTaxUpload", message);
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Cannot find the file, please upload again.", "");
            FacesContext.getCurrentInstance().addMessage("selfEmployedTaxUpload", message);
        }
    }

    public void jointIdentificationUpload(FileUploadEvent event) throws FileNotFoundException, IOException {
        this.file = event.getFile();

        if (file != null) {
            String newFilePath = System.getProperty("user.dir").replace("config", "docroot") + System.getProperty("file.separator");

            String filename = jointIdentificationNum + ".pdf";
            File newFile = new File(newFilePath, filename);
            FileOutputStream fileOutputStream = new FileOutputStream(newFile);

            int a;
            int BUFFER_SIZE = 8192;
            byte[] buffer = new byte[BUFFER_SIZE];

            InputStream inputStream = file.getInputstream();

            while (true) {
                a = inputStream.read(buffer);

                if (a < 0) {
                    break;
                }

                fileOutputStream.write(buffer, 0, a);
                fileOutputStream.flush();
            }

            fileOutputStream.close();
            inputStream.close();

            uploads.replace("jointIdentification", true);
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, file.getFileName() + " uploaded successfully.", "");
            FacesContext.getCurrentInstance().addMessage("jointIdentificationUpload", message);
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Cannot find the file, please upload again.", "");
            FacesContext.getCurrentInstance().addMessage("jointIdentificationUpload", message);
        }
    }

    public void jointRelationshipUpload(FileUploadEvent event) throws FileNotFoundException, IOException {
        this.file = event.getFile();

        if (file != null) {
            String newFilePath = System.getProperty("user.dir").replace("config", "docroot") + System.getProperty("file.separator");

            String filename = customerIdentificationNum + "-joint_relationship.pdf";
            File newFile = new File(newFilePath, filename);
            FileOutputStream fileOutputStream = new FileOutputStream(newFile);

            int a;
            int BUFFER_SIZE = 8192;
            byte[] buffer = new byte[BUFFER_SIZE];

            InputStream inputStream = file.getInputstream();

            while (true) {
                a = inputStream.read(buffer);

                if (a < 0) {
                    break;
                }

                fileOutputStream.write(buffer, 0, a);
                fileOutputStream.flush();
            }

            fileOutputStream.close();
            inputStream.close();

            uploads.replace("relationship", true);
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, file.getFileName() + " uploaded successfully.", "");
            FacesContext.getCurrentInstance().addMessage("jointRelationshipUpload", message);
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Cannot find the file, please upload again.", "");
            FacesContext.getCurrentInstance().addMessage("jointRelationshipUpload", message);
        }
    }

    public void jointEmployeeTaxUpload(FileUploadEvent event) throws FileNotFoundException, IOException {
        this.file = event.getFile();
        if (file != null) {
            String newFilePath = System.getProperty("user.dir").replace("config", "docroot") + System.getProperty("file.separator");

            String filename = customerIdentificationNum + "-joint_employee_tax.pdf";
            File newFile = new File(newFilePath, filename);
            FileOutputStream fileOutputStream = new FileOutputStream(newFile);

            int a;
            int BUFFER_SIZE = 8192;
            byte[] buffer = new byte[BUFFER_SIZE];

            InputStream inputStream = file.getInputstream();

            while (true) {
                a = inputStream.read(buffer);

                if (a < 0) {
                    break;
                }

                fileOutputStream.write(buffer, 0, a);
                fileOutputStream.flush();
            }

            fileOutputStream.close();
            inputStream.close();

            uploads.replace("jointEmployeeTax", true);
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, file.getFileName() + " uploaded successfully.", "");
            FacesContext.getCurrentInstance().addMessage("jointEmployeeTaxUpload", message);
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Cannot find the file, please upload again.", "");
            FacesContext.getCurrentInstance().addMessage("jointEmployeeTaxUpload", message);
        }
    }

    public void jointSelfEmployedTaxUpload(FileUploadEvent event) throws FileNotFoundException, IOException {
        this.file = event.getFile();
        if (file != null) {
            String newFilePath = System.getProperty("user.dir").replace("config", "docroot") + System.getProperty("file.separator");

            String filename = customerIdentificationNum + "-joint_self-employed_tax.pdf";
            File newFile = new File(newFilePath, filename);
            FileOutputStream fileOutputStream = new FileOutputStream(newFile);

            int a;
            int BUFFER_SIZE = 8192;
            byte[] buffer = new byte[BUFFER_SIZE];

            InputStream inputStream = file.getInputstream();

            while (true) {
                a = inputStream.read(buffer);

                if (a < 0) {
                    break;
                }

                fileOutputStream.write(buffer, 0, a);
                fileOutputStream.flush();
            }

            fileOutputStream.close();
            inputStream.close();

            uploads.replace("jointSelfEmployedTax", true);
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, file.getFileName() + " uploaded successfully.", "");
            FacesContext.getCurrentInstance().addMessage("jointSelfEmployedTaxUpload", message);
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Cannot find the file, please upload again.", "");
            FacesContext.getCurrentInstance().addMessage("jointSelfEmployedTaxUpload", message);
        }
    }

    private String changeDateFormat(Date inputDate) {
        String dateString = inputDate.toString();
        String[] dateSplit = dateString.split(" ");
        String outputDate = dateSplit[2] + "/" + dateSplit[1] + "/" + dateSplit[5];
        return outputDate;
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

    public Date getCustomerDateOfBirth() {
        return customerDateOfBirth;
    }

    public void setCustomerDateOfBirth(Date customerDateOfBirth) {
        this.customerDateOfBirth = customerDateOfBirth;
    }

    public String getCustomerGender() {
        return customerGender;
    }

    public void setCustomerGender(String customerGender) {
        this.customerGender = customerGender;
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

    public boolean isSalutationPanelVisible() {
        return salutationPanelVisible;
    }

    public void setSalutationPanelVisible(boolean salutationPanelVisible) {
        this.salutationPanelVisible = salutationPanelVisible;
    }

    public String getCustomerSalutationOthers() {
        return customerSalutationOthers;
    }

    public void setCustomerSalutationOthers(String customerSalutationOthers) {
        this.customerSalutationOthers = customerSalutationOthers;
    }

    public boolean isNationalitySGPanelVisible() {
        return nationalitySGPanelVisible;
    }

    public void setNationalitySGPanelVisible(boolean nationalitySGPanelVisible) {
        this.nationalitySGPanelVisible = nationalitySGPanelVisible;
    }

    public boolean isNationalityOthersPanelVisible() {
        return nationalityOthersPanelVisible;
    }

    public void setNationalityOthersPanelVisible(boolean nationalityOthersPanelVisible) {
        this.nationalityOthersPanelVisible = nationalityOthersPanelVisible;
    }

    public boolean isNricPanelVisible() {
        return nricPanelVisible;
    }

    public void setNricPanelVisible(boolean nricPanelVisible) {
        this.nricPanelVisible = nricPanelVisible;
    }

    public boolean isPassportPanelVisible() {
        return passportPanelVisible;
    }

    public void setPassportPanelVisible(boolean passportPanelVisible) {
        this.passportPanelVisible = passportPanelVisible;
    }

    public String getCustomerIsPR() {
        return customerIsPR;
    }

    public void setCustomerIsPR(String customerIsPR) {
        this.customerIsPR = customerIsPR;
    }

    public String getCustomerEducation() {
        return customerEducation;
    }

    public void setCustomerEducation(String customerEducation) {
        this.customerEducation = customerEducation;
    }

    public String getCustomerStreetName() {
        return customerStreetName;
    }

    public void setCustomerStreetName(String customerStreetName) {
        this.customerStreetName = customerStreetName;
    }

    public String getCustomerBlockNum() {
        return customerBlockNum;
    }

    public void setCustomerBlockNum(String customerBlockNum) {
        this.customerBlockNum = customerBlockNum;
    }

    public String getCustomerUnitNum() {
        return customerUnitNum;
    }

    public void setCustomerUnitNum(String customerUnitNum) {
        this.customerUnitNum = customerUnitNum;
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

    public String getCustomerEmploymentStatus() {
        return customerEmploymentStatus;
    }

    public void setCustomerEmploymentStatus(String customerEmploymentStatus) {
        this.customerEmploymentStatus = customerEmploymentStatus;
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

    public String getCustomerIndustryTypeOthers() {
        return customerIndustryTypeOthers;
    }

    public void setCustomerIndustryTypeOthers(String customerIndustryTypeOthers) {
        this.customerIndustryTypeOthers = customerIndustryTypeOthers;
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

    public String getCustomerPreviousCompany() {
        return customerPreviousCompany;
    }

    public void setCustomerPreviousCompany(String customerPreviousCompany) {
        this.customerPreviousCompany = customerPreviousCompany;
    }

    public String getCustomerOtherMonthlyIncomeSource() {
        return customerOtherMonthlyIncomeSource;
    }

    public void setCustomerOtherMonthlyIncomeSource(String customerOtherMonthlyIncomeSource) {
        this.customerOtherMonthlyIncomeSource = customerOtherMonthlyIncomeSource;
    }

    public boolean isIndustryTypePanelVisible() {
        return industryTypePanelVisible;
    }

    public void setIndustryTypePanelVisible(boolean industryTypePanelVisible) {
        this.industryTypePanelVisible = industryTypePanelVisible;
    }

    public boolean isCurrentPositionPanelVisible() {
        return currentPositionPanelVisible;
    }

    public void setCurrentPositionPanelVisible(boolean currentPositionPanelVisible) {
        this.currentPositionPanelVisible = currentPositionPanelVisible;
    }

    public String getCustomerCurrentPositionOthers() {
        return customerCurrentPositionOthers;
    }

    public void setCustomerCurrentPositionOthers(String customerCurrentPositionOthers) {
        this.customerCurrentPositionOthers = customerCurrentPositionOthers;
    }

    public String getCustomerPropertyStreetName() {
        return customerPropertyStreetName;
    }

    public void setCustomerPropertyStreetName(String customerPropertyStreetName) {
        this.customerPropertyStreetName = customerPropertyStreetName;
    }

    public String getCustomerPropertyBlockNum() {
        return customerPropertyBlockNum;
    }

    public void setCustomerPropertyBlockNum(String customerPropertyBlockNum) {
        this.customerPropertyBlockNum = customerPropertyBlockNum;
    }

    public String getCustomerPropertyUnitNum() {
        return customerPropertyUnitNum;
    }

    public void setCustomerPropertyUnitNum(String customerPropertyUnitNum) {
        this.customerPropertyUnitNum = customerPropertyUnitNum;
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

    public String getCustomerPropertyOwner() {
        return customerPropertyOwner;
    }

    public void setCustomerPropertyOwner(String customerPropertyOwner) {
        this.customerPropertyOwner = customerPropertyOwner;
    }

    public String getCustomerPropertyType() {
        return customerPropertyType;
    }

    public void setCustomerPropertyType(String customerPropertyType) {
        this.customerPropertyType = customerPropertyType;
    }

    public String getCustomerIdentificationNum() {
        return customerIdentificationNum;
    }

    public void setCustomerIdentificationNum(String customerIdentificationNum) {
        this.customerIdentificationNum = customerIdentificationNum;
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

    public boolean isPropertyTOPPanelVisible() {
        return propertyTOPPanelVisible;
    }

    public void setPropertyTOPPanelVisible(boolean propertyTOPPanelVisible) {
        this.propertyTOPPanelVisible = propertyTOPPanelVisible;
    }

    public Date getCustomerPropertyDateOfPurchase() {
        return customerPropertyDateOfPurchase;
    }

    public void setCustomerPropertyDateOfPurchase(Date customerPropertyDateOfPurchase) {
        this.customerPropertyDateOfPurchase = customerPropertyDateOfPurchase;
    }

    public String getCustomerPropertyTenureType() {
        return customerPropertyTenureType;
    }

    public void setCustomerPropertyTenureType(String customerPropertyTenureType) {
        this.customerPropertyTenureType = customerPropertyTenureType;
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

    public String getCustomerWithBenefitsFromVendor() {
        return customerWithBenefitsFromVendor;
    }

    public void setCustomerWithBenefitsFromVendor(String customerWithBenefitsFromVendor) {
        this.customerWithBenefitsFromVendor = customerWithBenefitsFromVendor;
    }

    public boolean isPropertyTenurePanelVisible() {
        return propertyTenurePanelVisible;
    }

    public void setPropertyTenurePanelVisible(boolean propertyTenurePanelVisible) {
        this.propertyTenurePanelVisible = propertyTenurePanelVisible;
    }

    public Integer getCustomerNumOfDependents() {
        return customerNumOfDependents;
    }

    public void setCustomerNumOfDependents(Integer customerNumOfDependents) {
        this.customerNumOfDependents = customerNumOfDependents;
    }

    public Integer getCustomerLengthOfResidence() {
        return customerLengthOfResidence;
    }

    public void setCustomerLengthOfResidence(Integer customerLengthOfResidence) {
        this.customerLengthOfResidence = customerLengthOfResidence;
    }

    public Integer getCustomerLengthOfCurrentJob() {
        return customerLengthOfCurrentJob;
    }

    public void setCustomerLengthOfCurrentJob(Integer customerLengthOfCurrentJob) {
        this.customerLengthOfCurrentJob = customerLengthOfCurrentJob;
    }

    public Integer getCustomerLengthOfPreviousJob() {
        return customerLengthOfPreviousJob;
    }

    public void setCustomerLengthOfPreviousJob(Integer customerLengthOfPreviousJob) {
        this.customerLengthOfPreviousJob = customerLengthOfPreviousJob;
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

    public Integer getCustomerPropertyTenancyExpiryYear() {
        return customerPropertyTenancyExpiryYear;
    }

    public void setCustomerPropertyTenancyExpiryYear(Integer customerPropertyTenancyExpiryYear) {
        this.customerPropertyTenancyExpiryYear = customerPropertyTenancyExpiryYear;
    }

    public ArrayList<HashMap> getCustomerFinancialCommitments() {
        return customerFinancialCommitments;
    }

    public void setCustomerFinancialCommitments(ArrayList<HashMap> customerFinancialCommitments) {
        this.customerFinancialCommitments = customerFinancialCommitments;
    }

    public String getCustomerFacilityType() {
        return customerFacilityType;
    }

    public void setCustomerFacilityType(String customerFacilityType) {
        this.customerFacilityType = customerFacilityType;
    }

    public String getCustomerFinancialInstitution() {
        return customerFinancialInstitution;
    }

    public void setCustomerFinancialInstitution(String customerFinancialInstitution) {
        this.customerFinancialInstitution = customerFinancialInstitution;
    }

    public HashMap getCustomerFinancialCommitment() {
        return customerFinancialCommitment;
    }

    public void setCustomerFinancialCommitment(HashMap customerFinancialCommitment) {
        this.customerFinancialCommitment = customerFinancialCommitment;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public BigDecimal getCustomerMonthlyFixedIncome() {
        return customerMonthlyFixedIncome;
    }

    public void setCustomerMonthlyFixedIncome(BigDecimal customerMonthlyFixedIncome) {
        this.customerMonthlyFixedIncome = customerMonthlyFixedIncome;
    }

    public BigDecimal getCustomerOtherMonthlyIncome() {
        return customerOtherMonthlyIncome;
    }

    public void setCustomerOtherMonthlyIncome(BigDecimal customerOtherMonthlyIncome) {
        this.customerOtherMonthlyIncome = customerOtherMonthlyIncome;
    }

    public BigDecimal getCustomerLoanAmount() {
        return customerLoanAmount;
    }

    public void setCustomerLoanAmount(BigDecimal customerLoanAmount) {
        this.customerLoanAmount = customerLoanAmount;
    }

    public BigDecimal getCustomerMonthlyInstalment() {
        return customerMonthlyInstalment;
    }

    public void setCustomerMonthlyInstalment(BigDecimal customerMonthlyInstalment) {
        this.customerMonthlyInstalment = customerMonthlyInstalment;
    }

    public BigDecimal getCustomerPropertyBuiltUpArea() {
        return customerPropertyBuiltUpArea;
    }

    public void setCustomerPropertyBuiltUpArea(BigDecimal customerPropertyBuiltUpArea) {
        this.customerPropertyBuiltUpArea = customerPropertyBuiltUpArea;
    }

    public BigDecimal getCustomerPropertyLandArea() {
        return customerPropertyLandArea;
    }

    public void setCustomerPropertyLandArea(BigDecimal customerPropertyLandArea) {
        this.customerPropertyLandArea = customerPropertyLandArea;
    }

    public BigDecimal getCustomerPropertyPurchasePrice() {
        return customerPropertyPurchasePrice;
    }

    public void setCustomerPropertyPurchasePrice(BigDecimal customerPropertyPurchasePrice) {
        this.customerPropertyPurchasePrice = customerPropertyPurchasePrice;
    }

    public BigDecimal getCustomerPropertyTenancyIncome() {
        return customerPropertyTenancyIncome;
    }

    public void setCustomerPropertyTenancyIncome(BigDecimal customerPropertyTenancyIncome) {
        this.customerPropertyTenancyIncome = customerPropertyTenancyIncome;
    }

    public BigDecimal getCustomerBenefitsFromVendor() {
        return customerBenefitsFromVendor;
    }

    public void setCustomerBenefitsFromVendor(BigDecimal customerBenefitsFromVendor) {
        this.customerBenefitsFromVendor = customerBenefitsFromVendor;
    }

    public BigDecimal getCustomerCashDownpayment() {
        return customerCashDownpayment;
    }

    public void setCustomerCashDownpayment(BigDecimal customerCashDownpayment) {
        this.customerCashDownpayment = customerCashDownpayment;
    }

    public BigDecimal getCustomerCPFDownpayment() {
        return customerCPFDownpayment;
    }

    public void setCustomerCPFDownpayment(BigDecimal customerCPFDownpayment) {
        this.customerCPFDownpayment = customerCPFDownpayment;
    }

    public HashMap getUploads() {
        return uploads;
    }

    public void setUploads(HashMap uploads) {
        this.uploads = uploads;
    }

    public boolean isPropertyOTPPanelVisible() {
        return propertyOTPPanelVisible;
    }

    public void setPropertyOTPPanelVisible(boolean propertyOTPPanelVisible) {
        this.propertyOTPPanelVisible = propertyOTPPanelVisible;
    }

    public boolean isPropertyTenancyPanelVisible() {
        return propertyTenancyPanelVisible;
    }

    public void setPropertyTenancyPanelVisible(boolean propertyTenancyPanelVisible) {
        this.propertyTenancyPanelVisible = propertyTenancyPanelVisible;
    }

    public boolean isPropertyBenefitsPanelVisible() {
        return propertyBenefitsPanelVisible;
    }

    public void setPropertyBenefitsPanelVisible(boolean propertyBenefitsPanelVisible) {
        this.propertyBenefitsPanelVisible = propertyBenefitsPanelVisible;
    }

    public BigDecimal getCustomerLoanAmountRequired() {
        return customerLoanAmountRequired;
    }

    public void setCustomerLoanAmountRequired(BigDecimal customerLoanAmountRequired) {
        this.customerLoanAmountRequired = customerLoanAmountRequired;
    }

    public Integer getCustomerLoanTenure() {
        return customerLoanTenure;
    }

    public void setCustomerLoanTenure(Integer customerLoanTenure) {
        this.customerLoanTenure = customerLoanTenure;
    }

    public String getCustomerFinancialRequest() {
        return customerFinancialRequest;
    }

    public void setCustomerFinancialRequest(String customerFinancialRequest) {
        this.customerFinancialRequest = customerFinancialRequest;
    }

    public boolean isPropertyNewPurchasePanelVisible() {
        return propertyNewPurchasePanelVisible;
    }

    public void setPropertyNewPurchasePanelVisible(boolean propertyNewPurchasePanelVisible) {
        this.propertyNewPurchasePanelVisible = propertyNewPurchasePanelVisible;
    }

    public boolean isPropertyRefinancingPanelVisible() {
        return propertyRefinancingPanelVisible;
    }

    public void setPropertyRefinancingPanelVisible(boolean propertyRefinancingPanelVisible) {
        this.propertyRefinancingPanelVisible = propertyRefinancingPanelVisible;
    }

    public String getCustomerExistingFinancer() {
        return customerExistingFinancer;
    }

    public void setCustomerExistingFinancer(String customerExistingFinancer) {
        this.customerExistingFinancer = customerExistingFinancer;
    }

    public BigDecimal getCustomerOutstandingLoan() {
        return customerOutstandingLoan;
    }

    public void setCustomerOutstandingLoan(BigDecimal customerOutstandingLoan) {
        this.customerOutstandingLoan = customerOutstandingLoan;
    }

    public Integer getCustomerOutstandingYear() {
        return customerOutstandingYear;
    }

    public void setCustomerOutstandingYear(Integer customerOutstandingYear) {
        this.customerOutstandingYear = customerOutstandingYear;
    }

    public BigDecimal getCustomerTotalCPFWithdrawal() {
        return customerTotalCPFWithdrawal;
    }

    public void setCustomerTotalCPFWithdrawal(BigDecimal customerTotalCPFWithdrawal) {
        this.customerTotalCPFWithdrawal = customerTotalCPFWithdrawal;
    }

    public Integer getCustomerOutstandingMonth() {
        return customerOutstandingMonth;
    }

    public void setCustomerOutstandingMonth(Integer customerOutstandingMonth) {
        this.customerOutstandingMonth = customerOutstandingMonth;
    }

    public boolean isAgreement() {
        return agreement;
    }

    public void setAgreement(boolean agreement) {
        this.agreement = agreement;
    }

    public String getCustomerOccupation() {
        return customerOccupation;
    }

    public void setCustomerOccupation(String customerOccupation) {
        this.customerOccupation = customerOccupation;
    }

    public String getCustomerSignature() {
        return customerSignature;
    }

    public void setCustomerSignature(String customerSignature) {
        this.customerSignature = customerSignature;
    }

    public boolean isOccupationPanelVisible() {
        return occupationPanelVisible;
    }

    public void setOccupationPanelVisible(boolean occupationPanelVisible) {
        this.occupationPanelVisible = occupationPanelVisible;
    }

    public boolean isEmploymentPanelVisible() {
        return employmentPanelVisible;
    }

    public void setEmploymentPanelVisible(boolean employmentPanelVisible) {
        this.employmentPanelVisible = employmentPanelVisible;
    }

    public CRMCustomerSessionBean getcRMCustomerSessionBeanLocal() {
        return cRMCustomerSessionBeanLocal;
    }

    public void setcRMCustomerSessionBeanLocal(CRMCustomerSessionBean cRMCustomerSessionBeanLocal) {
        this.cRMCustomerSessionBeanLocal = cRMCustomerSessionBeanLocal;
    }

    public LoanApplicationSessionBeanLocal getLoanApplicationSessionBeanLocal() {
        return loanApplicationSessionBeanLocal;
    }

    public void setLoanApplicationSessionBeanLocal(LoanApplicationSessionBeanLocal loanApplicationSessionBeanLocal) {
        this.loanApplicationSessionBeanLocal = loanApplicationSessionBeanLocal;
    }

    public String getCustomerSingaporeNRIC() {
        return customerSingaporeNRIC;
    }

    public void setCustomerSingaporeNRIC(String customerSingaporeNRIC) {
        this.customerSingaporeNRIC = customerSingaporeNRIC;
    }

    public String getCustomerForeignNRIC() {
        return customerForeignNRIC;
    }

    public void setCustomerForeignNRIC(String customerForeignNRIC) {
        this.customerForeignNRIC = customerForeignNRIC;
    }

    public String getCustomerForeignPassport() {
        return customerForeignPassport;
    }

    public void setCustomerForeignPassport(String customerForeignPassport) {
        this.customerForeignPassport = customerForeignPassport;
    }

    public String getInterestPackage() {
        return interestPackage;
    }

    public void setInterestPackage(String interestPackage) {
        this.interestPackage = interestPackage;
    }

    public ArrayList getPropertyTypes() {
        return propertyTypes;
    }

    public void setPropertyTypes(ArrayList propertyTypes) {
        this.propertyTypes = propertyTypes;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public boolean isJointApplicantPanelVisible() {
        return jointApplicantPanelVisible;
    }

    public void setJointApplicantPanelVisible(boolean jointApplicantPanelVisible) {
        this.jointApplicantPanelVisible = jointApplicantPanelVisible;
    }

    public boolean isNoJointApplicantPanelVisible() {
        return noJointApplicantPanelVisible;
    }

    public void setNoJointApplicantPanelVisible(boolean noJointApplicantPanelVisible) {
        this.noJointApplicantPanelVisible = noJointApplicantPanelVisible;
    }

    public String getHasJointApplicant() {
        return hasJointApplicant;
    }

    public void setHasJointApplicant(String hasJointApplicant) {
        this.hasJointApplicant = hasJointApplicant;
    }

    public String getJointSalutation() {
        return jointSalutation;
    }

    public void setJointSalutation(String jointSalutation) {
        this.jointSalutation = jointSalutation;
    }

    public String getJointSalutationOthers() {
        return jointSalutationOthers;
    }

    public void setJointSalutationOthers(String jointSalutationOthers) {
        this.jointSalutationOthers = jointSalutationOthers;
    }

    public String getJointName() {
        return jointName;
    }

    public void setJointName(String jointName) {
        this.jointName = jointName;
    }

    public Date getJointDateOfBirth() {
        return jointDateOfBirth;
    }

    public void setJointDateOfBirth(Date jointDateOfBirth) {
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

    public String getJointIsPR() {
        return jointIsPR;
    }

    public void setJointIsPR(String jointIsPR) {
        this.jointIsPR = jointIsPR;
    }

    public String getJointIdentificationNum() {
        return jointIdentificationNum;
    }

    public void setJointIdentificationNum(String jointIdentificationNum) {
        this.jointIdentificationNum = jointIdentificationNum;
    }

    public String getJointSingaporeNRIC() {
        return jointSingaporeNRIC;
    }

    public void setJointSingaporeNRIC(String jointSingaporeNRIC) {
        this.jointSingaporeNRIC = jointSingaporeNRIC;
    }

    public String getJointForeignNRIC() {
        return jointForeignNRIC;
    }

    public void setJointForeignNRIC(String jointForeignNRIC) {
        this.jointForeignNRIC = jointForeignNRIC;
    }

    public String getJointForeignPassport() {
        return jointForeignPassport;
    }

    public void setJointForeignPassport(String jointForeignPassport) {
        this.jointForeignPassport = jointForeignPassport;
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

    public String getJointStreetName() {
        return jointStreetName;
    }

    public void setJointStreetName(String jointStreetName) {
        this.jointStreetName = jointStreetName;
    }

    public String getJointBlockNum() {
        return jointBlockNum;
    }

    public void setJointBlockNum(String jointBlockNum) {
        this.jointBlockNum = jointBlockNum;
    }

    public String getJointUnitNum() {
        return jointUnitNum;
    }

    public void setJointUnitNum(String jointUnitNum) {
        this.jointUnitNum = jointUnitNum;
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

    public String getJointIndustryTypeOthers() {
        return jointIndustryTypeOthers;
    }

    public void setJointIndustryTypeOthers(String jointIndustryTypeOthers) {
        this.jointIndustryTypeOthers = jointIndustryTypeOthers;
    }

    public String getJointCurrentPosition() {
        return jointCurrentPosition;
    }

    public void setJointCurrentPosition(String jointCurrentPosition) {
        this.jointCurrentPosition = jointCurrentPosition;
    }

    public String getJointCurrentPositionOthers() {
        return jointCurrentPositionOthers;
    }

    public void setJointCurrentPositionOthers(String jointCurrentPositionOthers) {
        this.jointCurrentPositionOthers = jointCurrentPositionOthers;
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

    public BigDecimal getJointMonthlyFixedIncome() {
        return jointMonthlyFixedIncome;
    }

    public void setJointMonthlyFixedIncome(BigDecimal jointMonthlyFixedIncome) {
        this.jointMonthlyFixedIncome = jointMonthlyFixedIncome;
    }

    public BigDecimal getJointOtherMonthlyIncome() {
        return jointOtherMonthlyIncome;
    }

    public void setJointOtherMonthlyIncome(BigDecimal jointOtherMonthlyIncome) {
        this.jointOtherMonthlyIncome = jointOtherMonthlyIncome;
    }

    public String getJointOtherMonthlyIncomeSource() {
        return jointOtherMonthlyIncomeSource;
    }

    public void setJointOtherMonthlyIncomeSource(String jointOtherMonthlyIncomeSource) {
        this.jointOtherMonthlyIncomeSource = jointOtherMonthlyIncomeSource;
    }

    public String getJointSignature() {
        return jointSignature;
    }

    public void setJointSignature(String jointSignature) {
        this.jointSignature = jointSignature;
    }

    public boolean isJointSalutationPanelVisible() {
        return jointSalutationPanelVisible;
    }

    public void setJointSalutationPanelVisible(boolean jointSalutationPanelVisible) {
        this.jointSalutationPanelVisible = jointSalutationPanelVisible;
    }

    public boolean isJointNationalitySGPanelVisible() {
        return jointNationalitySGPanelVisible;
    }

    public void setJointNationalitySGPanelVisible(boolean jointNationalitySGPanelVisible) {
        this.jointNationalitySGPanelVisible = jointNationalitySGPanelVisible;
    }

    public boolean isJointNationalityOthersPanelVisible() {
        return jointNationalityOthersPanelVisible;
    }

    public void setJointNationalityOthersPanelVisible(boolean jointNationalityOthersPanelVisible) {
        this.jointNationalityOthersPanelVisible = jointNationalityOthersPanelVisible;
    }

    public boolean isJointNricPanelVisible() {
        return jointNricPanelVisible;
    }

    public void setJointNricPanelVisible(boolean jointNricPanelVisible) {
        this.jointNricPanelVisible = jointNricPanelVisible;
    }

    public boolean isJointPassportPanelVisible() {
        return jointPassportPanelVisible;
    }

    public void setJointPassportPanelVisible(boolean jointPassportPanelVisible) {
        this.jointPassportPanelVisible = jointPassportPanelVisible;
    }

    public boolean isJointIndustryTypePanelVisible() {
        return jointIndustryTypePanelVisible;
    }

    public void setJointIndustryTypePanelVisible(boolean jointIndustryTypePanelVisible) {
        this.jointIndustryTypePanelVisible = jointIndustryTypePanelVisible;
    }

    public boolean isJointCurrentPositionPanelVisible() {
        return jointCurrentPositionPanelVisible;
    }

    public void setJointCurrentPositionPanelVisible(boolean jointCurrentPositionPanelVisible) {
        this.jointCurrentPositionPanelVisible = jointCurrentPositionPanelVisible;
    }

    public boolean isJointOccupationPanelVisible() {
        return jointOccupationPanelVisible;
    }

    public void setJointOccupationPanelVisible(boolean jointOccupationPanelVisible) {
        this.jointOccupationPanelVisible = jointOccupationPanelVisible;
    }

    public boolean isJointEmploymentPanelVisible() {
        return jointEmploymentPanelVisible;
    }

    public void setJointEmploymentPanelVisible(boolean jointEmploymentPanelVisible) {
        this.jointEmploymentPanelVisible = jointEmploymentPanelVisible;
    }

    public ArrayList<HashMap> getJointFinancialCommitments() {
        return jointFinancialCommitments;
    }

    public void setJointFinancialCommitments(ArrayList<HashMap> jointFinancialCommitments) {
        this.jointFinancialCommitments = jointFinancialCommitments;
    }

    public String getJointFacilityType() {
        return jointFacilityType;
    }

    public void setJointFacilityType(String jointFacilityType) {
        this.jointFacilityType = jointFacilityType;
    }

    public String getJointFinancialInstitution() {
        return jointFinancialInstitution;
    }

    public void setJointFinancialInstitution(String jointFinancialInstitution) {
        this.jointFinancialInstitution = jointFinancialInstitution;
    }

    public BigDecimal getJointLoanAmount() {
        return jointLoanAmount;
    }

    public void setJointLoanAmount(BigDecimal jointLoanAmount) {
        this.jointLoanAmount = jointLoanAmount;
    }

    public BigDecimal getJointMonthlyInstalment() {
        return jointMonthlyInstalment;
    }

    public void setJointMonthlyInstalment(BigDecimal jointMonthlyInstalment) {
        this.jointMonthlyInstalment = jointMonthlyInstalment;
    }

    public HashMap getJointFinancialCommitment() {
        return jointFinancialCommitment;
    }

    public void setJointFinancialCommitment(HashMap jointFinancialCommitment) {
        this.jointFinancialCommitment = jointFinancialCommitment;
    }

    public String getCustomerCollateralDetails() {
        return customerCollateralDetails;
    }

    public void setCustomerCollateralDetails(String customerCollateralDetails) {
        this.customerCollateralDetails = customerCollateralDetails;
    }

    public Integer getCustomerRemainingTenure() {
        return customerRemainingTenure;
    }

    public void setCustomerRemainingTenure(Integer customerRemainingTenure) {
        this.customerRemainingTenure = customerRemainingTenure;
    }

    public BigDecimal getCustomerCurrentInterest() {
        return customerCurrentInterest;
    }

    public void setCustomerCurrentInterest(BigDecimal customerCurrentInterest) {
        this.customerCurrentInterest = customerCurrentInterest;
    }

    public String getJointCollateralDetails() {
        return jointCollateralDetails;
    }

    public void setJointCollateralDetails(String jointCollateralDetails) {
        this.jointCollateralDetails = jointCollateralDetails;
    }

    public Integer getJointRemainingTenure() {
        return jointRemainingTenure;
    }

    public void setJointRemainingTenure(Integer jointRemainingTenure) {
        this.jointRemainingTenure = jointRemainingTenure;
    }

    public BigDecimal getJointCurrentInterest() {
        return jointCurrentInterest;
    }

    public void setJointCurrentInterest(BigDecimal jointCurrentInterest) {
        this.jointCurrentInterest = jointCurrentInterest;
    }

    public String getJointRelationship() {
        return jointRelationship;
    }

    public void setJointRelationship(String jointRelationship) {
        this.jointRelationship = jointRelationship;
    }

}
