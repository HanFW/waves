package managedbean.deposit.employee;

import ejb.deposit.session.BankAccountSessionBeanLocal;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;

@Named(value = "employeeViewIdentificationManagedBean")
@SessionScoped

public class EmployeeViewIdentificationManagedBean implements Serializable{

    @EJB
    private BankAccountSessionBeanLocal bankAccountSessionBeanLocal;

    private Long customerId;
    private String customerName;
    private String customerIdentificationNum;

    public EmployeeViewIdentificationManagedBean() {
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        if(customerId == null) {
            System.out.println("customer id is null set");
        } else {
            System.out.println("customer id is not null set");
            System.out.println(customerId);
        }
        this.customerId = customerId;
    }

    public String getCustomerName() {
//        CustomerBasic customerBasic = bankAccountSessionBeanLocal.retrieveCustomerBasicById(customerId);
//        customerName = customerBasic.getCustomerName();

        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerIdentificationNum() {
//        CustomerBasic customerBasic = bankAccountSessionBeanLocal.retrieveCustomerBasicById(customerId);
//        customerIdentificationNum = customerBasic.getCustomerIdentificationNum();

        return customerIdentificationNum;
    }

    public void setCustomerIdentificationNum(String customerIdentificationNum) {
        if(customerIdentificationNum == null) {
            System.out.println("customer num is null set");
        } else {
            System.out.println("customer num is not null set");
            System.out.println(customerIdentificationNum);
        }
        
        this.customerIdentificationNum = customerIdentificationNum;
    }

}
