package ejb.deposit.session;

import ejb.deposit.entity.Payee;
import ejb.deposit.entity.RegularPayee;
import java.util.List;
import javax.ejb.Local;

@Local
public interface RegularPayeeSessionBeanLocal {

    public Long addNewRegularPayee(String payeeName, String payeeAccountNum, String payeeAccountType,
            String lastTransactionDate, String payeeType, Long customerBasicId);

    public List<RegularPayee> retrieveRegularPayeeByCusId(Long customerBasicId);
    
    public Payee retrieveRegularPayeeByName(String payeeName);
}
