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
public class SWIFTPayee implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long swiftPayeeId;
    private String swiftInstitution;
    private String swiftPayeeAccountNum;
    private String swiftPayeeAccountType;
    private String swiftPayeeCode;
    private String lastTransactionDate;
    private String swiftPayeeCountry;
    
    @ManyToOne(cascade = {CascadeType.PERSIST}, fetch = FetchType.EAGER)
    private CustomerBasic customerBasic;

    public Long getSwiftPayeeId() {
        return swiftPayeeId;
    }

    public void setSwiftPayeeId(Long swiftPayeeId) {
        this.swiftPayeeId = swiftPayeeId;
    }

    public String getSwiftInstitution() {
        return swiftInstitution;
    }

    public void setSwiftInstitution(String swiftInstitution) {
        this.swiftInstitution = swiftInstitution;
    }

    public String getSwiftPayeeAccountNum() {
        return swiftPayeeAccountNum;
    }

    public void setSwiftPayeeAccountNum(String swiftPayeeAccountNum) {
        this.swiftPayeeAccountNum = swiftPayeeAccountNum;
    }

    public String getSwiftPayeeAccountType() {
        return swiftPayeeAccountType;
    }

    public void setSwiftPayeeAccountType(String swiftPayeeAccountType) {
        this.swiftPayeeAccountType = swiftPayeeAccountType;
    }

    public String getSwiftPayeeCode() {
        return swiftPayeeCode;
    }

    public void setSwiftPayeeCode(String swiftPayeeCode) {
        this.swiftPayeeCode = swiftPayeeCode;
    }

    public String getLastTransactionDate() {
        return lastTransactionDate;
    }

    public void setLastTransactionDate(String lastTransactionDate) {
        this.lastTransactionDate = lastTransactionDate;
    }

    public CustomerBasic getCustomerBasic() {
        return customerBasic;
    }

    public void setCustomerBasic(CustomerBasic customerBasic) {
        this.customerBasic = customerBasic;
    }

    public String getSwiftPayeeCountry() {
        return swiftPayeeCountry;
    }

    public void setSwiftPayeeCountry(String swiftPayeeCountry) {
        this.swiftPayeeCountry = swiftPayeeCountry;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (swiftPayeeId != null ? swiftPayeeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SWIFTPayee)) {
            return false;
        }
        SWIFTPayee other = (SWIFTPayee) object;
        if ((this.swiftPayeeId == null && other.swiftPayeeId != null) || (this.swiftPayeeId != null && !this.swiftPayeeId.equals(other.swiftPayeeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ejb.payment.entity.SWIFTPayee[ id=" + swiftPayeeId + " ]";
    }

}
