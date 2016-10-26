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
import java.io.OutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.apache.commons.io.IOUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.FlowEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author hanfengwei
 */
@Named(value = "publicHDBLoanApplication")
@ViewScoped
public class PublicHDBLoanApplicationManagedBean implements Serializable {

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

    /**
     * Creates a new instance of PublicHDBLoanApplication
     */
    public PublicHDBLoanApplicationManagedBean() {
        uploads.put("identification", false);
        uploads.put("otp", false);
        uploads.put("purchaseAgreement", false);
        uploads.put("existingLoan", false);
        uploads.put("cpfWithdrawal", false);
        uploads.put("evidenceOfSale", false);
        uploads.put("tenancy", false);
        uploads.put("employeeTax", false);
        uploads.put("employeeCPF", false);
        uploads.put("selfEmployedTax", false);
    }

    public void addLoanApplicationFast() throws IOException {
        Long newCustomerBasicId = cRMCustomerSessionBeanLocal.addNewCustomerBasic("Han Fengwei",
                "Ms", "G1320505T",
                "Female", "hanfengwei96@gmail.com", "83114121", "07/Mar/1996",
                "China", "China", "Chinese",
                "Single", "Student", "NUS",
                "customer address", "118425", null, null, null, "Yes");
        Long newCustomerAdvancedId = cRMCustomerSessionBeanLocal.addNewCustomerAdvanced(3, "Degree", "Rented",
                3, "Government", 5, "Employee",
                10000, "HDB", "company address",
                "118426", "Senior Management", "CEO",
                "NTU", 3, 2000,
                "other income source");

        ArrayList<CustomerDebt> debts = new ArrayList<CustomerDebt>();
        debts.add(loanApplicationSessionBeanLocal.addNewCustomerDebt("car loan", "DBS", 500000, 1000));
        debts.add(loanApplicationSessionBeanLocal.addNewCustomerDebt("HDB loan", "UOB", 800000, 2000));

        customerPropertyOwners.add("Han Fengwei");
        customerPropertyOwners.add("Dai Hailang");
        CustomerProperty cp = new CustomerProperty();
        cp.create("property address", "118427", customerPropertyOwners, "3-Room",
                170.8, 200, "Completed",
                null, "Owner Occupation", "Leasehold",
                99, 2012, null);
        MortgageLoanApplication mortgage = new MortgageLoanApplication();
        mortgage.create("HDB - New Purchase", 500000, 20, 550000,
                new Date(), "Developer/HDB", "no", null, "yes",
                5000, 2012, "yes", 30000,
                20000, 30000, "Employee");
        loanApplicationSessionBeanLocal.submitLoanApplication(newCustomerBasicId, newCustomerAdvancedId, debts, cp, mortgage, null, "purchase", "HDB-Fixed");
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(ec.getRequestContextPath() + "/web/merlionBank/loan/publicMortgageLoanApplicationDone.xhtml?faces-redirect=true");
    }

    public void addLoanApplication() throws IOException {
        System.out.println("====== loan/PublicHDBLoanApplicationManagedBean: addLoanApplication() ======");
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        customerSignature = ec.getSessionMap().get("customerSignature").toString();
        if (customerSignature.equals("") || !agreement) {
            if (customerSignature.equals("")) {
                FacesContext.getCurrentInstance().addMessage("input", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed! Please provide your digital signature", "Failed!"));
            }
            if (!agreement) {
                FacesContext.getCurrentInstance().addMessage("agreement", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed! Please agree to terms to proceed", "Failed!"));
            }
        } else {
            //create CustomerBasic
            String newCustomer = "Yes";
            if (customerSalutation.equals("Others")) {
                customerSalutation = customerSalutationOthers;
            }

            String dateOfBirth = changeDateFormat(customerDateOfBirth);
            String customerAddress = customerStreetName + ", " + customerBlockNum + ", " + customerUnitNum + ", " + customerPostal;
            Long newCustomerBasicId = cRMCustomerSessionBeanLocal.addNewCustomerBasic(customerName,
                    customerSalutation, customerIdentificationNum.toUpperCase(),
                    customerGender, customerEmail, customerMobile, dateOfBirth,
                    customerNationality, customerCountryOfResidence, customerRace,
                    customerMaritalStatus, customerOccupation, customerCompanyName,
                    customerAddress, customerPostal, null, null, customerSignature.getBytes(), newCustomer);

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
            if (customerEmploymentStatus.equals("Unemployed")) {
                newCustomerAdvancedId = cRMCustomerSessionBeanLocal.addNewCustomerAdvanced(customerNumOfDependents, customerEducation, customerResidentialStatus,
                        customerLengthOfResidence, null, 0, customerEmploymentStatus,
                        customerMonthlyFixedIncome.doubleValue(), customerResidentialType, null,
                        null, null, null,
                        null, 0, customerOtherMonthlyIncome.doubleValue(),
                        customerOtherMonthlyIncomeSource);
            } else {
                newCustomerAdvancedId = cRMCustomerSessionBeanLocal.addNewCustomerAdvanced(customerNumOfDependents, customerEducation, customerResidentialStatus,
                        customerLengthOfResidence, customerIndustryType, customerLengthOfCurrentJob, customerEmploymentStatus,
                        customerMonthlyFixedIncome.doubleValue(), customerResidentialType, customerCompanyAddress,
                        customerCompanyPostal, customerCurrentPosition, customerCurrentJobTitle,
                        customerPreviousCompany, customerLengthOfPreviousJob, customerOtherMonthlyIncome.doubleValue(),
                        customerOtherMonthlyIncomeSource);
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
                debts.add(loanApplicationSessionBeanLocal.addNewCustomerDebt(facilityType, financialInstitution,
                        totalAmount, instalment));
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
                mortgage.create("HDB - New Purchase", customerLoanAmountRequired.doubleValue(), customerLoanTenure, customerPropertyPurchasePrice.doubleValue(),
                        customerPropertyDateOfPurchase, customerPropertySource, customerPropertyWithOTP, customerPropertyOTPDate, customerPropertyWithTenancy,
                        customerPropertyTenancyIncome.doubleValue(), customerPropertyTenancyExpiryYear, customerWithBenefitsFromVendor, customerBenefitsFromVendor.doubleValue(),
                        customerCashDownpayment.doubleValue(), customerCPFDownpayment.doubleValue(), customerEmploymentStatus);

                loanApplicationSessionBeanLocal.submitLoanApplication(newCustomerBasicId, newCustomerAdvancedId, debts, cp, mortgage, null, customerFinancialRequest, interestPackage);
                ec.getFlash().put("loanType", "HDB - New Purchase");

            } else {
                RefinancingApplication refinancing = new RefinancingApplication();
                refinancing.create("HDB - Refinancing", customerLoanAmountRequired.doubleValue(), customerLoanTenure, customerExistingFinancer,
                        customerOutstandingLoan.doubleValue(), customerOutstandingYear, customerOutstandingMonth, customerTotalCPFWithdrawal.doubleValue(), customerEmploymentStatus);

                loanApplicationSessionBeanLocal.submitLoanApplication(newCustomerBasicId, newCustomerAdvancedId, debts, cp, null, refinancing, customerFinancialRequest, interestPackage);
                ec.getFlash().put("loanType", "HDB - Refinancing");
            }

            HashMap emailActions = new HashMap();
            emailActions.put("loanType", "HDB");
            emailActions.put("request", customerFinancialRequest);
            customerEmailSessionBeanLocal.sendEmail(cRMCustomerSessionBeanLocal.getCustomerBasicById(newCustomerBasicId), "mortgageLoanApplication", emailActions);
            ec.getFlash().put("amountRequired", customerLoanAmountRequired);
            ec.getFlash().put("tenure", customerLoanTenure);
            ec.redirect(ec.getRequestContextPath() + "/web/merlionBank/loan/publicMortgageLoanApplicationDone.xhtml?faces-redirect=true");
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

            if (age < 16 || age > 65) {
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
        if (customerFinancialInstitution == null || customerFacilityType == null || customerLoanAmount == null || customerMonthlyInstalment == null) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please fill in all the fields", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        } else {
            customerFinancialCommitment.put("institution", customerFinancialInstitution);
            customerFinancialCommitment.put("type", customerFacilityType);
            customerFinancialCommitment.put("amount", customerLoanAmount);
            customerFinancialCommitment.put("instalment", customerMonthlyInstalment);
            customerFinancialCommitments.add(customerFinancialCommitment);
            customerFinancialInstitution = null;
            customerFacilityType = null;
            customerLoanAmount = null;
            customerMonthlyInstalment = null;
            customerFinancialCommitment = new HashMap();
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

    public void identificationUpload(FileUploadEvent event) throws FileNotFoundException, IOException {
        this.file = event.getFile();
        if (file != null) {
            String filename = customerIdentificationNum + ".pdf";
            InputStream input = file.getInputstream();
            OutputStream output = new FileOutputStream(new File("/Users/hanfengwei/Desktop/customerDocuments", filename));
            try {
                IOUtils.copy(input, output);
            } finally {
                IOUtils.closeQuietly(input);
                IOUtils.closeQuietly(output);
            }
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
            String filename = customerIdentificationNum + "-otp.pdf";
            InputStream input = file.getInputstream();
            OutputStream output = new FileOutputStream(new File("/Users/hanfengwei/Desktop/customerDocuments", filename));
            try {
                IOUtils.copy(input, output);
            } finally {
                IOUtils.closeQuietly(input);
                IOUtils.closeQuietly(output);
            }
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
            String filename = customerIdentificationNum + "-purchase_agreement.pdf";
            InputStream input = file.getInputstream();
            OutputStream output = new FileOutputStream(new File("/Users/hanfengwei/Desktop/customerDocuments", filename));
            try {
                IOUtils.copy(input, output);
            } finally {
                IOUtils.closeQuietly(input);
                IOUtils.closeQuietly(output);
            }
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
            String filename = customerIdentificationNum + "-existing_loan.pdf";
            InputStream input = file.getInputstream();
            OutputStream output = new FileOutputStream(new File("/Users/hanfengwei/Desktop/customerDocuments", filename));
            try {
                IOUtils.copy(input, output);
            } finally {
                IOUtils.closeQuietly(input);
                IOUtils.closeQuietly(output);
            }
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
            String filename = customerIdentificationNum + "-cpf_withdrawal.pdf";
            InputStream input = file.getInputstream();
            OutputStream output = new FileOutputStream(new File("/Users/hanfengwei/Desktop/customerDocuments", filename));
            try {
                IOUtils.copy(input, output);
            } finally {
                IOUtils.closeQuietly(input);
                IOUtils.closeQuietly(output);
            }
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
            String filename = customerIdentificationNum + "-evidence_of_sale.pdf";
            InputStream input = file.getInputstream();
            OutputStream output = new FileOutputStream(new File("/Users/hanfengwei/Desktop/customerDocuments", filename));
            try {
                IOUtils.copy(input, output);
            } finally {
                IOUtils.closeQuietly(input);
                IOUtils.closeQuietly(output);
            }
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
            String filename = customerIdentificationNum + "-tenancy.pdf";
            InputStream input = file.getInputstream();
            OutputStream output = new FileOutputStream(new File("/Users/hanfengwei/Desktop/customerDocuments", filename));
            try {
                IOUtils.copy(input, output);
            } finally {
                IOUtils.closeQuietly(input);
                IOUtils.closeQuietly(output);
            }
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
            String filename = customerIdentificationNum + "-employee_tax.pdf";
            InputStream input = file.getInputstream();
            OutputStream output = new FileOutputStream(new File("/Users/hanfengwei/Desktop/customerDocuments", filename));
            try {
                IOUtils.copy(input, output);
            } finally {
                IOUtils.closeQuietly(input);
                IOUtils.closeQuietly(output);
            }
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
            String filename = customerIdentificationNum + "-employee_cpf.pdf";
            InputStream input = file.getInputstream();
            OutputStream output = new FileOutputStream(new File("/Users/hanfengwei/Desktop/customerDocuments", filename));
            try {
                IOUtils.copy(input, output);
            } finally {
                IOUtils.closeQuietly(input);
                IOUtils.closeQuietly(output);
            }
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
            String filename = customerIdentificationNum + "-self-employed_tax.pdf";
            InputStream input = file.getInputstream();
            OutputStream output = new FileOutputStream(new File("/Users/hanfengwei/Desktop/customerDocuments", filename));
            try {
                IOUtils.copy(input, output);
            } finally {
                IOUtils.closeQuietly(input);
                IOUtils.closeQuietly(output);
            }
            uploads.replace("selfEmployedTax", true);
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, file.getFileName() + " uploaded successfully.", "");
            FacesContext.getCurrentInstance().addMessage("selfEmployedTaxUpload", message);
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Cannot find the file, please upload again.", "");
            FacesContext.getCurrentInstance().addMessage("selfEmployedTaxUpload", message);
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
        propertyOTPPanelVisible = customerPropertyWithOTP.equals("yes");
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
            uploads.replace("employeeTax", true);
            uploads.replace("employeeCPF", true);
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
}