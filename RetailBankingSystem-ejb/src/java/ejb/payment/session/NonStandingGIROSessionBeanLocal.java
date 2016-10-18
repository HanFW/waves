package ejb.payment.session;

import ejb.payment.entity.NonStandingGIRO;
import java.util.List;
import javax.ejb.Local;

@Local
public interface NonStandingGIROSessionBeanLocal {
    public Long addNewNonStandingGIRO(String billingOrganization, String billReference, String bankAccountNum,
            String bankAccountNumWithType, String paymentFrequency, String paymentAmt, 
            String giroType, Long customerBasicId);
    public List<NonStandingGIRO> retrieveNonStandingGIROByCusId(Long customerBasicId);
}
