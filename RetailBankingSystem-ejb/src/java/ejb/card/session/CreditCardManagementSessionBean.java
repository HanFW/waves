/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.card.session;

import ejb.card.entity.CreditCard;
import ejb.card.entity.CreditCardType;
import ejb.customer.entity.CustomerBasic;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
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
public class CreditCardManagementSessionBean implements CreditCardManagementSessionBeanLocal {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @PersistenceContext
    private EntityManager em;

    @EJB
    private CreditCardSessionBeanLocal creditCardSessionBeanLocal;

//    @Override
//    public String CancelCreditCard(String creditCardNum) {
//        if (getCardByCardNum(debitCardNum) == null) {
//            return "debit card not exist";
//        } else {
//            CreditCard findCreditCard = getCardByCardNum(debitCardNum);
//            String debitCardPwd = debitCardPassword;
//
//            //check if pwd matches
//            String hashedInputPwd;
//            try {
//                hashedInputPwd = md5Hashing(debitCardPwd + findDebitCard.getCardNum().substring(0, 3));
//
//                if (!findCreditCard.getDebitCardPwd().equals(hashedInputPwd)) {
//                    return "wrong pwd";
//                } else {
//                    BankAccount depositAccount = findDebitCard.getBankAccount();
//                    DebitCardType debitCardType = findDebitCard.getDebitCardType();
//
//                    depositAccount.removeDebitCard(findDebitCard);
//                    debitCardType.removeDebitCard(findDebitCard);
//
//                    em.remove(findDebitCard);
//                    return "success";
//
//                }
//            } catch (NoSuchAlgorithmException ex) {
//                Logger.getLogger(DebitCardManagementSessionBean.class.getName()).log(Level.SEVERE, null, ex);
//            }
//
//        }
//        return null;
//
//    }
    @Override
    public void cancelCreditCardAfterReplacement(Long creditCardId) {
        CreditCard findCreditCard = em.find(CreditCard.class, creditCardId);

        CustomerBasic cb = findCreditCard.getCustomerBasic();
        CreditCardType creditCardType = findCreditCard.getCreditCardType();

        creditCardType.removeCreditCard(findCreditCard);

        em.remove(findCreditCard);
        System.out.println("CreditCardManagementSessionBean: predecessor credit card has been removed from database");
    }

    @Override
    public String reportCreditCardLoss(String creditCardNum, String identificationNum) {

        if (getCardByCardNum(creditCardNum) == null) {
            return "credit card not exist";
        } else {
            CreditCard findCreditCard = getCardByCardNum(creditCardNum);
            System.out.println("!!!!!!!!!!!!!!credit card" + findCreditCard.getCardNum());
            System.out.println("!!!!!!!!!!!!!!credit card holder id" + findCreditCard.getCustomerBasic().getCustomerIdentificationNum());

            if (!findCreditCard.getCustomerBasic().getCustomerIdentificationNum().equals(identificationNum)) {
                return "wrong id";
            } else {
                Long cbId = findCreditCard.getCustomerBasic().getCustomerBasicId();
                Long caId = findCreditCard.getCustomerBasic().getCustomerAdvanced().getCustomerAdvancedId();
                Long creditCardTypeId = findCreditCard.getCreditCardType().getCreditCardTypeId();

                String cardHolderName = findCreditCard.getCardHolderName();
                double limit = findCreditCard.getCreditLimit();
                String expDate = findCreditCard.getCardExpiryDate();
                int remainingMonths = findCreditCard.getRemainingExpirationMonths();
                
//                CustomerBasic cb = findCreditCard.getCustomerBasic();
//                CreditCardType cct = findCreditCard.getCreditCardType();
//
//                cb.removeCreditCard(findCreditCard);
//                cct.removeCreditCard(findCreditCard);
                
                System.out.println(findCreditCard);
                em.remove(findCreditCard);
                System.out.println("!!!!!!!after delete card" + findCreditCard);
                //issue a new debit card
                System.out.println("!!!!!!!!!!!!!!!!!!management session bean ccct ID"+creditCardTypeId);
                creditCardSessionBeanLocal.createNewCardAfterLost(cbId, caId, creditCardTypeId, cardHolderName, limit, expDate, remainingMonths);
                System.out.println("Credit Card management session bean: issue a new card after reporting credit card loss");

                return "success";
            }
        }
    }
    //    @Override
    //    public String requestForCreditCardReplacement(String creditCardNum, String securityCode, String requestForReplacementTime) {
    //        if (getCardByCardNum(creditCardNum) == null) {
    //            return "credit card not exist";
    //        } else {
    //            CreditCard findCreditCard = getCardByCardNum(creditCardNum);
    //            String csc = securityCode;
    //
    //            //check if pwd matches
    //            String hashedInputPwd;
    //            try {
    //                hashedInputPwd = md5Hashing(csc + findCreditCard.getCardNum().substring(0, 3));
    //
    //                if (!findCreditCard.getCardSecurityCode().equals(hashedInputPwd)) {
    //                    return "wrong csc";
    //                } else {
    //                    CustomerBasic cb = findCreditCard.getCustomerBasic();
    //                    CreditCardType creditCardType = findCreditCard.getCreditCardType();
    //
    //                    //store debit card info before removing the card & for later use to issue a new debit card
    //                    
    //                    String cardHolderName = findCreditCard.getCardHolderName();
    //                    String cardTypeName = creditCardType.getCreditCardTypeName();
    //                    String applicationDate = requestForReplacementTime;
    //
    //                    //get the id of the current card and save it as the predecessor of the new debit card issued
    //                    Long predecessorId = findCreditCard.getCardId();
    //
    //                    //issue a new debit card and set a predecessor to
    //                    creditCardSessionBeanLocal.issueDebitCardForCardReplacement(cb, cardHolderName, applicationDate, cardTypeName, predecessorId);
    //                    System.out.println("Debit Card management session bean: issue a new card after requesting for debit card replacement");
    //
    //                    return "success";
    //
    //                }
    //            } catch (NoSuchAlgorithmException ex) {
    //                Logger.getLogger(DebitCardManagementSessionBean.class.getName()).log(Level.SEVERE, null, ex);
    //            }
    //
    //        }
    //        return null;
    //    }

    private CreditCard getCardByCardNum(String cardNum) {
        Query query = em.createQuery("SELECT c FROM CreditCard c WHERE c.cardNum = :cardNum");
        query.setParameter("cardNum", cardNum);

        if (query.getResultList().isEmpty()) {
            return null;
        } else {
            CreditCard findCreditCard = (CreditCard) query.getResultList().get(0);
            return findCreditCard;
        }
    }

    private String md5Hashing(String stringToHash) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        return Arrays.toString(md.digest(stringToHash.getBytes()));
    }
}
