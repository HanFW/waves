/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.card.session;

import ejb.card.entity.CreditCard;
import ejb.card.entity.CreditCardType;
import ejb.card.entity.PrincipalCard;
import ejb.card.entity.SupplementaryCard;
import ejb.customer.entity.CustomerAdvanced;
import ejb.customer.entity.CustomerBasic;
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
    private CustomerAdminSessionBeanLocal customerAdminSessionBeanLocal;
    @EJB
    private CreditCardManagementSessionBeanLocal creditCardManagementSessionBeanLocal;

    @PersistenceContext
    private EntityManager em;

    @Override
    public void createCreditCard(Long newCustomerBasicId, Long newCustomerAdvancedId, Long creditCardTypeId, String cardHolderName, String hasCreditLimit, double creditLimit, String applicationDate) {
        PrincipalCard creditCard = new PrincipalCard();

        String cardNum = generateCardNum();

        CreditCardType cct = em.find(CreditCardType.class, creditCardTypeId);
        if (hasCreditLimit.equals("Yes")) {
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
        creditCard.setRemainingActivationDays(15);
        creditCard.setCardType("PrincipalCard");
        creditCard.setStatus("Pending");

        CustomerBasic cb = em.find(CustomerBasic.class, newCustomerBasicId);
        creditCard.setCustomerBasic(cb);
        cb.addNewCreditCard(creditCard);

        CustomerAdvanced ca = em.find(CustomerAdvanced.class, newCustomerAdvancedId);
        cb.setCustomerAdvanced(ca);
        ca.setCustomerBasic(cb);

        em.flush();
        System.out.println("****** card/CreditCardSessionBean: createCreditCard()" + cardNum + " ******");
    }

    @Override
    public void addSupplementaryCard(Long principalCardId, String cardHolderName, String dateOfBirth, String relationship, String identificationNum) {
        PrincipalCard pc = em.find(PrincipalCard.class, principalCardId);

        SupplementaryCard sc = new SupplementaryCard();

        sc.setCardExpiryDate(pc.getCardExpiryDate());
        sc.setCardType("SupplementaryCard");
        sc.setCreditCardType(pc.getCreditCardType());
        sc.setDateOfBirth(dateOfBirth);
        sc.setCardHolderName(cardHolderName);
        sc.setIdentificationNum(identificationNum);
        sc.setPrincipalCard(pc);
        sc.setCustomerBasic(pc.getCustomerBasic());
        sc.setRelationship(relationship);
        sc.setRemainingActivationDays(15);
        sc.setRemainingExpirationMonths(pc.getRemainingExpirationMonths());
        sc.setStatus("Pending");

        String cardNum = generateCardNum();
        sc.setCardNum(cardNum);
        String creditCardSecurityCode = generateCardSecurityCode();
        try {
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!supplementarycardNum:" + cardNum);
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!supplemenetary card security code: " + creditCardSecurityCode);
            String hashedDebitCardSecurityCode = md5Hashing(creditCardSecurityCode + cardNum.substring(0, 3));
            sc.setCardSecurityCode(hashedDebitCardSecurityCode);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(DebitCardSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }

        em.persist(sc);
    }

    @Override
    public void createNewCardAfterLost(Long cbId, Long caId, Long creditCardTypeId, String cardHolderName,
            double creditLimit, String expDate, int remainingMonths, List<SupplementaryCard> supplCards, double outstandingBalance) {
        PrincipalCard creditCard = new PrincipalCard();

        String cardNum = generateCardNum();
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!before!!!!debug cct ID " + creditCardTypeId);
//        CreditCardType cct = em.find(CreditCardType.class, creditCardTypeId);
        Query query = em.createQuery("SELECT c FROM CreditCardType c WHERE c.creditCardTypeId = :cardTypeNameId");
        query.setParameter("cardTypeNameId", creditCardTypeId);
        CreditCardType cct = (CreditCardType) query.getResultList().get(0);
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!debug cct" + cct);

        creditCard.setCreditLimit(creditLimit);
        creditCard.setCreditCardType(cct);
        creditCard.setCardNum(cardNum);
        creditCard.setCardHolderName(cardHolderName);
        creditCard.setCardType("CreditCard");
        creditCard.setSupplementaryCards(supplCards);
        creditCard.setOutstandingBalance(outstandingBalance);

        String creditCardSecurityCode = generateCardSecurityCode();
        try {
            System.out.println("!!!!!!!!!!!report loss!!!!!!!!!!!!!new creditcardNum:" + cardNum);
            System.out.println("!!!!!!!!!!!report loss!!!!!!!!!!!!!new credit card security code: " + creditCardSecurityCode);
            String hashedDebitCardSecurityCode = md5Hashing(creditCardSecurityCode + cardNum.substring(0, 3));
            creditCard.setCardSecurityCode(hashedDebitCardSecurityCode);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(DebitCardSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }

        creditCard.setCardExpiryDate(expDate);
        System.out.println("!!!!!!!!!!!!expdate " + expDate);
        creditCard.setRemainingExpirationMonths(remainingMonths);

        System.out.println("!!!!!!!!!!!!rem months " + remainingMonths);
        creditCard.setStatus("Approved");
        System.out.println("!!!!!!!!!!!!Aproved ");

        System.out.println("!!!!!!!!!!!!!!!!cbID" + cbId);
        CustomerBasic cb = em.find(CustomerBasic.class, cbId);
        creditCard.setCustomerBasic(cb);
        cb.addNewCreditCard(creditCard);
        System.out.println("!!!!!!!!!!!!cb!!!!!!!!");

        System.out.println("!!!!!!!!!!!!!!!!caID" + caId);
        CustomerAdvanced ca = em.find(CustomerAdvanced.class, caId);
        cb.setCustomerAdvanced(ca);
        ca.setCustomerBasic(cb);
        System.out.println("!!!!!!!!!!!!CAdvanced!!!!!!!!!!!!!");

        em.flush();
        System.out.println("****** card/CreditCardSessionBean: createCreditCardAfterLoss()" + cardNum + " ******");
    }

    @Override
    public void createNewCardAfterDamage(Long cbId, Long caId, Long creditCardTypeId, String cardHolderName, double creditLimit, String expDate, int remainingMonths, List<SupplementaryCard> supplCards, Long predecessorId) {
        PrincipalCard creditCard = new PrincipalCard();

        String cardNum = generateCardNum();
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!before!!!!debug cct ID " + creditCardTypeId);
//        CreditCardType cct = em.find(CreditCardType.class, creditCardTypeId);
        Query query = em.createQuery("SELECT c FROM CreditCardType c WHERE c.creditCardTypeId = :cardTypeNameId");
        query.setParameter("cardTypeNameId", creditCardTypeId);
        CreditCardType cct = (CreditCardType) query.getResultList().get(0);
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!debug cct" + cct);

        creditCard.setPredecessor(predecessorId);
        creditCard.setCreditLimit(creditLimit);
        creditCard.setCreditCardType(cct);
        creditCard.setCardNum(cardNum);
        creditCard.setCardHolderName(cardHolderName);
        creditCard.setCardType("CreditCard");
        creditCard.setSupplementaryCards(supplCards);

        String creditCardSecurityCode = generateCardSecurityCode();
        try {
            System.out.println("!!!!!!!!!!!report loss!!!!!!!!!!!!!new creditcardNum:" + cardNum);
            System.out.println("!!!!!!!!!!!report loss!!!!!!!!!!!!!new credit card security code: " + creditCardSecurityCode);
            String hashedDebitCardSecurityCode = md5Hashing(creditCardSecurityCode + cardNum.substring(0, 3));
            creditCard.setCardSecurityCode(hashedDebitCardSecurityCode);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(DebitCardSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }

        creditCard.setCardExpiryDate(expDate);
        System.out.println("!!!!!!!!!!!!expdate " + expDate);
        creditCard.setRemainingExpirationMonths(remainingMonths);

        System.out.println("!!!!!!!!!!!!rem months " + remainingMonths);
        creditCard.setStatus("Approved");
        System.out.println("!!!!!!!!!!!!Aproved ");

        System.out.println("!!!!!!!!!!!!!!!!cbID" + cbId);
        CustomerBasic cb = em.find(CustomerBasic.class, cbId);
        creditCard.setCustomerBasic(cb);
        cb.addNewCreditCard(creditCard);
        System.out.println("!!!!!!!!!!!!cb!!!!!!!!");

        System.out.println("!!!!!!!!!!!!!!!!caID" + caId);
        CustomerAdvanced ca = em.find(CustomerAdvanced.class, caId);
        cb.setCustomerAdvanced(ca);
        ca.setCustomerBasic(cb);
        System.out.println("!!!!!!!!!!!!CAdvanced!!!!!!!!!!!!!");

        em.flush();
        System.out.println("****** card/CreditCardSessionBean: createCreditCardAfterLoss()" + cardNum + " ******");
    }

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
        em.refresh(customer);

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
        em.refresh(customer);

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
    public SupplementaryCard getSupplementaryCardById(Long supplementaryCardId) {
        SupplementaryCard sc = em.find(SupplementaryCard.class, supplementaryCardId);
        em.refresh(sc);

        return sc;
    }

    @Override
    public List<SupplementaryCard> getAllSupplementaryCardByCustomer(CustomerBasic customer) {
        Query query = em.createQuery("SELECT s FROM SupplementaryCard s WHERE s.customerBasic = :customer");
        query.setParameter("customer", customer);

        return query.getResultList();
    }

    @Override
    public List<PrincipalCard> getAllPendingCreditCards() {
        Query query = em.createQuery("SELECT p FROM PrincipalCard p WHERE p.status = :status");
        query.setParameter("status", "Pending");

        return query.getResultList();
    }

    @Override
    public List<SupplementaryCard> getAllPendingSupplementaryCards() {
        Query query = em.createQuery("SELECT s FROM SupplementaryCard s WHERE s.status = :status");
        query.setParameter("status", "Pending");

        return query.getResultList();
    }

    @Override
    public List<String> getAllPlatinumCards(Long customerId) {
        List<String> creditCardNames = new ArrayList();
        CustomerBasic customer = em.find(CustomerBasic.class, customerId);
        em.refresh(customer);

        Query query = em.createQuery("SELECT p FROM CreditCard p WHERE p.creditCardType.creditCardTypeId = :typeId AND p.customerBasic.customerBasicId =:customerBasicId AND p.cardType =:cardType");
        query.setParameter("typeId", 3);
        query.setParameter("customerBasicId", customerId);
        query.setParameter("cardType", "PrincipalCard");

        List<CreditCard> prinicpalCards = query.getResultList();
        System.out.println("##################result list" + prinicpalCards);
        int size = prinicpalCards.size();
        for (int j = 0; j < size; j++) {
            CreditCard principalCard = prinicpalCards.get(j);
            if (principalCard.getStatus().equals("Activated")) {
                String info = principalCard.getCreditCardType().getCreditCardTypeName() + "-" + principalCard.getCardNum();
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
                PrincipalCard findCreditCard = getPrincipalCardByCardNum(creditCardNum);
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
                            PrincipalCard predecessorCard = em.find(PrincipalCard.class, predecessorId);
                            double outstandingBalance = predecessorCard.getOutstandingBalance();
                            findCreditCard.setOutstandingBalance(outstandingBalance);
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

    @Override
    public CreditCard getCardByCardNum(String cardNum) {
        Query query = em.createQuery("SELECT c FROM CreditCard c WHERE c.cardNum = :cardNum");
        query.setParameter("cardNum", cardNum);

        if (query.getResultList().isEmpty()) {
            return null;
        } else {
            CreditCard findCreditCard = (CreditCard) query.getResultList().get(0);
            return findCreditCard;
        }
    }

    @Override
    public PrincipalCard getPrincipalCardByCardNum(String cardNum) {
        Query query = em.createQuery("SELECT p FROM PrincipalCard p WHERE p.cardNum = :cardNum");
        query.setParameter("cardNum", cardNum);

        if (query.getResultList().isEmpty()) {
            return null;
        } else {
            PrincipalCard findCreditCard = (PrincipalCard) query.getResultList().get(0);
            return findCreditCard;
        }
    }

    @Override
    public CreditCard getCardByCardId(Long cardId) {
        Query query = em.createQuery("SELECT c FROM CreditCard c WHERE c.cardId = :cardId");
        query.setParameter("cardId", cardId);

        if (query.getResultList().isEmpty()) {
            return null;
        } else {
            CreditCard findCreditCard = (CreditCard) query.getResultList().get(0);
            return findCreditCard;
        }
    }

    @Override
    public double[] getCreditLimitMaxInterval() {
        System.out.println("****** loan/LoanApplicationSessionBean: getCreditLimitMaxInterval() ******");
        double[] maxInterval = new double[2];
        maxInterval[0] = 1000;
        maxInterval[1] = 20000;
        return maxInterval;
    }

    @Override
    public double getCreditLimitRiskRatio() {
        System.out.println("****** loan/LoanApplicationSessionBean: getCreditLimitiskRatio() ******");
        double ratio = 0;
        return ratio;
    }

    @Override
    public double[] getCreditLimitSuggestedInterval() {
        System.out.println("****** loan/LoanApplicationSessionBean: getCreditLimitSuggestedInterval() ******");
        double[] interval = new double[2];
        interval[0] = 1000;
        interval[1] = 20000;
        return interval;
    }

    @Override
    public void approveRequest(Long creditCardId, double creditLimit) {
        System.out.println("****** loan/LoanApplicationSessionBean: approveMortgageLoanRequest() ******");
        PrincipalCard cc = em.find(PrincipalCard.class, creditCardId);
        cc.setCreditLimit(creditLimit);
        cc.setStatus("Approved");
        em.flush();
    }

    @Override
    public void rejectRequest(Long creditCardId) {
        System.out.println("****** loan/LoanApplicationSessionBean: rejectMortgageLoanRequest() ******");
        CreditCard cc = em.find(CreditCard.class, creditCardId);
        CustomerBasic customer = cc.getCustomerBasic();
        CustomerAdvanced ca = customer.getCustomerAdvanced();

//        CreditReportBureauScore report = customer.getBureauScore();
//        
//        em.remove(report);
        em.remove(ca);
        em.remove(customer);
        em.flush();

    }

    @Override
    public void approveSupplementary(Long supplementaryCardId) {
        SupplementaryCard sc = em.find(SupplementaryCard.class, supplementaryCardId);
        sc.setStatus("Approved");
        em.flush();
    }

    @Override
    public void rejectSupplementary(Long supplementaryCardId) {
        SupplementaryCard sc = em.find(SupplementaryCard.class, supplementaryCardId);
        em.remove(sc);
        em.flush();
    }

    private PrincipalCard getPrincipalByCardNum(String cardNum) {
        Query query = em.createQuery("SELECT p FROM PrincipalCard p WHERE p.cardNum = :cardNum");
        query.setParameter("cardNum", cardNum);

        if (query.getResultList().isEmpty()) {
            return null;
        } else {
            PrincipalCard findCreditCard = (PrincipalCard) query.getResultList().get(0);
            return findCreditCard;
        }
    }

    private String md5Hashing(String stringToHash) throws NoSuchAlgorithmException {
        System.out.println("md5 hashing- string to hash " + stringToHash);
        MessageDigest md = MessageDigest.getInstance("MD5");
        return Arrays.toString(md.digest(stringToHash.getBytes()));
    }

    @Override
    public void updateCreditCardLimit(String creditCardNum, double transactionAmt) {
        PrincipalCard card = getPrincipalCardByCardNum(creditCardNum);
        double newCreditLimit = card.getCreditLimit() - transactionAmt;
        card.setCreditLimit(newCreditLimit);
        double newOutstandingBalance = card.getOutstandingBalance() + transactionAmt;
        card.setOutstandingBalance(newOutstandingBalance);
        em.flush();

    }

    @Override
    public List<String> getAllPrincipalCardInfoByCustomer(CustomerBasic customer) {
        List<PrincipalCard> cards;
        List<String> cardsInfo = new ArrayList<>();
        Query query = em.createQuery("SELECT p FROM PrincipalCard p WHERE p.customerBasic=:customer");
        query.setParameter("customer", customer);

        if (!query.getResultList().isEmpty()) {
            cards = query.getResultList();
            for (int i = 0; i < cards.size(); i++) {
                String info = cards.get(i).getCreditCardType().getCreditCardTypeName() + "-" + cards.get(i).getCardNum();
                cardsInfo.add(info);
            }
        }

        return cardsInfo;
    }

    @Override
    public List<PrincipalCard> getAllPrincipalCardByCustomer(CustomerBasic customer) {
        Query query = em.createQuery("SELECT p FROM PrincipalCard p WHERE p.customerBasic=:customer");
        query.setParameter("customer", customer);

        return query.getResultList();
    }

}
