package ejb.bi.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class DepositAccountOpen implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long depositAccountOpenId;
    private Long currentTimeMilis;
    private String currentTime;

    public Long getDepositAccountOpenId() {
        return depositAccountOpenId;
    }

    public void setDepositAccountOpenId(Long depositAccountOpenId) {
        this.depositAccountOpenId = depositAccountOpenId;
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
        hash += (depositAccountOpenId != null ? depositAccountOpenId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DepositAccountOpen)) {
            return false;
        }
        DepositAccountOpen other = (DepositAccountOpen) object;
        if ((this.depositAccountOpenId == null && other.depositAccountOpenId != null) || (this.depositAccountOpenId != null && !this.depositAccountOpenId.equals(other.depositAccountOpenId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ejb.bi.entity.DepositAccountOpen[ id=" + depositAccountOpenId + " ]";
    }

}
