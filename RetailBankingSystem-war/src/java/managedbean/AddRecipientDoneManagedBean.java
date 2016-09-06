package managedbean;

import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;

@Named(value = "addRecipientDoneManagedBean")
@RequestScoped

public class AddRecipientDoneManagedBean {

    private String statusMessage;
    private Long newPayeeId;
    private String payeeName;
    private String payeeAccountNum;
    private String payeeAccountType;
    
    public AddRecipientDoneManagedBean() {
    }
    
    @PostConstruct
    public void init()
    {
         statusMessage = FacesContext.getCurrentInstance().getExternalContext().getFlash().get("statusMessage").toString();
         newPayeeId = (Long)FacesContext.getCurrentInstance().getExternalContext().getFlash().get("newPayeeId");
         payeeName = FacesContext.getCurrentInstance().getExternalContext().getFlash().get("payeeName").toString();
         payeeAccountNum = FacesContext.getCurrentInstance().getExternalContext().getFlash().get("payeeAccountNum").toString();
         payeeAccountType = FacesContext.getCurrentInstance().getExternalContext().getFlash().get("payeeAccountType").toString();
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public Long getNewPayeeId() {
        return newPayeeId;
    }

    public void setNewPayeeId(Long newPayeeId) {
        this.newPayeeId = newPayeeId;
    }

    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }

    public String getPayeeAccountNum() {
        return payeeAccountNum;
    }

    public void setPayeeAccountNum(String payeeAccountNum) {
        this.payeeAccountNum = payeeAccountNum;
    }

    public String getPayeeAccountType() {
        return payeeAccountType;
    }

    public void setPayeeAccountType(String payeeAccountType) {
        this.payeeAccountType = payeeAccountType;
    }
}
