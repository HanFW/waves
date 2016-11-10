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
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

/**
 *
 * @author Nicole
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
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

    @Test
    public void testGetCustomerEnquiry() {
        System.out.println("getCustomerEnquiry");
        Long customerId = (long) 1;
        int expResult = 2;
        List<EnquiryCase> result = enquirySessionBeanRemote.getCustomerEnquiry(customerId);
        assertEquals(expResult, result.size());
    }

    @Test
    public void testGetAllPendingCustomerEnquiry() {
        System.out.println("getAllPendingCustomerEnquiry");
        int expResult = 3;
        List<EnquiryCase> result = enquirySessionBeanRemote.getAllPendingCustomerEnquiry();
        assertEquals(expResult, result.size());
    }

    @Test
    public void testGetCustomerEnquiryDetail() {
        System.out.println("getCustomerEnquiryDetail");
        Long caseId = (long) 4;
        String expResult = "Any student loan availible?";
        String result = enquirySessionBeanRemote.getCustomerEnquiryDetail(caseId);
        assertEquals(expResult, result);
    }

    @Test
    public void testGetCustomerEnquiryReply() {
        System.out.println("getCustomerEnquiryReply");
        Long caseId = (long) 4;
        String expResult = "Dear Customer, thank you for your enquiry. We will get back to you soon!";
        String result = enquirySessionBeanRemote.getCustomerEnquiryReply(caseId);
        assertEquals(expResult, result);
    }

    @Test
    public void testGetAllPendingLoanIssue() {
        System.out.println("getAllPendingLoanIssue");
        int expResult = 1;
        List<Issue> result = enquirySessionBeanRemote.getAllPendingLoanIssue();
        assertEquals(expResult, result.size());
    }

    @Test
    public void testGetAllPendingCardIssue() {
        System.out.println("getAllPendingCardIssue");
        int expResult = 1;
        List<Issue> result = enquirySessionBeanRemote.getAllPendingCardIssue();
        assertEquals(expResult, result.size());
    }

    @Test
    public void testGetAllPendingDepositIssue() {
        System.out.println("getAllPendingDepositIssue");
        int expResult = 0;
        List<Issue> result = enquirySessionBeanRemote.getAllPendingDepositIssue();
        assertEquals(expResult, result.size());
    }

    @Test
    public void testGetAllPendingOperationIssue() {
        System.out.println("getAllPendingOperationIssue");
        int expResult = 0;
        List<Issue> result = enquirySessionBeanRemote.getAllPendingOperationIssue();
        assertEquals(expResult, result.size());

    }

    @Test
    public void testGetAllPendingRMIssue() {
        System.out.println("getAllPendingRMIssue");
        int expResult = 0;
        List<Issue> result = enquirySessionBeanRemote.getAllPendingRMIssue();
        assertEquals(expResult, result.size());

    }

    @Test
    public void testGetIssueDetail() {
        System.out.println("getIssueDetail");
        Long issueId = (long) 1;
        String expResult = "Please help to handle this enquiry.";
        String result = enquirySessionBeanRemote.getIssueDetail(issueId);
        assertEquals(expResult, result);
    }

    @Test
    public void testGetAllEnquiry() {
        System.out.println("getAllEnquiry");
        int expResult = 3;
        List<EnquiryCase> result = enquirySessionBeanRemote.getAllEnquiry();
        assertEquals(expResult, result.size());
    }

    @Test
    public void testGetEnquiryByCaseId() {
        System.out.println("getEnquiryByCaseId");
        Long caseId = (long) 2;
        String expResult = "Wealth Management";
        List<EnquiryCase> result = enquirySessionBeanRemote.getEnquiryByCaseId(caseId);
        assertEquals(expResult, result.get(0).getCaseType());

    }

    @Test
    public void testGetFollowUpByCaseId() {
        System.out.println("getFollowUpByCaseId");
        Long caseId = (long) 1;
        int expResult = 1;
        List<FollowUp> result = enquirySessionBeanRemote.getFollowUpByCaseId(caseId);
        assertEquals(expResult, result.size());
    }

    @Test
    public void testGetCaseIssue() {
        System.out.println("getCaseIssue");
        Long caseId = (long) 1;
        int expResult = 2;
        List<Issue> result = enquirySessionBeanRemote.getCaseIssue(caseId);
        assertEquals(expResult, result.size());
    }

    @Test
    public void testGetCaseFollowUp() {
        System.out.println("getCaseFollowUp");
        Long caseId = (long) 1;
        int expResult = 1;
        List<FollowUp> result = enquirySessionBeanRemote.getCaseFollowUp(caseId);
        assertEquals(expResult, result.size());

    }

    @Test
    public void testAddNewCase() {
        System.out.println("addNewCase");
        Long customerId = (long) 3;
        String type = "Loan";
        String detail = "Any student loan availible?";
        String expResult = "Enquiry sent successfully";
        String result = enquirySessionBeanRemote.addNewCase(customerId, type, detail);
        assertEquals(expResult, result);
    }

    @Test
    public void testAddNewCaseIssue() {
        System.out.println("addNewCaseIssue");
        Long caseId = (long) 4;
        String departmentTo = "Loan";
        String issueProblem = "Please help me to handle this.";
        String expResult = "Successful";
        String result = enquirySessionBeanRemote.addNewCaseIssue(caseId, departmentTo, issueProblem);
        assertEquals(expResult, result);

    }

    @Test
    public void testUpdateStatus() {
        System.out.println("updateStatus");
        Long caseId = (long) 4;
        String caseStatus = "solved";
        String expResult = "Update Successful";
        String result = enquirySessionBeanRemote.updateStatus(caseId, caseStatus);
        assertEquals(expResult, result);

    }

    @Test
    public void testAddFollowUp() {
        System.out.println("addFollowUp");
        Long caseId = (long) 2;
        String caseFollowUp = "Is guarantor required for this loan?";
        String expResult = "Sent Successful";
        String result = enquirySessionBeanRemote.addFollowUp(caseId, caseFollowUp);
        assertEquals(expResult, result);

    }

    @Test
    public void testCaseIssueIsCreated() {
        System.out.println("caseIssueIsCreated");
        Long caseId = (long) 1;
        String expResult = "Yes";
        String result = enquirySessionBeanRemote.caseIssueIsCreated(caseId);
        assertEquals(expResult, result);
    }

    @Test
    public void testReplyCustomerFollowUp() {
        System.out.println("replyCustomerFollowUp");
        Long followUpId = (long) 2;
        String followUpSolution = "To slove this...";
        String expResult = "Reply Sent Successful";
        String result = enquirySessionBeanRemote.replyCustomerFollowUp(followUpId, followUpSolution);
        assertEquals(expResult, result);
    }

    @Test
    public void testReplyIssue() {
        System.out.println("replyIssue");
        Long issueId = (long) 1;
        String issueSolution = "To solve this...s";
        String expResult = "Reply Sent Successful";
        String result = enquirySessionBeanRemote.replyIssue(issueId, issueSolution);
        assertEquals(expResult, result);
    }

    @Test
    public void testCaseIssueAllReplied() {
        System.out.println("caseIssueAllReplied");
        Long caseId = (long) 1;
        String expResult = "No";
        String result = enquirySessionBeanRemote.caseIssueAllReplied(caseId);
        assertEquals(expResult, result);

    }

    @Test
    public void testDeleteCase() {
        System.out.println("deleteCase");
        Long caseId = (long) 3;
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
