/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.loan.session;

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
        query.setParameter("accountStatus2", "bankrupt");
        accounts = query.getResultList();

        for (LoanRepaymentAccount account : accounts) {
            checkLastRepayment(account);
            calculateNewRepayment(account);
            account.setRepaymentMonths(account.getRepaymentMonths() + 1);
            checkAccountStatus(account);
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
        double instalment = 0;
        double principal = payableAccount.getInitialAmount();
        double period = loanApplicaiton.getPeriodSuggested();
        double interest = 0;
        double oldTotalRemaining = payableAccount.getAccountBalance();
        double newTotalRemaining;

        if (oldTotalRemaining > 0) {
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

            instalment = (rate / 12 * principal) / (1 - Math.pow((1 + rate / 12), -period));
            System.out.println("*** new month instalment: " + instalment);
            interest = oldTotalRemaining * rate / 12;
            interest = Math.round(interest * 100.0) / 100.0;
            System.out.println("*** new month interest: " + interest);
            newTotalRemaining = oldTotalRemaining + interest;
            System.out.println("*** old account balance: " + oldTotalRemaining);
            System.out.println("*** new account balance: " + newTotalRemaining);
            instalment = Math.round(instalment * 100.0) / 100.0;
            payableAccount.setAccountBalance(newTotalRemaining);
            addLoanAccountTransaction(account, interest, "Interet", "debit");

            account.setInterest(interest);
            account.setPrincipal(instalment - interest);
            account.setInstalment(instalment);
            double previousAccountBalance = account.getAccountBalance();
            if(oldTotalRemaining < instalment){
                account.setAccountBalance(previousAccountBalance + oldTotalRemaining);
            }else{
                account.setAccountBalance(previousAccountBalance + instalment);
            }
        }

        em.flush();
    }

    public void checkAccountStatus(LoanRepaymentAccount account) {
        LoanPayableAccount payableAccount = account.getLoanPayableAccount();
        
        if (payableAccount.getAccountBalance() - account.getInstalment()<= 0) {
            payableAccount.setAccountStatus("ended");
        } else if(account.getRepaymentMonths() >= payableAccount.getLoanApplication().getPeriodSuggested()){
            payableAccount.setAccountStatus("ended");
        }
    }
    
    private void addLoanAccountTransaction(LoanRepaymentAccount repaymentAccount, double amount, String description, String action){
        Calendar cal = Calendar.getInstance();
        Long transactionDateMilis = cal.getTimeInMillis();
        LoanRepaymentTransaction transaction = new LoanRepaymentTransaction();
        transaction.setAccountBalance(repaymentAccount.getAccountBalance() - amount);
        transaction.setAccountCredit(amount);
        if(action.equals("credit")){
            transaction.setAccountCredit(amount);
            transaction.setAccountDebit(0);
        }else{
            transaction.setAccountCredit(0);
            transaction.setAccountDebit(amount);
        }
        transaction.setDescription(description);
        transaction.setTransactionDate(cal.getTime());
        transaction.setTransactionMillis(transactionDateMilis);
        transaction.setLoanRepaymentAccount(repaymentAccount);
        
        em.persist(transaction);
        em.flush();
    }
}
