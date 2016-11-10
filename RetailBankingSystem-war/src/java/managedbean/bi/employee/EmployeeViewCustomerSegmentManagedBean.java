package managedbean.bi.employee;

import ejb.bi.entity.CustomerCLV;
import ejb.bi.session.CustomerCLVSessionBeanLocal;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

@Named(value = "employeeViewCustomerSegmentManagedBean")
@RequestScoped

public class EmployeeViewCustomerSegmentManagedBean {

    @EJB
    private CustomerCLVSessionBeanLocal customerCLVSessionBeanLocal;

    public EmployeeViewCustomerSegmentManagedBean() {
    }

    public List<CustomerCLV> getHighCustomerCLV() {
        List<CustomerCLV> highCustomer = customerCLVSessionBeanLocal.retrieveCustomerCLVByLevel("High");
        return highCustomer;
    }

    public List<CustomerCLV> getLowCustomerCLV() {
        List<CustomerCLV> lowCustomer = customerCLVSessionBeanLocal.retrieveCustomerCLVByLevel("Low");
        return lowCustomer;
    }

    public List<CustomerCLV> getMediumCustomerCLV() {
        List<CustomerCLV> mediumCustomer = customerCLVSessionBeanLocal.retrieveCustomerCLVByLevel("Medium");
        return mediumCustomer;
    }
}
