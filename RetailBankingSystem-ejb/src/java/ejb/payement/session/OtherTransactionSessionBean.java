package ejb.payement.session;

import ejb.payment.entity.OtherBankAccount;
import ejb.payment.entity.OtherBankAccountTransaction;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class OtherTransactionSessionBean implements OtherTransactionSessionBeanLocal {

    @EJB
    private OtherBankAccountSessionBeanLocal otherBankAccountSessionBeanLocal;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<OtherBankAccountTransaction> retrieveAccTransactionByBankNum(String otherBankAccountNum) {
        
        OtherBankAccount otherBankAccount = otherBankAccountSessionBeanLocal.retrieveBankAccountByNum(otherBankAccountNum);

        if (otherBankAccount == null) {
            return new ArrayList<OtherBankAccountTransaction>();
        }
        try {
            Query query = entityManager.createQuery("Select o From OtherBankAccountTransaction o Where o.otherBankAccount=:otherBankAccount");
            query.setParameter("otherBankAccount", otherBankAccount);

            if (query.getResultList().isEmpty()) {
                return new ArrayList<OtherBankAccountTransaction>();
            } else {
                return query.getResultList();
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());

            return new ArrayList<OtherBankAccountTransaction>();
        }
    }
    
    @Override
    public Long addNewOtherTransaction(String otherTransactionDate, String otherTransactionCode, String otherTransactionRef,
            String otherAccountDebit, String otherAccountCredit, Long otherBankAccountId) {
        
        OtherBankAccountTransaction otherBankAccountTransaction = new OtherBankAccountTransaction();

        otherBankAccountTransaction.setOtherTransactionRef(otherTransactionRef);
        otherBankAccountTransaction.setOtherAccountCredit(otherAccountCredit);
        otherBankAccountTransaction.setOtherAccountDebit(otherAccountDebit);
        otherBankAccountTransaction.setOtherTransactionCode(otherTransactionCode);
        otherBankAccountTransaction.setOtherTransactionDate(otherTransactionDate);
        otherBankAccountTransaction.setOtherBankAccount(otherBankAccountSessionBeanLocal.retrieveBankAccountById(otherBankAccountId));

        entityManager.persist(otherBankAccountTransaction);
        entityManager.flush();

        return otherBankAccountTransaction.getOtherTransactionId();
    }
}
