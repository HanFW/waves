package ejb.bi.session;

import ejb.bi.entity.CustomerRFM;
import ejb.bi.entity.Rate;
import ejb.customer.entity.CustomerBasic;
import ejb.deposit.entity.AccTransaction;
import ejb.deposit.entity.BankAccount;
import ejb.deposit.session.BankAccountSessionBeanLocal;
import ejb.loan.entity.LoanApplication;
import ejb.loan.entity.LoanPayableAccount;
import ejb.loan.entity.LoanRepaymentTransaction;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class CustomerRFMSessionBean implements CustomerRFMSessionBeanLocal {

    @EJB
    private RateSessionBeanLocal rateSessionBeanLocal;

    @EJB
    private BankAccountSessionBeanLocal bankAccountSessionBeanLocal;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long addNewCustomerRFM(String customerName, String rValue, String fValue, String mValue,
            Integer updateMonth, Integer updateYear, Long startTimeMilis, Long transactionDays,
            Integer numOfTransactions, Double totalSpends, String totalRFMValue,
            String RFMType, Long customerBasicId) {

        CustomerRFM customerRFM = new CustomerRFM();

        customerRFM.setCustomerName(customerName);
        customerRFM.setUpdateMonth(updateMonth);
        customerRFM.setUpdateYear(updateYear);
        customerRFM.setfValue(fValue);
        customerRFM.setmValue(mValue);
        customerRFM.setrValue(rValue);
        customerRFM.setStartTimeMilis(startTimeMilis);
        customerRFM.setNumOfTransactions(numOfTransactions);
        customerRFM.setTransactionDays(transactionDays);
        customerRFM.setTotalSpends(totalSpends);
        customerRFM.setTotalRFMValue(totalRFMValue);
        customerRFM.setRFMType(RFMType);
        customerRFM.setCustomerBasic(bankAccountSessionBeanLocal.retrieveCustomerBasicById(customerBasicId));

        entityManager.persist(customerRFM);
        entityManager.flush();

        return customerRFM.getCustomerRFMId();
    }

    @Override
    public void generateMonthlyCustomerRFM() {

        Query query = entityManager.createQuery("SELECT c FROM CustomerBasic c");
        List<CustomerBasic> allCustomerBasic = query.getResultList();

        Calendar cal = Calendar.getInstance();
        Long endTime = cal.getTimeInMillis();
        Long startTime = cal.getTimeInMillis() - 300000;

        for (CustomerBasic customerBasic : allCustomerBasic) {

            List<BankAccount> bankAccounts = customerBasic.getBankAccount();
            Long maxTransactionDateMilis = Long.valueOf(0);
            Double totalSpends = Double.valueOf(0);
            List<AccTransaction> customerTransaction = new ArrayList();

            for (int i = 0; i < bankAccounts.size(); i++) {

                Query transactionQuery = entityManager.createQuery("SELECT a FROM AccTransaction a WHERE a.transactionDateMilis >= :startTime And a.transactionDateMilis<=:endTime And a.bankAccount=:bankAccount");
                transactionQuery.setParameter("startTime", startTime);
                transactionQuery.setParameter("endTime", endTime);
                transactionQuery.setParameter("bankAccount", bankAccounts.get(i));

                customerTransaction = transactionQuery.getResultList();
                for (int j = 0; j < customerTransaction.size(); j++) {
                    if (customerTransaction.get(j).getTransactionDateMilis() > maxTransactionDateMilis) {
                        maxTransactionDateMilis = customerTransaction.get(j).getTransactionDateMilis();
                    }
                    if (!customerTransaction.get(j).getAccountDebit().equals(" ")) {
                        totalSpends = totalSpends + Double.valueOf(customerTransaction.get(j).getAccountDebit());
                    }
                }
            }

            Long diff = Long.valueOf(0);
            Long transactionDays = Long.valueOf(0);
            if (customerTransaction.isEmpty()) {
                transactionDays = Long.valueOf(0);
            } else {
                diff = maxTransactionDateMilis - startTime;
                transactionDays = diff / 10000;
            }

            Integer recencyLevel = checkRecencyLevel(transactionDays);
            Integer frequencyLevel = checkFrequencyLevel(customerTransaction.size());
            Integer monetaryLevel = checkMonetaryLevel(totalSpends);

            Rate acqRate = rateSessionBeanLocal.getAcqRate();
            Integer updateMonth = acqRate.getUpdateMonth();
            Integer updateYear = acqRate.getUpdateYear();

            Integer totalRFMValue = recencyLevel + frequencyLevel + monetaryLevel;
            String RFMType = "Deposit Transaction";

            Long newCustomerRFMId = addNewCustomerRFM(customerBasic.getCustomerName(), recencyLevel.toString(),
                    frequencyLevel.toString(), monetaryLevel.toString(), updateMonth, updateYear,
                    startTime, transactionDays, customerTransaction.size(), totalSpends,
                    totalRFMValue.toString(), RFMType, customerBasic.getCustomerBasicId());
        }

    }

    @Override
    public void generateLoanMonthlyRFM() {

        Query query = entityManager.createQuery("SELECT c FROM CustomerBasic c");
        List<CustomerBasic> allCustomerBasic = query.getResultList();

        Calendar cal = Calendar.getInstance();
        Long endTime = cal.getTimeInMillis();
        Long startTime = cal.getTimeInMillis() - 300000;

        for (CustomerBasic customerBasic : allCustomerBasic) {
            List<LoanApplication> loanApplication = customerBasic.getLoanApplication();
            Long maxTransactionDateMilis = Long.valueOf(0);
            Double totalSpends = Double.valueOf(0);
            List<LoanRepaymentTransaction> loanRepaymentTransaction = new ArrayList();

            if (!loanApplication.isEmpty()) {
                for (int i = 0; i < loanApplication.size(); i++) {
                    LoanPayableAccount loanPayableAccount = loanApplication.get(i).getLoanPayableAccount();

                    Query transactionQuery = entityManager.createQuery("SELECT l FROM LoanRepaymentTransaction l WHERE l.transactionMillis >= :startTime And l.transactionMillis<=:endTime And l.loanPayableAccount=:loanPayableAccount");
                    transactionQuery.setParameter("startTime", startTime);
                    transactionQuery.setParameter("endTime", endTime);
                    transactionQuery.setParameter("loanPayableAccount", loanPayableAccount);

                    loanRepaymentTransaction = transactionQuery.getResultList();
                    for (int j = 0; j < loanRepaymentTransaction.size(); j++) {
                        if (loanRepaymentTransaction.get(j).getTransactionMillis() > maxTransactionDateMilis) {
                            maxTransactionDateMilis = loanRepaymentTransaction.get(j).getTransactionMillis();
                        }
                        if (loanRepaymentTransaction.get(j).getAccountDebit() != 0.0) {
                            totalSpends = totalSpends + loanRepaymentTransaction.get(j).getAccountDebit();
                        }
                    }
                }

                Long diff = Long.valueOf(0);
                Long transactionDays = Long.valueOf(0);
                if (loanRepaymentTransaction.isEmpty()) {
                    transactionDays = Long.valueOf(0);
                } else {
                    diff = maxTransactionDateMilis - startTime;
                    transactionDays = diff / 10000;
                }

                Integer recencyLevel = checkRecencyLevel(transactionDays);
                Integer frequencyLevel = checkFrequencyLevel(loanRepaymentTransaction.size());
                Integer monetaryLevel = checkMonetaryLevel(totalSpends);

                Rate acqRate = rateSessionBeanLocal.getAcqRate();
                Integer updateMonth = acqRate.getUpdateMonth();
                Integer updateYear = acqRate.getUpdateYear();

                Integer totalRFMValue = recencyLevel + frequencyLevel + monetaryLevel;
                String RFMType = "Loan";

                Long newCustomerRFMId = addNewCustomerRFM(customerBasic.getCustomerName(), recencyLevel.toString(),
                        frequencyLevel.toString(), monetaryLevel.toString(), updateMonth, updateYear,
                        startTime, transactionDays, loanRepaymentTransaction.size(), totalSpends,
                        totalRFMValue.toString(), RFMType, customerBasic.getCustomerBasicId());
            } else {

                Rate acqRate = rateSessionBeanLocal.getAcqRate();
                Integer updateMonth = acqRate.getUpdateMonth();
                Integer updateYear = acqRate.getUpdateYear();

                Integer totalRFMValue = 0;
                String RFMType = "Loan";

                Long newCustomerRFMId = addNewCustomerRFM(customerBasic.getCustomerName(), "0",
                        "0", "0", updateMonth, updateYear,
                        startTime, Long.valueOf(0), Integer.valueOf(0), Double.valueOf(0),
                        totalRFMValue.toString(), RFMType, customerBasic.getCustomerBasicId());
            }
        }
    }

    private Integer checkRecencyLevel(Long transactionDays) {

        if (transactionDays >= 0 && transactionDays <= 5) {
            return 1;
        } else if (transactionDays >= 6 && transactionDays <= 10) {
            return 2;
        } else if (transactionDays >= 11 && transactionDays <= 15) {
            return 3;
        } else if (transactionDays >= 16 && transactionDays <= 20) {
            return 4;
        } else if (transactionDays >= 26) {
            return 5;
        }

        return 0;
    }

    private Integer checkFrequencyLevel(Integer numOfTransactions) {

        if (numOfTransactions >= 0 && numOfTransactions <= 15) {
            return 1;
        } else if (numOfTransactions >= 16 && numOfTransactions <= 30) {
            return 2;
        } else if (numOfTransactions >= 31 && numOfTransactions <= 45) {
            return 3;
        } else if (numOfTransactions >= 46 && numOfTransactions <= 60) {
            return 4;
        } else if (numOfTransactions >= 61) {
            return 5;
        }

        return 0;
    }

    private Integer checkMonetaryLevel(Double totalSpends) {

        if (totalSpends >= 0 && totalSpends <= 500) {
            return 1;
        } else if (totalSpends >= 501 && totalSpends <= 1500) {
            return 2;
        } else if (totalSpends >= 1501 && totalSpends <= 5000) {
            return 3;
        } else if (totalSpends >= 5001 && totalSpends <= 10000) {
            return 4;
        } else if (totalSpends >= 10001) {
            return 5;
        }

        return 0;
    }
}
