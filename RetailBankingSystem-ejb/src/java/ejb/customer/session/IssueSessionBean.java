package ejb.customer.session;

import ejb.customer.entity.EnquiryCase;
import ejb.customer.entity.Issue;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class IssueSessionBean implements IssueSessionBeanLocal {

    @EJB
    private FollowUpSessionBeanLocal followUpSessionBeanLocal;

    @EJB
    private EnquirySessionBeanLocal enquirySessionBeanLocal;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Issue addNewIssue(String departmentTo, String issueProblem, String createdTime, String issueStatus, Long caseId) {
        Issue issue = new Issue();

        issue.setDepartmentTo(departmentTo);
        issue.setIssueProblem(issueProblem);
        issue.setCreatedTime(createdTime);
        issue.setIssueStatus(issueStatus);
//        EnquiryCase enquiryCase = entityManager.find(EnquiryCase.class, caseId);
//        issue.setEnquiryCase(enquiryCase);
        issue.setEnquiryCase(enquirySessionBeanLocal.retrieveEnquiryById(caseId));
//        issue.setFollowUp(followUpSessionBeanLocal.retrieveEnquiryById(followUpId));

        entityManager.persist(issue);
        entityManager.flush();
        return issue;
    }

    @Override
    public String deleteIssue(Long issueId) {
        Issue issue = retrieveIssueByID(issueId);

        entityManager.remove(issue);
        entityManager.flush();

        return "Successfully deleted!";
    }

    @Override
    public Issue retrieveIssueByID(Long issueId) {
        Issue issue = new Issue();

        try {
            Query query = entityManager.createQuery("Select i From Issue i Where i.issueId:issueId");
            query.setParameter("issueId", issueId);

            if (query.getResultList().isEmpty()) {
                return new Issue();
            } else {
                issue = (Issue) query.getResultList().get(0);
            }
        } catch (EntityNotFoundException enfe) {
            System.out.println("\nEntity not found error: " + enfe.getMessage());
            return new Issue();
        } catch (NonUniqueResultException nure) {
            System.out.println("\nNon unique result error: " + nure.getMessage());
        }

        return issue;
    }
}
