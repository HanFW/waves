package ejb.deposit.session;

import ejb.deposit.entity.BankAccount;
import ejb.deposit.entity.AccTransaction;
import ejb.customer.entity.CustomerBasic;
import ejb.customer.session.CRMCustomerSessionBeanLocal;
import ejb.deposit.entity.Interest;
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
import javax.ejb.EJB;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NonUniqueResultException;

@Stateless

public class BankAccountSessionBean implements BankAccountSessionBeanLocal {

    @EJB
    private TransactionSessionBeanLocal transactionSessionBeanLocal;

    @EJB
    private StatementSessionBeanLocal statementSessionBeanLocal;

    @EJB
    private CRMCustomerSessionBeanLocal customerSessionBeanLocal;

    @EJB
    private InterestSessionBeanLocal interestSessionLocal;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public BankAccount retrieveBankAccountById(Long bankAccountId) {
        System.out.println("*");
        System.out.println("****** deposit/BankAccountSessionBean: retrieveBankAccountById() ******");
        BankAccount bankAccount = new BankAccount();

        try {
            Query query = entityManager.createQuery("Select a From BankAccount a Where a.bankAccountId=:bankAccountId");
            query.setParameter("bankAccountId", bankAccountId);

            if (query.getResultList().isEmpty()) {
                System.out.println("****** deposit/BankAccountSessionBean: retrieveBankAccountById(): invalid bank account Id: no result found, return new bank account");
                return new BankAccount();
            } else {
                System.out.println("****** deposit/BankAccountSessionBean: retrieveBankAccountById(): valid bank account Id: return bank account");
                bankAccount = (BankAccount) query.getSingleResult();
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("****** deposit/BankAccountSessionBean: retrieveBankAccountById(): Entity not found error: " + enfe.getMessage());
            return new BankAccount();
        } catch (NonUniqueResultException nure) {
            System.out.println("****** deposit/BankAccountSessionBean: retrieveBankAccountById(): Non unique result error: " + nure.getMessage());
        }

        return bankAccount;
    }

    @Override
    public BankAccount retrieveBankAccountByNum(String bankAccountNum) {
        System.out.println("*");
        System.out.println("****** deposit/BankAccountSessionBean: retrieveBankAccountByNum() ******");
        BankAccount bankAccount = new BankAccount();

        try {
            Query query = entityManager.createQuery("Select a From BankAccount a Where a.bankAccountNum=:bankAccountNum");
            query.setParameter("bankAccountNum", bankAccountNum);

            if (query.getResultList().isEmpty()) {
                System.out.println("****** deposit/BankAccountSessionBean: retrieveBankAccountByNum(): invalid bank account number: no result found, return new bank account");
                return new BankAccount();
            } else {
                System.out.println("****** deposit/BankAccountSessionBean: retrieveBankAccountByNum(): valid bank account Number: return bank account");
                bankAccount = (BankAccount) query.getSingleResult();
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("****** deposit/BankAccountSessionBean: retrieveBankAccountByNum(): Entity not found error: " + enfe.getMessage());
            return new BankAccount();
        } catch (NonUniqueResultException nure) {
            System.out.println("****** deposit/BankAccountSessionBean: retrieveBankAccountByNum(): Non unique result error: " + nure.getMessage());
        }

        return bankAccount;
    }

    @Override
    public List<BankAccount> retrieveBankAccountByCusIC(String customerIdentificationNum) {
        System.out.println("*");
        System.out.println("****** deposit/BankAccountSessionBean: retrieveBankAccountByCusIC() ******");
        CustomerBasic customerBasic = customerSessionBeanLocal.retrieveCustomerBasicByIC(customerIdentificationNum.toUpperCase());

        if (customerBasic.getCustomerBasicId() == null) {
            System.out.println("****** deposit/BankAccountSessionBean: retrieveBankAccountByCusIC(): invalid customer identification number: no result found, return new bank account");
            return new ArrayList<BankAccount>();
        }
        try {
            Query query = entityManager.createQuery("Select a From BankAccount a Where a.customerBasic=:customerBasic");
            query.setParameter("customerBasic", customerBasic);
            System.out.println("****** deposit/BankAccountSessionBean: retrieveBankAccountByCusIC(): valid customer identification number: return bank account");
            return query.getResultList();
        } catch (EntityNotFoundException enfe) {
            System.out.println("****** deposit/BankAccountSessionBean: retrieveBankAccountByCusIC(): Entity not found error: " + enfe.getMessage());
            return new ArrayList<BankAccount>();
        }
    }

    @Override
    public CustomerBasic retrieveCustomerBasicById(Long customerBasicId) {
        System.out.println("*");
        System.out.println("****** deposit/BankAccountSessionBean: retrieveCustomerBasicById() ******");
        CustomerBasic customerBasic = new CustomerBasic();

        try {
            Query query = entityManager.createQuery("Select c From CustomerBasic c Where c.customerBasicId=:customerBasicId");
            query.setParameter("customerBasicId", customerBasicId);

            if (query.getResultList().isEmpty()) {
                System.out.println("****** deposit/BankAccountSessionBean: retrieveCustomerBasicById(): invalid bank account Id: no result found, return new bank account");
                return new CustomerBasic();
            } else {
                System.out.println("****** deposit/BankAccountSessionBean: retrieveCustomerBasicById(): valid bank account Id: return bank account");
                customerBasic = (CustomerBasic) query.getSingleResult();
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("****** deposit/BankAccountSessionBean: retrieveCustomerBasicById(): Entity not found error: " + enfe.getMessage());
            return new CustomerBasic();
        } catch (NonUniqueResultException nure) {
            System.out.println("****** deposit/BankAccountSessionBean: retrieveCustomerBasicById(): Non unique result error: " + nure.getMessage());
        }

        return customerBasic;
    }

    @Override
    public CustomerBasic retrieveCustomerBasicByIC(String customerIdentificationNum) {
        System.out.println("*");
        System.out.println("****** deposit/BankAccountSessionBean: retrieveCustomerBasicByIC() ******");
        CustomerBasic customerBasic = new CustomerBasic();

        try {
            Query query = entityManager.createQuery("Select c From CustomerBasic c Where c.customerIdentificationNum=:customerIdentificationNum");
            query.setParameter("customerIdentificationNum", customerIdentificationNum.toUpperCase());

            if (query.getResultList().isEmpty()) {
                System.out.println("****** deposit/BankAccountSessionBean: retrieveCustomerBasicByIC(): invalid customer identification number: no result found, return new customer");
                return new CustomerBasic();
            } else {
                System.out.println("****** deposit/BankAccountSessionBean: retrieveCustomerBasicByIC(): valid customer identification number: return customer");
                customerBasic = (CustomerBasic) query.getSingleResult();
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("****** deposit/BankAccountSessionBean: retrieveCustomerBasicByIC(): Entity not found error: " + enfe.getMessage());
            return new CustomerBasic();
        } catch (NonUniqueResultException nure) {
            System.out.println("****** deposit/BankAccountSessionBean: retrieveCustomerBasicByIC(): Non unique result error: " + nure.getMessage());
        }

        return customerBasic;
    }

    @Override
    public AccTransaction retrieveAccTransactionById(Long transactionId) {
        System.out.println("*");
        System.out.println("****** deposit/BankAccountSessionBean: retrieveAccTransactionById() ******");
        AccTransaction accTransaction = new AccTransaction();

        try {
            Query query = entityManager.createQuery("Select t From AccTransaction t Where t.transactionId=:transactionId");
            query.setParameter("transactionId", transactionId);

            if (query.getResultList().isEmpty()) {
                System.out.println("****** deposit/BankAccountSessionBean: retrieveAccTransactionById(): invalid transaction Id: no result found, return new transaction");
            } else {
                System.out.println("****** deposit/BankAccountSessionBean: retrieveAccTransactionById(): valid transaction Id: return transaction");
                accTransaction = (AccTransaction) query.getSingleResult();
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("****** deposit/BankAccountSessionBean: retrieveAccTransactionById(): Entity not found error: " + enfe.getMessage());
            return new AccTransaction();
        } catch (NonUniqueResultException nure) {
            System.out.println("****** deposit/BankAccountSessionBean: retrieveAccTransactionById(): Non unique result error: " + nure.getMessage());
        }

        return accTransaction;
    }

    @Override
    public Long addNewAccount(String bankAccountNum,
            String bankAccountType, String totalBankAccountBalance, String availableBankAccountBalance,
            String transferDailyLimit, String transferBalance, String bankAccountStatus, String bankAccountMinSaving,
            String bankAccountDepositPeriod, String currentFixedDepositPeriod,
            String fixedDepositStatus, Double statementDateDouble,
            Long customerBasicId, Long interestId) {

        System.out.println("*");
        System.out.println("****** deposit/BankAccountSessionBean: addNewAccount() ******");
        BankAccount bankAccount = new BankAccount();

        bankAccount.setBankAccountNum(bankAccountNum);
        bankAccount.setBankAccountType(bankAccountType);
        bankAccount.setTotalBankAccountBalance(totalBankAccountBalance);
        bankAccount.setAvailableBankAccountBalance(availableBankAccountBalance);
        bankAccount.setTransferDailyLimit(transferDailyLimit);
        bankAccount.setTransferBalance(transferBalance);
        bankAccount.setBankAccountStatus(bankAccountStatus);
        bankAccount.setBankAccountMinSaving(bankAccountMinSaving);
        bankAccount.setBankAccountDepositPeriod(bankAccountDepositPeriod);
        bankAccount.setCurrentFixedDepositPeriod(currentFixedDepositPeriod);
        bankAccount.setFixedDepositStatus(fixedDepositStatus);
        bankAccount.setStatementDateDouble(statementDateDouble);
        bankAccount.setCustomerBasic(retrieveCustomerBasicById(customerBasicId));
        bankAccount.setInterest(interestSessionLocal.retrieveInterestById(interestId));

        Interest interest = interestSessionLocal.retrieveInterestById(interestId);
        interest.setBankAccount(bankAccount);

        entityManager.persist(bankAccount);
        entityManager.persist(interest);
        entityManager.flush();

        return bankAccount.getBankAccountId();
    }

    @Override
    public String deleteAccount(String bankAccountNum) {
        System.out.println("*");
        System.out.println("****** deposit/BankAccountSessionBean: deleteAccount() ******");

        BankAccount bankAccount = retrieveBankAccountByNum(bankAccountNum);

        if (!bankAccount.getStatement().isEmpty()) {
            for (int j = bankAccount.getStatement().size() - 1; j > 0; j--) {
                statementSessionBeanLocal.deleteStatement(bankAccount.getStatement().get(j).getStatementId());
            }
        }

        if (!bankAccount.getAccTransaction().isEmpty()) {
            for (int i = bankAccount.getAccTransaction().size() - 1; i > 0; i--) {
                transactionSessionBeanLocal.deleteAccTransaction(bankAccount.getAccTransaction().get(i).getTransactionId());
            }
        }

        entityManager.remove(bankAccount);
        entityManager.flush();

        return "Successfully deleted!";
    }

    @Override
    public void interestAccuring() {
        DecimalFormat df = new DecimalFormat("#.00");

        Query query = entityManager.createQuery("SELECT a FROM BankAccount a WHERE a.bankAccountStatus = :bankAccountStatus");
        query.setParameter("bankAccountStatus", "Active");
        List<BankAccount> activatedBankAccounts = query.getResultList();

        for (BankAccount activatedBankAccount : activatedBankAccounts) {

            Double currentInterest = 0.0;
            Double currentTotalBalance = 0.0;
            Double currentAvailableBalance = 0.0;
            Double totalInterest = 0.0;
            Double accuredInterest = 0.0;
            Double finalTotalBalance = 0.0;
            Double finalAvailableBalance = 0.0;
            Double finalInterest = 0.0;
            Interest interest = activatedBankAccount.getInterest();

            currentAvailableBalance = Double.valueOf(activatedBankAccount.getAvailableBankAccountBalance());
            currentTotalBalance = Double.valueOf(activatedBankAccount.getTotalBankAccountBalance());

            if (activatedBankAccount.getFixedDepositStatus().equals("Deposited") && activatedBankAccount.getBankAccountType().equals("Fixed Deposit Account")
                    && currentAvailableBalance > 0) {

                if (activatedBankAccount.getInterest().getIsTransfer().equals("1") || activatedBankAccount.getInterest().getIsWithdraw().equals("1")) {

                    interest.setIsTransfer("0");
                    interest.setIsWithdraw("0");

                    currentInterest = Double.valueOf(activatedBankAccount.getInterest().getDailyInterest());
                    interest.setDailyInterest("0");
                    activatedBankAccount.setFixedDepositStatus("Withdrawn");
                    activatedBankAccount.setBankAccountDepositPeriod("None");

                    finalInterest = currentInterest * 0.4;

                    finalAvailableBalance = currentAvailableBalance + finalInterest;
                    finalTotalBalance = currentTotalBalance + finalInterest;

                    activatedBankAccount.setAvailableBankAccountBalance(df.format(finalAvailableBalance));
                    activatedBankAccount.setTotalBankAccountBalance(df.format(finalTotalBalance));

                    String accountDebit = " ";
                    String transactionCode = "DEFI";
                    String transactionRef = "Interest Crediting";

                    Calendar cal = Calendar.getInstance();

                    Long transactionDateMilis = cal.getTimeInMillis();

                    Long newAccTransactionId = transactionSessionBeanLocal.addNewTransaction(cal.getTime().toString(), transactionCode, transactionRef,
                            accountDebit, finalInterest.toString(), transactionDateMilis, activatedBankAccount.getBankAccountId());

                } else {

                    Double currentPeriod = Double.valueOf(activatedBankAccount.getCurrentFixedDepositPeriod());
                    Double finalPeriod = currentPeriod + 1;
                    activatedBankAccount.setCurrentFixedDepositPeriod(finalPeriod.toString());

                    currentInterest = Double.valueOf(activatedBankAccount.getInterest().getDailyInterest());
                    currentAvailableBalance = Double.valueOf(activatedBankAccount.getAvailableBankAccountBalance());
                    currentTotalBalance = Double.valueOf(activatedBankAccount.getTotalBankAccountBalance());

                    String interestRate = checkInterestRate(activatedBankAccount.getBankAccountDepositPeriod());

                    totalInterest = currentInterest + currentAvailableBalance * Double.valueOf(interestRate) / 360;
                    accuredInterest = Double.valueOf(df.format(totalInterest));

                    if (activatedBankAccount.getCurrentFixedDepositPeriod().equals(activatedBankAccount.getBankAccountDepositPeriod())) {

                        interest.setDailyInterest(totalInterest.toString());
                        finalAvailableBalance = currentAvailableBalance + totalInterest;
                        finalTotalBalance = currentTotalBalance + totalInterest;

                        interest.setDailyInterest("0");

                        activatedBankAccount.setAvailableBankAccountBalance(df.format(finalAvailableBalance));
                        activatedBankAccount.setTotalBankAccountBalance(df.format(finalTotalBalance));

                        activatedBankAccount.setFixedDepositStatus("Withdrawn");
                        activatedBankAccount.setBankAccountDepositPeriod("None");
                        activatedBankAccount.setBankAccountStatus("Inactive");

                        String accountDebit = " ";
                        String transactionCode = "DEFI";
                        String transactionRef = "Interest Crediting";

                        Calendar cal = Calendar.getInstance();

                        Long transactionDateMilis = cal.getTimeInMillis();

                        Long newAccTransactionId = transactionSessionBeanLocal.addNewTransaction(cal.getTime().toString(), transactionCode, transactionRef,
                                accountDebit, accuredInterest.toString(), transactionDateMilis, activatedBankAccount.getBankAccountId());
                    } else {
                        interest.setDailyInterest(totalInterest.toString());
                    }
                }
            } else if (activatedBankAccount.getFixedDepositStatus().equals("Withdrawn") && activatedBankAccount.getBankAccountType().equals("Fixed Deposit Account")
                    && currentAvailableBalance > 0) {

                currentInterest = Double.valueOf(activatedBankAccount.getInterest().getDailyInterest());
                currentAvailableBalance = Double.valueOf(activatedBankAccount.getAvailableBankAccountBalance());

                totalInterest = currentInterest + currentAvailableBalance * 0.0005 / 360;

                interest.setDailyInterest(totalInterest.toString());

            } else if (currentAvailableBalance > 0) {

                currentInterest = Double.valueOf(interest.getDailyInterest());
                currentAvailableBalance = Double.valueOf(activatedBankAccount.getAvailableBankAccountBalance());

                totalInterest = currentInterest + currentAvailableBalance * 0.0005 / 360;

                interest.setDailyInterest(totalInterest.toString());
            }
        }
    }

    @Override
    public void interestCrediting() {
        System.out.println("*");
        System.out.println("****** deposit/BankAccountSessionBean: interestCrediting() ******");
        DecimalFormat df = new DecimalFormat("#.00");

        Query query = entityManager.createQuery("SELECT a FROM BankAccount a WHERE a.bankAccountStatus = :bankAccountStatus");
        query.setParameter("bankAccountStatus", "Active");
        List<BankAccount> activatedBankAccounts = query.getResultList();

        for (BankAccount activatedBankAccount : activatedBankAccounts) {

            Interest interest = activatedBankAccount.getInterest();
            Double dailyInterest = Double.valueOf(interest.getDailyInterest());
            Double bonusInterest = 0.0;
            Double totalInterest = 0.0;
            Double creditedInterest = 0.0;
            Double finalAvailableBalance = 0.0;
            Double finalTotalBalance = 0.0;
            Double currentTotalBalance = 0.0;
            Double currentAvailableBalance = 0.0;

            currentAvailableBalance = Double.valueOf(activatedBankAccount.getAvailableBankAccountBalance());
            currentTotalBalance = Double.valueOf(activatedBankAccount.getTotalBankAccountBalance());

            if (activatedBankAccount.getBankAccountType().equals("Fixed Deposit Account") && !activatedBankAccount.getFixedDepositStatus().equals("Withdrawn")) {
                System.out.println("One Month Gone");
            } else if (activatedBankAccount.getBankAccountType().equals("Fixed Deposit Account") && activatedBankAccount.getFixedDepositStatus().equals("Withdrawn")
                    && currentAvailableBalance > 0) {

                bonusInterest = 0.0;
                totalInterest = dailyInterest + bonusInterest;
                creditedInterest = Double.valueOf(df.format(totalInterest));
                finalAvailableBalance = Double.valueOf(activatedBankAccount.getAvailableBankAccountBalance()) + totalInterest;
                finalTotalBalance = Double.valueOf(activatedBankAccount.getTotalBankAccountBalance()) + totalInterest;

                interest.setMonthlyInterest(totalInterest.toString());

                activatedBankAccount.setAvailableBankAccountBalance(df.format(finalAvailableBalance));
                activatedBankAccount.setTotalBankAccountBalance(df.format(finalTotalBalance));

                String accountDebit = " ";
                String transactionCode = "DEFI";
                String transactionRef = "Interest Crediting";

                Calendar cal = Calendar.getInstance();

                Long transactionDateMilis = cal.getTimeInMillis();

                Long newAccTransactionId = transactionSessionBeanLocal.addNewTransaction(cal.getTime().toString(), transactionCode, transactionRef,
                        accountDebit, creditedInterest.toString(), transactionDateMilis, activatedBankAccount.getBankAccountId());

            } else if (currentAvailableBalance > 0) {

                if ((interest.getIsTransfer().equals("0")) && (interest.getIsWithdraw().equals("0"))) {
                    interest.setDailyInterest("0");

                    if (activatedBankAccount.getBankAccountType().equals("Monthly Savings Account")) {

                        if (activatedBankAccount.getBankAccountMinSaving().equals("Sufficient")) {
                            bonusInterest = Double.valueOf(activatedBankAccount.getAvailableBankAccountBalance()) * 0.0035 / 12;
                            activatedBankAccount.setBankAccountMinSaving("Insufficient");
                        } else if (activatedBankAccount.getBankAccountMinSaving().equals("Insufficient")) {
                            bonusInterest = 0.0;
                        }
                    } else if (activatedBankAccount.getBankAccountType().equals("Bonus Savings Account")) {
                        bonusInterest = Double.valueOf(activatedBankAccount.getAvailableBankAccountBalance()) * 0.0075 / 12;
                    } else {
                        bonusInterest = 0.0;
                    }

                    totalInterest = dailyInterest + bonusInterest;
                    creditedInterest = Double.valueOf(df.format(totalInterest));

                    finalAvailableBalance = Double.valueOf(activatedBankAccount.getAvailableBankAccountBalance()) + totalInterest;
                    finalTotalBalance = Double.valueOf(activatedBankAccount.getTotalBankAccountBalance()) + totalInterest;

                    String accountDebit = " ";
                    String transactionCode = "DEFI";
                    String transactionRef = "Interest Crediting";

                    Calendar cal = Calendar.getInstance();

                    Long transactionDateMilis = cal.getTimeInMillis();

                    interest.setMonthlyInterest(totalInterest.toString());
                    activatedBankAccount.setAvailableBankAccountBalance(df.format(finalAvailableBalance));
                    activatedBankAccount.setAvailableBankAccountBalance(df.format(finalTotalBalance));

                    Long newAccTransactionId = transactionSessionBeanLocal.addNewTransaction(cal.getTime().toString(), transactionCode, transactionRef,
                            accountDebit, creditedInterest.toString(), transactionDateMilis, activatedBankAccount.getBankAccountId());
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
        System.out.println("*");
        System.out.println("****** deposit/BankAccountSessionBean: checkAccountDuplication() ******");
        BankAccount bankAccount = retrieveBankAccountByNum(bankAccountNum);

        if (bankAccount.getBankAccountId() != null) {
            System.out.println("****** deposit/BankAccountSessionBean: checkAccountDuplication(): account number duplicate");
            return "Duplicated";
        } else {
            System.out.println("****** deposit/BankAccountSessionBean: checkAccountDuplication(): success");
            return "Success";
        }
    }

    @Override
    public String generateBankAccount() {
        System.out.println("*");
        System.out.println("****** deposit/BankAccountSessionBean: generateBankAccount() ******");
        String bankAccountNum;
        String status;
        SecureRandom random = new SecureRandom();

        bankAccountNum = new BigInteger(23, random).setBit(22).toString(10);
        status = checkAccountDuplication(bankAccountNum);
        System.out.println("****** deposit/BankAccountSessionBean: generateBankAccount(): generate a bank account number");

        while (status.equals("Duplicated")) {
            bankAccountNum = new BigInteger(23, random).setBit(22).toString(10);
            status = checkAccountDuplication(bankAccountNum);
            System.out.println("****** deposit/BankAccountSessionBean: generateBankAccount(): bank account number duplicate: generate a new bank account number");
        }

        return bankAccountNum;
    }

    @Override
    public boolean checkExistence(String customerIdentificationNum) {
        System.out.println("*");
        System.out.println("****** deposit/BankAccountSessionBean: checkExistence() ******");
        List<BankAccount> bankAccount = retrieveBankAccountByCusIC(customerIdentificationNum.toUpperCase());

        if (bankAccount.isEmpty()) {
            System.out.println("****** deposit/BankAccountSessionBean: checkExistence(): customer identification number invalid: retun false");
            return false;
        }

        System.out.println("****** deposit/BankAccountSessionBean: checkExistence(): customer identification number valid: retun true");
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
        System.out.println("*");
        System.out.println("****** deposit/BankAccountSessionBean: updateDepositPeriod() ******");

        BankAccount bankAccount = retrieveBankAccountByNum(bankAccountNum);

        String[] depositPeriods = fixedDepositPeriod.split(" ");
        String depositPeriod = depositPeriods[0];
        Double depositPeriodDay = Double.valueOf(depositPeriod) * 30;

        if (bankAccount.getFixedDepositStatus().equals("Withdrawn")) {
            bankAccount.setBankAccountStatus("Active");
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

    @Override
    public boolean checkOnlyOneAccount(String customerIdentificationNum) {
        System.out.println("*");
        System.out.println("****** deposit/BankAccountSessionBean: checkOnlyOneAccount() ******");
        List<BankAccount> bankAccount = retrieveBankAccountByCusIC(customerIdentificationNum.toUpperCase());

        if (bankAccount.size() > 1) {
            System.out.println("****** deposit/BankAccountSessionBean: checkOnlyOneAccount(): more than one bank account: return false");
            return false;
        }

        System.out.println("****** deposit/BankAccountSessionBean: checkOnlyOneAccount(): only one bank account: return true");
        return true;
    }

    @Override
    public CustomerBasic retrieveCustomerBasicByAccNum(String bankAccountNum) {
        System.out.println("*");
        System.out.println("****** deposit/BankAccountSessionBean: retrieveCustomerBasicByAccNum() ******");
        CustomerBasic customerBasic = new CustomerBasic();
        BankAccount bankAccount = retrieveBankAccountByNum(bankAccountNum);

        try {
            Query query = entityManager.createQuery("Select c From CustomerBasic c Where c.bankAccount=:bankAccount");
            query.setParameter("bankAccount", bankAccount);

            if (query.getResultList().isEmpty()) {
                System.out.println("****** deposit/BankAccountSessionBean: retrieveCustomerBasicByAccNum(): invalid bank account number: no result found, return new customer");
                return new CustomerBasic();
            } else {
                System.out.println("****** deposit/BankAccountSessionBean: retrieveCustomerBasicByAccNum(): valid bank account number: return customer");
                customerBasic = (CustomerBasic) query.getSingleResult();
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("****** deposit/BankAccountSessionBean: retrieveCustomerBasicByAccNum(): Entity not found error: " + enfe.getMessage());
            return new CustomerBasic();
        } catch (NonUniqueResultException nure) {
            System.out.println("****** deposit/BankAccountSessionBean: retrieveCustomerBasicByAccNum(): Non unique result error: " + nure.getMessage());
        }

        return customerBasic;
    }

    @Override
    public void resetDailyTransferLimit() {

        Query query = entityManager.createQuery("SELECT a FROM BankAccount a WHERE a.bankAccountStatus = :bankAccountStatus");
        query.setParameter("bankAccountStatus", "Active");
        List<BankAccount> activatedBankAccounts = query.getResultList();

        for (BankAccount activatedBankAccount : activatedBankAccounts) {
            activatedBankAccount.setTransferBalance(activatedBankAccount.getTransferDailyLimit());
        }
    }

    @Override
    public void updateDailyTransferLimit(String bankAccountNum, String dailyTransferLimit) {
        System.out.println("*");
        System.out.println("****** deposit/BankAccountSessionBean: updateDailyTransferLimit() ******");
        BankAccount bankAccount = retrieveBankAccountByNum(bankAccountNum);
        bankAccount.setTransferDailyLimit(dailyTransferLimit);
    }

    @Override
    public void autoCloseAccount() {
        System.out.println("*");
        System.out.println("****** deposit/BankAccountSessionBean: autoCloseAccount() ******");

        Query query = entityManager.createQuery("SELECT a FROM BankAccount a WHERE a.bankAccountStatus = :bankAccountStatus");
        query.setParameter("bankAccountStatus", "Inactive");
        List<BankAccount> inActivatedBankAccounts = query.getResultList();

        for (BankAccount inActivatedBankAccount : inActivatedBankAccounts) {
            CustomerBasic customerBasic = retrieveCustomerBasicByAccNum(inActivatedBankAccount.getBankAccountNum());

            if (customerBasic.getBankAccount().size() > 1) {
                deleteAccount(inActivatedBankAccount.getBankAccountNum());
            } else if (customerBasic.getBankAccount().size() == 1) {
                deleteAccount(inActivatedBankAccount.getBankAccountNum());
                customerSessionBeanLocal.deleteCustomerBasic(customerBasic.getCustomerIdentificationNum());
            }
        }
    }

    @Override
    public Long addNewAccountOneTime(String bankAccountNum, String bankAccountType,
            String totalBankAccountBalance, String availableBankAccountBalance, String transferDailyLimit,
            String transferBalance, String bankAccountStatus, String bankAccountMinSaving,
            String bankAccountDepositPeriod, String currentFixedDepositPeriod,
            String fixedDepositStatus, Double statementDateDouble,
            Long customerBasicId, Long interestId) {

        BankAccount bankAccount = new BankAccount();

        bankAccount.setBankAccountNum(bankAccountNum);
        bankAccount.setBankAccountType(bankAccountType);
        bankAccount.setTotalBankAccountBalance(totalBankAccountBalance);
        bankAccount.setAvailableBankAccountBalance(availableBankAccountBalance);
        bankAccount.setTransferDailyLimit(transferDailyLimit);
        bankAccount.setTransferBalance(transferBalance);
        bankAccount.setBankAccountStatus(bankAccountStatus);
        bankAccount.setBankAccountMinSaving(bankAccountMinSaving);
        bankAccount.setBankAccountDepositPeriod(bankAccountDepositPeriod);
        bankAccount.setCurrentFixedDepositPeriod(currentFixedDepositPeriod);
        bankAccount.setFixedDepositStatus(fixedDepositStatus);
        bankAccount.setStatementDateDouble(statementDateDouble);
        bankAccount.setCustomerBasic(retrieveCustomerBasicById(customerBasicId));
        bankAccount.setInterest(interestSessionLocal.retrieveInterestById(interestId));

        Interest interest = interestSessionLocal.retrieveInterestById(interestId);
        interest.setBankAccount(bankAccount);

        entityManager.persist(bankAccount);
        entityManager.persist(interest);
        entityManager.flush();

        return bankAccount.getBankAccountId();
    }

    @Override
    public void updateBankAccountAvailableBalance(String bankAccountNum, String availableBankAccountBalance) {

        DecimalFormat df = new DecimalFormat("#.00");

        BankAccount bankAccount = retrieveBankAccountByNum(bankAccountNum);
        bankAccount.setAvailableBankAccountBalance(availableBankAccountBalance);
    }

    @Override
    public void updateBankAccountBalance(String bankAccountNum, String availableBankAccountBalance,
            String totalBankAccountBalance) {

        DecimalFormat df = new DecimalFormat("#.00");

        BankAccount bankAccount = retrieveBankAccountByNum(bankAccountNum);

        bankAccount.setAvailableBankAccountBalance(availableBankAccountBalance);
        bankAccount.setTotalBankAccountBalance(totalBankAccountBalance);
    }

    @Override
    public void updateDepositAccountAvailableBalance(String bankAccountNum, double transactionAmt){
        System.out.println("!!!!!!!updateDepositAccountAvailableBalance");
        BankAccount account=retrieveBankAccountByNum(bankAccountNum);
        Double availableBalance;
        availableBalance = Double.valueOf(account.getAvailableBankAccountBalance());
        String newAvailableBalance = String.valueOf(availableBalance-transactionAmt);
        account.setAvailableBankAccountBalance(newAvailableBalance);
        entityManager.flush();
    }

    @Override
    public void updateDepositAccountTotalBalance(String bankAccountNum, double transactionAmt){
         BankAccount account=retrieveBankAccountByNum(bankAccountNum);
        Double totalBalance;
        totalBalance = Double.valueOf(account.getTotalBankAccountBalance());
        String newTotalBalance = String.valueOf(totalBalance-transactionAmt);
        account.setTotalBankAccountBalance(newTotalBalance);
        entityManager.flush();
               
    }
}
