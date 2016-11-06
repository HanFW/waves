package ejb.bi.session;

import ejb.bi.entity.NumOfExistingCustomer;
import java.util.List;
import javax.ejb.Local;

@Local
public interface NumOfExistingCustomerSessionBeanLocal {

    public Long addNewNumOfExistingCustomer(String numOfExistingCustomer, String numOfOpeningAccounts,
            String numOfClosingAccounts, Integer updateMonth, Integer updateYear, String status,
            String currentYeaar);

    public NumOfExistingCustomer retrieveNumOfExistingCustomerByMonth(Integer updateMonth);

    public NumOfExistingCustomer getNumOfExistingCustomer();

    public List<NumOfExistingCustomer> getCurrentYearNumOfExistingCustomer();

    public NumOfExistingCustomer retrievePreviousNumOfExistingCustomerByMonth(Integer updateMonth, Integer updateYear);
}
