package ejb.customer.session;

import ejb.customer.entity.EnquiryCase;
import ejb.customer.entity.FollowUp;
import static ejb.customer.entity.Issue_.enquiryCase;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class FollowUpSessionBean implements FollowUpSessionBeanLocal {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public FollowUp retrieveFollowUpById(Long followUpId) {
        FollowUp followUp = new FollowUp();

        try {
            Query query = entityManager.createQuery("Select f From FollowUp f Where f.followUpId=:followUpId");
            query.setParameter("followUpId", followUpId);
            if (query.getResultList().isEmpty()) {
                return new FollowUp();
            } else {
                followUp = (FollowUp) query.getSingleResult();
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error: " + enfe.getMessage());
            return new FollowUp();
        } catch (NonUniqueResultException nure) {
            System.out.println("\nNon unique result error: " + nure.getMessage());
        }

        return followUp;
    }

    @Override
    public String deleteFollowUp (Long followUpId) {
        FollowUp followUp = retrieveFollowUpById(followUpId);

        entityManager.remove(followUp);
        entityManager.flush();

        return "Successfully deleted!";
    }

}
