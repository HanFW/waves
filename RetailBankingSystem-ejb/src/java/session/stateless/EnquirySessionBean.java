/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session.stateless;

import entity.CustomerBasic;
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
    public List<EnquiryCase> getCustomerEnquiry(String onlineBankingAccountNum) {
        Query query = entityManager.createQuery("SELECT ec FROM EnquiryCase ec WHERE ec.onlineBankingAccountNum = :onlineBankingAccountNum");
        query.setParameter("onlineBankingAccountNum", onlineBankingAccountNum);
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
    public String addNewCase(String onlineBankingAccountNum, String type, String detail) {

        EnquiryCase enquiryCase = new EnquiryCase();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");

        Calendar cal = Calendar.getInstance();
        String createdTime = sdf.format(cal.getTime());

        enquiryCase.setCaseDetail(detail);
        enquiryCase.setCaseType(type);
        enquiryCase.setOnlineBankingAccountNum(onlineBankingAccountNum);
        enquiryCase.setCreatedTime(createdTime);
        enquiryCase.setCaseStatus("Pending");
        enquiryCase.setCaseReply("Dear Customer, thank you for your enquiry. We will get back to you soon!");

        Query query = entityManager.createQuery("SELECT cb FROM CustomerBasic cb WHERE cb.customerOnlineBankingAccountNum = :onlineBankingAccountNum");
        query.setParameter("onlineBankingAccountNum", onlineBankingAccountNum);
        CustomerBasic customerBasic = (CustomerBasic) query.getSingleResult();
        enquiryCase.setCustomerBasic(customerBasic);
        customerBasic.addNewCase(enquiryCase);

        entityManager.persist(enquiryCase);
        entityManager.flush();

        return "Enquiry sent successfully";
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

}
