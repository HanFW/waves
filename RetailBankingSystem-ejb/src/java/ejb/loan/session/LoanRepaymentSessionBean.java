/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.loan.session;

import ejb.deposit.entity.BankAccount;
import ejb.deposit.session.BankAccountSessionBeanLocal;
import ejb.loan.entity.LoanPayableAccount;
import ejb.loan.entity.LoanRepaymentAccount;
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
    private BankAccountSessionBeanLocal bankAccountSessionBeanLocal;
    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;

    @Override
    public void makeMonthlyRepayment(String depositAccount, String repaymentAccount, double amount) {
        BankAccount deposit = bankAccountSessionBeanLocal.retrieveBankAccountByNum(depositAccount);
        Double newBalance = Double.valueOf(deposit.getAvailableBankAccountBalance()) - amount;
        deposit.setAvailableBankAccountBalance(newBalance.toString());
        
        LoanRepaymentAccount loanRepaymentAccount = getRepaymentAccountByAccountNum(repaymentAccount);
        loanRepaymentAccount.setAccountBalance(loanRepaymentAccount.getAccountBalance()-amount);
        LoanPayableAccount loanPayableAccount = loanRepaymentAccount.getLoanPayableAccount();
        loanPayableAccount.setAccountBalance(loanPayableAccount.getAccountBalance()-amount);
        
        em.flush();
    }

    private LoanRepaymentAccount getRepaymentAccountByAccountNum(String accountNum) {
        Query query = em.createQuery("Select a From LoanRepaymentAccount a Where a.accountNumber=:accountNum");
        query.setParameter("accountNum", accountNum);
        
        List resultList = query.getResultList();
        return (LoanRepaymentAccount) resultList.get(0);
    }
}
