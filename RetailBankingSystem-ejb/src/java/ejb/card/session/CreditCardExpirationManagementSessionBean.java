/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.card.session;

import ejb.card.entity.CreditCard;
import ejb.customer.entity.CustomerBasic;
import ejb.infrastructure.session.CustomerEmailSessionBeanLocal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author aaa
 */
@Stateless
public class CreditCardExpirationManagementSessionBean implements CreditCardExpirationManagementSessionBeanLocal {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @PersistenceContext
    private EntityManager em;
    
    @EJB
    private CustomerEmailSessionBeanLocal customerEmailSessionBeanLocal;

    @Override
    public void handleCreditCardExpiration() {
     Query q=em.createQuery("SELECT c from CreditCard c");
     List<CreditCard> allCreditCards=q.getResultList();
     if(!allCreditCards.isEmpty()){
         System.out.println("update the remaining expiration months of all the credit cards");
         for(int i=0;i<allCreditCards.size();i++){
             int remainingExpirationMonths = allCreditCards.get(i).getRemainingExpirationMonths();
             int newRemainingExpirationMonths = remainingExpirationMonths-1;
             allCreditCards.get(i).setRemainingExpirationMonths(newRemainingExpirationMonths);
         }
     }
     
     System.out.println("find all cards whose remaining expiration months is less than 6 months");
     Query query=em.createQuery("SELECT c from CreditCard c WHERE c.remainingExpirationMonths <=6 AND c.status=:status");
     String requiredStatus = "Activated";
     query.setParameter("status",requiredStatus);
     
     List<CreditCard> findCreditCard=query.getResultList();
     if(!findCreditCard.isEmpty()){
         for(int j=0;j<findCreditCard.size();j++){
             CreditCard creditCard = findCreditCard.get(j);
             CustomerBasic findCustomer = creditCard.getCustomerBasic();
             String creditCardTypeName = creditCard.getCreditCardType().getCreditCardTypeName();
             String cardNumber = creditCard.getCardNum();
             String expirationDate = creditCard.getCardExpiryDate();
             
             int remainingExpirationMonths = creditCard.getRemainingExpirationMonths();
             
             if(remainingExpirationMonths>0){
                     
             Map emailActions =new HashMap();
             emailActions.put("cardTypeName",creditCardTypeName);
             emailActions.put("cardNumber", cardNumber);
             emailActions.put("expirationTime", expirationDate);
             emailActions.put("remainingMonths",remainingExpirationMonths);
             
             customerEmailSessionBeanLocal.sendEmail(findCustomer, "cardToBeExpired", emailActions);
             }
             else {
                 creditCard.setStatus("close");
             }
                     
         }
     }
     
    }
}
