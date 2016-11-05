package ejb.bi.session;

import ejb.bi.entity.AccountClosureReason;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class AccountClosureReasonSessionBean implements AccountClosureReasonSessionBeanLocal {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long addNewAccountClosureReason(String rateValue, String rateName,
            Integer updateMonth, Integer updateYear, String accountClosureReasonStatus,
            String currentYear) {

        AccountClosureReason accountClosureReason = new AccountClosureReason();

        accountClosureReason.setRateName(rateName);
        accountClosureReason.setRateValue(rateValue);
        accountClosureReason.setUpdateMonth(updateMonth);
        accountClosureReason.setUpdateYear(updateYear);
        accountClosureReason.setCurrentYear(currentYear);
        accountClosureReason.setAccountClosureReasonStatus(accountClosureReasonStatus);

        entityManager.persist(accountClosureReason);
        entityManager.flush();

        return accountClosureReason.getAccountClosureReasonId();
    }

    @Override
    public AccountClosureReason getNewInterestAccountClosureReason() {
        Query query = entityManager.createQuery("SELECT a FROM AccountClosureReason a Where a.accountClosureReasonStatus=:accountClosureReasonStatus And a.rateName=:rateName");
        query.setParameter("accountClosureReasonStatus", "New");
        query.setParameter("rateName", "Interest");

        return (AccountClosureReason) query.getSingleResult();
    }

    @Override
    public AccountClosureReason getNewServiceChargeAccountClosureReason() {
        Query query = entityManager.createQuery("SELECT a FROM AccountClosureReason a Where a.accountClosureReasonStatus=:accountClosureReasonStatus And a.rateName=:rateName");
        query.setParameter("accountClosureReasonStatus", "New");
        query.setParameter("rateName", "Service Charge");

        return (AccountClosureReason) query.getSingleResult();
    }

    @Override
    public AccountClosureReason getNewCustomerServiceAccountClosureReason() {
        Query query = entityManager.createQuery("SELECT a FROM AccountClosureReason a Where a.accountClosureReasonStatus=:accountClosureReasonStatus And a.rateName=:rateName");
        query.setParameter("accountClosureReasonStatus", "New");
        query.setParameter("rateName", "Customer Service");

        return (AccountClosureReason) query.getSingleResult();
    }

    @Override
    public AccountClosureReason getNewDontNeedAccountClosureReason() {
        Query query = entityManager.createQuery("SELECT a FROM AccountClosureReason a Where a.accountClosureReasonStatus=:accountClosureReasonStatus And a.rateName=:rateName");
        query.setParameter("accountClosureReasonStatus", "New");
        query.setParameter("rateName", "Dont Need");

        return (AccountClosureReason) query.getSingleResult();
    }

    @Override
    public AccountClosureReason getNewAppliedAnotherAccountClosureReason() {
        Query query = entityManager.createQuery("SELECT a FROM AccountClosureReason a Where a.accountClosureReasonStatus=:accountClosureReasonStatus And a.rateName=:rateName");
        query.setParameter("accountClosureReasonStatus", "New");
        query.setParameter("rateName", "Applied Another");

        return (AccountClosureReason) query.getSingleResult();
    }

    @Override
    public AccountClosureReason getNewOtherReasonsAccountClosureReason() {
        Query query = entityManager.createQuery("SELECT a FROM AccountClosureReason a Where a.accountClosureReasonStatus=:accountClosureReasonStatus And a.rateName=:rateName");
        query.setParameter("accountClosureReasonStatus", "New");
        query.setParameter("rateName", "Other Reasons");

        return (AccountClosureReason) query.getSingleResult();
    }

    @Override
    public List<AccountClosureReason> getCurrentYearAccountClosureReason() {
        Query query = entityManager.createQuery("SELECT a FROM AccountClosureReason a Where a.currentYear=:currentYear");
        query.setParameter("currentYear", "Yes");

        return query.getResultList();
    }

    @Override
    public AccountClosureReason retrieveAccountClosureReasonByName(String rateName) {
        AccountClosureReason accountClosureReason = new AccountClosureReason();

        try {
            Query query = entityManager.createQuery("Select a From AccountClosureReason a Where a.rateName=:rateName And a.currentYear=:currentYear And a.accountClosureReasonStatus=:accountClosureReasonStatus");
            query.setParameter("rateName", rateName);
            query.setParameter("currentYear", "Yes");
            query.setParameter("accountClosureReasonStatus", "New");

            if (query.getResultList().isEmpty()) {
                return new AccountClosureReason();
            } else {
                accountClosureReason = (AccountClosureReason) query.getSingleResult();
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
            return new AccountClosureReason();
        } catch (NonUniqueResultException nure) {
            System.out.println("Non unique result error: " + nure.getMessage());
        }

        return accountClosureReason;
    }
}
