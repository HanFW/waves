package ejb.payment.session;

import javax.ejb.Local;

@Local
public interface MerlionBankSessionBeanLocal {

    public void approveAccount(String customerIdentificationNum);

    public void debitBankAccount(String debitBankAccountNum, Double debitAmt);

    public void sendEmailToRejectCustomer(String customerIdentificationNum);
}
