/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.loan.session;

import javax.ejb.Local;

/**
 *
 * @author hanfengwei
 */
@Local
public interface LoanRepaymentSessionBeanLocal {
    public void makeMonthlyRepayment(String depositAccount, String repaymentAccount, double amount);
}
