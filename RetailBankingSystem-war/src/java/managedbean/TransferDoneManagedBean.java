package managedbean;

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
    
    public TransferDoneManagedBean() {
    }
    
    @PostConstruct
    public void init()
    {
        statusMessage = FacesContext.getCurrentInstance().getExternalContext().getFlash().get("statusMessage").toString();
        fromAccount = FacesContext.getCurrentInstance().getExternalContext().getFlash().get("fromAccount").toString();
        toAccount = FacesContext.getCurrentInstance().getExternalContext().getFlash().get("toAccount").toString();
        transferAmt = FacesContext.getCurrentInstance().getExternalContext().getFlash().get("transferAmt").toString();
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
}
