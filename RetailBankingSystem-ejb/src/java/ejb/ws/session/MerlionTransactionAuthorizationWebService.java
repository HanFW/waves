/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.ws.session;

import ejb.card.entity.CreditCard;
import ejb.card.entity.DebitCard;
import ejb.card.entity.PrincipalCard;
import ejb.card.entity.SupplementaryCard;
import ejb.card.session.DebitCardSessionBeanLocal;
import ejb.deposit.entity.BankAccount;
import ejb.deposit.session.BankAccountSessionBeanLocal;
import ejb.deposit.session.CreditCardTransactionSessionBeanLocal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.xml.ws.WebServiceRef;
import ws.client.merlionTransactionAuthorization.TransactionAuthorizationWebService_Service;
import ws.client.merlionTransactionAuthorization.TransactionToBeAuthorized;

/**
 *
 * @author Jingyuan
 */
@WebService(serviceName = "MerlionTransactionAuthorizationWebService")
@Stateless()
public class MerlionTransactionAuthorizationWebService {

    @WebServiceRef(wsdlLocation = "META-INF/wsdl/localhost_8080/TransactionAuthorizationWebService/TransactionAuthorizationWebService.wsdl")
    private TransactionAuthorizationWebService_Service service;

    @PersistenceContext
    private EntityManager em;

    @EJB
    private BankAccountSessionBeanLocal bankAccountSessionBeanLocal;

    @EJB
    private DebitCardSessionBeanLocal debitCardSessionBeanLocal;
    
    @EJB
    private CreditCardTransactionSessionBeanLocal creditCardTransactionSessionBeanLocal;

    @WebMethod(operationName = "checkTransactionAuthorizationById")
    public String checkTransactionAuthorizationById(@WebParam(name = "id") Long id) {

        TransactionToBeAuthorized transaction = getTransactionToBeAuthorizedById(id);

        String cardType = transaction.getCardType();
        String cardNum = transaction.getCardNum();
        String cardPwd = transaction.getCardPwd();

        double transactionAmt = transaction.getTransactionAmt();

        if (cardType.equals("debit")) {
            Query query = em.createQuery("SELECT d FROM DebitCard d WHERE d.cardNum = :cardNum");
            query.setParameter("cardNum", cardNum);

            if (query.getResultList().isEmpty()) {
                System.out.println("card not exist");
                return "not authorized";
            } else {
                DebitCard findDebitCard = (DebitCard) query.getSingleResult();
                if (!findDebitCard.getStatus().equals("activated")) {
                    System.out.println("card not activated");
                    return "not authorized";
                } else {
                    try {
                        String hashedPwd = md5Hashing(cardPwd + cardNum.substring(0, 3));
                        System.out.println("hashed pwd: " + hashedPwd);
                        if (!hashedPwd.equals(findDebitCard.getDebitCardPwd())) {
                            System.out.println("test card pwd is wrong");
                            return "not authorized";
                        } else {
                            BankAccount depositAccount = findDebitCard.getBankAccount();
                            Double accountBalance = Double.valueOf(depositAccount.getTotalBankAccountBalance());
                            System.out.println("accountBalance: " + accountBalance);
                            double availableTransactionBalance = findDebitCard.getAvailableTransactionBalance();
                            System.out.println("availableTransactionBalance: " + availableTransactionBalance);
                            if (transactionAmt > accountBalance || transactionAmt > availableTransactionBalance) {
                                System.out.println("depoist account balance insufficient or transaction exceeds daily transaction limit");
                                return "not authorized";
                            } else {
                                double newAvailableTransactionBalance = availableTransactionBalance - transactionAmt;
                                findDebitCard.setAvailableTransactionBalance(newAvailableTransactionBalance);
                                em.flush();

                                if (findDebitCard.getDebitCardType().getCardNetwork().equals("Visa")) {
                                    bankAccountSessionBeanLocal.updateDepositAccountAvailableBalance(cardNum, transactionAmt);
                                    System.out.println("authorized haahhahaa");
                                    return "authorized-Visa";
                                } else {
                                    bankAccountSessionBeanLocal.updateDepositAccountAvailableBalance(cardNum, transactionAmt);
                                    System.out.println("authorized hahahahah");
                                    return "authorized-MasterCard";
                                }
                            }//exceed account balance
                        }//debit card pwd is wrong
                    } //debit card not activated
                    catch (NoSuchAlgorithmException ex) {
                        System.out.println("NoSuchAlgorithmException");
                    }
                }
            }//debit card not exist

        }// debit card
        else {
            Query query = em.createQuery("SELECT c FROM CreditCard c WHERE c.cardNum = :cardNum");
            query.setParameter("cardNum", cardNum);
            if (query.getResultList().isEmpty()) {
                System.out.println("card not exist");
                return "not authorized";
            } else {
                CreditCard findCreditCard = (CreditCard) query.getSingleResult();
                if (!findCreditCard.getStatus().equals("Activated")) {
                    System.out.println("card not activated test");
                    return "not authorized";
                } else {
                    String creditCardType = findCreditCard.getCardType();
                    double creditLimit = 0.0;
                    double outstandingBalance = 0.0;
                    double newOutstandingBalance = 0.0;

                    if (creditCardType.equals("SupplementaryCard")) {
                        System.out.println("SupplementaryCard");
                        Query q = em.createQuery("SELECT s FROM SupplementaryCard s WHERE s.cardNum=:cardNum");
                        q.setParameter("cardNum", cardNum);
                        SupplementaryCard card = (SupplementaryCard) q.getSingleResult();
                        creditLimit = card.getPrincipalCard().getCreditLimit();
                        outstandingBalance = card.getPrincipalCard().getOutstandingBalance();
                        newOutstandingBalance = outstandingBalance + transactionAmt;

                        if (transactionAmt <= creditLimit || transactionAmt <= (creditLimit - outstandingBalance)) {
                            card.getPrincipalCard().setOutstandingBalance(newOutstandingBalance);
                            em.flush();
                        }

                    } else {
                        System.out.println("PrincipalCard");
                        Query q2 = em.createQuery("select p from PrincipalCard p where p.cardNum=:cardNum");
                        q2.setParameter("cardNum", cardNum);
                        PrincipalCard card2 = (PrincipalCard) q2.getSingleResult();
                        creditLimit = card2.getCreditLimit();
                        outstandingBalance = card2.getOutstandingBalance();
                        newOutstandingBalance = outstandingBalance + transactionAmt;

                        if (transactionAmt <= creditLimit && transactionAmt <= (creditLimit - outstandingBalance)) {
                            card2.setOutstandingBalance(newOutstandingBalance);
                            em.flush();
                        }

                    }//determine credit card type and update outstandingBalance

                    if (transactionAmt > creditLimit || transactionAmt > (creditLimit - outstandingBalance)) {
                        System.out.println("credit limit insufficient");
                        return "not authorized";
                    } else {

                        if (findCreditCard.getCreditCardType().getCardNetwork().equals("Visa")) {
                            transaction.setTransactionStatus("authorized");
                            return "authorized-Visa";
                        } else {
                            transaction.setTransactionStatus("authorized");
                            return "authorized-MasterCard";
                        }

                    }//exceed credit limit
                }//credit card not activated
            }//credit card not exist
        }//credit card              
        return "";
    }

    private String md5Hashing(String stringToHash) throws NoSuchAlgorithmException {
        System.out.println("md5hashing: string to hash " + stringToHash);
        MessageDigest md = MessageDigest.getInstance("MD5");
        return Arrays.toString(md.digest(stringToHash.getBytes()));
    }

    private TransactionToBeAuthorized getTransactionToBeAuthorizedById(java.lang.Long id) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        ws.client.merlionTransactionAuthorization.TransactionAuthorizationWebService port = service.getTransactionAuthorizationWebServicePort();
        return port.getTransactionToBeAuthorizedById(id);
    }

    private java.util.List<ws.client.merlionTransactionAuthorization.TransactionToBeAuthorized> getAllAuthorizedTransactions() {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        ws.client.merlionTransactionAuthorization.TransactionAuthorizationWebService port = service.getTransactionAuthorizationWebServicePort();
        return port.getAllAuthorizedTransactions();
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "merlionCreditCustomerAccountForTransaction")
    public String merlionCreditCustomerAccountForTransaction() {
        List<TransactionToBeAuthorized> transactions = getAllAuthorizedTransactions();

        System.out.println("getAllAuthorizedTransactions(): " + transactions);

        for (int i = 0; i < transactions.size(); i++) {
            if (transactions.get(i).getCardType().equals("debit") && transactions.get(i).getDebitBankAccount().equals("no")) {
                String cardNum = transactions.get(i).getCardNum();
                System.out.println("debug!!! cardNum" + cardNum);
                double transactionAmt = transactions.get(i).getTransactionAmt();
                String merchantName = transactions.get(i).getMerchantName();
                bankAccountSessionBeanLocal.updateDepositAccountTotalBalance(cardNum, transactionAmt, merchantName);
            }//debit card authorized transactions
            else if (transactions.get(i).getCardType().equals("credit") && transactions.get(i).getDebitBankAccount().equals("no")) {
                String cardNum = transactions.get(i).getCardNum();
                System.out.println("debug!!! cardNum" + cardNum);
                double transactionAmt = transactions.get(i).getTransactionAmt();
                String merchantName = transactions.get(i).getMerchantName();
                creditCardTransactionSessionBeanLocal.addCreditCardTransaction(cardNum, transactionAmt, merchantName);
            }//credit card authorized transactions
        }
        debitCardSessionBeanLocal.updateAllDebitCardsAvailableDailyTransactionBalance();
        return "success";

    }

}
