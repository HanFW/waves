package ejb.payement.session;

import javax.ejb.Local;

@Local
public interface DBSBankSessionBeanLocal {
    public void actualTransfer(String fromAccountNum, String toAccountNum, Double transferAmt);
}
