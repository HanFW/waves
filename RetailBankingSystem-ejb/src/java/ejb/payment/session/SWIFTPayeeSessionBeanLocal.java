package ejb.payment.session;

import ejb.payment.entity.SWIFTPayee;
import java.util.List;
import javax.ejb.Local;

@Local
public interface SWIFTPayeeSessionBeanLocal {
    public Long addNewSWIFTPayee(String swiftPayeeName, String swiftPayeeAccountNum, String swiftPayeeAccountType,
            String swiftPayeeCode, String lastTransactionDate, String swiftPayeeCountry, 
            String payeeBank, Long customerBasicId);
    public SWIFTPayee retrieveSWIFTPayeeById(Long swiftPayeeId);
    public List<SWIFTPayee> retrieveSWIFTPayeeByCusIC(String customerIdentificationNum);
    public SWIFTPayee retrieveSWIFTPayeeByInstitution(String swiftInstitution);
}
