package ejb.bi.session;

import ejb.bi.entity.DepositAccountClosure;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class DepositAccountClosureSessionBean implements DepositAccountClosureSessionBeanLocal {

    @PersistenceContext
    private EntityManager entityManager;
    
    @Override
    public Long addNewDepositAccountClosure(String accountClosureReason, Long currentTimeMilis,
            String currentTime, String accountStatus) {
        
        DepositAccountClosure depositAccountClosure = new DepositAccountClosure();
        
        depositAccountClosure.setAccountClosureReason(accountClosureReason);
        depositAccountClosure.setCurrentTime(currentTime);
        depositAccountClosure.setCurrentTimeMilis(currentTimeMilis);
        depositAccountClosure.setAccountStatus(accountStatus);
        
        entityManager.persist(depositAccountClosure);
        entityManager.flush();
        
        return depositAccountClosure.getDepositAccountClosureId();
    }
}
