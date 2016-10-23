package managedbean.deposit.employee;

import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;

@Named(value = "employeeTransferFinishedManagedBean")
@RequestScoped

public class EmployeeTransferFinishedManagedBean {

    private String statusMessage;
    private String fromAccount;
    private String toAccount;
    private String transferAmt;
    private String transactionId;
    private String toBankAccountNumWithType;
    private String fromBankAccountNumWithType;
    private String fromAccountAvailableBalance;
    private String fromAccountTotalBalance;

    public EmployeeTransferFinishedManagedBean() {
    }

    @PostConstruct
    public void init() {
        statusMessage = FacesContext.getCurrentInstance().getExternalContext().getFlash().get("statusMessage").toString();
        transactionId = FacesContext.getCurrentInstance().getExternalContext().getFlash().get("newTransactionId").toString();
        toBankAccountNumWithType = FacesContext.getCurrentInstance().getExternalContext().getFlash().get("toBankAccountNumWithType").toString();
        fromBankAccountNumWithType = FacesContext.getCurrentInstance().getExternalContext().getFlash().get("fromBankAccountNumWithType").toString();
        transferAmt = "S$" + FacesContext.getCurrentInstance().getExternalContext().getFlash().get("transferAmt").toString();
        fromAccount = FacesContext.getCurrentInstance().getExternalContext().getFlash().get("fromAccount").toString();
        toAccount = FacesContext.getCurrentInstance().getExternalContext().getFlash().get("toAccount").toString();
        fromAccountAvailableBalance = "S$" + FacesContext.getCurrentInstance().getExternalContext().getFlash().get("fromAccountAvailableBalance").toString();
        fromAccountTotalBalance = "S$" + FacesContext.getCurrentInstance().getExternalContext().getFlash().get("fromAccountTotalBalance").toString();
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(String fromAccount) {
        this.fromAccount = fromAccount;
    }

    public String getToAccount() {
        return toAccount;
    }

    public void setToAccount(String toAccount) {
        this.toAccount = toAccount;
    }

    public String getTransferAmt() {
        return transferAmt;
    }

    public void setTransferAmt(String transferAmt) {
        this.transferAmt = transferAmt;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getToBankAccountNumWithType() {
        return toBankAccountNumWithType;
    }

    public void setToBankAccountNumWithType(String toBankAccountNumWithType) {
        this.toBankAccountNumWithType = toBankAccountNumWithType;
    }

    public String getFromBankAccountNumWithType() {
        return fromBankAccountNumWithType;
    }

    public void setFromBankAccountNumWithType(String fromBankAccountNumWithType) {
        this.fromBankAccountNumWithType = fromBankAccountNumWithType;
    }

    public String getFromAccountAvailableBalance() {
        return fromAccountAvailableBalance;
    }

    public void setFromAccountAvailableBalance(String fromAccountAvailableBalance) {
        this.fromAccountAvailableBalance = fromAccountAvailableBalance;
    }

    public String getFromAccountTotalBalance() {
        return fromAccountTotalBalance;
    }

    public void setFromAccountTotalBalance(String fromAccountTotalBalance) {
        this.fromAccountTotalBalance = fromAccountTotalBalance;
    }
}
