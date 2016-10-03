package ejb.payment.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class SACH implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sachId;
    private String dbsAccountBalance;
    private String merlionAccountBalance;
    private String dbsTotalCredit;
    private String merlionTotalCredit;

    public Long getSachId() {
        return sachId;
    }

    public void setSachId(Long sachId) {
        this.sachId = sachId;
    }

    public String getDbsAccountBalance() {
        return dbsAccountBalance;
    }

    public void setDbsAccountBalance(String dbsAccountBalance) {
        this.dbsAccountBalance = dbsAccountBalance;
    }

    public String getMerlionAccountBalance() {
        return merlionAccountBalance;
    }

    public void setMerlionAccountBalance(String merlionAccountBalance) {
        this.merlionAccountBalance = merlionAccountBalance;
    }

    public String getDbsTotalCredit() {
        return dbsTotalCredit;
    }

    public void setDbsTotalCredit(String dbsTotalCredit) {
        this.dbsTotalCredit = dbsTotalCredit;
    }

    public String getMerlionTotalCredit() {
        return merlionTotalCredit;
    }

    public void setMerlionTotalCredit(String merlionTotalCredit) {
        this.merlionTotalCredit = merlionTotalCredit;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (sachId != null ? sachId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SACH)) {
            return false;
        }
        SACH other = (SACH) object;
        if ((this.sachId == null && other.sachId != null) || (this.sachId != null && !this.sachId.equals(other.sachId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ejb.payment.entity.SACH[ id=" + sachId + " ]";
    }
    
}
