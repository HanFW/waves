package session.stateless;

import entity.BankAccount;
import entity.AccTransaction;
import entity.CustomerBasic;
import entity.Interest;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NonUniqueResultException;

@Stateless
@LocalBean
public class BankAccountSession implements BankAccountSessionLocal {
    @EJB
    private AdminSessionBeanLocal adminSessionBeanLocal;

    @EJB
    private TransactionSessionLocal transactionSessionLocal;

    @EJB
    private InterestSessionLocal interestSessionLocal;

    @EJB
    private CRMCustomerSessionBean customerSessionBean;
    
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public BankAccount retrieveBankAccountById(Long bankAccountId) {
        BankAccount bankAccount = new BankAccount();

        System.out.println(bankAccountId);
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

            if (query.getResultList().isEmpty()) {
                return new BankAccount();
            } else {
                bankAccount = (BankAccount) query.getSingleResult();
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error: " + enfe.getMessage());
            return new BankAccount();
        } catch (NonUniqueResultException nure) {
            System.out.println("\nNon unique result error: " + nure.getMessage());
        }

        return bankAccount;
    }

    @Override
    public List<BankAccount> retrieveBankAccountByCusIC(String customerIdentificationNum) {
        CustomerBasic customerBasic = customerSessionBean.retrieveCustomerBasicByIC(customerIdentificationNum.toUpperCase());
        
        if (customerBasic.getCustomerBasicId() == null) {
            return new ArrayList<BankAccount>();
        }
        try {
            Query query = entityManager.createQuery("Select a From BankAccount a Where a.customerBasic=:customerBasic");
            query.setParameter("customerBasic", customerBasic);
            return query.getResultList();
        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error: " + enfe.getMessage());
            return new ArrayList<BankAccount>();
        }
    }

    @Override
    public CustomerBasic retrieveCustomerBasicById(Long customerBasicId) {
        CustomerBasic customerBasic = new CustomerBasic();

        try {
            Query query = entityManager.createQuery("Select c From CustomerBasic c Where c.customerBasicId=:customerBasicId");
            query.setParameter("customerBasicId", customerBasicId);
            customerBasic = (CustomerBasic) query.getSingleResult();
        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error: " + enfe.getMessage());
            return new CustomerBasic();
        } catch (NonUniqueResultException nure) {
            System.out.println("\nNon unique result error: " + nure.getMessage());
        }

        return customerBasic;
    }

    @Override
    public CustomerBasic retrieveCustomerBasicByIC(String customerIdentificationNum) {
        CustomerBasic customerBasic = new CustomerBasic();

        try {
            Query query = entityManager.createQuery("Select c From CustomerBasic c Where c.customerIdentificationNum=:customerIdentificationNum");
            query.setParameter("customerIdentificationNum", customerIdentificationNum.toUpperCase());
            customerBasic = (CustomerBasic) query.getSingleResult();
        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error: " + enfe.getMessage());
            return new CustomerBasic();
        } catch (NonUniqueResultException nure) {
            System.out.println("\nNon unique result error: " + nure.getMessage());
        }

        return customerBasic;
    }

    @Override
    public AccTransaction retrieveAccTransactionById(Long transactionId) {
        AccTransaction accTransaction = new AccTransaction();

        try {
            Query query = entityManager.createQuery("Select t From AccTransaction t Where t.transactionId=:transactionId");
            query.setParameter("transactionId", transactionId);
            accTransaction = (AccTransaction) query.getSingleResult();
        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error: " + enfe.getMessage());
            return new AccTransaction();
        } catch (NonUniqueResultException nure) {
            System.out.println("\nNon unique result error: " + nure.getMessage());
        }

        return accTransaction;
    }

    @Override
    public List<BankAccount> getAllBankAccount() {
        Query query = entityManager.createQuery("SELECT a FROM BankAccount a");
        return query.getResultList();
    }

    @Override
    public Long addNewAccount(String bankAccountNum, String bankAccountPwd,
            String bankAccountType, String bankAccountBalance, String transferDailyLimit,
            String transferBalance, String bankAccountStatus, Long customerBasicId, Long interestId) {

        BankAccount bankAccount = new BankAccount();
        String hashedPwd="";

        try {
            hashedPwd = md5Hashing(bankAccountPwd);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(BankAccountSession.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        bankAccount.setBankAccountNum(bankAccountNum);
        bankAccount.setBankAccountPwd(hashedPwd);
        bankAccount.setBankAccountTyep(bankAccountType);
        bankAccount.setBankAccountBalance(bankAccountBalance);
        bankAccount.setTransferDailyLimit(transferDailyLimit);
        bankAccount.setTransferBalance(transferBalance);
        bankAccount.setBankAccountStatus(bankAccountStatus);
        bankAccount.setCustomerBasic(retrieveCustomerBasicById(customerBasicId));
        bankAccount.setInterest(interestSessionLocal.retrieveInterestById(interestId));

        entityManager.persist(bankAccount);
        entityManager.flush();
        
        String onlineAccount = adminSessionBeanLocal.createOnlineBankingAccount(customerBasicId);

        return bankAccount.getBankAccountId();
    }

    @Override
    public String deleteAccount(String bankAccountNum) {
        Query query = entityManager.createQuery("Select a From BankAccount a Where a.bankAccountNum=:bankAccountNum");
        query.setParameter("bankAccountNum", bankAccountNum);
        List<BankAccount> result = query.getResultList();

        if (result.isEmpty()) {
            return "Error! Account does not exist!";
        } else {
            BankAccount bankAccount = result.get(0);
            entityManager.remove(bankAccount);

            return "Successfully deleted!";
        }
    }

    @Override
    public void activateAccounts(String bankAccountNum) {
        DecimalFormat df = new DecimalFormat("#.00");

        BankAccount bankAccount = retrieveBankAccountByNum(bankAccountNum);

        Double currentInterest = Double.valueOf(bankAccount.getInterest().getDailyInterest());
        Double currentBalance = Double.valueOf(bankAccount.getBankAccountBalance());

        Double totalInterest = currentInterest + currentBalance * 0.0005;
        Double accuredInterest = Double.valueOf(df.format(totalInterest));

        Interest interest = bankAccount.getInterest();
        interest.setDailyInterest(accuredInterest.toString());

    }

    @Override
    public void interestCrediting(String bankAccountNum) {
        DecimalFormat df = new DecimalFormat("#.00");

        BankAccount bankAccount = retrieveBankAccountByNum(bankAccountNum);

        Interest interest = bankAccount.getInterest();
        Double dailyInterest = Double.valueOf(interest.getDailyInterest());

        if ((interest.getIsTransfer().equals("0")) && (interest.getIsWithdraw().equals("0"))) {
            Double bonusInterest = Double.valueOf(bankAccount.getBankAccountBalance()) * 0.0035;
            Double totalInterest = dailyInterest + bonusInterest;
            Double creditedInterest = Double.valueOf(df.format(totalInterest));

            Date date = new Date();

            String accountCredit = null;
            String transactionCode = "DEFI";
            String transactionRef = "Interest Crediting";

            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
            String transactionDate = dayOfMonth + "-" + (month + 1) + "-" + year;

            interest.setMonthlyInterest(creditedInterest.toString());

            Long newAccTransactionId = transactionSessionLocal.addNewTransaction(transactionDate, transactionCode, transactionRef,
                    creditedInterest.toString(), accountCredit, bankAccount.getBankAccountId());

            interest.setDailyInterest("0");
            interest.setIsTransfer("0");
            interest.setIsWithdraw("0");
        }
    }

    @Override
    public String checkAccountDuplication(String bankAccountNum) {
        BankAccount bankAccount = retrieveBankAccountByNum(bankAccountNum);

        if (bankAccount.getBankAccountId() != null) {
            return "Duplicated";
        } else {
            return "Success";
        }
    }

    @Override
    public String generateBankAccount(String customerIdentificationNum) {
        String bankAccountNum;
        String status;
        SecureRandom random = new SecureRandom();

        bankAccountNum = new BigInteger(13, random).setBit(12).toString(10) + "-" + Math.abs(customerIdentificationNum.toUpperCase().hashCode());
        status = checkAccountDuplication(bankAccountNum);

        while (status.equals("Duplicated")) {
            bankAccountNum = new BigInteger(13, random).setBit(12).toString(10) + "-" + Math.abs(customerIdentificationNum.toUpperCase().hashCode());
            status = checkAccountDuplication(bankAccountNum);
        }

        return bankAccountNum;
    }

    @Override
    public boolean checkExistence(String customerIdentificationNum) {
        List<BankAccount> bankAccount = retrieveBankAccountByCusIC(customerIdentificationNum.toUpperCase());

        if (bankAccount.isEmpty()) {
            return false;
        }

        return true;
    }

    @Override
    public String changeDateFormat(Date customerDateOfBirth) {
        String dateOfBirth = customerDateOfBirth.toString();
        String[] dateSplit = dateOfBirth.split(" ");
        String changedDate = dateSplit[2] + "/" + dateSplit[1] + "/" + dateSplit[5];

        return changedDate;
    }
    
    private String md5Hashing(String stringToHash) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        return Arrays.toString(md.digest(stringToHash.getBytes()));
    }
}