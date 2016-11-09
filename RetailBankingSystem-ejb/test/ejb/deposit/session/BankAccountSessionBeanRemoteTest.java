package ejb.deposit.session;

import ejb.customer.entity.CustomerBasic;
import ejb.deposit.entity.AccTransaction;
import ejb.deposit.entity.BankAccount;
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
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

/**
 *
 * @author Nicole
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BankAccountSessionBeanRemoteTest {

    BankAccountSessionBeanRemote bankAccountSessionBeanRemote = lookupBankAccountSessionBeanRemote();

    public BankAccountSessionBeanRemoteTest() {
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
    public void testRetrieveBankAccountById() {
        System.out.println("retrieveBankAccountById");
        Long bankAccountId = (long) 1;
        String expResult = "5061102";
        BankAccount result = bankAccountSessionBeanRemote.retrieveBankAccountById(bankAccountId);
        assertEquals(expResult, result.getBankAccountNum());
    }

    @Test
    public void testRetrieveBankAccountByNum() {
        System.out.println("retrieveBankAccountByNum");
        String bankAccountNum = "5061102";
        Long expResult = (long) 1;
        BankAccount result = bankAccountSessionBeanRemote.retrieveBankAccountByNum(bankAccountNum);
        assertEquals(expResult, result.getBankAccountId());
    }

    @Test
    public void testRetrieveBankAccountByCusIC() {
        System.out.println("retrieveBankAccountByCusIC");
        String customerIdentificationNum = "G12345678";
        int expResult = 2;
        List<BankAccount> result = bankAccountSessionBeanRemote.retrieveBankAccountByCusIC(customerIdentificationNum);
        assertEquals(expResult, result.size());
    }

    @Test
    public void testRetrieveCustomerBasicById() {
        System.out.println("retrieveCustomerBasicById");
        Long customerBasicId = (long) 1;
        String expResult = "John Lee";
        CustomerBasic result = bankAccountSessionBeanRemote.retrieveCustomerBasicById(customerBasicId);
        assertEquals(expResult, result.getCustomerName());
    }

    @Test
    public void testRetrieveAccTransactionById() {
        System.out.println("retrieveAccTransactionById");
        Long accTransactionId = (long) 1;
        String expResult = "Basic Savings Account - 5061102";
        AccTransaction result = bankAccountSessionBeanRemote.retrieveAccTransactionById(accTransactionId);
        assertEquals(expResult, result.getTransactionRef());
    }

    @Test
    public void testDeleteAccount() {
        System.out.println("deleteAccount");
        String bankAccountNum = "2345243";
        String expResult = "Successfully deleted!";
        String result = bankAccountSessionBeanRemote.deleteAccount(bankAccountNum);
        assertEquals(expResult, result);
    }

    @Test
    public void testCheckAccountDuplication() {
        System.out.println("checkAccountDuplication");
        String bankAccountNum = "5061102";
        String expResult = "Duplicated";
        String result = bankAccountSessionBeanRemote.checkAccountDuplication(bankAccountNum);
        assertEquals(expResult, result);
    }

    @Test
    public void testGenerateBankAccount() {
        System.out.println("generateBankAccount");
        String result = bankAccountSessionBeanRemote.generateBankAccount();
        assertNotNull(result);
    }

    @Test
    public void testCheckExistence() {
        System.out.println("checkExistence");
        String customerIdentificationNum = "G12345678";
        boolean expResult = true;
        boolean result = bankAccountSessionBeanRemote.checkExistence(customerIdentificationNum);
        assertEquals(expResult, result);
    }

    @Test
    public void testCheckOnlyOneAccount() {
        System.out.println("checkOnlyOneAccount");
        String customerIdentificationNum = "G12345678";
        boolean expResult = false;
        boolean result = bankAccountSessionBeanRemote.checkOnlyOneAccount(customerIdentificationNum);
        assertEquals(expResult, result);
    }

    @Test
    public void testRetrieveCustomerBasicByAccNum() {
        System.out.println("retrieveCustomerBasicByAccNum");
        String bankAccountNum = "5061102";
        String expResult = "John Lee";
        CustomerBasic result = bankAccountSessionBeanRemote.retrieveCustomerBasicByAccNum(bankAccountNum);
        assertEquals(expResult, result.getCustomerName());
    }

    @Test
    public void testResetDailyTransferLimit() {
        System.out.println("resetDailyTransferLimit");
        String expResult = "Daily Transfer Limit Reset Successfully!";
        String result = bankAccountSessionBeanRemote.resetDailyTransferLimit();
        assertEquals(expResult, result);
    }

    @Test
    public void testUpdateDailyTransferLimit() {
        System.out.println("updateDailyTransferLimit");
        String bankAccountNum = "5061102";
        String dailyTransferLimit = "2000";
        String expResult = "Daily Transfer Limit Update Successfully!";
        String result = bankAccountSessionBeanRemote.updateDailyTransferLimit(bankAccountNum, dailyTransferLimit);
        assertEquals(expResult, result);
    }

    @Test
    public void testAddNewAccountOneTime() {
        System.out.println("addNewAccountOneTime");
        String bankAccountNum = "6242352";
        String bankAccountType = "Bonus Savings Account";
        String totalBankAccountBalance = "20000";
        String availableBankAccountBalance = "20000";
        String transferDailyLimit = "1000";
        String transferBalance = "0";
        String bankAccountStatus = "Active";
        String bankAccountMinSaving = "1000";
        String bankAccountDepositPeriod = "";
        String currentFixedDepositPeriod = "";
        String fixedDepositStatus = null;
        Double statementDateDouble = null;
        Long currentTimeMilis = null;
        Long customerBasicId = (long)2;
        Long interestId = (long)1;
        Long expResult = (long) 4;
        Long result = bankAccountSessionBeanRemote.addNewAccountOneTime(bankAccountNum, bankAccountType, totalBankAccountBalance, availableBankAccountBalance, transferDailyLimit, transferBalance, bankAccountStatus, bankAccountMinSaving, bankAccountDepositPeriod, currentFixedDepositPeriod, fixedDepositStatus, statementDateDouble, currentTimeMilis, customerBasicId, interestId);
        assertEquals(expResult, result);

    }

    @Test
    public void testUpdateBankAccountAvailableBalance() {
        System.out.println("updateBankAccountAvailableBalance");
        String bankAccountNum = "5061102";
        String availableBankAccountBalance = "800000";
        String expResult = "Successfully Updated!";
        String result = bankAccountSessionBeanRemote.updateBankAccountAvailableBalance(bankAccountNum, availableBankAccountBalance);
        assertEquals(expResult, result);

    }

    @Test
    public void testUpdateBankAccountBalance() {
        System.out.println("updateBankAccountBalance");
        String bankAccountNum = "5061102";
        String availableBankAccountBalance = "820000";
        String totalBankAccountBalance = "820000";
        String expResult = "Successfully Updated!";
        String result = bankAccountSessionBeanRemote.updateBankAccountBalance(bankAccountNum, availableBankAccountBalance, totalBankAccountBalance);
        assertEquals(expResult, result);
    }

    private BankAccountSessionBeanRemote lookupBankAccountSessionBeanRemote() {
        try {
            Context c = new InitialContext();
            return (BankAccountSessionBeanRemote) c.lookup("java:global/RetailBankingSystem/RetailBankingSystem-ejb/BankAccountSessionBean!ejb.deposit.session.BankAccountSessionBeanRemote");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

}
