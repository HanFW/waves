/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.card.session;

import javax.ejb.Local;

/**
 *
 * @author aaa
 */
@Local
public interface CreditCardManagementSessionBeanLocal {
    public void cancelCreditCardAfterReplacement(Long creditCardId);
    public String reportCreditCardLoss(String creditCardNum, String identificationNum);
    public void replaceDamagedCreditCard(String creditCardNum);
}
