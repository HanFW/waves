package ejb.payement.session;

import ejb.payment.entity.SACHMasterAccountTransaction;
import java.util.List;
import javax.ejb.Local;

@Local
public interface SACHMasterAccountTransactionSessionBeanLocal {
    public List<SACHMasterAccountTransaction> retrieveSACHMasterAccountTransactionByAccNum(String sachMasterAccountNum);
    public Long addNewMasterAccountTransaction(String transactionDate, String transactionRef,
            String accountDebit, String accountCredit, Long masterBankAccountId);
}
