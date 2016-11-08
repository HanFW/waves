/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.loan.session;

import ejb.customer.entity.CustomerBasic;
import ejb.deposit.entity.BankAccount;
import ejb.loan.entity.CashlineApplication;
import ejb.loan.entity.LoanApplication;
import ejb.loan.entity.LoanPayableAccount;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author hanfengwei
 */
@Stateless
public class LoanManagementSessionBean implements LoanManagementSessionBeanLocal {

    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;

    @Override
    public List<LoanPayableAccount> getLoanPayableAccountByIdentification(String identification) {
        List<LoanPayableAccount> accounts;
        
        Query query = em.createQuery("SELECT a FROM LoanPayableAccount a WHERE a.loanApplication.customerBasic.customerIdentificationNum = :identification AND a.accountStatus = :status");
        query.setParameter("identification", identification);
        query.setParameter("status", "started");
        accounts = query.getResultList();
        
        Query query2 = em.createQuery("SELECT a FROM LoanPayableAccount a WHERE a.loanApplication.customerBasic.customerIdentificationNum = :identification AND a.accountStatus = :status");
        query2.setParameter("identification", identification);
        query2.setParameter("status", "default");
        accounts.addAll(query2.getResultList());

        Query query3 = em.createQuery("SELECT a FROM LoanPayableAccount a WHERE a.loanApplication.customerBasic.customerIdentificationNum = :identification AND a.accountStatus = :status");
        query3.setParameter("identification", identification);
        query3.setParameter("status", "ended");
        accounts.addAll(query3.getResultList());
        
        Query query4 = em.createQuery("SELECT a FROM LoanPayableAccount a WHERE a.loanApplication.customerBasic.customerIdentificationNum = :identification AND a.accountStatus = :status");
        query4.setParameter("identification", identification);
        query4.setParameter("status", "completed");
        accounts.addAll(query4.getResultList());
        
        Query query5 = em.createQuery("SELECT a FROM LoanPayableAccount a WHERE a.loanApplication.customerBasic.customerIdentificationNum = :identification AND a.accountStatus = :status");
        query5.setParameter("identification", identification);
        query5.setParameter("status", "bankrupt");
        accounts.addAll(query5.getResultList());

        return accounts;
    }

    @Override
    public List<LoanApplication> getLoanApplicationsByIdentification(String identification) {
        System.out.println("get identification: " + identification);
        Query query = em.createQuery("SELECT a FROM LoanApplication a WHERE a.customerBasic.customerIdentificationNum = :identification");
        query.setParameter("identification", identification);

        List<LoanApplication> applications = query.getResultList();

        return applications;
    }

    @Override
    public LoanPayableAccount getLoanPayableAccountById(Long loanId) {
        LoanPayableAccount account = em.find(LoanPayableAccount.class, loanId);
        return account;
    }

    
    @Override
    public List<CashlineApplication> getCashlineApplicationsByIdentification(String identification){
        System.out.println("get identification: " + identification);
        Query query = em.createQuery("SELECT a FROM CashlineApplication a WHERE a.customerBasic.customerIdentificationNum = :identification");
        query.setParameter("identification", identification);
        
        List<CashlineApplication> resultList = query.getResultList();
        
        return resultList;
    }
    
    @Override
    public List<BankAccount> getCustomerDepositAccounts(Long customerId){
        CustomerBasic customer = em.find(CustomerBasic.class, customerId);
        return customer.getBankAccount();
    }
}
