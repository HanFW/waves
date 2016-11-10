/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.deposit.session;

import ejb.deposit.entity.Statement;
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
public class StatementSessionBeanRemoteTest {

    StatementSessionBeanRemote statementSessionBeanRemote = lookupStatementSessionBeanRemote();

    public StatementSessionBeanRemoteTest() {
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
    public void testAddNewStatement() {
        System.out.println("addNewStatement");
        String statementDate = "06/Nov/2016";
        String statementType = "Bank Statement";
        String accountDetails = "Bonus Savings Acccount 6784485";
        Long startTime = null;
        Long endTime = null;
        Long customerBasicId = (long) 2;
        Long expResult = (long) 3;
        Long result = statementSessionBeanRemote.addNewStatement(statementDate, statementType, accountDetails, startTime, endTime, customerBasicId);
        assertEquals(expResult, result);
    }

    @Test
    public void testRetrieveStatementById() {
        System.out.println("retrieveStatementById");
        Long statementId = (long) 1;
        String expResult = "Bank Statement";
        Statement result = statementSessionBeanRemote.retrieveStatementById(statementId);
        assertEquals(expResult, result.getStatementType());

    }

    @Test
    public void testRetrieveStatementByAccNum() {
        System.out.println("retrieveStatementByAccNum");
        String bankAccountNum = "5061102";
        int expResult = 2;
        List<Statement> result = statementSessionBeanRemote.retrieveStatementByAccNum(bankAccountNum);
        assertEquals(expResult, result.size());
    }

    @Test
    public void testDeleteStatement() {
        System.out.println("deleteStatement");
        Long statementId = (long) 3;
        String expResult = "Successfully Deleted!";
        String result = statementSessionBeanRemote.deleteStatement(statementId);
        assertEquals(expResult, result);
    }

    private StatementSessionBeanRemote lookupStatementSessionBeanRemote() {
        try {
            Context c = new InitialContext();
            return (StatementSessionBeanRemote) c.lookup("java:global/RetailBankingSystem/RetailBankingSystem-ejb/StatementSessionBean!ejb.deposit.session.StatementSessionBeanRemote");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

}
