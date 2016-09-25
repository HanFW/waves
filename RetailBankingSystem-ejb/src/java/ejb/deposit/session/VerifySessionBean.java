package ejb.deposit.session;

import ejb.deposit.entity.Verify;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless

public class VerifySessionBean implements VerifySessionBeanLocal {

    @PersistenceContext
    private EntityManager entityManager;
    
    @Override
    public Long addNewVerify(String customerName,String customerIdentificationType,
            String customerIdentificationNum, String identification) {
        System.out.println("*");
        System.out.println("****** deposit/TransactionSessionBean: addNewVerify() ******");
        Verify verify = new Verify();
        
        verify.setCustomerName(customerName);
        verify.setCustomerIdentificationType(customerIdentificationType);
        verify.setCustomerIdentificationNum(customerIdentificationNum);
        verify.setIdentification(identification);
        
        entityManager.persist(verify);
        entityManager.flush();
        
        return verify.getVerifyId();
    }
    
    @Override
    public Verify retrieveVerifyByCusIc(String customerIdentificationNum) {
        System.out.println("*");
        System.out.println("****** deposit/TransactionSessionBean: retrieveVerifyByCusIc() ******");
        Verify verify = new Verify();
        
        try{
            Query query = entityManager.createQuery("Select v From Verify v Where v.customerIdentificationNum=:customerIdentificationNum");
            query.setParameter("customerIdentificationNum",customerIdentificationNum);
            
            if(query.getResultList().isEmpty()){
                System.out.println("****** deposit/TransactionSessionBean: retrieveVerifyByCusIc(): invalid customer identification number: no result found, return new verify");
                return new Verify();
            }
            else {
                System.out.println("****** deposit/TransactionSessionBean: retrieveVerifyByCusIc(): valid customer identification number: return verify");
                verify = (Verify)query.getResultList().get(0);
            }
        }
        catch(EntityNotFoundException enfe) {
            System.out.println("****** deposit/TransactionSessionBean: retrieveVerifyByCusIc(): Entity not found error: "+enfe.getMessage());
            return new Verify();
        }
        catch(NonUniqueResultException nure) {
            System.out.println("****** deposit/TransactionSessionBean: retrieveVerifyByCusIc(): Non unique result error: "+nure.getMessage());
        }
        
        return verify;
    }
}
