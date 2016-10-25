package ejb.payment.session;

import javax.ejb.Local;

@Local
public interface OnHoldSessionBeanLocal {

    public Long addNewRecord(String bankName, String bankAccountNum,
            String debitOrCredit, String paymentAmt, String onHoldStatus,
            String debitOrCreditBankName, String debitOrCreditBankAccountNum);
}
