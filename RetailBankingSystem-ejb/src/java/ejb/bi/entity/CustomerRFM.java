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
public class CustomerRFM implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerRFMId;
    private String rValue;
    private String fValue;
    private String mValue;
    private String customerName;
    private Integer updateMonth;
    private Integer updateYear;
    private Long startTimeMilis;
    private Long transactionDays;
    private Integer numOfTransactions;
    private Double totalSpends;

    @ManyToOne(cascade = {CascadeType.PERSIST}, fetch = FetchType.EAGER)
    private CustomerBasic customerBasic;

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Long getCustomerRFMId() {
        return customerRFMId;
    }

    public void setCustomerRFMId(Long customerRFMId) {
        this.customerRFMId = customerRFMId;
    }

    public String getrValue() {
        return rValue;
    }

    public void setrValue(String rValue) {
        this.rValue = rValue;
    }

    public String getfValue() {
        return fValue;
    }

    public void setfValue(String fValue) {
        this.fValue = fValue;
    }

    public String getmValue() {
        return mValue;
    }

    public void setmValue(String mValue) {
        this.mValue = mValue;
    }

    public Integer getUpdateMonth() {
        return updateMonth;
    }

    public void setUpdateMonth(Integer updateMonth) {
        this.updateMonth = updateMonth;
    }

    public Integer getUpdateYear() {
        return updateYear;
    }

    public void setUpdateYear(Integer updateYear) {
        this.updateYear = updateYear;
    }

    public CustomerBasic getCustomerBasic() {
        return customerBasic;
    }

    public void setCustomerBasic(CustomerBasic customerBasic) {
        this.customerBasic = customerBasic;
    }

    public Long getStartTimeMilis() {
        return startTimeMilis;
    }

    public void setStartTimeMilis(Long startTimeMilis) {
        this.startTimeMilis = startTimeMilis;
    }

    public Long getTransactionDays() {
        return transactionDays;
    }

    public void setTransactionDays(Long transactionDays) {
        this.transactionDays = transactionDays;
    }

    public Integer getNumOfTransactions() {
        return numOfTransactions;
    }

    public void setNumOfTransactions(Integer numOfTransactions) {
        this.numOfTransactions = numOfTransactions;
    }

    public Double getTotalSpends() {
        return totalSpends;
    }

    public void setTotalSpends(Double totalSpends) {
        this.totalSpends = totalSpends;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (customerRFMId != null ? customerRFMId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CustomerRFM)) {
            return false;
        }
        CustomerRFM other = (CustomerRFM) object;
        if ((this.customerRFMId == null && other.customerRFMId != null) || (this.customerRFMId != null && !this.customerRFMId.equals(other.customerRFMId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ejb.bi.entity.CustomerRFM[ id=" + customerRFMId + " ]";
    }

}
