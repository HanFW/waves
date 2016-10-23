package ejb.payment.session;

import ejb.payment.entity.GIRO;
import java.util.List;
import javax.ejb.Local;

@Local
public interface GIROSessionBeanLocal {

    public GIRO retrieveGIROById(Long giroId);
    public String deleteGIRO(Long giroId);
}
