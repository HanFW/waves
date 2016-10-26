package ejb.payment.session;

import ejb.payment.entity.Cheque;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless

public class ChequeSessionBean implements ChequeSessionBeanLocal {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Cheque retrieveGIROById(Long chequeId) {
        Cheque cheque = new Cheque();

        try {
            Query query = entityManager.createQuery("Select c From Cheque c Where c.chequeId=:chequeId");
            query.setParameter("chequeId", chequeId);

            if (query.getResultList().isEmpty()) {
                return new Cheque();
            } else {
                cheque = (Cheque) query.getSingleResult();
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
            return new Cheque();
        } catch (NonUniqueResultException nure) {
            System.out.println("Non unique result error: " + nure.getMessage());
        }

        return cheque;
    }
}
