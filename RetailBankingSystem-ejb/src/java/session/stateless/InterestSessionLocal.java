package session.stateless;

import entity.Interest;
import javax.ejb.Local;

@Local
public interface InterestSessionLocal {
    public Long addNewInterest(String dailyInterest,String monthlyInterest,boolean isTransfer,boolean isWithdraw);
    public Interest retrieveInterestById(Long interestId);
    public Interest retrieveInterestByAccountNum(String bankAccountNum);
    public Double accuredInterest(String bankAccountNum);
    public void creditedInterest(String bankAccountNum);
    public void calculateInterest(String bankAccountNum);
}
