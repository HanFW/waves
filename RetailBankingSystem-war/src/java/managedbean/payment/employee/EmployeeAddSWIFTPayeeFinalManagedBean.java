package managedbean.payment.employee;

import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;

@Named(value = "employeeAddSWIFTPayeeFinalManagedBean")
@RequestScoped

public class EmployeeAddSWIFTPayeeFinalManagedBean {

    private String statusMessage;
    private String payeeInstitution;
    private String payeeAccountNum;
    private String payeeAccountType;
    private String payeeSWIFTCode;
    private String payeeCountry;
    private String payeeBank;
    
    public EmployeeAddSWIFTPayeeFinalManagedBean() {
    }
    
    @PostConstruct
    public void init() {
        statusMessage = FacesContext.getCurrentInstance().getExternalContext().getFlash().get("statusMessage").toString();
        payeeInstitution = FacesContext.getCurrentInstance().getExternalContext().getFlash().get("payeeInstitution").toString();
        payeeAccountNum = FacesContext.getCurrentInstance().getExternalContext().getFlash().get("payeeAccountNum").toString();
        payeeAccountType = FacesContext.getCurrentInstance().getExternalContext().getFlash().get("payeeAccountType").toString();
        payeeSWIFTCode = FacesContext.getCurrentInstance().getExternalContext().getFlash().get("payeeSWIFTCode").toString();
        payeeCountry = FacesContext.getCurrentInstance().getExternalContext().getFlash().get("payeeCountry").toString();
        payeeBank = FacesContext.getCurrentInstance().getExternalContext().getFlash().get("payeeBank").toString();
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String getPayeeInstitution() {
        return payeeInstitution;
    }

    public void setPayeeInstitution(String payeeInstitution) {
        this.payeeInstitution = payeeInstitution;
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

    public String getPayeeSWIFTCode() {
        return payeeSWIFTCode;
    }

    public void setPayeeSWIFTCode(String payeeSWIFTCode) {
        this.payeeSWIFTCode = payeeSWIFTCode;
    }

    public String getPayeeCountry() {
        return payeeCountry;
    }

    public void setPayeeCountry(String payeeCountry) {
        this.payeeCountry = payeeCountry;
    }

    public String getPayeeBank() {
        return payeeBank;
    }

    public void setPayeeBank(String payeeBank) {
        this.payeeBank = payeeBank;
    }
}
