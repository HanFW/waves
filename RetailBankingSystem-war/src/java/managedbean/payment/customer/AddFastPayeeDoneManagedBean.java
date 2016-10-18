package managedbean.payment.customer;

import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;

@Named(value = "addFastPayeeDoneManagedBean")
@RequestScoped

public class AddFastPayeeDoneManagedBean {

    private String statusMessage;
    private Long fastPayeeId;
    private String payeeName;
    private String payeeAccountNum;
    private String payeeAccountType;
    
    public AddFastPayeeDoneManagedBean() {
    }
    
    @PostConstruct
    public void init() {
        statusMessage = FacesContext.getCurrentInstance().getExternalContext().getFlash().get("statusMessage").toString();
        fastPayeeId = (Long) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("fastPayeeId");
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

    public Long getFastPayeeId() {
        return fastPayeeId;
    }

    public void setFastPayeeId(Long fastPayeeId) {
        this.fastPayeeId = fastPayeeId;
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
