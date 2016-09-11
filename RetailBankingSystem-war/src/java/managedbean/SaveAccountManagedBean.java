package managedbean;

import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;

@Named(value = "saveAccountManagedBean")
@RequestScoped

public class SaveAccountManagedBean {

    private String statusMessage;
    private String bankAccountId;
    private String customerBasicId;
    private String bankAccountNum;
    private String bankAccountType;
    private String initialDepositAmt;
            
    public SaveAccountManagedBean() {
    }
    
    @PostConstruct
    public void init() {
        statusMessage = FacesContext.getCurrentInstance().getExternalContext().getFlash().get("statusMessage").toString();
        bankAccountId = FacesContext.getCurrentInstance().getExternalContext().getFlash().get("newAccountId").toString();
        customerBasicId = FacesContext.getCurrentInstance().getExternalContext().getFlash().get("newCustomerBasicId").toString();
        bankAccountNum = FacesContext.getCurrentInstance().getExternalContext().getFlash().get("bankAccountNum").toString();
        bankAccountType = FacesContext.getCurrentInstance().getExternalContext().getFlash().get("bankAccountType").toString();
        initialDepositAmt = FacesContext.getCurrentInstance().getExternalContext().getFlash().get("initialDepositAmt").toString();
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
    
    
}
