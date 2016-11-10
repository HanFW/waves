package ejb.card.session;

import ejb.card.entity.PrincipalCard;
import ejb.customer.entity.CustomerBasic;
import ejb.customer.session.CRMCustomerSessionBeanLocal;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class PrincipalCardSessionBean implements PrincipalCardSessionBeanLocal {

    @EJB
    private CRMCustomerSessionBeanLocal customerSessionBeanLocal;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public PrincipalCard retrieveCardById(Long cardId) {
        PrincipalCard principalCard = new PrincipalCard();

        try {
            Query query = entityManager.createQuery("Select c From Card c Where c.cardId=:cardId");
            query.setParameter("cardId", cardId);

            if (query.getResultList().isEmpty()) {
                return new PrincipalCard();
            } else {
                principalCard = (PrincipalCard) query.getSingleResult();
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
            return new PrincipalCard();
        } catch (NonUniqueResultException nure) {
            System.out.println("Non unique result error: " + nure.getMessage());
        }

        return principalCard;
    }

    @Override
    public List<PrincipalCard> retrievePrincipalCardByCusIC(String customerIdentificationNum) {
        CustomerBasic customerBasic = customerSessionBeanLocal.retrieveCustomerBasicByIC(customerIdentificationNum.toUpperCase());

        if (customerBasic.getCustomerBasicId() == null) {
            return new ArrayList<PrincipalCard>();
        }
        try {
            Query query = entityManager.createQuery("Select p From PrincipalCard p Where p.customerBasic=:customerBasic");
            query.setParameter("customerBasic", customerBasic);
            return query.getResultList();
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
            return new ArrayList<PrincipalCard>();
        }
    }

    public void persist(Object object) {
        entityManager.persist(object);
    }
}
