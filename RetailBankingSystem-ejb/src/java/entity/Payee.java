package entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id; 
import javax.persistence.CascadeType;
import javax.persistence.FetchType;

import javax.persistence.ManyToOne;

@Entity
public class Payee implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long payeeId;
    private String payeeName;
    private String payeeAccountNum;
    private String payeeAccountType;
    private String lastTransactionDate;
    private Long customerBasicId;

    @ManyToOne(cascade={CascadeType.ALL},fetch=FetchType.EAGER)
    private CustomerBasic customerBasic;
    
    public Long getPayeeId() {
        return payeeId;
    }

    public void setPayeeId(Long payeeId) {
        this.payeeId = payeeId;
    }

    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }

    public String getPayeeAccountNum() {
        return payeeAccountNum;
    }

    public void setPayeeAccountNum(String payeeAccountNum) {
        this.payeeAccountNum = payeeAccountNum;
    }

    public String getPayeeAccountType() {
        return payeeAccountType;
    }

    public void setPayeeAccountType(String payeeAccountType) {
        this.payeeAccountType = payeeAccountType;
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

    public Long getCustomerBasicId() {
        return customerBasicId;
    }

    public void setCustomerBasicId(Long customerBasicId) {
        this.customerBasicId = customerBasicId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (payeeId != null ? payeeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Payee)) {
            return false;
        }
        Payee other = (Payee) object;
        if ((this.payeeId == null && other.payeeId != null) || (this.payeeId != null && !this.payeeId.equals(other.payeeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Payee[ id=" + payeeId + " ]";
    }
    
}
