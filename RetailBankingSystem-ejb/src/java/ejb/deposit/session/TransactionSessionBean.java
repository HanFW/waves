package ejb.deposit.session;

import ejb.deposit.entity.BankAccount;
import ejb.deposit.entity.AccTransaction;
import ejb.deposit.entity.Interest;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NonUniqueResultException;

@Stateless
@LocalBean
public class TransactionSessionBean implements TransactionSessionBeanLocal {

    @EJB
    private BankAccountSessionBeanLocal bankAccountSessionLocal;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public BankAccount retrieveBankAccountById(Long bankAccountId) {
        System.out.println("*");
        System.out.println("****** deposit/TransactionSessionBean: retrieveBankAccountById() ******");
        BankAccount bankAccount = new BankAccount();

        try {
            Query query = entityManager.createQuery("Select a From BankAccount a Where a.bankAccountId=:bankAccountId");
            query.setParameter("bankAccountId", bankAccountId);

            if (query.getResultList().isEmpty()) {
                System.out.println("****** deposit/TransactionSessionBean: retrieveBankAccountById(): invalid bank account Id: no result found, return new bank account");
                return new BankAccount();
            } else {
                System.out.println("****** deposit/TransactionSessionBean: retrieveBankAccountById(): valid bank account Id: return bank account");
                bankAccount = (BankAccount) query.getSingleResult();
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("****** deposit/TransactionSessionBean: retrieveBankAccountById(): Entity not found error: " + enfe.getMessage());
            return new BankAccount();
        } catch (NonUniqueResultException nure) {
            System.out.println("****** deposit/TransactionSessionBean: retrieveBankAccountById(): Non unique result error: " + nure.getMessage());
        }

        return bankAccount;
    }

    @Override
    public BankAccount retrieveBankAccountByNum(String bankAccountNum) {
        System.out.println("*");
        System.out.println("****** deposit/TransactionSessionBean: retrieveBankAccountByNum() ******");
        BankAccount bankAccount = new BankAccount();

        try {
            Query query = entityManager.createQuery("Select a From BankAccount a Where a.bankAccountNum=:bankAccountNum");
            query.setParameter("bankAccountNum", bankAccountNum);

            if (query.getResultList().isEmpty()) {
                System.out.println("****** deposit/TransactionSessionBean: retrieveBankAccountByNum(): invalid bank account number: no result found, return new bank account");
                return new BankAccount();
            } else {
                System.out.println("****** deposit/TransactionSessionBean: retrieveBankAccountByNum(): valid bank account number: return new bank account");
                bankAccount = (BankAccount) query.getSingleResult();
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("****** deposit/TransactionSessionBean: retrieveBankAccountByNum(): Entity not found error: " + enfe.getMessage());
            return new BankAccount();
        } catch (NonUniqueResultException nure) {
            System.out.println("****** deposit/TransactionSessionBean: retrieveBankAccountByNum(): Non unique result error: " + nure.getMessage());
        }

        return bankAccount;
    }

    @Override
    public List<AccTransaction> retrieveAccTransactionByBankNum(String bankAccountNum) {
        System.out.println("*");
        System.out.println("****** deposit/TransactionSessionBean: retrieveAccTransactionByBankNum() ******");
        BankAccount bankAccount = bankAccountSessionLocal.retrieveBankAccountByNum(bankAccountNum);

        if (bankAccount == null) {
            System.out.println("****** deposit/TransactionSessionBean: retrieveAccTransactionByBankNum(): invalid bank account number: no result found, return new transaction");
            return new ArrayList<AccTransaction>();
        }
        try {
            Query query = entityManager.createQuery("Select t From AccTransaction t Where t.bankAccount=:bankAccount");
            query.setParameter("bankAccount", bankAccount);

            if (query.getResultList().isEmpty()) {
                System.out.println("****** deposit/TransactionSessionBean: retrieveAccTransactionByBankNum(): wrong bank account number: no result found, return new transaction");
                return new ArrayList<AccTransaction>();
            } else {
                System.out.println("****** deposit/TransactionSessionBean: retrieveAccTransactionByBankNum(): correct bank account number: return transaction");
                return query.getResultList();
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("****** deposit/TransactionSessionBean: retrieveAccTransactionByBankNum(): Entity not found error: " + enfe.getMessage());
            return null;
        }
    }

    @Override
    public Long addNewTransaction(String transactionDate, String transactionCode, String transactionRef,
            String accountDebit, String accountCredit, Long transactionDateMilis, Long bankAccountId) {
        System.out.println("*");
        System.out.println("****** deposit/TransactionSessionBean: addNewTransaction() ******");
        AccTransaction accTransaction = new AccTransaction();

        accTransaction.setTransactionDate(transactionDate);
        accTransaction.setTransactionCode(transactionCode);
        accTransaction.setTransactionRef(transactionRef);
        accTransaction.setAccountDebit(accountDebit);
        accTransaction.setAccountCredit(accountCredit);
        accTransaction.setTransactionDateMilis(transactionDateMilis);
        accTransaction.setBankAccount(retrieveBankAccountById(bankAccountId));

        entityManager.persist(accTransaction);
        entityManager.flush();

        return accTransaction.getTransactionId();
    }

    @Override
    public Long cashDeposit(String bankAccountNum, String depositAmt) {
        System.out.println("*");
        System.out.println("****** deposit/TransactionSessionBean: cashDeposit() ******");

        Long bankAccountId;
        Long accTransactionId = Long.valueOf(0);

        BankAccount bankAccount = retrieveBankAccountByNum(bankAccountNum);
        bankAccountId = bankAccount.getBankAccountId();

        if (bankAccountId == null) {
            return accTransactionId;
        } else {
            if (bankAccount.getBankAccountType().equals("Monthly Savings Account")) {
                if (Double.valueOf(depositAmt) >= 50) {
                    bankAccount.setBankAccountMinSaving("Sufficient");
                }
            }

            String accountDebit = " ";
            String transactionCode = "ADP";
            String transactionRef = "Merlion Bank Branch";

            Calendar cal = Calendar.getInstance();
//            int year = cal.get(Calendar.YEAR);
//            int month = cal.get(Calendar.MONTH);
//            int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
//            String transactionDate = dayOfMonth + "-" + (month + 1) + "-" + year;
            Long transactionDateMilis = cal.getTimeInMillis();

            accTransactionId = addNewTransaction(cal.getTime().toString(), transactionCode, transactionRef,
                    accountDebit, depositAmt, transactionDateMilis, bankAccountId);

            Double preBalance = Double.valueOf(bankAccount.getBankAccountBalance());
            Double depositAmtDouble = Double.valueOf(depositAmt);
            Double totalBalance = preBalance + depositAmtDouble;
            String balance = totalBalance.toString();
            bankAccount.setBankAccountBalance(balance);

            return accTransactionId;
        }
    }

    @Override
    public Long cashWithdraw(String bankAccountNum, String withdrawAmt) {
        System.out.println("*");
        System.out.println("****** deposit/TransactionSessionBean: cashWithdraw() ******");

        Long bankAccountId;
        Long accTransactionId = Long.valueOf(0);

        BankAccount bankAccount = retrieveBankAccountByNum(bankAccountNum);
        Interest interest = bankAccount.getInterest();

        bankAccountId = bankAccount.getBankAccountId();

        if (bankAccountId == null) {
            return accTransactionId;
        } else {
            String accountCredit = " ";
            String transactionCode = "AWL";
            String transactionRef = "Merlion Bank Branch";

            Calendar cal = Calendar.getInstance();
//            int year = cal.get(Calendar.YEAR);
//            int month = cal.get(Calendar.MONTH);
//            int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
//            String transactionDate = dayOfMonth + "-" + (month + 1) + "-" + year;
            Long transactionDateMilis = cal.getTimeInMillis();

            accTransactionId = addNewTransaction(cal.getTime().toString(), transactionCode, transactionRef,
                    withdrawAmt, accountCredit, transactionDateMilis, bankAccountId);

            Double preBalance = Double.valueOf(bankAccount.getBankAccountBalance());
            Double withdrawAmtDouble = Double.valueOf(withdrawAmt);
            Double totalBalance = preBalance - withdrawAmtDouble;
            String balance = totalBalance.toString();
            bankAccount.setBankAccountBalance(balance);
            interest.setIsWithdraw("1");

            return accTransactionId;
        }
    }

    @Override
    public Long fundTransfer(String fromAccount, String toAccount, String transferAmt) {
        System.out.println("*");
        System.out.println("****** deposit/TransactionSessionBean: fundTransfer() ******");

        BankAccount bankAccountFrom = bankAccountSessionLocal.retrieveBankAccountByNum(fromAccount);
        BankAccount bankAccountTo = bankAccountSessionLocal.retrieveBankAccountByNum(toAccount);

        if (bankAccountTo.getBankAccountType().equals("Monthly Savings Account")) {
            if (Double.valueOf(transferAmt) >= 50) {
                bankAccountTo.setBankAccountMinSaving("Sufficient");
            }
        }
        Double balanceAccountFrom = Double.valueOf(bankAccountFrom.getBankAccountBalance()) - Double.valueOf(transferAmt);
        Double balanceAccountTo = Double.valueOf(bankAccountTo.getBankAccountBalance()) + Double.valueOf(transferAmt);

        Long bankAccountFromId = bankAccountFrom.getBankAccountId();
        Long bankAccountToId = bankAccountTo.getBankAccountId();

        String transactionCode = "TRF";
        String transactionRefFrom = bankAccountTo.getBankAccountType()+"-"+toAccount;
        String transactionRefTo = bankAccountFrom.getBankAccountType()+"-"+fromAccount;

        Calendar cal = Calendar.getInstance();
//        int year = cal.get(Calendar.YEAR);
//        int month = cal.get(Calendar.MONTH);
//        int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
//        String transactionDate = dayOfMonth + "-" + (month + 1) + "-" + year;
        Long transactionDateMilis = cal.getTimeInMillis();

        Long fromTransactionId = addNewTransaction(cal.getTime().toString(), transactionCode, transactionRefFrom,
                transferAmt, " ", transactionDateMilis, bankAccountFromId);
        Long toTransactionId = addNewTransaction(cal.getTime().toString(), transactionCode, transactionRefTo,
                " ", transferAmt, transactionDateMilis, bankAccountToId);

        bankAccountFrom.setBankAccountBalance(balanceAccountFrom.toString());
        bankAccountTo.setBankAccountBalance(balanceAccountTo.toString());
        bankAccountFrom.getInterest().setIsTransfer("1");

        Double currentDailyTransferLimit = Double.valueOf(bankAccountFrom.getTransferBalance()) - Double.valueOf(transferAmt);
        bankAccountFrom.setTransferBalance(currentDailyTransferLimit.toString());

        return fromTransactionId;
    }

    private String md5Hashing(String stringToHash) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        return Arrays.toString(md.digest(stringToHash.getBytes()));
    }

    @Override
    public String checkAccountActivation(String bankAccountNum, String initialDepositAmount) {
        System.out.println("*");
        System.out.println("****** deposit/TransactionSessionBean: checkAccountActivation() ******");
        BankAccount bankAccount = bankAccountSessionLocal.retrieveBankAccountByNum(bankAccountNum);
        String bankAccountType = bankAccount.getBankAccountType();

        if (bankAccountType.equals("Bonus Savings Account")) {
            if (Double.valueOf(initialDepositAmount) < 3000) {
                return "Initial deposit amount is insufficient.";
            } else {
                bankAccount.setBankAccountStatus("Active");
            }
        } else if (bankAccountType.equals("Basic Savings Account")) {
            if (Double.valueOf(initialDepositAmount) < 1) {
                return "Initial deposit amount is insufficient.";
            } else {
                bankAccount.setBankAccountStatus("Active");
            }
        } else if (bankAccountType.equals("Fixed Deposit Account")) {

            if (bankAccount.getBankAccountDepositPeriod().equals("None")) {
                return "Please declare your deposit period";
            } else {

                if (Double.valueOf(initialDepositAmount) < 1000) {
                    return "Initial deposit amount is insufficient.";
                } else if (Double.valueOf(initialDepositAmount) > 999999) {
                    return "Please contact us at 800 820 8820 or visit our branch.";
                } else {
                    bankAccount.setBankAccountStatus("Active");
                }
            }
        }

        return "Activated successfully.";
    }
    
    @Override
    public void deleteAccTransaction(Long transactionId) {
        AccTransaction transaction = bankAccountSessionLocal.retrieveAccTransactionById(transactionId);
        
        entityManager.remove(transaction);
        entityManager.flush();
    }
}
