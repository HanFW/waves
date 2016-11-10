/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.deposit.session;

import ejb.deposit.entity.AccTransaction;
import ejb.deposit.entity.BankAccount;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Nicole
 */
@Remote
public interface TransactionSessionBeanRemote {

    public Long addNewTransaction(String transactionDate, String transactionCode, String transactionRef,
            String accountDebit, String accountCredit, Long transactionDateMilis, Long bankAccountId);

    public Long cashDeposit(String bankAccountNum, String depositAmt);

    public BankAccount retrieveBankAccountById(Long bankAccountId);

    public List<AccTransaction> retrieveAccTransactionByBankNum(String bankAccountNum);

    public BankAccount retrieveBankAccountByNum(String bankAccountNum);

    public Long cashWithdraw(String bankAccountNum, String withdrawAmt);

    public Long fundTransfer(String fromAccount, String toAccount, String transferAmt);

    public String checkAccountActivation(String bankAccountNum, String initialDepositAmount);

    public String deleteAccTransaction(Long transactionId);

    public String fastTransfer(String fromBankAccount, String toBankAccount, Double transferAmt);

}
