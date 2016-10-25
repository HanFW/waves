/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.card.session;

import ejb.card.entity.CreditCard;
import ejb.card.entity.CreditCardType;
import ejb.customer.entity.CustomerAdvanced;
import ejb.customer.entity.CustomerBasic;
import static ejb.customer.entity.CustomerBasic_.creditCard;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Random;
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
public class CreditCardSessionBean implements CreditCardSessionBeanLocal {

    @EJB
    private CreditCardSessionBeanLocal creditCardSessionLocal;

    @PersistenceContext
    private EntityManager em;
    
    @Override
    public void createCreditCard(Long newCustomerBasicId, Long newCustomerAdvancedId, Long creditCardTypeId, String cardHolderName, String hasCreditLimit, double creditLimit, String applicationDate) {
        CreditCard creditCard = new CreditCard();

        String cardNum = generateCardNum();

        CreditCardType cct = em.find(CreditCardType.class, creditCardTypeId);
        creditCard.setCreditCardType(cct);
        creditCard.setCreditLimit(creditLimit);
        creditCard.setCardNum(cardNum);
        creditCard.setCardHolderName(cardHolderName);

        String creditCardSecurityCode = generateCardSecurityCode();
        try {
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!creditcardNum:" + cardNum);
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!credit card security code: " + creditCardSecurityCode);
            String hashedDebitCardSecurityCode = md5Hashing(creditCardSecurityCode + cardNum.substring(0, 3));
            creditCard.setCardSecurityCode(hashedDebitCardSecurityCode);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(DebitCardSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }

        String[] applicationDateToString = applicationDate.split("/");
        String applicationYear = applicationDateToString[2];
        String applicationMonth = applicationDateToString[1];

        int expiryYearToInt = Integer.valueOf(applicationYear) + 5;
        String expiryYear = String.valueOf(expiryYearToInt);
        String creditCardExpiryDate = applicationMonth + "/" + expiryYear;

        creditCard.setCardExpiryDate(creditCardExpiryDate);
        creditCard.setRemainingExpirationMonths(60);
        creditCard.setStatus("pending");

        CustomerBasic cb = em.find(CustomerBasic.class, newCustomerBasicId);
        creditCard.setCustomerBasic(cb);
        cb.addNewCreditCard(creditCard);

        CustomerAdvanced ca = em.find(CustomerAdvanced.class, newCustomerAdvancedId);
        cb.setCustomerAdvanced(ca);
        ca.setCustomerBasic(cb);

        em.flush();
    }

    private String generateCardNum() {
        System.out.println("*");
        System.out.println("****** card/CreditCardSessionBean: generateCreditCardNumber() ******");

        String cardNumber = generateNdigitNumber(16);
        //check duplicate
        while (getCardByCardNum(cardNumber) != null) {
            cardNumber = generateNdigitNumber(16);
        }

        System.out.println("****** card/CreditCardSessionBean: generateCreditCardNumber(): credit card number generated" + cardNumber);
        return cardNumber;

    }

    private String generateCardSecurityCode() {
        System.out.println("*");
        System.out.println("****** card/CreditCardSessionBean: generateCardSecurityCode() ******");
        String csc = generateNdigitNumber(3);

        System.out.println("****** card/CreditCardSessionBean: generateCardSecurityCode(): credit CSC generated " + csc);
        return csc;
    }

    private String generateNdigitNumber(int n) {
        Random rnd = new Random();

        final char[] ch = new char[n];
        for (int i = 0; i < n; i++) {
            ch[i] = (char) ('0' + (i == 0 ? rnd.nextInt(9) + 1 : rnd.nextInt(10)));
        }
        String nDigitNumber = String.valueOf(ch);
        return nDigitNumber;
    }

    private CreditCardType findCardTypeByTypeName(String cardTypeName) {
        Query query = em.createQuery("SELECT c FROM CreditCardType c WHERE c.creditCardTypeName = :cardTypeName");
        query.setParameter("cardTypeName", cardTypeName);

        if (query.getResultList().isEmpty()) {
            return null;
        } else {
            CreditCardType findCreditCardType = (CreditCardType) query.getResultList().get(0);
            System.out.println("****** card/CreditCardSessionBean: find cardType: " + findCreditCardType);
            return findCreditCardType;
        }
    }

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
        System.out.println("md5 hashing- string to hash " + stringToHash);
        MessageDigest md = MessageDigest.getInstance("MD5");
        return Arrays.toString(md.digest(stringToHash.getBytes()));
    }

}
