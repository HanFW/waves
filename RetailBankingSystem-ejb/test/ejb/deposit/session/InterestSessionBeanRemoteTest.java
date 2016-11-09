/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.deposit.session;

import ejb.deposit.entity.Interest;
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
public class InterestSessionBeanRemoteTest {
    
    InterestSessionBeanRemote interestSessionBeanRemote = lookupInterestSessionBeanRemote();
    
    public InterestSessionBeanRemoteTest() {
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
    public void testAddNewInterest() {
        System.out.println("addNewInterest");
        String dailyInterest = "0.05";
        String monthlyInterest = "0.05";
        String isTransfer = "Yes";
        String isWithdraw = "No";
        Long expResult = (long)4;
        Long result = interestSessionBeanRemote.addNewInterest(dailyInterest, monthlyInterest, isTransfer, isWithdraw);
        assertEquals(expResult, result);
    }


    @Test
    public void testRetrieveInterestById() {
        System.out.println("retrieveInterestById");
        Long interestId = (long)2;
        String expResult = "0.06";
        Interest result = interestSessionBeanRemote.retrieveInterestById(interestId);
        assertEquals(expResult, result.getMonthlyInterest());
    }

    @Test
    public void testDeleteInterest() {
        System.out.println("deleteInterest");
        Long interestId = (long)3;
        String expResult = "Successfully Deleted!";
        String result = interestSessionBeanRemote.deleteInterest(interestId);
        assertEquals(expResult, result);
    }

    private InterestSessionBeanRemote lookupInterestSessionBeanRemote() {
        try {
            Context c = new InitialContext();
            return (InterestSessionBeanRemote) c.lookup("java:global/RetailBankingSystem/RetailBankingSystem-ejb/InterestSessionBean!ejb.deposit.session.InterestSessionBeanRemote");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
    
}
