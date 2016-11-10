package ejb.deposit.session;

import ejb.deposit.entity.Interest;
import javax.ejb.Local;

@Local
public interface InterestSessionBeanLocal {
    public Long addNewInterest(String dailyInterest,String monthlyInterest,String isTransfer,
            String isWithdraw);
    public Interest retrieveInterestById(Long interestId);
    public Interest retrieveInterestByAccountNum(String bankAccountNum);
    public String deleteInterest(Long interestId);
}
