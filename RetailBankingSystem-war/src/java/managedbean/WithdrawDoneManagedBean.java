package managedbean;

import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;

@Named(value = "withdrawDoneManagedBean")
@RequestScoped

public class WithdrawDoneManagedBean {

    private String statusMessage;
    private String withdrawAccountNum;
    private String withdrawAmt;

    public WithdrawDoneManagedBean() {
    }
    
    @PostConstruct
    public void init()
    {
        statusMessage = FacesContext.getCurrentInstance().getExternalContext().getFlash().get("statusMessage").toString();
        withdrawAccountNum = FacesContext.getCurrentInstance().getExternalContext().getFlash().get("withdrawAccountNum").toString();
        withdrawAmt = FacesContext.getCurrentInstance().getExternalContext().getFlash().get("withdrawAmt").toString();
    }
    
    public String getStatusMessage() {
        return statusMessage;
    }
    
    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String getWithdrawAccountNum() {
        return withdrawAccountNum;
    }

    public void setWithdrawAccountNum(String withdrawAccountNum) {
        this.withdrawAccountNum = withdrawAccountNum;
    }

    public String getWithdrawAmt() {
        return withdrawAmt;
    }

    public void setWithdrawAmt(String withdrawAmt) {
        this.withdrawAmt = withdrawAmt;
    }
    
}
