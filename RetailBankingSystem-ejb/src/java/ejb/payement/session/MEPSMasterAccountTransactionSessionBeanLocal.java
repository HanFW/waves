package ejb.payement.session;

import ejb.payment.entity.MEPSMasterAccountTransaction;
import java.util.List;
import javax.ejb.Local;

@Local
public interface MEPSMasterAccountTransactionSessionBeanLocal {
    public List<MEPSMasterAccountTransaction> retrieveMEPSMasterAccountTransactionByAccNum(String mepsMasterAccountNum);
    public Long addNewMasterAccountTransaction(String transactionDate, String transactionRef,
            String accountDebit, String accountCredit, Long masterBankAccountId);
}
