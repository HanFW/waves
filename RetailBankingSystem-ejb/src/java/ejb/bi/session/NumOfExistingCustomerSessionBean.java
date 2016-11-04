package ejb.bi.session;

import ejb.bi.entity.NumOfExistingCustomer;
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
    public Long addNewNumOfExistingCustomer(String numOfExistingCustomer, Integer updateDate,
            String numOfOpeningAccounts, String numOfClosingAccounts) {

        NumOfExistingCustomer numOfCustomer = new NumOfExistingCustomer();

        numOfCustomer.setNumOfExistingCustomer(numOfExistingCustomer);
        numOfCustomer.setUpdateDate(updateDate);
        numOfCustomer.setNumOfOpeningAccounts(numOfOpeningAccounts);
        numOfCustomer.setNumOfClosingAccounts(numOfClosingAccounts);

        entityManager.persist(numOfCustomer);
        entityManager.flush();

        return numOfCustomer.getNumOfExistingCustomerId();
    }

    @Override
    public NumOfExistingCustomer retrieveNumOfExistingCustomerByDate(Integer updateDate) {
        NumOfExistingCustomer numOfCustomer = new NumOfExistingCustomer();

        try {
            Query query = entityManager.createQuery("Select n From NumOfExistingCustomer n Where n.updateDate=:updateDate");
            query.setParameter("updateDate", updateDate);

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

}
