/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.card.session;

import ejb.card.entity.DebitCard;
import ejb.card.entity.DebitCardType;
import ejb.deposit.entity.BankAccount;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class DebitCardManagementSessionBean implements DebitCardManagementSessionBeanLocal {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @PersistenceContext
    private EntityManager em;

    @EJB
    private DebitCardSessionBeanLocal debitCardSessionBeanLocal;

    @Override
    public String CancelDebitCard(String debitCardNum, String debitCardPassword) {
        if (getCardByCardNum(debitCardNum) == null) {
            return "debit card not exist";
        } else {
            DebitCard findDebitCard = getCardByCardNum(debitCardNum);
            String debitCardPwd = debitCardPassword;

            //check if pwd matches
            String hashedInputPwd;
            try {
                hashedInputPwd = md5Hashing(debitCardPwd + findDebitCard.getCardNum().substring(0, 3));

                if (!findDebitCard.getDebitCardPwd().equals(hashedInputPwd)) {
                    return "wrong pwd";
                } else {
//                    BankAccount depositAccount = findDebitCard.getBankAccount();
//                    DebitCardType debitCardType = findDebitCard.getDebitCardType();
//
//                    depositAccount.removeDebitCard(findDebitCard);
//                    debitCardType.removeDebitCard(findDebitCard);
//
//                    em.remove(findDebitCard);
                    findDebitCard.setStatus("cancel");
                    return "success";

                }
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(DebitCardManagementSessionBean.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        return null;

    }

    @Override
    public void CancelDebitCardAfterReplacement(Long debitCardId) {
        DebitCard findDebitCard = em.find(DebitCard.class, debitCardId);

        BankAccount depositAccount = findDebitCard.getBankAccount();
        DebitCardType debitCardType = findDebitCard.getDebitCardType();

        depositAccount.removeDebitCard(findDebitCard);
        debitCardType.removeDebitCard(findDebitCard);

        em.remove(findDebitCard);
        System.out.println("DebitCardManagementSessionBean: predecessor debit card has been removed from database");
    }

    @Override
    public String reportDebitCardLoss(String debitCardNum, String debitCardPassword, String reportLossTime) {

        if (getCardByCardNum(debitCardNum) == null) {
            return "debit card not exist";
        } else {
            DebitCard findDebitCard = getCardByCardNum(debitCardNum);
            String debitCardPwd = debitCardPassword;

            //check if pwd matches
            String hashedInputPwd;
            try {
                hashedInputPwd = md5Hashing(debitCardPwd + findDebitCard.getCardNum().substring(0, 3));

                if (!findDebitCard.getDebitCardPwd().equals(hashedInputPwd)) {
                    return "wrong pwd";
                } else {
                    //cancel debit card
                    BankAccount depositAccount = findDebitCard.getBankAccount();
                    DebitCardType debitCardType = findDebitCard.getDebitCardType();

                    //store debit card info before removing the card & for later use to issue a new debit card
                    String bankAccountNum = depositAccount.getBankAccountNum();
                    String cardHolderName = findDebitCard.getCardHolderName();
                    String cardTypeName = debitCardType.getDebitCardTypeName();
                    String expDate = findDebitCard.getCardExpiryDate();
                    int remainingMonths = findDebitCard.getRemainingExpirationMonths();
                    int transactionLimit = findDebitCard.getTransactionLimit();

                    depositAccount.removeDebitCard(findDebitCard);
                    debitCardType.removeDebitCard(findDebitCard);

                    em.remove(findDebitCard);

                    //issue a new debit card
                    debitCardSessionBeanLocal.lostDebitCardRecreate(bankAccountNum, cardHolderName, expDate, remainingMonths, transactionLimit, cardTypeName);
                    System.out.println("Debit Card management session bean: issue a new card after reporting debit card loss");

                    return "success";

                }
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(DebitCardManagementSessionBean.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        return null;

    }

    @Override
    public void replaceDamagedDebitCard(String debitCardNum) {

        DebitCard findDebitCard = getCardByCardNum(debitCardNum);

        BankAccount depositAccount = findDebitCard.getBankAccount();
        DebitCardType debitCardType = findDebitCard.getDebitCardType();

        //store debit card info before removing the card & for later use to issue a new debit card
        String bankAccountNum = depositAccount.getBankAccountNum();
        String cardHolderName = findDebitCard.getCardHolderName();
        String expDate = findDebitCard.getCardExpiryDate();
        String cardTypeName = debitCardType.getDebitCardTypeName();
        int remainingMonths = findDebitCard.getRemainingExpirationMonths();
        int transactionLimit = findDebitCard.getTransactionLimit();
        Long predecessorId = findDebitCard.getCardId();

        debitCardSessionBeanLocal.replaceDebitCard(bankAccountNum, cardHolderName, expDate, remainingMonths, transactionLimit, cardTypeName, predecessorId);

        System.out.println("Debit Card management session bean: issue new card to replace damaged card");

    }

    @Override
    public String requestForDebitCardReplacement(String debitCardNum, String debitCardPassword, String requestForReplacementTime) {
        if (getCardByCardNum(debitCardNum) == null) {
            return "debit card not exist";
        } else {
            DebitCard findDebitCard = getCardByCardNum(debitCardNum);
            String debitCardPwd = debitCardPassword;

            //check if pwd matches
            String hashedInputPwd;
            try {
                hashedInputPwd = md5Hashing(debitCardPwd + findDebitCard.getCardNum().substring(0, 3));

                if (!findDebitCard.getDebitCardPwd().equals(hashedInputPwd)) {
                    return "wrong pwd";
                } else {
                    //get debit card info
                    BankAccount depositAccount = findDebitCard.getBankAccount();
                    DebitCardType debitCardType = findDebitCard.getDebitCardType();

                    //store debit card info before removing the card & for later use to issue a new debit card
                    String bankAccountNum = depositAccount.getBankAccountNum();
                    String cardHolderName = findDebitCard.getCardHolderName();
                    String cardTypeName = debitCardType.getDebitCardTypeName();
                    String applicationDate = requestForReplacementTime;

                    //get the id of the current card and save it as the predecessor of the new debit card issued
                    Long predecessorId = findDebitCard.getCardId();

                    //issue a new debit card and set a predecessor to
                    debitCardSessionBeanLocal.issueDebitCardForCardReplacement(bankAccountNum, cardHolderName, applicationDate, cardTypeName, predecessorId);
                    System.out.println("Debit Card management session bean: issue a new card after requesting for debit card replacement");

                    return "success";

                }
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(DebitCardManagementSessionBean.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        return null;
    }

    private DebitCard getCardByCardNum(String cardNum) {
        Query query = em.createQuery("SELECT d FROM DebitCard d WHERE d.cardNum = :cardNum");
        query.setParameter("cardNum", cardNum);

        if (query.getResultList().isEmpty()) {
            return null;
        } else {
            DebitCard findDebitCard = (DebitCard) query.getResultList().get(0);
            return findDebitCard;
        }
    }

    private String md5Hashing(String stringToHash) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        return Arrays.toString(md.digest(stringToHash.getBytes()));
    }

}
