package ejb.customer.session;

import ejb.customer.entity.CustomerBasic;
import ejb.customer.entity.EnquiryCase;
import ejb.customer.entity.FollowUp;
import ejb.customer.entity.Issue;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Nicole
 */
@Remote
public interface EnquirySessionBeanRemote {
     public List<EnquiryCase> getCustomerEnquiry(Long customerId);

    public List<EnquiryCase> getAllPendingCustomerEnquiry();

    public String getCustomerEnquiryDetail(Long caseId);

    public String getCustomerEnquiryReply(Long caseId);

    public List<Issue> getAllPendingLoanIssue();

    public List<Issue> getAllPendingCardIssue();

    public List<Issue> getAllPendingDepositIssue();

    public List<Issue> getAllPendingOperationIssue();

    public List<Issue> getAllPendingRMIssue();

    public String getIssueDetail(Long issueId);

    public List<EnquiryCase> getAllEnquiry();

    public List<EnquiryCase> getEnquiryByCaseId(Long caseId);

    public List<CustomerBasic> getCustomerByCaseId(Long caseId);

    public List<FollowUp> getFollowUpByCaseId(Long caseId);

    public List<Issue> getCaseIssue(Long caseId);

    public List<FollowUp> getCaseFollowUp(Long caseId);

    public String addNewCase(Long customerId, String type, String detail);

    public String addNewCaseIssue(Long caseId, String departmentTo, String issueProblem);

    public String updateStatus(Long caseId, String caseStatus);

    public String addFollowUp(Long caseId, String caseFollowUp);

    public String replyCustomerCase(Long caseId, String caseReply, List<FollowUp> selectedFollowUp, List<FollowUp> followUps);

    public String replyCustomerFollowUp(Long followUpId, String followUpSolution);

    public String replyIssue(Long issueId, String issueSolution);

    public String caseIssueIsCreated(Long caseId);

    public String caseIssueAllReplied(Long caseId);

    public String deleteCase(Long caseId);
}
