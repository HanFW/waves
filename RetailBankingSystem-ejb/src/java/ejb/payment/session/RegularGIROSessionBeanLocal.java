package ejb.payment.session;

import ejb.payment.entity.RegularGIRO;
import javax.ejb.Local;

@Local
public interface RegularGIROSessionBeanLocal {
    public Long addNewRegularGRIO(String bankAccountNum, String bankAccountNumWithType, String giroType,
            String paymentAmt, String paymentFrequency, String payeeBankName, String payeeBankAccountNum,
            Long customerBasicId);
    public RegularGIRO retrieveRegularGIROById(Long giroId);
    public void updatePaymentAmt(Long giroId, String paymentAmt);
}
