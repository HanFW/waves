package managedbean.payment.employee;

import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;

@Named(value = "employeeAddBillingOrganizationFinalManagedBean")
@RequestScoped

public class EmployeeAddBillingOrganizationFinalManagedBean {

    private String statusMessage;
    private String giroId;
    private String updateDate;
    private String billingOrganization;
    private String billReference;

    public EmployeeAddBillingOrganizationFinalManagedBean() {
    }

    @PostConstruct
    public void init() {
        statusMessage = FacesContext.getCurrentInstance().getExternalContext().getFlash().get("statusMessage").toString();
        giroId = FacesContext.getCurrentInstance().getExternalContext().getFlash().get("giroId").toString();
        updateDate = FacesContext.getCurrentInstance().getExternalContext().getFlash().get("updateDate").toString();
        billingOrganization = FacesContext.getCurrentInstance().getExternalContext().getFlash().get("billingOrganization").toString();
        billReference = FacesContext.getCurrentInstance().getExternalContext().getFlash().get("billReference").toString();
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String getGiroId() {
        return giroId;
    }

    public void setGiroId(String giroId) {
        this.giroId = giroId;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getBillingOrganization() {
        return billingOrganization;
    }

    public void setBillingOrganization(String billingOrganization) {
        this.billingOrganization = billingOrganization;
    }

    public String getBillReference() {
        return billReference;
    }

    public void setBillReference(String billReference) {
        this.billReference = billReference;
    }
}
