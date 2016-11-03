package ejb.payment.session;

import ejb.deposit.session.BankAccountSessionBeanLocal;
import ejb.payment.entity.RegularGIRO;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class RegularGIROSessionBean implements RegularGIROSessionBeanLocal {

    @EJB
    private BankAccountSessionBeanLocal bankAccountSessionBeanLocal;
    
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long addNewRegularGRIO(String bankAccountNum, String bankAccountNumWithType, String giroType,
            String paymentAmt, String paymentFrequency, String payeeBankName, String payeeBankAccountNum,
            Long customerBasicId) {

        RegularGIRO regularGIRO = new RegularGIRO();

        regularGIRO.setBankAccountNum(bankAccountNum);
        regularGIRO.setBankAccountNumWithType(bankAccountNumWithType);
        regularGIRO.setGiroType(giroType);
        regularGIRO.setPaymentAmt(paymentAmt);
        regularGIRO.setPaymentFrequency(paymentFrequency);
        regularGIRO.setPayeeBankName(payeeBankName);
        regularGIRO.setPayeeAccountNum(payeeBankAccountNum);
        regularGIRO.setCustomerBasic(bankAccountSessionBeanLocal.retrieveCustomerBasicById(customerBasicId));
        
        entityManager.persist(regularGIRO);
        entityManager.flush();
        
        return regularGIRO.getGiroId();
    }
    
    @Override
    public RegularGIRO retrieveRegularGIROById(Long giroId) {
        RegularGIRO regularGIRO = new RegularGIRO();

        try {
            Query query = entityManager.createQuery("Select r From RegularGIRO r Where r.giroId=:giroId");
            query.setParameter("giroId", giroId);

            if (query.getResultList().isEmpty()) {
                return new RegularGIRO();
            } else {
                regularGIRO = (RegularGIRO) query.getSingleResult();
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("Entity not found error: " + enfe.getMessage());
            return new RegularGIRO();
        } catch (NonUniqueResultException nure) {
            System.out.println("Non unique result error: " + nure.getMessage());
        }

        return regularGIRO;
    }
    
    @Override
    public void updatePaymentAmt(Long giroId, String paymentAmt) {
        RegularGIRO regularGIRO = retrieveRegularGIROById(giroId);
        regularGIRO.setPaymentAmt(paymentAmt);
    }
}
