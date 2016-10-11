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
    private Double otherBankTotalCredit;
    private Double merlionTotalCredit;
    private String updateDate;
    private String bankNames;
    private String fastTransferRef;

    public Long getSachId() {
        return sachId;
    }

    public void setSachId(Long sachId) {
        this.sachId = sachId;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getBankNames() {
        return bankNames;
    }

    public void setBankNames(String bankNames) {
        this.bankNames = bankNames;
    }

    public Double getMerlionTotalCredit() {
        return merlionTotalCredit;
    }

    public void setMerlionTotalCredit(Double merlionTotalCredit) {
        this.merlionTotalCredit = merlionTotalCredit;
    }

    public Double getOtherBankTotalCredit() {
        return otherBankTotalCredit;
    }

    public void setOtherBankTotalCredit(Double otherBankTotalCredit) {
        this.otherBankTotalCredit = otherBankTotalCredit;
    }

    public String getFastTransferRef() {
        return fastTransferRef;
    }

    public void setFastTransferRef(String fastTransferRef) {
        this.fastTransferRef = fastTransferRef;
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
