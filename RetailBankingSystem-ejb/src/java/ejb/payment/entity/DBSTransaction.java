package ejb.payment.entity;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class DBSTransaction implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dbsTransactionId;
    private String dbsTransactionDate;
    private String dbsTransactionCode;
    private String dbsTransactionRef;
    private String dbsAccountDebit;
    private String dbsAccountCredit;
    
    @ManyToOne(cascade={CascadeType.PERSIST},fetch=FetchType.EAGER)
    private DBSBankAccount dbsBankAccount;

    public Long getId() {
        return dbsTransactionId;
    }

    public void setId(Long id) {
        this.dbsTransactionId = id;
    }

    public Long getDbsTransactionId() {
        return dbsTransactionId;
    }

    public void setDbsTransactionId(Long dbsTransactionId) {
        this.dbsTransactionId = dbsTransactionId;
    }

    public String getDbsTransactionDate() {
        return dbsTransactionDate;
    }

    public void setDbsTransactionDate(String dbsTransactionDate) {
        this.dbsTransactionDate = dbsTransactionDate;
    }

    public String getDbsTransactionCode() {
        return dbsTransactionCode;
    }

    public void setDbsTransactionCode(String dbsTransactionCode) {
        this.dbsTransactionCode = dbsTransactionCode;
    }

    public String getDbsTransactionRef() {
        return dbsTransactionRef;
    }

    public void setDbsTransactionRef(String dbsTransactionRef) {
        this.dbsTransactionRef = dbsTransactionRef;
    }

    public String getDbsAccountDebit() {
        return dbsAccountDebit;
    }

    public void setDbsAccountDebit(String dbsAccountDebit) {
        this.dbsAccountDebit = dbsAccountDebit;
    }

    public String getDbsAccountCredit() {
        return dbsAccountCredit;
    }

    public void setDbsAccountCredit(String dbsAccountCredit) {
        this.dbsAccountCredit = dbsAccountCredit;
    }

    public DBSBankAccount getDbsBankAccount() {
        return dbsBankAccount;
    }

    public void setDbsBankAccount(DBSBankAccount dbsBankAccount) {
        this.dbsBankAccount = dbsBankAccount;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (dbsTransactionId != null ? dbsTransactionId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DBSTransaction)) {
            return false;
        }
        DBSTransaction other = (DBSTransaction) object;
        if ((this.dbsTransactionId == null && other.dbsTransactionId != null) || (this.dbsTransactionId != null && !this.dbsTransactionId.equals(other.dbsTransactionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ejb.payment.entity.DBSTransaction[ id=" + dbsTransactionId + " ]";
    }
    
}
