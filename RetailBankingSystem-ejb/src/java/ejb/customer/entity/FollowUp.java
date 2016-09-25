/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.customer.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author aaa
 */
@Entity
public class FollowUp implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long followUpId;
    private String followUpDetail;
    private String followUpSolution;
    private String followUpStatus;
    private String sendTime;

//    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER, mappedBy = "followUp")
//    private List<Issue> issue;

    @ManyToOne(cascade = {CascadeType.PERSIST}, fetch = FetchType.EAGER)
    private EnquiryCase enquiryCase;

    public Long getFollowUpId() {
        return followUpId;
    }

    public void setFollowUpId(Long followUpId) {
        this.followUpId = followUpId;
    }

    public String getFollowUpDetail() {
        return followUpDetail;
    }

    public void setFollowUpDetail(String followUpDetail) {
        this.followUpDetail = followUpDetail;
    }

    public String getFollowUpSolution() {
        return followUpSolution;
    }

    public void setFollowUpSolution(String followUpSolution) {
        this.followUpSolution = followUpSolution;
    }

    public String getFollowUpStatus() {
        return followUpStatus;
    }

    public void setFollowUpStatus(String followUpStatus) {
        this.followUpStatus = followUpStatus;
    }

//    public List<Issue> getIssue() {
//        return issue;
//    }
//
//    public void setIssue(List<Issue> issue) {
//        this.issue = issue;
//    }

    public EnquiryCase getEnquiryCase() {
        return enquiryCase;
    }

    public void setEnquiryCase(EnquiryCase enquiryCase) {
        this.enquiryCase = enquiryCase;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

//    public void addNewIssue(Issue newIssue) {
//        issue.add(newIssue);
//    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (followUpId != null ? followUpId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FollowUp)) {
            return false;
        }
        FollowUp other = (FollowUp) object;
        if ((this.followUpId == null && other.followUpId != null) || (this.followUpId != null && !this.followUpId.equals(other.followUpId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.FollowUp[ id=" + followUpId + " ]";
    }

}
