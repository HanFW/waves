package managedbean.payment.customer;

import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;

@Named(value = "sWIFTTransferDoneManagedBean")
@RequestScoped

public class SWIFTTransferDoneManagedBean {

    private String statusMessage;
    private String toBankAccountNumWithType;
    private String fromBankAccountNumWithType;
    private String transferAmt;

    public SWIFTTransferDoneManagedBean() {
    }

    @PostConstruct
    public void init() {
        statusMessage = FacesContext.getCurrentInstance().getExternalContext().getFlash().get("statusMessage").toString();
        toBankAccountNumWithType = FacesContext.getCurrentInstance().getExternalContext().getFlash().get("toBankAccountNumWithType").toString();
        fromBankAccountNumWithType = FacesContext.getCurrentInstance().getExternalContext().getFlash().get("fromBankAccountNumWithType").toString();
        transferAmt = "S$" + FacesContext.getCurrentInstance().getExternalContext().getFlash().get("transferAmt").toString();
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

    public String getFromBankAccountNumWithType() {
        return fromBankAccountNumWithType;
    }

    public void setFromBankAccountNumWithType(String fromBankAccountNumWithType) {
        this.fromBankAccountNumWithType = fromBankAccountNumWithType;
    }

    public String getTransferAmt() {
        return transferAmt;
    }

    public void setTransferAmt(String transferAmt) {
        this.transferAmt = transferAmt;
    }
}
