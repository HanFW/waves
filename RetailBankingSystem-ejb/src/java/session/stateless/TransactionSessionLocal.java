package session.stateless;

import entity.AccTransaction;
import entity.BankAccount;
import java.sql.Timestamp;
import java.util.List;
import javax.ejb.Local;


@Local
public interface TransactionSessionLocal {
    public Long addNewTransaction(String transactionDate,String transactionCode,String transactionRef,
            String accountDebit,String accountCredit,Long bankAccountId);
    public String cashDeposit(String bankAccountNum,String depositAmt);
    public BankAccount retrieveBankAccountById(Long bankAccountId);
    public List<AccTransaction> retrieveAccTransactionByBankNum(String bankAccountNum);
    public BankAccount retrieveBankAccountByNum(String bankAccountNum);
    public String checkPassword(String bankAccountNum,String bankAccountPwd);
    public String cashWithdraw(String bankAccountNum,String withdrawAmt);
    public Long fundTransfer(String fromAccount,String toAccount,String transferAmt);
    public void initialDeposit(Long bankAccountId, String initialDepositAmt);
    public String checkAccountActivation(String bankAccountNum,String initialDepositAmount);
}
