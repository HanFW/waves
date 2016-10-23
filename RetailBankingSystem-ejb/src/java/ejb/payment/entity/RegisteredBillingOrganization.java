package ejb.payment.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class RegisteredBillingOrganization implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long billingOrganizationId;
    private String billingOrganizationName;
    private String bankAccountNum;
    private String bankAccountType;
    private String bankName;

    public Long getBillingOrganizationId() {
        return billingOrganizationId;
    }

    public void setBillingOrganizationId(Long billingOrganizationId) {
        this.billingOrganizationId = billingOrganizationId;
    }

    public String getBillingOrganizationName() {
        return billingOrganizationName;
    }

    public void setBillingOrganizationName(String billingOrganizationName) {
        this.billingOrganizationName = billingOrganizationName;
    }

    public String getBankAccountNum() {
        return bankAccountNum;
    }

    public void setBankAccountNum(String bankAccountNum) {
        this.bankAccountNum = bankAccountNum;
    }

    public String getBankAccountType() {
        return bankAccountType;
    }

    public void setBankAccountType(String bankAccountType) {
        this.bankAccountType = bankAccountType;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (billingOrganizationId != null ? billingOrganizationId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RegisteredBillingOrganization)) {
            return false;
        }
        RegisteredBillingOrganization other = (RegisteredBillingOrganization) object;
        if ((this.billingOrganizationId == null && other.billingOrganizationId != null) || (this.billingOrganizationId != null && !this.billingOrganizationId.equals(other.billingOrganizationId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ejb.payment.entity.BillingOrganization[ id=" + billingOrganizationId + " ]";
    }
    
}
