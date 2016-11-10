package ejb.bi.entity;

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
public class CustomerCLV implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerCLVId;
    private String loanInterest;
    private String depositInterest;
    private Integer updateYear;
    private Integer updateMonth;
    private String clvSegment;
    private String clvValue;

    @ManyToOne(cascade = {CascadeType.PERSIST}, fetch = FetchType.EAGER)
    private CustomerBasic customerBasic;

    public Long getCustomerCLVId() {
        return customerCLVId;
    }

    public void setCustomerCLVId(Long customerCLVId) {
        this.customerCLVId = customerCLVId;
    }

    public String getLoanInterest() {
        return loanInterest;
    }

    public void setLoanInterest(String loanInterest) {
        this.loanInterest = loanInterest;
    }

    public String getDepositInterest() {
        return depositInterest;
    }

    public void setDepositInterest(String depositInterest) {
        this.depositInterest = depositInterest;
    }

    public Integer getUpdateYear() {
        return updateYear;
    }

    public void setUpdateYear(Integer updateYear) {
        this.updateYear = updateYear;
    }

    public Integer getUpdateMonth() {
        return updateMonth;
    }

    public void setUpdateMonth(Integer updateMonth) {
        this.updateMonth = updateMonth;
    }

    public String getClvSegment() {
        return clvSegment;
    }

    public void setClvSegment(String clvSegment) {
        this.clvSegment = clvSegment;
    }

    public String getClvValue() {
        return clvValue;
    }

    public void setClvValue(String clvValue) {
        this.clvValue = clvValue;
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
        hash += (customerCLVId != null ? customerCLVId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CustomerCLV)) {
            return false;
        }
        CustomerCLV other = (CustomerCLV) object;
        if ((this.customerCLVId == null && other.customerCLVId != null) || (this.customerCLVId != null && !this.customerCLVId.equals(other.customerCLVId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ejb.bi.entity.CustomerCLV[ id=" + customerCLVId + " ]";
    }

}
