package ejb.payement.session;

import ejb.payment.entity.GIRO;
import javax.ejb.Local;

@Local
public interface GIROSessionBeanLocal {

    public GIRO retrieveGIROById(Long giroId);
}
