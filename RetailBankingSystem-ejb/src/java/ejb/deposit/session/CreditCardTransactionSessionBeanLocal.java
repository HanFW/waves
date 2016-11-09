/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.deposit.session;

import javax.ejb.Local;

/**
 *
 * @author Jingyuan
 */
@Local
public interface CreditCardTransactionSessionBeanLocal {
    public void addCreditCardTransaction(String cardNum, double transactionAmt,String merchantName);
    
    public void addNewTransaction(String transactionDate,String transactionCode,String transactionRef,
            String accountDebit,String accountCredit,Long transactionDateMilis,Long principalCardId);
}
