package ejb.bi.session;

import ejb.bi.entity.DepositAccountOpen;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class DepositAccountOpenSessionBean implements DepositAccountOpenSessionBeanLocal {

    @PersistenceContext
    private EntityManager entityManager;
    
    @Override
    public Long addNewDepositAccountOpen(Long currentTimeMilis, String currentTime) {
        DepositAccountOpen depositAccountOpen = new DepositAccountOpen();
        
        depositAccountOpen.setCurrentTimeMilis(currentTimeMilis);
        depositAccountOpen.setCurrentTime(currentTime);
        
        entityManager.persist(depositAccountOpen);
        entityManager.flush();
        
        return depositAccountOpen.getDepositAccountOpenId();
    }
}
