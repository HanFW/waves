package ejb.payment.session;

import ejb.payment.entity.NonStandingGIRO;
import java.util.List;
import javax.ejb.Local;

@Local
public interface NonStandingGIROSessionBeanLocal {
    public Long addNewNonStandingGIRO(String billingOrganizationName, String billReference, String bankAccountNum,
            String bankAccountNumWithType, String paymentFrequency, String paymentAmt, 
            String giroType, Long customerBasicId);
    public List<NonStandingGIRO> retrieveNonStandingGIROByCusId(Long customerBasicId);
    public NonStandingGIRO retrieveNonStandingGIROById(Long giroId);
    public void updatePaymentAmt(Long giroId, String paymentAmt);
    public void weeklyRecurrentPayment();
    public List<NonStandingGIRO> retrieveOneTimeGIROByCusId(Long customerBasicId);
    public List<NonStandingGIRO> retrieveRecurrentGIROByCusId(Long customerBasicId);
    public void dailyRecurrentPayment();
    public void monthlyRecurrentPayment();
}
