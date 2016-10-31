package ejb.bi.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Rate implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rateId;
    private Double rateValue;
    private int updateDate;
    private String rateType;
    private String openAccountNumOfPeople;
    private String closeAccountNumOfPeople;

    public Long getRateId() {
        return rateId;
    }

    public void setRateId(Long rateId) {
        this.rateId = rateId;
    }

    public Double getRateValue() {
        return rateValue;
    }

    public void setRateValue(Double rateValue) {
        this.rateValue = rateValue;
    }

    public int getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(int updateDate) {
        this.updateDate = updateDate;
    }

    public String getRateType() {
        return rateType;
    }

    public void setRateType(String rateType) {
        this.rateType = rateType;
    }

    public String getOpenAccountNumOfPeople() {
        return openAccountNumOfPeople;
    }

    public void setOpenAccountNumOfPeople(String openAccountNumOfPeople) {
        this.openAccountNumOfPeople = openAccountNumOfPeople;
    }

    public String getCloseAccountNumOfPeople() {
        return closeAccountNumOfPeople;
    }

    public void setCloseAccountNumOfPeople(String closeAccountNumOfPeople) {
        this.closeAccountNumOfPeople = closeAccountNumOfPeople;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rateId != null ? rateId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Rate)) {
            return false;
        }
        Rate other = (Rate) object;
        if ((this.rateId == null && other.rateId != null) || (this.rateId != null && !this.rateId.equals(other.rateId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ejb.bi.entity.Rate[ id=" + rateId + " ]";
    }
    
}
