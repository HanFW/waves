/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session.stateless;

import entity.CustomerBasic;
import entity.Issue;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import entity.EnquiryCase;
import entity.FollowUp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.persistence.Query;

/**
 *
 * @author aaa
 */
@Stateless
@LocalBean
public class EnquirySessionBean implements EnquirySessionBeanLocal {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<EnquiryCase> getCustomerEnquiry(Long customerId) {
        Query query = entityManager.createQuery("SELECT ec FROM EnquiryCase ec WHERE ec.customerBasic.customerBasicId = :customerId");
        query.setParameter("customerId", customerId);
        return query.getResultList();
    }

    @Override
    public List<EnquiryCase> getAllPendingCustomerEnquiry() {
        Query query = entityManager.createQuery("SELECT ec FROM EnquiryCase ec WHERE ec.caseStatus = :caseStatus");
        query.setParameter("caseStatus", "Pending");
        return query.getResultList();
    }

    @Override
    public String getCustomerEnquiryDetail(Long caseId) {
        Query query = entityManager.createQuery("SELECT ec FROM EnquiryCase ec WHERE ec.caseId = :caseId");
        query.setParameter("caseId", caseId);
        List resultList = query.getResultList();
        EnquiryCase ec = (EnquiryCase) resultList.get(0);
        return ec.getCaseDetail();
    }

    @Override
    public String getCustomerFollowUpDetail(Long followUpId) {
        Query query = entityManager.createQuery("SELECT fu FROM FollowUp fu WHERE fu.followUpId = :followUpId");
        query.setParameter("followUpId", followUpId);
        List resultList = query.getResultList();
        FollowUp fu = (FollowUp) resultList.get(0);
        return fu.getFollowUpDetail();
    }

    @Override
    public List<FollowUp> getAllPendingCustomerFollowUp() {
        Query query = entityManager.createQuery("SELECT fu FROM FollowUp fu WHERE fu.followUpStatus = :followUpStatus");
        query.setParameter("followUpStatus", "Pending");
        return query.getResultList();
    }

    @Override
    public List<EnquiryCase> getAllEnquiry() {
        Query query = entityManager.createQuery("SELECT ec FROM EnquiryCase ec");
        return query.getResultList();
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
        } else {
            CustomerBasic customerBasic = (CustomerBasic) customerBasics.get(0);
            enquiryCase.setCustomerBasic(customerBasic);
            customerBasic.addNewCase(enquiryCase);

            entityManager.persist(enquiryCase);
            entityManager.flush();

            return "Enquiry sent successfully";
        }
    }

    @Override
    public String updateStatus(Long caseId, String caseStatus) {

        Query query = entityManager.createQuery("SELECT ec FROM EnquiryCase ec WHERE ec.caseId = :caseId");
        query.setParameter("caseId", caseId);

        List resultList = query.getResultList();

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
        Query query = entityManager.createQuery("SELECT ec FROM EnquiryCase ec WHERE ec.caseId = :caseId");
        query.setParameter("caseId", caseId);
        List resultList = query.getResultList();

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
            followUp.setFollowUpSolution("Dear Customer, thank you for your enquiry. We will get back to you soon!");
            followUp.setEnquiryCase(ec);
            ec.addNewFollowUp(followUp);

            entityManager.persist(followUp);
            entityManager.flush();
            return "Sent Successful";
        }
    }

    @Override
    public String addNewCaseIssue(Long caseId, String departmentTo, String issueProblem) {

        Issue issue = new Issue();

        issue.setDepartmentTo(departmentTo);
        issue.setIssueProblem(issueProblem);
        issue.setIssueStatus("Pending");

        Query query = entityManager.createQuery("SELECT ec FROM EnquiryCase ec WHERE ec.caseId = :caseId");
        query.setParameter("caseId", caseId);
        List resultList = query.getResultList();

        if (resultList.isEmpty()) {
            return "Invalid Case ID";
        } else {
            EnquiryCase ec = (EnquiryCase) resultList.get(0);
            issue.setEnquiryCase(ec);
            ec.addNewIssue(issue);

            entityManager.persist(issue);
            entityManager.flush();

            return "Issue sent successfully. You can now edit your next issue.";
        }
    }

    @Override
    public String addNewFollowUpIssue(Long followUpId, String departmentTo, String issueProblem) {

        Issue issue = new Issue();

        issue.setDepartmentTo(departmentTo);
        issue.setIssueProblem(issueProblem);
        issue.setIssueStatus("Pending");

        Query query = entityManager.createQuery("SELECT fu FROM FollowUp fu WHERE fu.followUpId = :followUpId");
        query.setParameter("followUpId", followUpId);
        List resultList = query.getResultList();

        if (resultList.isEmpty()) {
            return "Invalid FollowUp ID";
        } else {
            FollowUp fu = (FollowUp) resultList.get(0);
            issue.setFollowUp(fu);
            fu.addNewIssue(issue);

            entityManager.persist(issue);
            entityManager.flush();

            return "Issue sent successfully. You can now edit your next issue.";
        }
    }

    @Override
    public String replyCustomerCase(Long caseId, String caseReply) {
        Query query = entityManager.createQuery("SELECT ec FROM EnquiryCase ec WHERE ec.caseId = :caseId");
        query.setParameter("caseId", caseId);
        List resultList = query.getResultList();
        if (resultList.isEmpty()) {
            return "Incorrect Case ID.";
        } else {
            EnquiryCase ec = (EnquiryCase) resultList.get(0);
            ec.setCaseReply(caseReply);
            ec.setCaseStatus("Solved");

            entityManager.flush();
            return "Reply Sent Successful";
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

}
