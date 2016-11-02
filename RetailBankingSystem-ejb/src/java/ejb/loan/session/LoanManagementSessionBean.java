/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.loan.session;

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
        Query query = em.createQuery("SELECT a FROM LoanPayableAccount a WHERE a.loanApplication.customerBasic.customerIdentificationNum = :identification AND a.accountStatus = :status");
        query.setParameter("identification", identification);
        query.setParameter("status", "started");
        
        List<LoanPayableAccount> accounts = query.getResultList();
        
        return accounts;
    }
    
    @Override
    public List<LoanApplication> getLoanApplicationsByIdentification(String identification){
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
}
