/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.card.session;

import ejb.card.entity.DebitCard;
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
public class DebitCardTransactionSessionBean implements DebitCardTransactionSessionBeanLocal {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")   
    @PersistenceContext
    private EntityManager em;

    @Override
    public void setTransactionLimit(String selectedDebitCard, int newTransactionLimit) {
        System.out.println("DebitCardTransactionSessionBean: setTransactionLimit " + selectedDebitCard);

        String[] debitCardInfo = selectedDebitCard.split("-");
        String debitCardNum = debitCardInfo[1];

        DebitCard findDebitCard = getCardByCardNum(debitCardNum);
        findDebitCard.setTransactionLimit(newTransactionLimit);
        em.flush();

    }

    @Override
    public int getTransactionLimitByDebitCardNum(String selectedDebitCard) {
        String[] debitCardInfo = selectedDebitCard.split("-");
        String debitCardNum = debitCardInfo[1];

        DebitCard findDebitCard = getCardByCardNum(debitCardNum);
        int transactionLimit = findDebitCard.getTransactionLimit();
        
        return transactionLimit;
    }

    private DebitCard getCardByCardNum(String cardNum) {
        Query query = em.createQuery("SELECT d FROM DebitCard d WHERE d.debitCardNum = :cardNum");
        query.setParameter("cardNum", cardNum);

        if (query.getResultList().isEmpty()) {
            return null;
        } else {
            DebitCard findDebitCard = (DebitCard) query.getResultList().get(0);
            return findDebitCard;
        }
    }

}
