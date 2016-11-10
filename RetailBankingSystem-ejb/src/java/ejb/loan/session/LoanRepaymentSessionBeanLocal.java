/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.loan.session;

import ejb.deposit.entity.BankAccount;
import ejb.loan.entity.LoanPayableAccount;
import ejb.loan.entity.LoanRepaymentAccount;
import javax.ejb.Local;

/**
 *
 * @author hanfengwei
 */
@Local
public interface LoanRepaymentSessionBeanLocal {
    public Long makeMonthlyRepayment(BankAccount depositAccount, LoanRepaymentAccount repaymentAccount, double amount);
    public LoanRepaymentAccount getRepaymentAccountByAccountNum(String accountNum);
    public void addLoanRepaymentTransaction(LoanRepaymentAccount repaymentAccount, double amount, String description);
    public void addLoanPayableTransaction(LoanPayableAccount payableAccount, double amount);
}
