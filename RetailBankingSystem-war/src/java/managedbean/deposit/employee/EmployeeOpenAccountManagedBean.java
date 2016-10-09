package managedbean.deposit.employee;

import ejb.customer.entity.CustomerBasic;
import javax.ejb.EJB;
import ejb.customer.session.CRMCustomerSessionBean;
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
import javax.faces.view.ViewScoped;
import org.apache.commons.io.IOUtils;

@Named(value = "employeeOpenAccountManagedBean")
@ViewScoped

public class EmployeeOpenAccountManagedBean implements Serializable {

    @EJB
    private InterestSessionBeanLocal interestSessionLocal;

    @EJB
    private CRMCustomerSessionBean customerSessionBean;

    @EJB
    private BankAccountSessionBeanLocal bankAccountSessionLocal;

    private BankAccount bankAccount;

    private Long bankAccountId;
    private String bankAccountNum;
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

    private String newCustomer;
    private String accountApproval;

    //private ExternalContext ec;
    //ec = FacesContext.getCurrentInstance().getExternalContext();
    public EmployeeOpenAccountManagedBean() {
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
        } else {
            visible2 = false;
            nricSGRender = false;
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
        } else {
            visible3 = false;
            singaporePRRender = false;
        }
    }

    public void show4() {

        if (singaporePR.equals("Yes")) {
            visible4 = true;
            nricRender = true;
            passportRender = false;
        } else {
            visible4 = false;
            nricRender = false;
        }
    }

    public void show5() {

        if (singaporePR.equals("No")) {
            visible5 = true;
            passportRender = true;
            nricRender = false;
        } else {
            visible5 = false;
            passportRender = false;
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

    public String getNewCustomer() {
        return newCustomer;
    }

    public void setNewCustomer(String newCustomer) {
        this.newCustomer = newCustomer;
    }

    public String getAccountApproval() {
        return accountApproval;
    }

    public void setAccountApproval(String accountApproval) {
        this.accountApproval = accountApproval;
    }

    public boolean isFixedDepositRender() {
        return fixedDepositRender;
    }

    public void setFixedDepositRender(boolean fixedDepositRender) {
        this.fixedDepositRender = fixedDepositRender;
    }

    public void saveAccount() throws IOException {
        ec = FacesContext.getCurrentInstance().getExternalContext();

        customerSignature = ec.getSessionMap().get("customerSignature").toString();

        checkIdentificationType();
        checkSalutation();

        bankAccountNum = bankAccountSessionLocal.generateBankAccount();
        checkExist = bankAccountSessionLocal.checkExistence(customerIdentificationNum);
        dateOfBirth = bankAccountSessionLocal.changeDateFormat(customerDateOfBirth);

        if (existingCustomer.equals("Yes") && checkExist && agreement) {

            customerBasicId = customerSessionBean.retrieveCustomerBasicByIC(customerIdentificationNum.toUpperCase()).getCustomerBasicId();

            bankAccountBalance = "0";
            transferDailyLimit = "3000";
            transferBalance = "3000";
            bankAccountMinSaving = "";
            currentFixedDepositPeriod = "0";
            fixedDepositStatus = "";
            statementDateDouble = 0.0;
            bankAccountStatus = "Inactive";

            dailyInterest = "0";
            monthlyInterest = "0";
            isTransfer = "0";
            isWithdraw = "0";
            newInterestId = interestSessionLocal.addNewInterest(dailyInterest, monthlyInterest, isTransfer, isWithdraw);

            if (bankAccountDepositPeriod == null) {
                bankAccountDepositPeriod = "None";
            }

            newAccountId = bankAccountSessionLocal.addNewAccount(bankAccountNum, bankAccountType,
                    bankAccountBalance, transferDailyLimit, transferBalance, bankAccountStatus, bankAccountMinSaving,
                    bankAccountDepositPeriod, currentFixedDepositPeriod, fixedDepositStatus,
                    statementDateDouble, customerBasicId, newInterestId);

            bankAccount = bankAccountSessionLocal.retrieveBankAccountById(newAccountId);
            bankAccountSessionLocal.retrieveBankAccountByCusIC(customerIdentificationNum).add(bankAccount);

            bankAccountSessionLocal.updateDepositPeriod(bankAccountNum, bankAccountDepositPeriod);

            statusMessage = "New Account Saved Successfully.";

            ec.getFlash().put("statusMessage", statusMessage);
            ec.getFlash().put("newAccountId", newAccountId);
            ec.getFlash().put("newCustomerBasicId", customerBasicId);
            ec.getFlash().put("bankAccountNum", bankAccountNum);
            ec.getFlash().put("bankAccountType", bankAccountType);
            ec.getFlash().put("bankAccountStatus", bankAccountStatus);

            ec.redirect(ec.getRequestContextPath() + "/web/internalSystem/deposit/employeeSaveAccount.xhtml?faces-redirect=true");

        } else if (existingCustomer.equals("Yes") && !checkExist) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Failed! You don't have Merlion bank account yet.", "Failed!"));
        } else if (existingCustomer.equals("No") && !checkExist && agreement) {

            customerAddress = customerStreetName + ", " + customerBlockNum + ", " + customerUnitNum + ", " + customerPostal;
            newCustomer = "Yes";

            newCustomerBasicId = customerSessionBean.addNewCustomerBasic(customerName,
                    customerSalutation, customerIdentificationNum.toUpperCase(),
                    customerGender, customerEmail, customerMobile.toString(), dateOfBirth,
                    customerNationality, customerCountryOfResidence, customerRace,
                    customerMaritalStatus, customerOccupation, customerCompany,
                    customerAddress, customerPostal, customerOnlineBankingAccountNum,
                    customerOnlineBankingPassword, customerSignature.getBytes(), newCustomer);

            bankAccountBalance = "0";
            transferDailyLimit = "3000";
            transferBalance = "3000";
            bankAccountMinSaving = "";
            currentFixedDepositPeriod = "0";
            fixedDepositStatus = "";
            statementDateDouble = 0.0;
            bankAccountStatus = "Inactive";

            dailyInterest = "0";
            monthlyInterest = "0";
            isTransfer = "0";
            isWithdraw = "0";
            newInterestId = interestSessionLocal.addNewInterest(dailyInterest, monthlyInterest, isTransfer, isWithdraw);

            if (bankAccountDepositPeriod == null) {
                bankAccountDepositPeriod = "None";
            }

            newAccountId = bankAccountSessionLocal.addNewAccount(bankAccountNum, bankAccountType,
                    bankAccountBalance, transferDailyLimit, transferBalance, bankAccountStatus, bankAccountMinSaving,
                    bankAccountDepositPeriod, currentFixedDepositPeriod, fixedDepositStatus,
                    statementDateDouble, newCustomerBasicId, newInterestId);

            bankAccount = bankAccountSessionLocal.retrieveBankAccountById(newAccountId);

            bankAccountSessionLocal.updateDepositPeriod(bankAccountNum, bankAccountDepositPeriod);

            statusMessage = "New Account Saved Successfully.";

            ec.getFlash().put("statusMessage", statusMessage);
            ec.getFlash().put("newAccountId", newAccountId);
            ec.getFlash().put("newCustomerBasicId", newCustomerBasicId);
            ec.getFlash().put("bankAccountNum", bankAccountNum);
            ec.getFlash().put("bankAccountType", bankAccountType);
            ec.getFlash().put("bankAccountStatus", bankAccountStatus);

            ec.redirect(ec.getRequestContextPath() + "/web/internalSystem/deposit/employeeSaveAccount.xhtml?faces-redirect=true");

        } else if (existingCustomer.equals("No") && checkExist) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Failed! You have Merlion bank account already. Please check.", "Failed!"));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Failed! Please agree to terms.", "Failed!"));
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

        file = event.getFile();

        checkIdentificationType();

        if (file != null) {
            String filename = customerName + "-" + customerIdentificationNum + ".png";
            InputStream input = file.getInputstream();
            OutputStream output = new FileOutputStream(new File("/Users/hanfengwei/NetBeansProjects/waves/RetailBankingSystem-war/web/resources/customerIdentification", filename));

            try {
                IOUtils.copy(input, output);
            } finally {
                IOUtils.closeQuietly(input);
                IOUtils.closeQuietly(output);
            }
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Succesful " + file.getFileName() + " is uploaded.", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        } else {

            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cannot find the file, please upload again.", "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }
}
