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
    public Long addNewAccount(String bankAccountNum, String bankAccountPwd,
            String bankAccountType, String bankAccountBalance, String transferDailyLimit,
            String transferBalance, String bankAccountStatus, String bankAccountMinSaving,
            String bankAccountDepositPeriod, String currentFixedDepositPeriod,
            String fixedDepositStatus, Long customerBasicId, Long interestId) {

        BankAccount bankAccount = new BankAccount();
        String hashedPwd = "";

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
        bankAccount.setBankAccountMinSaving(bankAccountMinSaving);
        bankAccount.setBankAccountDepositPeriod(bankAccountDepositPeriod);
        bankAccount.setCurrentFixedDepositPeriod(currentFixedDepositPeriod);
        bankAccount.setFixedDepositStatus(fixedDepositStatus);
        bankAccount.setCustomerBasic(retrieveCustomerBasicById(customerBasicId));
        bankAccount.setInterest(interestSessionLocal.retrieveInterestById(interestId));

        entityManager.persist(bankAccount);
        entityManager.flush();

        String onlineBankingAccount = adminSessionBeanLocal.createOnlineBankingAccount(customerBasicId);

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
    public void interestAccuring() {
        DecimalFormat df = new DecimalFormat("#.00");

        Query query = entityManager.createQuery("SELECT a FROM BankAccount a WHERE a.bankAccountStatus = :bankAccountStatus");
        query.setParameter("bankAccountStatus", "Activated");
        List<BankAccount> activatedBankAccounts = query.getResultList();

        for (BankAccount activatedBankAccount : activatedBankAccounts) {

            Double currentInterest = 0.0;
            Double currentBalance = 0.0;
            Double totalInterest = 0.0;
            Double accuredInterest = 0.0;
            Double finalBalance = 0.0;
            Double finalInterest = 0.0;
            Interest interest = new Interest();

            if (activatedBankAccount.getFixedDepositStatus().equals("Deposited") && activatedBankAccount.getBankAccountType().equals("Fixed Deposit Account")) {

                if (activatedBankAccount.getInterest().getIsTransfer().equals("1") || activatedBankAccount.getInterest().getIsWithdraw().equals("1")) {

                    interest = activatedBankAccount.getInterest();
                    interest.setIsTransfer("0");
                    interest.setIsWithdraw("0");

                    currentInterest = Double.valueOf(activatedBankAccount.getInterest().getDailyInterest());
                    interest.setDailyInterest("0");
                    activatedBankAccount.setFixedDepositStatus("Withdrawn");
                    activatedBankAccount.setBankAccountDepositPeriod("None");

                    finalInterest = currentInterest * 0.4;

                    currentBalance = Double.valueOf(activatedBankAccount.getBankAccountBalance());
                    finalBalance = currentBalance + finalInterest;
                    activatedBankAccount.setBankAccountBalance(df.format(finalBalance));

                    String accountCredit = " ";
                    String transactionCode = "DEFI";
                    String transactionRef = "Interest Crediting";

                    Calendar cal = Calendar.getInstance();
                    int year = cal.get(Calendar.YEAR);
                    int month = cal.get(Calendar.MONTH);
                    int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
                    String transactionDate = dayOfMonth + "-" + (month + 1) + "-" + year;

                    Long newAccTransactionId = transactionSessionLocal.addNewTransaction(transactionDate, transactionCode, transactionRef,
                            finalInterest.toString(), accountCredit, activatedBankAccount.getBankAccountId());

                } else {

                    Double currentPeriod = Double.valueOf(activatedBankAccount.getCurrentFixedDepositPeriod());
                    Double finalPeriod = currentPeriod + 1;
                    activatedBankAccount.setCurrentFixedDepositPeriod(finalPeriod.toString());

                    currentInterest = Double.valueOf(activatedBankAccount.getInterest().getDailyInterest());
                    currentBalance = Double.valueOf(activatedBankAccount.getBankAccountBalance());
                    String interestRate = checkInterestRate(activatedBankAccount.getBankAccountDepositPeriod());

                    totalInterest = currentInterest + currentBalance * Double.valueOf(interestRate) / 360;
                    accuredInterest = Double.valueOf(df.format(totalInterest));

                    interest = activatedBankAccount.getInterest();

                    if (activatedBankAccount.getCurrentFixedDepositPeriod().equals(activatedBankAccount.getBankAccountDepositPeriod())) {

                        interest.setDailyInterest(totalInterest.toString());
                        finalBalance = currentBalance + totalInterest;

                        interest.setDailyInterest("0");
                        activatedBankAccount.setBankAccountBalance(df.format(finalBalance));
                        activatedBankAccount.setFixedDepositStatus("Withdrawn");
                        activatedBankAccount.setBankAccountDepositPeriod("None");
                        activatedBankAccount.setBankAccountStatus("Inactivated");

                        String accountCredit = " ";
                        String transactionCode = "DEFI";
                        String transactionRef = "Interest Crediting";

                        Calendar cal = Calendar.getInstance();
                        int year = cal.get(Calendar.YEAR);
                        int month = cal.get(Calendar.MONTH);
                        int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
                        String transactionDate = dayOfMonth + "-" + (month + 1) + "-" + year;

                        Long newAccTransactionId = transactionSessionLocal.addNewTransaction(transactionDate, transactionCode, transactionRef,
                                accuredInterest.toString(), accountCredit, activatedBankAccount.getBankAccountId());
                    } else {
                        interest.setDailyInterest(totalInterest.toString());
                    }
                }
            } else if (activatedBankAccount.getFixedDepositStatus().equals("Withdrawn") && activatedBankAccount.getBankAccountType().equals("Fixed Deposit Account")) {

                currentInterest = Double.valueOf(activatedBankAccount.getInterest().getDailyInterest());
                currentBalance = Double.valueOf(activatedBankAccount.getBankAccountBalance());

                totalInterest = currentInterest + currentBalance * 0.0005 / 360;

                interest = activatedBankAccount.getInterest();
                interest.setDailyInterest(totalInterest.toString());

            } else {

                currentInterest = Double.valueOf(activatedBankAccount.getInterest().getDailyInterest());
                currentBalance = Double.valueOf(activatedBankAccount.getBankAccountBalance());

                totalInterest = currentInterest + currentBalance * 0.0005 / 360;

                interest = activatedBankAccount.getInterest();

                interest.setDailyInterest(totalInterest.toString());
            }
        }

    }

    @Override
    public void interestCrediting() {
        DecimalFormat df = new DecimalFormat("#.00");

        Query query = entityManager.createQuery("SELECT a FROM BankAccount a WHERE a.bankAccountStatus = :bankAccountStatus");
        query.setParameter("bankAccountStatus", "Activated");
        List<BankAccount> activatedBankAccounts = query.getResultList();

        for (BankAccount activatedBankAccount : activatedBankAccounts) {

            Interest interest = activatedBankAccount.getInterest();
            Double dailyInterest = Double.valueOf(interest.getDailyInterest());
            Double bonusInterest = 0.0;
            Double totalInterest = 0.0;
            Double creditedInterest = 0.0;
            Double finalBalance = 0.0;

            if (activatedBankAccount.getBankAccountType().equals("Fixed Deposit Account") && !activatedBankAccount.getFixedDepositStatus().equals("Withdrawn")) {
                System.out.println("One Month Gone");
            } else if (activatedBankAccount.getBankAccountType().equals("Fixed Deposit Account") && activatedBankAccount.getFixedDepositStatus().equals("Withdrawn")) {

                bonusInterest = 0.0;
                totalInterest = dailyInterest + bonusInterest;
                creditedInterest = Double.valueOf(df.format(totalInterest));
                finalBalance = Double.valueOf(activatedBankAccount.getBankAccountBalance()) + totalInterest;

                interest.setMonthlyInterest(totalInterest.toString());
                activatedBankAccount.setBankAccountBalance(df.format(finalBalance));

                String accountCredit = " ";
                String transactionCode = "DEFI";
                String transactionRef = "Interest Crediting";

                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
                String transactionDate = dayOfMonth + "-" + (month + 1) + "-" + year;

                Long newAccTransactionId = transactionSessionLocal.addNewTransaction(transactionDate, transactionCode, transactionRef,
                        creditedInterest.toString(), accountCredit, activatedBankAccount.getBankAccountId());

            } else {

                if ((interest.getIsTransfer().equals("0")) && (interest.getIsWithdraw().equals("0"))) {
                    interest.setDailyInterest("0");

                    if (activatedBankAccount.getBankAccountType().equals("Monthly Savings Account")) {

                        if (activatedBankAccount.getBankAccountMinSaving().equals("Sufficient")) {
                            bonusInterest = Double.valueOf(activatedBankAccount.getBankAccountBalance()) * 0.0035 / 12;
                            activatedBankAccount.setBankAccountMinSaving("Insufficient");
                        } else if (activatedBankAccount.getBankAccountMinSaving().equals("Insufficient")) {
                            bonusInterest = 0.0;
                        }
                    } else if (activatedBankAccount.getBankAccountType().equals("Bonus Savings Account")) {
                        bonusInterest = Double.valueOf(activatedBankAccount.getBankAccountBalance()) * 0.0075 / 12;
                    } else {
                        bonusInterest = 0.0;
                    }

                    totalInterest = dailyInterest + bonusInterest;
                    creditedInterest = Double.valueOf(df.format(totalInterest));
                    finalBalance = Double.valueOf(activatedBankAccount.getBankAccountBalance()) + totalInterest;

                    String accountCredit = " ";
                    String transactionCode = "DEFI";
                    String transactionRef = "Interest Crediting";

                    Calendar cal = Calendar.getInstance();
                    int year = cal.get(Calendar.YEAR);
                    int month = cal.get(Calendar.MONTH);
                    int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
                    String transactionDate = dayOfMonth + "-" + (month + 1) + "-" + year;

                    interest.setMonthlyInterest(totalInterest.toString());
                    activatedBankAccount.setBankAccountBalance(df.format(finalBalance));

                    Long newAccTransactionId = transactionSessionLocal.addNewTransaction(transactionDate, transactionCode, transactionRef,
                            creditedInterest.toString(), accountCredit, activatedBankAccount.getBankAccountId());
                } else {
                    interest.setDailyInterest("0");
                    interest.setIsTransfer("0");
                    interest.setIsWithdraw("0");
                }
            }
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

        bankAccountNum = new BigInteger(23, random).setBit(22).toString(10);
        status = checkAccountDuplication(bankAccountNum);

        while (status.equals("Duplicated")) {
            bankAccountNum = new BigInteger(23, random).setBit(22).toString(10);
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

    @Override
    public void updateDepositPeriod(String bankAccountNum, String fixedDepositPeriod) {

        BankAccount bankAccount = retrieveBankAccountByNum(bankAccountNum);

        String[] depositPeriods = fixedDepositPeriod.split(" ");
        String depositPeriod = depositPeriods[0];
        Double depositPeriodDay = Double.valueOf(depositPeriod) * 30;

        if (bankAccount.getFixedDepositStatus().equals("Withdrawn")) {
            bankAccount.setBankAccountStatus("Activated");
        }
        bankAccount.setBankAccountDepositPeriod(depositPeriodDay.toString());
        bankAccount.setFixedDepositStatus("Deposited");
        bankAccount.setCurrentFixedDepositPeriod("0");
        bankAccount.getInterest().setIsTransfer("0");
        bankAccount.getInterest().setIsWithdraw("0");
        bankAccount.getInterest().setDailyInterest("0");
        bankAccount.getInterest().setMonthlyInterest("0");
    }

    private String md5Hashing(String stringToHash) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        return Arrays.toString(md.digest(stringToHash.getBytes()));
    }

    private String checkInterestRate(String fixedDepositPeriodDay) {

        Double interestRate = 0.0;
        Double fixedDepositPeriodDayDouble = Double.valueOf(fixedDepositPeriodDay);

        if (fixedDepositPeriodDayDouble >= 30 && fixedDepositPeriodDayDouble <= 60) {
            interestRate = 0.0005;
        } else if (fixedDepositPeriodDayDouble >= 61 && fixedDepositPeriodDayDouble <= 150) {
            interestRate = 0.001;
        } else if (fixedDepositPeriodDayDouble >= 151 && fixedDepositPeriodDayDouble <= 240) {
            interestRate = 0.0015;
        } else if (fixedDepositPeriodDayDouble >= 241 && fixedDepositPeriodDayDouble <= 330) {
            interestRate = 0.002;
        } else if (fixedDepositPeriodDayDouble >= 331 && fixedDepositPeriodDayDouble <= 360) {
            interestRate = 0.0025;
        } else if (fixedDepositPeriodDayDouble >= 361 && fixedDepositPeriodDayDouble <= 540) {
            interestRate = 0.005;
        } else if (fixedDepositPeriodDayDouble >= 541 && fixedDepositPeriodDayDouble <= 720) {
            interestRate = 0.0055;
        } else {
            interestRate = 0.0065;
        }

        return interestRate.toString();
    }
}
