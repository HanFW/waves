/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.card.session;

import ejb.card.entity.CreditCard;
import ejb.card.entity.PrincipalCard;
import ejb.card.entity.SupplementaryCard;
import ejb.card.session.CreditCardSessionBeanLocal;
import ejb.card.entity.CreditCardTransaction;
import java.util.Calendar;
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
public class CreditCardTransactionSessionBean implements CreditCardTransactionSessionBeanLocal {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @EJB
    private CreditCardSessionBeanLocal creditCardSessionBeanLocal;

    @PersistenceContext
    private EntityManager em;

    @Override
    public void addCreditCardTransaction(String cardNum, double transactionAmt, String merchantName) {
        PrincipalCard principalCard = checkPrincipalCard(cardNum);
        Long id=principalCard.getCardId();

        Calendar cal = Calendar.getInstance();
        String transactionDate = cal.getTime().toString();
        Long transactionDateMilis = cal.getTimeInMillis();
        String transactionCode = merchantName;
        String debitAmt = String.valueOf(transactionAmt);
        
        addNewTransaction(transactionDate,transactionCode,"",debitAmt,"",transactionDateMilis,id);
    }

    @Override
    public void addNewTransaction(String transactionDate, String transactionCode, String transactionRef,
            String accountDebit, String accountCredit, Long transactionDateMilis, Long principalCardId) {
        PrincipalCard card = em.find(PrincipalCard.class, principalCardId);

        CreditCardTransaction transaction = new CreditCardTransaction();
        transaction.setTransactionDate(transactionDate);
        transaction.setTransactionCode(transactionCode);
        transaction.setTransactionRef(transactionRef);
        transaction.setAccountDebit(accountDebit);
        transaction.setAccountCredit(accountCredit);
        transaction.setTransactionDateMilis(transactionDateMilis);
        transaction.setPrincipalCard(card);

        em.persist(transaction);
        card.addTransaction(transaction);
        em.flush();
    }

    private PrincipalCard checkPrincipalCard(String cardNum) {
        Query query = em.createQuery("SELECT c FROM CreditCard c WHERE c.cardNum = :cardNum");
        query.setParameter("cardNum", cardNum);

        PrincipalCard principalCard = new PrincipalCard();

        if (!query.getResultList().isEmpty()) {
            CreditCard card = (CreditCard) query.getResultList().get(0);
            if (card.getCardType().equals("PrincipalCard")) {
                principalCard = (PrincipalCard) card;
                return principalCard;
            } else {
                SupplementaryCard supplementaryCard = (SupplementaryCard) card;
                principalCard = supplementaryCard.getPrincipalCard();
            }

        }
        return principalCard;

    }
}
