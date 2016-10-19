/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.card.session;

import javax.ejb.Local;

/**
 *
 * @author Jingyuan
 */
@Local
public interface DebitCardTransactionSessionBeanLocal {
    
   public void setTransactionLimit(String selectedDebitCard, int newTransactionLimit);  
   
   public int getTransactionLimitByDebitCardNum(String selectedDebitCard);
}
