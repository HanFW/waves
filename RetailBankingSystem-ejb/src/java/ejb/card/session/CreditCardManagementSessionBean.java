/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.card.session;

import ejb.card.entity.CreditCard;
import ejb.card.entity.CreditCardType;
import ejb.card.entity.SupplementaryCard;
import ejb.customer.entity.CustomerBasic;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    @Override
    public String cancelCreditCard(String creditCardNum, String securityCode) {
        if (getCardByCardNum(creditCardNum) == null) {
            return "credit card not exist";
        } else {
            CreditCard findCreditCard = getCardByCardNum(creditCardNum);
            System.out.println("##############outstanding limit" + findCreditCard.getOutstandingBalance());

            //check if pwd matches
            String hashedInputPwd;
            try {
                hashedInputPwd = md5Hashing(securityCode + findCreditCard.getCardNum().substring(0, 3));

                if (!findCreditCard.getCardSecurityCode().equals(hashedInputPwd)) {
                    return "wrong pwd";
                } else if (findCreditCard.getOutstandingBalance() != 0.0){
                    return "credit limit unpaid";
                } else {
                    findCreditCard.setStatus("cancel");
                    
                    return "success";
                }
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(DebitCardManagementSessionBean.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        return null;

    }
    
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
                List<SupplementaryCard> supplCards = findCreditCard.getSupplementaryCard();

//                CustomerBasic cb = findCreditCard.getCustomerBasic();
//                CreditCardType cct = findCreditCard.getCreditCardType();
//
//                cb.removeCreditCard(findCreditCard);
//                cct.removeCreditCard(findCreditCard);
                System.out.println(findCreditCard);
                em.remove(findCreditCard);
                System.out.println("!!!!!!!after delete card" + findCreditCard);

                System.out.println("!!!!!!!!!!!!!!!!!!management session bean ccct ID" + creditCardTypeId);
                creditCardSessionBeanLocal.createNewCardAfterLost(cbId, caId, creditCardTypeId, cardHolderName, limit, expDate, remainingMonths, supplCards);
                System.out.println("Credit Card management session bean: issue a new card after reporting credit card loss");

                return "success";
            }
        }
    }

    @Override
    public void replaceDamagedCreditCard(String creditCardNum) {

        CreditCard findCreditCard = getCardByCardNum(creditCardNum);

        Long cbId = findCreditCard.getCustomerBasic().getCustomerBasicId();
        Long caId = findCreditCard.getCustomerBasic().getCustomerAdvanced().getCustomerAdvancedId();
        Long creditCardTypeId = findCreditCard.getCreditCardType().getCreditCardTypeId();
        String cardHolderName = findCreditCard.getCardHolderName();
        String expDate = findCreditCard.getCardExpiryDate();
        CreditCardType cardTypeName = findCreditCard.getCreditCardType();
        double limit = findCreditCard.getCreditLimit();
        int remainingMonths = findCreditCard.getRemainingExpirationMonths();
        List<SupplementaryCard> supplCards = findCreditCard.getSupplementaryCard();
        Long predecessorId = findCreditCard.getCardId();

        creditCardSessionBeanLocal.createNewCardAfterDamage(cbId, caId, creditCardTypeId, cardHolderName, limit, expDate, remainingMonths, supplCards, predecessorId);

        System.out.println("Credit Card management session bean: issue new card to replace damaged card");

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
