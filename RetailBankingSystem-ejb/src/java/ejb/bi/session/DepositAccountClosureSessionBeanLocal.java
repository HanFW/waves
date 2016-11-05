package ejb.bi.session;

import javax.ejb.Local;

@Local
public interface DepositAccountClosureSessionBeanLocal {
    public Long addNewDepositAccountClosure(String accountClosureReason, Long currentTimeMilis,
            String currentTime);
}
