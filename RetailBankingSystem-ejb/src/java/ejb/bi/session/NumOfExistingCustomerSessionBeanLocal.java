package ejb.bi.session;

import ejb.bi.entity.NumOfExistingCustomer;
import javax.ejb.Local;

@Local
public interface NumOfExistingCustomerSessionBeanLocal {

    public Long addNewNumOfExistingCustomer(String numOfExistingCustomer, Integer updateDate,
            String numOfOpeningAccounts, String numOfClosingAccounts);

    public NumOfExistingCustomer retrieveNumOfExistingCustomerByDate(Integer updateDate);
}
