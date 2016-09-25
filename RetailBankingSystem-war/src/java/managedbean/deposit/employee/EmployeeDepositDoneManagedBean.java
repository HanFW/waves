package managedbean.deposit.employee;

import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;

@Named(value = "employeeDepositDoneManagedBean")
@RequestScoped

public class EmployeeDepositDoneManagedBean {
    
    private String statusMessage;
    private String depositAccountNum;
    private String depositAmt;
    private String transactionId;
    
    public EmployeeDepositDoneManagedBean() {
    }
    
    @PostConstruct
    public void init()
    {
        statusMessage = FacesContext.getCurrentInstance().getExternalContext().getFlash().get("statusMessage").toString();
        depositAccountNum = FacesContext.getCurrentInstance().getExternalContext().getFlash().get("depositAccountNum").toString();
        depositAmt = FacesContext.getCurrentInstance().getExternalContext().getFlash().get("depositAmt").toString();
        transactionId = FacesContext.getCurrentInstance().getExternalContext().getFlash().get("transactionId").toString();
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
    
    public String getStatusMessage() {
        return statusMessage;
    }
    
    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String getDepositAccountNum() {
        return depositAccountNum;
    }

    public void setDepositAccountNum(String depositAccountNum) {
        this.depositAccountNum = depositAccountNum;
    }

    public String getDepositAmt() {
        return depositAmt;
    }

    public void setDepositAmt(String depositAmt) {
        this.depositAmt = depositAmt;
    }
}
