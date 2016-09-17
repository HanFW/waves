/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session.stateless;

import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import entity.EnquiryCase;
import java.util.ArrayList;
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
    public String addNewCase(String onlineBankingAccountNum, String type, String detail) {

        EnquiryCase enquiryCase = new EnquiryCase();

        enquiryCase.setCaseDetail(detail);
        enquiryCase.setCaseType(type);
        enquiryCase.setOnlineBankingAccountNum(onlineBankingAccountNum);
        enquiryCase.setCaseStatus("Pending");
        enquiryCase.setCaseFollowUp(null);

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
        List<String> followUps = new ArrayList<>();

        if (resultList.isEmpty()) {
            return "Incorrect Case ID";
        } else {
            EnquiryCase ec = (EnquiryCase) resultList.get(0);
            followUps.add(caseFollowUp);
            ec.setCaseFollowUp(followUps);

            entityManager.flush();
            return "Sent Successful";
        }
    }

}

