package ejb.deposit.session;

import ejb.deposit.entity.BankAccount;
import ejb.deposit.entity.Interest;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class InterestSessionBean implements InterestSessionBeanLocal, InterestSessionBeanRemote {

    @EJB
    private BankAccountSessionBeanLocal bankAccountSessionLocal;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long addNewInterest(String dailyInterest, String monthlyInterest, String isTransfer, String isWithdraw) {

        System.out.println("*");
        System.out.println("****** deposit/InterestSessionBean: addNewInterest() ******");
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
        System.out.println("*");
        System.out.println("****** deposit/InterestSessionBean: retrieveInterestById() ******");
        Interest interest = new Interest();

        try {
            Query query = entityManager.createQuery("Select i From Interest i Where i.interestId=:interestId");
            query.setParameter("interestId", interestId);

            if (query.getResultList().isEmpty()) {
                System.out.println("****** deposit/InterestSessionBean: retrieveInterestById(): invalid interest Id: no result found, return new interest");
                return new Interest();
            } else {
                System.out.println("****** deposit/InterestSessionBean: retrieveInterestById(): valid interest Id: return interest");
                interest = (Interest) query.getSingleResult();
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("****** deposit/InterestSessionBean: retrieveInterestById(): Entity not found error: " + enfe.getMessage());
            return new Interest();
        } catch (NonUniqueResultException nure) {
            System.out.println("****** deposit/InterestSessionBean: retrieveInterestById(): Non unique result error: " + nure.getMessage());
        }

        return interest;
    }

    @Override
    public Interest retrieveInterestByAccountNum(String bankAccountNum) {
        System.out.println("*");
        System.out.println("****** deposit/InterestSessionBean: retrieveInterestByAccountNum() ******");
        BankAccount bankAccount = bankAccountSessionLocal.retrieveBankAccountByNum(bankAccountNum);
        Interest interest = new Interest();

        if (bankAccount.getBankAccountId() == null) {
            System.out.println("****** deposit/InterestSessionBean: retrieveInterestByAccountNum(): invalid bank account number: no result found, return new interest");
            return new Interest();
        }
        try {
            Query query = entityManager.createQuery("Select i From Interest i Where i.bankAccount=:bankAccount");
            query.setParameter("bankAccount", bankAccount);

            if (query.getResultList().isEmpty()) {
                System.out.println("****** deposit/InterestSessionBean: retrieveInterestByAccountNum(): wrong bank account number: return new interest");
                return new Interest();
            } else {
                System.out.println("****** deposit/InterestSessionBean: retrieveInterestByAccountNum(): correct bank account number: return interest");
                interest = (Interest) query.getResultList();
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("****** deposit/InterestSessionBean: retrieveInterestByAccountNum(): Entity not found error: " + enfe.getMessage());
            return null;
        } catch (NonUniqueResultException nure) {
            System.out.println("****** deposit/InterestSessionBean: retrieveInterestByAccountNum(): Non unique result error: " + nure.getMessage());
        }

        return interest;
    }
    
    @Override
    public String deleteInterest(Long interestId) {
        System.out.println("*");
        System.out.println("****** deposit/InterestSessionBean: deleteInterest() ******");
        Interest interest = retrieveInterestById(interestId);
        
        entityManager.remove(interest);
        entityManager.flush();
        
        return "Successfully Deleted!";
        
    }
}
