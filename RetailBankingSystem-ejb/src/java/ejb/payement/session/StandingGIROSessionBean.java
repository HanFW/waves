package ejb.payement.session;

import ejb.customer.entity.CustomerBasic;
import ejb.deposit.session.BankAccountSessionBeanLocal;
import ejb.payment.entity.GIRO;
import ejb.payment.entity.StandingGIRO;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class StandingGIROSessionBean implements StandingGIROSessionBeanLocal {
    @EJB
    private GIROSessionBeanLocal gIROSessionBeanLocal;

    @EJB
    private BankAccountSessionBeanLocal bankAccountSessionBeanLocal;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long addNewStandingGIRO(String billingOrganization, String billReference, String paymemtLimit,
            String customerName, String customerMobile, String bankAccountNum, String standingGiroStatus,
            String bankAccountNumWithType, Long customerBasicId) {

        StandingGIRO standingGiro = new StandingGIRO();

        standingGiro.setBankAccountNum(bankAccountNum);
        standingGiro.setBillReference(billReference);
        standingGiro.setBillingOrganization(billingOrganization);
        standingGiro.setCustomerMobile(customerMobile);
        standingGiro.setCustomerName(customerName);
        standingGiro.setPaymentLimit(paymemtLimit);
        standingGiro.setStandingGiroStatus(standingGiroStatus);
        standingGiro.setBankAccountNumWithType(bankAccountNumWithType);
        standingGiro.setCustomerBasic(bankAccountSessionBeanLocal.retrieveCustomerBasicById(customerBasicId));

        entityManager.persist(standingGiro);
        entityManager.flush();

        return standingGiro.getGiroId();
    }

    @Override
    public String deleteStandingGIRO(Long giroId) {
        GIRO standingGiro = gIROSessionBeanLocal.retrieveGIROById(giroId);

        entityManager.remove(standingGiro);
        entityManager.flush();

        return "Successfully deleted!";
    }

    @Override
    public List<StandingGIRO> retrieveStandingGIROByCusId(Long customerBasicId) {

        CustomerBasic customerBasic = bankAccountSessionBeanLocal.retrieveCustomerBasicById(customerBasicId);

        if (customerBasic.getCustomerBasicId() == null) {
            return new ArrayList<StandingGIRO>();
        }
        try {
            Query query = entityManager.createQuery("Select s From StandingGIRO s Where s.customerBasic=:customerBasic");
            query.setParameter("customerBasic", customerBasic);

            if (query.getResultList().isEmpty()) {
                return new ArrayList<StandingGIRO>();
            } else {
                return query.getResultList();
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
            return new ArrayList<StandingGIRO>();
        }
    }
}
