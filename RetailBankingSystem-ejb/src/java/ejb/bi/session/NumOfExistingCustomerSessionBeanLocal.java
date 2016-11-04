package ejb.bi.session;

import ejb.bi.entity.NumOfExistingCustomer;
import javax.ejb.Local;

@Local
public interface NumOfExistingCustomerSessionBeanLocal {
    public NumOfExistingCustomer retrieveNumOfExistingCustomerByDate(Integer updateDate);
    public Long addNewNumOfExistingCustomer(String numOfExistingCustomer, Integer updateDate);
}
