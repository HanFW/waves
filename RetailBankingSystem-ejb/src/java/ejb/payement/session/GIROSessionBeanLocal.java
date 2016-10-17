package ejb.payement.session;

import ejb.payment.entity.GIRO;
import java.util.List;
import javax.ejb.Local;

@Local
public interface GIROSessionBeanLocal {
    public Long addNewGIRO(String billingOrganization, String billReference, String paymemtLimit,
            String customerName, String customerMobile, String bankAccountNum, 
            String giroStatus, String bankAccountNumWithType, Long customerBasicId);
    public GIRO retrieveGIROById(Long giroId);
    public String deleteGIRO(Long giroId);
    public List<GIRO> retrieveGIROByCusId(Long customerBasicId);
}
