/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.card.session;

import ejb.card.entity.CreditCard;
import ejb.card.entity.PrincipalCard;
import ejb.card.entity.SupplementaryCard;
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
    public void addSupplementaryCard(Long principalCardId, String cardHolderName, String dateOfBirth, String relationship, String identificationNum);
    public void createNewCardAfterDamage(Long cbId, Long caId, Long creditCardTypeId, String cardHolderName, double creditLimit, String expDate, int remainingMonths, List<SupplementaryCard> supplCards, Long predecessorId);
    public void createNewCardAfterLost(Long cbId, Long caId, Long creditCardTypeId, String cardHolderName, double creditLimit, String expDate, int remainingMonths,List<SupplementaryCard> supplCards);
    public String findTypeNameById(Long cardTypeId);
    public List<String> getAllDebitCards(Long customerId);
    public List<String> getAllNonActivatedCreditCards(Long customerId);
    public List<String> getAllActivatedCreditCards(Long customerId);
    public List<String> getAllPlatinumCards(Long customerId);
    public List<PrincipalCard> getAllPendingCreditCards();
    public List<SupplementaryCard> getAllPendingSupplementaryCards();
    public String creditCardNumValiadation(String creditCardNum, String cardHolderName, String creditCardSecurityCode);
    public CreditCard getCardByCardNum(String cardNum);
    public CreditCard getCardByCardId (Long cardId);
    public double[] getCreditLimitMaxInterval();
    public double getCreditLimitRiskRatio();
    public double[] getCreditLimitSuggestedInterval();
    public void approveRequest(Long creditCardId, double creditLimit);
    public void rejectRequest(Long creditCardId);
    
}
