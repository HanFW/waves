package ejb.payment.session;

import ejb.payment.entity.OnHoldRecord;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class OnHoldSessionBean implements OnHoldSessionBeanLocal {
    
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long addNewRecord(String bankName, String bankAccountNum, 
            String debitOrCredit, String paymentAmt, String onHoldStatus,
            String debitOrCreditBankName, String debitOrCreditBankAccountNum,
            String paymentMethod) {
        
        OnHoldRecord onHoldRecord = new OnHoldRecord();
        
        onHoldRecord.setBankAccountNum(bankAccountNum);
        onHoldRecord.setBankName(bankName);
        onHoldRecord.setDebitOrCredit(debitOrCredit);
        onHoldRecord.setPaymentAmt(paymentAmt);
        onHoldRecord.setOnHoldStatus(onHoldStatus);
        onHoldRecord.setDebitOrCreditBankName(debitOrCreditBankName);
        onHoldRecord.setDebitOrCreditBankAccountNum(debitOrCreditBankAccountNum);
        onHoldRecord.setPaymentMethod(paymentMethod);
        
        entityManager.persist(onHoldRecord);
        entityManager.flush();
        
        return onHoldRecord.getOnHoldRecordId();
    }
    
    @Override
    public OnHoldRecord retrieveOnHoldRecordById(Long onHoldRecordId) {
        OnHoldRecord onHoldRecord = new OnHoldRecord();

        try {
            Query query = entityManager.createQuery("Select o From OnHoldRecord o Where o.onHoldRecordId=:onHoldRecordId");
            query.setParameter("onHoldRecordId", onHoldRecordId);

            if (query.getResultList().isEmpty()) {
                return new OnHoldRecord();
            } else {
                onHoldRecord = (OnHoldRecord) query.getSingleResult();
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
            return new OnHoldRecord();
        } catch (NonUniqueResultException nure) {
            System.out.println("Non unique result error: " + nure.getMessage());
        }

        return onHoldRecord;
    }
    
    @Override
    public void updateOnHoldChequeNum(Long onHoldRecordId,String chequeNum) {
        OnHoldRecord onHoldRecord = retrieveOnHoldRecordById(onHoldRecordId);
        onHoldRecord.setChequeNum(chequeNum);
    }
}
