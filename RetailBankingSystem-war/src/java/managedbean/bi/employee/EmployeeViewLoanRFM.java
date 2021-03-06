package managedbean.bi.employee;

import ejb.customer.entity.CustomerBasic;
import ejb.customer.session.CRMCustomerSessionBeanLocal;
import java.io.IOException;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;

@Named(value = "employeeViewLoanRFM")
@RequestScoped

public class EmployeeViewLoanRFM {

    @EJB
    private CRMCustomerSessionBeanLocal customerSessionBeanLocal;

    private ExternalContext ec;
    private Long customerBasicId;

    public EmployeeViewLoanRFM() {
    }

    public Long getCustomerBasicId() {
        return customerBasicId;
    }

    public void setCustomerBasicId(Long customerBasicId) {
        this.customerBasicId = customerBasicId;
    }

    public List<CustomerBasic> getCustomerBasic() throws IOException {

        List<CustomerBasic> customerBasic = customerSessionBeanLocal.retrieveAllCustomer();
        return customerBasic;
    }
}
