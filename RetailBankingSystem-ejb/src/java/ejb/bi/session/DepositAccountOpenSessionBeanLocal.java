package ejb.bi.session;

import javax.ejb.Local;

@Local
public interface DepositAccountOpenSessionBeanLocal {

    public Long addNewDepositAccountOpen(Long currentTimeMilis, String currentTime);
}
