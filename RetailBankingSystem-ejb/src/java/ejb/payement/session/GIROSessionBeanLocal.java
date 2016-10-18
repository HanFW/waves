package ejb.payement.session;

import ejb.payment.entity.GIRO;
import java.util.List;
import javax.ejb.Local;

@Local
public interface GIROSessionBeanLocal {

    public GIRO retrieveGIROById(Long giroId);
    public List<GIRO> retrieveStandingGIROByCusId(Long customerBasicId);
    public String deleteGIRO(Long giroId);
    public List<GIRO> retrieveNonStandingGIROByCusId(Long customerBasicId);
}
