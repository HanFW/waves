/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.infrastructure.session;

import ejb.card.session.DebitCardSessionBean;
import ejb.customer.entity.CustomerBasic;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

/**
 *
 * @author Nicole
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CustomerAdminSessionBeanRemoteTest {

    CustomerAdminSessionBeanRemote customerAdminSessionBeanRemote = lookupCustomerAdminSessionBeanRemote();

    public CustomerAdminSessionBeanRemoteTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testCreateOnlineBankingAccount() {
        System.out.println("createOnlineBankingAccount");
        Long customerId = (long) 1;
        String expResult = "not a new customer";
        String result = customerAdminSessionBeanRemote.createOnlineBankingAccount(customerId);
        assertEquals(expResult, result);

    }

    @Test
    public void testLogin1() {
        System.out.println("login");
        String customerAccount = "jack1986";
        String customerIdentificationNum = "G11223344";
        String password = "11223344";
        String hashedCustomerOnlineBankingPassword ="";
        try {
        hashedCustomerOnlineBankingPassword = md5Hashing(password + customerIdentificationNum.substring(0, 3));
        } catch (NoSuchAlgorithmException ex) {      
        }
        String expResult = "loggedIn";
        String result = customerAdminSessionBeanRemote.login(customerAccount, hashedCustomerOnlineBankingPassword);
        assertEquals(expResult, result);
    }
    
     private String md5Hashing(String stringToHash) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        return Arrays.toString(md.digest(stringToHash.getBytes()));
    }

    @Test
    public void testLogin2() {
        System.out.println("login");
        String customerAccount = "johnlee2";
        String password = "12345678";
        String expResult = "invalidAccount";
        String result = customerAdminSessionBeanRemote.login(customerAccount, password);
        assertEquals(expResult, result);

    }

    public void testLogin3() {
        System.out.println("login");
        String customerAccount = "johnlee";
        String password = "11111111";
        String expResult = "invalidPassword";
        String result = customerAdminSessionBeanRemote.login(customerAccount, password);
        assertEquals(expResult, result);

    }

    @Test
    public void testGetCustomerByOnlineBankingAccount() {
        System.out.println("getCustomerByOnlineBankingAccount");
        String customerAccount = "johnlee";
        String expResult = "John Lee";
        CustomerBasic result = customerAdminSessionBeanRemote.getCustomerByOnlineBankingAccount(customerAccount);
        assertEquals(expResult, result.getCustomerName());
    }

    @Test
    public void testUpdateOnlineBankingAccount1() {
        System.out.println("updateOnlineBankingAccount");
        String accountNum = "andynew";
        String password = "11223344";
        Long customerId = (long) 4;
        String expResult = "activated";
        String result = customerAdminSessionBeanRemote.updateOnlineBankingAccount(accountNum, password, customerId);
        assertEquals(expResult, result);
    }

    @Test
    public void testUpdateOnlineBankingAccount2() {
        System.out.println("updateOnlineBankingAccount");
        String accountNum = "tome1996";
        String password = "12345678";
        Long customerId = (long) 5;
        String expResult = "invalidAccountNum";
        String result = customerAdminSessionBeanRemote.updateOnlineBankingAccount(accountNum, password, customerId);
        assertEquals(expResult, result);
    }

    @Test
    public void testGetCustomerByIdentificationNum() {
        System.out.println("getCustomerByIdentificationNum");
        String identificationNum = "S1729390K";
        String expResult = "Tom";
        CustomerBasic result = customerAdminSessionBeanRemote.getCustomerByIdentificationNum(identificationNum);
        assertEquals(expResult, result.getCustomerName());
    }

    @Test
    public void testResetPassword1() {
        System.out.println("resetPassword");
        String identificationNum = "S1729390K";
        Boolean expResult = true;
        Boolean result = customerAdminSessionBeanRemote.resetPassword(identificationNum);
        assertEquals(expResult, result);
    }

    @Test
    public void testResetPassword2() {
        System.out.println("resetPassword");
        String identificationNum = "G22222222";
        Boolean expResult = false;
        Boolean result = customerAdminSessionBeanRemote.resetPassword(identificationNum);
        assertEquals(expResult, result);
    }


    @Test
    public void testLockCustomerOnlineBankingAccount() {
        System.out.println("lockCustomerOnlineBankingAccount");
        Long customerId = (long) 3;
        String expResult = "Account Locked!";
        String result = customerAdminSessionBeanRemote.lockCustomerOnlineBankingAccount(customerId);
        assertEquals(expResult, result);

    }

    @Test
    public void testUnlockCustomerOnlineBankingAccount() {
        System.out.println("unlockCustomerOnlineBankingAccount");
        Long customerId = (long) 3;
        String expResult = "Account Unlocked!";
        String result = customerAdminSessionBeanRemote.unlockCustomerOnlineBankingAccount(customerId);
        assertEquals(expResult, result);
    }

    private CustomerAdminSessionBeanRemote lookupCustomerAdminSessionBeanRemote() {
        try {
            Context c = new InitialContext();
            return (CustomerAdminSessionBeanRemote) c.lookup("java:global/RetailBankingSystem/RetailBankingSystem-ejb/CustomerAdminSessionBean!ejb.infrastructure.session.CustomerAdminSessionBeanRemote");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

}
