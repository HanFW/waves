package ejb.payment.session;

import ejb.payment.entity.SWIFTPayee;
import java.util.List;
import javax.ejb.Local;

@Local
public interface SWIFTPayeeSessionBeanLocal {

    public Long addNewSWIFTPayee(String swiftInstitution, String swiftPayeeAccountNum, String swiftPayeeAccountType,
            String swiftPayeeCode, String lastTransactionDate, String swiftPayeeCountry,
            String payeeBank, String payeeType, Long customerBasicId);

    public SWIFTPayee retrieveSWIFTPayeeById(Long payeeId);

    public List<SWIFTPayee> retrieveSWIFTPayeeByCusIC(String customerIdentificationNum);

    public SWIFTPayee retrieveSWIFTPayeeByInstitution(String swiftInstitution);

    public SWIFTPayee retrieveSWIFTPayeeByNum(String payeeAccountNum);

    public void updateLastTransactionDate(String payeeAccountNum);
}
