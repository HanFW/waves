package managedbean.deposit;

import ejb.customer.entity.CustomerBasic;
import javax.ejb.EJB;
import ejb.customer.session.CRMCustomerSessionBean;
import ejb.deposit.entity.BankAccount;
import ejb.deposit.entity.Verify;
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
import ejb.deposit.session.VerifySessionBeanLocal;
import ejb.infrastructure.session.LoggingSessionBeanLocal;
import ejb.infrastructure.session.MessageSessionBeanLocal;
import java.util.Calendar;
import javax.faces.view.ViewScoped;
import org.apache.commons.io.IOUtils;

@Named(value = "accountManagedBean")
@ViewScoped

public class AccountManagedBean implements Serializable {

    @EJB
    private LoggingSessionBeanLocal loggingSessionBeanLocal;

    @EJB
    private MessageSessionBeanLocal messageSessionBeanLocal;

    @EJB
    private VerifySessionBeanLocal verifySessionBeanLocal;

    @EJB
    private InterestSessionBeanLocal interestSessionLocal;

    @EJB
    private CRMCustomerSessionBean customerSessionBean;

    @EJB
    private BankAccountSessionBeanLocal bankAccountSessionLocal;

    private BankAccount bankAccount;

    private Long bankAccountId;
    private String bankAccountNum;
    private String bankAccountPwd;
    private String confirmBankAccountPwd;
    private String bankAccountType;
    private String bankAccountBalance;
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
    private String customerPostal;
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

    private String subject;
    private Date receivedDate;
    private String messageContent;

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

    public String getBankAccountPwd() {
        return bankAccountPwd;
    }

    public void setBankAccountPwd(String bankAccountPwd) {
        this.bankAccountPwd = bankAccountPwd;
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

    public String getBankAccountBalance() {
        return bankAccountBalance;
    }

    public void setBankAccountBalance(String bankAccountBalance) {
        this.bankAccountBalance = bankAccountBalance;
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

    public void saveAccount() throws IOException {
        System.out.println("=");
        System.out.println("====== deposit/AccountManagedBean: saveAccount() ======");
        ec = FacesContext.getCurrentInstance().getExternalContext();

        String customerVerify = "";

        if (customerNationality.equals("Singapore")) {
            customerVerify = customerVerify(customerName, customerNRICSG, "Local");
        } else {
            if (singaporePR.equals("Yes")) {
                customerVerify = customerVerify(customerName, customerNRIC, "PR");
            } else if (singaporePR.equals("No")) {
                customerVerify = customerVerify(customerName, customerPassport, "Foreigner");
            }
        }

        if (customerVerify.equals("Verify Failed. Invalid Identification Number")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Verify Failed. Invalid Identification Number", "Failed!"));
        } else if (customerVerify.equals("Verify Failed. Please check your identification number")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Verify Failed. Please check your identification number", "Failed!"));
        } else if (customerVerify.equals("Verify Failed. Please check your identification type and identification number")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Verify Failed. Please check your identification type and identification number", "Failed!"));
        } else if (customerVerify.equals("Verify Successfully")) {

            if ((customerNRIC.length() > 9 || customerNRIC.length() < 9 || customerNRICSG.length() < 9 || customerNRICSG.length() > 9)
                    && (customerPassport == null)) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Invalid NRIC", "Failed!"));
            } else {
                customerSignature = ec.getSessionMap().get("customerSignature").toString();

                checkIdentificationType();
                checkSalutation();

                bankAccountNum = bankAccountSessionLocal.generateBankAccount();
                checkExist = bankAccountSessionLocal.checkExistence(customerIdentificationNum);
                dateOfBirth = bankAccountSessionLocal.changeDateFormat(customerDateOfBirth);

                if (existingCustomer.equals("Yes") && checkExist && agreement) {
                    dailyInterest = "0";
                    monthlyInterest = "0";
                    isTransfer = "0";
                    isWithdraw = "0";

                    customerBasicId = customerSessionBean.retrieveCustomerBasicByIC(customerIdentificationNum.toUpperCase()).getCustomerBasicId();
                    CustomerBasic customerBasic = bankAccountSessionLocal.retrieveCustomerBasicById(customerBasicId);
                    Double customerAgeDouble = Double.valueOf(customerBasic.getCustomerAge());

                    if (customerAgeDouble < 16) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Eligibility of openning account is 16 years old and above.", "Failed!"));
                    } else {

                        newInterestId = interestSessionLocal.addNewInterest(dailyInterest, monthlyInterest, isTransfer, isWithdraw);

                        bankAccountBalance = "0";
                        transferDailyLimit = "3000";
                        transferBalance = "3000";
                        bankAccountMinSaving = "";
                        bankAccountDepositPeriod = "None";
                        currentFixedDepositPeriod = "0";
                        fixedDepositStatus = "";
                        statementDateDouble = 0.0;

                        if (bankAccountType.equals("Monthly Savings Account")) {
                            bankAccountStatus = "Activated";
                            bankAccountMinSaving = "Insufficient";
                        } else {
                            bankAccountStatus = "Inactivated";
                        }
                        newAccountId = bankAccountSessionLocal.addNewAccount(bankAccountNum, bankAccountPwd, bankAccountType,
                                bankAccountBalance, transferDailyLimit, transferBalance, bankAccountStatus, bankAccountMinSaving,
                                bankAccountDepositPeriod, currentFixedDepositPeriod, fixedDepositStatus,
                                statementDateDouble, customerBasicId, newInterestId);

                        bankAccount = bankAccountSessionLocal.retrieveBankAccountById(newAccountId);
                        bankAccountSessionLocal.retrieveBankAccountByCusIC(customerIdentificationNum).add(bankAccount);

                        statusMessage = "New Account Saved Successfully.";
                        loggingSessionBeanLocal.createNewLogging("customer", customerBasic.getCustomerBasicId(), "open account", "successful", null);

                        subject = "Welcome to Merlion Bank";
                        Calendar cal = Calendar.getInstance();
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

                        ec.redirect(ec.getRequestContextPath() + "/web/merlionBank/deposit/publicSaveAccount.xhtml?faces-redirect=true");
                    }

                } else if (existingCustomer.equals(
                        "Yes") && !checkExist) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Failed! You don't have Merlion bank account yet.", "Failed!"));
                } else if (existingCustomer.equals(
                        "No") && !checkExist && agreement) {

                    customerAddress = customerStreetName + ", " + customerBlockNum + ", " + customerUnitNum + ", " + customerPostal;

                    newCustomerBasicId = customerSessionBean.addNewCustomerBasic(customerName,
                            customerSalutation, customerIdentificationNum.toUpperCase(),
                            customerGender, customerEmail, customerMobile.toString(), dateOfBirth,
                            customerNationality, customerCountryOfResidence, customerRace,
                            customerMaritalStatus, customerOccupation, customerCompany,
                            customerAddress, customerPostal, customerOnlineBankingAccountNum,
                            customerOnlineBankingPassword, customerSignature.getBytes());

                    CustomerBasic customerBasic = bankAccountSessionLocal.retrieveCustomerBasicById(newCustomerBasicId);
                    Double customerAgeDouble = Double.valueOf(customerBasic.getCustomerAge());

                    if (customerAgeDouble < 16) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Eligibility of openning account is 16 years old and above.", "Failed!"));
                    } else {
                        dailyInterest = "0";
                        monthlyInterest = "0";
                        isTransfer = "0";
                        isWithdraw = "0";
                        newInterestId = interestSessionLocal.addNewInterest(dailyInterest, monthlyInterest, isTransfer, isWithdraw);

                        bankAccountBalance = "0";
                        transferDailyLimit = "3000";
                        transferBalance = "3000";
                        bankAccountMinSaving = "";
                        bankAccountDepositPeriod = "None";
                        currentFixedDepositPeriod = "0";
                        fixedDepositStatus = "";
                        statementDateDouble = 0.0;

                        if (bankAccountType.equals("Monthly Savings Account")) {
                            bankAccountStatus = "Activated";
                            bankAccountMinSaving = "Insufficient";
                        } else {
                            bankAccountStatus = "Inactivated";
                        }

                        newAccountId = bankAccountSessionLocal.addNewAccount(bankAccountNum, bankAccountPwd, bankAccountType,
                                bankAccountBalance, transferDailyLimit, transferBalance, bankAccountStatus, bankAccountMinSaving,
                                bankAccountDepositPeriod, currentFixedDepositPeriod, fixedDepositStatus,
                                statementDateDouble, newCustomerBasicId, newInterestId);

                        statusMessage = "New Account Saved Successfully.";
                        loggingSessionBeanLocal.createNewLogging("customer", customerBasic.getCustomerBasicId(), "open account", "successful", null);

                        subject = "Welcome to Merlion Bank";
                        Calendar cal = Calendar.getInstance();
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

                        ec.redirect(ec.getRequestContextPath() + "/web/merlionBank/deposit/publicSaveAccount.xhtml?faces-redirect=true");
                    }
                } else if (existingCustomer.equals("No") && checkExist) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Failed! You have Merlion bank account already. Please check.", "Failed!"));
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Failed! Please agree to terms.", "Failed!"));
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

        if (file != null) {
            String filename = customerName + "-" + customerIdentificationNum + ".png";
            InputStream input = file.getInputstream();
            OutputStream output = new FileOutputStream(new File("/Users/Jingyuan/Desktop/waves/RetailBankingSystem-war/web/resources/customerIdentification", filename));

            try {
                IOUtils.copy(input, output);
            } finally {
                IOUtils.closeQuietly(input);
                IOUtils.closeQuietly(output);
            }
            loggingSessionBeanLocal.createNewLogging("customer", null, "upload softcopy of passport or IC", "successful", null);
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Succesful " + file.getFileName() + " is uploaded.", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        } else {
            loggingSessionBeanLocal.createNewLogging("customer", null, "upload softcopy of passport or IC", "failed", "file cannot be found");
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cannot find the file, please upload again.", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    public String customerVerify(String customerName, String customerIdentificationNum, String identification) {
        Verify verify = verifySessionBeanLocal.retrieveVerifyByCusIc(customerIdentificationNum);

        if (verify.getVerifyId() == null) {
            return "Verify Failed. Invalid Identification Number";
        } else {
            if (!verify.getCustomerName().equals(customerName)) {
                return "Verify Failed. Please check your identification number";
            } else if (!verify.getIdentification().equals(identification)) {
                return "Verify Failed. Please check your identification type and identification number";
            } else {
                return "Verify Successfully";
            }
        }
    }
}
