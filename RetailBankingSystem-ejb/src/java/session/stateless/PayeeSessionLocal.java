package session.stateless;

import entity.Payee;
import javax.ejb.Local;

@Local
public interface PayeeSessionLocal {
    public Long addNewPayee(String payeeName,String payeeAccountNum,String payeeAccountType,
            String lastTransactionDate,Long customerBasicId);
    public String deletePayee(String payeeName);
    public Payee retrievePayeeById(Long payeeId);
    public Payee retrievePayeeByName(String payeeName);
}
