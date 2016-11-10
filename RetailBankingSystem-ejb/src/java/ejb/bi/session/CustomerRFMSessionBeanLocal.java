package ejb.bi.session;

import ejb.bi.entity.CustomerRFM;
import java.util.List;
import javax.ejb.Local;

@Local
public interface CustomerRFMSessionBeanLocal {

    public Long addNewCustomerRFM(String customerName, String rValue, String fValue, String mValue,
            Integer updateMonth, Integer updateYear, Long startTimeMilis, Long transactionDays,
            Integer numOfTransactions, Double totalSpends, String totalRFMValue,
            String RFMType, Long customerBasicId);

    public void generateMonthlyCustomerRFM();

    public void generateLoanMonthlyRFM();

    public List<CustomerRFM> retrieveCustomerRFMByCusIC(String customerIdentificationNum);
}
