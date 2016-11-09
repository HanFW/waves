package ejb.bi.session;

import javax.ejb.Local;

@Local
public interface CustomerRFMSessionBeanLocal {

    public Long addNewCustomerRFM(String customerName, String rValue, String fValue, String mValue,
            Integer updateMonth, Integer updateYear, Long startTimeMilis, Long transactionDays,
            Integer numOfTransactions, Double totalSpends, Long customerBasicId);

    public void generateMonthlyCustomerRFM();
}
