/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session.stateless;

import javax.ejb.Local;
import entity.EnquiryCase;
import entity.FollowUp;
import entity.Issue;
import java.util.List;

/**
 *
 * @author aaa
 */
@Local
public interface EnquirySessionBeanLocal {

    public List<EnquiryCase> getCustomerEnquiry(Long customerId);

    public List<EnquiryCase> getAllPendingCustomerEnquiry();

    public String getCustomerEnquiryDetail(Long caseId);

    public List<FollowUp> getAllPendingCustomerFollowUp();

    public String getCustomerFollowUpDetail(Long followUpId);

    public List<EnquiryCase> getAllEnquiry();

    public EnquiryCase getEnquiryByCaseId(Long caseId);

    public List<FollowUp> getFollowUpByCaseId(Long caseId);
    
    public List<Issue> getCaseIssue (Long caseId);
    
    public List<Issue> getFollowUpIssue(Long followUpId);

    public String addNewCase(Long customerId, String type, String detail);

    public String addNewCaseIssue(Long caseId, String departmentTo, String issueProblem);

    public String addNewFollowUpIssue(Long followUpId, String departmentTo, String issueProblem);

    public String updateStatus(Long caseId, String caseStatus);

    public String addFollowUp(Long caseId, String caseFollowUp);

    public String replyCustomerCase(Long caseId, String caseReply);

    public String replyCustomerFollowUp(Long followUpId, String followUpSolution);

    public String caseIssueIsCreated(Long caseId);

    public String followUpIssueIsCreated(Long followUpId);

    public String caseIssueAllReplied(Long caseId);

    public String followUpIssueAllReplied(Long followUpId);

}
