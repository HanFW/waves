package session.stateless;

import entity.BankAccount;
import entity.Interest;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
@LocalBean

public class InterestSession implements InterestSessionLocal {

    @EJB
    private EjbTimerSessionLocal ejbTimerSessionLocal;

    @EJB
    private BankAccountSessionLocal bankAccountSessionLocal;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long addNewInterest(String dailyInterest, String monthlyInterest, boolean isTransfer, boolean isWithdraw) {

        Interest interest = new Interest();

        interest.setDailyInterest(dailyInterest);
        interest.setMonthlyInterest(monthlyInterest);
        interest.setIsTransfer(isTransfer);
        interest.setIsWithdraw(isWithdraw);

        entityManager.persist(interest);
        entityManager.flush();

        return interest.getInterestId();
    }

    @Override
    public Interest retrieveInterestById(Long interestId) {
        Interest interest = new Interest();

        try {
            Query query = entityManager.createQuery("Select i From Interest i Where i.interestId=:interestId");
            query.setParameter("interestId", interestId);
            if (query.getResultList().isEmpty()) {
                return new Interest();
            } else {
                interest = (Interest) query.getSingleResult();
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error: " + enfe.getMessage());
            return new Interest();
        } catch (NonUniqueResultException nure) {
            System.out.println("\nNon unique result error: " + nure.getMessage());
        }

        return interest;
    }

    @Override
    public Interest retrieveInterestByAccountNum(String bankAccountNum) {
        BankAccount bankAccount = bankAccountSessionLocal.retrieveBankAccountByNum(bankAccountNum);
        Interest interest = new Interest();

        if (bankAccount == null) {
            return null;
        }
        try {
            Query query = entityManager.createQuery("Select i From Interest i Where i.bankAccount=:bankAccount");
            query.setParameter("bankAccount", bankAccount);
            interest = (Interest) query.getResultList();
        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error: " + enfe.getMessage());

            return null;
        } catch (NonUniqueResultException nure) {
            System.out.println("\nNon unique result error: " + nure.getMessage());
        }

        return interest;
    }

    @Override
    public Double accuredInterest(String bankAccountNum) {
        BankAccount bankAccount = bankAccountSessionLocal.retrieveBankAccountByNum(bankAccountNum);
        Double currentInterest = Double.valueOf(bankAccount.getInterest().getDailyInterest());
        Double currentBalance = Double.valueOf(bankAccount.getBankAccountBalance());
       
        Double accuredInterest = currentInterest + currentBalance*0.005;
        
        return accuredInterest;
    }

    @Override
    public void creditedInterest(String bankAccountNum) {

    }

    @Override
    public void calculateInterest(String bankAccountNum) {

    }
}
