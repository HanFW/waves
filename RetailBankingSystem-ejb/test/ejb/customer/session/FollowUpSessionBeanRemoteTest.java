/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.customer.session;

import ejb.customer.entity.FollowUp;
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
public class FollowUpSessionBeanRemoteTest {

    FollowUpSessionBeanRemote followUpSessionBeanRemote = lookupFollowUpSessionBeanRemote();

    public FollowUpSessionBeanRemoteTest() {
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
    public void testRetrieveFollowUpById() {
        System.out.println("retrieveFollowUpById");
        Long followUpId = (long) 1;
        String expResult = "In addition, I would like to..";
        FollowUp result = followUpSessionBeanRemote.retrieveFollowUpById(followUpId);
        assertEquals(expResult, result.getFollowUpDetail());
    }

   
    @Test
    public void testDeleteFollowUp() {
        System.out.println("deleteFollowUp");
        Long followUpId = (long) 2;
        String expResult = "Successfully deleted!";
        String result = followUpSessionBeanRemote.deleteFollowUp(followUpId);
        assertEquals(expResult, result);
    }

    private FollowUpSessionBeanRemote lookupFollowUpSessionBeanRemote() {
        try {
            Context c = new InitialContext();
            return (FollowUpSessionBeanRemote) c.lookup("java:global/RetailBankingSystem/RetailBankingSystem-ejb/FollowUpSessionBean!ejb.customer.session.FollowUpSessionBeanRemote");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

}
