/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.card.session;

import ejb.card.entity.CreditCard;
import ejb.card.entity.CreditCardType;
import ejb.card.entity.DebitCard;
import ejb.card.entity.DebitCardType;
import ejb.customer.entity.CustomerAdvanced;
import ejb.customer.entity.CustomerBasic;
import ejb.deposit.entity.BankAccount;
import ejb.infrastructure.session.CustomerAdminSessionBeanLocal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
    @EJB
    private CustomerAdminSessionBeanLocal customerAdminSessionBeanLocal;
    @EJB
    private CreditCardManagementSessionBeanLocal creditCardManagementSessionBeanLocal;

    @PersistenceContext
    private EntityManager em;

    @Override
    public void createCreditCard(Long newCustomerBasicId, Long newCustomerAdvancedId, Long creditCardTypeId, String cardHolderName, String hasCreditLimit, double creditLimit, String applicationDate) {
        CreditCard creditCard = new CreditCard();

        String cardNum = generateCardNum();

        CreditCardType cct = em.find(CreditCardType.class, creditCardTypeId);
        if (hasCreditLimit == "Yes") {
            creditCard.setCreditLimit(creditLimit);
        } else {
            creditCard.setCreditLimit(0.0);
        }

        creditCard.setCreditCardType(cct);
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
        creditCard.setStatus("Approved");

        CustomerBasic cb = em.find(CustomerBasic.class, newCustomerBasicId);
        creditCard.setCustomerBasic(cb);
        cb.addNewCreditCard(creditCard);

        CustomerAdvanced ca = em.find(CustomerAdvanced.class, newCustomerAdvancedId);
        cb.setCustomerAdvanced(ca);
        ca.setCustomerBasic(cb);

        customerAdminSessionBeanLocal.createOnlineBankingAccount(newCustomerBasicId);

        em.flush();
        System.out.println("****** card/CreditCardSessionBean: createCreditCard()" + cardNum + " ******");
    }
    
//    @Override
//    public void issueCreditCardForCardReplacement(String bankAccountNum, String cardHolderName, String applicationDate, String cardTypeName, Long predecessorId) {
//        DebitCard debitCard = new DebitCard();
//        debitCard.setCardHolderName(cardHolderName);
//
//        BankAccount depositAccount = findDepositAccountByAccountNum(bankAccountNum);
//        debitCard.setBankAccount(depositAccount);
//
//        DebitCardType debitCardType = findCardTypeByTypeName(cardTypeName);
//        debitCard.setDebitCardType(debitCardType);
//
//        String cardNum = generateCardNum();
//        debitCard.setCardNum(cardNum);
//
//        String debitCardSecurityCode = generateCardSecurityCode();
//        try {
//            System.out.println("debug cardNum:" + cardNum);
//            System.out.println("debug csc initial:" + debitCardSecurityCode);
//            String hashedDebitCardSecurityCode = md5Hashing(debitCardSecurityCode + cardNum.substring(0, 3));
//            System.out.println("debug:" + hashedDebitCardSecurityCode);
//            debitCard.setCardSecurityCode(hashedDebitCardSecurityCode);
//        } catch (NoSuchAlgorithmException ex) {
//            Logger.getLogger(DebitCardSessionBean.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
////        String[] applicationDateToString = changeDateFormat(applicationDate);
//        String[] applicationDateToString = applicationDate.split("/");
//        String applicationYear = applicationDateToString[2];
//        String applicationMonth = applicationDateToString[1];
//
//        int expiryYearToInt = Integer.valueOf(applicationYear) + 5;
//        String expiryYear = String.valueOf(expiryYearToInt);
//        String debitCardExpiryDate = applicationMonth + "/" + expiryYear;
//
//        debitCard.setCardExpiryDate(debitCardExpiryDate);
//
//        debitCard.setRemainingExpirationMonths(60);
//
//        debitCard.setStatus("not activated");
//
//        debitCard.setTransactionLimit(500);
//
//        debitCard.setPredecessor(predecessorId);
//
//        em.persist(debitCard);
//        depositAccount.addDebitCard(debitCard);
//        debitCardType.addDebitCard(debitCard);
//      
//    }

    @Override
    public String findTypeNameById(Long cardTypeId) {
        CreditCardType cct = em.find(CreditCardType.class, cardTypeId);
        return cct.getCreditCardTypeName();
    }

    //get all debit cards of a customer
    @Override
    public List<String> getAllDebitCards(Long customerId) {
        List<String> creditCardNames = new ArrayList();
        CustomerBasic customer = em.find(CustomerBasic.class, customerId);

        int size = customer.getCreditCard().size();

        for (int j = 0; j < size; j++) {
            CreditCard creditCard = customer.getCreditCard().get(j);

            String info = creditCard.getCreditCardType().getCreditCardTypeName() + "-" + creditCard.getCardNum();
            creditCardNames.add(info);

        }
        return creditCardNames;
    }

    //get all activated credit cards of a customer
    @Override
    public List<String> getAllActivatedCreditCards(Long customerId) {
        List<String> creditCardNames = new ArrayList();
        CustomerBasic customer = em.find(CustomerBasic.class, customerId);

        int size = customer.getCreditCard().size();

        for (int j = 0; j < size; j++) {
            CreditCard creditCard = customer.getCreditCard().get(j);
            if (creditCard.getStatus().equals("Activated")) {
                String info = creditCard.getCreditCardType().getCreditCardTypeName() + "-" + creditCard.getCardNum();
                creditCardNames.add(info);
            }
        }
        return creditCardNames;
    }

    //get all non-activated credit cards of a customer
    @Override
    public List<String> getAllNonActivatedCreditCards(Long customerId) {
        List<String> creditCardNames = new ArrayList();
        CustomerBasic customer = em.find(CustomerBasic.class, customerId);

        int size = customer.getCreditCard().size();

        for (int j = 0; j < size; j++) {
            CreditCard creditCard = customer.getCreditCard().get(j);
            if (creditCard.getStatus().equals("Approved")) {
                String info = creditCard.getCreditCardType().getCreditCardTypeName() + "-" + creditCard.getCardNum();
                creditCardNames.add(info);
            }
        }

        return creditCardNames;
    }

    @Override
    public String creditCardNumValiadation(String creditCardNum, String cardHolderName, String creditCardSecurityCode) {
        try {
            if (getCardByCardNum(creditCardNum) == null) {
                return "credit card not exist";
            } else {
                CreditCard findCreditCard = getCardByCardNum(creditCardNum);
                if (!findCreditCard.getCardHolderName().equals(cardHolderName)) {
                    return "cardHolderName not match";
                } else {
                    String hashedCSC;
                    System.out.println("debug check hashed csc - csc:" + creditCardSecurityCode);
                    System.out.println("debug check hashed csc - creditCardNum:" + creditCardNum);
                    hashedCSC = md5Hashing(creditCardSecurityCode + creditCardNum.substring(0, 3));
                    System.out.println("debug check hashed csc:" + hashedCSC);
                    if (!findCreditCard.getCardSecurityCode().equals(hashedCSC)) {
                        return "csc not match";
                    } else {
                        findCreditCard.setStatus("Activated");
                        if (findCreditCard.getPredecessor() != null) {
                            Long predecessorId = findCreditCard.getPredecessor();
                            creditCardManagementSessionBeanLocal.cancelCreditCardAfterReplacement(predecessorId);
                            findCreditCard.setPredecessor(null);
                        }//if the card has a predecessor, then delete the predecessor from database
                        return "valid";
                    }

                }//card holder name match

            }//credit card exist
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(CreditCardSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
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
    
//    private CreditCard getCardByCardName (String cardName) {
//        Query query = em.createQuery("SELECT c FROM CreditCard c WHERE c. = :cardNum");
//        query.setParameter("cardNum", cardName);
//
//        if (query.getResultList().isEmpty()) {
//            return null;
//        } else {
//            CreditCard findCreditCard = (CreditCard) query.getResultList().get(0);
//            return findCreditCard;
//        }
//    }

    private String md5Hashing(String stringToHash) throws NoSuchAlgorithmException {
        System.out.println("md5 hashing- string to hash " + stringToHash);
        MessageDigest md = MessageDigest.getInstance("MD5");
        return Arrays.toString(md.digest(stringToHash.getBytes()));
    }

}
