package ejb.payement.session;

import ejb.payment.entity.DBSBankAccount;
import ejb.payment.entity.DBSTransaction;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class DBSTransactionSessionBean implements DBSTransactionSessionBeanLocal {

    @EJB
    private DBSBankAccountSessionBeanLocal dBSBankAccountSessionBeanLocal;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<DBSTransaction> retrieveAccTransactionByBankNum(String dbsBankAccountNum) {
        
        DBSBankAccount dbsBankAccount = dBSBankAccountSessionBeanLocal.retrieveBankAccountByNum(dbsBankAccountNum);

        if (dbsBankAccount == null) {
            return new ArrayList<DBSTransaction>();
        }
        try {
            Query query = entityManager.createQuery("Select t From DBSTransaction t Where t.dbsBankAccount=:dbsBankAccount");
            query.setParameter("dbsBankAccount", dbsBankAccount);

            if (query.getResultList().isEmpty()) {
                return new ArrayList<DBSTransaction>();
            } else {
                return query.getResultList();
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());

            return new ArrayList<DBSTransaction>();
        }
    }
    
    @Override
    public Long addNewDBSTransaction(String dbsTransactionDate, String dbsTransactionCode, String dbsTransactionRef,
            String dbsAccountDebit, String dbsAccountCredit, Long dbsBankAccountId) {
        
        DBSTransaction dbsTransaction = new DBSTransaction();

        dbsTransaction.setDbsTransactionDate(dbsTransactionDate);
        dbsTransaction.setDbsTransactionCode(dbsTransactionCode);
        dbsTransaction.setDbsTransactionRef(dbsTransactionRef);
        dbsTransaction.setDbsAccountDebit(dbsAccountDebit);
        dbsTransaction.setDbsAccountCredit(dbsAccountCredit);
        dbsTransaction.setDbsBankAccount(dBSBankAccountSessionBeanLocal.retrieveBankAccountById(dbsBankAccountId));

        entityManager.persist(dbsTransaction);
        entityManager.flush();

        return dbsTransaction.getDbsTransactionId();
    }
}
