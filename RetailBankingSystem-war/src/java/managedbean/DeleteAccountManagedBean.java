package managedbean;

import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;

@Named(value = "deleteAccountManagedBean")
@RequestScoped

public class DeleteAccountManagedBean {

    private String statusMessage;
    private String bankAccountNum;
    private String bankAccountType;

    public DeleteAccountManagedBean() {
    }

    @PostConstruct
    public void init() {
        statusMessage = FacesContext.getCurrentInstance().getExternalContext().getFlash().get("statusMessage").toString();
        bankAccountNum = FacesContext.getCurrentInstance().getExternalContext().getFlash().get("bankAccountNum").toString();
        bankAccountType = FacesContext.getCurrentInstance().getExternalContext().getFlash().get("bankAccountType").toString();
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
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
}
