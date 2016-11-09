/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.loan.session;

import ejb.deposit.entity.BankAccount;
import ejb.loan.entity.CashlineApplication;
import ejb.loan.entity.LoanApplication;
import ejb.loan.entity.LoanPayableAccount;
import ejb.loan.entity.LoanRepaymentTransaction;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author hanfengwei
 */
@Local
public interface LoanManagementSessionBeanLocal {

    List<LoanPayableAccount> getLoanPayableAccountByIdentification(String identification);
    public List<LoanApplication> getLoanApplicationsByIdentification(String identification);
    public LoanPayableAccount getLoanPayableAccountById(Long loanId);
    public List<CashlineApplication> getCashlineApplicationsByIdentification(String identification);
    public List<BankAccount> getCustomerDepositAccounts(Long customerId);
    public List<LoanRepaymentTransaction> getRepaymentHistory(Long accountId);
    public void setRecurringLoanServingAccount(String accountNum, Long repaymentAccountId);
    public void deleteRecurringLoanServingAccount(Long repaymentAccountId);
}
