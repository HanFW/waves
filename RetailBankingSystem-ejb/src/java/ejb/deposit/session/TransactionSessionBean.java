package ejb.deposit.session;

import ejb.deposit.entity.BankAccount;
import ejb.deposit.entity.AccTransaction;
import ejb.deposit.entity.Interest;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
        BankAccount bankAccount = new BankAccount();

        try {
            Query query = entityManager.createQuery("Select a From BankAccount a Where a.bankAccountId=:bankAccountId");
            query.setParameter("bankAccountId", bankAccountId);
            bankAccount = (BankAccount) query.getSingleResult();
        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error: " + enfe.getMessage());
            return new BankAccount();
        } catch (NonUniqueResultException nure) {
            System.out.println("\nNon unique result error: " + nure.getMessage());
        }

        return bankAccount;
    }

    @Override
    public BankAccount retrieveBankAccountByNum(String bankAccountNum) {
        BankAccount bankAccount = new BankAccount();

        try {
            Query query = entityManager.createQuery("Select a From BankAccount a Where a.bankAccountNum=:bankAccountNum");
            query.setParameter("bankAccountNum", bankAccountNum);
            bankAccount = (BankAccount) query.getSingleResult();
        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error: " + enfe.getMessage());
            return new BankAccount();
        } catch (NonUniqueResultException nure) {
            System.out.println("\nNon unique result error: " + nure.getMessage());
        }

        return bankAccount;
    }

    @Override
    public List<AccTransaction> retrieveAccTransactionByBankNum(String bankAccountNum) {
        BankAccount bankAccount = bankAccountSessionLocal.retrieveBankAccountByNum(bankAccountNum);

        if (bankAccount == null) {
            return null;
        }
        try {
            Query query = entityManager.createQuery("Select t From AccTransaction t Where t.bankAccount=:bankAccount");
            query.setParameter("bankAccount", bankAccount);
            return query.getResultList();
        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error: " + enfe.getMessage());
            return null;
        }
    }

    @Override
    public Long addNewTransaction(String transactionDate, String transactionCode, String transactionRef,
            String accountDebit, String accountCredit, String transactionDateMilis, Long bankAccountId) {
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
    public String cashDeposit(String bankAccountNum, String depositAmt) {

        Long bankAccountId;
        Long accTransactionId;

        BankAccount bankAccount = retrieveBankAccountByNum(bankAccountNum);
        bankAccountId = bankAccount.getBankAccountId();

        if (bankAccountId == null) {
            return "Error! Bank account does not exist!";
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
            String transactionDateMilis = String.valueOf(cal.getTimeInMillis());

            accTransactionId = addNewTransaction(cal.getTime().toString(), transactionCode, transactionRef,
                    accountDebit, depositAmt, transactionDateMilis, bankAccountId);

            Double preBalance = Double.valueOf(bankAccount.getBankAccountBalance());
            Double depositAmtDouble = Double.valueOf(depositAmt);
            Double totalBalance = preBalance + depositAmtDouble;
            String balance = totalBalance.toString();
            bankAccount.setBankAccountBalance(balance);

            return "Deposit successfully!";
        }
    }

    @Override
    public String cashWithdraw(String bankAccountNum, String withdrawAmt) {

        Long bankAccountId;
        Long accTransactionId;

        BankAccount bankAccount = retrieveBankAccountByNum(bankAccountNum);
        Interest interest = bankAccount.getInterest();

        bankAccountId = bankAccount.getBankAccountId();

        if (bankAccountId == null) {
            return "Error! Bank account does not exist!";
        } else {
            String accountCredit = " ";
            String transactionCode = "AWL";
            String transactionRef = "Merlion Bank Branch";

            Calendar cal = Calendar.getInstance();
//            int year = cal.get(Calendar.YEAR);
//            int month = cal.get(Calendar.MONTH);
//            int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
//            String transactionDate = dayOfMonth + "-" + (month + 1) + "-" + year;
            String transactionDateMilis = String.valueOf(cal.getTimeInMillis());

            accTransactionId = addNewTransaction(cal.getTime().toString(), transactionCode, transactionRef,
                    withdrawAmt, accountCredit, transactionDateMilis, bankAccountId);

            Double preBalance = Double.valueOf(bankAccount.getBankAccountBalance());
            Double withdrawAmtDouble = Double.valueOf(withdrawAmt);
            Double totalBalance = preBalance - withdrawAmtDouble;
            String balance = totalBalance.toString();
            bankAccount.setBankAccountBalance(balance);
            interest.setIsWithdraw("1");

            return "Withdraw successfully!";
        }
    }

    @Override
    public String checkPassword(String bankAccountNum, String bankAccountPwd) {

        BankAccount bankAccount = bankAccountSessionLocal.retrieveBankAccountByNum(bankAccountNum);
        String hashedPwd = "";

        try {
            hashedPwd = md5Hashing(bankAccountPwd);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(BankAccountSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (bankAccount.getBankAccountId() == null) {
            return "Error! Bank account does not exist!";
        } else {
            if (!hashedPwd.equals(bankAccount.getBankAccountPwd())) {
                return "Password is incorrect!";
            }
        }
        return "Password is correct!";
    }

    @Override
    public Long fundTransfer(String fromAccount, String toAccount, String transferAmt) {

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
        String transactionRefFrom = toAccount;
        String transactionRefTo = fromAccount;

        Calendar cal = Calendar.getInstance();
//        int year = cal.get(Calendar.YEAR);
//        int month = cal.get(Calendar.MONTH);
//        int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
//        String transactionDate = dayOfMonth + "-" + (month + 1) + "-" + year;
        String transactionDateMilis = String.valueOf(cal.getTimeInMillis());

        Long fromTransactionId = addNewTransaction(cal.getTime().toString(), transactionCode, transactionRefFrom,
                transferAmt, " ", transactionDateMilis, bankAccountFromId);
        Long toTransactionId = addNewTransaction(cal.getTime().toString(), transactionCode, transactionRefTo,
                " ", transferAmt, transactionDateMilis, bankAccountToId);

        bankAccountFrom.setBankAccountBalance(balanceAccountFrom.toString());
        bankAccountTo.setBankAccountBalance(balanceAccountTo.toString());
        bankAccountFrom.getInterest().setIsTransfer("1");

        Double transfer = Double.valueOf(bankAccountFrom.getTransferBalance()) - Double.valueOf(transferAmt);
        bankAccountFrom.setTransferBalance(transfer.toString());

        return fromTransactionId;
    }

    @Override
    public void initialDeposit(Long bankAccountId, String initialDepositAmt) {
        bankAccountSessionLocal.retrieveBankAccountById(bankAccountId).setBankAccountBalance(initialDepositAmt);
    }

    private String md5Hashing(String stringToHash) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        return Arrays.toString(md.digest(stringToHash.getBytes()));
    }

    @Override
    public String checkAccountActivation(String bankAccountNum, String initialDepositAmount) {
        BankAccount bankAccount = bankAccountSessionLocal.retrieveBankAccountByNum(bankAccountNum);
        String bankAccountType = bankAccount.getBankAccountType();

        if (bankAccountType.equals("Bonus Savings Account")) {
            if (Double.valueOf(initialDepositAmount) < 3000) {
                return "Initial deposit amount is insufficient.";
            } else {
                bankAccount.setBankAccountStatus("Activated");
            }
        } else if (bankAccountType.equals("Basic Savings Account")) {
            if (Double.valueOf(initialDepositAmount) < 1) {
                return "Initial deposit amount is insufficient.";
            } else {
                bankAccount.setBankAccountStatus("Activated");
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
                    bankAccount.setBankAccountStatus("Activated");
                }
            }
        }

        return "Activated successfully.";
    }
}
