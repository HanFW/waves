/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.customer.entity;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author aaa
 */
@Entity
public class Issue implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long issueId;
    private String departmentTo;
    private String issueProblem;
    private String issueSolution;
    private String issueStatus;
    private String createdTime;
    
    @ManyToOne(cascade={CascadeType.ALL},fetch=FetchType.EAGER)
    private EnquiryCase enquiryCase;
    
//    @ManyToOne(cascade={CascadeType.ALL},fetch=FetchType.EAGER)
//    private FollowUp followUp;

    
    public EnquiryCase getEnquiryCase() {
        return enquiryCase;
    }

    public void setEnquiryCase(EnquiryCase enquiryCase) {
        this.enquiryCase = enquiryCase;
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

//    public FollowUp getFollowUp() {
//        return followUp;
//    }
//
//    public void setFollowUp(FollowUp followUp) {
//        this.followUp = followUp;
//    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (issueId != null ? issueId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Issue)) {
            return false;
        }
        Issue other = (Issue) object;
        if ((this.issueId == null && other.issueId != null) || (this.issueId != null && !this.issueId.equals(other.issueId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Issue[ id=" + issueId + " ]";
    }
    
}
