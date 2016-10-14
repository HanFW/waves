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
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import javax.ejb.EJB;

import javax.persistence.Query;

@Stateless
public class EnquirySessionBean implements EnquirySessionBeanLocal {

    @EJB
    private FollowUpSessionBeanLocal followUpSessionBeanLocal;

    @EJB
    private IssueSessionBeanLocal issueSessionBeanLocal;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<EnquiryCase> getAllEnquiry() {
        Query query = entityManager.createQuery("SELECT ec FROM EnquiryCase ec");
        return query.getResultList();
    }

    @Override
    public List<EnquiryCase> getEnquiryByCaseId(Long caseId) {
        Query query = entityManager.createQuery("SELECT ec FROM EnquiryCase ec WHERE ec.caseId = :caseId");
        query.setParameter("caseId", caseId);
        System.out.println("+++++++++++" + query.getResultList().isEmpty() + "+++++++");
        return query.getResultList();
    }
    
    @Override
    public List<CustomerBasic> getCustomerByCaseId(Long caseId) {
        Query query = entityManager.createQuery("SELECT cb FROM CustomerBasic cb WHERE cb.enquiryCase.caseId = :caseId");
        query.setParameter("caseId", caseId);

        return query.getResultList();
    }

    @Override
    public List<EnquiryCase> getCustomerEnquiry(Long customerId) {
        Query query = entityManager.createQuery("SELECT ec FROM EnquiryCase ec WHERE ec.customerBasic.customerBasicId = :customerId");
        query.setParameter("customerId", customerId);
        return query.getResultList();
    }

    @Override
    public List<EnquiryCase> getAllPendingCustomerEnquiry() {
        Query query = entityManager.createQuery("SELECT ec FROM EnquiryCase ec WHERE ec.caseStatus != :caseStatus");
        query.setParameter("caseStatus", "Solved");
        return query.getResultList();
    }

    @Override
    public List<Issue> getAllPendingLoanIssue() {
        Query query = entityManager.createQuery("SELECT i FROM Issue i WHERE i.departmentTo = :departmentTo AND i.issueStatus = :issueStatus");
        query.setParameter("departmentTo", "Loan");
        query.setParameter("issueStatus", "Pending");
        return query.getResultList();
    }

    @Override
    public List<Issue> getAllPendingCardIssue() {
        Query query = entityManager.createQuery("SELECT i FROM Issue i WHERE i.departmentTo = :departmentTo AND i.issueStatus = :issueStatus");
        query.setParameter("departmentTo", "Card");
        query.setParameter("issueStatus", "Pending");
        return query.getResultList();
    }

    @Override
    public List<Issue> getAllPendingDepositIssue() {
        Query query = entityManager.createQuery("SELECT i FROM Issue i WHERE i.departmentTo = :departmentTo AND i.issueStatus = :issueStatus");
        query.setParameter("departmentTo", "Deposit");
        query.setParameter("issueStatus", "Pending");
        return query.getResultList();
    }

    @Override
    public List<Issue> getAllPendingOperationIssue() {
        Query query = entityManager.createQuery("SELECT i FROM Issue i WHERE i.departmentTo = :departmentTo AND i.issueStatus = :issueStatus");
        query.setParameter("departmentTo", "Operation");
        query.setParameter("issueStatus", "Pending");
        return query.getResultList();
    }

    @Override
    public List<Issue> getAllPendingRMIssue() {
        Query query = entityManager.createQuery("SELECT i FROM Issue i WHERE i.departmentTo = :departmentTo AND i.issueStatus = :issueStatus");
        query.setParameter("departmentTo", "RM");
        query.setParameter("issueStatus", "Pending");
        return query.getResultList();
    }

    @Override
    public String getCustomerEnquiryDetail(Long caseId) {
        List resultList = getEnquiryByCaseId(caseId);
        EnquiryCase ec = (EnquiryCase) resultList.get(0);
        return ec.getCaseDetail();
    }

    @Override
    public String getCustomerEnquiryReply(Long caseId) {
        List resultList = getEnquiryByCaseId(caseId);
        EnquiryCase ec = (EnquiryCase) resultList.get(0);
        return ec.getCaseReply();
    }

    @Override
    public String getIssueDetail(Long issueId) {
        Query query = entityManager.createQuery("SELECT i FROM Issue i WHERE i.issueId = :issueId");
        query.setParameter("issueId", issueId);
        List resultList = query.getResultList();
        Issue i = (Issue) resultList.get(0);
        return i.getIssueProblem();
    }

    @Override
    public List<Issue> getCaseIssue(Long caseId) {
        List resultList = getEnquiryByCaseId(caseId);
        EnquiryCase ec = (EnquiryCase) resultList.get(0);
        return ec.getIssue();
    }

    @Override
    public List<FollowUp> getCaseFollowUp(Long caseId) {
        List resultList = getEnquiryByCaseId(caseId);
        EnquiryCase ec = (EnquiryCase) resultList.get(0);
        return ec.getFollowUp();
    }

    @Override
    public String deleteCase(Long caseId) {
        EnquiryCase ec = getEnquiryByCaseId(caseId).get(0);

        if (!ec.getIssue().isEmpty()) {
            for (int i = ec.getIssue().size() - 1; i > 0; i--) {
                issueSessionBeanLocal.deleteIssue(ec.getIssue().get(i).getIssueId());
            }
        }
        if (!ec.getFollowUp().isEmpty()) {
            for (int j = ec.getFollowUp().size() - 1; j > 0; j--) {
                followUpSessionBeanLocal.deleteFollowUp(ec.getFollowUp().get(j).getFollowUpId());
            }
        }

        entityManager.remove(ec);
        entityManager.flush();

        return "Successfully deleted!";
    }

    @Override
    public List<FollowUp> getFollowUpByCaseId(Long caseId) {
        Query query = entityManager.createQuery("SELECT fu FROM FollowUp fu WHERE fu.enquiryCase.caseId = :caseId");
        query.setParameter("caseId", caseId);
        return query.getResultList();
    }

    @Override
    public String addNewCase(Long customerId, String type, String detail) {

        EnquiryCase enquiryCase = new EnquiryCase();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");

        Calendar cal = Calendar.getInstance();
        String createdTime = sdf.format(cal.getTime());

        enquiryCase.setCaseDetail(detail);
        enquiryCase.setCaseType(type);
        enquiryCase.setCreatedTime(createdTime);
        enquiryCase.setCaseStatus("Pending");
        enquiryCase.setCaseReply("Dear Customer, thank you for your enquiry. We will get back to you soon!");

        Query query = entityManager.createQuery("SELECT cb FROM CustomerBasic cb WHERE cb.customerBasicId = :customerId");
        query.setParameter("customerId", customerId);
        List customerBasics = query.getResultList();

        if (customerBasics.isEmpty()) {
            return "No Customer is detected";
        } else if (detail.isEmpty()) {
            return "Please provide enquiry details";
        }else {
            CustomerBasic customerBasic = (CustomerBasic) customerBasics.get(0);
            enquiryCase.setCustomerBasic(customerBasic);
            customerBasic.getEnquiryCase().add(enquiryCase);

            query = entityManager.createQuery("SELECT cb FROM CustomerBasic cb WHERE cb.customerIdentificationNum = :customerIdentificationNum");
            query.setParameter("customerIdentificationNum", customerBasic.getCustomerIdentificationNum());

            return "Enquiry sent successfully";
        }
    }

    @Override
    public String updateStatus(Long caseId, String caseStatus) {
        List resultList = getEnquiryByCaseId(caseId);

        if (resultList.isEmpty()) {
            return "Incorrect Case ID";
        } else {
            EnquiryCase ec = (EnquiryCase) resultList.get(0);
            ec.setCaseStatus(caseStatus);

            entityManager.flush();
            return "Update Successful";
        }
    }

    @Override
    public String addFollowUp(Long caseId, String caseFollowUp) {
        List resultList = getEnquiryByCaseId(caseId);

        if (resultList.isEmpty()) {
            return "Incorrect Case ID. Please go back to View Enquiry Page to add another follow-up question.";
        } else {
            EnquiryCase ec = (EnquiryCase) resultList.get(0);

            FollowUp followUp = new FollowUp();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");

            Calendar cal = Calendar.getInstance();
            String sendTime = sdf.format(cal.getTime());

            followUp.setFollowUpDetail(caseFollowUp);
            followUp.setSendTime(sendTime);
            followUp.setFollowUpStatus("Pending");
            ec.setCaseStatus("Pending");
            followUp.setFollowUpSolution("Dear Customer, thank you for your enquiry. We will get back to you soon!");
            followUp.setEnquiryCase(ec);
            ec.getFollowUp().add(followUp);

            return "Sent Successful";
        }
    }

    @Override
    public String addNewCaseIssue(Long caseId, String departmentTo, String issueProblem) {
        List resultList = getEnquiryByCaseId(caseId);
        EnquiryCase ec = (EnquiryCase) resultList.get(0);

        List<FollowUp> followUps = ec.getFollowUp();
        for (int i = 0; i < followUps.size(); i++) {
            if (followUps.get(i).getFollowUpStatus() == "Pending") {
                followUps.get(i).setFollowUpStatus("Waiting for Specialist's Reply");
            }
        }
        ec.setFollowUp(followUps);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        String createdTime = sdf.format(cal.getTime());

        if (resultList.isEmpty()) {
            return "Invalid Case ID";
        } else {
            if (departmentTo.trim().isEmpty()) {
                return "Empty department";
            } else if (issueProblem.isEmpty()) {
                return "Empty Problem";
            } else {

                Issue issue = issueSessionBeanLocal.addNewIssue(departmentTo, issueProblem, createdTime, "Pending", caseId);
                ec.getIssue().add(issue);

                return "Successful";
            }
        }
    }

    @Override
    public String replyCustomerCase(Long caseId, String caseReply, List<FollowUp> selectedFollowUp, List<FollowUp> followUps) {
        List resultList = getEnquiryByCaseId(caseId);
        if (resultList.isEmpty()) {
            return "Incorrect Case ID";
        } else {
            if (selectedFollowUp.isEmpty() && !followUps.isEmpty()) {
                return "Box unchecked";
            } else {
                for (int i = 0; i < selectedFollowUp.size(); i++) {
                    Query query = entityManager.createQuery("SELECT fu FROM FollowUp fu WHERE fu.followUpId = :followUpId");
                    query.setParameter("followUpId", selectedFollowUp.get(i).getFollowUpId());
                    List resultList2 = query.getResultList();
                    FollowUp fu = (FollowUp) resultList2.get(0);
                    fu.setFollowUpStatus("Solved");
                    fu.setFollowUpSolution(caseReply);

                    entityManager.flush();
                }
                EnquiryCase ec = (EnquiryCase) resultList.get(0);
                ec.setCaseReply(caseReply);
                if (followUps.size() == 0) {
                    ec.setCaseStatus("Solved");
                } else {
                    for (int a = 0; a < followUps.size(); a++) {
                        Query query = entityManager.createQuery("SELECT fu FROM FollowUp fu WHERE fu.followUpId = :followUpId");
                        query.setParameter("followUpId", followUps.get(a).getFollowUpId());
                        List resultList3 = query.getResultList();
                        FollowUp f = (FollowUp) resultList3.get(0);
                        if (!f.getFollowUpStatus().equalsIgnoreCase("Solved")) {
                            ec.setCaseStatus("In Progress");
                            break;
                        } else {
                            ec.setCaseStatus("Solved");
                        }
                    }
                }
                return "Successful";
            }
        }
    }

    @Override
    public String replyCustomerFollowUp(Long followUpId, String followUpSolution) {
        Query query = entityManager.createQuery("SELECT fu FROM FollowUp fu WHERE fu.followUpId = :followUpId");
        query.setParameter("followUpId", followUpId);
        List resultList = query.getResultList();

        if (resultList.isEmpty()) {
            return "Incorrect Case ID.";
        } else {
            FollowUp fu = (FollowUp) resultList.get(0);
            fu.setFollowUpSolution(followUpSolution);
            fu.setFollowUpStatus("Solved");

            entityManager.flush();
            return "Reply Sent Successful";
        }
    }

    @Override
    public String replyIssue(Long issueId, String issueSolution) {
        Query query = entityManager.createQuery("SELECT i FROM Issue i WHERE i.issueId = :issueId");
        query.setParameter("issueId", issueId);
        List resultList = query.getResultList();

        if (resultList.isEmpty()) {
            return "Incorrect Issue ID.";
        } else {
            Issue is = (Issue) resultList.get(0);
            is.setIssueSolution(issueSolution);
            is.setIssueStatus("Solved");

            entityManager.flush();
            return "Reply Sent Successful";
        }
    }

    @Override
    public String caseIssueIsCreated(Long caseId) {
        List resultList = getEnquiryByCaseId(caseId);
        EnquiryCase ec = (EnquiryCase) resultList.get(0);
        List issueList = ec.getIssue();
        if (issueList.isEmpty()) {
            return "No";
        } else {
            return "Yes";
        }
    }

    @Override
    public String caseIssueAllReplied(Long caseId) {
        List resultList = getEnquiryByCaseId(caseId);
        EnquiryCase ec = (EnquiryCase) resultList.get(0);
        List<Issue> issueList = ec.getIssue();
        String result = "N/A";
        for (int i = 0; i < issueList.size(); i++) {
            if (issueList.get(i).getIssueStatus().equals("Pending")) {
                result = "No";
                break;
            } else {
                result = "Yes";
            }
        }
        return result;
    }
}
