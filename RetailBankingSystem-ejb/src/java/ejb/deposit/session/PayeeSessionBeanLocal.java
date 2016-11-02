package ejb.deposit.session;

import ejb.deposit.entity.Payee;
import java.util.List;
import javax.ejb.Local;

@Local
public interface PayeeSessionBeanLocal {

    public String deletePayee(String payeeAccountNum);

    public Payee retrievePayeeById(Long payeeId);

    public List<Payee> retrievePayeeByCusId(Long customerBasicId);

    public Payee retrievePayeeByNum(String payeeAccountNum);

    public void updateLastTransactionDate(String bankAccountNum);
}
