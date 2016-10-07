package managedbean.deposit.employee;

import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;

@Named(value = "employeeSaveAccountManagedBean")
@RequestScoped

public class EmployeeSaveAccountManagedBean {

    private String statusMessage;
    private String bankAccountId;
    private String customerBasicId;
    private String bankAccountNum;
    private String bankAccountType;
    private String initialDepositAmt;
    private String bankAccountStatus;
    private String attention;

    public EmployeeSaveAccountManagedBean() {
    }

    @PostConstruct
    public void init() {
        statusMessage = FacesContext.getCurrentInstance().getExternalContext().getFlash().get("statusMessage").toString();
        bankAccountId = FacesContext.getCurrentInstance().getExternalContext().getFlash().get("newAccountId").toString();
        customerBasicId = FacesContext.getCurrentInstance().getExternalContext().getFlash().get("newCustomerBasicId").toString();
        bankAccountNum = FacesContext.getCurrentInstance().getExternalContext().getFlash().get("bankAccountNum").toString();
        bankAccountType = FacesContext.getCurrentInstance().getExternalContext().getFlash().get("bankAccountType").toString();
        bankAccountStatus = FacesContext.getCurrentInstance().getExternalContext().getFlash().get("bankAccountStatus").toString();

        if (bankAccountStatus.equals("Inactive") && !bankAccountType.equals("Fixed Deposit Account")) {
            attention = "Customer bank account is inactive."
                    + "To activate, please deposit/transfer sufficient amount to your account."
                    + "Please activate your bank account within one week. \n"
                    + "Otherwise, our system will automatically close your account after one week.";
        } else if (bankAccountStatus.equals("Inactive") && bankAccountType.equals("Fixed Deposit Account")) {
            attention = "Customer has to declare your fixed deposit period before activating your account."
                    + "To activate, please deposit/transfer sufficient amount to your account."
                    + "Please activate your bank account within one week. \n"
                    + "Otherwise, our system will automatically close your account after one week.";
        }
        if (bankAccountType.equals("Monthly Savings Account")) {
            attention = "Minimum monthly saving is S$50.";
        }
    }

    public String getAttention() {
        return attention;
    }

    public void setAttention(String attention) {
        this.attention = attention;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String getBankAccountId() {
        return bankAccountId;
    }

    public void setBankAccountId(String bankAccountId) {
        this.bankAccountId = bankAccountId;
    }

    public String getCustomerBasicId() {
        return customerBasicId;
    }

    public void setCustomerBasicId(String customerBasicId) {
        this.customerBasicId = customerBasicId;
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

    public String getInitialDepositAmt() {
        return initialDepositAmt;
    }

    public void setInitialDepositAmt(String initialDepositAmt) {
        this.initialDepositAmt = initialDepositAmt;
    }

    public String getBankAccountStatus() {
        return bankAccountStatus;
    }

    public void setBankAccountStatus(String bankAccountStatus) {
        this.bankAccountStatus = bankAccountStatus;
    }
}
