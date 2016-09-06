package session.stateless;

import entity.Payee;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.PersistenceContext;
import javax.ejb.LocalBean;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NonUniqueResultException;

@Stateless
@LocalBean
public class PayeeSession implements PayeeSessionLocal {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    @Override
    public Long addNewPayee(String payeeName,String payeeAccountNum,String payeeAccountType,
            String lastTransactionDate,Long customerBasicId) {
        Payee payee = new Payee();
        
        payee.setPayeeName(payeeName);
        payee.setPayeeAccountNum(payeeAccountNum);
        payee.setPayeeAccountType(payeeAccountType);
        payee.setLastTransactionDate(lastTransactionDate);
        payee.setCustomerBasicId(customerBasicId);
        
        entityManager.persist(payee);
        entityManager.flush();
        
        return payee.getPayeeId();
    }
    
    @Override
    public String deletePayee(String payeeName){
        Payee payee = retrievePayeeByName(payeeName);
        entityManager.remove(payee);
        
        return "Successfully deleted!";
    }
    
    @Override
    public Payee retrievePayeeById(Long payeeId){
        Payee payee = new Payee();
        
        try{
            Query query = entityManager.createQuery("Select p From Payee p Where p.payeeId=:payeeId");
            query.setParameter("payeeId",payeeId);
            payee = (Payee)query.getSingleResult();
        }
        catch(EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error: "+enfe.getMessage());
            return new Payee();
        }
        catch(NonUniqueResultException nure) {
            System.out.println("\nNon unique result error: "+nure.getMessage());
        }
        
        return payee;
    }
    
    @Override
    public Payee retrievePayeeByName(String payeeName) {
        Payee payee = new Payee();
        
        try{
            Query query = entityManager.createQuery("Select p From Payee p Where p.payeeName=:payeeName");
            query.setParameter("payeeName",payeeName);
            
            if(query.getResultList().isEmpty()){
                return new Payee();
            }
            else {
                payee = (Payee)query.getResultList().get(0);
            } 
        }
        catch(EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error: "+enfe.getMessage());
            return new Payee();
        }
        catch(NonUniqueResultException nure) {
            System.out.println("\nNon unique result error: "+nure.getMessage());
        }
        
        return payee;
    }
}
