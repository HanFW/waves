package ejb.payement.session;

import javax.ejb.Local;

@Local
public interface OtherBankSessionBeanLocal {
    public void actualTransfer(String fromAccountNum, String toAccountNum, Double transferAmt);
}
