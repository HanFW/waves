/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.card.session;

import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author aaa
 */
@Local
public interface CreditCardSessionBeanLocal {

    public void createCreditCard(Long newCustomerBasicId, Long newCustomerAdvancedId, Long creditCardTypeId,
            String cardHolderName, String hasCreditLimit, double creditLimit, String applicationDate);
    public void createNewCardAfterLost(Long cbId, Long caId, Long creditCardTypeId, String cardHolderName, double creditLimit, String expDate, int remainingMonths);
    public String findTypeNameById(Long cardTypeId);
    public List<String> getAllDebitCards(Long customerId);
    public List<String> getAllNonActivatedCreditCards(Long customerId);
    public List<String> getAllActivatedCreditCards(Long customerId);
    public String creditCardNumValiadation(String creditCardNum, String cardHolderName, String creditCardSecurityCode);
}
