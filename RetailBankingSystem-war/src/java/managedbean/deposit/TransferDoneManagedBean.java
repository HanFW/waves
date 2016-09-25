package managedbean.deposit;

import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;

@Named(value = "transferDoneManagedBean")
@RequestScoped

public class TransferDoneManagedBean {

    private String statusMessage;
    private String fromAccount;
    private String toAccount;
    private String transferAmt;
    private String transactionId;
    private String toBankAccountNumWithType;
    private String fromBankAccountNumWithType;
    private String fromBankAccountBalance;
    private String toBankAccountBalance;
    private String customerName;

    private boolean visible;

    public TransferDoneManagedBean() {
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
        fromBankAccountBalance = "S$" + FacesContext.getCurrentInstance().getExternalContext().getFlash().get("fromAccountBalance").toString();
        toBankAccountBalance = "S$" + FacesContext.getCurrentInstance().getExternalContext().getFlash().get("toAccountBalance").toString();
        customerName = FacesContext.getCurrentInstance().getExternalContext().getFlash().get("customerName").toString();
    }

    public void show() {
        visible = true;
    }

    public void hide() {
        visible = false;
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

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public String getFromBankAccountNumWithType() {
        return fromBankAccountNumWithType;
    }

    public void setFromBankAccountNumWithType(String fromBankAccountNumWithType) {
        this.fromBankAccountNumWithType = fromBankAccountNumWithType;
    }

    public String getFromBankAccountBalance() {
        return fromBankAccountBalance;
    }

    public void setFromBankAccountBalance(String fromBankAccountBalance) {
        this.fromBankAccountBalance = fromBankAccountBalance;
    }

    public String getToBankAccountBalance() {
        return toBankAccountBalance;
    }

    public void setToBankAccountBalance(String toBankAccountBalance) {
        this.toBankAccountBalance = toBankAccountBalance;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}
