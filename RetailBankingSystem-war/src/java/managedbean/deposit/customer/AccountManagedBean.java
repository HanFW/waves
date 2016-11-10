package managedbean.deposit.customer;

import ejb.bi.session.DepositAccountOpenSessionBeanLocal;
import ejb.customer.entity.CustomerBasic;
import javax.ejb.EJB;
import ejb.customer.session.CRMCustomerSessionBeanLocal;
import ejb.deposit.entity.BankAccount;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Date;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.FlowEvent;
import org.primefaces.model.UploadedFile;
import ejb.deposit.session.BankAccountSessionBeanLocal;
import ejb.deposit.session.InterestSessionBeanLocal;
import ejb.infrastructure.session.LoggingSessionBeanLocal;
import ejb.infrastructure.session.MessageSessionBeanLocal;
import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import javax.faces.view.ViewScoped;

@Named(value = "accountManagedBean")
@ViewScoped

public class AccountManagedBean implements Serializable {

    @EJB
    private DepositAccountOpenSessionBeanLocal depositAccountOpenSessionBeanLocal;

    @EJB
    private CRMCustomerSessionBeanLocal customerSessionBeanLocal;

    @EJB
    private LoggingSessionBeanLocal loggingSessionBeanLocal;

    @EJB
    private MessageSessionBeanLocal messageSessionBeanLocal;

    @EJB
    private InterestSessionBeanLocal interestSessionBeanLocal;

    @EJB
    private BankAccountSessionBeanLocal bankAccountSessionLocal;
    
    

    private BankAccount bankAccount;

    private Long bankAccountId;
    private String bankAccountNum;
    private String confirmBankAccountPwd;
    private String bankAccountType;
    private String totalBankAccountBalance;
    private String availableBankAccountBalance;
    private Long newAccountId;
    private String transferDailyLimit;
    private String bankAccountStatus;
    private String transferBalance;
    private String statusMessage;
    private String bankAccountMinSaving;
    private String bankAccountDepositPeriod;
    private String currentFixedDepositPeriod;
    private String fixedDepositStatus;

    private String existingCustomer;
    private Long customerBasicId;
    private String customerSalutation;
    private String customerSalutationOthers;
    private String customerName;
    private String customerEmail;
    private Integer customerMobile;
    private String customerNationality;
    private String customerCountryOfResidence;
    private Date customerDateOfBirth;
    private String customerGender;
    private String customerRace;
    private String customerMaritalStatus;
    private String customerOccupation;
    private String customerCompany;
    private String customerAddress;
    private Integer customerPostal;
    private String customerIdentificationNum;
    private Long newCustomerBasicId;
    private String customerOnlineBankingAccountNum;
    private String customerOnlineBankingPassword;
    private String singaporePR;
    private String customerNRICSG;
    private String customerNRIC;
    private String customerPassport;
    private String customerStreetName;
    private String customerBlockNum;
    private String customerUnitNum;

    private Long newInterestId;
    private String dailyInterest;
    private String monthlyInterest;
    private String isTransfer;
    private String isWithdraw;

    private String initialDepositAmt;
    private String depositPeriod;

    private boolean agreement;
    private boolean checkExist;

    private boolean visible = false;
    private boolean visible2 = false;
    private boolean visible3 = false;
    private boolean visible4 = false;
    private boolean visible5 = false;
    private boolean visible6 = false;

    private ExternalContext ec;
    private CustomerBasic customerBasic;

    private UploadedFile file;
    private String customerSignature;
    private String dateOfBirth;
    private Double statementDateDouble;

    private boolean salutationRender = false;
    private boolean nricSGRender = false;
    private boolean nricRender = false;
    private boolean passportRender = false;
    private boolean singaporePRRender = false;
    private boolean fixedDepositRender = false;

    private String subject;
    private Date receivedDate;
    private String messageContent;

    private Long accountId;
    private Long interestId;
    private String newCustomer;

//    private boolean singaporePROutputRender = false;
//    private boolean singaporeNRICOutputRender = false;
//    private boolean NRICOutputRender = false;
//    private boolean passportOutputRender = false;
    //private ExternalContext ec;
    //ec = FacesContext.getCurrentInstance().getExternalContext();
    public AccountManagedBean() {
    }

    public boolean isSalutationRender() {
        return salutationRender;
    }

    public void setSalutationRender(boolean salutationRender) {
        this.salutationRender = salutationRender;
    }

    public boolean isNricSGRender() {
        return nricSGRender;
    }

    public void setNricSGRender(boolean nricSGRender) {
        this.nricSGRender = nricSGRender;
    }

    public boolean isNricRender() {
        return nricRender;
    }

    public void setNricRender(boolean nricRender) {
        this.nricRender = nricRender;
    }

    public boolean isPassportRender() {
        return passportRender;
    }

    public void setPassportRender(boolean passportRender) {
        this.passportRender = passportRender;
    }

    public boolean isSingaporePRRender() {
        return singaporePRRender;
    }

    public void setSingaporePRRender(boolean singaporePRRender) {
        this.singaporePRRender = singaporePRRender;
    }

    public void show() {

        if (customerSalutation.equals("Others")) {
            visible = true;
            salutationRender = true;
        } else {
            visible = false;
            salutationRender = false;
        }
    }

    public void show2() {

        if (customerNationality.equals("Singapore")) {
            visible2 = true;
            visible4 = false;
            visible5 = false;
            singaporePR = null;
            nricSGRender = true;
//            singaporeNRICOutputRender = true;
//            singaporePROutputRender = false;
//            NRICOutputRender = false;
//            passportOutputRender=false;
        } else {
            visible2 = false;
            nricSGRender = false;
//            singaporeNRICOutputRender = false;
        }
    }

    public void show3() {

        if (!customerNationality.equals("Singapore")) {
            visible3 = true;
            visible4 = false;
            visible5 = false;
            singaporePR = null;
            singaporePRRender = true;
            nricRender = false;
            passportRender = false;
            nricSGRender = false;
//            singaporePROutputRender = true;
//            singaporeNRICOutputRender=false;
        } else {
            visible3 = false;
            singaporePRRender = false;
//            singaporePROutputRender = false;
        }
    }

    public void show4() {

        if (singaporePR.equals("Yes")) {
            visible4 = true;
            nricRender = true;
            passportRender = false;
//            NRICOutputRender = true;
        } else {
            visible4 = false;
            nricRender = false;
//            NRICOutputRender = false;
        }
    }

    public void show5() {

        if (singaporePR.equals("No")) {
            visible5 = true;
            passportRender = true;
            nricRender = false;
//            passportOutputRender = true;

        } else {
            visible5 = false;
            passportRender = false;
//            passportOutputRender = false;
        }
    }

    public void show6() {

        if (bankAccountType.equals("Fixed Deposit Account")) {
            visible6 = true;
            fixedDepositRender = true;
        } else {
            visible6 = false;
            fixedDepositRender = false;
        }
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isVisible2() {
        return visible2;
    }

    public void setVisible2(boolean visible2) {
        this.visible2 = visible2;
    }

    public boolean isVisible3() {
        return visible3;
    }

    public void setVisible3(boolean visible3) {
        this.visible3 = visible3;
    }

    public boolean isVisible4() {
        return visible4;
    }

    public void setVisible4(boolean visible4) {
        this.visible4 = visible4;
    }

    public boolean isVisible5() {
        return visible5;
    }

    public void setVisible5(boolean visible5) {
        this.visible5 = visible5;
    }

    public boolean isVisible6() {
        return visible6;
    }

    public void setVisible6(boolean visible6) {
        this.visible6 = visible6;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public boolean isAgreement() {
        return agreement;
    }

    public void setAgreement(boolean agreement) {
        this.agreement = agreement;
    }

    public CustomerBasic getCustomerBasic() {
        return customerBasic;
    }

    public void setCustomerBasic(CustomerBasic customerBasic) {
        this.customerBasic = customerBasic;
    }

    public String getBankAccountNum() {
        return bankAccountNum;
    }

    public void setBankAccountNum(String bankAccountNum) {
        this.bankAccountNum = bankAccountNum;
    }

    public String getBankAccountType() {
        return bankAccountType;
    }

    public void setBankAccountType(String bankAccountType) {
        this.bankAccountType = bankAccountType;
    }

    public Long getNewAccountId() {
        return newAccountId;
    }

    public void setNewAccountId(Long newAccountId) {
        this.newAccountId = newAccountId;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String getCustomerIdentificationNum() {
        return customerIdentificationNum;
    }

    public void setCustomerIdentificationNum(String customerIdentificationNum) {
        this.customerIdentificationNum = customerIdentificationNum;
    }

    public Long getNewCustomerBasicId() {
        return newCustomerBasicId;
    }

    public void setNewCustomerBasicId(Long newCustomerBasicId) {
        this.newCustomerBasicId = newCustomerBasicId;
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

    public Integer getCustomerPostal() {
        return customerPostal;
    }

    public void setCustomerPostal(Integer customerPostal) {
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

    public String getTotalBankAccountBalance() {
        return totalBankAccountBalance;
    }

    public void setTotalBankAccountBalance(String totalBankAccountBalance) {
        this.totalBankAccountBalance = totalBankAccountBalance;
    }

    public String getAvailableBankAccountBalance() {
        return availableBankAccountBalance;
    }

    public void setAvailableBankAccountBalance(String availableBankAccountBalance) {
        this.availableBankAccountBalance = availableBankAccountBalance;
    }

    public String getConfirmBankAccountPwd() {
        return confirmBankAccountPwd;
    }

    public void setConfirmBankAccountPwd(String confirmBankAccountPwd) {
        this.confirmBankAccountPwd = confirmBankAccountPwd;
    }

    public Long getCustomerBasicId() {
        return customerBasicId;
    }

    public void setCustomerBasicId(Long customerBasicId) {
        this.customerBasicId = customerBasicId;
    }

    public String getExistingCustomer() {
        return existingCustomer;
    }

    public void setExistingCustomer(String existingCustomer) {
        this.existingCustomer = existingCustomer;
    }

    public Long getNewInterestId() {
        return newInterestId;
    }

    public void setNewInterestId(Long newInterestId) {
        this.newInterestId = newInterestId;
    }

    public String getTransferDailyLimit() {
        return transferDailyLimit;
    }

    public void setTransferDailyLimit(String transferDailyLimit) {
        this.transferDailyLimit = transferDailyLimit;
    }

    public String getDailyInterest() {
        return dailyInterest;
    }

    public void setDailyInterest(String dailyInterest) {
        this.dailyInterest = dailyInterest;
    }

    public String getMonthlyInterest() {
        return monthlyInterest;
    }

    public void setMonthlyInterest(String monthlyInterest) {
        this.monthlyInterest = monthlyInterest;
    }

    public String getIsTransfer() {
        return isTransfer;
    }

    public void setIsTransfer(String isTransfer) {
        this.isTransfer = isTransfer;
    }

    public String getIsWithdraw() {
        return isWithdraw;
    }

    public void setIsWithdraw(String isWithdraw) {
        this.isWithdraw = isWithdraw;
    }

    public Long getBankAccountId() {
        return bankAccountId;
    }

    public void setBankAccountId(Long bankAccountId) {
        this.bankAccountId = bankAccountId;
    }

    public String getCustomerSalutationOthers() {
        return customerSalutationOthers;
    }

    public void setCustomerSalutationOthers(String customerSalutationOthers) {
        this.customerSalutationOthers = customerSalutationOthers;
    }

    public String onFlowProcess(FlowEvent event) {
        ec = FacesContext.getCurrentInstance().getExternalContext();

        if (customerDateOfBirth != null) {
            dateOfBirth = bankAccountSessionLocal.changeDateFormat(customerDateOfBirth);
            String customerAge = getAge(dateOfBirth);
            Double customerAgeDouble = Double.valueOf(customerAge);

            if (customerAgeDouble < 16) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Eligibility of openning account is 16 years old and above.", "Failed!"));
                return event.getOldStep();
            } else {
                return event.getNewStep();
            }
        }

        if (customerNRIC != null) {
            if (customerNRIC.length() > 9 || customerNRIC.length() < 9) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid NRIC", "Failed!"));
                return event.getOldStep();
            } else {
                return event.getNewStep();
            }
        }

        if (customerNRICSG != null) {
            if (customerNRICSG.length() < 9 || customerNRICSG.length() > 9) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid NRIC", "Failed!"));
                return event.getOldStep();
            } else {
                return event.getNewStep();
            }
        }

        return event.getNewStep();
    }

    public String getBankAccountStatus() {
        return bankAccountStatus;
    }

    public void setBankAccountStatus(String bankAccountStatus) {
        this.bankAccountStatus = bankAccountStatus;
    }

    public String getInitialDepositAmt() {
        return initialDepositAmt;
    }

    public void setInitialDepositAmt(String initialDepositAmt) {
        this.initialDepositAmt = initialDepositAmt;
    }

    public String getDepositPeriod() {
        return depositPeriod;
    }

    public void setDepositPeriod(String depositPeriod) {
        this.depositPeriod = depositPeriod;
    }

    public String getSingaporePR() {
        return singaporePR;
    }

    public void setSingaporePR(String singaporePR) {
        this.singaporePR = singaporePR;
    }

    public boolean isCheckExist() {
        return checkExist;
    }

    public void setCheckExist(boolean checkExist) {
        this.checkExist = checkExist;
    }

    public String getTransferBalance() {
        return transferBalance;
    }

    public void setTransferBalance(String transferBalance) {
        this.transferBalance = transferBalance;
    }

    public String getCustomerNRIC() {
        return customerNRIC;
    }

    public void setCustomerNRIC(String customerNRIC) {
        this.customerNRIC = customerNRIC;
    }

    public String getCustomerPassport() {
        return customerPassport;
    }

    public void setCustomerPassport(String customerPassport) {
        this.customerPassport = customerPassport;
    }

    public String getCustomerNRICSG() {
        return customerNRICSG;
    }

    public void setCustomerNRICSG(String customerNRICSG) {
        this.customerNRICSG = customerNRICSG;
    }

    public String getCustomerSignature() {
        return customerSignature;
    }

    public void setCustomerSignature(String customerSignature) {
        this.customerSignature = customerSignature;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getBankAccountMinSaving() {
        return bankAccountMinSaving;
    }

    public void setBankAccountMinSaving(String bankAccountMinSaving) {
        this.bankAccountMinSaving = bankAccountMinSaving;
    }

    public String getBankAccountDepositPeriod() {
        return bankAccountDepositPeriod;
    }

    public void setBankAccountDepositPeriod(String bankAccountDepositPeriod) {
        this.bankAccountDepositPeriod = bankAccountDepositPeriod;
    }

    public String getCurrentFixedDepositPeriod() {
        return currentFixedDepositPeriod;
    }

    public void setCurrentFixedDepositPeriod(String currentFixedDepositPeriod) {
        this.currentFixedDepositPeriod = currentFixedDepositPeriod;
    }

    public String getFixedDepositStatus() {
        return fixedDepositStatus;
    }

    public void setFixedDepositStatus(String fixedDepositStatus) {
        this.fixedDepositStatus = fixedDepositStatus;
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

    public Integer getCustomerMobile() {
        return customerMobile;
    }

    public void setCustomerMobile(Integer customerMobile) {
        this.customerMobile = customerMobile;
    }

    public Double getStatementDateDouble() {
        return statementDateDouble;
    }

    public void setStatementDateDouble(Double statementDateDouble) {
        this.statementDateDouble = statementDateDouble;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Date getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(Date receivedDate) {
        this.receivedDate = receivedDate;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getInterestId() {
        return interestId;
    }

    public void setInterestId(Long interestId) {
        this.interestId = interestId;
    }

    public String getNewCustomer() {
        return newCustomer;
    }

    public void setNewCustomer(String newCustomer) {
        this.newCustomer = newCustomer;
    }

    public boolean isFixedDepositRender() {
        return fixedDepositRender;
    }

    public void setFixedDepositRender(boolean fixedDepositRender) {
        this.fixedDepositRender = fixedDepositRender;
    }

    public void saveAccount() throws IOException {
        System.out.println("=");
        System.out.println("====== deposit/AccountManagedBean: saveAccount() ======");
        ec = FacesContext.getCurrentInstance().getExternalContext();

        if (file == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please upload your identification softcopy", "Failed!"));
        } else {

            if ((customerNRIC.length() > 9 || customerNRIC.length() < 9 || customerNRICSG.length() < 9 || customerNRICSG.length() > 9)
                    && (customerPassport == null)) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid NRIC", "Failed!"));
            } else {
                customerSignature = ec.getSessionMap().get("customerSignature").toString();

                if (customerSignature.equals("")) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed! Please provide your digital signature", "Failed!"));
                } else {

                    checkIdentificationType();
                    checkSalutation();

                    bankAccountNum = bankAccountSessionLocal.generateBankAccount();
                    checkExist = bankAccountSessionLocal.checkExistence(customerIdentificationNum);
                    dateOfBirth = bankAccountSessionLocal.changeDateFormat(customerDateOfBirth);

                    if (existingCustomer.equals("Yes") && checkExist && agreement) {

                        customerBasicId = customerSessionBeanLocal.retrieveCustomerBasicByIC(customerIdentificationNum.toUpperCase()).getCustomerBasicId();
                        CustomerBasic customerBasic = bankAccountSessionLocal.retrieveCustomerBasicById(customerBasicId);
                        Double customerAgeDouble = Double.valueOf(customerBasic.getCustomerAge());

                        if (customerAgeDouble < 16) {
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Eligibility of openning account is 16 years old and above.", "Failed!"));
                        } else {

                            totalBankAccountBalance = "0.0";
                            availableBankAccountBalance = "0.0";
                            transferDailyLimit = "3000.0";
                            transferBalance = "3000.0";
                            bankAccountMinSaving = "";
                            currentFixedDepositPeriod = "0";
                            fixedDepositStatus = "";
                            statementDateDouble = 0.0;

                            dailyInterest = "0";
                            monthlyInterest = "0";
                            isTransfer = "0";
                            isWithdraw = "0";
                            newInterestId = interestSessionBeanLocal.addNewInterest(dailyInterest, monthlyInterest, isTransfer, isWithdraw);

                            if (bankAccountType.equals("Monthly Savings Account")) {
                                bankAccountStatus = "Active";
                                bankAccountMinSaving = "Insufficient";
                            } else {
                                bankAccountStatus = "Inactive";
                            }

                            if (bankAccountDepositPeriod == null) {
                                bankAccountDepositPeriod = "None";
                            }

                            Calendar cal = Calendar.getInstance();
                            Long currentTimeMilis = cal.getTimeInMillis();

                            newAccountId = bankAccountSessionLocal.addNewAccount(bankAccountNum, bankAccountType,
                                    totalBankAccountBalance, availableBankAccountBalance, transferDailyLimit,
                                    transferBalance, bankAccountStatus, bankAccountMinSaving,
                                    bankAccountDepositPeriod, currentFixedDepositPeriod, fixedDepositStatus,
                                    statementDateDouble, currentTimeMilis, customerBasicId, newInterestId);

                            if (bankAccountType.equals("Monthly Savings Account")) {
                                depositAccountOpenSessionBeanLocal.addNewDepositAccountOpen(currentTimeMilis, cal.getTime().toString());
                            }

                            bankAccount = bankAccountSessionLocal.retrieveBankAccountById(newAccountId);
                            bankAccountSessionLocal.retrieveBankAccountByCusIC(customerIdentificationNum).add(bankAccount);

                            if (!bankAccountDepositPeriod.equals("None")) {
                                bankAccountSessionLocal.updateDepositPeriod(bankAccountNum, bankAccountDepositPeriod);
                            }

                            statusMessage = "New Account Saved Successfully.";
                            loggingSessionBeanLocal.createNewLogging("customer", customerBasic.getCustomerBasicId(), "open account", "successful", null);

                            subject = "Welcome to Merlion Bank";
                            receivedDate = cal.getTime();
                            messageContent = "Welcome to Merlion Bank! Please deposit/transfer sufficient fund to your bank account.\n"
                                    + "For fixed deposit account, please declare your deposit period before activating your account.\n"
                                    + "Please contact us at 800 820 8820 or write enquiry after you login.\n";
                            messageSessionBeanLocal.sendMessage("Merlion Bank", "Service", subject, receivedDate.toString(),
                                    messageContent, customerBasicId);
                            loggingSessionBeanLocal.createNewLogging("system", customerBasic.getCustomerBasicId(), "send welcome message", "successful", null);

                            ec.getFlash().put("statusMessage", statusMessage);
                            ec.getFlash().put("newAccountId", newAccountId);
                            ec.getFlash().put("newCustomerBasicId", customerBasicId);
                            ec.getFlash().put("bankAccountNum", bankAccountNum);
                            ec.getFlash().put("bankAccountType", bankAccountType);
                            ec.getFlash().put("bankAccountStatus", bankAccountStatus);

                            ec.redirect(ec.getRequestContextPath() + "/web/merlionBank/deposit/publicSaveAccountExistingCustomer.xhtml?faces-redirect=true");
                        }

                    } else if (existingCustomer.equals(
                            "Yes") && !checkExist) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed! You don't have Merlion bank account yet.", "Failed!"));
                    } else if (existingCustomer.equals(
                            "No") && !checkExist && agreement) {

                        customerAddress = customerStreetName + ", " + customerBlockNum + ", " + customerUnitNum + ", " + customerPostal;
                        newCustomer = "Yes";

                        newCustomerBasicId = customerSessionBeanLocal.addNewCustomerBasic(customerName,
                                customerSalutation, customerIdentificationNum.toUpperCase(),
                                customerGender, customerEmail, customerMobile.toString(), dateOfBirth,
                                customerNationality, customerCountryOfResidence, customerRace,
                                customerMaritalStatus, customerOccupation, customerCompany,
                                customerAddress, customerPostal.toString(), customerOnlineBankingAccountNum,
                                customerOnlineBankingPassword, customerSignature.getBytes(), newCustomer);

                        CustomerBasic customerBasic = bankAccountSessionLocal.retrieveCustomerBasicById(newCustomerBasicId);
                        Double customerAgeDouble = Double.valueOf(customerBasic.getCustomerAge());

                        if (customerAgeDouble < 16) {
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Eligibility of openning account is 16 years old and above.", "Failed!"));
                        } else {

                            totalBankAccountBalance = "0.0";
                            availableBankAccountBalance = "0.0";
                            transferDailyLimit = "3000.0";
                            transferBalance = "3000.0";
                            bankAccountMinSaving = "";
                            currentFixedDepositPeriod = "0";
                            fixedDepositStatus = "";
                            statementDateDouble = 0.0;
                            bankAccountStatus = "Inactive";

                            dailyInterest = "0";
                            monthlyInterest = "0";
                            isTransfer = "0";
                            isWithdraw = "0";
                            newInterestId = interestSessionBeanLocal.addNewInterest(dailyInterest, monthlyInterest, isTransfer, isWithdraw);

                            if (bankAccountType.equals("Monthly Savings Account")) {
                                bankAccountStatus = "Active";
                                bankAccountMinSaving = "Insufficient";
                            } else {
                                bankAccountStatus = "Inactive";
                            }

                            if (bankAccountDepositPeriod == null) {
                                bankAccountDepositPeriod = "None";
                            }

                            Calendar cal = Calendar.getInstance();
                            Long currentTimeMilis = cal.getTimeInMillis();

                            newAccountId = bankAccountSessionLocal.addNewAccount(bankAccountNum, bankAccountType,
                                    totalBankAccountBalance, availableBankAccountBalance, transferDailyLimit, transferBalance, bankAccountStatus, bankAccountMinSaving,
                                    bankAccountDepositPeriod, currentFixedDepositPeriod, fixedDepositStatus,
                                    statementDateDouble, currentTimeMilis, newCustomerBasicId, newInterestId);

                            bankAccount = bankAccountSessionLocal.retrieveBankAccountById(newAccountId);

                            if (bankAccountType.equals("Monthly Savings Account")) {
                                depositAccountOpenSessionBeanLocal.addNewDepositAccountOpen(currentTimeMilis, cal.getTime().toString());
                            }

                            if (!bankAccountDepositPeriod.equals("None")) {
                                bankAccountSessionLocal.updateDepositPeriod(bankAccountNum, bankAccountDepositPeriod);
                            }

                            statusMessage = "New Account Saved Successfully.";
                            loggingSessionBeanLocal.createNewLogging("customer", customerBasic.getCustomerBasicId(), "open account", "successful", null);

                            subject = "Welcome to Merlion Bank";
                            receivedDate = cal.getTime();
                            messageContent = "Welcome to Merlion Bank! Please deposit/transfer sufficient fund to your bank account.\n"
                                    + "For fixed deposit account, please declare your deposit period before activating your account.\n"
                                    + "Please contact us at 800 820 8820 or write enquiry after you login.\n";
                            messageSessionBeanLocal.sendMessage("Merlion Bank", "Service", subject, receivedDate.toString(),
                                    messageContent, newCustomerBasicId);
                            loggingSessionBeanLocal.createNewLogging("system", customerBasic.getCustomerBasicId(), "send welcome message", "successful", null);

                            ec.getFlash().put("statusMessage", statusMessage);
                            ec.getFlash().put("newAccountId", newAccountId);
                            ec.getFlash().put("newCustomerBasicId", newCustomerBasicId);
                            ec.getFlash().put("bankAccountNum", bankAccountNum);
                            ec.getFlash().put("bankAccountType", bankAccountType);
                            ec.getFlash().put("bankAccountStatus", bankAccountStatus);

                            ec.redirect(ec.getRequestContextPath() + "/web/merlionBank/deposit/publicSaveAccountNewCustomer.xhtml?faces-redirect=true");
                        }
                    } else if (existingCustomer.equals("No") && checkExist) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed! You have Merlion bank account already. Please check.", "Failed!"));
                    } else {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed! Please agree to terms.", "Failed!"));
                    }
                }
            }
        }
    }

    public void checkIdentificationType() {
        if (customerNationality.equals("Singapore")) {
            customerIdentificationNum = customerNRICSG;
        } else {
            if (singaporePR.equals("Yes")) {
                customerIdentificationNum = customerNRIC;
            } else {
                customerIdentificationNum = customerPassport;
            }
        }
    }

    public void checkSalutation() {
        if (customerSalutation.equals("Others")) {
            customerSalutation = customerSalutationOthers;
        }
    }

    public void upload(FileUploadEvent event) throws IOException {
        System.out.println("=");
        System.out.println("====== deposit/AccountManagedBean: upload() ======");
        file = event.getFile();

        checkIdentificationType();

        if (file != null) {
            String filename = customerIdentificationNum + ".png";

            String newFilePath = System.getProperty("user.dir").replace("config", "docroot") + System.getProperty("file.separator");
            OutputStream output = new FileOutputStream(new File(newFilePath, filename));

            int a;
            int BUFFER_SIZE = 8192;
            byte[] buffer = new byte[BUFFER_SIZE];

            InputStream inputStream = file.getInputstream();

            while (true) {
                a = inputStream.read(buffer);
                if (a < 0) {
                    break;
                }
                output.write(buffer, 0, a);
                output.flush();
            }

            output.close();
            inputStream.close();

            loggingSessionBeanLocal.createNewLogging("customer", null, "upload softcopy of passport or IC", "successful", null);
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Succesful " + file.getFileName() + " is uploaded.", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        } else {
            loggingSessionBeanLocal.createNewLogging("customer", null, "upload softcopy of passport or IC", "failed", "file cannot be found");
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Cannot find the file, please upload again.", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    public void openAccountOneClick() throws IOException {

        ec = FacesContext.getCurrentInstance().getExternalContext();

        //Customer Details
        customerName = "Dai HaiLiang";
        customerSalutation = "Mr";
        customerIdentificationNum = "G12345678";
        customerGender = "Male";
        customerEmail = "yongxue0701@gmail.com";
        customerMobile = 84819970;
        dateOfBirth = "01/Jul/1993";
        customerNationality = "Chinese";
        customerCountryOfResidence = "Singapore";
        customerRace = "Chinese";
        customerMaritalStatus = "Single";
        customerOccupation = "Student";
        customerCompany = "NUS";
        customerAddress = "PGP" + ", " + "BLK24" + ", " + "#04-G" + ", " + "118429";
        customerPostal = 118429;
        customerOnlineBankingAccountNum = "123456789";
        customerOnlineBankingPassword = "123456789";
        customerSignature = "1234";
        newCustomer = "Yes";

        newCustomerBasicId = customerSessionBeanLocal.addNewCustomerOneTime(customerName,
                customerSalutation, customerIdentificationNum.toUpperCase(),
                customerGender, customerEmail, customerMobile.toString(), dateOfBirth,
                customerNationality, customerCountryOfResidence, customerRace,
                customerMaritalStatus, customerOccupation, customerCompany,
                customerAddress, customerPostal.toString(), customerOnlineBankingAccountNum,
                customerOnlineBankingPassword, customerSignature.getBytes(), newCustomer);

        customerBasicId = customerSessionBeanLocal.addNewCustomerOneTime("Han Fengwei",
                "Ms", "G11223344".toUpperCase(),
                "Female", "yongxue0701@gmail.com", "84819970", "02/Jul/1993",
                customerNationality, customerCountryOfResidence, customerRace,
                customerMaritalStatus, customerOccupation, customerCompany,
                customerAddress, customerPostal.toString(), "11223344",
                "11223344", customerSignature.getBytes(), newCustomer);

        //Interest Details
        dailyInterest = "0";
        monthlyInterest = "0";
        isTransfer = "0";
        isWithdraw = "0";

        newInterestId = interestSessionBeanLocal.addNewInterest(dailyInterest, monthlyInterest, isTransfer, isWithdraw);
        interestId = interestSessionBeanLocal.addNewInterest(dailyInterest, monthlyInterest, isTransfer, isWithdraw);

        //Bank Account Details
        bankAccountNum = bankAccountSessionLocal.generateBankAccount();
        bankAccountType = "Bonus Savings Account";
        totalBankAccountBalance = "10000.0";
        availableBankAccountBalance = "10000.0";
        transferDailyLimit = "3000.0";
        transferBalance = "3000.0";
        bankAccountMinSaving = "";
        bankAccountDepositPeriod = "None";
        currentFixedDepositPeriod = "0";
        fixedDepositStatus = "";
        statementDateDouble = 0.0;
        bankAccountStatus = "Active";

        Calendar cal = Calendar.getInstance();
        Long currentTimeMilis = cal.getTimeInMillis();

        newAccountId = bankAccountSessionLocal.addNewAccountOneTime(bankAccountNum, bankAccountType,
                totalBankAccountBalance, availableBankAccountBalance, transferDailyLimit, transferBalance, bankAccountStatus, bankAccountMinSaving,
                bankAccountDepositPeriod, currentFixedDepositPeriod, fixedDepositStatus,
                statementDateDouble, currentTimeMilis, newCustomerBasicId, newInterestId);

        accountId = bankAccountSessionLocal.addNewAccountOneTime(bankAccountSessionLocal.generateBankAccount(),
                "Basic Savings Account", totalBankAccountBalance, availableBankAccountBalance, transferDailyLimit, transferBalance, bankAccountStatus, bankAccountMinSaving,
                bankAccountDepositPeriod, currentFixedDepositPeriod, fixedDepositStatus,
                statementDateDouble, currentTimeMilis, customerBasicId, interestId);

        Long newDepositAccountOpenId = depositAccountOpenSessionBeanLocal.addNewDepositAccountOpen(currentTimeMilis, cal.getTime().toString());
        Long depositAccountOpenId = depositAccountOpenSessionBeanLocal.addNewDepositAccountOpen(currentTimeMilis, cal.getTime().toString());

        statusMessage = "New Account Saved Successfully.";

        subject = "Welcome to Merlion Bank";
        receivedDate = cal.getTime();
        messageContent = "Welcome to Merlion Bank! Please deposit/transfer sufficient fund to your bank account.\n"
                + "For fixed deposit account, please declare your deposit period before activating your account.\n"
                + "Please contact us at 800 820 8820 or write enquiry after you login.\n";
        messageSessionBeanLocal.sendMessage("Merlion Bank", "Service", subject, receivedDate.toString(),
                messageContent, newCustomerBasicId);
        messageSessionBeanLocal.sendMessage("Merlion Bank", "Service", subject, receivedDate.toString(),
                messageContent, customerBasicId);

        ec.getFlash().put("statusMessage", statusMessage);
        ec.getFlash().put("newAccountId", newAccountId);
        ec.getFlash().put("newCustomerBasicId", newCustomerBasicId);
        ec.getFlash().put("bankAccountNum", bankAccountNum);
        ec.getFlash().put("bankAccountType", bankAccountType);
        ec.getFlash().put("bankAccountStatus", bankAccountStatus);

        ec.redirect(ec.getRequestContextPath() + "/web/merlionBank/deposit/publicSaveAccountNewCustomer.xhtml?faces-redirect=true");
    }

    private String getAge(String customerDateOfBirth) {
        String daystr = customerDateOfBirth.substring(0, 2);
        String monthstr = customerDateOfBirth.substring(3, 6);
        String yearstr = customerDateOfBirth.substring(7);
        int month = 0;
        int day = Integer.parseInt(daystr);
        int year = Integer.parseInt(yearstr);
        String customerAge = "";

        switch (monthstr) {
            case "Jan":
                month = 1;
                break;
            case "Feb":
                month = 2;
                break;
            case "Mar":
                month = 3;
                break;
            case "Apr":
                month = 4;
                break;
            case "May":
                month = 5;
                break;
            case "Jun":
                month = 6;
                break;
            case "Jul":
                month = 7;
                break;
            case "Aug":
                month = 8;
                break;
            case "Sep":
                month = 9;
                break;
            case "Oct":
                month = 10;
                break;
            case "Nov":
                month = 11;
                break;
            case "Dec":
                month = 12;
                break;
        }

        LocalDate localBirth = LocalDate.of(year, month, day);
        LocalDate now = LocalDate.now();
        Period p = Period.between(localBirth, now);
        customerAge = String.valueOf(p.getYears());

        return customerAge;
    }
}
