package ejb.payment.entity;

import ejb.customer.entity.CustomerBasic;
import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class GIRO implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long giroId;
    private String billingOrganizationName;
    private String billReference;
    private String bankAccountNum;
    private String bankAccountNumWithType;
    private String giroType;

    @ManyToOne(cascade = {CascadeType.PERSIST}, fetch = FetchType.EAGER)
    private CustomerBasic customerBasic;
    
    public Long getGiroId() {
        return giroId;
    }

    public void setGiroId(Long giroId) {
        this.giroId = giroId;
    }

    public String getBillingOrganizationName() {
        return billingOrganizationName;
    }

    public void setBillingOrganizationName(String billingOrganizationName) {
        this.billingOrganizationName = billingOrganizationName;
    }

    public String getBillReference() {
        return billReference;
    }

    public void setBillReference(String billReference) {
        this.billReference = billReference;
    }

    public String getBankAccountNum() {
        return bankAccountNum;
    }

    public void setBankAccountNum(String bankAccountNum) {
        this.bankAccountNum = bankAccountNum;
    }

    public String getBankAccountNumWithType() {
        return bankAccountNumWithType;
    }

    public void setBankAccountNumWithType(String bankAccountNumWithType) {
        this.bankAccountNumWithType = bankAccountNumWithType;
    }

    public String getGiroType() {
        return giroType;
    }

    public void setGiroType(String giroType) {
        this.giroType = giroType;
    }

    public CustomerBasic getCustomerBasic() {
        return customerBasic;
    }

    public void setCustomerBasic(CustomerBasic customerBasic) {
        this.customerBasic = customerBasic;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (giroId != null ? giroId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GIRO)) {
            return false;
        }
        GIRO other = (GIRO) object;
        if ((this.giroId == null && other.giroId != null) || (this.giroId != null && !this.giroId.equals(other.giroId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ejb.payment.entity.GIRO[ id=" + giroId + " ]";
    }

}
