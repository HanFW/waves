/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.customer.session;

import ejb.customer.entity.Issue;
import javax.ejb.Local;

/**
 *
 * @author aaa
 */
@Local
public interface IssueSessionBeanLocal {
    public Issue addNewIssue(String departmentTo,String issueProblem,String createdTime,String issueStatus,
            Long caseId);
    
    public String deleteIssue(Long issueId);
    
    public Issue retrieveIssueByID(Long issueId);
}
