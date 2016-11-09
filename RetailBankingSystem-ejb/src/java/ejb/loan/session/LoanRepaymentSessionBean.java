/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.loan.session;

import ejb.deposit.entity.BankAccount;
import ejb.deposit.session.TransactionSessionBean;
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
public class LoanRepaymentSessionBean implements LoanRepaymentSessionBeanLocal {

    @EJB
    private TransactionSessionBean transactionSessionBeanLocal;

    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;

    @Override
    public Long makeMonthlyRepayment(BankAccount depositAccount, LoanRepaymentAccount repaymentAccount, double amount) {

        LoanRepaymentAccount ra = em.find(LoanRepaymentAccount.class, repaymentAccount.getId());
        BankAccount deposit = em.find(BankAccount.class, depositAccount.getBankAccountId());

        Long fromTransactionId = addDepositTransction(deposit, ra, amount);
        updateLoanAccounts(ra, amount);

        em.flush();
        return fromTransactionId;
    }

    private void updateLoanAccounts(LoanRepaymentAccount repaymentAccount, double amount) {
        System.out.println("****** update loan accounts");
        LoanPayableAccount payableAccount = repaymentAccount.getLoanPayableAccount();

        // set new account balance
        double previousRepaymentBalance = repaymentAccount.getAccountBalance();
        double newRepaymentBalance = previousRepaymentBalance - amount;
        repaymentAccount.setAccountBalance(newRepaymentBalance);

        // set new overdue
        double previousOverdue = repaymentAccount.getOverdueBalance();
        double previousFee = repaymentAccount.getFees();
        double currentInstalment = repaymentAccount.getCurrentInstalment();
        double currentInterest = repaymentAccount.getCurrentInterest();
        double currentPrincipal = repaymentAccount.getCurrentPrincipal();
        double totalInterest = repaymentAccount.getTotalInterest();
        double totalPrincipal = repaymentAccount.getTotalPrincipal();
        double previousInterest = totalInterest - repaymentAccount.getCurrentInterest();
        double previousPrincipal = totalPrincipal - repaymentAccount.getCurrentPrincipal();

        if (amount <= previousRepaymentBalance) {
            //partial payment
            if (amount <= previousInterest) {
                repaymentAccount.setTotalInterest(totalInterest - amount);
                repaymentAccount.setOverdueBalance(previousOverdue - amount);
                addLoanRepaymentTransaction(repaymentAccount, amount, "Interest Payment");
            } else if (amount < (previousInterest + previousPrincipal)) {
                repaymentAccount.setTotalInterest(currentInterest);
                double principalPaid = amount - previousInterest;
                repaymentAccount.setTotalPrincipal(totalPrincipal - principalPaid);
                repaymentAccount.setOverdueBalance(previousOverdue - previousInterest - principalPaid);
                addLoanRepaymentTransaction(repaymentAccount, previousInterest, "Interest Payment");
                addLoanRepaymentTransaction(repaymentAccount, principalPaid, "Principal Payment");
                payableAccount.setAccountBalance(payableAccount.getAccountBalance() - principalPaid);
                //payable transaction
                addLoanPayableTransaction(payableAccount, principalPaid);
            } else if (amount < (previousInterest + previousPrincipal + previousFee)) {
                repaymentAccount.setTotalInterest(currentInterest);
                repaymentAccount.setTotalPrincipal(currentPrincipal);
                double feePaid = amount - previousInterest - previousPrincipal;
                repaymentAccount.setFees(previousFee - feePaid);
                repaymentAccount.setOverdueBalance(0);
                payableAccount.setAccountBalance(payableAccount.getAccountBalance() - previousPrincipal);
                addLoanRepaymentTransaction(repaymentAccount, previousInterest, "Interest Payment");
                addLoanRepaymentTransaction(repaymentAccount, previousPrincipal, "Principal Payment");
                addLoanRepaymentTransaction(repaymentAccount, feePaid, "Late Fee Payment");
                //payable transaction
                addLoanPayableTransaction(payableAccount, previousPrincipal);
            } else if (amount < (previousInterest + previousPrincipal + previousFee + currentInterest)) {
                repaymentAccount.setFees(0);
                repaymentAccount.setOverdueBalance(0);
                double interestPaid = amount - previousOverdue - previousFee;
                repaymentAccount.setCurrentInterest(currentInterest - interestPaid);
                repaymentAccount.setCurrentInstalment(currentInstalment - interestPaid);
                payableAccount.setAccountBalance(payableAccount.getAccountBalance() - previousPrincipal);
                repaymentAccount.setTotalInterest(currentInterest - interestPaid);
                repaymentAccount.setTotalPrincipal(currentPrincipal);
                addLoanRepaymentTransaction(repaymentAccount, previousInterest, "Interest Payment");
                addLoanRepaymentTransaction(repaymentAccount, previousPrincipal, "Principal Payment");
                addLoanRepaymentTransaction(repaymentAccount, previousFee, "Late Fee Payment");
                addLoanRepaymentTransaction(repaymentAccount, interestPaid, "Interest Payment");
                addLoanPayableTransaction(payableAccount, previousPrincipal);
            } else {
                repaymentAccount.setFees(0);
                repaymentAccount.setOverdueBalance(0);
                double principalPaid = amount - previousOverdue - previousFee - previousInterest;
                repaymentAccount.setCurrentInterest(0);
                repaymentAccount.setCurrentPrincipal(currentPrincipal - principalPaid);
                repaymentAccount.setCurrentInstalment(currentInstalment - currentInterest - principalPaid);
                payableAccount.setAccountBalance(payableAccount.getAccountBalance() - previousPrincipal - principalPaid);
                repaymentAccount.setTotalInterest(0);
                repaymentAccount.setTotalPrincipal(currentPrincipal - principalPaid);
                addLoanRepaymentTransaction(repaymentAccount, previousInterest, "Interest Payment");
                addLoanRepaymentTransaction(repaymentAccount, previousPrincipal, "Principal Payment");
                addLoanRepaymentTransaction(repaymentAccount, previousFee, "Late Fee Payment");
                addLoanRepaymentTransaction(repaymentAccount, currentInterest, "Interest Payment");
                addLoanRepaymentTransaction(repaymentAccount, principalPaid, "Principal Payment");
                addLoanPayableTransaction(payableAccount, previousPrincipal);
                addLoanPayableTransaction(payableAccount, principalPaid);
            }
        } else if (amount == previousRepaymentBalance) {
            //full payment
            repaymentAccount.setFees(0);
            repaymentAccount.setOverdueBalance(0);
            repaymentAccount.setCurrentInterest(0);
            repaymentAccount.setCurrentPrincipal(0);
            repaymentAccount.setTotalInterest(0);
            repaymentAccount.setTotalPrincipal(0);
            repaymentAccount.setCurrentInstalment(0);
            addLoanRepaymentTransaction(repaymentAccount, previousInterest, "Interest Payment");
            addLoanRepaymentTransaction(repaymentAccount, previousPrincipal, "Principal Payment");
            addLoanRepaymentTransaction(repaymentAccount, previousFee, "Late Fee Payment");
            addLoanRepaymentTransaction(repaymentAccount, currentInterest, "Interest Payment");
            addLoanRepaymentTransaction(repaymentAccount, currentPrincipal, "Principal Payment");
            addLoanPayableTransaction(payableAccount, previousPrincipal);
            addLoanPayableTransaction(payableAccount, currentPrincipal);
        } else {
            //overpaid
            repaymentAccount.setFees(0);
            repaymentAccount.setOverdueBalance(0);
            repaymentAccount.setCurrentInterest(0);
            repaymentAccount.setCurrentPrincipal(0);
            repaymentAccount.setTotalInterest(0);
            repaymentAccount.setTotalPrincipal(0);
            repaymentAccount.setCurrentInstalment(0);
            addLoanRepaymentTransaction(repaymentAccount, previousInterest, "Interest Payment");
            addLoanRepaymentTransaction(repaymentAccount, previousPrincipal, "Principal Payment");
            addLoanRepaymentTransaction(repaymentAccount, previousFee, "Late Fee Payment");
            addLoanRepaymentTransaction(repaymentAccount, currentInterest, "Interest Payment");
            addLoanRepaymentTransaction(repaymentAccount, currentPrincipal, "Principal Payment");
            addLoanPayableTransaction(payableAccount, previousPrincipal);
            addLoanPayableTransaction(payableAccount, currentPrincipal);
        }

        double previousPayableBalance = payableAccount.getAccountBalance();
        if (previousPayableBalance - amount <= 0) {
            payableAccount.setAccountStatus("completed");
        }

        em.flush();
    }

    @Override
    public void addLoanRepaymentTransaction(LoanRepaymentAccount repaymentAccount, double amount, String description) {
        Calendar cal = Calendar.getInstance();
        Long transactionDateMilis = cal.getTimeInMillis();
        LoanRepaymentTransaction transaction = new LoanRepaymentTransaction();
        transaction.setAccountBalance(repaymentAccount.getAccountBalance() - amount);
        transaction.setAccountCredit(amount);
        transaction.setAccountDebit(0);
        transaction.setDescription(description);
        transaction.setTransactionDate(cal.getTime());
        transaction.setTransactionMillis(transactionDateMilis);
        transaction.setLoanRepaymentAccount(repaymentAccount);
        repaymentAccount.addLoanRepaymentTransaction(transaction);

        em.persist(transaction);
        em.flush();
    }

    @Override
    public void addLoanPayableTransaction(LoanPayableAccount payableAccount, double amount) {
        Calendar cal = Calendar.getInstance();
        Long transactionDateMilis = cal.getTimeInMillis();
        LoanRepaymentTransaction transaction = new LoanRepaymentTransaction();
        transaction.setAccountBalance(payableAccount.getAccountBalance() - amount);
        transaction.setAccountCredit(amount);
        transaction.setAccountDebit(0);
        transaction.setDescription("Monthly Repayment");
        transaction.setTransactionDate(cal.getTime());
        transaction.setTransactionMillis(transactionDateMilis);
        transaction.setLoanPayableAccount(payableAccount);
        payableAccount.addLoanRepaymentTransaction(transaction);

        em.persist(transaction);
        em.flush();
    }

    private Long addDepositTransction(BankAccount depositAccount, LoanRepaymentAccount repaymentAccount, double amount) {
        Double newAvailableBalance = Double.valueOf(depositAccount.getAvailableBankAccountBalance()) - amount;
        Double newTotalBalanace = Double.valueOf(depositAccount.getTotalBankAccountBalance()) - amount;

        Calendar cal = Calendar.getInstance();
        Long transactionDateMilis = cal.getTimeInMillis();

        String longType = repaymentAccount.getLoanPayableAccount().getLoanApplication().getLoanType();
        String transactionCode;
        if (longType.equals("Education Loan")) {
            transactionCode = "EDR";
        } else if (longType.equals("Car Loan")) {
            transactionCode = "CRR";
        } else if (longType.equals("Renovation Loan")) {
            transactionCode = "RNR";
        } else if (longType.equals("Cashline")) {
            transactionCode = "CLR";
        } else {
            transactionCode = "MGR";
        }

        String transactionRefFrom = depositAccount.getBankAccountType() + "-" + depositAccount.getBankAccountNum();

        Long fromTransactionId = transactionSessionBeanLocal.addNewTransaction(cal.getTime().toString(), transactionCode, transactionRefFrom,
                Double.valueOf(amount).toString(), " ", transactionDateMilis, depositAccount.getBankAccountId());

        depositAccount.setAvailableBankAccountBalance(newAvailableBalance.toString());
        depositAccount.setTotalBankAccountBalance(newTotalBalanace.toString());

        em.flush();
        return fromTransactionId;
    }

    @Override
    public LoanRepaymentAccount getRepaymentAccountByAccountNum(String accountNum) {
        Query query = em.createQuery("Select a From LoanRepaymentAccount a Where a.accountNumber=:accountNum");
        query.setParameter("accountNum", accountNum);

        List resultList = query.getResultList();
        return (LoanRepaymentAccount) resultList.get(0);
    }
}
