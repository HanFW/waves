package session.stateless;

import entity.Interest;
import javax.ejb.Local;

@Local
public interface InterestSessionLocal {
    public Long addNewInterest(String dailyInterest,String monthlyInterest,String isTransfer,String isWithdraw);
    public Interest retrieveInterestById(Long interestId);
    public Interest retrieveInterestByAccountNum(String bankAccountNum);
}
