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
        Query query = em.createQuery("SELECT a FROM LoanRepaymentAccount a WHERE a.loanPayableAccount.accountStatus = :accountStatus OR a.loanPayableAccount.accountStatus = :accountStatus2");
        query.setParameter("accountStatus", "started");
        query.setParameter("accountStatus2", "default");

        List<LoanRepaymentAccount> accounts = query.getResultList();

        for (LoanRepaymentAccount account : accounts) {
            checkLastRepayment(account);
            calculateNewRepayment(account);
            account.setRepaymentMonths(account.getRepaymentMonths() + 1);
        }

        em.flush();
    }

    private void checkLastRepayment(LoanRepaymentAccount account) {
//            if (account.getAccountBalance() > 0) {
//                double newOverdue = account.getAccountBalance();
//                double totalOverdue = (newOverdue + overdueBalance) * 1.05;
//                account.setOverdueBalance(totalOverdue);
//            }
        em.flush();
    }

    private void calculateNewRepayment(LoanRepaymentAccount account) {
        LoanPayableAccount payableAccount = account.getLoanPayableAccount();
        LoanApplication loanApplicaiton = payableAccount.getLoanApplication();
        String interestPackage = loanApplicaiton.getLoanInterestPackage().getPackageName();
        double rate = 0;
        double fees = account.getFees();
        double overdueBalance = account.getOverdueBalance();
        double instalment = 0;
        double principle = payableAccount.getInitialAmount();
        double period = loanApplicaiton.getPeriodSuggested();
        double interest = 0;
        double oldTotalRemaining = payableAccount.getAccountBalance();
        double newTotalRemaining = oldTotalRemaining;

        if (oldTotalRemaining > 0) {
            if (interestPackage.equals("HDB-Fixed")) {
                if (account.getRepaymentMonths() == 0) {
                    rate = 0.018;
                } else if (account.getRepaymentMonths() < 36) {
                    rate = 0.018;
                } else {
                    rate = loanApplicaiton.getLoanInterestPackage().getInterestRate() + 0.012;
                }
            } else if (interestPackage.equals("HDB-Floating")) {
                rate = loanApplicaiton.getLoanInterestPackage().getInterestRate() + 0.013;
            } else if (interestPackage.equals("Private Property-Fixed")) {
                if (account.getRepaymentMonths() == 0) {
                    rate = 0.018;
                } else if (account.getRepaymentMonths() < 36) {
                    rate = 0.018;
                } else {
                    rate = loanApplicaiton.getLoanInterestPackage().getInterestRate() + 0.012;
                }
            } else if (interestPackage.equals("Private Property-Floating")) {
                rate = loanApplicaiton.getLoanInterestPackage().getInterestRate() + 0.0125;
            }

            instalment = (rate / 12 * principle) / (1 - Math.pow((1 + rate / 12), -period));
            System.out.println("*** new month instalment: "+instalment);
            interest = oldTotalRemaining * rate;
            System.out.println("*** new month interest: "+interest);
            newTotalRemaining = oldTotalRemaining - (instalment - interest);
            System.out.println("*** old account balance: "+oldTotalRemaining);
            System.out.println("*** new account balance: "+newTotalRemaining);
            instalment = Math.round(instalment * 100.0) / 100.0;
            account.setInstalment(instalment);
            account.setAccountBalance(instalment + fees + overdueBalance);
        }

        em.flush();
    }
}
