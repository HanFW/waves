/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.card.session;

import ejb.card.entity.DebitCard;
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
 * @author Jingyuan
 */
@Stateless
public class DebitCardExpirationManagementSessionBean implements DebitCardExpirationManagementSessionBeanLocal {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @PersistenceContext
    private EntityManager em;
    
    @EJB
    private CustomerEmailSessionBeanLocal customerEmailSessionBeanLocal;

    @Override
    public void handleDebitCardExpiration() {
     Query q=em.createQuery("SELECT d from DebitCard d");
     List<DebitCard> allDebitCards=q.getResultList();
     if(!allDebitCards.isEmpty()){
         System.out.println("update the remaining expiration months of all the debit cards");
         for(int i=0;i<allDebitCards.size();i++){
             int remainingExpirationMonths = allDebitCards.get(i).getRemainingExpirationMonths();
             int newRemainingExpirationMonths = remainingExpirationMonths-1;
             allDebitCards.get(i).setRemainingExpirationMonths(newRemainingExpirationMonths);
         }
     }
     
     System.out.println("find all cards whose remaining expiration months is less than 6 months");
     Query query=em.createQuery("SELECT d FROM DebitCard d WHERE d.remainingExpirationMonths <=6 AND d.status=:status");
     String requiredStatus = "activated";
     query.setParameter("status",requiredStatus);
     
     List<DebitCard> findDebitCards=query.getResultList();
     if(!findDebitCards.isEmpty()){
         for(int j=0;j<findDebitCards.size();j++){
             DebitCard debitCard = findDebitCards.get(j);
             CustomerBasic findCustomer = debitCard.getBankAccount().getCustomerBasic();
             String debitCardTypeName = debitCard.getDebitCardType().getDebitCardTypeName();
             String cardNumber = debitCard.getCardNum();
             String expirationDate = debitCard.getCardExpiryDate();
             
             int remainingExpirationMonths = debitCard.getRemainingExpirationMonths();
             
             if(remainingExpirationMonths>0){
                     
             Map emailActions =new HashMap();
             emailActions.put("cardTypeName",debitCardTypeName);
             emailActions.put("cardNumber", cardNumber);
             emailActions.put("expirationTime", expirationDate);
             emailActions.put("remainingMonths",remainingExpirationMonths);
             
             customerEmailSessionBeanLocal.sendEmail(findCustomer, "cardToBeExpired", emailActions);
             }
             else {
                 debitCard.setStatus("close");
             }
                     
         }
     }
     
    }
}
