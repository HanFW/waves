package ejb.payment.session;

import ejb.payment.entity.GIRO;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class GIROSessionBean implements GIROSessionBeanLocal {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public GIRO retrieveGIROById(Long giroId) {

        GIRO giro = new GIRO();

        try {
            Query query = entityManager.createQuery("Select g From GIRO g Where g.giroId=:giroId");
            query.setParameter("giroId", giroId);

            if (query.getResultList().isEmpty()) {
                return new GIRO();
            } else {
                giro = (GIRO) query.getSingleResult();
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
            return new GIRO();
        } catch (NonUniqueResultException nure) {
            System.out.println("Non unique result error: " + nure.getMessage());
        }

        return giro;
    }

    @Override
    public String deleteGIRO(Long giroId) {
        GIRO giro = retrieveGIROById(giroId);

        entityManager.remove(giro);
        entityManager.flush();

        return "Successfully deleted!";
    }
}
