package ejb.bi.session;

import javax.ejb.Local;

@Local
public interface AccountClosureReasonSessionBeanLocal {

    public Long addNewAccountClosureReason(String rateValue, String rateName,
            Integer updateMonth, Integer updateYear, String accountClosureReasonStatus);
}
