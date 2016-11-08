/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.wealth.session;

import javax.ejb.Local;

/**
 *
 * @author Jingyuan
 */
@Local
public interface CreateFinancialGoalSessionBeanLocal {
    public void createPortfolio(Long id, double startingBalance,double monthlyContribution,int contributeDuration, int investmentPeriod, double amountExpected);
}
