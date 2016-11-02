package ejb.payment.session;

import ejb.payment.entity.OtherBankPayee;
import java.util.List;
import javax.ejb.Local;

@Local
public interface OtherBankPayeeSessionBeanLocal {
    public OtherBankPayee retrieveOtherBankPayeeById(Long payeeId);
    public List<OtherBankPayee> retrieveOtherBankPayeeByCusId(Long customerBasicId);
    public Long addNewOtherBankPayee(String otherBankPayeeName, String otherBankPayeeAccountNum, 
            String otherBankPayeeAccountType, String lastTransactionDate, String payeeType, 
            Long customerBasicId);
    public OtherBankPayee retrieveOtherBankPayeeByNum(String payeeAccountNum);
}
