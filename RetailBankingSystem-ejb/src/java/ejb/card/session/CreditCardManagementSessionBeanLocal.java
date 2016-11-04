/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.card.session;

import ejb.card.entity.PrincipalCard;
import javax.ejb.Local;

/**
 *
 * @author aaa
 */
@Local
public interface CreditCardManagementSessionBeanLocal {
    public void cancelCreditCardAfterReplacement(Long creditCardId);
    public void cancelSupplementaryCard (Long cardId);
    public String reportCreditCardLoss(String creditCardNum, String identificationNum);
    public void replaceDamagedCreditCard(String creditCardNum);
    public String cancelCreditCard(String creditCardNum, String securityCode);
    public PrincipalCard getPrincipalByCardNum (String cardNum);
}
