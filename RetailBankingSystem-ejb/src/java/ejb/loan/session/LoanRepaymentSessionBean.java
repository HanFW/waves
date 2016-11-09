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
        addLoanTransactions(ra, amount);
        
        em.flush();
        return fromTransactionId;
    }
    
    private void updateLoanAccounts(LoanRepaymentAccount repaymentAccount, double amount){
        System.out.println("****** update loan accounts");
        LoanPayableAccount payableAccount = repaymentAccount.getLoanPayableAccount();
        
        double previousRepaymentBalance = repaymentAccount.getAccountBalance();
        double newRepaymentBalance = previousRepaymentBalance - amount;
        repaymentAccount.setAccountBalance(newRepaymentBalance);
        
        double previousOverdue = repaymentAccount.getOverdueBalance();
        double previousFee = repaymentAccount.getFees();
        
        if(amount < previousRepaymentBalance){
            if(amount <= previousOverdue){
                repaymentAccount.setOverdueBalance(previousOverdue - amount);
                payableAccount.setOverdueBalance(previousOverdue - amount);
            } else if(amount <= (previousOverdue + previousFee)){
                repaymentAccount.setOverdueBalance(0);
                payableAccount.setOverdueBalance(0);
                payableAccount.setAccountStatus("started");
                repaymentAccount.setDefaultMonths(0);
                repaymentAccount.setFees(previousFee - (amount - previousOverdue));
            } else{
                repaymentAccount.setOverdueBalance(0);
                payableAccount.setOverdueBalance(0);
                payableAccount.setAccountStatus("started");
                repaymentAccount.setDefaultMonths(0);
                repaymentAccount.setFees(0);
            }
            repaymentAccount.setPaymentStatus("partially paid");
        }else if(amount == previousRepaymentBalance){
            repaymentAccount.setOverdueBalance(0);
            repaymentAccount.setFees(0);
            repaymentAccount.setPaymentStatus("paid");
            repaymentAccount.setDefaultMonths(0);
            payableAccount.setAccountStatus("started");
        }else{
            repaymentAccount.setOverdueBalance(0);
            repaymentAccount.setFees(0);
            repaymentAccount.setPaymentStatus("over paid");
            repaymentAccount.setDefaultMonths(0);
            payableAccount.setAccountStatus("started");
        }
        
        double previousPayableBalance = payableAccount.getAccountBalance();
        payableAccount.setAccountBalance(previousPayableBalance - amount);
        if(previousPayableBalance - amount <= 0){
            payableAccount.setAccountStatus("completed");
        }
        em.flush();
    }
    
    private void addLoanTransactions(LoanRepaymentAccount repaymentAccount, double amount){
        double accountInterest = repaymentAccount.getInterest();
        
    }
    
    @Override
    public void addLoanRepaymentTransaction(LoanRepaymentAccount repaymentAccount, double amount, String description){
        Calendar cal = Calendar.getInstance();
        Long transactionDateMilis = cal.getTimeInMillis();
        LoanRepaymentTransaction transaction = new LoanRepaymentTransaction();
        transaction.setAccountBalance(repaymentAccount.getAccountBalance() - amount);
        transaction.setAccountCredit(amount);
        transaction.setAccountDebit(0);
        transaction.setDescription(description);
        transaction.setTransactionDate(cal.getTime());
        transaction.setTransactionMillis(transactionDateMilis);
        
        em.persist(transaction);
        em.flush();
    }
    
    @Override
    public void addLoanPayableTransaction(LoanPayableAccount payableAccount, double amount){
        Calendar cal = Calendar.getInstance();
        Long transactionDateMilis = cal.getTimeInMillis();
        LoanRepaymentTransaction transaction = new LoanRepaymentTransaction();
        transaction.setAccountBalance(payableAccount.getAccountBalance() - amount);
        transaction.setAccountCredit(amount);
        transaction.setAccountDebit(0);
        transaction.setDescription("Monthly Repayment");
        transaction.setTransactionDate(cal.getTime());
        transaction.setTransactionMillis(transactionDateMilis);
        
        em.persist(transaction);
        em.flush();
    }
    
    private Long addDepositTransction(BankAccount depositAccount, LoanRepaymentAccount repaymentAccount, double amount){        
        Double newAvailableBalance = Double.valueOf(depositAccount.getAvailableBankAccountBalance()) - amount;
        Double newTotalBalanace = Double.valueOf(depositAccount.getTotalBankAccountBalance()) - amount;
        
        Calendar cal = Calendar.getInstance();
        Long transactionDateMilis = cal.getTimeInMillis();
        
        String longType = repaymentAccount.getLoanPayableAccount().getLoanApplication().getLoanType();
        String transactionCode;
        if(longType.equals("Education Loan")){
            transactionCode = "EDR";
        }else if(longType.equals("Car Loan")){
            transactionCode = "CRR";
        }else if(longType.equals("Renovation Loan")){
            transactionCode = "RNR";
        }else if(longType.equals("Cashline")){
            transactionCode = "CLR";
        }else{
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
