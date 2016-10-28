package ejb.payment.session;

import ejb.payment.entity.FastPayee;
import java.util.List;
import javax.ejb.Local;

@Local
public interface FastPayeeSessionBeanLocal {
    public FastPayee retrieveFastPayeeById(Long fastPayeeId);
    public List<FastPayee> retrieveFastPayeeByCusId(Long customerBasicId);
    public Long addNewFastPayee(String fastPayeeName, String fastPayeeAccountNum, String fasrPayeeAccountType,
            String lastTransactionDate, String payeeType, Long customerBasicId);
    public FastPayee retrieveFastPayeeByNum(String fastPayeeAccountNum);
}
