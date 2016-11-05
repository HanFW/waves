package ejb.bi.session;

import ejb.bi.entity.AccountClosureReason;
import java.util.List;
import javax.ejb.Local;

@Local
public interface AccountClosureReasonSessionBeanLocal {

    public Long addNewAccountClosureReason(String rateValue, String rateName,
            Integer updateMonth, Integer updateYear, String accountClosureReasonStatus,
            String currentYear);

    public AccountClosureReason getNewInterestAccountClosureReason();

    public List<AccountClosureReason> getCurrentYearAccountClosureReason();

    public AccountClosureReason retrieveAccountClosureReasonByName(String rateName);

    public AccountClosureReason getNewServiceChargeAccountClosureReason();

    public AccountClosureReason getNewCustomerServiceAccountClosureReason();

    public AccountClosureReason getNewDontNeedAccountClosureReason();

    public AccountClosureReason getNewAppliedAnotherAccountClosureReason();

    public AccountClosureReason getNewOtherReasonsAccountClosureReason();
}
