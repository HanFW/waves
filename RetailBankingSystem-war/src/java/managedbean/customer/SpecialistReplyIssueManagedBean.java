/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.customer;

import ejb.customer.entity.Issue;
import ejb.customer.session.EnquirySessionBeanLocal;
import ejb.infrastructure.entity.Employee;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;

/**
 *
 * @author aaa
 */
@Named(value = "specialistReplyIssueManagedBean")
@SessionScoped
public class SpecialistReplyIssueManagedBean implements Serializable {

    @EJB
    private EnquirySessionBeanLocal enquirySessionBeanLocal;

    private Long issueId;
    private String departmentTo;
    private String issueProblem;
    private String issueSolution;
    private String issueStatus;

    private Issue issue = new Issue();
    private Employee employee = new Employee();

    public SpecialistReplyIssueManagedBean() {
    }

    public Long getIssueId() {
        return issueId;
    }

    public void setIssueId(Long issueId) {
        this.issueId = issueId;
    }

    public String getDepartmentTo() {
        return departmentTo;
    }

    public void setDepartmentTo(String departmentTo) {
        this.departmentTo = departmentTo;
    }

    public String getIssueProblem() {
        return issueProblem;
    }

    public void setIssueProblem(String issueProblem) {
        this.issueProblem = issueProblem;
    }

    public String getIssueSolution() {
        return issueSolution;
    }

    public void setIssueSolution(String issueSolution) {
        this.issueSolution = issueSolution;
    }

    public String getIssueStatus() {
        return issueStatus;
    }

    public void setIssueStatus(String issueStatus) {
        this.issueStatus = issueStatus;
    }

    public Issue getIssue() {
        return issue;
    }

    public void setIssue(Issue issue) {
        this.issue = issue;
    }

    public List<Issue> getLoanPendingIssue() {
        return enquirySessionBeanLocal.getAllPendingLoanIssue();
    }

    public List<Issue> getCardPendingIssue() {
        return enquirySessionBeanLocal.getAllPendingCardIssue();
    }

    public List<Issue> getDepositPendingIssue() {
        return enquirySessionBeanLocal.getAllPendingDepositIssue();
    }

    public List<Issue> getOperationPendingIssue() {
        return enquirySessionBeanLocal.getAllPendingOperationIssue();
    }

    public List<Issue> getRMPendingIssue() {
        return enquirySessionBeanLocal.getAllPendingRMIssue();
    }

    public void redirectToReplyPage() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext ec = context.getExternalContext();
        ec.redirect(ec.getRequestContextPath() + "/web/internalSystem/enquiry/specialistsReplyIssue.xhtml?faces-redirect=true");
    }

    public String getIssueDetailById() {
        return enquirySessionBeanLocal.getIssueDetail(issueId);
    }

    public void departmentReplyIssue() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext ec = context.getExternalContext();
        context.addMessage(null, new FacesMessage(enquirySessionBeanLocal.replyIssue(issueId, issueSolution), " "));
        issueId = null;
        departmentTo = null;
        issueProblem = null;
        issueSolution = null;
        issueStatus = null;
        ec.redirect(ec.getRequestContextPath() + "/web/internalSystem/enquiry/issueReplyDone.xhtml?faces-redirect=true");
    }

}
