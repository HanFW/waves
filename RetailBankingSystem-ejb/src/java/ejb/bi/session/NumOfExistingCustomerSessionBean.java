package ejb.bi.session;

import ejb.bi.entity.NumOfExistingCustomer;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class NumOfExistingCustomerSessionBean implements NumOfExistingCustomerSessionBeanLocal {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long addNewNumOfExistingCustomer(String numOfExistingCustomer, String numOfOpeningAccounts,
            String numOfClosingAccounts, Integer updateMonth, Integer updateYear, String status,
            String currentYeaar) {

        NumOfExistingCustomer numOfCustomer = new NumOfExistingCustomer();

        numOfCustomer.setNumOfClosingAccounts(numOfClosingAccounts);
        numOfCustomer.setNumOfExistingCustomer(numOfExistingCustomer);
        numOfCustomer.setNumOfOpeningAccounts(numOfOpeningAccounts);
        numOfCustomer.setStatus(status);
        numOfCustomer.setUpdateMonth(updateMonth);
        numOfCustomer.setUpdateYear(updateYear);
        numOfCustomer.setCurrentYear(currentYeaar);

        entityManager.persist(numOfCustomer);
        entityManager.flush();

        return numOfCustomer.getNumOfExistingCustomerId();
    }

    @Override
    public NumOfExistingCustomer retrieveNumOfExistingCustomerByMonth(Integer updateMonth) {
        NumOfExistingCustomer numOfCustomer = new NumOfExistingCustomer();

        try {
            Query query = entityManager.createQuery("Select n From NumOfExistingCustomer n Where n.updateMonth=:updateMonth And n.currentYear=:currentYear");
            query.setParameter("updateMonth", updateMonth);
            query.setParameter("currentYear", "Yes");

            if (query.getResultList().isEmpty()) {
                return new NumOfExistingCustomer();
            } else {
                numOfCustomer = (NumOfExistingCustomer) query.getSingleResult();
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
            return new NumOfExistingCustomer();
        } catch (NonUniqueResultException nure) {
            System.out.println("Non unique result error: " + nure.getMessage());
        }

        return numOfCustomer;
    }

    @Override
    public NumOfExistingCustomer retrievePreviousNumOfExistingCustomerByMonth(Integer updateMonth, Integer updateYear) {
        NumOfExistingCustomer numOfCustomer = new NumOfExistingCustomer();

        try {
            Query query = entityManager.createQuery("Select n From NumOfExistingCustomer n Where n.updateMonth=:updateMonth And n.currentYear=:currentYear And n.updateYear=:updateYear");
            query.setParameter("updateMonth", updateMonth);
            query.setParameter("updateYear", updateYear);
            query.setParameter("currentYear", "No");

            if (query.getResultList().isEmpty()) {
                return new NumOfExistingCustomer();
            } else {
                numOfCustomer = (NumOfExistingCustomer) query.getSingleResult();
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
            return new NumOfExistingCustomer();
        } catch (NonUniqueResultException nure) {
            System.out.println("Non unique result error: " + nure.getMessage());
        }

        return numOfCustomer;
    }

    @Override
    public NumOfExistingCustomer getNumOfExistingCustomer() {
        Query query = entityManager.createQuery("SELECT n FROM NumOfExistingCustomer n Where n.status=:status");
        query.setParameter("status", "New");

        return (NumOfExistingCustomer) query.getSingleResult();
    }

    @Override
    public List<NumOfExistingCustomer> getCurrentYearNumOfExistingCustomer() {
        Query query = entityManager.createQuery("SELECT n FROM NumOfExistingCustomer n Where n.currentYear=:currentYear");
        query.setParameter("currentYear", "Yes");

        return query.getResultList();
    }
}
