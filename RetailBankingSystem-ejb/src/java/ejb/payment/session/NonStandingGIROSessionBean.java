package ejb.payment.session;

import ejb.customer.entity.CustomerBasic;
import ejb.deposit.session.BankAccountSessionBeanLocal;
import ejb.payment.entity.GIRO;
import ejb.payment.entity.NonStandingGIRO;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class NonStandingGIROSessionBean implements NonStandingGIROSessionBeanLocal {
    @EJB
    private BankAccountSessionBeanLocal bankAccountSessionBeanLocal;
    
    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager entityManager;
    
    @Override
    public Long addNewNonStandingGIRO(String billingOrganization, String billReference, String bankAccountNum,
            String bankAccountNumWithType, String paymentFrequency, String paymentAmt, 
            String giroType, Long customerBasicId) {
        
        NonStandingGIRO nonStandingGIRO = new NonStandingGIRO();
        
        nonStandingGIRO.setBankAccountNum(bankAccountNum);
        nonStandingGIRO.setBankAccountNumWithType(bankAccountNumWithType);
        nonStandingGIRO.setBillReference(billReference);
        nonStandingGIRO.setBillingOrganization(billingOrganization);
        nonStandingGIRO.setPaymentAmt(paymentAmt);
        nonStandingGIRO.setPaymentFrequency(paymentFrequency);
        nonStandingGIRO.setGiroType(giroType);
        nonStandingGIRO.setCustomerBasic(bankAccountSessionBeanLocal.retrieveCustomerBasicById(customerBasicId));

        entityManager.persist(nonStandingGIRO);
        entityManager.flush();
        
        return nonStandingGIRO.getGiroId();
    }
    
    @Override
    public List<NonStandingGIRO> retrieveNonStandingGIROByCusId(Long customerBasicId) {

        CustomerBasic customerBasic = bankAccountSessionBeanLocal.retrieveCustomerBasicById(customerBasicId);

        if (customerBasic.getCustomerBasicId() == null) {
            return new ArrayList<NonStandingGIRO>();
        }
        try {
            Query query = entityManager.createQuery("Select g From NonStandingGIRO g Where g.customerBasic=:customerBasic And g.giroType=:giroType");
            query.setParameter("customerBasic", customerBasic);
            query.setParameter("giroType", "Non Standing");

            if (query.getResultList().isEmpty()) {
                return new ArrayList<NonStandingGIRO>();
            } else {
                return query.getResultList();
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
            return new ArrayList<NonStandingGIRO>();
        }
    }
}
