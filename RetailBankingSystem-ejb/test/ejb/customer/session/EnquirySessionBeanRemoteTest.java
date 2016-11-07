/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.customer.session;

import ejb.customer.entity.CustomerBasic;
import ejb.customer.entity.EnquiryCase;
import ejb.customer.entity.FollowUp;
import ejb.customer.entity.Issue;
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
public class EnquirySessionBeanRemoteTest {
    
    EnquirySessionBeanRemote enquirySessionBeanRemote = lookupEnquirySessionBeanRemote();
    
    public EnquirySessionBeanRemoteTest() {
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
     * Test of getCustomerEnquiry method, of class EnquirySessionBeanRemote.
     */
    @Test
    public void testGetCustomerEnquiry() {
        System.out.println("getCustomerEnquiry");
        Long customerId = (long)1;
        List<EnquiryCase> result = enquirySessionBeanRemote.getCustomerEnquiry(customerId);
        assertNotNull(result);
       
    }

    /**
     * Test of getAllPendingCustomerEnquiry method, of class EnquirySessionBeanRemote.
     */
    @Test
    public void testGetAllPendingCustomerEnquiry() {
        System.out.println("getAllPendingCustomerEnquiry");
        List<EnquiryCase> result = enquirySessionBeanRemote.getAllPendingCustomerEnquiry();
        assertNotNull(result);
    }

    /**
     * Test of getCustomerEnquiryDetail method, of class EnquirySessionBeanRemote.
     */
    @Test
    public void testGetCustomerEnquiryDetail() {
        System.out.println("getCustomerEnquiryDetail");
        Long caseId = (long)1;
        String expResult = "Am I eligible for the loan?";
        String result = enquirySessionBeanRemote.getCustomerEnquiryDetail(caseId);
        assertEquals(expResult, result);
    }

    /**
     * Test of getCustomerEnquiryReply method, of class EnquirySessionBeanRemote.
     */
    @Test
    public void testGetCustomerEnquiryReply() {
        System.out.println("getCustomerEnquiryReply");
        Long caseId = (long)1;
        String expResult = "Dear John, We are happy to inform you that you are aligible for the loan.";
        String result = enquirySessionBeanRemote.getCustomerEnquiryReply(caseId);
        assertEquals(expResult, result);
    }

    /**
     * Test of getAllPendingLoanIssue method, of class EnquirySessionBeanRemote.
     */
    @Test
    public void testGetAllPendingLoanIssue() {
        System.out.println("getAllPendingLoanIssue");
        int expResult = 1;
        List<Issue> result = enquirySessionBeanRemote.getAllPendingLoanIssue();
        assertEquals(expResult, result.size());
    }

    /**
     * Test of getAllPendingCardIssue method, of class EnquirySessionBeanRemote.
     */
    @Test
    public void testGetAllPendingCardIssue() {
        System.out.println("getAllPendingCardIssue");
        int expResult = 2;
        List<Issue> result = enquirySessionBeanRemote.getAllPendingCardIssue();
        assertEquals(expResult, result.size()); 
    }

    /**
     * Test of getAllPendingDepositIssue method, of class EnquirySessionBeanRemote.
     */
    @Test
    public void testGetAllPendingDepositIssue() {
        System.out.println("getAllPendingDepositIssue");
        int expResult = 1;
        List<Issue> result = enquirySessionBeanRemote.getAllPendingDepositIssue();
        assertEquals(expResult, result.size());
    }

    /**
     * Test of getAllPendingOperationIssue method, of class EnquirySessionBeanRemote.
     */
    @Test
    public void testGetAllPendingOperationIssue() {
        System.out.println("getAllPendingOperationIssue");
         int expResult = 0;
        List<Issue> result = enquirySessionBeanRemote.getAllPendingOperationIssue();
        assertEquals(expResult, result.size());
      
    }

    /**
     * Test of getAllPendingRMIssue method, of class EnquirySessionBeanRemote.
     */
    @Test
    public void testGetAllPendingRMIssue() {
        System.out.println("getAllPendingRMIssue");
         int expResult = 0;
        List<Issue> result = enquirySessionBeanRemote.getAllPendingRMIssue();
        assertEquals(expResult, result.size());

    }

    /**
     * Test of getIssueDetail method, of class EnquirySessionBeanRemote.
     */
    @Test
    public void testGetIssueDetail() {
        System.out.println("getIssueDetail");
        Long issueId = (long)1;
        String expResult = "Please help to handle this enquiry.";
        String result = enquirySessionBeanRemote.getIssueDetail(issueId);
        assertEquals(expResult, result);
       
    }

    /**
     * Test of getAllEnquiry method, of class EnquirySessionBeanRemote.
     */
    @Test
    public void testGetAllEnquiry() {
        System.out.println("getAllEnquiry");
        int expResult = 5;
        List<EnquiryCase> result = enquirySessionBeanRemote.getAllEnquiry();
        assertEquals(expResult, result.size());
    }

    /**
     * Test of getEnquiryByCaseId method, of class EnquirySessionBeanRemote.
     */
    @Test
    public void testGetEnquiryByCaseId() {
        System.out.println("getEnquiryByCaseId");
        Long caseId = (long)2;
        String expResult = "How should I apply for a credit card?";
        List<EnquiryCase> result = enquirySessionBeanRemote.getEnquiryByCaseId(caseId);
        assertEquals(expResult, result.get(0).getCaseDetail());
      
    }

    /**
     * Test of getCustomerByCaseId method, of class EnquirySessionBeanRemote.
     */
    @Test
    public void testGetCustomerByCaseId() {
        System.out.println("getCustomerByCaseId");
        Long caseId = (long)1;
        String expResult = "John Lee";
        List<CustomerBasic> result = enquirySessionBeanRemote.getCustomerByCaseId(caseId);
        assertEquals(expResult, result.get(0).getCustomerName());
    }

    /**
     * Test of getFollowUpByCaseId method, of class EnquirySessionBeanRemote.
     */
    @Test
    public void testGetFollowUpByCaseId() {
        System.out.println("getFollowUpByCaseId");
        Long caseId = (long)1;
        String expResult = "Please suggest some loan that are suitble for me";
        List<FollowUp> result = enquirySessionBeanRemote.getFollowUpByCaseId(caseId);
        assertEquals(expResult, result.get(0).getFollowUpDetail());
    }

    /**
     * Test of getCaseIssue method, of class EnquirySessionBeanRemote.
     */
    @Test
    public void testGetCaseIssue() {
        System.out.println("getCaseIssue");
        Long caseId = (long)1;
        int expResult = 1;
        List<Issue> result = enquirySessionBeanRemote.getCaseIssue(caseId);
        assertEquals(expResult, result.size());
    }

    /**
     * Test of getCaseFollowUp method, of class EnquirySessionBeanRemote.
     */
    @Test
    public void testGetCaseFollowUp() {
        System.out.println("getCaseFollowUp");
        Long caseId = (long)2;
        int expResult = 0;
        List<FollowUp> result = enquirySessionBeanRemote.getCaseFollowUp(caseId);
        assertEquals(expResult, result.size());

    }

    /**
     * Test of addNewCase method, of class EnquirySessionBeanRemote.
     */
    @Test
    public void testAddNewCase() {
        System.out.println("addNewCase");
        Long customerId = (long)2;
        String type = "Loan";
        String detail = "Any student loan avalible?";
        String expResult = "Enquiry sent successfully";
        String result = enquirySessionBeanRemote.addNewCase(customerId, type, detail);
        assertEquals(expResult, result);
    }

    /**
     * Test of addNewCaseIssue method, of class EnquirySessionBeanRemote.
     */
    @Test
    public void testAddNewCaseIssue() {
        System.out.println("addNewCaseIssue");
        Long caseId = (long)6;
        String departmentTo = "Loan";
        String issueProblem = "Please help me to handle this.";
        String expResult = "Successful";
        String result = enquirySessionBeanRemote.addNewCaseIssue(caseId, departmentTo, issueProblem);
        assertEquals(expResult, result);
      
    }

    /**
     * Test of updateStatus method, of class EnquirySessionBeanRemote.
     */
    @Test
    public void testUpdateStatus() {
        System.out.println("updateStatus");
        Long caseId = (long)6;
        String caseStatus = "solved";
        String expResult = "Update Successful";
        String result = enquirySessionBeanRemote.updateStatus(caseId, caseStatus);
        assertEquals(expResult, result);
       
    }

    /**
     * Test of addFollowUp method, of class EnquirySessionBeanRemote.
     */
    @Test
    public void testAddFollowUp() {
        System.out.println("addFollowUp");
        Long caseId = (long)3;
        String caseFollowUp = "Is guarantor required for this loan?";
        String expResult = "Sent Successful";
        String result = enquirySessionBeanRemote.addFollowUp(caseId, caseFollowUp);
        assertEquals(expResult, result);
        
    }

    /**
     * Test of replyCustomerCase method, of class EnquirySessionBeanRemote.
     */
//    @Test
//    public void testReplyCustomerCase() {
//        System.out.println("replyCustomerCase");
//        Long caseId = (long)3;
//        String caseReply = "Dear customer...";
//        List<FollowUp> selectedFollowUp = null;
//        List<FollowUp> followUps = null;
//        String expResult = "";
//        String result = enquirySessionBeanRemote.replyCustomerCase(caseId, caseReply, selectedFollowUp, followUps);
//        assertEquals(expResult, result);
//      
//    }

    /**
     * Test of replyCustomerFollowUp method, of class EnquirySessionBeanRemote.
     */
    @Test
    public void testReplyCustomerFollowUp() {
        System.out.println("replyCustomerFollowUp");
        Long followUpId = (long)4;
        String followUpSolution = "To slove this...";
        String expResult = "Reply Sent Successful";
        String result = enquirySessionBeanRemote.replyCustomerFollowUp(followUpId, followUpSolution);
        assertEquals(expResult, result);      
    }

    /**
     * Test of replyIssue method, of class EnquirySessionBeanRemote.
     */
    @Test
    public void testReplyIssue() {
        System.out.println("replyIssue");
        Long issueId = (long)1;
        String issueSolution = "To solve this...s";
        String expResult = "Reply Sent Successful";
        String result = enquirySessionBeanRemote.replyIssue(issueId, issueSolution);
        assertEquals(expResult, result);
    }

    /**
     * Test of caseIssueIsCreated method, of class EnquirySessionBeanRemote.
     */
//    @Test
//    public void testCaseIssueIsCreated() {
//        System.out.println("caseIssueIsCreated");
//        Long caseId = null;
//        String expResult = "";
//        String result = enquirySessionBeanRemote.caseIssueIsCreated(caseId);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of caseIssueAllReplied method, of class EnquirySessionBeanRemote.
     */
    @Test
    public void testCaseIssueAllReplied() {
        System.out.println("caseIssueAllReplied");
        Long caseId = (long)1;
        String expResult = "No";
        String result = enquirySessionBeanRemote.caseIssueAllReplied(caseId);
        assertEquals(expResult, result);
       
    }

    /**
     * Test of deleteCase method, of class EnquirySessionBeanRemote.
     */
    @Test
    public void testDeleteCase() {
        System.out.println("deleteCase");
        Long caseId = (long)1;
        String expResult = "Successfully deleted!";
        String result = enquirySessionBeanRemote.deleteCase(caseId);
        assertEquals(expResult, result);
    }

    private EnquirySessionBeanRemote lookupEnquirySessionBeanRemote() {
        try {
            Context c = new InitialContext();
            return (EnquirySessionBeanRemote) c.lookup("java:global/RetailBankingSystem/RetailBankingSystem-ejb/EnquirySessionBean!ejb.customer.session.EnquirySessionBeanRemote");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
    
}
