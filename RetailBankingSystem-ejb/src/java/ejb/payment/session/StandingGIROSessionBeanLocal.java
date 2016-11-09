package ejb.payment.session;

import ejb.payment.entity.StandingGIRO;
import java.util.List;
import javax.ejb.Local;

@Local
public interface StandingGIROSessionBeanLocal {

    public Long addNewStandingGIRO(String billingOrganizationName, String billReference, String paymemtLimit,
            String customerName, String customerMobile, String bankAccountNum, String standingGiroStatus,
            String bankAccountNumWithType, String giroType, Long customerBasicId);

    public String deleteStandingGIRO(Long giroId);
    
    public List<StandingGIRO> retrieveStandingGIROByCusId(Long customerBasicId);
    
    public StandingGIRO retrieveStandingGIROByBillRef(String billingReference);
}
