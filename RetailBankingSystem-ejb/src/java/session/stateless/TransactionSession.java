package session.stateless;

import entity.BankAccount;
import entity.AccTransaction;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Date;
import java.util.Calendar;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NonUniqueResultException;

@Stateless
@LocalBean
public class TransactionSession implements TransactionSessionLocal {
    @EJB
    private BankAccountSessionLocal bankAccountSessionLocal;
    
    @PersistenceContext
    private EntityManager entityManager;
    
    @Override
    public BankAccount retrieveBankAccountById(Long bankAccountId){
        BankAccount bankAccount = new BankAccount();
        
        try{
            Query query = entityManager.createQuery("Select a From BankAccount a Where a.bankAccountId=:bankAccountId");
            query.setParameter("bankAccountId",bankAccountId);
            bankAccount = (BankAccount)query.getSingleResult();
        }
        catch(EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error: "+enfe.getMessage());
            return new BankAccount();
        }
        catch(NonUniqueResultException nure) {
            System.out.println("\nNon unique result error: "+nure.getMessage());
        }
        
        return bankAccount;
    }
    
    @Override
    public BankAccount retrieveBankAccountByNum(String bankAccountNum){
        BankAccount bankAccount = new BankAccount();
        
        try{
            Query query = entityManager.createQuery("Select a From BankAccount a Where a.bankAccountNum=:bankAccountNum");
            query.setParameter("bankAccountNum",bankAccountNum);
            bankAccount = (BankAccount)query.getSingleResult();
        }
        catch(EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error: "+enfe.getMessage());
            return new BankAccount();
        }
        catch(NonUniqueResultException nure) {
            System.out.println("\nNon unique result error: "+nure.getMessage());
        }
        
        return bankAccount;
    }
    
    @Override
    public List<AccTransaction> retrieveAccTransactionByBankNum(String bankAccountNum) {
        BankAccount bankAccount= bankAccountSessionLocal.retrieveBankAccountByNum(bankAccountNum);
        
        if(bankAccount==null) {
            return null;
        }
        try{
            Query query = entityManager.createQuery("Select t From AccTransaction t Where t.bankAccount=:bankAccount");
            query.setParameter("bankAccount",bankAccount);
            return query.getResultList();
        }
        catch(EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error: "+enfe.getMessage());
            return null;
        }
    }
    
    @Override
    public Long addNewTransaction(String transactionDate,String transactionCode,String transactionRef,
            String accountDebit,String accountCredit,Long bankAccountId) {
        AccTransaction accTransaction = new AccTransaction();
        
        accTransaction.setTransactionDate(transactionDate);
        accTransaction.setTransactionCode(transactionCode);
        accTransaction.setTransactionRef(transactionRef);
        accTransaction.setAccountDebit(accountDebit);
        accTransaction.setAccountCredit(accountCredit);
        accTransaction.setBankAccount(retrieveBankAccountById(bankAccountId));
        
        entityManager.persist(accTransaction);
        entityManager.flush();
        
        return accTransaction.getTransactionId();
    }
    
    @Override
    public String cashDeposit(String bankAccountNum,String depositAmt) {
        
        Long bankAccountId;
        Long accTransactionId;
        
        BankAccount bankAccount = retrieveBankAccountByNum(bankAccountNum);
        bankAccountId = bankAccount.getBankAccountId();
        
        if(bankAccountId==null){
            return "Error! Bank account does not exist!";
        }
        else
        {
            Date date= new Date();
            
            String accountCredit = null;
            String transactionCode="ADP";
            String transactionRef="Merlion Bank Branch";
            
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
            String transactionDate = dayOfMonth+"-"+(month+1)+"-"+year;
            
            accTransactionId=addNewTransaction(transactionDate,transactionCode,transactionRef,
                    depositAmt,accountCredit,bankAccountId);
            
            Double preBalance = Double.valueOf(bankAccount.getBankAccountBalance());
            Double depositAmtDouble = Double.valueOf(depositAmt);
            Double totalBalance = preBalance+depositAmtDouble;
            String balance = totalBalance.toString();
            bankAccount.setBankAccountBalance(balance);
            
            return "Deposit successfully!";
        }
    }
    
    @Override
    public String cashWithdraw(String bankAccountNum,String withdrawAmt) {
        
        Long bankAccountId;
        Long accTransactionId;
        
        BankAccount bankAccount = retrieveBankAccountByNum(bankAccountNum);
        bankAccountId = bankAccount.getBankAccountId();
        
        if(bankAccountId==null){
            return "Error! Bank account does not exist!";
        }
        else
        {
            Date date= new Date();
            
            String accountDebit = null;
            String transactionCode="AWL";
            String transactionRef="Merlion Bank Branch";
            
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
            String transactionDate = dayOfMonth+"-"+(month+1)+"-"+year;
            
            accTransactionId=addNewTransaction(transactionDate,transactionCode,transactionRef,
                    accountDebit,withdrawAmt,bankAccountId);
            
            Double preBalance = Double.valueOf(bankAccount.getBankAccountBalance());
            Double withdrawAmtDouble = Double.valueOf(withdrawAmt);
            Double totalBalance = preBalance-withdrawAmtDouble;
            String balance = totalBalance.toString();
            bankAccount.setBankAccountBalance(balance);
            
            return "Withdraw successfully!";
        }
    }
    
    @Override
    public String checkPassword(String bankAccountNum,String bankAccountPwd) {
        
        Long bankAccountId;
        
        Query query = entityManager.createQuery("select a from BankAccount a where a.bankAccountNum =:bankAccountNum");
        query.setParameter("bankAccountNum", bankAccountNum);
        
        BankAccount bankAccount;
        try{
            bankAccount = (BankAccount)query.getSingleResult();
            
        } catch (Exception e){
            bankAccount = null;
        }
        
        if(bankAccount==null){
            return "Error! Bank account does not exist!";
        } else {
            if(!bankAccount.getBankAccountPwd().equals(bankAccountPwd)){
                return "Password is incorrect!";
            }
        }
        return "Password is correct!";
    }
    
    @Override
    public String fundTransfer(String fromAccount,String toAccount,String transferAmt) {
        
        BankAccount bankAccountFrom = bankAccountSessionLocal.retrieveBankAccountByNum(fromAccount);
        BankAccount bankAccountTo = bankAccountSessionLocal.retrieveBankAccountByNum(toAccount);
        
        Double balanceAccountFrom = Double.valueOf(bankAccountFrom.getBankAccountBalance())-Double.valueOf(transferAmt);
        Double balanceAccountTo = Double.valueOf(bankAccountTo.getBankAccountBalance())+Double.valueOf(transferAmt);
        
        Long bankAccountFromId = bankAccountFrom.getBankAccountId();
        Long bankAccountToId = bankAccountTo.getBankAccountId();
        
        Date date= new Date();
        
        String transactionCode="TRF";
        String transactionRefFrom=toAccount;
        String transactionRefTo=fromAccount;
        
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
        String transactionDate = dayOfMonth+"-"+(month+1)+"-"+year;
        
        Long fromTransactionId=addNewTransaction(transactionDate,transactionCode,transactionRefFrom,
                null,transferAmt,bankAccountFromId);
        Long toTransactionId=addNewTransaction(transactionDate,transactionCode,transactionRefTo,
                transferAmt,null,bankAccountToId);
        bankAccountFrom.setBankAccountBalance(balanceAccountFrom.toString());
        bankAccountTo.setBankAccountBalance(balanceAccountTo.toString());
        
        return "Fund Transfer Sucessfully!";
    }
}
