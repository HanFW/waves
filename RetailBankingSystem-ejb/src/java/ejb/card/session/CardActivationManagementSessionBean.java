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
public class CardActivationManagementSessionBean implements CardActivationManagementSessionBeanLocal {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
     @PersistenceContext
    private EntityManager em;
    
    @EJB
    private CustomerEmailSessionBeanLocal customerEmailSessionBeanLocal;

    @Override
    public void handleCardActivation() {
     Query q=em.createQuery("SELECT d from DebitCard d");
     List<DebitCard> allDebitCards=q.getResultList();
     if(!allDebitCards.isEmpty()){
         System.out.println("update the remaining activation months of all the debit cards");
         for(int i=0;i<allDebitCards.size();i++){
             int remainingActivationDays = allDebitCards.get(i).getRemainingActivationDays();
             int newRemainingActivationDays = remainingActivationDays-1;
             allDebitCards.get(i).setRemainingActivationDays(newRemainingActivationDays);
         }
     }
     
     System.out.println("find all cards whose activation days is less than 3 days");
     Query query=em.createQuery("SELECT d FROM DebitCard d WHERE d.remainingActivationDays <=3 AND d.status=:status");
     String requiredStatus = "not activated";
     query.setParameter("status",requiredStatus);
     
     List<DebitCard> findDebitCards=query.getResultList();
     if(!findDebitCards.isEmpty()){
         for(int j=0;j<findDebitCards.size();j++){
             DebitCard debitCard = findDebitCards.get(j);
             CustomerBasic findCustomer = debitCard.getBankAccount().getCustomerBasic();
             String debitCardTypeName = debitCard.getDebitCardType().getDebitCardTypeName();
             String cardNumber = debitCard.getCardNum();
             int remainingActivationDays = debitCard.getRemainingActivationDays();
             
             
             if(remainingActivationDays>0){
                     
             Map emailActions =new HashMap();
             emailActions.put("cardTypeName",debitCardTypeName);
             emailActions.put("cardNumber", cardNumber);
             emailActions.put("remainingDays",remainingActivationDays);
             
             customerEmailSessionBeanLocal.sendEmail(findCustomer, "cardToBeActivated", emailActions);
             }
             else {
                 debitCard.setStatus("close");
             }
                     
         }
     }
     
    }
}
