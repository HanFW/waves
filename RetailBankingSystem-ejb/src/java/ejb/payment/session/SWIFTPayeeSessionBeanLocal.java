package ejb.payment.session;

import ejb.payment.entity.SWIFTPayee;
import javax.ejb.Local;

@Local
public interface SWIFTPayeeSessionBeanLocal {
    public Long addNewSWIFTPayee(String swiftPayeeName, String swiftPayeeAccountNum, String swiftPayeeAccountType,
            String swiftPayeeCode, String lastTransactionDate, String swiftPayeeCountry, 
            String payeeBank, Long customerBasicId);
    public SWIFTPayee retrieveFastPayeeById(Long fastPayeeId);
}
