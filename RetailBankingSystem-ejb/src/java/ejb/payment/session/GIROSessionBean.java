package ejb.payment.session;

import ejb.customer.entity.CustomerBasic;
import ejb.deposit.session.BankAccountSessionBeanLocal;
import ejb.payment.entity.GIRO;
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
public class GIROSessionBean implements GIROSessionBeanLocal {
    @EJB
    private BankAccountSessionBeanLocal bankAccountSessionBeanLocal;

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
    public List<GIRO> retrieveStandingGIROByCusId(Long customerBasicId) {

        CustomerBasic customerBasic = bankAccountSessionBeanLocal.retrieveCustomerBasicById(customerBasicId);

        if (customerBasic.getCustomerBasicId() == null) {
            return new ArrayList<GIRO>();
        }
        try {
            Query query = entityManager.createQuery("Select g From GIRO g Where g.customerBasic=:customerBasic And g.giroType=:giroType");
            query.setParameter("customerBasic", customerBasic);
            query.setParameter("giroType", "Standing");

            if (query.getResultList().isEmpty()) {
                return new ArrayList<GIRO>();
            } else {
                return query.getResultList();
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
            return new ArrayList<GIRO>();
        }
    }
    
    @Override
    public String deleteGIRO(Long giroId) {
        GIRO giro = retrieveGIROById(giroId);

        entityManager.remove(giro);
        entityManager.flush();

        return "Successfully deleted!";
    }
    
    @Override
    public List<GIRO> retrieveNonStandingGIROByCusId(Long customerBasicId) {

        CustomerBasic customerBasic = bankAccountSessionBeanLocal.retrieveCustomerBasicById(customerBasicId);

        if (customerBasic.getCustomerBasicId() == null) {
            return new ArrayList<GIRO>();
        }
        try {
            Query query = entityManager.createQuery("Select g From GIRO g Where g.customerBasic=:customerBasic And g.giroType=:giroType");
            query.setParameter("customerBasic", customerBasic);
            query.setParameter("giroType", "Non Standing");

            if (query.getResultList().isEmpty()) {
                return new ArrayList<GIRO>();
            } else {
                return query.getResultList();
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
            return new ArrayList<GIRO>();
        }
    }
}
