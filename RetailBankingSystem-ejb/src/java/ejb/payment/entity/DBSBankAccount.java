package ejb.payment.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class DBSBankAccount implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dbsBankAccountId;
    private String dbsBankAccountNum;
    private String dbsBankAccountType;
    private String dbsBankAccountBalance;
    
    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER, mappedBy = "dbsBankAccount")
    private List<DBSTransaction> dbsTransaction;

    public Long getDbsBankAccountId() {
        return dbsBankAccountId;
    }

    public void setDbsBankAccountId(Long dbsBankAccountId) {
        this.dbsBankAccountId = dbsBankAccountId;
    }

    public String getDbsBankAccountNum() {
        return dbsBankAccountNum;
    }

    public void setDbsBankAccountNum(String dbsBankAccountNum) {
        this.dbsBankAccountNum = dbsBankAccountNum;
    }

    public String getDbsBankAccountType() {
        return dbsBankAccountType;
    }

    public void setDbsBankAccountType(String dbsBankAccountType) {
        this.dbsBankAccountType = dbsBankAccountType;
    }

    public String getDbsBankAccountBalance() {
        return dbsBankAccountBalance;
    }

    public void setDbsBankAccountBalance(String dbsBankAccountBalance) {
        this.dbsBankAccountBalance = dbsBankAccountBalance;
    }

    public List<DBSTransaction> getDbsTransaction() {
        return dbsTransaction;
    }

    public void setDbsTransaction(List<DBSTransaction> dbsTransaction) {
        this.dbsTransaction = dbsTransaction;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (dbsBankAccountId != null ? dbsBankAccountId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DBSBankAccount)) {
            return false;
        }
        DBSBankAccount other = (DBSBankAccount) object;
        if ((this.dbsBankAccountId == null && other.dbsBankAccountId != null) || (this.dbsBankAccountId != null && !this.dbsBankAccountId.equals(other.dbsBankAccountId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ejb.payment.entity.DBSBankAccount[ id=" + dbsBankAccountId + " ]";
    }
    
}
