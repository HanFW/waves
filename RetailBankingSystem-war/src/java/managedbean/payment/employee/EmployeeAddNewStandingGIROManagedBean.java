package managedbean.payment.employee;

import ejb.customer.entity.CustomerBasic;
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

@Named(value = "employeeAddNewStandingGIROManagedBean")
@RequestScoped

public class EmployeeAddNewStandingGIROManagedBean {

    @EJB
    private CRMCustomerSessionBeanLocal customerSessionBeanLocal;

    @EJB
    private BankAccountSessionBeanLocal bankAccountSessionBeanLocal;

    private String customerName;
    private String customerIdentificationNum;
    private Date customerDateOfBirth;
    
    private ExternalContext ec;
    
    public EmployeeAddNewStandingGIROManagedBean() {
    }
    
    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
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

    public void submit() throws IOException {
        System.out.println("=");
        System.out.println("====== deposit/EmployeeChangeDailyTransferLimit: submit() ======");
        ec = FacesContext.getCurrentInstance().getExternalContext();

        CustomerBasic customerBasic = customerSessionBeanLocal.retrieveCustomerBasicByIC(customerIdentificationNum.toUpperCase());

        if (customerBasic.getCustomerBasicId() == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed! Customer does not exist.", "Failed!"));
        } else {
            String name = customerBasic.getCustomerName();
            String customerDateOfBirthString = customerBasic.getCustomerDateOfBirth();
            String dateOfBirth = bankAccountSessionBeanLocal.changeDateFormat(customerDateOfBirth);

            if (!name.toUpperCase().equals(customerName.toUpperCase())) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed! Customer Name is Wrong.", "Failed!"));
            } else if (!dateOfBirth.equals(customerDateOfBirthString)) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed! Customer Date of Birth is Wrong.", "Failed!"));
            } else {

                ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
                Map<String, Object> sessionMap = externalContext.getSessionMap();
                sessionMap.put("customerIdentificationNum", customerIdentificationNum);

                ec.redirect(ec.getRequestContextPath() + "/web/internalSystem/payment/employeeAddStandingGIRODone.xhtml?faces-redirect=true");
            }
        }
    }
}
