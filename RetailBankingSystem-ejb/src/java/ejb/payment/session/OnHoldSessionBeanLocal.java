package ejb.payment.session;

import ejb.payment.entity.OnHoldRecord;
import javax.ejb.Local;

@Local
public interface OnHoldSessionBeanLocal {

    public Long addNewRecord(String bankName, String bankAccountNum,
            String debitOrCredit, String paymentAmt, String onHoldStatus,
            String debitOrCreditBankName, String debitOrCreditBankAccountNum,
            String paymentMethod);

    public OnHoldRecord retrieveOnHoldRecordById(Long onHoldRecordId);

    public void updateOnHoldChequeNum(Long onHoldRecordId, String chequeNum);
}
