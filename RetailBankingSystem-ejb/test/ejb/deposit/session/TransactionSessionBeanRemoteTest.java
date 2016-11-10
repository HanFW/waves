/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.deposit.session;

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
public class TransactionSessionBeanRemoteTest {

    TransactionSessionBeanRemote transactionSessionBeanRemote = lookupTransactionSessionBeanRemote();

    public TransactionSessionBeanRemoteTest() {
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
        BankAccount result = transactionSessionBeanRemote.retrieveBankAccountById(bankAccountId);
        assertEquals(expResult, result.getBankAccountNum());

    }

    @Test
    public void testRetrieveAccTransactionByBankNum() {
        System.out.println("retrieveAccTransactionByBankNum");
        String bankAccountNum = "5061102";
        int expResult = 3;
        List<AccTransaction> result = transactionSessionBeanRemote.retrieveAccTransactionByBankNum(bankAccountNum);
        assertEquals(expResult, result.size());
    }

    @Test
    public void testRetrieveBankAccountByNum() {
        System.out.println("retrieveBankAccountByNum");
        String bankAccountNum = "5061102";
        Long expResult = (long) 1;
        BankAccount result = transactionSessionBeanRemote.retrieveBankAccountByNum(bankAccountNum);
        assertEquals(expResult, result.getBankAccountId());
    }
  
   
    @Test
    public void testCashDeposit() {
        System.out.println("cashDeposit");
        String bankAccountNum = "2345264";
        String depositAmt = "50000";
        Long expResult = (long) 4;
        Long result = transactionSessionBeanRemote.cashDeposit(bankAccountNum, depositAmt);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testCashWithdraw() {
        System.out.println("cashWithdraw");
        String bankAccountNum = "2345264";
        String withdrawAmt = "1000";
        Long expResult = (long) 5;
        Long result = transactionSessionBeanRemote.cashWithdraw(bankAccountNum, withdrawAmt);
        assertEquals(expResult, result);
    }

    @Test
    public void testFundTransfer() {
        System.out.println("fundTransfer");
        String fromAccount = "2345264";
        String toAccount = "5061102";
        String transferAmt = "200";
        Long expResult = (long) 7;
        Long result = transactionSessionBeanRemote.fundTransfer(fromAccount, toAccount, transferAmt);
        assertEquals(expResult, result);
    }
    
     @Test
    public void testFastTransfer() {
        System.out.println("fastTransfer");
        String fromBankAccount = "2345264";
        String toBankAccount = "5061102";
        Double transferAmt = 200.0;
        String expResult = "Successfully Transfered!";
        String result = transactionSessionBeanRemote.fastTransfer(fromBankAccount, toBankAccount, transferAmt);
        assertEquals(expResult, result);
    }

    @Test
    public void testCheckAccountActivation() {
        System.out.println("checkAccountActivation");
        String bankAccountNum = "5061102";
        String initialDepositAmount = "80000";
        String expResult = "Activated";
        String result = transactionSessionBeanRemote.checkAccountActivation(bankAccountNum, initialDepositAmount);
        assertEquals(expResult, result);
    }

    
     @Test
    public void testAddNewTransaction() {
        System.out.println("addNewTransaction");
        String transactionDate = "10/Nov/2016";
        String transactionCode = "TRF";
        String transactionRef = "Basic Savings Account - 2345243";
        String accountDebit = "300";
        String accountCredit = "0";
        Long transactionDateMilis = null;
        Long bankAccountId = (long) 3;
        Long expResult = (long) 3;
        Long result = transactionSessionBeanRemote.addNewTransaction(transactionDate, transactionCode, transactionRef, accountDebit, accountCredit, transactionDateMilis, bankAccountId);
        assertEquals(expResult, result);

    }
    
    @Test
    public void testDeleteAccTransaction() {
        System.out.println("deleteAccTransaction");
        Long transactionId = (long)9;
        String expResult = "Successfully Deleted!";
        String result = transactionSessionBeanRemote.deleteAccTransaction(transactionId);
        assertEquals(expResult, result);
    }
    
    private TransactionSessionBeanRemote lookupTransactionSessionBeanRemote() {
        try {
            Context c = new InitialContext();
            return (TransactionSessionBeanRemote) c.lookup("java:global/RetailBankingSystem/RetailBankingSystem-ejb/TransactionSessionBean!ejb.deposit.session.TransactionSessionBeanRemote");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
