package ejb.deposit.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Verify implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long verifyId;
    private String customerName;
    private String customerIdentificationNum;
    private String customerIdentificationType;
    private String identification;

    public Long getVerifyId() {
        return verifyId;
    }

    public void setVerifyId(Long verifyId) {
        this.verifyId = verifyId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerIdentificationNum() {
        return customerIdentificationNum;
    }

    public void setCustomerIdentificationNum(String customerIdentificationNum) {
        this.customerIdentificationNum = customerIdentificationNum;
    }

    public String getCustomerIdentificationType() {
        return customerIdentificationType;
    }

    public void setCustomerIdentificationType(String customerIdentificationType) {
        this.customerIdentificationType = customerIdentificationType;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (verifyId != null ? verifyId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Verify)) {
            return false;
        }
        Verify other = (Verify) object;
        if ((this.verifyId == null && other.verifyId != null) || (this.verifyId != null && !this.verifyId.equals(other.verifyId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ejb.deposit.entity.Verify[ id=" + verifyId + " ]";
    }
    
}
