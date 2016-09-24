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
        Verify verify = new Verify();
        
        try{
            System.out.println("retrieve try");
            Query query = entityManager.createQuery("Select v From Verify v Where v.customerIdentificationNum=:customerIdentificationNum");
            query.setParameter("customerIdentificationNum",customerIdentificationNum);
            
            if(query.getResultList().isEmpty()){
                System.out.println("retrieve try if");
                return new Verify();
            }
            else {
                System.out.println("retrieve try else");
                verify = (Verify)query.getResultList().get(0);
            }
        }
        catch(EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error: "+enfe.getMessage());
            return new Verify();
        }
        catch(NonUniqueResultException nure) {
            System.out.println("\nNon unique result error: "+nure.getMessage());
        }
        
        return verify;
    }
}
