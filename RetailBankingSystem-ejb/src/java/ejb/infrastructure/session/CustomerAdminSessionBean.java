package ejb.infrastructure.session;

import ejb.customer.entity.CustomerBasic;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.jboss.aerogear.security.otp.api.Base32;

/**
 *
 * @author hanfengwei
 */
@Stateless
public class CustomerAdminSessionBean implements CustomerAdminSessionBeanLocal {

    @EJB
    private CustomerEmailSessionBeanLocal customerEmailSessionBeanLocal;

    @PersistenceContext
    private EntityManager em;

    @Override
    public String createOnlineBankingAccount(Long customerId) {
        CustomerBasic customer = em.find(CustomerBasic.class, customerId);
        String account = null;
        String password = null;
        String secret = null;

        if (isNewCustomer(customer)) {
            //generate online banking account number
            account = generateAccountNumber();

            //generate random password
            password = generatePassword();

            //generate customerOTP secret
            secret = generateOTPSecret();

            //create customer account
            try {
                customer.setCustomerOnlineBankingAccountNum(account);
                String hashedPassword = password;
                hashedPassword = md5Hashing(password + customer.getCustomerIdentificationNum().substring(0, 3));
                customer.setCustomerOnlineBankingPassword(hashedPassword);
                customer.setCustomerStatus("new");
                customer.setCustomerOTPSecret(secret);
                em.flush();
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(CustomerAdminSessionBean.class.getName()).log(Level.SEVERE, null, ex);
            }

            //generate email
            Map emailActions = new HashMap();
            emailActions.put("onlineBanking", "yes");
            emailActions.put("onlineBankingPassword", password);
            customerEmailSessionBeanLocal.sendEmail(customer, "openAccount", emailActions);
            System.out.println("*** adminSessionBean: email sent to customer (online banking account created)");
            return account + "," + password;
        } else {
            Map emailActions = new HashMap();
            emailActions.put("onlineBanking", "no");
            customerEmailSessionBeanLocal.sendEmail(customer, "openAccount", emailActions);
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
        String password = new BigInteger(50, random).toString(32);
        System.out.println("****** adminSessionBean: generatePassword(): online banking account password generated");
        return password;
    }

    //Generate initial account number for customer online banking account
    private String generateAccountNumber() {
        SecureRandom random = new SecureRandom();
        String accountNumber = new BigInteger(25, random).toString();
        while (getCustomerByOnlineBankingAccount(accountNumber) != null) {
            accountNumber = new BigInteger(25, random).toString();
        }
        System.out.println("****** adminSessionBean: generateAccountNumber(): online banking account number generated");
        return accountNumber;
    }

    //Generate customer OTP secret
    private String generateOTPSecret() {
        SecureRandom random = new SecureRandom();
        String secret = new BigInteger(80, random).toString(32);
        
        boolean isUnique = false;
        while (!isUnique) {
            Query query = em.createQuery("SELECT c FROM CustomerBasic c WHERE c.customerOTPSecret = :secret");
            query.setParameter("secret", secret);
            List resultList = query.getResultList();
            if(resultList.isEmpty()){
                isUnique = true;
            }else{
                secret = new BigInteger(80, random).toString(32);
            }
        }
        System.out.println("****** adminSessionBean: generateOTPSecret(): OTP secret generated");
        return secret;
    }

    //Do customer login
    @Override
    public String login(String customerAccount, String password) {
        System.out.println("****** infrastructure/AdminSessionBean: login() ***");

        Query query = em.createQuery("SELECT c FROM CustomerBasic c WHERE c.customerOnlineBankingAccountNum = :accountNum");
        query.setParameter("accountNum", customerAccount);

        CustomerBasic thisCustomer;

        List resultList = query.getResultList();
        if (resultList.isEmpty()) {
            System.out.println("*** infrastructure/AdminSessionBean: login(): login failed: invalid account");
            return "invalidAccount";
        } else {
            thisCustomer = (CustomerBasic) resultList.get(0);
            System.out.println("*** infrastructure/AdminSessionBean: login(): customer login account: " + thisCustomer.getCustomerOnlineBankingAccountNum());
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
        if (resultList.isEmpty()) {
            System.out.println("*** infrastructure/AdminSessionBean: getCustomerByOnlineBankingAccount(): invalid account number: no result found");
            return null;
        } else {
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
    public CustomerBasic getCustomerByIdentificationNum(String identificationNum) {
        System.out.println("*** infrastructure/AdminSessionBean: getCustomerAccountById() ***");
        Query query = em.createQuery("SELECT c FROM CustomerBasic c WHERE c.customerIdentificationNum = :idNum");
        query.setParameter("idNum", identificationNum);
        List resultList = query.getResultList();
        if (resultList.isEmpty()) {
            System.out.println("*** infrastructure/AdminSessionBean: getCustomerAccountById(): no customer found");
            return null;
        } else {
            CustomerBasic customer = (CustomerBasic) resultList.get(0);
            System.out.println("*** infrastructure/AdminSessionBean: getCustomerAccountById(): get customer" + customer);
            return customer;
        }
    }

    @Override
    public Boolean resetPassword(String identificationNum) {
        CustomerBasic customer = getCustomerByIdentificationNum(identificationNum);
        if (customer == null) {
            return false;
        } else {
            try {
                String password = generatePassword();
                String hashedPassword = md5Hashing(password + identificationNum.substring(0, 3));
                customer.setCustomerOnlineBankingPassword(hashedPassword);
                em.flush();
                Map emailActions = new HashMap();
                emailActions.put("onlineBankingPassword", password);
                customerEmailSessionBeanLocal.sendEmail(customer, "resetOnlineBankingPassword", emailActions);
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(CustomerAdminSessionBean.class.getName()).log(Level.SEVERE, null, ex);
            }
            return true;
        }
    }

    private String md5Hashing(String stringToHash) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        return Arrays.toString(md.digest(stringToHash.getBytes()));
    }
}
