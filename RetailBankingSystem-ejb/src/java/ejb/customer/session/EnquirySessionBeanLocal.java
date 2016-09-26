/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.customer.session;

import javax.ejb.Local;
import ejb.customer.entity.EnquiryCase;
import ejb.customer.entity.FollowUp;
import ejb.customer.entity.Issue;
import java.util.List;

/**
 *
 * @author aaa
 */
public interface EnquirySessionBeanLocal {

    public List<EnquiryCase> getCustomerEnquiry(Long customerId);

    public List<EnquiryCase> getAllPendingCustomerEnquiry();

    public String getCustomerEnquiryDetail(Long caseId);

    public List<FollowUp> getAllPendingCustomerFollowUp();

    public List<Issue> getAllPendingLoanIssue();

    public List<Issue> getAllPendingCardIssue();

    public List<Issue> getAllPendingDepositIssue();

    public List<Issue> getAllPendingOperationIssue();

    public List<Issue> getAllPendingRMIssue();

    public String getCustomerFollowUpDetail(Long followUpId);

    public String getIssueDetail(Long issueId);

    public List<EnquiryCase> getAllEnquiry();

    public EnquiryCase getEnquiryByCaseId(Long caseId);

    public List<FollowUp> getFollowUpByCaseId(Long caseId);

    public List<Issue> getCaseIssue(Long caseId);

    public List<FollowUp> getCaseFollowUp(Long caseId);
//
//    public List<Issue> getFollowUpIssue(Long followUpId);

    public String addNewCase(Long customerId, String type, String detail);

    public String addNewCaseIssue(Long caseId, String departmentTo, String issueProblem, List<FollowUp> followUps);

//    public String addNewFollowUpIssue(Long followUpId, String departmentTo, String issueProblem);

    public String updateStatus(Long caseId, String caseStatus);

    public String addFollowUp(Long caseId, String caseFollowUp);

    public String replyCustomerCase(Long caseId, String caseReply, List<FollowUp> selectedFollowUp, List<FollowUp> followUps);

    public String replyCustomerFollowUp(Long followUpId, String followUpSolution);

    public String replyIssue(Long issueId, String issueSolution);

    public String caseIssueIsCreated(Long caseId);

//    public String followUpIssueIsCreated(Long followUpId);

    public String caseIssueAllReplied(Long caseId);

//    public String followUpIssueAllReplied(Long followUpId);
    
    public EnquiryCase retrieveEnquiryById(Long caseId);
    
    public String deleteCase(Long caseId);

}
