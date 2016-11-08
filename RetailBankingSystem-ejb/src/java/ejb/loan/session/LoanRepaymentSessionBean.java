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
    public Long makeMonthlyRepayment(BankAccount depositAccount, LoanRepaymentAccount repaymentAccount, double amount) {
        Double newBalance = Double.valueOf(depositAccount.getAvailableBankAccountBalance()) - amount;
        depositAccount.setAvailableBankAccountBalance(newBalance.toString());

        repaymentAccount.setAccountBalance(repaymentAccount.getAccountBalance() - amount);
        LoanPayableAccount loanPayableAccount = repaymentAccount.getLoanPayableAccount();
        loanPayableAccount.setAccountBalance(loanPayableAccount.getAccountBalance() - amount);

        em.flush();
        return Long.valueOf("1");
    }

    @Override
    public LoanRepaymentAccount getRepaymentAccountByAccountNum(String accountNum) {
        Query query = em.createQuery("Select a From LoanRepaymentAccount a Where a.accountNumber=:accountNum");
        query.setParameter("accountNum", accountNum);

        List resultList = query.getResultList();
        return (LoanRepaymentAccount) resultList.get(0);
    }
}
