package ejb.payement.session;

import ejb.payment.entity.OtherBankAccountTransaction;
import java.util.List;
import javax.ejb.Local;

@Local
public interface OtherTransactionSessionBeanLocal {
    public List<OtherBankAccountTransaction> retrieveAccTransactionByBankNum(String otherBankAccountNum);
    public Long addNewOtherTransaction(String otherTransactionDate, String otherTransactionCode, String otherTransactionRef,
            String otherAccountDebit, String otherAccountCredit, Long otherBankAccountId);
}
