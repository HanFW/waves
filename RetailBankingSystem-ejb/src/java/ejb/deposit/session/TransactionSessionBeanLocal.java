package ejb.deposit.session;

import ejb.deposit.entity.AccTransaction;
import ejb.deposit.entity.BankAccount;
import java.util.List;
import javax.ejb.Local;


@Local
public interface TransactionSessionBeanLocal {
    public Long addNewTransaction(String transactionDate,String transactionCode,String transactionRef,
            String accountDebit,String accountCredit,Long transactionDateMilis,Long bankAccountId);
    public Long cashDeposit(String bankAccountNum,String depositAmt);
    public BankAccount retrieveBankAccountById(Long bankAccountId);
    public List<AccTransaction> retrieveAccTransactionByBankNum(String bankAccountNum);
    public BankAccount retrieveBankAccountByNum(String bankAccountNum);
    public String checkPassword(String bankAccountNum,String bankAccountPwd);
    public Long cashWithdraw(String bankAccountNum,String withdrawAmt);
    public Long fundTransfer(String fromAccount,String toAccount,String transferAmt);
    public String checkAccountActivation(String bankAccountNum,String initialDepositAmount);
    public void deleteAccTransaction(Long transactionId);
    public void fastTransfer(String fromBankAccount,String toBankAccount,Double transferAmt);
    public void actualTransfer(String fromAccountNum, String toAccountNum, Double transferAmt);
}
