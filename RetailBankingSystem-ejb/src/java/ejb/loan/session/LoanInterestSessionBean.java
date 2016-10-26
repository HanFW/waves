/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.loan.session;

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
    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;
    
    
    @Override
    public void calculateInstalment(){
        Query query = em.createQuery("SELECT a FROM LoanRepaymentAccount a WHERE a.loanPayableAccount.accountStatus = :bankAccountStatus");
        query.setParameter("bankAccountStatus", "Active");
    }
}
