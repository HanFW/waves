package session.stateless;

import javax.ejb.Local;
import entity.BankAccount;
import entity.CustomerBasic;
import entity.AccTransaction;
import java.util.List;

@Local
public interface BankAccountSessionLocal {
    public BankAccount retrieveBankAccountById(Long bankAccountId);
    public BankAccount retrieveBankAccountByNum(String bankAccountNum);
    public List<BankAccount> retrieveBankAccountByCusIC(String customerIdentificationNum);
    public CustomerBasic retrieveCustomerBasicById(Long customerBasicId);
    public CustomerBasic retrieveCustomerBasicByIC(String customerIdentificationNum);
    public AccTransaction retrieveAccTransactionById(Long accTransactionId);
    public List <BankAccount> getAllBankAccount();
    public Long addNewAccount(String bankAccountNum,String bankAccountPwd,
            String bankAccountType,String bankAccountBalance,String transferDailyLimit,
            Long customerBasicId,Long interestId);
    public String deleteAccount(String bankAccountNum);
}
