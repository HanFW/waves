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
public interface DebitCardManagementSessionBeanLocal {
    public String CancelDebitCard(String debitCardNum, String debitCardPwd);
    
    public void CancelDebitCardAfterReplacement(Long debitCardId);
    
    public void replaceDamagedDebitCard(String debitCardNum);
    
    public String reportDebitCardLoss(String debitCardNum, String debitCardPwd, String reportLossTime);
    
    public String requestForDebitCardReplacement(String debitCardNum, String debitCardPWd, String requestForReplacementTime);
    
}
