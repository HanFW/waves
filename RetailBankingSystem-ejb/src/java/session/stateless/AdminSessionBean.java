package session.stateless;

import entity.CustomerBasic;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author hanfengwei
 */
@Stateless
public class AdminSessionBean implements AdminSessionBeanLocal {

    @EJB
    private CustomerEmailSessionBeanLocal emailSessionBeanLocal;

    @PersistenceContext
    private EntityManager em;

    @Override
    public String createOnlineBankingAccount(Long customerId) {
        CustomerBasic customer = em.find(CustomerBasic.class, customerId);
        String account = null;
        String password = null;

        if (isNewCustomer(customer)) {
            //generate online banking account number
            account = generateAccountNumber();
            System.out.println("*** adminSessionBean: generateAccountNumber(): online banking account number generated");

            //generate random password
            password = generatePassword();
            System.out.println("*** adminSessionBean: generatePassword(): online banking account password generated");

            //create customer account
            try {
                customer.setCustomerOnlineBankingAccountNum(account);
                String hashedPassword = password;
                hashedPassword = md5Hashing(password + customer.getCustomerIdentificationNum().substring(0, 3));
                customer.setCustomerOnlineBankingPassword(hashedPassword);
                customer.setCustomerStatus("new");
                em.flush();
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(AdminSessionBean.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            //generate email
            Map emailActions = new HashMap();
            emailActions.put("onlineBanking", "yes");
            emailActions.put("onlineBankingPassword", password);
            emailSessionBeanLocal.sendEmail(customer, "openAccount", emailActions);
            System.out.println("*** adminSessionBean: email sent to customer (online banking account created)");
            return account + "," + password;
        } else {
            Map emailActions = new HashMap();
            emailActions.put("onlineBanking", "no");
            emailSessionBeanLocal.sendEmail(customer, "openAccount", emailActions);
            System.out.println("*** adminSessionBean: email sent to customer (not a new customer)");
            return "not a new customer";
        }
    }

    //Check if it's a new customer
    private boolean isNewCustomer(CustomerBasic customer) {
        return customer.getCustomerOnlineBankingAccountNum() == null;
    }

    //Generate initial password for customer online banking account
    private String generatePassword() {
        SecureRandom random = new SecureRandom();
        return new BigInteger(50, random).toString(32);
    }

    //Generate initial account number for customer online banking account
    private String generateAccountNumber() {
        SecureRandom random = new SecureRandom();
        String accountNumber = new BigInteger(25, random).toString();
        while(getCustomerByOnlineBankingAccount(accountNumber) != null){
            accountNumber = new BigInteger(25, random).toString();
        }
        return accountNumber;
    }

    //Do customer login
    @Override
    public String login(String customerAccount, String password) {
        System.out.println("*** infrastructure/AdminSessionBean: login() ***");

        Query query = em.createQuery("SELECT c FROM CustomerBasic c WHERE c.customerOnlineBankingAccountNum = :accountNum");
        query.setParameter("accountNum", customerAccount);

        CustomerBasic thisCustomer;

        try {
            thisCustomer = (CustomerBasic) query.getSingleResult();
            System.out.println("*** infrastructure/AdminSessionBean: login(): customer login account: " + thisCustomer.getCustomerOnlineBankingAccountNum());
        } catch (NoResultException ex) {
            System.out.println("*** infrastructure/AdminSessionBean: login(): login failed: invalid account");
            return "invalidAccount";
        }

        if (thisCustomer.getCustomerOnlineBankingPassword().equals(password)) {
            System.out.println("*** infrastructure/AdminSessionBean: login(): login successful");
            return "loggedIn";
        } else {
            System.out.println("*** infrastructure/AdminSessionBean: login(): login failed: invalid password");
            return "invalidPassword";
        }
    }

    @Override
    public CustomerBasic getCustomerByOnlineBankingAccount(String customerAccount) {
        System.out.println("*** infrastructure/AdminSessionBean: getCustomerByOnlineBankingAccount() ***");
        Query query = em.createQuery("SELECT c FROM CustomerBasic c WHERE c.customerOnlineBankingAccountNum = :accountNum");
        query.setParameter("accountNum", customerAccount);
        List resultList = query.getResultList();
        if(resultList.isEmpty()){
            System.out.println("*** infrastructure/AdminSessionBean: getCustomerByOnlineBankingAccount(): invalid account number: no result found");
            return null;
        }else{
            System.out.println("*** infrastructure/AdminSessionBean: getCustomerByOnlineBankingAccount(): valid account number: return CustomerBasic");
            return (CustomerBasic) resultList.get(0);
        }
    }

    @Override
    public String updateOnlineBankingAccount(String accountNum, String password, Long customerId) {
        System.out.println("*** adminSessionBean: updateOnlineBankingAccount(): start");
        CustomerBasic customer = em.find(CustomerBasic.class, customerId);
        customer.setCustomerOnlineBankingAccountNum(accountNum);
        customer.setCustomerOnlineBankingPassword(password);
        customer.setCustomerStatus("activated");
        em.flush();
        return "activated";
    }
    
    @Override
    public CustomerBasic getCustomerById(String identificationNum){
        System.out.println("*** infrastructure/AdminSessionBean: getCustomerAccountById() ***");
        Query query = em.createQuery("SELECT c FROM CustomerBasic c WHERE c.customerIdentificationNum = :idNum");
        query.setParameter("idNum", identificationNum);
        List resultList = query.getResultList();
        if(resultList.isEmpty()){
            System.out.println("*** infrastructure/AdminSessionBean: getCustomerAccountById(): no customer found");
            return null;
        }else{
            CustomerBasic customer = (CustomerBasic) resultList.get(0);
            System.out.println("*** infrastructure/AdminSessionBean: getCustomerAccountById(): get customer" + customer);
            return customer;
        }
    }

    private String md5Hashing(String stringToHash) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        return Arrays.toString(md.digest(stringToHash.getBytes()));
    }
}
