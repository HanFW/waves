package ejb.payement.session;

import ejb.payment.entity.SACHMasterAccountTransaction;
import ejb.payment.entity.SACHMasterBankAccount;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class SACHMasterAccountTransactionSessionBean implements SACHMasterAccountTransactionSessionBeanLocal {

    @EJB
    private SACHMasterBankAccountSessionBeanLocal sACHMasterBankAccountSessionBeanLocal;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<SACHMasterAccountTransaction> retrieveSACHMasterAccountTransactionByAccNum(String sachMasterAccountNum) {

        SACHMasterBankAccount sachMasterBankAccount = sACHMasterBankAccountSessionBeanLocal.retrieveSACHMasterBankAccountByAccNum(sachMasterAccountNum);
        
        if (sachMasterBankAccount == null) {
            return new ArrayList<SACHMasterAccountTransaction>();
        }
        try {
            Query query = entityManager.createQuery("Select t From SACHMasterAccountTransaction t Where t.sachMasterBankAccount=:sachMasterBankAccount");
            query.setParameter("sachMasterBankAccount", sachMasterBankAccount);

            if (query.getResultList().isEmpty()) {
                return new ArrayList<SACHMasterAccountTransaction>();
            } else {
                return query.getResultList();
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
            return null;
        }
    }
    
    @Override
    public Long addNewMasterAccountTransaction(String transactionDate, String transactionRef,
            String accountDebit, String accountCredit, Long masterBankAccountId) {
        
        SACHMasterAccountTransaction sachMasterAccountTransaction = new SACHMasterAccountTransaction();

        sachMasterAccountTransaction.setTransactionDate(transactionDate);
        sachMasterAccountTransaction.setTransactionRef(transactionRef);
        sachMasterAccountTransaction.setAccountDebit(accountDebit);
        sachMasterAccountTransaction.setAccountCredit(accountCredit);
        sachMasterAccountTransaction.setSachMasterBankAccount(sACHMasterBankAccountSessionBeanLocal.retrieveSACHMasterBankAccountById(masterBankAccountId));

        entityManager.persist(sachMasterAccountTransaction);
        entityManager.flush();

        return sachMasterAccountTransaction.getTransactionId();
    }
}
