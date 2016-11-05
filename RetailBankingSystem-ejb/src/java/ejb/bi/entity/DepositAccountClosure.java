package ejb.bi.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class DepositAccountClosure implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long depositAccountClosureId;
    private String accountClosureReason;
    private Long currentTimeMilis;
    private String currentTime;

    public Long getDepositAccountClosureId() {
        return depositAccountClosureId;
    }

    public void setDepositAccountClosureId(Long depositAccountClosureId) {
        this.depositAccountClosureId = depositAccountClosureId;
    }

    public String getAccountClosureReason() {
        return accountClosureReason;
    }

    public void setAccountClosureReason(String accountClosureReason) {
        this.accountClosureReason = accountClosureReason;
    }

    public Long getCurrentTimeMilis() {
        return currentTimeMilis;
    }

    public void setCurrentTimeMilis(Long currentTimeMilis) {
        this.currentTimeMilis = currentTimeMilis;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (depositAccountClosureId != null ? depositAccountClosureId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DepositAccountClosure)) {
            return false;
        }
        DepositAccountClosure other = (DepositAccountClosure) object;
        if ((this.depositAccountClosureId == null && other.depositAccountClosureId != null) || (this.depositAccountClosureId != null && !this.depositAccountClosureId.equals(other.depositAccountClosureId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ejb.bi.entity.DepositAccountClosure[ id=" + depositAccountClosureId + " ]";
    }

}
