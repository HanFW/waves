package managedbean.payment.employee;

import ejb.payment.session.RegisteredBillingOrganizationSessionBeanLocal;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@Named(value = "employeeEnterBillingOrganizationManagedBean")
@RequestScoped

public class EmployeeEnterBillingOrganizationManagedBean {
    @EJB
    private RegisteredBillingOrganizationSessionBeanLocal registeredBillingOrganizationSessionBeanLocal;

    private String billingOrganizationName;
    private String bankAccountNum;
    private String bankAccountType;
    private String bankName;

    private ExternalContext ec;
    
    public EmployeeEnterBillingOrganizationManagedBean() {
    }

    public String getBillingOrganizationName() {
        return billingOrganizationName;
    }

    public void setBillingOrganizationName(String billingOrganizationName) {
        this.billingOrganizationName = billingOrganizationName;
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

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
    
    public void submit() {
        ec = FacesContext.getCurrentInstance().getExternalContext();
        
        registeredBillingOrganizationSessionBeanLocal.addNewRegisteredBillingOrganization(billingOrganizationName, 
                bankAccountNum, bankAccountType, bankName);
        
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Successfully Add New Billing Organization", ""));
    }
}
