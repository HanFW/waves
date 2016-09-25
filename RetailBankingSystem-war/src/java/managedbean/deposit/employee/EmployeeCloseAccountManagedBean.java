package managedbean.deposit.employee;

import ejb.customer.entity.CustomerBasic;
import static ejb.customer.entity.CustomerBasic_.customerName;
import ejb.customer.session.CRMCustomerSessionBeanLocal;
import ejb.deposit.session.BankAccountSessionBeanLocal;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@Named(value = "employeeCloseAccountManagedBean")
@RequestScoped

public class EmployeeCloseAccountManagedBean {
    @EJB
    private BankAccountSessionBeanLocal bankAccountSessionBeanLocal;
    
    @EJB
    private CRMCustomerSessionBeanLocal customerSessionBeanLocal;

    private ExternalContext ec;
    
    private String customerIdentificationNum;
    private Date customerDateOfBirth;
    private String customerName;
    
    public EmployeeCloseAccountManagedBean() {
    }

    public String getCustomerIdentificationNum() {
        return customerIdentificationNum;
    }

    public void setCustomerIdentificationNum(String customerIdentificationNum) {
        this.customerIdentificationNum = customerIdentificationNum;
    }

    public Date getCustomerDateOfBirth() {
        return customerDateOfBirth;
    }

    public void setCustomerDateOfBirth(Date customerDateOfBirth) {
        this.customerDateOfBirth = customerDateOfBirth;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    
    public void submit() throws IOException {
        System.out.println("=");
        System.out.println("====== deposit/EmployeeCloseAccountManagedBean: submit() ======");
        ec = FacesContext.getCurrentInstance().getExternalContext();

        CustomerBasic customerBasic = customerSessionBeanLocal.retrieveCustomerBasicByIC(customerIdentificationNum.toUpperCase());
        
        if (customerBasic.getCustomerBasicId() == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Failed! Customer does not exist.", "Failed!"));
        } else {
            String name = customerBasic.getCustomerName();
            String customerDateOfBirthString = customerBasic.getCustomerDateOfBirth();
            String dateOfBirth = bankAccountSessionBeanLocal.changeDateFormat(customerDateOfBirth);

            if (!name.toUpperCase().equals(customerName.toUpperCase())) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Failed! Customer Name is Wrong.", "Failed!"));
            } else if (!dateOfBirth.equals(customerDateOfBirthString)) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Failed! Customer Date of Birth is Wrong.", "Failed!"));
            } else {
                ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
                Map<String, Object> sessionMap = externalContext.getSessionMap();
                sessionMap.put("customerIdentificationNum", customerIdentificationNum);
                
                ec.redirect(ec.getRequestContextPath() + "/web/internalSystem/deposit/employeeCloseAccountDone.xhtml?faces-redirect=true");
            }
        }
    }
}
