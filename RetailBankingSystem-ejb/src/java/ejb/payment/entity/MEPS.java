package ejb.payment.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class MEPS implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mepsId;
    private String settlementRef;
    private String settlementDate;
    private String bankNames;

    public Long getMepsId() {
        return mepsId;
    }

    public void setMepsId(Long mepsId) {
        this.mepsId = mepsId;
    }

    public String getSettlementRef() {
        return settlementRef;
    }

    public void setSettlementRef(String settlementRef) {
        this.settlementRef = settlementRef;
    }

    public String getSettlementDate() {
        return settlementDate;
    }

    public void setSettlementDate(String settlementDate) {
        this.settlementDate = settlementDate;
    }

    public String getBankNames() {
        return bankNames;
    }

    public void setBankNames(String bankNames) {
        this.bankNames = bankNames;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mepsId != null ? mepsId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MEPS)) {
            return false;
        }
        MEPS other = (MEPS) object;
        if ((this.mepsId == null && other.mepsId != null) || (this.mepsId != null && !this.mepsId.equals(other.mepsId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ejb.payment.entity.MEPS[ id=" + mepsId + " ]";
    }
    
}
