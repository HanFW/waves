package ejb.bi.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class AccountClosureReason implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountClosureReasonId;
    private String rateValue;
    private String rateName;
    private Integer updateMonth;
    private Integer updateYear;
    private String accountClosureReasonStatus;
    private String currentYear;

    public Long getAccountClosureReasonId() {
        return accountClosureReasonId;
    }

    public void setAccountClosureReasonId(Long accountClosureReasonId) {
        this.accountClosureReasonId = accountClosureReasonId;
    }

    public String getRateValue() {
        return rateValue;
    }

    public void setRateValue(String rateValue) {
        this.rateValue = rateValue;
    }

    public String getRateName() {
        return rateName;
    }

    public void setRateName(String rateName) {
        this.rateName = rateName;
    }

    public Integer getUpdateMonth() {
        return updateMonth;
    }

    public void setUpdateMonth(Integer updateMonth) {
        this.updateMonth = updateMonth;
    }

    public Integer getUpdateYear() {
        return updateYear;
    }

    public void setUpdateYear(Integer updateYear) {
        this.updateYear = updateYear;
    }

    public String getAccountClosureReasonStatus() {
        return accountClosureReasonStatus;
    }

    public void setAccountClosureReasonStatus(String accountClosureReasonStatus) {
        this.accountClosureReasonStatus = accountClosureReasonStatus;
    }

    public String getCurrentYear() {
        return currentYear;
    }

    public void setCurrentYear(String currentYear) {
        this.currentYear = currentYear;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (accountClosureReasonId != null ? accountClosureReasonId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AccountClosureReason)) {
            return false;
        }
        AccountClosureReason other = (AccountClosureReason) object;
        if ((this.accountClosureReasonId == null && other.accountClosureReasonId != null) || (this.accountClosureReasonId != null && !this.accountClosureReasonId.equals(other.accountClosureReasonId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ejb.bi.entity.AccountClosureReason[ id=" + accountClosureReasonId + " ]";
    }
    
}
