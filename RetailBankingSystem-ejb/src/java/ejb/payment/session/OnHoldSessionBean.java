package ejb.payment.session;

import ejb.payment.entity.OnHoldRecord;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class OnHoldSessionBean implements OnHoldSessionBeanLocal {
    
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long addNewRecord(String bankName, String bankAccountNum, 
            String debitOrCredit, String paymentAmt, String onHoldStatus,
            String debitOrCreditBankName, String debitOrCreditBankAccountNum) {
        
        OnHoldRecord onHoldRecord = new OnHoldRecord();
        
        onHoldRecord.setBankAccountNum(bankAccountNum);
        onHoldRecord.setBankName(bankName);
        onHoldRecord.setDebitOrCredit(debitOrCredit);
        onHoldRecord.setPaymentAmt(paymentAmt);
        onHoldRecord.setOnHoldStatus(onHoldStatus);
        onHoldRecord.setDebitOrCreditBankName(debitOrCreditBankName);
        onHoldRecord.setDebitOrCreditBankAccountNum(debitOrCreditBankAccountNum);
        
        entityManager.persist(onHoldRecord);
        entityManager.flush();
        
        return onHoldRecord.getOnHoldRecordId();
    }
}
