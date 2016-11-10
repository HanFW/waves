package managedbean.bi.employee;

import ejb.bi.entity.CustomerRFM;
import ejb.bi.session.CustomerRFMSessionBeanLocal;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;

@Named(value = "employeeViewLoanDoneManagedBean")
@SessionScoped

public class EmployeeViewLoanDoneManagedBean implements Serializable {

    @EJB
    private CustomerRFMSessionBeanLocal customerRFMSessionBeanLocal;

    private String customerIdentificationNum;
    private ExternalContext ec;

    public EmployeeViewLoanDoneManagedBean() {
    }

    public String getCustomerIdentificationNum() {
        return customerIdentificationNum;
    }

    public void setCustomerIdentificationNum(String customerIdentificationNum) {
        this.customerIdentificationNum = customerIdentificationNum;
    }

    public List<CustomerRFM> getCustomerRFM() throws IOException {

        List<CustomerRFM> customerRFM = customerRFMSessionBeanLocal.retrieveCustomerRFMByCusIC(customerIdentificationNum);
        return customerRFM;
    }
}
