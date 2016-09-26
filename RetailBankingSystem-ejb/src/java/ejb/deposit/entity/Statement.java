package ejb.deposit.entity;

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
public class Statement implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long statementId;
    private String statementDate;
    private String statementType;
    private String accountDetails;
    private Long startTime;
    private Long endTime;
    
    @ManyToOne(cascade={CascadeType.PERSIST},fetch=FetchType.EAGER)
    private BankAccount bankAccount;

    public Long getStatementId() {
        return statementId;
    }

    public void setStatementId(Long statementId) {
        this.statementId = statementId;
    }

    public String getStatementDate() {
        return statementDate;
    }

    public void setStatementDate(String statementDate) {
        this.statementDate = statementDate;
    }

    public String getStatementType() {
        return statementType;
    }

    public void setStatementType(String statementType) {
        this.statementType = statementType;
    }

    public String getAccountDetails() {
        return accountDetails;
    }

    public void setAccountDetails(String accountDetails) {
        this.accountDetails = accountDetails;
    }

    public BankAccount getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (statementId != null ? statementId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Statement)) {
            return false;
        }
        Statement other = (Statement) object;
        if ((this.statementId == null && other.statementId != null) || (this.statementId != null && !this.statementId.equals(other.statementId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Statement[ id=" + statementId + " ]";
    }
    
}
