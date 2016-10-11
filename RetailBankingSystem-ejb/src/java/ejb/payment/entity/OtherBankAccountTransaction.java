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
public class OtherBankAccountTransaction implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long otherTransactionId;
    private String otherTransactionDate;
    private String otherTransactionCode;
    private String otherTransactionRef;
    private String otherAccountDebit;
    private String otherAccountCredit;
    
    @ManyToOne(cascade={CascadeType.PERSIST},fetch=FetchType.EAGER)
    private OtherBankAccount otherBankAccount;

    public Long getOtherTransactionId() {
        return otherTransactionId;
    }

    public void setOtherTransactionId(Long otherTransactionId) {
        this.otherTransactionId = otherTransactionId;
    }

    public String getOtherTransactionDate() {
        return otherTransactionDate;
    }

    public void setOtherTransactionDate(String otherTransactionDate) {
        this.otherTransactionDate = otherTransactionDate;
    }

    public String getOtherTransactionCode() {
        return otherTransactionCode;
    }

    public void setOtherTransactionCode(String otherTransactionCode) {
        this.otherTransactionCode = otherTransactionCode;
    }

    public String getOtherTransactionRef() {
        return otherTransactionRef;
    }

    public void setOtherTransactionRef(String otherTransactionRef) {
        this.otherTransactionRef = otherTransactionRef;
    }

    public String getOtherAccountDebit() {
        return otherAccountDebit;
    }

    public void setOtherAccountDebit(String otherAccountDebit) {
        this.otherAccountDebit = otherAccountDebit;
    }

    public String getOtherAccountCredit() {
        return otherAccountCredit;
    }

    public void setOtherAccountCredit(String otherAccountCredit) {
        this.otherAccountCredit = otherAccountCredit;
    }

    public OtherBankAccount getOtherBankAccount() {
        return otherBankAccount;
    }

    public void setOtherBankAccount(OtherBankAccount otherBankAccount) {
        this.otherBankAccount = otherBankAccount;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (otherTransactionId != null ? otherTransactionId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OtherBankAccountTransaction)) {
            return false;
        }
        OtherBankAccountTransaction other = (OtherBankAccountTransaction) object;
        if ((this.otherTransactionId == null && other.otherTransactionId != null) || (this.otherTransactionId != null && !this.otherTransactionId.equals(other.otherTransactionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ejb.payment.entity.DBSTransaction[ id=" + otherTransactionId + " ]";
    }
    
}
