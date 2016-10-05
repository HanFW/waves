package ejb.payement.session;

import ejb.payment.entity.SACHMasterBankAccount;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class SACHMasterBankAccountSessionBean implements SACHMasterBankAccountSessionBeanLocal {

    @PersistenceContext
    private EntityManager entityManager;
    
    @Override
    public SACHMasterBankAccount retrieveSACHMasterBankAccountByBankName(String bankName) {
        
        SACHMasterBankAccount sachMasterBankAccount = new SACHMasterBankAccount();

        try {
            Query query = entityManager.createQuery("Select s From SACHMasterBankAccount s Where s.bankName=:bankName");
            query.setParameter("bankName", bankName);

            if (query.getResultList().isEmpty()) {
                return new SACHMasterBankAccount();
            } else {
                sachMasterBankAccount = (SACHMasterBankAccount) query.getSingleResult();
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
            return new SACHMasterBankAccount();
        } catch (NonUniqueResultException nure) {
            System.out.println("Non unique result error: " + nure.getMessage());
        }

        return sachMasterBankAccount;
    }
    
    @Override
    public SACHMasterBankAccount retrieveSACHMasterBankAccountByAccNum(String masterBankAccountNum) {
        
        SACHMasterBankAccount sachMasterBankAccount = new SACHMasterBankAccount();

        try {
            Query query = entityManager.createQuery("Select s From SACHMasterBankAccount s Where s.masterBankAccountNum=:masterBankAccountNum");
            query.setParameter("masterBankAccountNum", masterBankAccountNum);

            if (query.getResultList().isEmpty()) {
                return new SACHMasterBankAccount();
            } else {
                sachMasterBankAccount = (SACHMasterBankAccount) query.getSingleResult();
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
            return new SACHMasterBankAccount();
        } catch (NonUniqueResultException nure) {
            System.out.println("Non unique result error: " + nure.getMessage());
        }

        return sachMasterBankAccount;
    }
    
    @Override
    public SACHMasterBankAccount retrieveSACHMasterBankAccountById(Long masterBankAccountId) {
        
        SACHMasterBankAccount sachMasterBankAccount = new SACHMasterBankAccount();

        try {
            Query query = entityManager.createQuery("Select s From SACHMasterBankAccount s Where s.masterBankAccountId=:masterBankAccountId");
            query.setParameter("masterBankAccountId", masterBankAccountId);

            if (query.getResultList().isEmpty()) {
                return new SACHMasterBankAccount();
            } else {
                sachMasterBankAccount = (SACHMasterBankAccount) query.getSingleResult();
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
            return new SACHMasterBankAccount();
        } catch (NonUniqueResultException nure) {
            System.out.println("Non unique result error: " + nure.getMessage());
        }

        return sachMasterBankAccount;
    }
}
