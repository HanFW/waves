/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.card.session;

import ejb.card.entity.PrincipalCard;
import ejb.deposit.entity.BankAccount;
import ejb.deposit.session.TransactionSessionBean;
import ejb.infrastructure.session.CustomerEmailSessionBeanLocal;
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
public class CreditCardRepaymentSessionBean implements CreditCardRepaymentSessionBeanLocal {
    @EJB
    private CustomerEmailSessionBeanLocal customerEmailSessionBeanLocal;
    @EJB
    private TransactionSessionBean transactionSessionBeanLocal;
    
    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    @Override
    public Long makeCreditCardRepayment(BankAccount depositAccount, PrincipalCard card, double amount) {

        PrincipalCard creditCard = em.find(PrincipalCard.class, card.getCardId());
        BankAccount deposit = em.find(BankAccount.class, depositAccount.getBankAccountId());
        System.out.println(creditCard);
        System.out.println(deposit);

        Long fromTransactionId = addDepositTransaction(deposit, creditCard, amount);
        System.out.println(fromTransactionId);
        updateCreditCard(creditCard, amount);

        em.flush();
        return fromTransactionId;
    }
    
    private void updateCreditCard(PrincipalCard card, double amount){
        double overdueInterest = card.getOverdueInterest();
        double overduePrincipal = card.getOverduePrincipal();
        double currentExpense = card.getCurrentExpense();
        
        if(amount < overdueInterest){
            card.setOverdueInterest(overdueInterest - amount);
            addCreditCardRepaymentTransaction(card, amount, "Interest Payment");
        } else if(amount < (overdueInterest + overduePrincipal)){
            card.setOverdueInterest(0);
            card.setOverduePrincipal(overduePrincipal - (amount - overdueInterest));
            addCreditCardRepaymentTransaction(card, overdueInterest, "Interest Payment");
            addCreditCardRepaymentTransaction(card, amount - overdueInterest, "Principal Payment");
        } else{
            card.setOverdueInterest(0);
            card.setOverduePrincipal(0);
            card.setCurrentExpense(currentExpense- (amount - overdueInterest - overduePrincipal));
            addCreditCardRepaymentTransaction(card, overdueInterest, "Interest Payment");
            addCreditCardRepaymentTransaction(card, amount - overdueInterest, "Principal Payment");
        }
        
        em.flush();
    }
    
    private Long addDepositTransaction(BankAccount depositAccount, PrincipalCard card, double amount) {
        Double newAvailableBalance = Double.valueOf(depositAccount.getAvailableBankAccountBalance()) - amount;
        Double newTotalBalanace = Double.valueOf(depositAccount.getTotalBankAccountBalance()) - amount;

        Calendar cal = Calendar.getInstance();
        Long transactionDateMilis = cal.getTimeInMillis();

        String transactionCode = "CCR";

        String transactionRefFrom = depositAccount.getBankAccountType() + "-" + depositAccount.getBankAccountNum();

        Long fromTransactionId = transactionSessionBeanLocal.addNewTransaction(cal.getTime().toString(), transactionCode, transactionRefFrom,
                Double.valueOf(amount).toString(), " ", transactionDateMilis, depositAccount.getBankAccountId());

        depositAccount.setAvailableBankAccountBalance(newAvailableBalance.toString());
        depositAccount.setTotalBankAccountBalance(newTotalBalanace.toString());

        em.flush();
        return fromTransactionId;
    }
    
    private void addCreditCardRepaymentTransaction(PrincipalCard card, double amount, String description) {
        Calendar cal = Calendar.getInstance();
        Long transactionDateMilis = cal.getTimeInMillis();
        LoanRepaymentTransaction transaction = new LoanRepaymentTransaction();
        transaction.setAccountBalance(card.getOverdueInterest()+card.getOverduePrincipal()+card.getCurrentExpense());
        transaction.setAccountCredit(amount);
        transaction.setAccountDebit(0);
        transaction.setDescription(description);
        transaction.setTransactionDate(cal.getTime());
        transaction.setTransactionMillis(transactionDateMilis);
        transaction.setPrincipalCard(card);
        card.addRepaymentTransactions(transaction);

        em.persist(transaction);
        em.flush();
    }
    
    @Override
    public void calculateCreditCardInterest(){
        Query query = em.createQuery("SELECT c FROM PrincipalCard c");
        List<PrincipalCard> cards = query.getResultList();
        
        for(PrincipalCard card: cards){
            double newOutstanding = (card.getOverdueInterest()+card.getOverduePrincipal())*(1+0.0255/365);
            card.setOverdueInterest(newOutstanding - card.getOverduePrincipal());
            card.setDayInMonth(card.getDayInMonth()+1);
            System.out.println("day in months: " +  card.getDayInMonth()+1);
            if(card.getDayInMonth() == 30){
                double newOverdue = card.getCurrentExpense();
                if(newOverdue>0){
                    System.out.println("new default on credit card");
                    if(card.getDefaultMonths()<3){
                        customerEmailSessionBeanLocal.sendEmail(card.getCustomerBasic(), "creditCardDefualt", null);
                    }else if(card.getDefaultMonths()==4){
                        card.setCreditLimit(0);
                        customerEmailSessionBeanLocal.sendEmail(card.getCustomerBasic(), "creditCardDefualtAction", null);
                    }
                    card.setDefaultMonths(card.getDefaultMonths()+1);
                }else{
                    card.setDefaultMonths(0);
                }
                card.setOutstandingBalance(0);
                card.setOverduePrincipal(card.getOverduePrincipal()+newOverdue);
                card.setCurrentExpense(0);
                card.setDayInMonth(0);
            }
            em.flush();
        }
    }
}
