package managedbean.deposit;

import ejb.customer.entity.CustomerBasic;
import ejb.deposit.entity.BankAccount;
import ejb.deposit.session.BankAccountSessionBeanLocal;
import ejb.deposit.session.InterestSessionBeanLocal;
import java.io.IOException;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.primefaces.event.FlowEvent;

@Named(value = "employeeOpenAccount")
@RequestScoped

public class EmployeeOpenAccountManagedBean {

    @EJB
    private BankAccountSessionBeanLocal bankAccountSessionBeanLocal;

    @EJB
    private InterestSessionBeanLocal interestSessionBeanLocal;

    private String bankAccountType;
    private String bankAccountPwd;
    private String confirmBankAccountPwd;
    private String customerName;
    private String customerIdentificationNum;
    private String customerEmail;
    private String customerMobile;
    private boolean agreement;

    private String dailyInterest;
    private String monthlyInterest;
    private String isTransfer;
    private String isWithdraw;
    private Long newInterestId;

    private String bankAccountBalance;
    private String transferDailyLimit;
    private String transferBalance;
    private String bankAccountMinSaving;
    private String bankAccountDepositPeriod;
    private String currentFixedDepositPeriod;
    private String fixedDepositStatus;
    private String bankAccountStatus;

    private String bankAccountNum;
    private Long customerBasicId;
    private Long newAccountId;
    private String statusMessage;

    private ExternalContext ec;
    private BankAccount bankAccount;

    public EmployeeOpenAccountManagedBean() {
    }

    @PostConstruct
    public void init() {
        ec = FacesContext.getCurrentInstance().getExternalContext();

        CustomerBasic customerBasic = (CustomerBasic) ec.getSessionMap().get("customer");

        customerName = customerBasic.getCustomerName();
        customerIdentificationNum = customerBasic.getCustomerIdentificationNum();
        customerEmail = customerBasic.getCustomerEmail();
        customerMobile = customerBasic.getCustomerMobile();
    }

    public String getBankAccountType() {
        return bankAccountType;
    }

    public void setBankAccountType(String bankAccountType) {
        this.bankAccountType = bankAccountType;
    }

    public String getBankAccountPwd() {
        return bankAccountPwd;
    }

    public void setBankAccountPwd(String bankAccountPwd) {
        this.bankAccountPwd = bankAccountPwd;
    }

    public String getConfirmBankAccountPwd() {
        return confirmBankAccountPwd;
    }

    public void setConfirmBankAccountPwd(String confirmBankAccountPwd) {
        this.confirmBankAccountPwd = confirmBankAccountPwd;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerIdentificationNum() {
        return customerIdentificationNum;
    }

    public void setCustomerIdentificationNum(String customerIdentificationNum) {
        this.customerIdentificationNum = customerIdentificationNum;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerMobile() {
        return customerMobile;
    }

    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
    }

    public boolean isAgreement() {
        return agreement;
    }

    public void setAgreement(boolean agreement) {
        this.agreement = agreement;
    }

    public String onFlowProcess(FlowEvent event) {
        return event.getNewStep();
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

    public Long getNewInterestId() {
        return newInterestId;
    }

    public void setNewInterestId(Long newInterestId) {
        this.newInterestId = newInterestId;
    }

    public String getBankAccountBalance() {
        return bankAccountBalance;
    }

    public void setBankAccountBalance(String bankAccountBalance) {
        this.bankAccountBalance = bankAccountBalance;
    }

    public String getTransferDailyLimit() {
        return transferDailyLimit;
    }

    public void setTransferDailyLimit(String transferDailyLimit) {
        this.transferDailyLimit = transferDailyLimit;
    }

    public String getTransferBalance() {
        return transferBalance;
    }

    public void setTransferBalance(String transferBalance) {
        this.transferBalance = transferBalance;
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

    public String getBankAccountStatus() {
        return bankAccountStatus;
    }

    public void setBankAccountStatus(String bankAccountStatus) {
        this.bankAccountStatus = bankAccountStatus;
    }

    public String getBankAccountNum() {
        return bankAccountNum;
    }

    public void setBankAccountNum(String bankAccountNum) {
        this.bankAccountNum = bankAccountNum;
    }

    public Long getCustomerBasicId() {
        return customerBasicId;
    }

    public void setCustomerBasicId(Long customerBasicId) {
        this.customerBasicId = customerBasicId;
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

    public void saveAccount() throws IOException {
        System.out.println("hello");
        ec = FacesContext.getCurrentInstance().getExternalContext();

        CustomerBasic customerBasic = (CustomerBasic) ec.getSessionMap().get("customer");

        bankAccountNum = bankAccountSessionBeanLocal.generateBankAccount();
        System.out.println(agreement);
        if (agreement) {
            System.out.println("if");

            dailyInterest = "0";
            monthlyInterest = "0";
            isTransfer = "0";
            isWithdraw = "0";

            newInterestId = interestSessionBeanLocal.addNewInterest(dailyInterest, monthlyInterest, isTransfer, isWithdraw);

            bankAccountBalance = "0";
            transferDailyLimit = "3000";
            transferBalance = "3000";
            bankAccountMinSaving = "";
            bankAccountDepositPeriod = "None";
            currentFixedDepositPeriod = "0";
            fixedDepositStatus = "";

            if (bankAccountType.equals("Monthly Savings Account")) {
                bankAccountStatus = "Activated";
                bankAccountMinSaving = "Insufficient";
            } else {
                bankAccountStatus = "Inactivated";
            }

            customerBasicId = customerBasic.getCustomerBasicId();

            newAccountId = bankAccountSessionBeanLocal.addNewAccount(bankAccountNum, bankAccountPwd, bankAccountType,
                    bankAccountBalance, transferDailyLimit, transferBalance, bankAccountStatus, bankAccountMinSaving,
                    bankAccountDepositPeriod, currentFixedDepositPeriod, fixedDepositStatus, customerBasicId, newInterestId);
            bankAccount = bankAccountSessionBeanLocal.retrieveBankAccountById(newAccountId);

            statusMessage = "New Account Saved Successfully.";

            ec.getFlash().put("statusMessage", statusMessage);
            ec.getFlash().put("newAccountId", newAccountId);
            ec.getFlash().put("newCustomerBasicId", customerBasicId);
            ec.getFlash().put("bankAccountNum", bankAccountNum);
            ec.getFlash().put("bankAccountType", bankAccountType);

            ec.redirect(ec.getRequestContextPath() + "/web/onlineBanking/deposit/customerSaveAccount.xhtml?faces-redirect=true");

        } else {
            System.out.println("else");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Failed! Please agree to terms.", "Failed!"));
        }
    }
}
