/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.loan.customer;

import ejb.customer.session.CRMCustomerSessionBean;
import ejb.infrastructure.session.CustomerEmailSessionBeanLocal;
import ejb.loan.entity.EducationLoanApplication;
import ejb.loan.session.LoanApplicationSessionBeanLocal;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
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
@Named(value = "publicEducationLoanApplicationManagedBean")
@ViewScoped
public class PublicEducationLoanApplicationManagedBean implements Serializable {

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

    //customer - loan both
    private BigDecimal customerLoanAmountRequired;
    private Integer customerLoanTenure;
    private String customerEducationInstitution;
    private Date customerEducationStarton;
    private Date customerEducationEndon;
    private Integer customerCourseDuration;
    private Integer customerCourseFee;

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

    /**
     * Creates a new instance of PublicEducationLoanApplicationManagedBean
     */
    public PublicEducationLoanApplicationManagedBean() {
        uploads.put("identification", false);
        uploads.put("acceptance", false);
        uploads.put("invoice", false);
        uploads.put("employeeTax", false);
        uploads.put("selfEmployedTax", false);
    }

    public void addEducationLoanApplicationFast() throws IOException {
        System.out.println("====== loan/PublicEducationLoanApplicationManagedBean: addEducationLoanApplication() ======");
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        //create or update CustomerBasic

        Long newCustomerBasicId;

        boolean isExistingCustomer = cRMCustomerSessionBeanLocal.checkExistingCustomerByIdentification("E11223344");
        if (isExistingCustomer) {
            newCustomerBasicId = cRMCustomerSessionBeanLocal.updateCustomerBasic("E11223344", "hanfengwei96@gmail.com", "83114121",
                    "China", "China", "Single", "Student", "AfterYou",
                    "Zhou Jingyuan", "123123");
        } else {
            newCustomerBasicId = cRMCustomerSessionBeanLocal.addNewCustomerBasic("Zhou Jingyuan",
                    "Ms", "E11223344",
                    "Female", "hanfengwei96@gmail.com", "83114121", "08/Mar/1993",
                    "China", "China", "Chinese",
                    "Single", "Student", "AfterYou",
                    "address", "123123", null, null, null, null);
        }

        //create CustomerAdvanced
        Long newCustomerAdvancedId;
        boolean hasCustomerAdvanced = cRMCustomerSessionBeanLocal.checkExistingCustomerAdvanced("E11223344");
        if (hasCustomerAdvanced) {
            newCustomerAdvancedId = cRMCustomerSessionBeanLocal.updateCustomerAdvanced("E11223344", 2,
                    "Degree", "Rented", 5, "Government", 6,
                    "Employee", 3000, "HDB", "company address",
                    "123123", "Senior Management", "job title", null,
                    0, 2000, "income source");
        } else {
            newCustomerAdvancedId = cRMCustomerSessionBeanLocal.addNewCustomerAdvanced(5, "Degree", "Rented",
                    5, "Government", 6, "Employee",
                    3000, "HDB", "company address",
                    "123123", "Senior Management", "job title",
                    null, 0, 2000,
                    "income source");
        }

        //create education loan application
        EducationLoanApplication application = new EducationLoanApplication();
        DateFormat df = new SimpleDateFormat("dd/MMM/yyyy");
        Date starton = new Date();
        Date endon = new Date();
        try {
            starton = df.parse("16/Jan/2016");
            endon = df.parse("16/Jan/2020");
        } catch (ParseException ex) {
            Logger.getLogger(PublicEducationLoanApplicationManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        application.create(30000, 5, "Stockholm University", starton, endon, 4, 25000, "Employee");
        loanApplicationSessionBeanLocal.submitEducationLoanApplication(isExistingCustomer, hasCustomerAdvanced, application, newCustomerBasicId, newCustomerAdvancedId);

        //second loan applicaiton
        isExistingCustomer = cRMCustomerSessionBeanLocal.checkExistingCustomerByIdentification("G11223344");
        if (isExistingCustomer) {
            newCustomerBasicId = cRMCustomerSessionBeanLocal.updateCustomerBasic("G11223344", "hanfengwei96@gmail.com", "83114121",
                    "China", "China", "Single", "Student", "AfterYou",
                    "Han Fengwei", "123123");
        } else {
            newCustomerBasicId = cRMCustomerSessionBeanLocal.addNewCustomerBasic("Han Fengwei",
                    "Ms", "G11223344",
                    "Female", "hanfengwei96@gmail.com", "83114121", "08/Mar/1993",
                    "China", "China", "Chinese",
                    "Single", "Student", "AfterYou",
                    "address", "123123", null, null, null, null);
        }

        //create CustomerAdvanced
        hasCustomerAdvanced = cRMCustomerSessionBeanLocal.checkExistingCustomerAdvanced("G11223344");
        if (hasCustomerAdvanced) {
            newCustomerAdvancedId = cRMCustomerSessionBeanLocal.updateCustomerAdvanced("G11223344", 2,
                    "Degree", "Rented", 5, "Government", 6,
                    "Employee", 3000, "HDB", "company address",
                    "123123", "Senior Management", "job title", null,
                    0, 2000, "income source");
        } else {
            newCustomerAdvancedId = cRMCustomerSessionBeanLocal.addNewCustomerAdvanced(5, "Degree", "Rented",
                    5, "Government", 6, "Employee",
                    3000, "HDB", "company address",
                    "123123", "Senior Management", "job title",
                    null, 0, 2000,
                    "income source");
        }

        //create education loan application
        application = new EducationLoanApplication();
        try {
            starton = df.parse("01/May/2015");
            endon = df.parse("01/May/2019");
        } catch (ParseException ex) {
            Logger.getLogger(PublicEducationLoanApplicationManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        application.create(20000, 6, "Peking University", starton, endon, 4, 20000, "Employee");
        loanApplicationSessionBeanLocal.submitEducationLoanApplication(isExistingCustomer, hasCustomerAdvanced, application, newCustomerBasicId, newCustomerAdvancedId);
        
        ec.getFlash().put("amountRequired", BigDecimal.valueOf(30000));
        ec.getFlash().put("loanType", "Education Loan");
        ec.getFlash().put("tenure", 5);
        ec.redirect(ec.getRequestContextPath() + "/web/merlionBank/loan/publicLoanApplicationDone.xhtml?faces-redirect=true");
    }

    public void addEducationLoanApplication() throws IOException {
        System.out.println("====== loan/PublicEducationLoanApplicationManagedBean: addEducationLoanApplication() ======");
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
                            customerCompanyPostal, customerCurrentPosition, customerCurrentJobTitle, null,
                            0, customerOtherMonthlyIncome.doubleValue(), customerOtherMonthlyIncomeSource);
                } else {
                    newCustomerAdvancedId = cRMCustomerSessionBeanLocal.addNewCustomerAdvanced(customerNumOfDependents, customerEducation, customerResidentialStatus,
                            customerLengthOfResidence, customerIndustryType, customerLengthOfCurrentJob, customerEmploymentStatus,
                            customerMonthlyFixedIncome.doubleValue(), customerResidentialType, customerCompanyAddress,
                            customerCompanyPostal, customerCurrentPosition, customerCurrentJobTitle,
                            null, 0, customerOtherMonthlyIncome.doubleValue(),
                            customerOtherMonthlyIncomeSource);
                }
            }

            //create education loan application
            EducationLoanApplication application = new EducationLoanApplication();
            application.create(customerLoanAmountRequired.doubleValue(), customerLoanTenure, customerEducationInstitution, customerEducationStarton, customerEducationEndon, customerCourseDuration, customerCourseFee, customerEmploymentStatus);
            loanApplicationSessionBeanLocal.submitEducationLoanApplication(isExistingCustomer, hasCustomerAdvanced, application, newCustomerBasicId, newCustomerAdvancedId);
            customerEmailSessionBeanLocal.sendEmail(cRMCustomerSessionBeanLocal.getCustomerBasicById(newCustomerBasicId), "educationLoanApplication", null);
            ec.getFlash().put("amountRequired", customerLoanAmountRequired);
            ec.getFlash().put("loanType", "Education Loan");
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
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Eligibility: Singapore Citizen or Singapore Permanent Resident", "");
                FacesContext.getCurrentInstance().addMessage(null, message);
                nextStep = event.getOldStep();
            }

            if (age < 17 || age > 65) {
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
            if (grossIncome <= 1500) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "The minimum gross monthly income required is S$1,500", "");
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

    public void showSalutationPanel() {
        salutationPanelVisible = customerSalutation.equals("Others");
    }

    public void showNationalityPanel() {
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

    public void changeEmployeeStatus() {
        if (customerEmploymentStatus.equals("Employee")) {
            employmentPanelVisible = true;
            occupationPanelVisible = true;
            uploads.replace("selfEmployedTax", true);
            uploads.replace("employeeTax", false);
        } else if (customerEmploymentStatus.equals("Self-Employed")) {
            employmentPanelVisible = true;
            occupationPanelVisible = false;
            uploads.replace("selfEmployedTax", false);
            uploads.replace("employeeTax", true);
        } else {
            occupationPanelVisible = false;
            employmentPanelVisible = false;
            uploads.replace("selfEmployedTax", true);
            uploads.replace("employeeTax", false);
        }
    }

    private String changeDateFormat(Date inputDate) {
        String dateString = inputDate.toString();
        String[] dateSplit = dateString.split(" ");
        String outputDate = dateSplit[2] + "/" + dateSplit[1] + "/" + dateSplit[5];
        return outputDate;
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

    public void acceptanceLetterUpload(FileUploadEvent event) throws FileNotFoundException, IOException {
        this.file = event.getFile();
        if (file != null) {
            String newFilePath = System.getProperty("user.dir").replace("config", "docroot") + System.getProperty("file.separator");

            String filename = customerIdentificationNum + "-acceptance.pdf";
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

            uploads.replace("acceptance", true);
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, file.getFileName() + " uploaded successfully.", "");
            FacesContext.getCurrentInstance().addMessage("acceptanceLetterUpload", message);
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Cannot find the file, please upload again.", "");
            FacesContext.getCurrentInstance().addMessage("acceptanceLetterUpload", message);
        }
    }

    public void courseInvoiceUpload(FileUploadEvent event) throws FileNotFoundException, IOException {
        this.file = event.getFile();
        if (file != null) {
            String newFilePath = System.getProperty("user.dir").replace("config", "docroot") + System.getProperty("file.separator");

            String filename = customerIdentificationNum + "-invoice.pdf";
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

            uploads.replace("invoice", true);
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, file.getFileName() + " uploaded successfully.", "");
            FacesContext.getCurrentInstance().addMessage("courseInvoiceUpload", message);
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Cannot find the file, please upload again.", "");
            FacesContext.getCurrentInstance().addMessage("courseInvoiceUpload", message);
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

    public String getCustomerSalutation() {
        return customerSalutation;
    }

    public void setCustomerSalutation(String customerSalutation) {
        this.customerSalutation = customerSalutation;
    }

    public String getCustomerSalutationOthers() {
        return customerSalutationOthers;
    }

    public void setCustomerSalutationOthers(String customerSalutationOthers) {
        this.customerSalutationOthers = customerSalutationOthers;
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

    public String getCustomerNationality() {
        return customerNationality;
    }

    public void setCustomerNationality(String customerNationality) {
        this.customerNationality = customerNationality;
    }

    public String getCustomerIsPR() {
        return customerIsPR;
    }

    public void setCustomerIsPR(String customerIsPR) {
        this.customerIsPR = customerIsPR;
    }

    public String getCustomerIdentificationNum() {
        return customerIdentificationNum;
    }

    public void setCustomerIdentificationNum(String customerIdentificationNum) {
        this.customerIdentificationNum = customerIdentificationNum;
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

    public String getCustomerCurrentPositionOthers() {
        return customerCurrentPositionOthers;
    }

    public void setCustomerCurrentPositionOthers(String customerCurrentPositionOthers) {
        this.customerCurrentPositionOthers = customerCurrentPositionOthers;
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

    public String getCustomerOtherMonthlyIncomeSource() {
        return customerOtherMonthlyIncomeSource;
    }

    public void setCustomerOtherMonthlyIncomeSource(String customerOtherMonthlyIncomeSource) {
        this.customerOtherMonthlyIncomeSource = customerOtherMonthlyIncomeSource;
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

    public boolean isAgreement() {
        return agreement;
    }

    public void setAgreement(boolean agreement) {
        this.agreement = agreement;
    }

    public String getCustomerSignature() {
        return customerSignature;
    }

    public void setCustomerSignature(String customerSignature) {
        this.customerSignature = customerSignature;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public HashMap getUploads() {
        return uploads;
    }

    public void setUploads(HashMap uploads) {
        this.uploads = uploads;
    }

    public boolean isSalutationPanelVisible() {
        return salutationPanelVisible;
    }

    public void setSalutationPanelVisible(boolean salutationPanelVisible) {
        this.salutationPanelVisible = salutationPanelVisible;
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

}
