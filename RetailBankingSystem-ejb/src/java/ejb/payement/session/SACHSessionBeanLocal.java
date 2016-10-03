package ejb.payement.session;

import ejb.payment.entity.SACH;
import javax.ejb.Local;

@Local
public interface SACHSessionBeanLocal {
    public void SACHTransfer(String fromBankAccount, String toBankAccount, Double transferAmt);
    public SACH retrieveSACHById(Long sachId);
}
