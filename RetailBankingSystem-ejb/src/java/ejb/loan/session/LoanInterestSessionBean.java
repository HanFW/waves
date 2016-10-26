/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.loan.session;

import ejb.loan.entity.LoanApplication;
import ejb.loan.entity.LoanPayableAccount;
import ejb.loan.entity.LoanRepaymentAccount;
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
public class LoanInterestSessionBean implements LoanInterestSessionBeanLocal {

    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;

    @Override
    public void calculateInstalment() {
        System.out.println("****** loan/LoanInterestSessionBean: calculateInstalment() ******");
        Query query = em.createQuery("SELECT a FROM LoanRepaymentAccount a WHERE a.loanPayableAccount.accountStatus = :accountStatus");
        query.setParameter("accountStatus", "started");

        List<LoanRepaymentAccount> accounts = query.getResultList();

        for (LoanRepaymentAccount account : accounts) {
            LoanPayableAccount payableAccount = account.getLoanPayableAccount();
            LoanApplication loanApplicaiton = payableAccount.getLoanApplication();
            String interestPackage = loanApplicaiton.getLoanInterestPackage().getPackageName();
            double instalment;

            if (interestPackage.equals("HDB-Fixed")) {
                if (account.getRepaymentMonths() == 0) {
                    double rate = 1.8;
                    double principle = payableAccount.getInitialAmount();
                    double period = loanApplicaiton.getPeriodSuggested();
                    instalment = (rate / 12 * principle) / (1 - Math.pow((1 + rate / 12), -period));
                    account.setInstalment(instalment);
                } else if (account.getRepaymentMonths() < 36) {
                    double rate = 1.8;
                    double principle = payableAccount.getInitialAmount();
                    double period = loanApplicaiton.getPeriodSuggested();
                    instalment = (rate / 12 * principle) / (1 - Math.pow((1 + rate / 12), -period));
                    account.setInstalment(instalment);
                } else {
                    double rate = loanApplicaiton.getLoanInterestPackage().getInterestRate() + 1.2;
                    double principle = payableAccount.getInitialAmount();
                    double period = loanApplicaiton.getPeriodSuggested();
                    instalment = (rate / 12 * principle) / (1 - Math.pow((1 + rate / 12), -period));
                    account.setInstalment(instalment);
                }
            } else if (interestPackage.equals("HDB-Floating")) {
                double rate = loanApplicaiton.getLoanInterestPackage().getInterestRate() + 1.3;
                double principle = payableAccount.getInitialAmount();
                double period = loanApplicaiton.getPeriodSuggested();
                instalment = (rate / 12 * principle) / (1 - Math.pow((1 + rate / 12), -period));
                account.setInstalment(instalment);
            }
        }
        
        em.flush();
    }
}
