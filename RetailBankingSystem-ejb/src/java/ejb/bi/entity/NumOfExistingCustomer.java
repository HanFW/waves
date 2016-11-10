package ejb.bi.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class NumOfExistingCustomer implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long numOfExistingCustomerId;
    private String numOfExistingCustomer;
    private Integer updateMonth;
    private String numOfOpeningAccounts;
    private String numOfClosingAccounts;
    private String numOfClosingInactiveAccounts;
    private Integer updateYear;
    private String status;
    private String currentYear;

    public Long getNumOfExistingCustomerId() {
        return numOfExistingCustomerId;
    }

    public void setNumOfExistingCustomerId(Long numOfExistingCustomerId) {
        this.numOfExistingCustomerId = numOfExistingCustomerId;
    }

    public String getNumOfExistingCustomer() {
        return numOfExistingCustomer;
    }

    public void setNumOfExistingCustomer(String numOfExistingCustomer) {
        this.numOfExistingCustomer = numOfExistingCustomer;
    }

    public String getNumOfOpeningAccounts() {
        return numOfOpeningAccounts;
    }

    public void setNumOfOpeningAccounts(String numOfOpeningAccounts) {
        this.numOfOpeningAccounts = numOfOpeningAccounts;
    }

    public String getNumOfClosingAccounts() {
        return numOfClosingAccounts;
    }

    public void setNumOfClosingAccounts(String numOfClosingAccounts) {
        this.numOfClosingAccounts = numOfClosingAccounts;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCurrentYear() {
        return currentYear;
    }

    public void setCurrentYear(String currentYear) {
        this.currentYear = currentYear;
    }

    public String getNumOfClosingInactiveAccounts() {
        return numOfClosingInactiveAccounts;
    }

    public void setNumOfClosingInactiveAccounts(String numOfClosingInactiveAccounts) {
        this.numOfClosingInactiveAccounts = numOfClosingInactiveAccounts;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (numOfExistingCustomerId != null ? numOfExistingCustomerId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NumOfExistingCustomer)) {
            return false;
        }
        NumOfExistingCustomer other = (NumOfExistingCustomer) object;
        if ((this.numOfExistingCustomerId == null && other.numOfExistingCustomerId != null) || (this.numOfExistingCustomerId != null && !this.numOfExistingCustomerId.equals(other.numOfExistingCustomerId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ejb.bi.entity.NumOfExistingCustomer[ id=" + numOfExistingCustomerId + " ]";
    }

}
