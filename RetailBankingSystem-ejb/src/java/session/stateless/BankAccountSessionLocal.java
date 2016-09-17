package session.stateless;

import javax.ejb.Local;
import entity.BankAccount;
import entity.CustomerBasic;
import entity.AccTransaction;
import java.util.Date;
import java.util.List;

@Local
public interface BankAccountSessionLocal {
    public BankAccount retrieveBankAccountById(Long bankAccountId);
    public BankAccount retrieveBankAccountByNum(String bankAccountNum);
    public List<BankAccount> retrieveBankAccountByCusIC(String customerIdentificationNum);
    public CustomerBasic retrieveCustomerBasicById(Long customerBasicId);
    public CustomerBasic retrieveCustomerBasicByIC(String customerIdentificationNum);
    public AccTransaction retrieveAccTransactionById(Long accTransactionId);
    public Long addNewAccount(String bankAccountNum,String bankAccountPwd,
            String bankAccountType,String bankAccountBalance,String transferDailyLimit,
            String transferBalance,String bankAccountStatus,Long customerBasicId,Long interestId);
    public String deleteAccount(String bankAccountNum);
    public void activateAccounts();
    public void interestCrediting();
    public String checkAccountDuplication (String bankAccountNum);
    public String generateBankAccount(String customerIdentificationNum);
    public boolean checkExistence(String customerIdentificationNum);
    public String changeDateFormat (Date customerDateOfBirth);
}
