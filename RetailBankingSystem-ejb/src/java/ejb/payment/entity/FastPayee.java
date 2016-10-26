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
public class FastPayee implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fastPayeeId;
    private String fastPayeeName;
    private String fastPayeeAccountNum;
    private String fastPayeeAccountType;
    private String lastTransactionDate;

    @ManyToOne(cascade = {CascadeType.PERSIST}, fetch = FetchType.EAGER)
    private CustomerBasic customerBasic;

    public Long getFastPayeeId() {
        return fastPayeeId;
    }

    public void setFastPayeeId(Long fastPayeeId) {
        this.fastPayeeId = fastPayeeId;
    }

    public String getFastPayeeName() {
        return fastPayeeName;
    }

    public void setFastPayeeName(String fastPayeeName) {
        this.fastPayeeName = fastPayeeName;
    }

    public String getFastPayeeAccountNum() {
        return fastPayeeAccountNum;
    }

    public void setFastPayeeAccountNum(String fastPayeeAccountNum) {
        this.fastPayeeAccountNum = fastPayeeAccountNum;
    }

    public String getFastPayeeAccountType() {
        return fastPayeeAccountType;
    }

    public void setFastPayeeAccountType(String fastPayeeAccountType) {
        this.fastPayeeAccountType = fastPayeeAccountType;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (fastPayeeId != null ? fastPayeeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FastPayee)) {
            return false;
        }
        FastPayee other = (FastPayee) object;
        if ((this.fastPayeeId == null && other.fastPayeeId != null) || (this.fastPayeeId != null && !this.fastPayeeId.equals(other.fastPayeeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ejb.payment.entity.FastPayee[ id=" + fastPayeeId + " ]";
    }

}
