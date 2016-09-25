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
public class EnquiryCase implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long caseId;
    private String caseType;
    private String caseDetail;
    private String caseReply;
    private String caseStatus;
//    private String onlineBankingAccountNum;
    private String createdTime;

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER, mappedBy = "enquiryCase")
    private List<Issue> issue;

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER, mappedBy = "enquiryCase")
    private List<FollowUp> followUp;

    @ManyToOne(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    private CustomerBasic customerBasic;

    public Long getCaseId() {
        return caseId;
    }

    public void setCaseId(Long caseId) {
        this.caseId = caseId;
    }

    public String getCaseType() {
        return caseType;
    }

    public void setCaseType(String caseType) {
        this.caseType = caseType;
    }

    public String getCaseDetail() {
        return caseDetail;
    }

    public void setCaseDetail(String caseDetail) {
        this.caseDetail = caseDetail;
    }

    public String getCaseStatus() {
        return caseStatus;
    }

    public void setCaseStatus(String caseStatus) {
        this.caseStatus = caseStatus;
    }

    public List<Issue> getIssue() {
        return issue;
    }

    public void setIssue(List<Issue> issue) {
        this.issue = issue;
    }

    public List<FollowUp> getFollowUp() {
        return followUp;
    }

    public void setFollowUp(List<FollowUp> followUp) {
        this.followUp = followUp;
    }

    public CustomerBasic getCustomerBasic() {
        return customerBasic;
    }

    public void setCustomerBasic(CustomerBasic customerBasic) {
        this.customerBasic = customerBasic;
    }

//    public String getOnlineBankingAccountNum() {
//        return onlineBankingAccountNum;
//    }
//
//    public void setOnlineBankingAccountNum(String onlineBankingAccountNum) {
//        this.onlineBankingAccountNum = onlineBankingAccountNum;
//    }

    public String getCaseReply() {
        return caseReply;
    }

    public void setCaseReply(String caseReply) {
        this.caseReply = caseReply;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public void addNewIssue(Issue newIssue) {
        issue.add(newIssue);
    }

    public void addNewFollowUp(FollowUp newFollowUp) {
        followUp.add(newFollowUp);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (caseId != null ? caseId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EnquiryCase)) {
            return false;
        }
        EnquiryCase other = (EnquiryCase) object;
        if ((this.caseId == null && other.caseId != null) || (this.caseId != null && !this.caseId.equals(other.caseId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "EC" + caseId;
    }

}
