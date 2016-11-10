/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.card.session;

import ejb.card.entity.PrincipalCard;
import ejb.deposit.entity.BankAccount;
import javax.ejb.Local;

/**
 *
 * @author hanfengwei
 */
@Local
public interface CreditCardRepaymentSessionBeanLocal {
     public Long makeCreditCardRepayment(BankAccount depositAccount, PrincipalCard card, double amount);
     public void calculateCreditCardInterest();
}
