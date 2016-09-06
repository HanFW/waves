package session.stateless;

import entity.BankAccount;
import entity.AccTransaction;
import entity.CustomerBasic;
import java.util.ArrayList;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.PersistenceContext;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NonUniqueResultException;

@Stateless
@LocalBean
public class BankAccountSession implements BankAccountSessionLocal {
    @EJB
    private InterestSessionLocal interestSessionLocal;
    @EJB
    private CRMCustomerSessionBean customerSessionBean;
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
    public BankAccount retrieveBankAccountByNum(String bankAccountNum) {
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
    public List<BankAccount> retrieveBankAccountByCusIC(String customerIdentificationNum) {
        CustomerBasic customerBasic = customerSessionBean.retrieveCustomerBasicByIC(customerIdentificationNum);
        
        if(customerBasic==null) {
            return null;
        }
        try{
            Query query = entityManager.createQuery("Select a From BankAccount a Where a.customerBasic=:customerBasic");
            query.setParameter("customerBasic",customerBasic);
            return query.getResultList();
        }
        catch(EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error: "+enfe.getMessage());

            return null;
        }
    }
    
    @Override
    public CustomerBasic retrieveCustomerBasicById(Long customerBasicId){
        CustomerBasic customerBasic = new CustomerBasic();
        
        try{
            Query query = entityManager.createQuery("Select c From CustomerBasic c Where c.customerBasicId=:customerBasicId");
            query.setParameter("customerBasicId",customerBasicId);
            customerBasic = (CustomerBasic)query.getSingleResult();
        }
        catch(EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error: "+enfe.getMessage());
            return new CustomerBasic();
        }
        catch(NonUniqueResultException nure) {
            System.out.println("\nNon unique result error: "+nure.getMessage());
        }
        
        return customerBasic;
    }
    
    @Override
    public CustomerBasic retrieveCustomerBasicByIC(String customerIdentificationNum){
        CustomerBasic customerBasic = new CustomerBasic();
        
        try{
            Query query = entityManager.createQuery("Select c From CustomerBasic c Where c.customerIdentificationNum=:customerIdentificationNum");
            query.setParameter("customerIdentificationNum",customerIdentificationNum);
            customerBasic = (CustomerBasic)query.getSingleResult();
        }
        catch(EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error: "+enfe.getMessage());
            return new CustomerBasic();
        }
        catch(NonUniqueResultException nure) {
            System.out.println("\nNon unique result error: "+nure.getMessage());
        }
        
        return customerBasic;
    }
    
    @Override
    public AccTransaction retrieveAccTransactionById(Long transactionId){
        AccTransaction accTransaction = new AccTransaction();
        
        try{
            Query query = entityManager.createQuery("Select t From AccTransaction t Where t.transactionId=:transactionId");
            query.setParameter("transactionId",transactionId);
            accTransaction = (AccTransaction)query.getSingleResult();
        }
        catch(EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error: "+enfe.getMessage());
            return new AccTransaction();
        }
        catch(NonUniqueResultException nure) {
            System.out.println("\nNon unique result error: "+nure.getMessage());
        }
        
        return accTransaction;
    }
    
    @Override
    public List <BankAccount> getAllBankAccount()
    {
        Query query = entityManager.createQuery("SELECT a FROM BankAccount a");
        return query.getResultList();
    }
    
    @Override
    public Long addNewAccount(String bankAccountNum,String bankAccountPwd,
            String bankAccountType,String bankAccountBalance,String transferDailyLimit,
            Long customerBasicId,Long interestId) {
        BankAccount bankAccount = new BankAccount();
        
        bankAccount.setBankAccountNum(bankAccountNum);
        bankAccount.setBankAccountPwd(bankAccountPwd);
        bankAccount.setBankAccountTyep(bankAccountType);
        bankAccount.setBankAccountBalance(bankAccountBalance);
        bankAccount.setTransferDailyLimit(transferDailyLimit);
        bankAccount.setCustomerBasic(retrieveCustomerBasicById(customerBasicId));
        bankAccount.setInterest(interestSessionLocal.retrieveInterestById(interestId));
        
        entityManager.persist(bankAccount);
        entityManager.flush();
        
        return bankAccount.getBankAccountId();
    }
    
    @Override
    public String deleteAccount(String bankAccountNum){
        Query query = entityManager.createQuery("Select a From BankAccount a Where a.bankAccountNum=:bankAccountNum");
        query.setParameter("bankAccountNum",bankAccountNum);
        List<BankAccount> result = query.getResultList();
        
        if(result.isEmpty()) {
            return "Error! Account does not exist!";
        }
        else {
            BankAccount bankAccount = result.get(0);
            entityManager.remove(bankAccount);

            return "Successfully deleted!";
        }
    }
}
