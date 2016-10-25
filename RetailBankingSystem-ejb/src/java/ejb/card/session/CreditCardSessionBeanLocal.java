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
public interface CreditCardSessionBeanLocal {

    public void createCreditCard(Long newCustomerBasicId, Long newCustomerAdvancedId, Long creditCardTypeId,
            String cardHolderName, String hasCreditLimit, double creditLimit, String applicationDate);
}
