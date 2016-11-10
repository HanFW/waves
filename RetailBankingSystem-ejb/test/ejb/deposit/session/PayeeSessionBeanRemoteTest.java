/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.deposit.session;

import ejb.deposit.entity.Payee;
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
public class PayeeSessionBeanRemoteTest {
    
    PayeeSessionBeanRemote payeeSessionBeanRemote = lookupPayeeSessionBeanRemote();
    
    public PayeeSessionBeanRemoteTest() {
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
    public void testRetrievePayeeById() {
        System.out.println("retrievePayeeById");
        Long payeeId = (long)1;
        String expResult = "2345243";
        Payee result = payeeSessionBeanRemote.retrievePayeeById(payeeId);
        assertEquals(expResult, result.getPayeeAccountNum());
    }


    @Test
    public void testRetrievePayeeByCusId() {
        System.out.println("retrievePayeeByCusId");
        Long customerBasicId = (long)1;
        int expResult = 1;
        List<Payee> result = payeeSessionBeanRemote.retrievePayeeByCusId(customerBasicId);
        assertEquals(expResult, result.size());
    }


    @Test
    public void testRetrievePayeeByNum() {
        System.out.println("retrievePayeeByNum");
        String payeeAccountNum = "2345264";
        Payee result = payeeSessionBeanRemote.retrievePayeeByNum(payeeAccountNum);
        assertNull(result.getPayeeId());
    }
    
    @Test
    public void testDeletePayee() {
        System.out.println("deletePayee");
        String payeeAccountNum = "2345264";
        String expResult = "Successfully deleted!";
        String result = payeeSessionBeanRemote.deletePayee(payeeAccountNum);
        assertEquals(expResult, result);
    }

    private PayeeSessionBeanRemote lookupPayeeSessionBeanRemote() {
        try {
            Context c = new InitialContext();
            return (PayeeSessionBeanRemote) c.lookup("java:global/RetailBankingSystem/RetailBankingSystem-ejb/PayeeSessionBean!ejb.deposit.session.PayeeSessionBeanRemote");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
    
}
