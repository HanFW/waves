/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.customer.session;

import ejb.card.session.DebitCardSessionBean;
import ejb.customer.entity.CustomerAdvanced;
import ejb.customer.entity.CustomerBasic;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
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

/**
 *
 * @author Nicole
 */
public class CRMCustomerSessionBeanRemoteTest {

    CRMCustomerSessionBeanRemote systemUserSessionRemote = lookupCRMCustomerSessionBeanRemote();

    public CRMCustomerSessionBeanRemoteTest() {
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

    /**
     * Test of getCustomer method, of class CRMCustomerSessionBeanRemote.
     */
    @Test
    public void testGetCustomer() {
        System.out.println("getCustomer");
        String onlineBankingAccountNum = "johnlee";
        String expResult = "G11223344";
        CustomerBasic result = systemUserSessionRemote.getCustomer(onlineBankingAccountNum);
        assertEquals(expResult, result.getCustomerIdentificationNum());
    }

    /**
     * Test of getMyCustomerBasicProfile method, of class
     * CRMCustomerSessionBeanRemote.
     */
//    @Test
//    public void testGetMyCustomerBasicProfile() {
//        System.out.println("getMyCustomerBasicProfile");
//        String onlineBankingAccountNum = "";
//
//        List<CustomerBasic> expResult = null;
//        List<CustomerBasic> result = systemUserSessionRemote.getMyCustomerBasicProfile(onlineBankingAccountNum);
//        assertEquals(expResult, result);
//
//    }
    /**
     * Test of getCustomerAdvancedByAccNum method, of class
     * CRMCustomerSessionBeanRemote.
     */
    @Test
    public void testGetCustomerAdvancedByAccNum() {
        System.out.println("getCustomerAdvancedByAccNum");
        String onlineBankingAccountNum = "";
        CustomerAdvanced expResult = null;
        CustomerAdvanced result = systemUserSessionRemote.getCustomerAdvancedByAccNum(onlineBankingAccountNum);
        assertEquals(expResult, result);

    }

    /**
     * Test of getAllCustomerBasicProfile method, of class
     * CRMCustomerSessionBeanRemote.
     */
//    @Test
//    public void testGetAllCustomerBasicProfile() {
//        System.out.println("getAllCustomerBasicProfile");
//
//        List<CustomerBasic> expResult = null;
//        List<CustomerBasic> result = systemUserSessionRemote.getAllCustomerBasicProfile();
//        assertEquals(expResult, result);
//
//    }
    /**
     * Test of updateCustomerOnlineBankingAccountPIN method, of class
     * CRMCustomerSessionBeanRemote.
     */
    @Test
    public void testUpdateCustomerOnlineBankingAccountPIN1() {
        System.out.println("updateCustomerOnlineBankingAccountPIN");
        String onlineBankingAccountNum = "johnlee";
        String inputPassowrd = "12345678";
        String newPassword = "11223344";
        String expResult = "Update Successful";
        String result = "";
        CustomerBasic cb = systemUserSessionRemote.getCustomer(onlineBankingAccountNum);
        try {
            String hashedInputPassword = md5Hashing(inputPassowrd + cb.getCustomerIdentificationNum().substring(0, 3));
            String hashedNewPassword = md5Hashing(newPassword + cb.getCustomerIdentificationNum().substring(0, 3));
            result = systemUserSessionRemote.updateCustomerOnlineBankingAccountPIN(onlineBankingAccountNum, hashedInputPassword, hashedNewPassword);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(DebitCardSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        assertEquals(expResult, result);
    }

    private String md5Hashing(String stringToHash) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        return Arrays.toString(md.digest(stringToHash.getBytes()));
    }

    @Test
    public void testUpdateCustomerOnlineBankingAccountPIN2() {
        System.out.println("updateCustomerOnlineBankingAccountPIN");
        String onlineBankingAccountNum = "johnlee";
        String inputPassowrd = "22222222";
        String newPassword = "11223344";
        String expResult = "Incorrect Current Password";
        String result = "";
        CustomerBasic cb = systemUserSessionRemote.getCustomer(onlineBankingAccountNum);
        try {
            String hashedInputPassword = md5Hashing(inputPassowrd + cb.getCustomerIdentificationNum().substring(0, 3));
            String hashedNewPassword = md5Hashing(newPassword + cb.getCustomerIdentificationNum().substring(0, 3));
            result = systemUserSessionRemote.updateCustomerOnlineBankingAccountPIN(onlineBankingAccountNum, hashedInputPassword, hashedNewPassword);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(DebitCardSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        assertEquals(expResult, result);
    }

    @Test
    public void testUpdateCustomerOnlineBankingAccountPIN3() {
        System.out.println("updateCustomerOnlineBankingAccountPIN");
        String onlineBankingAccountNum = "fengwei";
        String inputPassowrd = "11223344";
        String newPassword = "11223344";
        String expResult = "Same Password";
        String result = "";
        CustomerBasic cb = systemUserSessionRemote.getCustomer(onlineBankingAccountNum);
        try {
            String hashedInputPassword = md5Hashing(inputPassowrd + cb.getCustomerIdentificationNum().substring(0, 3));
            String hashedNewPassword = md5Hashing(newPassword + cb.getCustomerIdentificationNum().substring(0, 3));
            result = systemUserSessionRemote.updateCustomerOnlineBankingAccountPIN(onlineBankingAccountNum, hashedInputPassword, hashedNewPassword);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(DebitCardSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        assertEquals(expResult, result);
    }

    /**
     * Test of updateCustomerBasicProfile method, of class
     * CRMCustomerSessionBeanRemote.
     */
    @Test
    public void testUpdateCustomerBasicProfile1() {
        System.out.println("updateCustomerBasicProfile");
        String customerOnlineBankingAccountNum = "johnlee";
        String customerNationality = "Chinese";
        String customerCountryOfResidence = "Singapore";
        String customerMaritalStatus = "Single";
        String customerOccupation = "Student";
        String customerCompany = "NUS";
        String customerEmail = "erhe@hotmail.com";
        String customerMobile = "98678075";
        String customerAddress = "PGP, BLK 24, #04-G, 118429";
        String customerPostal = "118429";

        String expResult = "Update Successful";
        String result = systemUserSessionRemote.updateCustomerBasicProfile(customerOnlineBankingAccountNum, customerNationality, customerCountryOfResidence, customerMaritalStatus, customerOccupation, customerCompany, customerEmail, customerMobile, customerAddress, customerPostal);
        assertEquals(expResult, result);
    }

    @Test
    public void testUpdateCustomerBasicProfile2() {
        System.out.println("updateCustomerBasicProfile");
        String customerOnlineBankingAccountNum = "johnlee2";
        String customerNationality = "Chinese";
        String customerCountryOfResidence = "Singapore";
        String customerMaritalStatus = "Single";
        String customerOccupation = "Student";
        String customerCompany = "NUS";
        String customerEmail = "erhe@hotmail.com";
        String customerMobile = "98678075";
        String customerAddress = "PGP, BLK 24, #04-G, 118429";
        String customerPostal = "118429";
        
        String expResult = "Cannot find your profile, please contact with our customer service";
        String result = systemUserSessionRemote.updateCustomerBasicProfile(customerOnlineBankingAccountNum, customerNationality, customerCountryOfResidence, customerMaritalStatus, customerOccupation, customerCompany, customerEmail, customerMobile, customerAddress, customerPostal);
        assertEquals(expResult, result);
    }

    /**
     * Test of updateCustomerAdvancedProfile method, of class
     * CRMCustomerSessionBeanRemote.
     */
    @Test
    public void testUpdateCustomerAdvancedProfile() {
        System.out.println("updateCustomerAdvancedProfile");
        Long customerAdvancedId = null;
        String education = "";
        String incomeMonthly = "";
        String jobDuration = "";
        String jobStatus = "";
        String jobIndustry = "";
        String jobType = "";
        String numOfDependent = "";
        String residentialStatus = "";
        String yearInResidence = "";

        String expResult = "";
        String result = systemUserSessionRemote.updateCustomerAdvancedProfile(customerAdvancedId, education, incomeMonthly, jobDuration, jobStatus, jobIndustry, jobType, numOfDependent, residentialStatus, yearInResidence);
        assertEquals(expResult, result);

    }

    /**
     * Test of addNewCustomerBasic method, of class
     * CRMCustomerSessionBeanRemote.
     */
    @Test
    public void testAddNewCustomerBasic() {
        System.out.println("addNewCustomerBasic");
        String customerName = "Mark Zuckerberg";
        String customerSalutation = "Mr";
        String customerIdentificationNum = "M1234567Z";
        String customerGender = "male";
        String customerEmail = "erhe@hotmail.com";
        String customerMobile = "98678075";
        String customerDateOfBirth = "02/Jul/1992";
        String customerNationality = "Singapore";
        String customerCountryOfResidence = "Singapore";
        String customerRace = "Chinese";
        String customerMaritalStatus = "Married";
        String customerOccupation = "Student";
        String customerCompany = "NUS";
        String customerAddress = "Street 1";
        String customerPostal = "123456";
        String customerOnlineBankingAccountNum = "testAccount";
        String customerOnlineBankingPassword = "123456";
        String hashedCustomerOnlineBankingPassword = "";
         try {          
            hashedCustomerOnlineBankingPassword = md5Hashing(customerOnlineBankingPassword + customerIdentificationNum.substring(0, 3));
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(DebitCardSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        byte[] customerSignature = null;
        String newCustomer = "";
        Long expResult = (long)7;
        Long result = systemUserSessionRemote.addNewCustomerBasic(customerName, customerSalutation, customerIdentificationNum, customerGender, customerEmail, customerMobile, customerDateOfBirth, customerNationality, customerCountryOfResidence, customerRace, customerMaritalStatus, customerOccupation, customerCompany, customerAddress, customerPostal, customerOnlineBankingAccountNum, hashedCustomerOnlineBankingPassword, customerSignature, newCustomer);
        assertEquals(expResult, result);

    }

    /**
     * Test of deleteCustomerBasic method, of class
     * CRMCustomerSessionBeanRemote.
     */
    @Test
    public void testDeleteCustomerBasic() {
        System.out.println("deleteCustomerBasic");
        String customerIdentificationNum = "M1234567Z";

        String expResult = "Delete Customer Successfully";
        String result = systemUserSessionRemote.deleteCustomerBasic(customerIdentificationNum);
        assertEquals(expResult, result);

    }

    /**
     * Test of retrieveCustomerBasicByIC method, of class
     * CRMCustomerSessionBeanRemote.
     */
    @Test
    public void testRetrieveCustomerBasicByIC() {
        System.out.println("retrieveCustomerBasicByIC");
        String customerIdentificationNum = "G11223344";
        String expResult = "John Lee";
        CustomerBasic result = systemUserSessionRemote.retrieveCustomerBasicByIC(customerIdentificationNum);
        assertEquals(expResult, result.getCustomerName());

    }

    /**
     * Test of retrieveCustomerAdvancedByAdId method, of class
     * CRMCustomerSessionBeanRemote.
     */
    @Test
    public void testRetrieveCustomerAdvancedByAdId() {
        System.out.println("retrieveCustomerAdvancedByAdId");
        Long customerAdvancedId = null;
        CustomerAdvanced expResult = null;
        CustomerAdvanced result = systemUserSessionRemote.retrieveCustomerAdvancedByAdId(customerAdvancedId);
        assertEquals(expResult, result);

    }

    /**
     * Test of deleteCustomerAdvanced method, of class
     * CRMCustomerSessionBeanRemote.
     */
//    @Test
//    public void testDeleteCustomerAdvanced() {
//        System.out.println("deleteCustomerAdvanced");
//        Long customerAdvancedId = null;
//        systemUserSessionRemote.deleteCustomerAdvanced(customerAdvancedId);
//    }

    /**
     * Test of getCustomerAdvancedById method, of class
     * CRMCustomerSessionBeanRemote.
     */
    @Test
    public void testGetCustomerAdvancedById() {
        System.out.println("getCustomerAdvancedById");
        Long id = (long)1;
        CustomerAdvanced result = systemUserSessionRemote.getCustomerAdvancedById(id);
        assertNotNull(result);

    }

    /**
     * Test of addNewCustomerOneTime method, of class
     * CRMCustomerSessionBeanRemote.
     */
//    @Test
//    public void testAddNewCustomerOneTime() {
//        System.out.println("addNewCustomerOneTime");
//        String customerName = "";
//        String customerSalutation = "";
//        String customerIdentificationNum = "";
//        String customerGender = "";
//        String customerEmail = "";
//        String customerMobile = "";
//        String customerDateOfBirth = "";
//        String customerNationality = "";
//        String customerCountryOfResidence = "";
//        String customerRace = "";
//        String customerMaritalStatus = "";
//        String customerOccupation = "";
//        String customerCompany = "";
//        String customerAddress = "";
//        String customerPostal = "";
//        String customerOnlineBankingAccountNum = "";
//        String customerOnlineBankingPassword = "";
//        byte[] customerSignature = null;
//        String newCustomer = "";
//
//        Long expResult = null;
//        Long result = systemUserSessionRemote.addNewCustomerOneTime(customerName, customerSalutation, customerIdentificationNum, customerGender, customerEmail, customerMobile, customerDateOfBirth, customerNationality, customerCountryOfResidence, customerRace, customerMaritalStatus, customerOccupation, customerCompany, customerAddress, customerPostal, customerOnlineBankingAccountNum, customerOnlineBankingPassword, customerSignature, newCustomer);
//        assertEquals(expResult, result);
//
//    }

    /**
     * Test of getAllNewCustomer method, of class CRMCustomerSessionBeanRemote.
     */
    @Test
    public void testGetAllNewCustomer() {
        System.out.println("getAllNewCustomer");
        int expResult = 1;
        List<CustomerBasic> result = systemUserSessionRemote.getAllNewCustomer();
        assertEquals(expResult, result.size());

    }

    /**
     * Test of updateCustomerMobile method, of class
     * CRMCustomerSessionBeanRemote.
     */
//    @Test
//    public void testUpdateCustomerMobile() {
//        System.out.println("updateCustomerMobile");
//        Long customerId = (long)5;
//        String customerMobile = "98678075";
//        systemUserSessionRemote.updateCustomerMobile(customerId, customerMobile);
//
//    }

    /**
     * Test of getCustomerBasicById method, of class
     * CRMCustomerSessionBeanRemote.
     */
    @Test
    public void testGetCustomerBasicById() {
        System.out.println("getCustomerBasicById");
        Long customerId = (long) 6;
        String expResult = "G11223344";
        CustomerBasic result = systemUserSessionRemote.getCustomerBasicById(customerId);
        assertEquals(expResult, result.getCustomerIdentificationNum());

    }

    /**
     * Test of addNewCustomerAdvanced method, of class
     * CRMCustomerSessionBeanRemote.
     */
    @Test
    public void testAddNewCustomerAdvanced() {
        System.out.println("addNewCustomerAdvanced");
        int customerNumOfDependents = 4;
        String customerEducation = "Degree";
        String customerResidentialStatus = "Rented";
        int customerLengthOfResidence = 3;
        String customerIndustryType = "IT";
        int customerLengthOfCurrentJob = 2;
        String customerEmploymentStatus = "Employee";
        double customerMonthlyFixedIncome = 7000;
        String customerResidentialType = "BHD";
        String customerCompanyAddress = "Street 2";
        String customerCompanyPostal = "112334";
        String customerCurrentPosition = "Manager";
        String customerCurrentJobTitle = "Manager";
        String customerPreviousCompany = "Apple";
        int customerLengthOfPreviousJob = 3;
        double customerOtherMonthlyIncome = 5000;
        String customerOtherMonthlyIncomeSource = "Software Development";
        Long expResult = (long)6;
        Long result = systemUserSessionRemote.addNewCustomerAdvanced(customerNumOfDependents, customerEducation, customerResidentialStatus, customerLengthOfResidence, customerIndustryType, customerLengthOfCurrentJob, customerEmploymentStatus, customerMonthlyFixedIncome, customerResidentialType, customerCompanyAddress, customerCompanyPostal, customerCurrentPosition, customerCurrentJobTitle, customerPreviousCompany, customerLengthOfPreviousJob, customerOtherMonthlyIncome, customerOtherMonthlyIncomeSource);
        assertEquals(expResult, result);

    }

    private CRMCustomerSessionBeanRemote lookupCRMCustomerSessionBeanRemote() {
        try {
            Context c = new InitialContext();
            return (CRMCustomerSessionBeanRemote) c.lookup("java:global/RetailBankingSystem/RetailBankingSystem-ejb/CRMCustomerSessionBean!ejb.customer.session.CRMCustomerSessionBeanRemote");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}