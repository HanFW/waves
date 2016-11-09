/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.loan.session;

import ejb.deposit.entity.BankAccount;
import ejb.deposit.session.BankAccountSessionBeanLocal;
import ejb.infrastructure.session.CustomerEmailSessionBeanLocal;
import ejb.loan.entity.LoanApplication;
import ejb.loan.entity.LoanPayableAccount;
import ejb.loan.entity.LoanRepaymentAccount;
import ejb.loan.entity.LoanRepaymentTransaction;
import java.util.Calendar;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author hanfengwei
 */
@Stateless
public class LoanInterestSessionBean implements LoanInterestSessionBeanLocal {

    @EJB
    private LoanManagementSessionBeanLocal loanManagementSessionBeanLocal;
    @EJB
    private BankAccountSessionBeanLocal bankAccountSessionBeanLocal;
    @EJB
    private LoanRepaymentSessionBeanLocal loanRepaymentSessionBeanLocal;
    @EJB
    private CustomerEmailSessionBeanLocal customerEmailSessionBeanLocal;

    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;

    @Override
    public void calculateInstalment() {
        System.out.println("****** loan/LoanInterestSessionBean: calculateInstalment() ******");

        List<LoanRepaymentAccount> accounts;
        Query query = em.createQuery("SELECT a FROM LoanRepaymentAccount a WHERE a.loanPayableAccount.accountStatus = :accountStatus OR a.loanPayableAccount.accountStatus = :accountStatus2 OR a.loanPayableAccount.accountStatus = :accountStatus3");
        query.setParameter("accountStatus", "started");
        query.setParameter("accountStatus2", "default");
        query.setParameter("accountStatus3", "bankrupt");
        accounts = query.getResultList();

        for (LoanRepaymentAccount account : accounts) {
            checkLastRepayment(account);
            String status = checkAccountStatus(account);
            if ((!status.equals("ended")) || (!status.equals("completed"))) {
                calculateNewRepayment(account);
                account.setRepaymentMonths(account.getRepaymentMonths() + 1);

                if (account.getDepositAccountNumber() != null) {
                    BankAccount deposit = bankAccountSessionBeanLocal.retrieveBankAccountByNum(account.getDepositAccountNumber());
                    Double availableBalance = Double.valueOf(deposit.getAvailableBankAccountBalance());
                    if (availableBalance < account.getAccountBalance()) {
                        loanManagementSessionBeanLocal.deleteRecurringLoanServingAccount(account.getId());
                        customerEmailSessionBeanLocal.sendEmail(account.getLoanPayableAccount().getLoanApplication().getCustomerBasic(), "recurringStopReminder", null);
                    } else {
                        loanRepaymentSessionBeanLocal.makeMonthlyRepayment(deposit, account, account.getAccountBalance());
                    }
                }
            }
        }

        Query query2 = em.createQuery("SELECT a FROM LoanRepaymentAccount a WHERE a.loanPayableAccount.accountStatus = :accountStatus");
        query2.setParameter("accountStatus", "ended");
        accounts = query2.getResultList();
        for (LoanRepaymentAccount account : accounts) {
            checkLastRepayment(account);
        }

        em.flush();
    }

    private void checkLastRepayment(LoanRepaymentAccount account) {
        LoanPayableAccount payableAccount = account.getLoanPayableAccount();
        if (account.getRepaymentMonths() > 0 && account.getAccountBalance() > 0) {
            int defaultMonths = account.getDefaultMonths() + 1;
            account.setDefaultMonths(defaultMonths);
            System.out.println("******* default for " + defaultMonths + " months");

            //change balance
            double newOverdue = account.getAccountBalance() - account.getFees();
            double newFee = newOverdue * 0.003;
            newFee = Math.round(newFee * 100.0) / 100.0;
            if (newFee < 30) {
                newFee = 30;
            }
            double extraFee = newFee - account.getFees();
            addLoanAccountTransaction(account, extraFee, "Late Charge", "debit");
            payableAccount.setAccountBalance(payableAccount.getAccountBalance() + extraFee);

            newOverdue = Math.round(newOverdue * 100.0) / 100.0;
            account.setOverdueBalance(newOverdue);
            payableAccount.setOverdueBalance(newOverdue);
            account.setAccountBalance(newOverdue + newFee);
            account.setFees(newFee);
            System.out.println("******* new overdue: " + newOverdue);
            System.out.println("******* new fee: " + newFee);

            if (defaultMonths > 0 && defaultMonths < 3) {
                //send gentle email
                payableAccount.setAccountStatus("default");
                customerEmailSessionBeanLocal.sendEmail(account.getLoanPayableAccount().getLoanApplication().getCustomerBasic(), "gentelDefaultReminder", null);
            } else if (defaultMonths < 6) {
                //send warning email, credit report record
                payableAccount.setAccountStatus("default");
                customerEmailSessionBeanLocal.sendEmail(account.getLoanPayableAccount().getLoanApplication().getCustomerBasic(), "seriousDefaultReminder", null);
            } else {
                //take actions
                payableAccount.setAccountStatus("bankrupt");
                customerEmailSessionBeanLocal.sendEmail(account.getLoanPayableAccount().getLoanApplication().getCustomerBasic(), "bankruptReminder", null);
            }
            account.setPaymentStatus("default");
        } else {
            //should be done when customer make payment
            System.out.println("******* no overdue balance this month");
            account.setDefaultMonths(0);
            account.setPaymentStatus("pending");
        }

        em.flush();
    }

    private void calculateNewRepayment(LoanRepaymentAccount account) {
        LoanPayableAccount payableAccount = account.getLoanPayableAccount();
        LoanApplication loanApplicaiton = payableAccount.getLoanApplication();
        String interestPackage = loanApplicaiton.getLoanInterestPackage().getPackageName();

        double rate = 0;
        double currentInstalment = 0;
        double remainingPrincipal = payableAccount.getAccountBalance();
        double period = loanApplicaiton.getPeriodSuggested();
        double repaymentPrincipal = account.getTotalPrincipal();
        double repaymentInterest = account.getTotalInterest();
        double previousAccountBalance = account.getAccountBalance();

        if (repaymentPrincipal < remainingPrincipal) {
            if (interestPackage.equals("Car Loan")) {
                rate = loanApplicaiton.getLoanInterestPackage().getInterestRate();
                double initialPrincipal = payableAccount.getInitialAmount();

                //current interest
                double currentInterest = initialPrincipal * rate / 12;
                currentInterest = Math.round(currentInterest * 100.0) / 100.0;
                account.setCurrentInterest(currentInterest);
                System.out.println("*** new month interest: " + currentInterest);

                //current principal
                double currentPrincipal = initialPrincipal / period;
                currentPrincipal = Math.round(currentPrincipal * 100.0) / 100.0;
                account.setCurrentPrincipal(currentPrincipal);
                System.out.println("*** new month principal: " + currentPrincipal);

                //current instalment
                currentInstalment = currentInterest + currentPrincipal;
                currentInstalment = Math.round(currentInstalment * 100.0) / 100.0;
                account.setCurrentInstalment(currentInstalment);
                System.out.println("*** new month instalment: " + currentInstalment);

                //total interest
                double newRepaymentInterest = repaymentInterest + currentInterest;
                newRepaymentInterest = Math.round(newRepaymentInterest * 100.0) / 100.0;
                account.setTotalInterest(newRepaymentInterest);
                System.out.println("*** new total interest: " + newRepaymentInterest);

                //total principal
                double newRepaymentPrincipal = repaymentPrincipal + currentPrincipal;
                newRepaymentPrincipal = Math.round(newRepaymentPrincipal * 100.0) / 100.0;
                account.setTotalPrincipal(newRepaymentPrincipal);
                System.out.println("*** new total principal: " + newRepaymentPrincipal);

                //new account balance
                double newAccountBalance = previousAccountBalance + currentInstalment;
                newAccountBalance = Math.round(newAccountBalance * 100.0) / 100.0;
                account.setAccountBalance(newAccountBalance);
                System.out.println("*** new account balance: " + newAccountBalance);

            } else {
                if (interestPackage.equals("HDB-Fixed")) {
                    if (account.getRepaymentMonths() == 0) {
                        rate = 0.018;
                    } else if (account.getRepaymentMonths() < 36) {
                        rate = 0.018;
                    } else {
                        rate = loanApplicaiton.getLoanInterestPackage().getInterestRate() + 0.012;
                    }
                } else if (interestPackage.equals("HDB-Floating")) {
                    rate = loanApplicaiton.getLoanInterestPackage().getInterestRate() + 0.013;
                } else if (interestPackage.equals("Private Property-Fixed")) {
                    if (account.getRepaymentMonths() == 0) {
                        rate = 0.018;
                    } else if (account.getRepaymentMonths() < 36) {
                        rate = 0.018;
                    } else {
                        rate = loanApplicaiton.getLoanInterestPackage().getInterestRate() + 0.012;
                    }
                } else if (interestPackage.equals("Private Property-Floating")) {
                    rate = loanApplicaiton.getLoanInterestPackage().getInterestRate() + 0.0125;
                } else {
                    rate = loanApplicaiton.getLoanInterestPackage().getInterestRate();
                }

                //instalment for this month
                currentInstalment = (rate / 12 * remainingPrincipal) / (1 - Math.pow((1 + rate / 12), -period));
                currentInstalment = Math.round(currentInstalment * 100.0) / 100.0;
                account.setCurrentInstalment(currentInstalment);
                System.out.println("*** new month instalment: " + currentInstalment);

                //interest for this month
                double currentInterest = remainingPrincipal * rate / 12;
                currentInterest = Math.round(currentInterest * 100.0) / 100.0;
                account.setCurrentInterest(currentInterest);
                System.out.println("*** new month interest: " + currentInterest);

                //principal for this month
                double currentPrincipal = currentInstalment - currentInterest;
                currentPrincipal = Math.round(currentPrincipal * 100.0) / 100.0;
                account.setCurrentPrincipal(currentPrincipal);
                System.out.println("*** new month principal: " + currentPrincipal);

                //total interest
                double newRepaymentInterest = repaymentInterest + currentInterest;
                newRepaymentInterest = Math.round(newRepaymentInterest * 100.0) / 100.0;
                account.setTotalInterest(newRepaymentInterest);
                System.out.println("*** new total interest: " + newRepaymentInterest);

                //total principal
                double newRepaymentPrincipal = repaymentPrincipal + currentPrincipal;
                newRepaymentPrincipal = Math.round(newRepaymentPrincipal * 100.0) / 100.0;
                account.setTotalPrincipal(newRepaymentPrincipal);
                System.out.println("*** new total principal: " + newRepaymentPrincipal);

                //new account balance
                double newAccountBalance = previousAccountBalance + currentInstalment;
                newAccountBalance = Math.round(newAccountBalance * 100.0) / 100.0;
                account.setAccountBalance(newAccountBalance);
                System.out.println("*** new account balance: " + newAccountBalance);

                //set payment status
                if (newAccountBalance < 0) {
                    account.setPaymentStatus("overpaid");
                } else if (newAccountBalance == 0) {
                    account.setPaymentStatus("paid");
                } else if (newAccountBalance < currentInstalment) {
                    account.setPaymentStatus("partially paid");
                } else if (newAccountBalance == currentInstalment) {
                    account.setPaymentStatus("pending");
                } else{
                    account.setPaymentStatus("default");
                }
            }

        }

        em.flush();
    }

    public String checkAccountStatus(LoanRepaymentAccount account) {
        System.out.println("****** checkLoanAccountStatus");
        LoanPayableAccount payableAccount = account.getLoanPayableAccount();

        if (payableAccount.getAccountBalance() - account.getAccountBalance() <= 0) {
            System.out.println("****** checkLoanAccountStatus: all balance counted");
            payableAccount.setAccountStatus("ended");
        } else if (account.getRepaymentMonths() >= payableAccount.getLoanApplication().getPeriodSuggested()) {
            payableAccount.setAccountStatus("ended");
            System.out.println("****** checkLoanAccountStatus: loan tenure finished");
        }
        em.flush();
        return "";
    }

    private void addLoanAccountTransaction(LoanRepaymentAccount repaymentAccount, double amount, String description, String action) {
        Calendar cal = Calendar.getInstance();
        Long transactionDateMilis = cal.getTimeInMillis();
        LoanRepaymentTransaction transaction = new LoanRepaymentTransaction();
        transaction.setAccountBalance(repaymentAccount.getAccountBalance() - amount);
        transaction.setAccountCredit(amount);
        if (action.equals("credit")) {
            transaction.setAccountCredit(amount);
            transaction.setAccountDebit(0);
        } else {
            transaction.setAccountCredit(0);
            transaction.setAccountDebit(amount);
        }
        transaction.setDescription(description);
        transaction.setTransactionDate(cal.getTime());
        transaction.setTransactionMillis(transactionDateMilis);

        em.persist(transaction);
        em.flush();
    }
}
