/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.card.session;

import ejb.card.entity.DebitCard;
import ejb.card.entity.DebitCardType;
import ejb.deposit.entity.BankAccount;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.List;
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
public class DebitCardSessionBean implements DebitCardSessionBeanLocal {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @EJB
    private DebitCardSessionBeanLocal debitCardSessionBeanLocal;

    @PersistenceContext
    private EntityManager em;

    @Override
    public String createDebitCard(String bankAccountNum, String cardHolderName, String applicationDate, String cardTypeName) {
        DebitCard debitCard = new DebitCard();
        debitCard.setCardHolderName(cardHolderName);

        BankAccount depositAccount = findDepositAccountByAccountNum(bankAccountNum);
        debitCard.setBankAccount(depositAccount);

        DebitCardType debitCardType = findCardTypeByTypeName(cardTypeName);
        debitCard.setDebitCardType(debitCardType);

        String cardNum = generateCardNum();
        debitCard.setCardNum(cardNum);

        String debitCardSecurityCode = generateCardSecurityCode();
        debitCard.setCardSecurityCode(debitCardSecurityCode);

//        String[] applicationDateToString = changeDateFormat(applicationDate);
        String[] applicationDateToString = applicationDate.split("/");
        String applicationYear = applicationDateToString[2];
        String applicationMonth = applicationDateToString[1];

        int expiryYearToInt = Integer.valueOf(applicationYear) + 5;
        String expiryYear = String.valueOf(expiryYearToInt);
        String debitCardExpiryDate = applicationMonth + "/" + expiryYear;

        debitCard.setCardExpiryDate(debitCardExpiryDate);

        em.persist(debitCard);
        return "success";

    }

    @Override
    public String debitCardNumValiadation(String debitCardNum, String cardHolderName, String debitCardSecurityCode) {
        //check if the debitCard exist by debit card number
        if (getCardByCardNum(debitCardNum) == null) {
            return "debit card not exist";
        } else {
            DebitCard findDebitCard = getCardByCardNum(debitCardNum);
            System.out.println("debug!! card holder name "+findDebitCard.getCardHolderName());
            if (!findDebitCard.getCardHolderName().equals(cardHolderName)) {
                return "cardHolderName not match";
            } else {
                if (!findDebitCard.getCardSecurityCode().equals(debitCardSecurityCode)) {
                    return "csc not match";
                } else {
                    return "valid";
                }
            }//card holder name match

        }//debit card exist
    }

    private BankAccount findDepositAccountByAccountNum(String bankAccountNum) {
        Query query = em.createQuery("SELECT b FROM BankAccount b WHERE b.bankAccountNum = :accountNum");
        query.setParameter("accountNum", bankAccountNum);

        BankAccount findBankAccount = (BankAccount) query.getSingleResult();
        return findBankAccount;
    }

    private String generateCardNum() {
        System.out.println("*");
        System.out.println("****** card/DebitCardSessionBean: generateDebitCardNumber() ******");
        SecureRandom random = new SecureRandom();
        String cardNumber = new BigInteger(48, random).toString(8);

        while (getCardByCardNum(cardNumber) != null) {
            cardNumber = new BigInteger(48, random).toString(8);
        }

        System.out.println("****** card/DebitCardSessionBean: generateDebitCardNumber(): debit card number generated");
        return cardNumber;
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

    private DebitCard getCardBySecurityCode(String cardSecurityCode) {
        Query query = em.createQuery("SELECT d FROM DebitCard d WHERE d.cardSecurityCode = :cardSecurityCode");
        query.setParameter("cardSecurityCode", cardSecurityCode);

        if (query.getResultList().isEmpty()) {
            return null;
        } else {
            DebitCard findDebitCard = (DebitCard) query.getResultList().get(0);
            return findDebitCard;
        }
    }

//change date to string
//    private String[] changeDateFormat(Date date) {
//        String dateToString = date.toString();
//        String[] changedDate = dateToString.split("/");
//        return changedDate;
//    }
    private String generateCardSecurityCode() {
        System.out.println("*");
        System.out.println("****** card/DebitCardSessionBean: generateCardSecurityCode() ******");
        SecureRandom random = new SecureRandom();
        String csc = new BigInteger(9, random).toString(8);

        while (getCardBySecurityCode(csc) != null) {
            csc = new BigInteger(9, random).toString(8);
        }

        System.out.println("****** card/DebitCardSessionBean: generateCardSecurityCode(): debit CSC generated");
        return csc;
    }

    private DebitCardType findCardTypeByTypeName(String cardTypeName) {
        Query query = em.createQuery("SELECT d FROM DebitCardType d WHERE d.debitCardTypeName = :cardTypeName");
        query.setParameter("cardTypeName", cardTypeName);

        if (query.getResultList().isEmpty()) {
            return null;
        } else {
            DebitCardType findDebitCardType = (DebitCardType) query.getResultList().get(0);
            System.out.println("****** card/DebitCardSessionBean: find cardType: " + findDebitCardType);
            return findDebitCardType;
        }
    }

    @Override
    public String checkDebitCardTypeForDepositAccount(String bankAccountNum, String cardTypeName) {
        System.out.println("debug debitCardsOfType "+cardTypeName);
        System.out.println("debug debitCardsOfAccount "+bankAccountNum);
        
        BankAccount depositAccount = findDepositAccountByAccountNum(bankAccountNum);
        System.out.println("debug depositAccount "+depositAccount);
        DebitCardType debitCardType = findCardTypeByTypeName(cardTypeName);
        System.out.println("debug debit card type "+debitCardType);
        System.out.println("debug debitCardsOfType "+debitCardType.getDebitCards());
        System.out.println("debug debitCardsOfAccount"+depositAccount.getDebitCards());
        if (debitCardType.getDebitCards() == null || depositAccount.getDebitCards() == null) {
            return "not existing";
        } else {
            List<DebitCard> debitCardsOfType = debitCardType.getDebitCards();
            List<DebitCard> debitCardsOfAccount = depositAccount.getDebitCards();

            for (int i = 0; i < debitCardsOfAccount.size(); i++) {
                DebitCard debitCard = debitCardsOfAccount.get(i);
                if (debitCardsOfType.contains(debitCard)) {
                    return "existing";
                }
            }

            return "not existing";
        }
    }
}
