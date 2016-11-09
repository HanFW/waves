/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.infrastructure.session;

import ejb.infrastructure.entity.MessageBox;
import java.util.Date;
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
public class MessageSessionBeanRemoteTest {

    MessageSessionBeanRemote messageSessionBeanRemote = lookupMessageSessionBeanRemote();

    public MessageSessionBeanRemoteTest() {
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
    public void testRetrieveMessageBoxByCusIC() {
        System.out.println("retrieveMessageBoxByCusIC");
        String customerIdentificationNum = "S1729390K";
        List<MessageBox> result = messageSessionBeanRemote.retrieveMessageBoxByCusIC(customerIdentificationNum);
        assertNotNull(result);
    }

   
    @Test
    public void testAddNewMessage() {
        System.out.println("addNewMessage");
        String fromWhere = "Merlion Bank";
        String messageType = "Service";
        String subject = "Wealth Management";
        String receivedDate = new Date().toString();
        String messageContent = "Dear Customer, we are glad to inform you about our wealth management products.";
        Long customerBasicId = (long) 1;
        Long expResult = (long) 2;
        MessageBox result = messageSessionBeanRemote.addNewMessage(fromWhere, messageType, subject, receivedDate, messageContent, customerBasicId);
        assertEquals(expResult, result.getMessageId());
    }

    
    @Test
    public void testRetrieveMessageBoxById() {
        System.out.println("retrieveMessageBoxById");
        Long messageBoxId = (long) 1;
        Long expResult = (long) 1;
        MessageBox result = messageSessionBeanRemote.retrieveMessageBoxById(messageBoxId);
        assertEquals(expResult, result.getMessageId());
    }

    
    @Test
    public void testDeleteMessage() {
        System.out.println("deleteMessage");
        Long messageId = (long) 2;
        String expResult = "Successfully deleted!";
        String result = messageSessionBeanRemote.deleteMessage(messageId);
        assertEquals(expResult, result);
    }

    private MessageSessionBeanRemote lookupMessageSessionBeanRemote() {
        try {
            Context c = new InitialContext();
            return (MessageSessionBeanRemote) c.lookup("java:global/RetailBankingSystem/RetailBankingSystem-ejb/MessageSessionBean!ejb.infrastructure.session.MessageSessionBeanRemote");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
