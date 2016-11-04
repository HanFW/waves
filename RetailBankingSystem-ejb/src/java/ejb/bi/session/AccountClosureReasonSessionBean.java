package ejb.bi.session;

import ejb.bi.entity.AccountClosureReason;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class AccountClosureReasonSessionBean implements AccountClosureReasonSessionBeanLocal {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long addNewAccountClosureReason(String rateValue, String rateName,
            Integer updateMonth, Integer updateYear, String accountClosureReasonStatus) {

        AccountClosureReason accountClosureReason = new AccountClosureReason();

        accountClosureReason.setRateName(rateName);
        accountClosureReason.setRateValue(rateValue);
        accountClosureReason.setUpdateMonth(updateMonth);
        accountClosureReason.setUpdateYear(updateYear);
        accountClosureReason.setAccountClosureReasonStatus(accountClosureReasonStatus);
        
        entityManager.persist(accountClosureReason);
        entityManager.flush();
        
        return accountClosureReason.getAccountClosureReasonId();
    }
}
