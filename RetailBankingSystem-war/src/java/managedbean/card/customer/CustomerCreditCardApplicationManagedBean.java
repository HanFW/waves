/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.card.customer;

import ejb.card.session.CreditCardSessionBeanLocal;
import ejb.customer.entity.CustomerBasic;
import ejb.customer.session.CRMCustomerSessionBeanLocal;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
 * @author aaa
 */
@Named(value = "customerCreditCardApplicationManagedBean")
@ViewScoped
public class CustomerCreditCardApplicationManagedBean implements Serializable {

    @EJB
    private CRMCustomerSessionBeanLocal customerSessionBeanLocal;

    @EJB
    private CreditCardSessionBeanLocal creditCardSessionLocal;

    //basic information
    private String customerSalutation;
    private String customerSalutationOthers;
    private String customerName;
    private Date customerDateOfBirth;
    private String customerGender;
    private String customerNationality;
    private String customerIsPR;
    private String customerIdentificationNum;
    private String customerCountryOfResidence;
    private String customerRace;
    private String customerMobile;
    private String customerEmail;
    //different identity 
    private String sgId;
    private String prId;
    private String passportId;

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
    private String customerCompanyName;
    private String customerCompanyAddress;
    private String customerCompanyPostal;
    private String customerIndustryType;
    private String customerIndustryTypeOthers;
    private String customerCurrentPosition;
    private String customerCurrentPositionOthers;
    private String customerCurrentJobTitle;
    private Integer customerLengthOfCurrentJob;
    private BigDecimal customerMonthlyFixedIncome;

    //credit card details
    private BigDecimal creditLimit;
    private String hasCreditLimit;
    private String creditCardTypeName;
    private Long creditCardTypeId;
    private String cardHolderName;

    //basic information
    private boolean salutationPanelVisible;
    private boolean nationalitySGPanelVisible;
    private boolean nationalityOthersPanelVisible;
    private boolean nricPanelVisible;
    private boolean passportPanelVisible;

    //personal details
    private boolean industryTypePanelVisible;
    private boolean currentPositionPanelVisible;
    private boolean creditLimitPanelVisible;

    //employement
//    private boolean occupationPanelVisible;
    private boolean employmentPanelVisible;

    //confirmation
    private boolean agreement;
    private String customerSignature;

    //documents
    private UploadedFile file;
    private HashMap uploads = new HashMap();


    public CustomerCreditCardApplicationManagedBean() {
        uploads.put("identification", false);
        uploads.put("income", false);
        uploads.put("workpass", true);
    }

    @PostConstruct
    public void init() {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        customerIdentificationNum = ec.getSessionMap().get("customerIdentificationNum").toString();
        CustomerBasic customer = customerSessionBeanLocal.retrieveCustomerBasicByIC(customerIdentificationNum);
        customerNationality = customer.getCustomerNationality();

        showNationalityPanel();
    }

    public String onFlowProcess(FlowEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        FacesMessage message = null;

        if (event.getOldStep().equals("employment")) {
            if (customerNationality.equals("Singapore")) {
                if (customerMonthlyFixedIncome.doubleValue() * 12 < 30000) {
                    message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Singaporean must earn at least $30,000 annually to apply credit card", null);
                    context.addMessage(null, message);
                    return event.getOldStep();
                }
            } else {
                if (customerMonthlyFixedIncome.doubleValue() * 12 < 45000) {
                    message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Foreigner must earn at least $45,000 annually to apply credit card", null);
                    context.addMessage(null, message);
                    return event.getOldStep();
                }
            }
        }
        if (event.getOldStep().equals("documents")) {
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
                message = new FacesMessage(FacesMessage.SEVERITY_ERROR, pendingDocs, "");
                FacesContext.getCurrentInstance().addMessage(null, message);
                return event.getOldStep();
            }
        }

        return event.getNewStep();

    }

    public void identificationUpload(FileUploadEvent event) throws FileNotFoundException, IOException {
        this.file = event.getFile();
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        CustomerBasic customer = (CustomerBasic) ec.getSessionMap().get("customer");
        customerIdentificationNum = customer.getCustomerIdentificationNum();
        if (file != null) {
            String newFilePath = System.getProperty("user.dir").replace("config", "docroot") + System.getProperty("file.separator");

            System.out.println("@@@@@@@@@@@@@@@@@@ID num " + customerIdentificationNum);
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

    public void incomeUpload(FileUploadEvent event) throws FileNotFoundException, IOException {
        this.file = event.getFile();
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        CustomerBasic customer = (CustomerBasic) ec.getSessionMap().get("customer");
        customerIdentificationNum = customer.getCustomerIdentificationNum();
        if (file != null) {
            String newFilePath = System.getProperty("user.dir").replace("config", "docroot") + System.getProperty("file.separator");

            String filename = customerIdentificationNum + "-income.pdf";
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

            uploads.replace("income", true);
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, file.getFileName() + " uploaded successfully.", "");
            FacesContext.getCurrentInstance().addMessage("incomeUpload", message);
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Cannot find the file, please upload again.", "");
            FacesContext.getCurrentInstance().addMessage("incomeUpload", message);
        }
    }

    public void workPassUpload(FileUploadEvent event) throws FileNotFoundException, IOException {
        this.file = event.getFile();
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        CustomerBasic customer = (CustomerBasic) ec.getSessionMap().get("customer");
        customerIdentificationNum = customer.getCustomerIdentificationNum();
        if (file != null) {
            String newFilePath = System.getProperty("user.dir").replace("config", "docroot") + System.getProperty("file.separator");

            String filename = customerIdentificationNum + "-work_pass.pdf";
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

            uploads.replace("workpass", true);
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, file.getFileName() + " uploaded successfully.", "");
            FacesContext.getCurrentInstance().addMessage("workPassUpload", message);
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Cannot find the file, please upload again.", "");
            FacesContext.getCurrentInstance().addMessage("workPassUpload", message);
        }
    }

    public void changeEmployeeStatus() {
        if (customerEmploymentStatus.equals("Employee")) {
            employmentPanelVisible = true;
        } else if (customerEmploymentStatus.equals("Self-Employed")) {
            employmentPanelVisible = true;
        } else {
            employmentPanelVisible = false;
        }
    }

    private String changeDateFormat(Date inputDate) {
        String dateString = inputDate.toString();
        String[] dateSplit = dateString.split(" ");
        String outputDate = dateSplit[2] + "/" + dateSplit[1] + "/" + dateSplit[5];
        return outputDate;
    }

    private int getAge(Date customerDateOfBirth) {
        Date now = new Date();
        int yearDif = now.getYear() - customerDateOfBirth.getYear();
        int monthDif = now.getMonth() - customerDateOfBirth.getMonth();
        int dayDif = now.getDate() - customerDateOfBirth.getDate();
        if (monthDif < 0) {
            yearDif--;
        }
        if (monthDif == 0) {
            if (dayDif < 0) {
                yearDif--;
            }
        }
        return yearDif;
    }

    public void showSalutationPanel() {
        if (customerSalutation.equals("Others")) {
            salutationPanelVisible = true;
        } else {
            salutationPanelVisible = false;
        }
    }

    public void showNationalityPanel() {
        customerIdentificationNum = null;
        customerIsPR = null;
        if (customerNationality.equals("Singapore")) {
            nationalitySGPanelVisible = true;
            nationalityOthersPanelVisible = false;
            nricPanelVisible = false;
            passportPanelVisible = false;
        } else {
            nationalityOthersPanelVisible = true;
            nationalitySGPanelVisible = false;
            nricPanelVisible = false;
            passportPanelVisible = true;
        }
    }

    public void showIdentificationPanel() {
        customerIdentificationNum = null;
        if (customerIsPR.equals("Yes")) {
            nricPanelVisible = true;
            passportPanelVisible = false;
            uploads.replace("workpass", true);
        } else {
            passportPanelVisible = true;
            nricPanelVisible = false;
            uploads.replace("workpass", false);
        }
    }

    public void showIndustryTypePanel() {
        industryTypePanelVisible = customerIndustryType.equals("Others");
    }

    public void showCurrentPositionPanel() {
        currentPositionPanelVisible = customerCurrentPosition.equals("Others");
    }

    public void showCreditLimitPanel() {
        creditLimitPanelVisible = hasCreditLimit.equals("Yes");
        if (creditLimitPanelVisible == false) {
            creditLimit = new BigDecimal("0");
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

    public boolean isCreditLimitPanelVisible() {
        return creditLimitPanelVisible;
    }

    public void setCreditLimitPanelVisible(boolean creditLimitPanelVisible) {
        this.creditLimitPanelVisible = creditLimitPanelVisible;
    }

    public String getHasCreditLimit() {
        return hasCreditLimit;
    }

    public void setHasCreditLimit(String hasCreditLimit) {
        this.hasCreditLimit = hasCreditLimit;
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

    public BigDecimal getCustomerMonthlyFixedIncome() {
        return customerMonthlyFixedIncome;
    }

    public void setCustomerMonthlyFixedIncome(BigDecimal customerMonthlyFixedIncome) {
        this.customerMonthlyFixedIncome = customerMonthlyFixedIncome;
    }

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
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

    public String getSgId() {
        return sgId;
    }

    public void setSgId(String sgId) {
        this.sgId = sgId;
    }

    public String getPrId() {
        return prId;
    }

    public void setPrId(String prId) {
        this.prId = prId;
    }

    public String getPassportId() {
        return passportId;
    }

    public void setPassportId(String passportId) {
        this.passportId = passportId;
    }

    public boolean isEmploymentPanelVisible() {
        return employmentPanelVisible;
    }

    public void setEmploymentPanelVisible(boolean employmentPanelVisible) {
        this.employmentPanelVisible = employmentPanelVisible;
    }

    public void addExistingCustomerCreditCardApplication() throws IOException {
        System.out.println("====== creditcard/publicCreditCardApplicationManagedBean: addExistingCustomerCreditCardApplication() ======");
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
            CustomerBasic customer = (CustomerBasic) ec.getSessionMap().get("customer");
            Long newCustomerBasicId = customer.getCustomerBasicId();
            Long newCustomerAdvancedId;
            if (customerEmploymentStatus.equals("Employee") || customerEmploymentStatus.equals("Self-Employed")) {
                if (customerIndustryType.equals("Others")) {
                    customerIndustryType = customerIndustryTypeOthers;
                }
                if (customerCurrentPosition.equals("Others")) {
                    customerCurrentPosition = customerCurrentPositionOthers;
                }
            }
            if (customer.getCustomerAdvanced() == null) {
                //create CustomerAdvanced

                if (customerEmploymentStatus.equals("Unemployed")) {
                    newCustomerAdvancedId = customerSessionBeanLocal.addNewCustomerAdvanced(customerNumOfDependents, customerEducation, customerResidentialStatus,
                            customerLengthOfResidence, null, 0, customerEmploymentStatus,
                            customerMonthlyFixedIncome.doubleValue(), customerResidentialType, null,
                            null, null, null,
                            null, 0, 0,
                            null);
                } else {
                    newCustomerAdvancedId = customerSessionBeanLocal.addNewCustomerAdvanced(customerNumOfDependents, customerEducation, customerResidentialStatus,
                            customerLengthOfResidence, customerIndustryType, customerLengthOfCurrentJob, customerEmploymentStatus,
                            customerMonthlyFixedIncome.doubleValue(), customerResidentialType, customerCompanyAddress,
                            customerCompanyPostal, customerCurrentPosition, customerCurrentJobTitle,
                            null, 0, 0,
                            null);
                }
            } else {
                if (customerEmploymentStatus.equals("Unemployed")) {
                    newCustomerAdvancedId = customerSessionBeanLocal.updateCustomerAdvanced(customer.getCustomerIdentificationNum(), customerNumOfDependents,
                            customerEducation, customerResidentialStatus, customerLengthOfResidence, null, 0,
                            customerEmploymentStatus, customerMonthlyFixedIncome.doubleValue(), customerResidentialType, null, null,
                            null, null, null, 0, 0.0,
                            null);
                } else {
                    newCustomerAdvancedId = customerSessionBeanLocal.updateCustomerAdvanced(customer.getCustomerIdentificationNum(), customerNumOfDependents,
                            customerEducation, customerResidentialStatus, customerLengthOfResidence, customerIndustryType, customerLengthOfCurrentJob,
                            customerEmploymentStatus, customerMonthlyFixedIncome.doubleValue(), customerResidentialType, customerCompanyAddress, customerCompanyPostal,
                            customerCurrentPosition, customerCurrentJobTitle, null, 0, 0.0,
                            null);
                }

            }

            creditCardTypeId = (Long) ec.getSessionMap().get("cardTypeId");
            System.out.println("##########################customer application type id = " + creditCardTypeId);

            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            Date applicationDate1 = new Date();
            String applicationDate = df.format(applicationDate1);
            //create credit card application
            System.out.println("@@@@@@@@@@@@@@@@@@@@basicId = " + newCustomerBasicId);
            System.out.println("@@@@@@@@@@@@@@@@@@@@adavnceId = " + newCustomerAdvancedId);
            System.out.println("@@@@@@@@@@@@@@@@@@@@cardtypeId = " + creditCardTypeId);
            System.out.println("@@@@@@@@@@@@@@@@@@@@holder name = " + cardHolderName);
            System.out.println("@@@@@@@@@@@@@@@@@@@@has limit = " + hasCreditLimit);
            System.out.println("@@@@@@@@@@@@@@@@@@@@limit double = " + creditLimit.doubleValue());
            System.out.println("@@@@@@@@@@@@@@@@@@@@date = " + applicationDate);
            creditCardSessionLocal.createCreditCard(newCustomerBasicId, newCustomerAdvancedId, creditCardTypeId, cardHolderName, hasCreditLimit, creditLimit.doubleValue(), applicationDate);
            creditCardTypeName = creditCardSessionLocal.findTypeNameById(creditCardTypeId);
            ec.getFlash().put("cardTypeName", creditCardTypeName);
            ec.getFlash().put("customerName", customerName);
            ec.redirect(ec.getRequestContextPath() + "/web/merlionBank/creditCard/publicCreditCardApplicationDone.xhtml?faces-redirect=true");
        }
    }

}
