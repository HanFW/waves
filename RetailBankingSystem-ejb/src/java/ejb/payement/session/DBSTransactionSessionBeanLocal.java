package ejb.payement.session;

import ejb.payment.entity.DBSTransaction;
import java.util.List;
import javax.ejb.Local;

@Local
public interface DBSTransactionSessionBeanLocal {
    public List<DBSTransaction> retrieveAccTransactionByBankNum(String dbsBankAccountNum);
    public Long addNewDBSTransaction(String dbsTransactionDate, String dbsTransactionCode, String dbsTransactionRef,
            String dbsAccountDebit, String dbsAccountCredit, Long dbsBankAccountId);
}
