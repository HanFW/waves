/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.deposit.session;

import ejb.deposit.entity.Payee;
import ejb.deposit.entity.RegularPayee;
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
public class RegularPayeeSessionBeanRemoteTest {
    
    RegularPayeeSessionBeanRemote regularPayeeSessionBeanRemote = lookupRegularPayeeSessionBeanRemote();
    
    public RegularPayeeSessionBeanRemoteTest() {
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
    public void testAddNewRegularPayee() {
        System.out.println("addNewRegularPayee");
        String payeeName = "John Lee";
        String payeeAccountNum = "5061102";
        String payeeAccountType = "Basic Savings Account";
        String lastTransactionDate = "";
        String payeeType = "Regular";
        Long customerBasicId = (long)2;
        Long expResult = (long)3;
        Long result = regularPayeeSessionBeanRemote.addNewRegularPayee(payeeName, payeeAccountNum, payeeAccountType, lastTransactionDate, payeeType, customerBasicId);
        assertEquals(expResult, result);
        
    }


    @Test
    public void testRetrieveRegularPayeeByCusId() {
        System.out.println("retrieveRegularPayeeByCusId");
        Long customerBasicId = (long)2;
        int expResult = 1;
        List<RegularPayee> result = regularPayeeSessionBeanRemote.retrieveRegularPayeeByCusId(customerBasicId);
        assertEquals(expResult, result.size());
        
    }


    @Test
    public void testRetrieveRegularPayeeByName() {
        System.out.println("retrieveRegularPayeeByName");
        String payeeName = "Jack";
        Long expResult = (long)1;
        Payee result = regularPayeeSessionBeanRemote.retrieveRegularPayeeByName(payeeName);
        assertEquals(expResult, result.getPayeeId());
        
    }

      private RegularPayeeSessionBeanRemote lookupRegularPayeeSessionBeanRemote() {
        try {
            Context c = new InitialContext();
            return (RegularPayeeSessionBeanRemote) c.lookup("java:global/RetailBankingSystem/RetailBankingSystem-ejb/RegularPayeeSessionBean!ejb.deposit.session.RegularPayeeSessionBeanRemote");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
    
}
