package ejb.payment.session;

import ejb.payment.entity.Cheque;
import javax.ejb.Local;

@Local
public interface ChequeSessionBeanLocal {
    public Cheque retrieveGIROById(Long chequeId);
}
