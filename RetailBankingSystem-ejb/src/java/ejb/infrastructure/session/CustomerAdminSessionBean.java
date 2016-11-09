package ejb.infrastructure.session;

import ejb.customer.entity.CustomerBasic;
import ejb.deposit.entity.BankAccount;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
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
public class CustomerAdminSessionBean implements CustomerAdminSessionBeanLocal, CustomerAdminSessionBeanRemote {

    @EJB
    private CustomerEmailSessionBeanLocal customerEmailSessionBeanLocal;

    @PersistenceContext
    private EntityManager em;

    @Override
    public String createOnlineBankingAccount(Long customerId) {
        System.out.println("*");
        System.out.println("****** infrastructure/CustomerAdminSessionBean: createOnlineBankingAccount() ******");
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
                customer.setCustomerOnlineBankingAccountLocked("no");
                customer.setCustomerOTPSecret(secret);
                em.flush();
                System.out.println("****** infrastructure/CustomerAdminSessionBean: createOnlineBankingAccount(): new online banking account created");
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(CustomerAdminSessionBean.class.getName()).log(Level.SEVERE, null, ex);
            }

            //generate email
            Map emailActions = new HashMap();
            emailActions.put("onlineBanking", "yes");
            emailActions.put("onlineBankingPassword", password);
            customerEmailSessionBeanLocal.sendEmail(customer, "openAccount", emailActions);
            return account + "," + password;
        } else {
            Map emailActions = new HashMap();
            emailActions.put("onlineBanking", "no");
            customerEmailSessionBeanLocal.sendEmail(customer, "openAccount", emailActions);
            return "not a new customer";
        }
    }

    //Check if it's a new customer
    private boolean isNewCustomer(CustomerBasic customer) {
        return customer.getCustomerOnlineBankingAccountNum() == null;
    }

    //Generate initial password for customer online banking account
    private String generatePassword() {
        System.out.println("*");
        System.out.println("****** infrastructure/CustomerAdminSessionBean: generatePassword() ******");
        SecureRandom random = new SecureRandom();
        String password = new BigInteger(50, random).toString(32);
        System.out.println("****** infrastructure/CustomerAdminSessionBean: generatePassword(): online banking account PIN generated");
        return password;
    }

    //Generate initial account number for customer online banking account
    private String generateAccountNumber() {
        System.out.println("*");
        System.out.println("****** infrastructure/CustomerAdminSessionBean: generateAccountNumber() ******");
        SecureRandom random = new SecureRandom();
        String accountNumber = new BigInteger(26, random).toString();
        while (getCustomerByOnlineBankingAccount(accountNumber) != null) {
            accountNumber = new BigInteger(26, random).toString();
        }
        System.out.println("****** infrastructure/CustomerAdminSessionBean: generateAccountNumber(): online banking account user ID generated");
        return accountNumber;
    }

    //Generate customer OTP secret
    private String generateOTPSecret() {
        System.out.println("*");
        System.out.println("****** infrastructure/CustomerAdminSessionBean: generateOTPSecret() ******");
        SecureRandom random = new SecureRandom();
//        String secret = new BigInteger(80, random).toString(32).toUpperCase();
        String secret = Base32.random();

        boolean isUnique = false;
        while (!isUnique) {
            Query query = em.createQuery("SELECT c FROM CustomerBasic c WHERE c.customerOTPSecret = :secret");
            query.setParameter("secret", secret);
            List resultList = query.getResultList();
            if (resultList.isEmpty()) {
                isUnique = true;
            } else {
                secret = Base32.random();
            }
        }
        System.out.println("****** infrastructure/CustomerAdminSessionBean: generateOTPSecret(): customer OTP secret generated");
        return secret;
    }

    //Do customer login
    @Override
    public String login(String customerAccount, String password) {
        System.out.println("*");
        System.out.println("****** infrastructure/CustomerAdminSessionBean: login() ******");

        Query query = em.createQuery("SELECT c FROM CustomerBasic c WHERE c.customerOnlineBankingAccountNum = :accountNum");
        query.setParameter("accountNum", customerAccount);

        CustomerBasic thisCustomer;

        List resultList = query.getResultList();
        if (resultList.isEmpty()) {
            System.out.println("****** infrastructure/CustomerAdminSessionBean: login(): login failed: invalid account");
            return "invalidAccount";
        } else {
            thisCustomer = (CustomerBasic) resultList.get(0);
            System.out.println("****** infrastructure/CustomerAdminSessionBean: login(): customer login account: " + thisCustomer.getCustomerOnlineBankingAccountNum());
        }

        if (thisCustomer.getCustomerOnlineBankingAccountLocked().equals("yes")) {
            System.out.println("****** infrastructure/CustomerAdminSessionBean: login(): online banking account locked");
            return "locked";
        } else if (thisCustomer.getCustomerOnlineBankingPassword().equals(password)) {
            System.out.println("****** infrastructure/CustomerAdminSessionBean: login(): login successful");
            return "loggedIn";
        } else {
            System.out.println("****** infrastructure/CustomerAdminSessionBean: login(): login failed: invalid password");
            return "invalidPassword";
        }
    }

    @Override
    public CustomerBasic getCustomerByOnlineBankingAccount(String customerAccount) {
        System.out.println("*");
        System.out.println("****** infrastructure/CustomerAdminSessionBean: getCustomerByOnlineBankingAccount() ******");
        Query query = em.createQuery("SELECT c FROM CustomerBasic c WHERE c.customerOnlineBankingAccountNum = :accountNum");
        query.setParameter("accountNum", customerAccount);
        List resultList = query.getResultList();
        if (resultList.isEmpty()) {
            System.out.println("****** infrastructure/CustomerAdminSessionBean: getCustomerByOnlineBankingAccount(): invalid account number: no result found");
            return null;
        } else {
            System.out.println("****** infrastructure/CustomerAdminSessionBean: getCustomerByOnlineBankingAccount(): valid account number: return CustomerBasic");
            return (CustomerBasic) resultList.get(0);
        }
    }

    // activate online banking account when customer first log in
    @Override
    public String updateOnlineBankingAccount(String accountNum, String password, Long customerId) {
        System.out.println("*");
        System.out.println("****** infrastructure/CustomerAdminSessionBean: updateOnlineBankingAccount() ******");

        Query query = em.createQuery("SELECT c FROM CustomerBasic c WHERE c.customerOnlineBankingAccountNum = :accountNum");
        query.setParameter("accountNum", accountNum);

        if (query.getResultList().isEmpty()) {
            CustomerBasic customer = em.find(CustomerBasic.class, customerId);
            customer.setCustomerOnlineBankingAccountNum(accountNum);
            customer.setCustomerOnlineBankingPassword(password);
            customer.setCustomerStatus("activated");
            em.flush();
            return "activated";
        } else {
            return "invalidAccountNum";
        }
    }

    @Override
    public void updateOnlineBankingPIN(String password, Long customerId) {
        System.out.println("*");
        System.out.println("****** infrastructure/CustomerAdminSessionBean: updateOnlineBankingAccount() ******");

        CustomerBasic customer = em.find(CustomerBasic.class, customerId);
        customer.setCustomerOnlineBankingPassword(password);
        customer.setCustomerStatus("activated");
        em.flush();
    }

    @Override
    public CustomerBasic getCustomerByIdentificationNum(String identificationNum) {
        System.out.println("*");
        System.out.println("****** infrastructure/CustomerAdminSessionBean: getCustomerByIdentificationNum() ******");
        Query query = em.createQuery("SELECT c FROM CustomerBasic c WHERE c.customerIdentificationNum = :idNum");
        query.setParameter("idNum", identificationNum);
        List resultList = query.getResultList();
        if (resultList.isEmpty()) {
            System.out.println("****** infrastructure/CustomerAdminSessionBean: getCustomerAccountById(): no customer found");
            return null;
        } else {
            CustomerBasic customer = (CustomerBasic) resultList.get(0);
            System.out.println("****** infrastructure/CustomerAdminSessionBean: getCustomerAccountById(): get customer");
            return customer;
        }
    }

    @Override
    public Boolean resetPassword(String identificationNum) {
        System.out.println("*");
        System.out.println("****** infrastructure/CustomerAdminSessionBean: getCustomerByIdentificationNum() ******");
        CustomerBasic customer = getCustomerByIdentificationNum(identificationNum);
        if (customer == null) {
            System.out.println("****** infrastructure/CustomerAdminSessionBean: getCustomerByIdentificationNum(): cannot find customer");
            return false;
        } else {
            System.out.println("****** infrastructure/CustomerAdminSessionBean: getCustomerByIdentificationNum(): reset password");
            try {
                String password = generatePassword();
                String hashedPassword = md5Hashing(password + identificationNum.substring(0, 3));
                customer.setCustomerOnlineBankingPassword(hashedPassword);
                if (!customer.getCustomerStatus().equals("new")) {
                    customer.setCustomerStatus("reset");
                }
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

    @Override
    public ArrayList<String> checkExistingService(Long customerId) {
        System.out.println("*");
        System.out.println("****** infrastructure/CustomerAdminSessionBean: checkExistingService() ******");

        ArrayList<String> services = new ArrayList();
        CustomerBasic customer = em.find(CustomerBasic.class, customerId);
        List<BankAccount> account = customer.getBankAccount();
        String service;
        for (int i = 0; i < account.size(); i++) {
            service = account.get(i).getBankAccountType() + "  " + account.get(i).getBankAccountNum();
            services.add(service);
        }
        return services;
    }

    @Override
    public void deleteOnlineBankingAccount(Long customerId) {
        CustomerBasic customer = em.find(CustomerBasic.class, customerId);
        if(customer!=null){
            customer.setCustomerOnlineBankingAccountNum(null);
        customer.setCustomerOnlineBankingPassword(null);
        customer.setCustomerStatus("deleteIBAccount");
        customer.setCustomerOnlineBankingAccountLocked(null);
        em.flush();      
    }
    }

    @Override
    public void recreateOnlineBankingAccount(Long customerId) {
        System.out.println("*");
        System.out.println("****** infrastructure/CustomerAdminSessionBean: recreateOnlineBankingAccount() ******");
        String userId = generateAccountNumber();
        String pin = generatePassword();
        String hashedPIN = pin;

        CustomerBasic customer = em.find(CustomerBasic.class, customerId);
        try {
            hashedPIN = md5Hashing(pin + customer.getCustomerIdentificationNum().substring(0, 3));
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(CustomerAdminSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        customer.setCustomerOnlineBankingAccountNum(userId);
        customer.setCustomerOnlineBankingPassword(hashedPIN);
        customer.setCustomerStatus("new");
        customer.setCustomerOnlineBankingAccountLocked("no");
        em.flush();

        Map emailActions = new HashMap();
        emailActions.put("userId", userId);
        emailActions.put("pin", pin);
        customerEmailSessionBeanLocal.sendEmail(customer, "recreateIBAccount", emailActions);
    }

    @Override
    public String lockCustomerOnlineBankingAccount(Long customerId) {
        System.out.println("*");
        System.out.println("****** infrastructure/CustomerAdminSessionBean: lockCustomerOnlineBankingAccount() ******");
        CustomerBasic customer = em.find(CustomerBasic.class, customerId);
        customer.setCustomerOnlineBankingAccountLocked("yes");
        em.flush();
        
        return "Account Locked!";
        
    }

    @Override
    public String unlockCustomerOnlineBankingAccount(Long customerId) {
        System.out.println("*");
        System.out.println("****** infrastructure/CustomerAdminSessionBean: unlockCustomerOnlineBankingAccount() ******");
        CustomerBasic customer = em.find(CustomerBasic.class, customerId);
        customer.setCustomerOnlineBankingAccountLocked("no");
        em.flush();
        
        return "Account Unlocked!";
    }

    private String md5Hashing(String stringToHash) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        return Arrays.toString(md.digest(stringToHash.getBytes()));
    }
}
