package ejb.payement.session;

import ejb.payment.entity.DBSBankAccount;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class DBSBankAccountSessionBean implements DBSBankAccountSessionBeanLocal {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<DBSBankAccount> getAllDBSBankAccount() {
        Query query = entityManager.createQuery("SELECT ba FROM DBSBankAccount ba");

        return query.getResultList();
    }

    @Override
    public DBSBankAccount retrieveBankAccountByNum(String dbsBankAccountNum) {
        DBSBankAccount dbsBankAccount = new DBSBankAccount();

        try {
            Query query = entityManager.createQuery("Select ba From DBSBankAccount ba Where ba.dbsBankAccountNum=:dbsBankAccountNum");
            query.setParameter("dbsBankAccountNum", dbsBankAccountNum);

            if (query.getResultList().isEmpty()) {
                return new DBSBankAccount();
            } else {
                dbsBankAccount = (DBSBankAccount) query.getSingleResult();
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
            return new DBSBankAccount();
        } catch (NonUniqueResultException nure) {
            System.out.println("Non unique result error: " + nure.getMessage());
        }

        return dbsBankAccount;
    }
    
    @Override
    public DBSBankAccount retrieveBankAccountById(Long dbsBankAccountId) {
        DBSBankAccount dbsBankAccount = new DBSBankAccount();

        try {
            Query query = entityManager.createQuery("Select ba From DBSBankAccount ba Where ba.dbsBankAccountId=:dbsBankAccountId");
            query.setParameter("dbsBankAccountId", dbsBankAccountId);

            if (query.getResultList().isEmpty()) {
                return new DBSBankAccount();
            } else {
                dbsBankAccount = (DBSBankAccount) query.getSingleResult();
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
            return new DBSBankAccount();
        } catch (NonUniqueResultException nure) {
            System.out.println("Non unique result error: " + nure.getMessage());
        }

        return dbsBankAccount;
    }
}
