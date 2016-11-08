package ejb.payment.session;

import ejb.payment.entity.RegularGIRO;
import java.util.List;
import javax.ejb.Local;

@Local
public interface RegularGIROSessionBeanLocal {

    public Long addNewRegularGRIO(String bankAccountNum, String bankAccountNumWithType, String giroType,
            String paymentAmt, String paymentFrequency, String payeeBankName, String payeeBankAccountNum,
            String regularGIROStatus, String payeeName, Long customerBasicId);

    public RegularGIRO retrieveRegularGIROById(Long giroId);

    public void updatePaymentAmt(Long giroId, String paymentAmt);

    public List<RegularGIRO> retrieveRegularGIROByNum(String payeeAccountNum);

    public List<RegularGIRO> retrieveRegularGIROByCusIC(String customerIdentificationNum);

    public String deleteRegularGIRO(Long giroId);
}
