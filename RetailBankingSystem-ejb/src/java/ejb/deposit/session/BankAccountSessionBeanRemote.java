/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.deposit.session;

import ejb.customer.entity.CustomerBasic;
import ejb.deposit.entity.AccTransaction;
import ejb.deposit.entity.BankAccount;
import java.util.Date;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Nicole
 */
@Remote
public interface BankAccountSessionBeanRemote {

    public BankAccount retrieveBankAccountById(Long bankAccountId);

    public BankAccount retrieveBankAccountByNum(String bankAccountNum);

    public List<BankAccount> retrieveBankAccountByCusIC(String customerIdentificationNum);

    public CustomerBasic retrieveCustomerBasicById(Long customerBasicId);

    public CustomerBasic retrieveCustomerBasicByIC(String customerIdentificationNum);

    public AccTransaction retrieveAccTransactionById(Long accTransactionId);

    public Long addNewAccount(String bankAccountNum, String bankAccountType, String totalBankAccountBalance,
            String availableBankAccountBalance, String transferDailyLimit,
            String transferBalance, String bankAccountStatus, String bankAccountMinSaving,
            String bankAccountDepositPeriod, String currentFixedDepositPeriod,
            String fixedDepositStatus, Double statementDateDouble, Long currentTimeMilis,
            Long customerBasicId, Long interestId);

    public String deleteAccount(String bankAccountNum);

    public void interestAccuring();

    public void interestCrediting();

    public String checkAccountDuplication(String bankAccountNum);

    public String generateBankAccount();

    public boolean checkExistence(String customerIdentificationNum);

    public String changeDateFormat(Date customerDateOfBirth);

    public void updateDepositPeriod(String bankAccountNum, String fixedDepositPeriod);

    public boolean checkOnlyOneAccount(String customerIdentificationNum);

    public CustomerBasic retrieveCustomerBasicByAccNum(String bankAccountNum);

    public String resetDailyTransferLimit();

    public String updateDailyTransferLimit(String bankAccountNum, String dailyTransferLimit);

    public void autoCloseAccount();

    public Long addNewAccountOneTime(String bankAccountNum, String bankAccountType,
            String totalBankAccountBalance, String availableBankAccountBalance, String transferDailyLimit,
            String transferBalance, String bankAccountStatus, String bankAccountMinSaving,
            String bankAccountDepositPeriod, String currentFixedDepositPeriod,
            String fixedDepositStatus, Double statementDateDouble, Long currentTimeMilis,
            Long customerBasicId, Long interestId);

    public String updateBankAccountAvailableBalance(String bankAccountNum, String availableBankAccountBalance);

    public String updateBankAccountBalance(String bankAccountNum, String availableBankAccountBalance,
            String totalBankAccountBalance);

    public String updateDepositAccountAvailableBalance(String cardNum, double transactionAmt);

    public String updateDepositAccountTotalBalance(String cardNum, double transactionAmt, String merchantName);

}
