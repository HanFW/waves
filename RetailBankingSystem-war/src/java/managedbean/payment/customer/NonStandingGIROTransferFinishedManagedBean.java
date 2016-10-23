package managedbean.payment.customer;

import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;

@Named(value = "nonStandingGIROTransferFinishedManagedBean")
@RequestScoped

public class NonStandingGIROTransferFinishedManagedBean {

    private String statusMessage;
    private String transactionId;
    private String toBankAccountNumWithType;
    private String transferAmt;
    private String fromBankAccountNumWithType;
    private String fromBankAccountAvailableBalance;
    private String fromBankAccountTotalBalance;

    public NonStandingGIROTransferFinishedManagedBean() {
    }

    @PostConstruct
    public void init() {
        statusMessage = FacesContext.getCurrentInstance().getExternalContext().getFlash().get("statusMessage").toString();
        transactionId = FacesContext.getCurrentInstance().getExternalContext().getFlash().get("transactionId").toString();
        toBankAccountNumWithType = FacesContext.getCurrentInstance().getExternalContext().getFlash().get("toBankAccountNumWithType").toString();
        fromBankAccountNumWithType = FacesContext.getCurrentInstance().getExternalContext().getFlash().get("fromBankAccountNumWithType").toString();
        transferAmt = "S$" + FacesContext.getCurrentInstance().getExternalContext().getFlash().get("transferAmt").toString();
        fromBankAccountAvailableBalance = "S$" + FacesContext.getCurrentInstance().getExternalContext().getFlash().get("fromBankAccountAvailableBalance").toString();
        fromBankAccountTotalBalance = "S$" + FacesContext.getCurrentInstance().getExternalContext().getFlash().get("fromBankAccountTotalBalance").toString();
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String getToBankAccountNumWithType() {
        return toBankAccountNumWithType;
    }

    public void setToBankAccountNumWithType(String toBankAccountNumWithType) {
        this.toBankAccountNumWithType = toBankAccountNumWithType;
    }

    public String getTransferAmt() {
        return transferAmt;
    }

    public void setTransferAmt(String transferAmt) {
        this.transferAmt = transferAmt;
    }

    public String getFromBankAccountNumWithType() {
        return fromBankAccountNumWithType;
    }

    public void setFromBankAccountNumWithType(String fromBankAccountNumWithType) {
        this.fromBankAccountNumWithType = fromBankAccountNumWithType;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getFromBankAccountAvailableBalance() {
        return fromBankAccountAvailableBalance;
    }

    public void setFromBankAccountAvailableBalance(String fromBankAccountAvailableBalance) {
        this.fromBankAccountAvailableBalance = fromBankAccountAvailableBalance;
    }

    public String getFromBankAccountTotalBalance() {
        return fromBankAccountTotalBalance;
    }

    public void setFromBankAccountTotalBalance(String fromBankAccountTotalBalance) {
        this.fromBankAccountTotalBalance = fromBankAccountTotalBalance;
    }
}
