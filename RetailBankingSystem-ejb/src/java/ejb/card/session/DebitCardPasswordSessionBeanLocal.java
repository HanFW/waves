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
public interface DebitCardPasswordSessionBeanLocal {

    //for customer who forgets debit card pwd
    public void setPassword(String debitCardPwd, String debitCardNum);
    
    //for customer who wants to change debit card pwd
    public String changePassword(String currentDebitCardPwd, String DebitCardPwd, String debitCardNum);
}
