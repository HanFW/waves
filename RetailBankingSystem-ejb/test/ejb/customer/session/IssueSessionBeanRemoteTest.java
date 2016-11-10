/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.customer.session;

import ejb.customer.entity.Issue;
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
public class IssueSessionBeanRemoteTest {
    
    IssueSessionBeanRemote issueSessionBeanRemote = lookupIssueSessionBeanRemote();
    
    public IssueSessionBeanRemoteTest() {
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
    public void testAddNewIssue() {
        System.out.println("addNewIssue");
        String departmentTo = "Loan";
        String issueProblem = "Please help to handle this enquiry.";
        String createdTime = "2016-Nov-6 15:26:42";
        String issueStatus = "Pending";
        Long caseId = (long)2;
        Long expResult = (long)4;
        Issue result = issueSessionBeanRemote.addNewIssue(departmentTo, issueProblem, createdTime, issueStatus, caseId);
        assertEquals(expResult, result.getIssueId());
    }

    
    @Test
    public void testDeleteIssue() {
        System.out.println("deleteIssue");
        Long issueId = (long)3;
        String expResult = "Successfully deleted!";
        String result = issueSessionBeanRemote.deleteIssue(issueId);
        assertEquals(expResult, result);
    }

    
    @Test
    public void testRetrieveIssueByID() {
        System.out.println("retrieveIssueByID");
        Long issueId = (long)4;
        String expResult = "Please help to handle this enquiry.";
        Issue result = issueSessionBeanRemote.retrieveIssueByID(issueId);
        assertEquals(expResult, result.getIssueProblem());
    }

    private IssueSessionBeanRemote lookupIssueSessionBeanRemote() {
        try {
            Context c = new InitialContext();
            return (IssueSessionBeanRemote) c.lookup("java:global/RetailBankingSystem/RetailBankingSystem-ejb/IssueSessionBean!ejb.customer.session.IssueSessionBeanRemote");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
    
}
