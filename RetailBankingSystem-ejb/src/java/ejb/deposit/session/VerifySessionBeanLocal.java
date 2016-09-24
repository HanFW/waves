package ejb.deposit.session;

import ejb.deposit.entity.Verify;
import javax.ejb.Local;

@Local
public interface VerifySessionBeanLocal {
    public Long addNewVerify(String customerName,String customerIdentificationType,
            String customerIdentificationNum,String identification);
    public Verify retrieveVerifyByCusIc(String customerIdentificationNum);
}
