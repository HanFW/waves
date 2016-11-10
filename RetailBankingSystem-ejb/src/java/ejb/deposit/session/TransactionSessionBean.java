package ejb.deposit.session;

import ejb.deposit.entity.BankAccount;
import ejb.deposit.entity.AccTransaction;
import ejb.deposit.entity.Interest;
import java.util.ArrayList;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Calendar;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NonUniqueResultException;
import javax.xml.ws.WebServiceRef;
import ws.client.otherbanks.OtherBankAccount;
import ws.client.otherbanks.OtherBanksWebService_Service;

@Stateless
public class TransactionSessionBean implements TransactionSessionBeanLocal, TransactionSessionBeanRemote {

    @WebServiceRef(wsdlLocation = "META-INF/wsdl/localhost_8080/OtherBanksWebService/OtherBanksWebService.wsdl")
    private OtherBanksWebService_Service service;

    @EJB
    private BankAccountSessionBeanLocal bankAccountSessionBeanLocal;

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
        BankAccount bankAccount = bankAccountSessionBeanLocal.retrieveBankAccountByNum(bankAccountNum);

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

            Long transactionDateMilis = cal.getTimeInMillis();

            accTransactionId = addNewTransaction(cal.getTime().toString(), transactionCode, transactionRef,
                    accountDebit, depositAmt, transactionDateMilis, bankAccountId);

            Double preAvailableBalance = Double.valueOf(bankAccount.getAvailableBankAccountBalance());
            Double preTotalBalance = Double.valueOf(bankAccount.getTotalBankAccountBalance());

            Double depositAmtDouble = Double.valueOf(depositAmt);

            Double availableBalance = preAvailableBalance + depositAmtDouble;
            Double totalBalance = preTotalBalance + depositAmtDouble;

            bankAccount.setAvailableBankAccountBalance(availableBalance.toString());
            bankAccount.setTotalBankAccountBalance(totalBalance.toString());

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

            Long transactionDateMilis = cal.getTimeInMillis();

            accTransactionId = addNewTransaction(cal.getTime().toString(), transactionCode, transactionRef,
                    withdrawAmt, accountCredit, transactionDateMilis, bankAccountId);

            Double preAvailableBalance = Double.valueOf(bankAccount.getAvailableBankAccountBalance());
            Double preTotalBalance = Double.valueOf(bankAccount.getTotalBankAccountBalance());

            Double withdrawAmtDouble = Double.valueOf(withdrawAmt);

            Double availableBalance = preAvailableBalance - withdrawAmtDouble;
            Double totalBalance = preTotalBalance - withdrawAmtDouble;

            bankAccount.setAvailableBankAccountBalance(availableBalance.toString());
            bankAccount.setTotalBankAccountBalance(totalBalance.toString());

            interest.setIsWithdraw("1");

            return accTransactionId;
        }
    }

    @Override
    public Long fundTransfer(String fromAccount, String toAccount, String transferAmt) {
        System.out.println("*");
        System.out.println("****** deposit/TransactionSessionBean: fundTransfer() ******");

        BankAccount bankAccountFrom = bankAccountSessionBeanLocal.retrieveBankAccountByNum(fromAccount);
        BankAccount bankAccountTo = bankAccountSessionBeanLocal.retrieveBankAccountByNum(toAccount);

        if (bankAccountTo.getBankAccountType().equals("Monthly Savings Account")) {
            if (Double.valueOf(transferAmt) >= 50) {
                bankAccountTo.setBankAccountMinSaving("Sufficient");
            }
        }
        Double totalBalanceAccountFrom = Double.valueOf(bankAccountFrom.getTotalBankAccountBalance()) - Double.valueOf(transferAmt);
        Double availableBalanceAccountFrom = Double.valueOf(bankAccountFrom.getAvailableBankAccountBalance()) - Double.valueOf(transferAmt);

        Double totalBalanceAccountTo = Double.valueOf(bankAccountTo.getTotalBankAccountBalance()) + Double.valueOf(transferAmt);
        Double availableBalanceAccountTo = Double.valueOf(bankAccountTo.getAvailableBankAccountBalance()) + Double.valueOf(transferAmt);

        Long bankAccountFromId = bankAccountFrom.getBankAccountId();
        Long bankAccountToId = bankAccountTo.getBankAccountId();

        String transactionCode = "TRF";
        String transactionRefFrom = bankAccountTo.getBankAccountType() + "-" + toAccount;
        String transactionRefTo = bankAccountFrom.getBankAccountType() + "-" + fromAccount;

        Calendar cal = Calendar.getInstance();

        Long transactionDateMilis = cal.getTimeInMillis();

        Long fromTransactionId = addNewTransaction(cal.getTime().toString(), transactionCode, transactionRefFrom,
                transferAmt, " ", transactionDateMilis, bankAccountFromId);
        Long toTransactionId = addNewTransaction(cal.getTime().toString(), transactionCode, transactionRefTo,
                " ", transferAmt, transactionDateMilis, bankAccountToId);

        bankAccountFrom.setAvailableBankAccountBalance(availableBalanceAccountFrom.toString());
        bankAccountFrom.setTotalBankAccountBalance(totalBalanceAccountFrom.toString());

        bankAccountTo.setAvailableBankAccountBalance(totalBalanceAccountTo.toString());
        bankAccountTo.setTotalBankAccountBalance(availableBalanceAccountTo.toString());

        bankAccountFrom.getInterest().setIsTransfer("1");

        Double currentDailyTransferLimit = Double.valueOf(bankAccountFrom.getTransferBalance()) - Double.valueOf(transferAmt);
        bankAccountFrom.setTransferBalance(currentDailyTransferLimit.toString());

        return fromTransactionId;
    }
    
    @Override
    public String checkAccountActivation(String bankAccountNum, String initialDepositAmount) {
        System.out.println("*");
        System.out.println("****** deposit/TransactionSessionBean: checkAccountActivation() ******");
        BankAccount bankAccount = bankAccountSessionBeanLocal.retrieveBankAccountByNum(bankAccountNum);
        String bankAccountType = bankAccount.getBankAccountType();

        if (bankAccountType.equals("Bonus Savings Account")) {
            if (Double.valueOf(initialDepositAmount) < 3000) {
                return "Insufficient";
            } else {
                bankAccount.setBankAccountStatus("Active");
            }
        } else if (bankAccountType.equals("Basic Savings Account")) {
            if (Double.valueOf(initialDepositAmount) < 1) {
                return "Insufficient";
            } else {
                bankAccount.setBankAccountStatus("Active");
            }
        } else if (bankAccountType.equals("Fixed Deposit Account")) {

            if (bankAccount.getBankAccountDepositPeriod().equals("None")) {
                return "Declare";
            } else {

                if (Double.valueOf(initialDepositAmount) < 1000) {
                    return "Insufficient";
                } else if (Double.valueOf(initialDepositAmount) > 999999) {
                    return "Contact";
                } else {
                    bankAccount.setBankAccountStatus("Active");
                }
            }
        }

        return "Activated";
    }

    @Override
    public String deleteAccTransaction(Long transactionId) {
        AccTransaction transaction = bankAccountSessionBeanLocal.retrieveAccTransactionById(transactionId);

        entityManager.remove(transaction);
        entityManager.flush();
        
        return "Successfully Deleted!";
    }

    @Override
    public String fastTransfer(String fromBankAccount, String toBankAccount, Double transferAmt) {

        OtherBankAccount otherBankAccount = retrieveBankAccountByNum_other(fromBankAccount);
        BankAccount bankAccount = bankAccountSessionBeanLocal.retrieveBankAccountByNum(toBankAccount);
        Double totalBalance = Double.valueOf(bankAccount.getTotalBankAccountBalance()) + transferAmt;
        Double availableBalance = Double.valueOf(bankAccount.getAvailableBankAccountBalance()) + transferAmt;

        Calendar cal = Calendar.getInstance();
        String transactionCode = "ICT";
        String transactionRef = "Transfer from " + otherBankAccount.getOtherBankAccountType() + "-" + otherBankAccount.getOtherBankAccountNum();
        String accountDebit = " ";
        String accountCredit = transferAmt.toString();
        Long transactionDateMilis = cal.getTimeInMillis();

        Long transactionId = addNewTransaction(cal.getTime().toString(),
                transactionCode, transactionRef, accountDebit, accountCredit,
                transactionDateMilis, bankAccount.getBankAccountId());

        bankAccount.setAvailableBankAccountBalance(availableBalance.toString());
        bankAccount.setTotalBankAccountBalance(totalBalance.toString());
        
        return "Successfully Transfered!";

    }

    private OtherBankAccount retrieveBankAccountByNum_other(java.lang.String otherBankAccountNum) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        ws.client.otherbanks.OtherBanksWebService port = service.getOtherBanksWebServicePort();
        return port.retrieveBankAccountByNum(otherBankAccountNum);
    }
}
